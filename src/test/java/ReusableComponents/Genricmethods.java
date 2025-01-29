package ReusableComponents;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import Utilities.Reporting;

public class Genricmethods {
	static WebDriver driver;

	public void browserSetup(String browser) {
		switch (browser.toUpperCase()) {
		case "CHROME":
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\ChromeDriver\\chromedriver.exe");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--ignore--ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--disable-web-security");
			options.addArguments("--disablel-infobbars");
			options.addArguments("force-device-scale-factor=0.98");
			options.addArguments("high-dpi-support=0.98");
			options.addArguments("--disable-gpu");
			//options.addArguments("--headless");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			Publicvariables.setDriver(new ChromeDriver(options));
			Publicvariables.getDriver().manage().window().maximize();
			Publicvariables.getDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			break;
		case "IE":
			System.out.println("IE Browser");
		}

	}

	public void launchUrl(String url) {
		try {
			Publicvariables.getDriver().get(url);
			System.out.println("Able to navigate to url");
			Reporting.writeToReport("PASS", "URL correct");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Not able to navigate to url "+e.getMessage());
		}
	}
	public String getConfigPropValue(String propPram) throws IOException {
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\resources\\configuration.properties");
		prop.load(fs);
		return prop.getProperty(propPram);

	}
	
	public static String currentTimeStamp() {
			String timestamp = new SimpleDateFormat("MMddHHmm").format(new Date());
			return timestamp;
	}
	
}
