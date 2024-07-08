package com.mystore.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.ietf.jgss.Oid;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.beust.jcommander.Parameter;
import com.mystore.actiondriver.Action;
import com.mystore.utility.ExtentManager;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Yashas An: BaseClass is used to load the config file and Initialize
 *         WebDriver
 * 
 */
public class BaseClass {
	public static Properties prop;

	// Declare ThreadLocal Driver
	// public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

	public static WebDriver driver;

	// loadConfig method is to load the configuration
	@BeforeSuite(groups = { "Smoke", "Sanity", "Regression" })
	public void loadConfig() {
		ExtentManager.setExtent();
		DOMConfigurator.configure("log4j.xml");

		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "\\Configuration\\config.properties");
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	public void launchApp(String browserName) {
		//String browserNamee = prop.getProperty("browser");
		if (browserName.equalsIgnoreCase("Chrome")) {
			String basedir = prop.getProperty("basedir");
			String chromeDriverPath = basedir + "\\drivers\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			// WebDriverManager.chromedriver().setup();
			// Set Browser to ThreadLocalMap
			driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("IE")) {
			String basedir = prop.getProperty("basedir");
			String edgeDriverPath = basedir + "\\drivers\\msedgedriver.exe";
			System.setProperty("webdriver.chrome.driver", edgeDriverPath);
			// WebDriverManager.iedriver().setup();
			// driver.set(new InternetExplorerDriver());
			driver = new EdgeDriver();
		}
		// Maximize the screen
		driver.manage().window().maximize();
		// Delete all the cookies
		driver.manage().deleteAllCookies();
		// Implicit TimeOuts
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(prop.getProperty("implicitWait")), TimeUnit.SECONDS);
		// PageLoad TimeOuts
		driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(prop.getProperty("pageLoadTimeOut")),
				TimeUnit.SECONDS);
		// Launching the URL
		driver.get(prop.getProperty("url"));
	}

	@AfterSuite(groups = { "Smoke", "Regression", "Sanity" })
	public void afterSuite() {
		ExtentManager.endReport();
	}
}
