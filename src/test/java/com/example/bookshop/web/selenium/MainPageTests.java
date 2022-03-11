package com.example.bookshop.web.selenium;

import com.example.bookshop.web.services.SeleniumUtils;
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
class MainPageTests {

    private static ChromeDriver driver;
    private static SeleniumUtils seleniumUtils;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${selenium.pause}")
    private int WAIT_TIME;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "127.0.0.1");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setUp() {
        seleniumUtils = new SeleniumUtils(driver);
        ReflectionTestUtils.setField(seleniumUtils, "WAIT_TIME", WAIT_TIME);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    public void testMainPageAccess() {
        seleniumUtils.callPageByUrl(BASE_URL)
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    public void testMainPageSearchByQuery() {
        seleniumUtils.callPageByUrl(BASE_URL)
                .setUpSearchTokenById("Sudden", "query")
                .submitElementById("search")
                .pause();

        assertTrue(driver.getPageSource().contains("Sudden Manhattan"));
    }

}