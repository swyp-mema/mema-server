package com.swyp.mema.global.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WebDriverConfig {

	private static final ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

	public WebDriver getWebDriver() {
		if (webDriverThreadLocal.get() == null) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
			webDriverThreadLocal.set(new ChromeDriver(options));
		}
		return webDriverThreadLocal.get();
	}

	public void quitWebDriver() {
		WebDriver driver = webDriverThreadLocal.get();
		if (driver != null) {
			driver.quit();
			webDriverThreadLocal.remove();
		}
	}
}
