package seminartest;

import static org.testng.Assert.fail;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* ChannelVisivility
 * Channel public, private 
 * Channel public : channel member, non channel member, non login user
 * Channel private: channel member, non channel member, non login user
 */
public class ChannelVisibility {
	
	public static String XPATH_CHANNEL_TOAST = "//div[@class='toast-inner normal']/span";

	public static String MSG_CHANNEL_PUBLIC_TOAST = "This channel has been changed to public.";
	public static String MSG_CHANNEL_PRIVATE_TOAST = "This channel has been changed to private.";
	
	public static WebDriver driver;
	public static WebDriver viewerdriver;

	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		viewerdriver = comm.setDriver(viewerdriver, browsertype, "lang=en_US");

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", viewerdriver);

		System.out.println("End BeforeTest!!!");
	}

	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver);
	}

	@Test(priority = 1)
	public void channelManageView() throws Exception {

		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL)) {
			Exception e = new Exception("not channel management view. current url : " + driver.getCurrentUrl());
			throw e;
		}
	}

	@Test(priority = 2, dependsOnMethods = { "channelManageView" })
	public void channelVisibilitySetting() throws Exception {

		String failMsg = "";

		// Defalut : private
		if (!isElementPresent(By.xpath("//i[@class='ricon-lock-round']"))) {
			failMsg = "Default Value. Private is not checked";
		}
	
		// change public. toast, lock icon
		driver.findElement(By.xpath("//label[@for='access-button']/span[1]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(XPATH_CHANNEL_TOAST)));
		} catch (Exception e) {
			System.out.println("cannot find element : " + e.getMessage());
		}
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_CHANNEL_TOAST)).getText()
				.contentEquals(MSG_CHANNEL_PUBLIC_TOAST)) {
			failMsg = failMsg + "\n 1.toast message after click public : [Actual] "
					+ driver.findElement(By.xpath(XPATH_CHANNEL_TOAST)).getText();
		}
		if(isElementPresent(By.xpath("//i[@class='ricon-lock-round']"))) {
			failMsg = failMsg + "\n 2.click public. lock icon is visible";
		}

		// change private. toast, lock icon
		driver.findElement(By.xpath("//label[@for='access-button']/span[1]")).click();
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(XPATH_CHANNEL_TOAST)));
		} catch (Exception e) {
			System.out.println("cannot find element : " + e.getMessage());
		}
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_CHANNEL_TOAST)).getText().contentEquals(MSG_CHANNEL_PRIVATE_TOAST)) {
			failMsg = failMsg + "\n 3.toast message after click private : [Actual] "
					+ driver.findElement(By.xpath(XPATH_CHANNEL_TOAST)).getText();
		}
		if(!isElementPresent(By.xpath("//i[@class='ricon-lock-round']"))) {
			failMsg = failMsg + "\n 4.click private. lock icon is not visible";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	public void checkSetting(boolean isPrivate) throws InterruptedException {
		
		if(isPrivate) {
			// private check
			if (!isElementPresent(By.xpath("//i[@class='ricon-lock-round']"))) {
				driver.findElement(By.xpath("//label[@for='access-button']/span[1]")).click();
			}
		} else {
			if (isElementPresent(By.xpath("//i[@class='ricon-lock-round']"))) {
				driver.findElement(By.xpath("//label[@for='access-button']/span[1]")).click();
			}
		}
		Thread.sleep(500);
	}
	
	public String channelMember(String channelURL) throws Exception {
		
		String failMsg = "";
		viewerdriver.get(CommonValues.SERVER_URL);
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(viewerdriver, CommonValues.USEREMAIL_PRES);
		   
	    Thread.sleep(1000); 
	    
	    viewerdriver.get(channelURL);
	    Thread.sleep(500);
	    
	    try {
	
	    	if (viewerdriver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU']")).getText().contentEquals("Channel list")) {
	    		failMsg = "";
	    	} else {
	    		failMsg = "channel member cannot find channel seminar list";
	    	}
		} catch (NoSuchElementException e) {
			failMsg = "channel member cannot find channel seminar list Element : " + e;
		}
	    
	    //logout
	    comm.logout(viewerdriver);
	    return failMsg;
	}
	
	public String nonchannelMember(String channelURL, boolean privateChannel) throws Exception {
		
		String failMsg = "";
		viewerdriver.get(CommonValues.SERVER_URL);
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(viewerdriver, CommonValues.USEREMAIL_RSUP9);
	    Thread.sleep(1000); 
	    
	    viewerdriver.get(channelURL);
	    Thread.sleep(500);
	    
	    try {
	
	    	if (privateChannel) {
	    		if (viewerdriver.findElement(By.xpath("//div[@class='channel-announce']")).getText().contentEquals("This channel is not public")) {
	    			failMsg = "";
		    	} else {
		    		failMsg = "non channel member cannot find private channel message : [Expected]" + CommonValues.CHANNEL_PRIVATE_MSG 
		    				+ "[Actual]" + viewerdriver.findElement(By.xpath("//div[@class='channel-announce']")).getText();
		    	}
	    	} else {
	    		if (viewerdriver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU']")).getText().contentEquals("Channel list")) {
	    			failMsg = "";
		    	} else {
		    		failMsg = "non channel member cannot find channel channel seminar Element";
		    	}
	    	}
	    	
		} catch (NoSuchElementException e) {
			failMsg = "non channel member cannot find channel Element : " + e;
		}
	    
	    //logout
	    comm.logout(viewerdriver);
	    return failMsg;
	}
	
	public String nonLoginUser(String channelURL, boolean privateChannel) throws InterruptedException {
		
		String failMsg = "";;
		
	    viewerdriver.get(channelURL);
	    Thread.sleep(1000);
	    
	    try {
	    	if (privateChannel) {
	    		if (viewerdriver.findElement(By.xpath("//div[@class='channel-announce']")).getText().contentEquals("This channel is not public")) {
	    			failMsg = "";
		    	} else {
		    		failMsg = "non login user cannot find private channel message : [Expected]" + CommonValues.CHANNEL_PRIVATE_MSG 
		    				+ "[Actual]" + viewerdriver.findElement(By.xpath("//div[@class='channel-announce']")).getText();
		    	}
	    	} else {
	    		if (viewerdriver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU']")).getText().contentEquals("Channel list")) {
	    			failMsg = "";
		    	} else {
		    		failMsg = "non login user cannot find channel channel seminar list name : " + viewerdriver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU']")).getText();
		    	}
	    	}
		} catch (NoSuchElementException e) {
			failMsg = "non login user cannot find channel Element";
		}
	    
	    return failMsg;
	}
	
	@Test(priority = 3, dependsOnMethods = { "channelManageView" })
	public void privateChannel() throws Exception {
		String failMsg = "";
		
		//private channel check
		checkSetting(true);
		String channelURL = driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText();
		System.out.println("***** serverurl "+ channelURL);
		
		if(!channelURL.contains("https://"))
			channelURL = "https://" + channelURL;
		String testMsg = "";
		//channel member
		testMsg = channelMember(channelURL);
		if (!testMsg.isEmpty())
			failMsg = "ChannelMember fail to visit private Channel : " + testMsg;
		
		//non channel member
		testMsg = nonchannelMember(channelURL, true);
		if (!testMsg.isEmpty())
			failMsg = failMsg + "\n non ChannelMember fail to private Channel : " + testMsg;

		//non login member
		testMsg = nonLoginUser(channelURL, true);
		if (!testMsg.isEmpty())
			failMsg = failMsg + "\n non LoginMember fail to private Channel : " + testMsg;
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, dependsOnMethods = { "channelManageView" })
	public void publicChannel() throws Exception {
		String failMsg = "";
		
		//private channel check
		checkSetting(false);
		String channelURL = driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText();
		
		if(!channelURL.contains("https://"))
			channelURL = "https://" + channelURL;
		
		System.out.println("#### serverurl "+ channelURL);
		
		String testMsg = "";
		//channel member
		testMsg = channelMember(channelURL);
		if (!testMsg.isEmpty())
			failMsg = "ChannelMember fail to visit public Channel : " + testMsg;
		
		//non channel member
		testMsg = nonchannelMember(channelURL, false);
		if (!testMsg.isEmpty())
			failMsg = failMsg + "\n non ChannelMember fail to public Channel : " + testMsg;

		//non login member
		testMsg = nonLoginUser(channelURL, false);
		if (!testMsg.isEmpty())
			failMsg = failMsg + "\n non LoginMember fail to public Channel : " + testMsg;
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, dependsOnMethods = { "channelManageView" })
	public void setPrivate() throws Exception {
		checkSetting(true);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		viewerdriver.quit();
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
