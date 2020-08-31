package admin;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CommonValues {
	public static final String WEB_CHROME_DRIVER_PATH = System.getProperty("user.dir") + "/driver/chromedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_PATH = System.getProperty("user.dir") + "/driver/geckodriver.exe";
	public static final String WEB_FIREFOX_DRIVER_LINUX_PATH = "/tools/webdriver/geckodriver";
	
	public static String ADMIN_URL = "https://alphaadmin.remoteseminar.com/#";
	
	/*user
	 * 
	 */

	public static String USER_SYSADMIN="rsp@rsupport.com";
	public static String USER_PARTNER_KR="testkr03@rsupport.com";
	public static String USER_PARTNER_1="patest01@rsupport.com";
	public static String USER_PARTNER_1_NAME="seleniumTest";
	
	public static String USER_PW="123456789";
	
	public static String[] SPECIAL_CHARACTER = {"!", "*", "'", "(", ")" , ";", ":", "@", "&", "=", "+", "$", ",", "/", "?", "#", "[", "]"};
	public static String CHARACTER_20 = "aaaaaaaaaaaaaaaaaaaa";
	public static String NUMBER_20 = "11111111111111111111";
	
	//URL
	public static String URL_PARTNERLIST = "/partner/list";
	public static String URL_PARTNEREDIT = "/partner/edit";
	public static String URL_PARTNERNEW = "/partner/new";
	public static String URL_USERLIST = "/user/list";
	public static String URL_USERINFO = "/user/info?email=";
	public static String URL_USEREDIT = "/user/edit?email=";
	public static String URL_USERNEW = "/user/new";
	public static String URL_CHANNELLIST = "/channel/list";
	public static String URL_SEMINARLIST = "/seminar/list";
	public static String URL_SEMINARINFO = "/seminar/info";

	public static String URL_CAHNNELNEW = "/channel/new";
	
	public static String XPATH_LOGIN_EMAIL = "//input[@id='email']";
	public static String XPATH_LOGIN_PW = "//input[@id='password']";
	public static String XPATH_LOGIN_BUTTON = "//button[@type='submit']";
	
	public static String XPATH_USER_PROFILE_BTN = "//button[@id='btn-profile']";
	public static String XPATH_USER_PROFILE_LOGOUT_BTN = "//div[@class='ant-dropdown ant-dropdown-placement-bottomLeft ']//span[@class='anticon anticon-logout']";
	public static String XPATH_LIST_ROW_ITEM = "//tr[@class='ant-table-row ant-table-row-level-0 custom-row-link']";
	public static String XPATH_LIST_ROW_CELL = "./td[@class='ant-table-cell']";
	public static String XPATH_LIST_SEARCH_BTN = "//button[@id='search']";
	public static String XPATH_LIST_SEARCH_INPUTBOX = "//input[@id='keyword']";
	public static String XPATH_LIST_REGISTER_BTN = "//button[@class='ant-btn ant-btn-sub ant-btn-sm']";
	
	public static String XPATH_MENU_PARTNER = "//ul[@role='menu']/li[1]";
	public static String XPATH_MENU_USER = "//ul[@role='menu']/li[2]";
	public static String XPATH_MENU_CHANNEL = "//ul[@role='menu']/li[3]";
	public static String XPATH_MENU_SEMINAR = "//ul[@role='menu']/li[4]";
	
	
	public void setDriverProperty(String browser) {
		String os = System.getProperty("os.name").toLowerCase();
		if (browser.contains("Chrome")) {
			if (os.contains("mac")) {
				 String path = System.getenv("DRIVER_HOME") + "/chromedriver";
				 System.setProperty("webdriver.chrome.driver", path);
			} else {
				System.setProperty("webdriver.chrome.driver", CommonValues.WEB_CHROME_DRIVER_PATH);
			}
		} else {
			System.setProperty("webdriver.gecko.driver", CommonValues.WEB_FIREFOX_DRIVER_PATH);
		}
	}
	
	public WebDriver setDriver(WebDriver driver, String browser, String lang) {
	
		if (browser.contains("Chrome")) {
			ChromeOptions options = new ChromeOptions();
		    options.addArguments(lang);
		    //options.addArguments("headless");
		    options.addArguments("disable-gpu");
		    
			driver = new ChromeDriver(options);
		} else {
			driver = new FirefoxDriver();
		}
		
		if(browser.contains("_test")){
			seminartest.CommonValues.FOR_JENKINS = false;
		} 
		
		driver.manage().window().maximize();
		return driver;
	}
	
	public void logintadmin(WebDriver driver, String user) throws Exception {
		logintadmin(driver, user, USER_PW);
	}
	
	public void logintadmin(WebDriver driver, String user, String pw) throws Exception {
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL) 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + "/")) {
			driver.get(CommonValues.ADMIN_URL);
		}

	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    driver.findElement(By.xpath(XPATH_LOGIN_EMAIL)).clear();
	    selectAll( driver.findElement(By.xpath(XPATH_LOGIN_EMAIL)));
	    driver.findElement(By.xpath(XPATH_LOGIN_EMAIL)).sendKeys(Keys.BACK_SPACE);
	    driver.findElement(By.xpath(XPATH_LOGIN_EMAIL)).sendKeys(user);
	    driver.findElement(By.xpath(XPATH_LOGIN_PW)).clear();
	    selectAll( driver.findElement(By.xpath(XPATH_LOGIN_PW)));
	    driver.findElement(By.xpath(XPATH_LOGIN_PW)).sendKeys(Keys.BACK_SPACE);
	    driver.findElement(By.xpath(XPATH_LOGIN_PW)).sendKeys(pw);
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    Thread.sleep(1000); 

	    String listurl = ADMIN_URL + URL_PARTNERLIST;

	    if(!driver.getCurrentUrl().contentEquals(listurl) )
	    {
	    	Exception e =  new Exception("login fail!!! : " + driver.getCurrentUrl());
	    	throw e;
	    }
	}
	
	public void logoutaddmin(WebDriver driver) throws InterruptedException {
		
		driver.findElement(By.xpath(XPATH_USER_PROFILE_BTN)).click();
		Thread.sleep(100);
		
		driver.findElement(By.xpath(XPATH_USER_PROFILE_LOGOUT_BTN)).click();
		Thread.sleep(500);
	}
	
	public void setCalender(WebDriver e) {
		String xpath_startDate = "//input[@placeholder='Start date']"; 
		
		selectAll(e.findElement(By.xpath(xpath_startDate)));
		e.findElement(By.xpath(xpath_startDate)).sendKeys("2019/01/01");
		
		e.findElement(By.xpath(XPATH_LIST_SEARCH_BTN)).click();
	}

	public void selectAll(WebElement e) {

		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			e.click();
			JavascriptExecutor executor = (JavascriptExecutor) getDriver(e);
			executor.executeScript("arguments[0].select();", e);
		} else {
			e.sendKeys(Keys.CONTROL,"a");
		}

	}

	public static WebDriver getDriver(SearchContext context) {
	    if (context instanceof WebDriver) {
	        return (WebDriver) context;
	    } else if (context instanceof WrapsDriver) {
	        return ((WrapsDriver) context).getWrappedDriver();
	    } else if (context instanceof WrapsElement) {
	        return ((WrapsDriver) ((WrapsElement) context).getWrappedElement()).getWrappedDriver();
	    }
	    throw new IllegalArgumentException("Driver could not be extracted from the specified context");
	}
}
