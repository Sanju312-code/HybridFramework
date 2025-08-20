package com.reporting;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;

import com.Utils.PropertyUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static ExtentReports extentBase64;
    private static ExtentReports extentPNG;

    private static ThreadLocal<ExtentTest> testBase64 = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> testPNG = new ThreadLocal<>();

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static String base64ReportPath;
    private static String pngReportPath;

    private static String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    // ===== New Variables for Execution Tracking =====
    private static Date executionStartTime;
    private static Date executionEndTime;
    private static int passedCount = 0;
    private static int failedCount = 0;

    // ==== REPORT PATHS ====
    private static String base64ReportName() {
        String reportName = PropertyUtil.getProperty("reportname") + "_Base64";
        return System.getProperty("user.dir") + "/Reports/Base64/" + reportName + "_" + timestamp + ".html";
    }

    private static String pngReportName() {
        String reportName = PropertyUtil.getProperty("reportname") + "_PNG";
        return System.getProperty("user.dir") + "/Reports/PNG/" + reportName + "_" + timestamp + ".html";
    }

   
    // ==== GET EXTENT REPORTS ====
    public static ExtentReports getExtentBase64() throws IOException {
        if (extentBase64 == null) {
            base64ReportPath = base64ReportName();
            ExtentSparkReporter spark = new ExtentSparkReporter(base64ReportPath);
            spark.config().setTimelineEnabled(true);
            extentBase64 = new ExtentReports();
            extentBase64.attachReporter(spark);

            // Record start time when first report is initialized
            if (executionStartTime == null) {
                executionStartTime = new Date();
            }
        }
        return extentBase64;
    }

    public static ExtentReports getExtentPNG() {
        if (extentPNG == null) {
            pngReportPath = pngReportName();
            ExtentSparkReporter spark = new ExtentSparkReporter(pngReportPath);
            extentPNG = new ExtentReports();
            extentPNG.attachReporter(spark);
        }
        return extentPNG;
    }

    // ==== SET & GET TEST ====
    public static void setTestBase64(ExtentTest extentTest) {
        testBase64.set(extentTest);
    }

    public static ExtentTest getTestBase64() {
        return testBase64.get();
    }

    public static void setTestPNG(ExtentTest extentTest) {
        testPNG.set(extentTest);
    }

    public static ExtentTest getTestPNG() {
        return testPNG.get();
    }

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void unload() {
        testBase64.remove();
        testPNG.remove();
        driver.remove();
    }

    public static String getReportPathpng() {
        return pngReportPath;
    }

    public static String getReportPath64() {
        return base64ReportPath;
    }

    // ====== NEW METHODS for Execution Tracking ======

    public static void markTestPassed() {
        passedCount++;
    }

    public static void markTestFailed() {
        failedCount++;
    }

    public static int getPassedCount() {
        return passedCount;
    }

    public static int getFailedCount() {
        return failedCount;
    }

    public static void markExecutionEnd() {
        executionEndTime = new Date();
    }

    public static String getExecutionStartTime() {
        return executionStartTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executionStartTime) : "";
    }

    public static String getExecutionEndTime() {
        return executionEndTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executionEndTime) : "";
    }

    public static String getExecutionDuration() {
        if (executionStartTime != null && executionEndTime != null) {
            long diff = executionEndTime.getTime() - executionStartTime.getTime();
            long seconds = (diff / 1000) % 60;
            long minutes = (diff / (1000 * 60)) % 60;
            long hours = diff / (1000 * 60 * 60);
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return "";
    }


}
