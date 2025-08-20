package com.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.ActionKeywords.ActionKeywords;
import com.Utils.PropertyUtil;
import com.reporting.ExtentManager;

public class BaseTest {

	protected WebDriver driver;

	@BeforeClass
	public void setup() throws Exception {

		driver = new ChromeDriver();
		ExtentManager.setDriver(driver);
		driver.manage().window().maximize();
		//ActionKeywords.loadLocators("LoginPage");
		 String locatorSheetName = PropertyUtil.getProperty("locator.sheet.name");

		 if (locatorSheetName != null && !locatorSheetName.trim().isEmpty()) {
	            // Trim, normalize case (first letter uppercase, rest lowercase)
	            locatorSheetName = locatorSheetName.trim();

	            // Example normalization: "loginpage" → "LoginPage"
	            locatorSheetName = locatorSheetName.substring(0, 1).toUpperCase() +
	                               locatorSheetName.substring(1).toLowerCase();

	            ActionKeywords.loadLocators(locatorSheetName);
	            System.out.println("✅ Loaded locators from sheet: " + locatorSheetName);
	        } else {
	            System.out.println("⚠ No locator sheet name provided in properties file.");
	        }
	    
		Thread.sleep(3000);

	}
	@AfterClass
	public void tearDown() {

		try {
            if (driver != null) {
                driver.quit();
                System.out.println("✅ Browser session closed successfully.");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error while quitting driver: " + e.getMessage());
        } finally {
            ExtentManager.unload();
        }
		
	}

}
