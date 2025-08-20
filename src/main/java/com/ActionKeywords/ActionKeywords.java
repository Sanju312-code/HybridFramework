package com.ActionKeywords;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Utils.PropertyUtil;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.reporting.ExtentManager;

public class ActionKeywords {

	private static final String EXCEL_PATH = "./src/test/resources/testdata.xlsx";
	private static Map<String, String> locatorMap = new HashMap<>();
	private static final int TIMEOUT = 30;


	public static void loadLocators(String sheetName) {
		try (FileInputStream fis = new FileInputStream(new File(EXCEL_PATH));
				Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				System.err.println("Sheet '" + sheetName + "' not found in Excel.");
				return;
			}

			for (Row row : sheet) {
				Cell keyCell = row.getCell(0);
				Cell valueCell = row.getCell(1);

				if (keyCell != null && valueCell != null) {
					String key = keyCell.getStringCellValue().trim();
					String value = valueCell.getStringCellValue().trim();
					locatorMap.put(key, value);
					System.out.println("Loaded locator: " + key + " = " + value);
				} else {
					System.err.println("Skipping row: missing key or value.");
				}
			}
		} catch (Exception e) {
			System.err.println("Error loading locators: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static String get(String elementName) {
		return locatorMap.get(elementName);
	}

	public static String captureScreenshot(String screenshotName) {
		WebDriver driver = ExtentManager.getDriver();
	    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + timestamp + ".png";

	    try {
	        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        File destFile = new File(screenshotPath);
	        FileUtils.copyFile(srcFile, destFile);
	        return screenshotPath;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static String captureScreenshotBase64() {
	    try {
	        return ((TakesScreenshot) ExtentManager.getDriver()).getScreenshotAs(OutputType.BASE64);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static void logToBothReports(String message, String screenshotName, WebDriver driver, boolean isPass) {
	    String base64 = captureScreenshotBase64(); // For Base64 report
	    String pngPath = captureScreenshot( screenshotName); // For PNG report

	    if (isPass) {
	        if (base64 != null) {
	            ExtentManager.getTestBase64().pass(message,
	                MediaEntityBuilder.createScreenCaptureFromBase64String(base64, screenshotName + ".png").build());
	        } else {
	            ExtentManager.getTestBase64().pass(message + " (Base64 screenshot failed)");
	        }

	        if (pngPath != null) {
	            ExtentManager.getTestPNG().pass(message,
	                MediaEntityBuilder.createScreenCaptureFromPath(pngPath).build());
	        } else {
	            ExtentManager.getTestPNG().pass(message + " (PNG screenshot failed)");
	        }
	    } else {
	        if (base64 != null) {
	            ExtentManager.getTestBase64().fail(message,
	                MediaEntityBuilder.createScreenCaptureFromBase64String(base64, screenshotName + ".png").build());
	        } else {
	            ExtentManager.getTestBase64().fail(message + " (Base64 screenshot failed)");
	        }

	        if (pngPath != null) {
	            ExtentManager.getTestPNG().fail(message,
	                MediaEntityBuilder.createScreenCaptureFromPath(pngPath).build());
	        } else {
	            ExtentManager.getTestPNG().fail(message + " (PNG screenshot failed)");
	        }
	    }
	}

	public static void navigate(String urlKey) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String url = PropertyUtil.getProperty(urlKey);
			driver.get(url);
			logToBothReports("Navigated to URL: " + url, "Navigate", driver, true);
		} catch (Exception e) {
			logToBothReports("Failed to navigate to URL: " + urlKey + " - " + e.getMessage(), "Navigate", driver,
					false);
			throw new RuntimeException(e);
		}
	}

	public static void enterText(String elementName, String valueOrKey, boolean isRuntime) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String locator = get(elementName);
			WebElement element = driver.findElement(By.xpath(locator));
			element.clear();

			String valueToEnter = isRuntime ? valueOrKey : PropertyUtil.getProperty(valueOrKey);
			element.sendKeys(valueToEnter);
			logToBothReports("Entered text in element: " + elementName + " → " + valueToEnter, elementName, driver,
					true);
		} catch (Exception e) {
			logToBothReports("Failed to enter text in element: " + elementName + " - " + e.getMessage(), elementName,
					driver, false);
			throw new RuntimeException(e);
		}
	}

	public static void click(String elementName) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String locator = get(elementName);
			WebElement element = driver.findElement(By.xpath(locator));
			element.click();
			logToBothReports("Clicked on element: " + elementName, elementName, driver, true);
		} catch (Exception e) {
			logToBothReports("Failed to click on element: " + elementName + " - " + e.getMessage(), elementName, driver,
					false);
			throw new RuntimeException(e);
		}
	}

	public static void validateText(String elementName, String expectedText) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String xpath = get(elementName);
			WebElement element = driver.findElement(By.xpath(xpath));
			String actualText = element.getText().trim();
			if (actualText.equals(expectedText.trim())) {
				logToBothReports("Validation Passed: " + expectedText, elementName, driver, true);
			} else {
				logToBothReports("Validation Failed: Expected [" + expectedText + "] but found [" + actualText + "]",
						elementName, driver, false);
			}
		} catch (Exception e) {
			logToBothReports("Exception in validating text: " + e.getMessage(), elementName, driver, false);
		}
	}

	public static void waitUntilElementPresent(String elementName) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String locator = get(elementName);
			new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			logToBothReports("Element is present: " + elementName, elementName, driver, true);
		} catch (Exception e) {
			logToBothReports("Failed to wait for element: " + elementName + " - " + e.getMessage(), elementName, driver,
					false);
			throw new RuntimeException(e);
		}
	}

	public static void moveToElement(String elementName) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String locator = get(elementName);
			WebElement element = driver.findElement(By.xpath(locator));
			new Actions(driver).moveToElement(element).perform();
			logToBothReports("Moved to element: " + elementName, elementName, driver, true);
		} catch (Exception e) {
			logToBothReports("Failed to move to element: " + elementName + " - " + e.getMessage(), elementName, driver,
					false);
			throw new RuntimeException(e);
		}
	}

	public static String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder result = new StringBuilder();
		Random random = new Random();

		try {
			for (int i = 0; i < length; i++) {
				int index = random.nextInt(characters.length());
				result.append(characters.charAt(index));
			}

			return result.toString();

		} catch (Exception e) {
			System.err.println("Error generating random string: " + e.getMessage());
			throw new RuntimeException("Failed to generate random string", e);
		}
	}

	public static void validatePartialText(String elementName, String textKey) {
		WebDriver driver = ExtentManager.getDriver();

		try {
			String locator = get(elementName);
			String expectedPartialText = PropertyUtil.getProperty(textKey);

			WebElement element = driver.findElement(By.xpath(locator));
			String actualText = element.getText();

			String screenshotBase64 = captureScreenshotBase64();
			String screenshotPath = captureScreenshot("PartialText_" + elementName);

			if (actualText.contains(expectedPartialText)) {
				ExtentManager.getTestBase64().pass(
						"Partial text validation passed for element: " + elementName + "<br>Expected to contain: '"
								+ expectedPartialText + "'" + "<br>Actual text: '" + actualText + "'",
						MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64,
								"PartialText_" + elementName + ".png").build());

				ExtentManager.getTestPNG().pass(
						"Partial text validation passed for element: " + elementName + "<br>Expected to contain: '"
								+ expectedPartialText + "'" + "<br>Actual text: '" + actualText + "'",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			} else {
				ExtentManager.getTestBase64().fail(
						"Partial text validation failed for element: " + elementName + "<br>Expected to contain: '"
								+ expectedPartialText + "'" + "<br>Actual text: '" + actualText + "'",
						MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64,
								"PartialText_" + elementName + ".png").build());

				ExtentManager.getTestPNG().fail(
						"Partial text validation failed for element: " + elementName + "<br>Expected to contain: '"
								+ expectedPartialText + "'" + "<br>Actual text: '" + actualText + "'",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

				throw new AssertionError("Partial text not found: " + expectedPartialText);
			}

		} catch (Exception e) {
			String screenshotBase64 = captureScreenshotBase64();
			String screenshotPath = captureScreenshot( "PartialText_Error_" + elementName);

			ExtentManager.getTestBase64()
					.fail("Error during partial text validation for element: " + elementName + "<br>Reason: "
							+ e.getMessage(),
							MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64,
									"PartialText_Error_" + elementName + ".png").build());

			ExtentManager
					.getTestPNG().fail(
							"Error during partial text validation for element: " + elementName + "<br>Reason: "
									+ e.getMessage(),
							MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			throw new RuntimeException("Error validating partial text for element: " + elementName, e);
		}
	}

	public static void validateDynamicText(String xpathTemplate, String dynamicTextValue, String expectedTextKey) {
		WebDriver driver = ExtentManager.getDriver();

		try {
			String locator = get(xpathTemplate);
			String finalXpath = locator.replace("{{text}}", dynamicTextValue);
			WebElement element = driver.findElement(By.xpath(finalXpath));

			String actualText = element.getText();
			String expectedText = PropertyUtil.getProperty(expectedTextKey);

			String screenshotBase64 = captureScreenshotBase64();
			String screenshotPath = captureScreenshot( "DynamicText_" + xpathTemplate);

			if (actualText.contains(expectedText)) {
				ExtentManager.getTestBase64()
						.pass("Dynamic text validation passed." + "<br>Dynamic XPath: " + finalXpath
								+ "<br>Expected (from property): " + expectedText + "<br>Actual: " + actualText,
								MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());

				ExtentManager.getTestPNG()
						.pass("Dynamic text validation passed." + "<br>Dynamic XPath: " + finalXpath
								+ "<br>Expected (from property): " + expectedText + "<br>Actual: " + actualText,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			} else {
				ExtentManager.getTestBase64()
						.fail("Dynamic text validation failed." + "<br>Dynamic XPath: " + finalXpath
								+ "<br>Expected to contain: " + expectedText + "<br>Actual: " + actualText,
								MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());

				ExtentManager.getTestPNG()
						.fail("Dynamic text validation failed." + "<br>Dynamic XPath: " + finalXpath
								+ "<br>Expected to contain: " + expectedText + "<br>Actual: " + actualText,
								MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

				throw new AssertionError("Text not matching at dynamic XPath.");
			}

		} catch (Exception e) {
			String screenshotBase64 = captureScreenshotBase64();
			String screenshotPath = captureScreenshot( "DynamicText_Error_" + xpathTemplate);

			ExtentManager.getTestBase64().fail("Error validating dynamic text.<br>Reason: " + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());

			ExtentManager.getTestPNG().fail("Error validating dynamic text.<br>Reason: " + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			throw new RuntimeException("Error in validateDynamicText", e);
		}
	}

	public static void waitUntilDynamicElementPresent(String xpathKey, String dynamicTextValue) {
		WebDriver driver = ExtentManager.getDriver();

		try {
			String baseXpath = get(xpathKey);
			String finalXpath = baseXpath.replace("{{text}}", dynamicTextValue);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(finalXpath)));

			logToBothReports("✅ Element found with dynamic XPath.<br><b>XPath:</b> " + finalXpath
					+ "<br><b>Dynamic Text:</b> " + dynamicTextValue, "DynamicElementFound_" + dynamicTextValue, driver,
					true);

		} catch (Exception e) {
			logToBothReports(
					"❌ Failed to find dynamic element.<br><b>XPath Key:</b> " + xpathKey + "<br><b>Dynamic Text:</b> "
							+ dynamicTextValue + "<br><b>Error:</b> " + e.getMessage(),
					"DynamicElementNotFound_" + dynamicTextValue, driver, false);
			throw new AssertionError("Dynamic element not found for text: " + dynamicTextValue, e);
		}
	}

	public static void waitUntilDynamicElementNotPresent(String xpathKey, String dynamicTextValue) {
		WebDriver driver = ExtentManager.getDriver();

		try {
			String baseXpath = get(xpathKey);
			String finalXpath = baseXpath.replace("{{text}}", dynamicTextValue);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			boolean isNotPresent = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(finalXpath)));

			if (isNotPresent) {
				logToBothReports(
						"✅ Element with dynamic XPath is not present as expected.<br><b>XPath:</b> " + finalXpath
								+ "<br><b>Dynamic Text:</b> " + dynamicTextValue,
						"DynamicElementNotPresent_" + dynamicTextValue, driver, true);
			} else {
				logToBothReports(
						"❌ Element still visible after wait.<br><b>XPath:</b> " + finalXpath
								+ "<br><b>Dynamic Text:</b> " + dynamicTextValue,
						"DynamicElementStillPresent_" + dynamicTextValue, driver, false);
				throw new AssertionError("Dynamic element still present for text: " + dynamicTextValue);
			}

		} catch (Exception e) {
			logToBothReports(
					"❌ Error while waiting for dynamic element to disappear.<br><b>XPath Key:</b> " + xpathKey
							+ "<br><b>Dynamic Text:</b> " + dynamicTextValue + "<br><b>Error:</b> " + e.getMessage(),
					"DynamicElementNotPresent_Error_" + dynamicTextValue, driver, false);
			throw new RuntimeException("Error waiting for dynamic element to disappear: " + dynamicTextValue, e);
		}
	}

	public static void performDragAndDrop(String source, String target) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String sourceXpath = get(source);
			String targetXpath = get(target);
			WebElement sourceElement = driver.findElement(By.xpath(sourceXpath));
			WebElement targetElement = driver.findElement(By.xpath(targetXpath));

			new Actions(driver).clickAndHold(sourceElement).moveToElement(targetElement).release().build().perform();

			logToBothReports("Drag and drop from '" + source + "' to '" + target + "' successful", "DragDrop", driver,
					true);
		} catch (Exception e) {
			logToBothReports("Failed drag and drop from '" + source + "' to '" + target + "' - " + e.getMessage(),
					"DragDrop", driver, false);
			throw new RuntimeException(e);
		}
	}

	public static String getTextAndStore(String elementName) {
		WebDriver driver = ExtentManager.getDriver();
		String text = "";

		try {
			String locator = get(elementName);
			WebElement element = driver.findElement(By.xpath(locator));

			text = element.getText().trim();

			logToBothReports("✅ Extracted text from element: <b>" + elementName + "</b> → <b>" + text + "</b>",
					"GetText_" + elementName, driver, true);

		} catch (Exception e) {
			logToBothReports("❌ Failed to get text from element: <b>" + elementName + "</b>" + "<br><b>Error:</b> "
					+ e.getMessage(), "GetTextError_" + elementName, driver, false);

			throw new RuntimeException("Error getting text from element: " + elementName, e);
		}

		return text;
	}

	public static void openGeneratedReports() {
		try {
			// Wait a bit to ensure reports are fully generated
			Thread.sleep(5000);

			String base64ReportPath = ExtentManager.getReportPath64();
			String pngReportPath = ExtentManager.getReportPathpng();

			File base64File = new File(base64ReportPath);
			File pngFile = new File(pngReportPath);

			int openedCount = 0;

			if (base64File.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(base64File.toURI());
				logToBothReports("✅ Opened Base64 Extent Report: <b>" + base64ReportPath + "</b>", "OpenReport_Base64",
						ExtentManager.getDriver(), true);
				openedCount++;
			}

			if (pngFile.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(pngFile.toURI());
				logToBothReports("✅ Opened PNG Extent Report: <b>" + pngReportPath + "</b>", "OpenReport_PNG",
						ExtentManager.getDriver(), true);
				openedCount++;
			}

			if (openedCount == 0) {
				logToBothReports("❌ No report files found to open.", "OpenReport_Error", ExtentManager.getDriver(),
						false);
			}

		} catch (Exception e) {
			logToBothReports("❌ Failed to open generated reports.<br><b>Error:</b> " + e.getMessage(),
					"OpenReport_Exception", ExtentManager.getDriver(), false);
			throw new RuntimeException("Error while opening generated reports", e);
		}
	}

	public static void clickIfElementIsPresent(String elementName) {
		WebDriver driver = ExtentManager.getDriver();
		try {
			String locator = get(elementName);
			try {
				WebElement element = driver.findElement(By.xpath(locator));
				if (element.isDisplayed()) {
					element.click();
					logToBothReports("Clicked on element: " + elementName, elementName, driver, true);
					return;
				}
			} catch (NoSuchElementException ignored) {
			}
			logToBothReports("Element not present: " + elementName, elementName, driver, true);
		} catch (Exception e) {
			logToBothReports("Failed in clickIfElementIsPresent: " + elementName + " - " + e.getMessage(), elementName,
					driver, false);
			throw new RuntimeException(e);
		}
	}
}
