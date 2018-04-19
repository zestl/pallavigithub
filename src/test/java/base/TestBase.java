package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import utilities.DbManager;
import utilities.ExcelReader;
import utilities.MonitoringMail;

public class TestBase {

	/*
	 * WebDriver
	 * Logs
	 * Database -DBManager.java
	 * Mail - MonitoringMail.java
	 * TestNG
	 * Properties
	 * ReportNG
	 * Screenshot util
	 * Excel
	 * Keywords - click,type ,select
	 */
	
	public static WebDriver driver;
	public static Logger Log = Logger.getLogger("devpinoyeLogger");
	public static MonitoringMail mail = new MonitoringMail();
	public static Properties OR = new Properties();
	public static Properties config = new Properties();
	public static FileInputStream fis;
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir")+"\\src\\test\\resources\\excel\\testdata.xlsx"); 
	public static WebDriverWait wait; 
	
	@BeforeSuite
	public void setUp(){
		
		if(driver == null) {
			Log.debug("test execution started !!!");
			try {
				fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
			} catch (IOException e) {
				Log.debug("config file loaded !!!");
				e.printStackTrace();
			}
			
			try {
				fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
			} catch (IOException e) {
				Log.debug("config file loaded !!!");
				e.printStackTrace();
			}
			
			if(config.getProperty("browser").equals("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\geckodriver.exe");
				driver = new FirefoxDriver();
				Log.debug("Firefox launched !!!");
			}else if(config.getProperty("browser").equals("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
				
				driver = new ChromeDriver();
				Log.debug("Chrome launched !!!");
			}else if(config.getProperty("browser").equals("ie")) {
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\IEDriverServer.exe");				
				driver = new InternetExplorerDriver();
                Log.debug("ie launched !!!");
			}
			
			driver.get(config.getProperty("testsiteurl"));
			Log.debug("Navigated to :"+config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),TimeUnit.SECONDS);
		   
			try {
				DbManager.setMysqlDbConnection();
				Log.debug("DB connection established !!!");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		wait = new WebDriverWait(driver, Integer.parseInt(config.getProperty("explicit.wait")));
		}
		
	}
	
	@AfterSuite
	public void tearDown() {
		
		driver.quit();
		Log.debug("Test execution complted");
		
		
	}
	}
