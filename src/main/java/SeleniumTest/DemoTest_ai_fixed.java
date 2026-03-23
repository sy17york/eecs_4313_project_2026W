package SeleniumTest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * EECS 4313 Project - Tool Demo & AI Exploration
 * This suite demonstrates Selenium automation integrated with AI-suggested edge cases,
 * handling server-side AuthenticationFilters and client-side localStorage state.
 */
public class DemoTest_ai_fixed extends BaseTest {

    // --- HELPERS ---

	private void ensureLoggedOut() {
	    driver.get(baseUrl + "/admin");

	    // If dashboard is showing, we are logged in — so log out
	    wait.until(ExpectedConditions.or(
	            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")),
	            ExpectedConditions.visibilityOfElementLocated(By.id("loginForm"))
	        ));
	    List<WebElement> dashboard = driver.findElements(By.id("dashboard"));
	    if (!dashboard.isEmpty() && dashboard.get(0).isDisplayed()) {
	        driver.findElement(By.xpath("//button[contains(text(),'Logout')]")).click();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
	    }

	    
	}
	

	private void ensureLoggedIn() {
	    driver.get(baseUrl + "/admin");

	    wait.until(ExpectedConditions.or(
	            ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")),
	            ExpectedConditions.visibilityOfElementLocated(By.id("loginForm"))
	    	));
	    // If login form is showing, we are logged out — so log in
	    List<WebElement> loginForm = driver.findElements(By.id("loginForm"));
	    if (!loginForm.isEmpty() && loginForm.get(0).isDisplayed()) {
	        driver.findElement(By.id("username")).sendKeys("admin");
	        driver.findElement(By.id("password")).sendKeys("admin");
	        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
	    }

	}

    // --- PART 1: NAVIGATION & SECURITY FILTER ---

    @Test
    public void testAdminNavigation() {
        ensureLoggedOut();
        driver.get(baseUrl); // back to home to test the nav link click
        demoPause();

        driver.findElement(By.linkText("Admin Dashboard")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        assertTrue(driver.findElement(By.id("loginForm")).isDisplayed());
        demoPause(2);
    }

    // --- PART 2: LOGIN SCENARIOS (Fail, Empty, Success) ---

    @Test
    public void testAdminLoginFail() {
        ensureLoggedOut(); // already on /admin login form
        driver.findElement(By.id("username")).sendKeys("wrongUser");
        driver.findElement(By.id("password")).sendKeys("wrongPass");
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage")));
        assertTrue(error.isDisplayed());
        demoPause(2);
    }

    @Test
    public void testEmptyLogin() {
        ensureLoggedOut(); // already on /admin login form
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm"))).isDisplayed());
        demoPause(2);
    }

    @Test
    public void testAdminLoginSuccess() {
        ensureLoggedOut();
        ensureLoggedIn(); // ← was loginAsAdmin()

        WebElement dashboard = driver.findElement(By.id("dashboard"));
        assertFalse(dashboard.getAttribute("class").contains("hidden"));
        demoPause(2);
    }

    // --- PART 3: AI-INSPIRED EDGE CASES ---

    @Test
    public void testLongInputLogin() {
        ensureLoggedOut(); // already on /admin login form
        String longInput = "a".repeat(100);

        driver.findElement(By.id("username")).sendKeys(longInput);
        driver.findElement(By.id("password")).sendKeys(longInput);
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage")));
        assertTrue(error.isDisplayed());
        demoPause(2);
    }

    @Test
    public void testSessionPersistenceOnRefresh() {
        ensureLoggedOut();
        ensureLoggedIn();

        driver.navigate().refresh();
        assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))).isDisplayed());
        demoPause(2);
    }

    // --- PART 4: DASHBOARD & LOGOUT ---

    @Test
    public void testDashboardFunctionalityAndRouting() {
        ensureLoggedOut(); // ← was missing
        ensureLoggedIn();  // ← was loginAsAdmin()

        assertTrue(driver.findElement(By.linkText("Manage Users")).isDisplayed());
        assertTrue(driver.findElement(By.linkText("View Inventory")).isDisplayed());

        driver.findElement(By.linkText("Manage Users")).click();
        wait.until(ExpectedConditions.urlContains("AdminController"));
        wait.until(ExpectedConditions.urlContains("action=manageUsers"));
        demoPause(2);
    }

    
    // --- PART 5: ADDITIONAL COVERAGE ---
    @Test
    public void testViewInventoryRouting() {
        ensureLoggedOut();
        ensureLoggedIn();

        driver.findElement(By.linkText("View Inventory")).click();
        wait.until(ExpectedConditions.urlContains("AdminController"));
        wait.until(ExpectedConditions.urlContains("action=viewInventory"));
        demoPause(2);
    }

    // Missing: Tests View Sales route
    @Test
    public void testViewSalesRouting() {
        ensureLoggedOut();
        ensureLoggedIn();

        driver.findElement(By.linkText("View Sales")).click();
        wait.until(ExpectedConditions.urlContains("AdminController"));
        wait.until(ExpectedConditions.urlContains("action=viewSalesHistory"));
        demoPause(2);
    }

    @Test
    public void testLogoutClearsAllStates() {
        ensureLoggedOut();
        ensureLoggedIn();

        driver.findElement(By.xpath("//button[contains(text(), 'Logout')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));

        // Verify localStorage was cleared
        String isLoggedIn = (String) ((JavascriptExecutor) driver)
                            .executeScript("return localStorage.getItem('isLoggedIn');");
        assertNull("LocalStorage should be cleared after logout", isLoggedIn);

        // Navigate back — filter lets through (session alive), but JS shows login form
        driver.get(baseUrl + "/admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        assertTrue(driver.findElement(By.id("loginForm")).isDisplayed());
        demoPause(2);
    }
}