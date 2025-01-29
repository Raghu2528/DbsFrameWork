package TestCases;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.Test;

import ReusableComponents.BaseTest;
import ReusableComponents.Publicvariables;
import Utilities.Reporting;

public class TestCase1 extends BaseTest{
	

	@Test(groups ="Regression")
	public void testCase1(Method m) throws IOException {
		System.out.println("TestCaseStarted" +m.getName());
		String url = getConfigPropValue("AppURL");
		System.out.println(url);
		launchUrl(url);
		Reporting.writeToReport("PASS", "Login");
	}
}
 