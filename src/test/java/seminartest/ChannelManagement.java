package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

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
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ChannelManagement {
	
	
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
	public void channelManageView() throws Exception {
	
		Thread.sleep(500);
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

	@Test (priority=2, dependsOnMethods={"channelManageView"}, enabled=true)
	public void channelTitle() throws Exception {
		String failMsg = "";
		
		String originTitle = driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		
		//save empty
		driver.findElement(By.xpath("//i[@class='ricon-pencil-paper icon']")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).clear();
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().contentEquals(originTitle)) {
			failMsg = "1. channel title saved empty. saved title : [Expected]" + originTitle + " [Actual]" + driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		}
		
		//save spacebar(blank)
		driver.findElement(By.xpath("//i[@class='ricon-pencil-paper icon']")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).sendKeys(Keys.SPACE);
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().contentEquals(originTitle)) {
			failMsg = failMsg + "\n 2. channel title saved blank. saved title : [Expected]" + originTitle + " [Actual]" + driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		}
		
		// save 40 characters(max 20)
		driver.findElement(By.xpath("//i[@class='ricon-pencil-paper icon']")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).sendKeys(CommonValues.TWENTY_A + CommonValues.TWENTY_ONE);
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		Thread.sleep(500);
		if (driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().length() != 30) {
			failMsg = failMsg + "\n 3. channel title saved 40 characters.(max 30) saved title : [length]" + driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().length()
					+ " [saved data]" + driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		}
		
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}
	
	@Test (priority=3, dependsOnMethods={"channelManageView"}, enabled=true)
	public void channelTitle_valid() throws Exception {
		String failMsg = "";
		
		String testtitle = "Test Title!";
		
		// input valid title
		driver.findElement(By.xpath("//i[@class='ricon-pencil-paper icon']")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).sendKeys(testtitle);
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().contentEquals(testtitle)) {
			failMsg = "1. channel title saved. [Expected]" + testtitle + " [Actual]" + driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		}
		
		//restore
		String restoreTitle = CommonValues.USEREMAIL_PRES.split("@")[0];
		driver.findElement(By.xpath("//i[@class='ricon-pencil-paper icon']")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).click();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU channel-title']//input[1]")).sendKeys(restoreTitle);
		driver.findElement(By.xpath("//i[@class='ricon-lock-round']")).click();
		Thread.sleep(500);

		if (!driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText().contentEquals(restoreTitle)) {
			failMsg = failMsg + "\n 2. channel title restore. [Expected]" + restoreTitle + " [Actual]"
					+ driver.findElement(By.xpath("//div[@class='container-inner']//h2[1]/div[1]")).getText();
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=10, dependsOnMethods={"channelManageView"})
	public void channelDesciption() throws Exception {
		String failMsg = "";
		WebElement descfield = driver.findElement(By.xpath("//div[@class='Overview_overview__37hJ5']"));
		
		String originDes = descfield.findElement(By.xpath("./div[1]/div[1]")).getText();

		//save empty
		descfield.findElement(By.xpath(".//i[@class='ricon-pencil-paper icon']")).click();
		Thread.sleep(100);
		descfield.findElement(By.xpath(".//textarea[1]")).clear();
		
		CommonValues comm = new CommonValues();
		comm.selectAll(descfield.findElement(By.xpath(".//textarea[1]")));
		
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		
		Thread.sleep(100);
		if(!descfield.findElement(By.xpath("./div[1]/div[1]")).getText().contentEquals(CommonValues.CHANNEL_DES_PLACEHOLDER)) {
			failMsg = failMsg + "\n1.channel title saved blank. saved title : " + descfield.findElement(By.xpath("./div[1]/div[1]")).getText();
		}
		/*
		if(!descfield.findElement(By.xpath("./div[1]/div[1]")).getText().isEmpty()) {
			failMsg = failMsg + "\n channel title saved blank. saved title : " + descfield.findElement(By.xpath("./div[1]/div[1]")).getText();
		}
		*/
		
		//save spacebar(blank)
		descfield.findElement(By.xpath(".//i[@class='ricon-pencil-paper icon']")).click();
		Thread.sleep(100);
		descfield.findElement(By.xpath(".//textarea[1]")).clear();
		comm.selectAll(descfield.findElement(By.xpath(".//textarea[1]")));
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(Keys.BACK_SPACE);
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys("       ");
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();

		Thread.sleep(100);
		if (!descfield.findElement(By.xpath("./div[1]/div[1]")).getText().isEmpty()) {
			failMsg = failMsg + "\n channel title saved blank. saved title : "
					+ descfield.findElement(By.xpath("./div[1]/div[1]")).getText();
		}
		
		//save 220 characters
		descfield.findElement(By.xpath(".//i[@class='ricon-pencil-paper icon']")).click();
		Thread.sleep(100);
		descfield.findElement(By.xpath(".//textarea[1]")).clear();
		comm.selectAll(descfield.findElement(By.xpath(".//textarea[1]")));
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(Keys.BACK_SPACE);
		for (int i = 0 ; i <  30 ; i++) descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(CommonValues.TWENTY_A);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();

		Thread.sleep(100);
		if (descfield.findElement(By.xpath("./div[1]/div[1]")).getText().length() != 500) {
			failMsg = failMsg + "\n2.channel desc saved 500 characters. saved title : [length]" + descfield.findElement(By.xpath("./div[1]/div[1]")).getText().length()
					+ " [saved data]" + descfield.findElement(By.xpath("./div[1]/div[1]")).getText();
		}
		
		//save valid 
		descfield.findElement(By.xpath(".//i[@class='ricon-pencil-paper icon']")).click();
		Thread.sleep(100);
		descfield.findElement(By.xpath(".//textarea[1]")).clear();
		comm.selectAll(descfield.findElement(By.xpath(".//textarea[1]")));
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(Keys.BACK_SPACE);
		descfield.findElement(By.xpath(".//textarea[1]")).sendKeys(originDes);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();

		Thread.sleep(100);
		if (!descfield.findElement(By.xpath("./div[1]/div[1]")).getText().contentEquals(originDes)) {
			failMsg = failMsg + "\n channel desc saved valid character. [Expected]"
					+ originDes + " [Actual]"+ descfield.findElement(By.xpath("./div[1]/div[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	@Test (priority=20, dependsOnMethods={"channelManageView"})
	public void channel_Member_search() throws Exception {
		String failMsg = "";
		
		String member_master = "//div[@class='item-inner-box']//span[@class='user-info-email']";

		// search : empty & click search icon
		driver.findElement(By.xpath("//input[@class='search-input']")).click();
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//i[@class='ricon-search']")));
		
		driver.findElement(By.xpath("//i[@class='ricon-search']")).click();
		if(!driver.findElement(By.xpath(member_master)).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = "member search (input : ''), searched NickName : " + driver.findElement(By.xpath(member_master)).getText();
		}
		
		//search : nickname & enter
		driver.findElement(By.xpath("//input[@class='search-input']")).click();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("rsrsup2");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		if(!driver.findElement(By.xpath(member_master)).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "\n member search (input : NickName), searched NickName : " + driver.findElement(By.xpath(member_master)).getText();
		}

		// search : email
		driver.findElement(By.xpath("//input[@class='search-input']")).click();
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(CommonValues.USEREMAIL_PRES);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		if(!driver.findElement(By.xpath(member_master)).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "\n member search (input : Email), searched Email : " + driver.findElement(By.xpath(member_master)).getText();
		}

		// search : failtest
		driver.findElement(By.xpath("//input[@class='search-input']")).click();
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("failtest");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		if (isElementPresent(By.xpath(member_master))) {
			failMsg = failMsg + "\n member search (input : failtest), searched NickName : "
					+ driver.findElement(By.xpath(member_master)).getText();
		}
		
		//clear input field
		driver.findElement(By.xpath("//input[@class='search-input']")).click();
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		
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
