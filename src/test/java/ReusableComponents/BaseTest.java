package ReusableComponents;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import Utilities.Reporting;

public class BaseTest extends Genricmethods{
	@BeforeSuite(alwaysRun = true)
	public void m1() {
		Reporting.startReporting();
		System.out.println("Started Test");
	}
	
	@BeforeMethod(alwaysRun = true)
	public void init() {
		browserSetup("chrome");
	}
	
	@AfterMethod(alwaysRun = true)
	public void close() {
		Publicvariables.getDriver().close();
		Publicvariables.getDriver().quit();
		Publicvariables.unload();
	}

	@AfterSuite(alwaysRun = true)
	public void flushReport() {
		Reporting.endReporting();
		System.out.println("END Test");
	}
}
