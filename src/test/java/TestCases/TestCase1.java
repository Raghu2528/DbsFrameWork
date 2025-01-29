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
		String url = getConfigPropValue("AppURL");
		launchUrl(url);
		clickOnSales();
	}
}
 