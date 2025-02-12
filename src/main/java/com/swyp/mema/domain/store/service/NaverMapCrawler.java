package com.swyp.mema.domain.store.service;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.swyp.mema.domain.store.dto.ImageRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NaverMapCrawler {

	public ImageRes crawaling(String url) {

		try {
			// 1. HTML 문서 로드
			Document doc = Jsoup.connect(url).get();

			// 2. 이미지 URL 가져오기
			String imgUrl = fetchImageUrl(doc);
			if (imgUrl == null) {
				log.info("No images found on the page.");
				return null;
			}

			BufferedImage originalImage;

			System.out.println("url : " + url);
			System.out.println("imgUrl : " + imgUrl);

			// 3. 이미지 로드 (data: URL 처리 추가)
			if (imgUrl.startsWith("data:")) {
				log.info("Data URL detected.");
				String base64Data = imgUrl.substring(imgUrl.indexOf(",") + 1);
				byte[] imageBytes = Base64.getDecoder().decode(base64Data);
				try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
					originalImage = ImageIO.read(inputStream);
				}
			} else {
				try (InputStream inputStream = new URL(imgUrl).openStream()) {
					originalImage = ImageIO.read(inputStream);
				}
			}

			System.out.println("originalImage = " + originalImage);
			if (originalImage == null)	return null;

			// 4. 크기 조정
			int targetWidth = 350; // 원하는 너비
			int targetHeight = calculateHeightKeepingAspectRatio(originalImage, targetWidth);
			BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

			// 5. 이미지를 Base64로 변환
			String base64Image = convertImageToBase64(resizedImage);
			log.info("Base64 Encoded Image: {}", base64Image);

			// 6. DTO 생성
			return new ImageRes("data:image/png;base64," + base64Image, targetWidth, targetHeight);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String fetchImageUrl(Document doc) {

		// // 1. 메타 태그에서 대표 이미지 가져오기
		Element metaImage = doc.selectFirst("meta[property=og:image]");
		if (metaImage != null) {
			String mainImageUrl = metaImage.attr("content");

			// 상대 경로일 경우 절대 경로로 변환
			if (!mainImageUrl.startsWith("https")) {
				try {
					mainImageUrl = new URL(new URL(doc.baseUri()), mainImageUrl).toString();
				} catch (MalformedURLException e) {
					e.printStackTrace();
					log.info("Failed to convert relative URL to absolute: {}", mainImageUrl);
					return null; // 예외가 발생하면 null 반환
				}
			}
			log.info("Main Image URL: {}", mainImageUrl);
			return mainImageUrl;
		}

		// 2. 특정 클래스 또는 alt/title 속성을 사용해 음식 사진 가져오기
		Elements foodImages = doc.select("img.food-image, img[alt*=food], img[title*=food]");
		if (foodImages != null && !foodImages.isEmpty()) {
			Element imgElement = foodImages.first();
			String foodImageUrl = imgElement.absUrl("src");
			log.info("Food Image URL: {}", foodImageUrl);
			return foodImageUrl;
		}

		// 3. 모든 이미지 중 첫 번째 이미지 가져오기
		Element firstImage = doc.selectFirst("img");
		if (firstImage != null) {
			String firstImageUrl = firstImage.absUrl("src");
			log.info("Fallback First Image URL: {}", firstImageUrl);
			return firstImageUrl;
		}

		// 4. 이미지가 전혀 없는 경우
		return null;
	}

	// 원본 비율을 유지하면서 높이 계산
	private static int calculateHeightKeepingAspectRatio(BufferedImage image, int targetWidth) {
		double aspectRatio = (double) image.getHeight() / image.getWidth();
		return (int) (targetWidth * aspectRatio);
	}

	// 이미지 크기 조정
	private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
		Graphics2D g2d = resizedImage.createGraphics();
		g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();
		return resizedImage;
	}

	// 이미지를 Base64 문자열로 변환
	private static String convertImageToBase64(BufferedImage image) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", outputStream);
		byte[] imageBytes = outputStream.toByteArray();
		return Base64.getEncoder().encodeToString(imageBytes);
	}
}
