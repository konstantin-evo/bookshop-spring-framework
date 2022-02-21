package com.example.bookshop.web.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
class MainPageSeleniumTests {

    private static ChromeDriver driver;
    private static MainPage mainPage;

    @Value("${base.url}")
    private String URL;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "127.0.0.1");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setUp() {
        mainPage = new MainPage(driver);
        ReflectionTestUtils.setField(mainPage, "URL", URL);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    public void testMainPageAccess() throws InterruptedException {
        mainPage.callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    public void testMainPageSearchByQuery() throws InterruptedException {
        mainPage.callPage()
                .pause()
                .setUpSearchToken("Sudden")
                .pause()
                .submit()
                .pause();

        assertTrue(driver.getPageSource().contains("Sudden Manhattan"));
    }

}