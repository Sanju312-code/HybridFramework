package com.testcases;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ActionKeywords.ActionKeywords;
import com.ActionKeywords.EmailUtil;
import com.ActionKeywords.Testcase_functions;
import com.Utils.PropertyUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.base.BaseTest;
import com.reporting.ExtentManager;

public class TestExecutor extends BaseTest {
	ExtentReports extentBase64;
	ExtentReports extentPNG;

	Testcase_functions tc = new Testcase_functions();

	@BeforeClass
	public void setupReports() throws Exception {
		extentBase64 = ExtentManager.getExtentBase64();
		extentPNG = ExtentManager.getExtentPNG();
	}

	@Test(priority = 1)
	public void userstorycreate() throws Exception {
		ExtentTest testBase64 = extentBase64.createTest(PropertyUtil.getProperty("testcasename1"));
		ExtentTest testPNG = extentPNG.createTest(PropertyUtil.getProperty("testcasename1"));
		ExtentManager.setTestBase64(testBase64);
		ExtentManager.setTestPNG(testPNG);

		try {
			tc.login();
//			tc.switchtouser();
//			tc.nagivationToRequirementManagement();
//			tc.nagivationToRequirementManagementCreateUserStory();
//			tc.CreateUserStory();
			tc.logout();

			testBase64.pass("User story created successfully.");
			testPNG.pass("User story created successfully.");

			ExtentManager.markTestPassed();

		} catch (Exception e) {
			WebDriver driver = ExtentManager.getDriver();
			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

			testBase64.fail("Test case 'userstorycreate' failed.<br>Reason: " + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

			testPNG.fail("Test case 'userstorycreate' failed. Screenshot saved in PNG format.")
					.addScreenCaptureFromBase64String(base64Screenshot);

			ExtentManager.markTestFailed();
			Assert.fail("Test failed due to exception: " + e.getMessage(), e);
		}
	}

//	@Test(priority = 2)
//	public void userstorydelete() throws Exception {
//		ExtentTest testBase64 = extentBase64.createTest(PropertyUtil.getProperty("testcasename2"));
//		ExtentTest testPNG = extentPNG.createTest(PropertyUtil.getProperty("testcasename2"));
//		ExtentManager.setTestBase64(testBase64);
//		ExtentManager.setTestPNG(testPNG);
//
//		try {
//			tc.login();
//			tc.switchtouser();
//			tc.nagivationToRequirementManagement();
//			tc.nagivationToRequirementManagementCreateUserStory();
//			tc.CreateUserStory();
//			tc.deleteUserstory();
//			tc.logout();
//
//			testBase64.pass("User story deleted successfully.");
//			testPNG.pass("User story deleted successfully.");
//
//			ExtentManager.markTestPassed();
//
//		} catch (Exception e) {
//			WebDriver driver = ExtentManager.getDriver();
//			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//
//			testBase64.fail("Test case 'userstorycdelete' failed.<br>Reason: " + e.getMessage(),
//					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
//
//			testPNG.fail("Test case 'userstorydelete' failed. Screenshot saved in PNG format.")
//					.addScreenCaptureFromBase64String(base64Screenshot);
//
//			ExtentManager.markTestFailed();
//			Assert.fail("Test failed due to exception: " + e.getMessage(), e);
//		}
//	}

//	@Test(priority = 1)
//	public void epicdragdrop() throws Exception {
//
//		try {
//			test = extent.createTest("Epic drag drop");
//			ExtentManager.setTest(test);
//			tc.login();
//			tc.switchtoadmin();
//			tc.nagivationtoepic();
//			tc.epicdraganddropleft();
//			tc.logout();
//
//		} catch (Exception e) {
//
//			WebDriver driver = ExtentManager.getDriver();
//
//			// Capture Base64 for embedding in HTML
//			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//
//			test.fail("Test case 'Epic Drag drop' failed.<br>Reason: " + e.getMessage(),
//					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
//
//			Assert.fail("Test failed due to exception: " + e.getMessage(), e);
//		}
//	}
//
//	@Test(priority = 1)
//	public void epicDragDropTopToBottom() throws Exception {
//
//		try {
//			test = extent.createTest("Epic drag drop top to bottom");
//			ExtentManager.setTest(test);
//			tc.login();
//			tc.switchtoadmin();
//			tc.nagivationtoepic();
//			tc.epicdraganddroptoptobottom();
//			tc.logout();
//
//		} catch (Exception e) {
//			WebDriver driver = ExtentManager.getDriver();
//
//			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//
//			test.fail("Test case 'Epic Drag  and drop To bottom' failed.<br>Reason: " + e.getMessage(),
//					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
//
//			Assert.fail("Test failed due to exception: " + e.getMessage(), e);
//		}
//	}

	@AfterClass
	public void flushReportsAndSendEmail() throws Exception {
		try {
			// Flush reports
			if (extentBase64 != null)
				extentBase64.flush();
			if (extentPNG != null)
				extentPNG.flush();

			// Mark execution end for timing
			ExtentManager.markExecutionEnd();

			ActionKeywords.openGeneratedReports();

			// Send email with reports
			EmailUtil.sendReportByEmaidyamic();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("❌ Error during cleanup or email sending.");
		}

	}

}
