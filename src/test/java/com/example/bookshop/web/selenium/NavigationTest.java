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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: Refactoring needed - tests don't work
@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
class NavigationTest {

    private static ChromeDriver driver;
    private static SeleniumUtils seleniumUtils;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${selenium.pause}")
    private int WAIT_TIME;

    private static final String XPATH_BOOK_FROM_MAIN = "/html/body/div/div/main/div[1]/div[2]/div[1]/div/div/div[1]/div/div/div[1]/a";
    private static final String XPATH_BOOK_FROM_NEWS = "/html/body/div[1]/div/main/div/div[2]/div[1]/div[2]/strong/a";
    private static final String XPATH_BOOK_FROM_POPULAR = "/html/body/div/div/main/div/div[2]/div[1]/div[2]/strong/a";
    private static final String XPATH_GENRE_DETECTIVES = "/html/body/div/div/main/div/div/div/div[8]/div[1]/div/a";
    private static final String XPATH_AUTHOR_AURELEA = "/html/body/div/div/main/div/div/div[2]/div/div[1]/a";
    private static final String NAV_MAIN_ID = "nav-main";
    private static final String NAV_GENRE_ID = "nav-genres";
    private static final String NAV_NEWS_ID = "nav-news";
    private static final String NAV_POPULAR_ID = "nav-popular";
    private static final String NAV_AUTHORS_ID = "nav-authors";

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "127.0.0.1");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
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

//    @Test
    void fromMainPageToGenreTest() {
        seleniumUtils
                .callPageByUrl(BASE_URL)
                .clickByXpath(XPATH_BOOK_FROM_MAIN)
                .clickById(NAV_GENRE_ID);

        boolean verifyTitle = driver.getTitle().equalsIgnoreCase("Genres");
        assertNotNull(driver.getTitle());
        assertTrue(verifyTitle);
    }

//    @Test
    void fromGenrePageToNewsTest() {
        seleniumUtils
                .callPageByUrl(BASE_URL + "genres")
                .clickByXpath(XPATH_GENRE_DETECTIVES)
                .clickById(NAV_NEWS_ID);

        boolean verifyTitle = driver.getTitle().equalsIgnoreCase("News");
        assertNotNull(driver.getTitle());
        assertTrue(verifyTitle);
    }

//    @Test
    void fromNewsPageToPopularTest() {
        seleniumUtils
                .callPageByUrl(BASE_URL + "books/recent")
                .clickByXpath(XPATH_BOOK_FROM_NEWS)
                .clickById(NAV_POPULAR_ID);

        boolean verifyTitle = driver.getTitle().equalsIgnoreCase("Popular");
        assertNotNull(driver.getTitle());
        assertTrue(verifyTitle);
    }

//    @Test
    void fromPopularPageToAuthorsTest() {
        seleniumUtils
                .callPageByUrl(BASE_URL + "books/popular")
                .clickByXpath(XPATH_BOOK_FROM_POPULAR)
                .clickById(NAV_AUTHORS_ID);

        boolean verifyTitle = driver.getTitle().equalsIgnoreCase("Authors");
        assertNotNull(driver.getTitle());
        assertTrue(verifyTitle);
    }

//    @Test
    void fromAuthorsPageToMainTest() {
        seleniumUtils
                .callPageByUrl(BASE_URL + "authors")
                .clickByXpath(XPATH_AUTHOR_AURELEA)
                .clickById(NAV_MAIN_ID);

        boolean verifyTitle = driver.getTitle().equalsIgnoreCase("Main");
        assertNotNull(driver.getTitle());
        assertTrue(verifyTitle);
    }

}