package com.guru99.tests;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;
import com.guru99.pages.LoginPage;

import commonLibs.implementation.CommonDriver;
import commonLibs.utils.ConfigUtils;
import commonLibs.utils.ReportUtils;
import commonLibs.utils.ScreenshotUtils;

public class BaseTests {
	
	CommonDriver cmnDriver;
	String url;
	
	WebDriver driver;
	
	LoginPage loginpage;
	
	String configFileName;
	String currentWorkingDirectory;
	
	Properties configPropety;
	
	String reportFilename;
	ReportUtils reportUtils;
	
	ScreenshotUtils screenshot;
	
	@BeforeSuite
	public void preSetup() throws Exception{
		currentWorkingDirectory = System.getProperty("user.dir");
		
		configFileName = currentWorkingDirectory + "/config/config.properties";
		reportFilename = currentWorkingDirectory + "/reports/guru99TestReport.html";
		
		configPropety = ConfigUtils.readProperties(configFileName);
		
		reportUtils = new ReportUtils(reportFilename);
	}
	
	@BeforeClass
	public void setup() throws Exception{
		
		url = configPropety.getProperty("baseUrl");
		
		String browserType = configPropety.getProperty("browserType");
		
		cmnDriver = new CommonDriver(browserType);
		
		driver = cmnDriver.getDriver();
		
		loginpage = new LoginPage(driver);
		
		screenshot = new ScreenshotUtils(driver);
		
		cmnDriver.navigateToUrl(url);
	}
	
	@AfterMethod
	public void postTestAction(ITestResult result) throws Exception {
		
		String testcasename = result.getName();
		long executionTime = System.currentTimeMillis();
		
		String screenshotFilename = currentWorkingDirectory +
				"/screenshots/"
				+ testcasename + executionTime +".jpeg";
		
		if(result.getStatus() == ITestResult.FAILURE) {
			
			reportUtils.addTestLog(Status.FAIL, "One or more steps failed");
			
			screenshot.captureAndSaveScreenshot(screenshotFilename);
			
			reportUtils.attachScreenshotToReport(screenshotFilename);
		}
		
	}
	
	@AfterClass
	public void tearDown() {
		cmnDriver.closeAllBrowser();
	}
	
	@AfterSuite
	public void postTeatdown() {
		reportUtils.flushReport();
	}

}
