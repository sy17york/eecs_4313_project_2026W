package SeleniumTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DemoTest_ai extends BaseTest {

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
    

    // AI-inspired: Edge case (long input)
    @Test
    public void testLongInputLogin() {
        driver.get(baseUrl + "/admin");

        String longInput = "aaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        driver.findElement(By.id("username")).sendKeys(longInput);
        driver.findElement(By.id("password")).sendKeys(longInput);

        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage"))
        );

        assertTrue(error.isDisplayed());
        demoPause(2);
    }
    
 // AI-inspired: Session Persistence
    @Test
    public void testLoginPersistence() {
        driver.get(baseUrl + "/admin");

        // login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
            .sendKeys("admin");

        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));

        // refresh
        driver.navigate().refresh();

        WebElement dashboard = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))
        );

        assertTrue(dashboard.isDisplayed());
        demoPause(2);
    }
    
    /**
     * HELPER: Logs in as admin to reach the dashboard state.
     * Use this to avoid repeating login logic in every dashboard test.
     */
    private void loginAsAdmin() {
    	
        driver.get(baseUrl + "/admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
    }
    
    // AI-inspired: Dashboard Component Verification
    @Test
    public void testDashboardLinksVisibility() {
        loginAsAdmin();
        
        // Verify specific action links are present
        WebElement viewSales = driver.findElement(By.linkText("View Sales"));
        WebElement viewInventory = driver.findElement(By.linkText("View Inventory"));
        WebElement manageUsers = driver.findElement(By.linkText("Manage Users"));

        assertTrue("View Sales link should be visible", viewSales.isDisplayed());
        assertTrue("View Inventory link should be visible", viewInventory.isDisplayed());
        assertTrue("Manage Users link should be visible", manageUsers.isDisplayed());
        
        demoPause(2);
    }
    
    //AI-inspired: Navigation Depth (Controller Integration)
    @Test
    public void testAdminControllerRouting() {
        loginAsAdmin();

        // Click Manage Users
        driver.findElement(By.linkText("Manage Users")).click();

        // Verify the URL changed to include the controller and the correct action
        wait.until(ExpectedConditions.urlContains("AdminController"));
        wait.until(ExpectedConditions.urlContains("action=manageUsers"));
        
        System.out.println("Current URL after click: " + driver.getCurrentUrl());
        demoPause(2);
    }
    
    // AI-inspired: UI State Consistency
    @Test
    public void testDashboardUIContent() {
        loginAsAdmin();

        WebElement header = driver.findElement(By.cssSelector("#dashboard h1"));
        assertEquals("Admin Dashboard", header.getText());

        // Check for specific UI instructions text
        WebElement instructionText = driver.findElement(By.xpath("//p[contains(text(), 'Select an option')]"));
        assertTrue(instructionText.isDisplayed());
        
        demoPause();
    }
    
    // AI-inspired: Logout
    @Test
    public void testLogout() {
        driver.get(baseUrl + "/admin");

        // login first
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
            .sendKeys("admin");

        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath("//button[contains(text(), " +
                "'Login')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));

        // logout
        driver.findElement(By.xpath("//button[contains(text(), 'Logout')]")).click();

        WebElement loginForm = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loginForm"))
        );

        assertTrue(loginForm.isDisplayed());
        demoPause(2);
    }
    
}