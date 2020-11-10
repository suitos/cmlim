package admin;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
	
	public static String ADMIN_URL = "https://alphaadmin.remoteseminar.com";
	
	/*user
	 * 
	 */

	public static String USER_SYSADMIN="rsp@rsupport.com";
	public static String USER_PARTNER_KR="testkr03@rsupport.com";
	public static String USER_PARTNER_KR2 ="testkr02@rsupport.com";
	public static String USER_PARTNER_1="patest01@rsupport.com";
	public static String USER_PARTNER_1_NAME="seleniumTest";
	
	public static String USER_PW="123456789";
	
	public static String[] SPECIAL_CHARACTER = {"!", "*", "'", "(", ")" , ";", ":", "@", "&", "=", "+", "$", ",", "/", "?", "#", "[", "]"};
	public static String CHARACTER_20 = "aaaaaaaaaaaaaaaaaaaa";
	public static String NUMBER_20 = "11111111111111111111";
	public static String NUMBER_10 = "1234567890";
	public static String SPECIAL_10 = "AaBb123!@#";
	
	public static String WRONG_EMAIL = "abcd123@rsupport.com";
	
	//URL
	public static String URL_PARTNERLIST = "/partner/list";
	public static String URL_PARTNEREDIT = "/partner/edit";
	public static String URL_PARTNERNEW = "/partner/new";
	public static String URL_USERLIST = "/user/list";
	public static String URL_USERINFO = "/user/info?email=";
	public static String URL_USEREDIT = "/user/edit?email=";
	public static String URL_USERNEW = "/user/new";
	public static String URL_CHANNELLIST = "/channel/list";
	public static String URL_CHANNELINFO = "/channel/info/";
	public static String URL_SEMINARLIST = "/seminar/list";
	public static String URL_SEMINARINFO = "/seminar/info";
	public static String URL_CHANNELNEW = "/channel/new";
	
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
	public static String XPATH_LIST_CHANNELREGISTER_BTN = "//button[@class='ant-btn ant-btn-primary ant-btn-sm']";
	public static String XPATH_LIST_SEARCHED_ITEM_COUNT = "//section[@id='index-page-wrap']//span[@class='total']";
	public static String XPATH_LIST_PAGING = "//ul[@class='ant-pagination ant-table-pagination ant-table-pagination-right']/li";
	public static String XPATH_LIST_ROWS = "//div[@class='ant-radio-group ant-radio-group-outline list-view-size']/label";
	public static String XPATH_LIST_DATERADIO = "//div[@id='dateRadio']/label";
	public static String XPATH_LIST_DATEPICKER_START = "//input[@placeholder='Start date']";
	public static String XPATH_LIST_DATEPICKER_END = "//input[@placeholder='End date']";
	
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
	
	public void setCalender(WebDriver e) throws InterruptedException {
		
		selectAll(e.findElement(By.xpath(XPATH_LIST_DATEPICKER_START)));
		e.findElement(By.xpath(XPATH_LIST_DATEPICKER_START)).sendKeys("2019/01/01");
		Thread.sleep(500);
		
		e.findElement(By.xpath(XPATH_LIST_SEARCH_BTN)).click();
		e.findElement(By.xpath(XPATH_LIST_SEARCH_BTN)).sendKeys(Keys.ENTER);
	}
	
	public String checkSearchedItemInRow(int num, WebDriver wd, Calendar start, Calendar end) throws ParseException {
		String ret = "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		
		String failForm = "\n%d-%d. %s. [Expected]%s [Actual]%s";
		String searchDate = format.format(start.getTime()) + "~" + format.format(end.getTime());
		
		if(!wd.findElement(By.xpath(XPATH_LIST_DATEPICKER_START)).getAttribute("value").contentEquals(format.format(start.getTime()))) {
			ret = ret + String.format(failForm, num, 1, "check last month, start date error"
					, format.format(start.getTime()), wd.findElement(By.xpath(XPATH_LIST_DATEPICKER_START)).getAttribute("value"));
		}
		if(!wd.findElement(By.xpath(XPATH_LIST_DATEPICKER_END)).getAttribute("value").contentEquals(format.format(end.getTime()))) {
			ret = ret + String.format(failForm, num, 2, "check last month, end date error"
					, format.format(start.getTime()), wd.findElement(By.xpath(XPATH_LIST_DATEPICKER_START)).getAttribute("value"));
		}
		List<WebElement> rows = wd.findElements(By.xpath(XPATH_LIST_ROW_ITEM));
		for (WebElement webElement : rows) {
			Date date = format.parse(webElement.findElement(By.xpath(XPATH_LIST_ROW_CELL+"[1]")).getText());
		
			if(!date.before(end.getTime()) || !date.after(start.getTime())) {
				ret = ret + String.format(failForm, num, 3, "searched date error"
						, searchDate, format.parse(webElement.findElement(By.xpath(XPATH_LIST_ROW_CELL+"[1]")).getText()));
			}
		}		
		
		return ret;
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
	
	public void insertData(WebElement e, int N, String data) throws Exception {
		switch(N) {
		case 1:
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			e.sendKeys(data);
		break;
		case 2:
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			for (int i=0; i<4; i++){
			e.sendKeys(data);}
		break;
		case 3:
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			for (int i=0; i<51; i++){
			e.sendKeys(data);}
		break;
		}
	}
	
	public String Excelpath(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		int num = 1;

		if (os.contains("windows")) {
			path = "C:\\Users\\admin\\Downloads\\" + filename + ".xlsx";

			File file = new File(path);

			if (!file.exists()) {
				while (true) {
					num++;
					path = "C:\\Users\\admin\\Downloads\\" + filename + " (" + num + ").xlsx";
					File file2 = new File(path);
					if (file2.exists())
						break;
					}
			} else {
			path = System.getProperty("user.home") + "/Downloads/" + filename + ".xlsx";
			}

		}
		return path;
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
