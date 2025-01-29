package ReusableComponents;

import org.openqa.selenium.WebDriver;

public class Publicvariables {
	private static WebDriver dr;
	public static String getTestName;

	private static ThreadLocal<WebDriver> dr1 = new ThreadLocal<>();
	public static WebDriver getDriver() {
		return dr1.get();
	}
	
	public static void setDriver(WebDriver driver) {
		dr1.set(driver);
	}
	
	public static void unload() {
		dr1.remove();
	}

	public static WebDriver getDr() {
		return dr;
	}

	public static void setDr(WebDriver dr) {
		Publicvariables.dr = dr;
	}
}
