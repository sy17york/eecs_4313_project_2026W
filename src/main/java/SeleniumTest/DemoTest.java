package SeleniumTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DemoTest extends BaseTest {

    // 1. Navigation
    @Test
    public void testAdminNavigation() {
        driver.get(baseUrl + "/");
        demoPause();

        WebElement adminLink = driver.findElement(By.linkText("Admin Dashboard"));
        assertTrue(adminLink.isDisplayed());

        adminLink.click();
        wait.until(ExpectedConditions.urlContains("/admin"));
        demoPause(2);
    }

    // 2. Invalid Login
    @Test
    public void testAdminLoginFail() {
        driver.get(baseUrl + "/admin");
        demoPause();

        driver.findElement(By.id("username")).sendKeys("wrong");
        driver.findElement(By.id("password")).sendKeys("wrong");

        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage"))
        );

        assertTrue(error.isDisplayed());
        demoPause(2);
    }
    @Test
    public void testEmptyLoginManual() {
        driver.get(baseUrl + "/admin");

        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        WebElement loginForm = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loginForm"))
        );

        assertTrue(loginForm.isDisplayed());
        demoPause(2);
    }

    // 3. Valid Login
    @Test
    public void testAdminLoginSuccess() {
        driver.get(baseUrl + "/admin");
        demoPause();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
            .sendKeys("admin");

        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();

        WebElement dashboard = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))
        );

        assertTrue(dashboard.isDisplayed());

        String dashboardClass = dashboard.getAttribute("class");
        assertFalse(dashboardClass.contains("hidden"));

        demoPause(2);
    }


}