package Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import ReusableComponents.Genricmethods;
import ReusableComponents.Publicvariables;

public class Reporting implements ITestListener{
	public static Logger log;
	public static ExtentReports extent;
	public static ExtentSparkReporter htmlReporter;
	public static String filepath = System.getProperty("user.dir") + "/ExtentReports/"+Genricmethods.currentTimeStamp()+".html";
	public static ThreadLocal<ExtentTest>test = new ThreadLocal<>();
	static String methodname;
	static String stDate = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	private static Set<String> addedGroups = new HashSet<>();

	public static ExtentReports GetExtent() throws IOException {
		if (extent!= null) 
			return extent;//Avoid creating new instance of html file
			extent = new ExtentReports();
			extent.attachReporter(getHtmlReporter());
			extent.setSystemInfo("Application", "Sample APP");
			extent.setSystemInfo("Environment", "QA");
		return extent;

	}

	public static ExtentSparkReporter getHtmlReporter() throws IOException {
		htmlReporter = new ExtentSparkReporter(Reporting.filepath);
		htmlReporter.config().setDocumentTitle("AutomationReport");
		htmlReporter.config().setReportName(System.getProperty("user.name"));
		htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/ExtentReports/extent-config.xml");
		return htmlReporter;
	}

	public static void startLogging(String logname) {
		Reporting.createTest(logname, "Verify Functionality as "+testCaseName());
		System.setProperty("htmlpath", System.getProperty("user.dir") + "\\test-output\\LogFiles\\"+ logname+stDate + "_application.html");
		System.setProperty("logpath", System.getProperty("user.dir") + "\\test-output\\LogFiles\\"+ logname+stDate + "_logifle.log");
		log = Logger.getLogger(logname);
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/test-output/LogFiles/Log4j.properties");
	}

	public static void startReporting() {
		try {
			extent = Reporting.GetExtent();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void endReporting() {
		extent.flush();
		extent = null;
		htmlReporter = null;
	}
	public static ExtentTest createTest(String name , String description) {
		if(Objects.isNull(getExtentTest())) {
			setExtentTest(extent.createTest(name,description));
		}
		return getExtentTest();
		
	}

	public static String testCaseName() {
		if(Objects.nonNull(Publicvariables.getTestName)) {
			methodname = Publicvariables.getTestName;			
		}
		return methodname;
	}

	public static void setExtentTest(ExtentTest extest) {
		test.set(extest);
	}

	public static ExtentTest getExtentTest() {
		return test.get();
	}

	public void onTestSuccess(ITestResult result) {
		getExtentTest().assignCategory(result.getMethod().getGroups());
		getExtentTest().log(Status.PASS, MarkupHelper.createLabel("Test Passed Successfully!"+ "for screenshot path:"
				+ "<a style=\"text-decoration:none;color:red;href="+getScreenshots()+">Click Here</a>", ExtentColor.GREEN));
	}
	
	public void onTestFailure(ITestResult result) {
		getExtentTest().assignCategory(result.getMethod().getGroups());
		getExtentTest().log(Status.FAIL,
				MarkupHelper.createLabel("Test Execution Failed!" + "for screenshot path:"
						+ "<a style=\"text-decoration:none;color:red;href=" + getScreenshots() + ">Click Here</a>",
						ExtentColor.RED));
	}
	
	public void onTestStart(ITestResult result) {
		setExtentTest(extent.createTest(result.getMethod().getMethodName()));
		methodname = result.getMethod().getMethodName();
		startLogging(methodname);
		log = Logger.getLogger(methodname);
		java.util.List<String> includedGroups = result.getTestContext().getCurrentXmlTest().getIncludedGroups();
		if (!includedGroups.isEmpty()) {
			for (String group : includedGroups) {
				if (!addedGroups.contains(group)) {
					extent.setSystemInfo("Groups", group);
					addedGroups.add(group);
				}
			}
		}
	}

	public static String getScreenshots() {
		String screenpath =null;
		try {
			TakesScreenshot ts = null;
			ts=(TakesScreenshot) Publicvariables.getDriver();
			File source=ts.getScreenshotAs(OutputType.FILE);
			String screenshotpath = System.getProperty("user.dir") + "/ExtentReports/Screenshots/" + testCaseName()
					+ "/" + stDate + "/" + stDate + ".png";
			screenpath ="./Screenshots"+testCaseName()+ "/" + stDate + "/" + stDate + ".png";
			File Desti = new File(screenshotpath);
			FileUtils.copyFile(source, Desti);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return screenpath;
	}
	
	public static void writeToReport(String status, String desc) {
		switch (status.toUpperCase()) {
		case "PASS":
			try {
				String sceenshotPath = getScreenshots();
				getExtentTest().log(Status.PASS, desc.toUpperCase()+"Screenshot path:"+"<a href="+sceenshotPath+">Click Here</a");
			} catch (Exception e) {
				// TODO: handle exception
				getExtentTest().log(Status.PASS, desc.toUpperCase());
			}
			break;
		case "INFO":
			try {
				getExtentTest().info(desc);
				log.info(desc.toUpperCase());
			} catch (Exception e) {
				getExtentTest().info(desc);
			}
		case "FAIL":
			try {
				String sceenshotPath = getScreenshots();
				getExtentTest().log(Status.FAIL, desc.toUpperCase()+"Screenshot path:"+"<a href="+sceenshotPath+">Click Here</a");
			} catch (Exception e) {
				// TODO: handle exception
			}
		default:
			break;
		}
	}
	
}
