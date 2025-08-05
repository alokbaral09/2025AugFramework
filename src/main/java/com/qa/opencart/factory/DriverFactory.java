package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.errors.AppError;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameWorkException;

public class DriverFactory {

	WebDriver driver;
	Properties prop;
	
   public static String isHighlight;
   
   public static ThreadLocal<WebDriver> tlDriver= new ThreadLocal<WebDriver>();
/**
 * This method is used to init the driver on the basis of given browsername.
 * @param browserName
 * @return it returns driver
 */
	public WebDriver initDriver(Properties prop) {
		
		String browserName=prop.getProperty("browser");
		System.out.println("Browser name is: " + browserName);
		
		isHighlight=prop.getProperty("highlight");
		
		OptionsManager optionsManager= new OptionsManager(prop);

		switch (browserName.toLowerCase().trim()) {
		case "chrome":
			//driver = new ChromeDriver();
			//driver = new ChromeDriver(optionsManager.getChromeOptions());
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			break;
		case "firefox":
			//driver = new FirefoxDriver();
			//driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			break;
		case "edge":
			//driver = new EdgeDriver();
			//driver = new EdgeDriver(optionsManager.getEdgeOptions());
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			break;
		case "safari":
			driver = new SafariDriver();
			tlDriver.set(new SafariDriver());
			break;
			
			default:
				System.out.println(AppError.INVALID_BROWSER_MESG + browserName +" is invalid");
				throw new BrowserException(AppError.INVALID_BROWSER_MESG);
				
		}
		
//		driver.manage().window().maximize();
//		driver.manage().deleteAllCookies();
//		driver.get(prop.getProperty("url"));
		//return driver;
		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		getDriver().get(prop.getProperty("url"));
		return getDriver();

	}
	/**
	 * This method is returning the driver with ThreadLocal
	 * @return
	 */
	public static WebDriver getDriver() {
		return tlDriver.get();
	}
	
	
	/**
	 * This method is used to init the properties from the config file
	 * @return
	 */
	
	//mvn clean innstall -Denvv="qa"
	public Properties initProp()  {
		
		prop= new Properties();
		FileInputStream ip=null;
		
		//for maven use code
		String envName=System.getProperty("env");
		System.out.println("running test on env:"+envName);
		try {
		if(envName==null) {
			System.out.println("env is null....hence running tests in QA env");
			ip= new FileInputStream("./src/test/resources/config/qa.config.properties");
		}else {
			switch (envName.toLowerCase().trim()) {
			case "qa":
				ip= new FileInputStream("./src/test/resources/config/qa.config.properties");
				break;
			case "dev":
				ip= new FileInputStream("./src/test/resources/config/dev.config.properties");
				break;
			case "stage":
				ip= new FileInputStream("./src/test/resources/config/stage.config.properties");
				break;
			case "uat":
				ip= new FileInputStream("./src/test/resources/config/uat.config.properties");
				break;
			case "prod":
				ip= new FileInputStream("./src/test/resources/config/config.properties");
					break;
			default:
				System.out.println("please pass right env name.."+envName);
				throw new FrameWorkException("INVALID ENV NAME");
				
			}
		}
		prop.load(ip);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * take screenshot
	 */

	public static String getScreenshot(String methodName) {
		
		File srcFile=((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path=System.getProperty("usr.dir") + "/ screehshot/"+methodName+"_"+System.currentTimeMillis()+".png";
		File destination= new File(path);
		
		try {
			FileHandler.copy(srcFile,destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
}
