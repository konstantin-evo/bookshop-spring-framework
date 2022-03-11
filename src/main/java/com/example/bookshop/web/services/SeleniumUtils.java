package com.example.bookshop.web.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * A SeleniumUtils class implement Selenium Webdriver utility functions:
 * call page by URL, clickWeb element By Xpath ot id, etc
 */
public class SeleniumUtils {

    @Value("${selenium.pause}")
    private int WAIT_TIME;

    private final ChromeDriver driver;
    private final WebDriverWait wait;

    public SeleniumUtils(ChromeDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, WAIT_TIME);
    }

    public SeleniumUtils callPageByUrl(String url) {
        driver.get(url);
        return this;
    }

    public SeleniumUtils clickByXpath(String xpath) {
        WebElement element =wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(xpath)));
        element.click();
        return this;
    }

    public SeleniumUtils clickById(String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                        By.id(id)));
        element.click();
        return this;
    }

    public SeleniumUtils setUpSearchTokenById(String token, String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        element.sendKeys(token);
        return this;
    }

    public SeleniumUtils submitElementById(String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        element.submit();
        return this;
    }

    public SeleniumUtils pause() {
        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        return this;
    }

}
