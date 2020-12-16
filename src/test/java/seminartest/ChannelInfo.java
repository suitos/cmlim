package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* ChannelInfo : rsrsup6
 * 1. 채널리스트 확인
 * 2. 채널 마스터 채널 url copy
 * 3. 채널정보 defalut
 * 4. 빈 채널의 세미나 리스트 확인
 * 
 * 10. 채널 리스트 버튼으로 채널리스트 돌아가기
 * 
 * 11. 채널관리 기본 값 확인
 */

public class ChannelInfo {
	
	public static String MSG_LIST_EMPTY_ALL = "View all is not available.";
	public static String MSG_LIST_EMPTY_CURRENT = "Current seminar is not available.";
	public static String MSG_LIST_EMPTY_UPCOMING = "Upcoming seminar is not available.";
	public static String MSG_LIST_EMPTY_PAST = "Past seminar is not available.";
	public static String MSG_MANAGEMENT_DEFAULT_DES = "Register the seminar on My Seminar.";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String testUser = "rsrsup6@gmail.com";
	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	
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
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, testUser);
	  }	
	

	// 1. 채널리스트 확인
	@Test (priority=1)
	public void channellist() throws Exception {
	
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath("//div[@class='content_role']")).getText().contentEquals(CommonValues.CHANNEL_LEVEL_MASTER)) {
			Exception e =  new Exception("channel list sorting. first channel level : " + driver.findElement(By.xpath("//div[@class='content_role']")).getText());
	    	throw e;
		}
	}

	// 2. 채널 마스터 채널 url copy
	@Test (priority=2, dependsOnMethods={"channellist"})
	public void channelInfoMaster() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//span[@class='ricon-copy']")).click();
		Thread.sleep(1000);
		//get channel url 
		String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

		if (!clipboardtxt.equalsIgnoreCase(driver.getCurrentUrl())) {
			failMsg =  "1. not mathced channel url. copy data : " + clipboardtxt;
		}
		
		if (!driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText().contentEquals("Channel URL has been copied.")) {
			failMsg = failMsg + failMsg + "\n 2.Copy toast message error : [Actual] " + driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 채널정보 defalut
	@Test (priority=3, dependsOnMethods={"channellist"})
	public void channelInfoMaster_Default() throws Exception {
		String failMsg = "";
		
		//check default Description
		if(!driver.findElement(By.xpath("//div[@class='Overview_overview__37hJ5']/div/div")).getText().contentEquals("Register the seminar on My Seminar.")) {
			failMsg = "Default Description error : [Actual] " + driver.findElement(By.xpath("//div[@class='Overview_overview__37hJ5']/div/div")).getText();
		}
		
		//seminar list(empty)
		if(isElementPresent(By.xpath("//ul[@class='user-seminar-list-empty']"))) {
			if(!driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText().contentEquals("View all is not available.")) {
				failMsg = failMsg + "\n Seminar List Empty Message Error : [Actual] " + driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText();
			}
			
		} else {
			failMsg = failMsg + "\n Seminar List Error. Not Empty";
		}
		
		//channel title
		String channeltitle = testUser.replace("@gmail.com", "");
		if(!driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']/h2/div")).getText().contentEquals(channeltitle)) {
			failMsg = failMsg + "\n Default Seminar title error : [Actual] " + driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']/h2/div")).getText();
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 4. 빈 채널의 세미나 리스트 확인
	@Test (priority=4, dependsOnMethods={"channellist"})
	public void channelInfoMaster_seminarlist() throws Exception {
		String failMsg = "";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		
		String tabpath = "//div[@class='styles_seminar-list-wrapper__StMWL styles_channel-title__2VOtW']/ul[@class='tab ']/li[%d]";
		
		// 전체 보기 탭 메세지
		if(!driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText().contentEquals(MSG_LIST_EMPTY_ALL)) {
			failMsg = "1. View all empty msg [Expected]" + MSG_LIST_EMPTY_ALL 
					+ " [Actual]" + driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText();
		}
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(String.format(tabpath, 2))));
		
		driver.findElement(By.xpath(String.format(tabpath, 2))).click();
		Thread.sleep(500);
		
		// 진행중 세미나 탭 메세지
		if(!driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText().contentEquals(MSG_LIST_EMPTY_CURRENT)) {
			failMsg = failMsg + "\n 2. Current Seminar empty msg [Expected]" + MSG_LIST_EMPTY_CURRENT 
					+ " [Actual]" + driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText();
		}
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(String.format(tabpath, 3))));
		
		driver.findElement(By.xpath(String.format(tabpath, 3))).click();
		Thread.sleep(500);
		
		// 예정 세미나 탭 메세지
		if(!driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText().contentEquals(MSG_LIST_EMPTY_UPCOMING)) {
			failMsg = failMsg + "\n 3. Current Seminar empty msg [Expected]" + MSG_LIST_EMPTY_UPCOMING 
					+ " [Actual]" + driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText();
		}
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(String.format(tabpath, 4))));
		
		driver.findElement(By.xpath(String.format(tabpath, 4))).click();
		Thread.sleep(500);
		
		// 지난 세미나 탭 메세지
		if(!driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText().contentEquals(MSG_LIST_EMPTY_PAST)) {
			failMsg = failMsg + "\n 4. Past Seminar empty msg [Expected]" + MSG_LIST_EMPTY_PAST 
					+ " [Actual]" + driver.findElement(By.xpath("//ul[@class='user-seminar-list-empty']/div")).getText();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 10. 채널 리스트 버튼 동작 
	@Test (priority=10, dependsOnMethods={"channellist"})
	public void channelInfoMaster_channelList() throws Exception {
		String failMsg = "";
		
		//click channel list button
		driver.findElement(By.xpath("//div[@class='channel-page__wrap']/a")).click();
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath("//div[@class='content_role']")).getText().contentEquals(CommonValues.CHANNEL_LEVEL_MASTER)) {
			Exception e =  new Exception("channel list sorting. first channel level : " + driver.findElement(By.xpath("//div[@class='content_role']")).getText());
	    	throw e;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 11. 채널관리 기본값 확인
	@Test (priority=11, dependsOnMethods={"channellist"})
	public void channelInfoMaster_managementDefault() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/channel/list")) {
			driver.get(CommonValues.SERVER_URL + "/channel/list");
			Thread.sleep(500);
		}
		// channel 정보
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(500);
		
		// channel 관리
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[@class='tab ']/li[2]")).click();
		Thread.sleep(500);
		
		// 채널명(default)
		String channelTitle = testUser.replace("@gmail.com", "");
		if(!driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//div")).getText().contentEquals(channelTitle)) {
			failMsg = "1. defalut channel title error [Expected]" + channelTitle 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//div")).getText();
		}
		
		// 채널 설명(default)
		if(!driver.findElement(By.xpath("//div[@class='Overview_overview__37hJ5']/div/div")).getText().contentEquals(MSG_MANAGEMENT_DEFAULT_DES)) {
			failMsg = failMsg + "\n 2. defalut channel description error [Expected]" + MSG_MANAGEMENT_DEFAULT_DES 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='Overview_overview__37hJ5']/div/div")).getText();
		}
		
		// 채널 공개여부(default : 비공개)
		if(!isElementPresent(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//i[@class='ricon-lock-round']"))) {
			failMsg = failMsg + "\n 3. defalut channel visibility error. lock icon is no visibility.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	
	public void clearAttributeValue(WebElement el) 
	{
		while(!el.getAttribute("value").isEmpty())
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

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
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
