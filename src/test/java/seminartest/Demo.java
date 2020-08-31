package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* Demo : rsrsup8
 * 1. 세미나 로그인 후 빈화면(초기화면) 확인
 * 2. 오늘(당일)만료 케이스
 * 3. 오늘(당일)만료 케이스, 만료일 이후로 세미나 만들기
 * 4. 이미 만료된 채널. 채널 정보 확인 및 게시 시도
 * 5. 이미 만료된 채널. 만료되지 않은 채널로 변경하여 저장 게시 시도
 */

public class Demo {

	public static String XPATH_SELECTED_CHANNEL = "//div[@class='wrap-channel-option']/span[@class='selected-channel']";
	public static String XPATH_CHANNEL_BTN = "//div[@class='wrap-channel-option']/button";
	public static String XPATH_CHANNEL_EXPIRED_MSG = "//div[@class='wrap-channel-option']/span[@class='seminar-info__expired-account-msg']";
	public static String XPATH_LIST_POST_BTN = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[1]";
	public static String XPATH_LIST_TOOLTIP = ".//span[@class='seminarListItem__buttons__disable-publish-notice']";
	public static String XPATH_INFO_POST_BTN = "//button[@class='btn btn-secondary-light btn-auto actionButton']";
	
	
	public static String MSG_CHANNEL_EXPIRED = "This channel has expired.";
	public static String MSG_LIST_CANNOT_POST = "Seminars cannot be registered after the payment is expired.";
	
	public static String USER_6 = "rsrsup6"; 
	public static String USER_8 = "rsrsup8";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String seminarTitle = "";
	public String seminarID = "";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		
		
		context.setAttribute("webDriver", driver);
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");
	}
	
	// 1. 세미나 로그인 후 빈화면(초기화면) 확인
	@Test(priority = 1)
	public void emptyview() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_6 + "@gmail.com");

		driver.findElement(By.xpath("//div[@class='SeminarEmpty_seminar-empty__1oDy5']/button")).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI)) {
			failMsg = "intro view & create seminar button error. current url : " + driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 2. 오늘(당일)만료 케이스
	@Test(priority = 2)
	public void demoExpiredToday() throws Exception {
		String failMsg = "";
		
		//오늘 만료로 설정
		DBConnection db = new DBConnection();
		db.setDemoExpire(false, USER_8);
		
		//logout(clear)
		driver.get(CommonValues.SERVER_URL + "/logout");
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_8 + "@gmail.com");
		
		//click create seminar(GNB)
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_CHANNEL_EXPIRED_MSG))) {
			failMsg = "1. find channel expired message : " + driver.findElement(By.xpath(XPATH_CHANNEL_EXPIRED_MSG)).getText();
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd hh:mm:ss");
		String title = format2.format(cal.getTime()) + "_demo1";
		
		comm.setCreateSeminar(driver, title, true);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, title);
		comm.postSeminar(driver, seminarID);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	// 3. 오늘(당일)만료 케이스, 만료일 이후로 세미나 만들기
	@Test(priority = 3)
	public void demoExpiredToday2() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		
		//click create seminar(GNB)
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_CHANNEL_EXPIRED_MSG))) {
			failMsg = "1. find channel expired message : " + driver.findElement(By.xpath(XPATH_CHANNEL_EXPIRED_MSG)).getText();
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd hh:mm:ss");
		String title = format2.format(cal.getTime()) + "_demo2";
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, title, false);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, title);
		seminarTitle = title;
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(ListTest.XPATH_LIST_TAB_SAVED)).click();
			Thread.sleep(500);
			
			String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
			
			if (!isElementPresent(By.xpath(listitem))) {
				failMsg = "\n2. can not find seminar in draft tab : seminar title : " + seminarTitle;
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			} else {
				//click post
				WebElement seminarTitle = driver.findElement(By.xpath(listitem));
				WebElement we = seminarTitle.findElement(By.xpath("../../../."));

				if(we.findElement(By.xpath(XPATH_LIST_POST_BTN)).isEnabled()) {
					failMsg = "\n3. post button is enabled ";
				}
				
				Actions actions = new Actions(driver);  
				// mouse hover
				actions.moveToElement(we.findElement(By.xpath(XPATH_LIST_POST_BTN))).perform();
				Thread.sleep(500);
				
				if(!we.findElement(By.xpath(XPATH_LIST_TOOLTIP)).getText().contentEquals(MSG_LIST_CANNOT_POST)) {
					failMsg = "\n4. tooltip message [Expected]" + MSG_LIST_CANNOT_POST 
							+ " [Actual]" + we.findElement(By.xpath(XPATH_LIST_TOOLTIP)).getText();
				}
				
				we.click();
				Thread.sleep(500);
				
				if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
					driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
					Thread.sleep(500);
				}
				
				if(driver.findElement(By.xpath(XPATH_INFO_POST_BTN)).isEnabled()) {
					failMsg = "\n5. POST button is enabled(info view)";
				}
			}
		}
		
		deleteSeminar(seminarID);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 4. 이미 만료된 채널. 채널 정보 확인 및 게시 시도
	@Test(priority = 4)
	public void demoExpired() throws Exception {
		String failMsg = "";
		
		DBConnection db = new DBConnection();
		db.setDemoExpire(true, USER_8);
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		
		//click create seminar(GNB)
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(XPATH_CHANNEL_EXPIRED_MSG))) {
			failMsg = "1. cannot find channel expired message";
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd hh:mm:ss");
		String title = format2.format(cal.getTime()) + "_demo3";
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, title, true);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, title);
		seminarTitle = title;
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(ListTest.XPATH_LIST_TAB_SAVED)).click();
			Thread.sleep(500);
			
			String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
			
			if (!isElementPresent(By.xpath(listitem))) {
				failMsg = "\n2. can not find seminar in draft tab : seminar title : " + seminarTitle;
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			} else {
				//click post
				WebElement seminarTitle = driver.findElement(By.xpath(listitem));
				WebElement we = seminarTitle.findElement(By.xpath("../../../."));

				if(we.findElement(By.xpath(XPATH_LIST_POST_BTN)).isEnabled()) {
					failMsg = "\n3. post button is enabled ";
				}
				
				Actions actions = new Actions(driver);  
				// mouse hover
				actions.moveToElement(we.findElement(By.xpath(XPATH_LIST_POST_BTN))).perform();
				Thread.sleep(100);
				
				if(!we.findElement(By.xpath(XPATH_LIST_TOOLTIP)).getText().contentEquals(MSG_LIST_CANNOT_POST)) {
					failMsg = "\n4. tooltip message [Expected]" + MSG_LIST_CANNOT_POST 
							+ " [Actual]" + we.findElement(By.xpath(XPATH_LIST_TOOLTIP)).getText();
				}
				
				we.click();
				Thread.sleep(500);
				
				if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
					driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
					Thread.sleep(500);
				}
				
				if(driver.findElement(By.xpath(XPATH_INFO_POST_BTN)).isEnabled()) {
					failMsg = "\n5. POST button is enabled(info view)";
				}
			}
		}
		
		deleteSeminar(seminarID);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 5. 이미 만료된 채널. 만료되지 않은 채널로 변경하여 저장 게시 시도
	@Test(priority = 5)
	public void demoChangeChannel1() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		
		//click create seminar(GNB)
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(XPATH_CHANNEL_EXPIRED_MSG))) {
			failMsg = "1. cannot find channel expired message";
		}
		
		CommonValues comm = new CommonValues();
		
		//change channel
		comm.setCreateSeminar_setChannel(driver);
		
		if(isElementPresent(By.xpath(XPATH_CHANNEL_EXPIRED_MSG))) {
			failMsg = "\n2. find channel expired message(rsrsup1 channel) message : "
					+ driver.findElement(By.xpath(XPATH_CHANNEL_EXPIRED_MSG)).getText();
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd hh:mm:ss");
		String title = format2.format(cal.getTime()) + "_demo4";
		
		
		comm.setCreateSeminar(driver, title, false);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, title);
		seminarTitle = title;
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(1000);
			
			driver.findElement(By.xpath(ListTest.XPATH_LIST_TAB_SAVED)).click();
			Thread.sleep(500);
			
			String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
			
			if (!isElementPresent(By.xpath(listitem))) {
				failMsg = "\n3. can not find seminar in draft tab : seminar title : " + seminarTitle;
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			} else {
				//click post
				WebElement seminarTitle = driver.findElement(By.xpath(listitem));
				WebElement we = seminarTitle.findElement(By.xpath("../../../."));

				if(!we.findElement(By.xpath(XPATH_LIST_POST_BTN)).isEnabled()) {
					failMsg = "\n4. post button is disabled ";
				}
				
				we.click();
				Thread.sleep(500);
				
				if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
					driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
					Thread.sleep(500);
				}
				
				if(!driver.findElement(By.xpath(XPATH_INFO_POST_BTN)).isEnabled()) {
					failMsg = "\n5. POST button is disabled(info view)";
				}
				driver.findElement(By.xpath(XPATH_INFO_POST_BTN)).click();
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(1000);
			}
		}
	
		deleteSeminar(seminarID);		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	
	private void deleteSeminar(String seminar) throws InterruptedException {
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminar)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminar);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
	}
	
	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}	

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}
	  
	private boolean findAlert(WebDriver wd, String msg) {
		// attendees
		if (ExpectedConditions.alertIsPresent().apply(wd) == null) {
			return false;

		} else {
			// 알림창이 존재하면 알림창 확인을 누를것
			assertEquals(closeAlertAndGetItsText_webdriver(wd), msg);
			return true;
		}
	}

	private String closeAlertAndGetItsText_webdriver(WebDriver wd) {
		try {
			Alert alert = wd.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

}
