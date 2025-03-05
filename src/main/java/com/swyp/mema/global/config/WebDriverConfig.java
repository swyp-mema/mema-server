package com.swyp.mema.global.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WebDriverConfig {

	public WebDriver createWebDriver() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
		return new ChromeDriver(options);
	}

	public void closeWebDriver(WebDriver driver) {
		if (driver != null) {
			driver.quit();
		}
	}
}
