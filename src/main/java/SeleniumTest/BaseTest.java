package SeleniumTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;
    protected WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.edge.driver",
                "E:\\UT\\York\\yorku2026winter\\eecs 4313 X\\project\\edgedriver_win64\\msedgedriver.exe");

        driver = new EdgeDriver();

        baseUrl = "http://localhost:8080/eecs4413_project_ShuYang/";
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void demoPause() {
        demoPause(1);
    }

    protected void demoPause(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}