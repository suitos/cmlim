package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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

public class ChannelURL {
	
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
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
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	  }	
	

	
	@Test (priority=1)
	public void channelManagementView() throws Exception {
	
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL)) {
			Exception e =  new Exception("not channel management view. current url : " + driver.getCurrentUrl());
	    	throw e;
		}
	}
	
	@Test (priority=2, dependsOnMethods={"channelManagementView"})
	public void channelManagement_urlpopup() throws Exception {
		
		String failMsg = "";
		
		//click Change URL button
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
		// placeholder : Enter the ID value.
		if (!driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).getAttribute("placeholder")
				.contentEquals("Enter the ID value.")) {
			failMsg = "Change URL popup placeholder : [Actual] "
					+ driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).getAttribute("placeholder");
		}
		String channel = CommonValues.SERVER_URL + "/channel/view/";
		channel = channel.replace("https://", "");
		//default url guide : server url + /channel/view/
		if (!driver.findElement(By.xpath("//span[@class='url']")).getText().contentEquals(channel)) {
			failMsg = "channel url guide error : [Actual] "
					+ driver.findElement(By.xpath("//span[@class='url']")).getText();
		}
		
		//button check
		if(driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n Selected button is enabled!(ID is empty)";
		}
		
		//click x button
		//driver.findElement(By.xpath("//i[@class='ricon-close']")).click();

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test (priority=3, dependsOnMethods={"channelManagementView"})
	public void channelManagement_invalidURL() throws Exception {
		String failMsg = "";
		
		//click Change URL button
		//driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']")).click();
		//Thread.sleep(500);
		
		for(int i = 0 ; i < CommonValues.CHANNEL_ID_INVALID.length ; i++) {
			driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
			driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
			driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).sendKeys(CommonValues.CHANNEL_ID_INVALID[i]);
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath("//div[@class='desc invalid']")).getText().contentEquals("URL must be less than 10 alphanumeric characters.")) {
				failMsg = failMsg + "\n invalid url Message : [Actual] " + driver.findElement(By.xpath("//div[@class='desc invalid']")).getText();
			}
			
			// button check
			if (driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).isEnabled()) {
				failMsg = failMsg + "\n Selected button is enabled!(invalid ID)";
			}
		}
		
		//long id(input 20)
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).sendKeys(CommonValues.TWENTY_A);
		Thread.sleep(500);
		
		if(driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).getText().length() >= 10) {
			failMsg = failMsg + "chanell url id lenght : [Actual] " + driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).getText().length();
		}
		

		//duplicated id : rsrsup1
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).sendKeys(CommonValues.USEREMAIL_CHID);
		
		//click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='desc invalid']")).getText().contentEquals("This URL cannot be used.")) {
			failMsg = failMsg + "\n invalid url(duplicated) Message : [Actual] " + driver.findElement(By.xpath("//div[@class='desc invalid']")).getText();
		}
		
		//click x button
		driver.findElement(By.xpath("//i[@class='ricon-close']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=4, dependsOnMethods={"channelManagementView"})
	public void channelManagement_validURL() throws Exception {
		String failMsg = "";
		
		//get original URL
		String originURL = driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText();
		
		//click Change URL button
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']")).click();
		Thread.sleep(500);
		
		// valid url : hello
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).sendKeys("hello");
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='desc valid']")).getText().contentEquals("This URL can be used.")) {
			failMsg = "valid url Message : [Actual] " + driver.findElement(By.xpath("//div[@class='desc valid']")).getText();
		}
		
		// button check
		if (!driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n Selected button is disabled!(valid ID)";
		}

		//click x button
		driver.findElement(By.xpath("//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		//channel url
		if(!driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText().contentEquals(originURL)) {
			failMsg = failMsg + "channel url : [Actual] " + driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=10) 
	public void channelManagement_loginuser1() throws Exception {

		//logout rsrsup2
		driver.findElement(By.id("profile-drop-down")).click();
		driver.findElement(By.linkText("Log out")).click();
		
		//login rsrsup1
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL);
	}
	
	@Test (priority=11, dependsOnMethods={"channelManagement_loginuser1"})
	public void channelManagement_changedID() throws Exception {
		String failMsg = "";
		
		//channel management view
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL)) {
			failMsg = "not channel management view. current url : " + driver.getCurrentUrl();
		}
		
		//check channelID change button. expected : not find button
		if(isElementPresent(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']"))) {
			failMsg = failMsg + "\n find channel URL change button(rsrsup1 is already changed URL)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=12, dependsOnMethods={"channelManagement_loginuser1"})
	public void channelManagement_logoutuser1() throws Exception {
		//logout rsrsup2
		driver.findElement(By.id("profile-drop-down")).click();
		driver.findElement(By.linkText("Log out")).click();
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
