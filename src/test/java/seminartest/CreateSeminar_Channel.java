package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/* CreateSeminar_Channel
 * part1
 * ID : rsrsup2 
 * 채널 : rsrsup1, rsrsup2(master)
 * 1. defalut 채널 확인, 채널 확인 팝업 - 취소
 * 2. 채널 변경 - 확인
 * 3. 기본 채널 변경
 * 4. 기본채널 변경 확인 - 원복
 * 
 * part2
 * rsrsup1
 * 11. 채널 1개인 채널 채널 1개 확인
 */
public class CreateSeminar_Channel {
	
	
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
		Thread.sleep(100);
		driver.get(CommonValues.SERVER_URL);

        System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	  }	
	

	
	@Test (priority=1)
	public void createView() throws Exception {
	
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if(!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e =  new Exception("Seminar Edit view : " +  driver.getCurrentUrl());
			throw e;
		}
	}
	
	//1. defalut 채널 확인, 채널 확인 팝업 - 취소
	@Test (priority=2)
	public void createView_channelpopup() throws Exception {
		String failMsg = "";
		
		//check default channel title
		if(!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText().contentEquals("rsrsup2")) {
			failMsg = "defalut channel title error [Actual]" + driver.findElement(By.xpath("//span[@class='selected-channel']")).getText();
		}
		
		//click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(1000);
		
		//channel popup : channel list

		List<WebElement> channelList = driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));
		if(channelList.size() != 2) {
			failMsg = failMsg + "\n 1. Channel list size [Expected] 2, [Actual] " + channelList.size();
		} else {
			for (int i = 0 ; i < channelList.size() ; i ++) {
				if(channelList.get(i).findElement(By.xpath(".//label/span[1]")).getText().contentEquals("rsrsup1")) {
					//click second channel
					channelList.get(i).findElement(By.xpath(".//label/span[1]")).click();
				}
			}
		}
		
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//2. 채널 변경 - 확인
	@Test (priority=3, dependsOnMethods={"createView"})
	public void createView_secondChannelS() throws Exception {
		String failMsg = "";
		
		//click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']"))
				.click();
		Thread.sleep(500);

		// channel popup : channel list
		List<WebElement> channelList = driver
				.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));

		if (channelList.size() != 2) {
			failMsg = failMsg + "1. Channel list size [Expected] 2, [Actual] " + channelList.size();
		} else {
			for (int i = 0 ; i < channelList.size() ; i ++) {
				if(channelList.get(i).findElement(By.xpath(".//label/span[1]")).getText().contentEquals("rsrsup1")) {
					//click second channel
					channelList.get(i).findElement(By.xpath(".//label/span[1]")).click();
				}
			}
		}

		// click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);

		// check default channel title
		if (!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText()
				.contentEquals("rsrsup1")) {
			failMsg = failMsg + "\n 2. selected channel title error [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-channel']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}

	//3. 기본 채널 변경 - 확인
	@Test(priority = 4, dependsOnMethods = { "createView" })
	public void createView_changeDefaultChannel() throws Exception {
		String failMsg = "";

		// click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(500);

		// channel popup : channel list
		List<WebElement> channelList = driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));

		if (channelList.size() != 2) {
			failMsg = failMsg + "1. Channel list size [Expected] 2, [Actual] " + channelList.size();
		} else {
			driver.findElement(By.xpath("//i[@class='icon star-empty']")).click();
		}

		// click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText()
				.contentEquals(CommonValues.TOAST_CHANGE_DEFAULT_CHANNEL)) {
			failMsg = failMsg + "\n 2. toast message (change defalut channel) : [Actual] "
					+ driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText();
		} else {
			Thread.sleep(2000);
		}
		
		//go to new create view
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if(!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e =  new Exception("Seminar Edit view : " +  driver.getCurrentUrl());
			throw e;
		}

		//check default channel title
		if (!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText().contentEquals("rsrsup1")) {
			failMsg = "1. defalut channel title error [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-channel']")).getText();
		}

		// click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']"))
				.click();
		Thread.sleep(1000);
		
		
		channelList = driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));
		List<WebElement> channelIcons= driver.findElements(By.xpath("//label[@class='Radio_radio-contents__wUdmP star']"));
		if (channelList.size() != 2) {
			failMsg = failMsg + "1. Channel list size [Expected] 2, [Actual] " + channelList.size();
		} else {
			for (int i = 0 ; i < channelList.size() ; i ++) {
				if(channelList.get(i).findElement(By.xpath(".//label/span[1]")).getText().contentEquals("rsrsup1")) {
					//check default icon
					try {
						channelIcons.get(i).findElement(By.xpath(".//i[@class='icon star-fill']"));
					    } catch (NoSuchElementException e) {
					    	failMsg = failMsg + "\n2. default channel icon ";
					    }

				}
			}
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}
	
	//4. 기본채널 변경  원복
	@Test(priority = 10, dependsOnMethods = { "createView_changeDefaultChannel" })
	public void createView_channelCheck() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if(!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e =  new Exception("Seminar Edit view : " +  driver.getCurrentUrl());
			throw e;
		}

		// click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(500);

		// channel popup : channel list
		List<WebElement> channelList = driver
				.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));

		if (channelList.size() != 2) {
			failMsg = failMsg + "1. Channel list size [Expected] 2, [Actual] " + channelList.size();
		} else {
			driver.findElement(By.xpath("//i[@class='icon star-empty']")).click();
		}

		// click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);

		if (!driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText()
				.contentEquals(CommonValues.TOAST_CHANGE_DEFAULT_CHANNEL)) {
			failMsg = failMsg + "\n 2. toast message (change defalut channel) : [Actual] "
					+ driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText();
		} else {
			Thread.sleep(2000);
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//11. 채널 1개인 채널 채널 1개 확인
	@Test(priority = 11, dependsOnMethods = { "createView_channelCheck" }, alwaysRun = true)
	public void createView_defalut1() throws Exception {
		String failMsg = "";
		
		//init : login rsrsup1
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL);
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			driver.get(createViewUri);
		}
		
		//check default channel title
		if (!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText().contentEquals("rsrsup1")) {
			failMsg = "1. defalut channel title error [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-channel']")).getText();
		}

		// click channel select
		driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(1000);

		// channel popup : channel list
		List<WebElement> channelList = driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_CHANNELLIST));
		if (channelList.size() != 1) {
			failMsg = failMsg + "\n 2. Channel list size [Expected] 1, [Actual] " + channelList.size();
		} else {
			if (!channelList.get(0).findElement(By.xpath(".//label/span[1]")).getText().contentEquals("rsrsup1")) {
				failMsg = failMsg + "\n 3. Channel name error [Expected] rsrsup1, [Actual] " + channelList.get(0).findElement(By.xpath(".//label/span[1]")).getText();
			}
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
