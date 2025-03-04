package com.swyp.mema.domain.store.service.naverAPI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import com.swyp.mema.domain.store.dto.naverAPI.ImageRes;
import com.swyp.mema.global.config.WebDriverConfig;
import com.swyp.mema.global.s3.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageCrawler {

	private final S3Service s3Service;
	private final WebDriverConfig webDriverConfig;

	public ImageRes crawaling(String url) {

		try {
			// 1️⃣ HTML 문서에서 이미지 URL 가져오기
			String imgUrl = fetchImageUrl(url);

			if (imgUrl == null) {
				log.info("No images found on the page.");
				return null;
			}

			// 2️⃣ 이미지 로드 및 S3 업로드
			String imageUrl = processAndUploadImage(imgUrl);
			return new ImageRes(imageUrl, 350, calculateHeight(350));

		} catch (Exception e) {
			log.error("Error in image crawling", e);
		}
		return null;
	}

	public String fetchImageUrl(String url) {

		try {

			Document doc = Jsoup.connect(url).get();

			// 1. 특정 도메인(예: 인스타그램)일 경우 별도 처리
			if (url.contains("instagram")) {
				log.info("Instagram detected, searching for post image: {}", url);
				return fetchInstagramImage(url);
			}

			// Jsoup으로 정적인 HTML에서 먼저 이미지 찾기
			String imageUrl = fetchImageUrlFromStaticHtml(doc);
			if (imageUrl != null) {
				return imageUrl;
			}

			// JavaScript 실행이 필요한 경우 Selenium 사용
			log.info("JavaScript 로딩 필요, Selenium 사용: {}", url);
			return fetchImageUrlUsingSelenium(url);

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Failed to fetch page: {}", url, e);
		}
		return null;
	}

	private String fetchImageUrlFromStaticHtml(Document doc) {

		Element metaImage = doc.selectFirst("meta[property=og:image]");

		if (metaImage != null) {
			return metaImage.attr("content");
		}

		Element imgElement = doc.selectFirst("img");
		if (imgElement != null) {
			return imgElement.absUrl("src");
		}
		return null;
	}

	public String fetchImageUrlUsingSelenium(String url) {

		WebDriver webDriver = webDriverConfig.createWebDriver();

		try {

			webDriver.get(url);
			Thread.sleep(5000); // JavaScript 실행 대기 (최소 3~5초 필요)

			// `meta[property=og:image]`에서 대표 이미지 가져오기
			List<WebElement> metaTags = webDriver.findElements(By.cssSelector("meta[property='og:image']"));
			if (!metaTags.isEmpty()) {
				return metaTags.get(0).getAttribute("content"); // 대표 이미지 반환
			}

			// `img` 태그에서 `src`, `data-src`, `srcset` 속성 확인
			List<WebElement> images = webDriver.findElements(By.tagName("img"));
			for (WebElement img : images) {
				String imageUrl = extractImageFromElement(img);
				if (imageUrl != null && !imageUrl.isEmpty()) {
					return imageUrl;
				}
			}

			// `div[style*="background-image"]` 같은 스타일 속성에서 이미지 찾기 (일부 사이트는 CSS로 설정)
			List<WebElement> bgImages = webDriver.findElements(By.cssSelector("div[style*='background-image']"));
			for (WebElement bg : bgImages) {
				String style = bg.getAttribute("style");
				if (style.contains("background-image")) {
					Matcher matcher = Pattern.compile("url\\(['\"]?(.*?)['\"]?\\)").matcher(style);
					if (matcher.find()) {
						return matcher.group(1);
					}
				}
			}

			// JavaScript를 직접 실행하여 `document.images`에서 이미지 리스트 가져오기
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			List<String> imgUrls = (List<String>) js.executeScript(
				"return Array.from(document.images).map(img => img.src);"
			);

			if (!imgUrls.isEmpty()) {
				return imgUrls.get(5); // 5번째 이미지 반환
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Error while waiting for JavaScript execution", e);
		} finally {
			webDriverConfig.closeWebDriver(webDriver); // WebDriver 종료
		}
		return null;
	}

	private String fetchInstagramImage(String url) {

		WebDriver webDriver = webDriverConfig.createWebDriver();

		try {
			webDriver.get(url);
			Thread.sleep(3000); // JavaScript 로드 대기

			// 1. 게시글 내부의 이미지 찾기 (대표 이미지 제외)
			List<WebElement> postImages = webDriver.findElements(By.cssSelector("div[role='presentation'] img"));

			for (WebElement img : postImages) {
				String imageUrl = img.getAttribute("src");
				if (isValidImage(imageUrl) && validateImageSize(img)) {
					log.info("Instagram post image found: {}", imageUrl);
					return imageUrl;
				}
			}

			// 2. 만약 게시글 내부 이미지가 없다면, 일반 `img` 태그에서 대체 이미지 찾기
			List<WebElement> images = webDriver.findElements(By.tagName("img"));
			for (WebElement img : images) {
				String imageUrl = img.getAttribute("src");
				if (isValidImage(imageUrl) && validateImageSize(img)) {
					log.info("Instagram image found: {}", imageUrl);
					return imageUrl;
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Error while waiting for JavaScript execution", e);
		} finally {
			webDriverConfig.closeWebDriver(webDriver); // WebDriver 종료
		}
		return null;
	}

	private boolean isValidImage(String imageUrl) {
		if (imageUrl == null || imageUrl.isEmpty()) return false;

		// 1. X 버튼, 로고 같은 이미지 제외
		String lowerUrl = imageUrl.toLowerCase();
		if (lowerUrl.contains("close") || lowerUrl.contains("x-icon") || lowerUrl.contains("logo")) {
			log.info("Skipping unwanted image: {}", imageUrl);
			return false;
		}

		// 2. 기본 프로필 이미지, 기본 썸네일 제외
		if (lowerUrl.contains("default") || lowerUrl.contains("placeholder")) {
			log.info("Skipping default/placeholder image: {}", imageUrl);
			return false;
		}
		return true;
	}

	private boolean validateImageSize(WebElement img) {
		try {
			int width = Integer.parseInt(img.getAttribute("width"));
			int height = Integer.parseInt(img.getAttribute("height"));

			// 최소 크기 기준 설정 (예: 너비 또는 높이가 50px 미만이면 제외)
			if (width < 50 || height < 50) {
				log.info("Skipping small image ({}x{}): {}", width, height, img.getAttribute("src"));
				return false;
			}
		} catch (Exception e) {
			// 크기 정보가 없는 경우 로그 출력 후 통과
			log.warn("Image size not found, skipping size check: {}", img.getAttribute("src"));
		}
		return true;
	}


	/** `src`, `data-src`, `srcset`, `style` 속성에서 이미지 추출 */
	private String extractImageFromElement(WebElement img) {

		String imageUrl = img.getAttribute("src");

		if (imageUrl == null || imageUrl.isEmpty()) {
			imageUrl = img.getAttribute("data-src"); // `data-src` 우선 적용
		}
		if ((imageUrl == null || imageUrl.isEmpty()) && img.getAttribute("srcset") != null) {
			imageUrl = img.getAttribute("srcset").split(" ")[0]; // `srcset`의 첫 번째 이미지 사용
		}
		if (isValidImage(imageUrl) && validateImageSize(img)) {
			log.info("Instagram post image found: {}", imageUrl);
			return imageUrl;
		}
		return null;
	}

	private String processAndUploadImage(String imgUrl) throws IOException {

		BufferedImage originalImage;

		// 1. Base64 이미지 처리
		if (imgUrl.startsWith("data:")) {
			log.info("Base64 Image Detected.");
			String base64Data = imgUrl.substring(imgUrl.indexOf(",") + 1);
			byte[] imageBytes = Base64.getDecoder().decode(base64Data);
			try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
				originalImage = ImageIO.read(inputStream);
			}
		} else {
			// 2. 일반 이미지 URL 처리
			originalImage = ImageIO.read(new URL(imgUrl));
		}

		// 이미지가 null이면 처리 중단
		if (originalImage == null) {
			log.warn("Failed to load image from URL: {}", imgUrl);
			return null;
		}
		// 3. 이미지 크기 조정
		BufferedImage resizedImage = resizeImage(originalImage, 350, calculateHeight(350));

		// 4. 이미지를 ByteBuffer로 변환
		ByteBuffer imageBytes = convertImageToByteBuffer(resizedImage);

		// 5. S3에 업로드 (파일명은 UUID 기반으로 생성)
		return s3Service.uploadImageFile(imageBytes);
	}

	private static int calculateHeight(int targetWidth) {
		return (int) (targetWidth * 0.75);
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
		Graphics2D g2d = resizedImage.createGraphics();
		g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();
		return resizedImage;
	}

	private static ByteBuffer convertImageToByteBuffer(BufferedImage image) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", outputStream);
		return ByteBuffer.wrap(outputStream.toByteArray());
	}
}
