package seminartest;

import java.util.regex.Pattern;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;

import org.testng.annotations.Test;

public class SampleTest{
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	public static String serverURL = "https://stapp.remoteseminar.com/";
	public static final String WEB_DRIVER_PATH = System.getProperty("user.dir") + "\\driver\\chromedriver.exe";
	
	public String seminarID = "/0edd26fd-fe14-4887-93b5-e900f9dd5e8c";
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		System.out.println("BeforeTest!!!");
		System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
		driver = new ChromeDriver();

		context.setAttribute("webDriver", driver);
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver);
		
		driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI + seminarID);
		driver.manage().window().maximize();
		
	}
	
	@Test (priority=25)
	  public void presentationView_DeleteItem() throws Exception {
	
		  //check before count
		  int items = 0;
		  while(isElementPresent(By.xpath("//ul[@class='styles_docitembox__33bKg undefined']/li[" + (items + 1) + "]")))
		  {
			  items++;
		  }
		  
		  Actions actions = new Actions(driver);
		  
		  //Delete 2 items
		  String finditem = "//*[contains(text(), 'RemoteMeeting')]";
		  WebElement web = driver.findElement(By.xpath(finditem));
		  actions.moveToElement(web).perform();
		  
		  web.findElement(By.xpath("../../../button[1]")).click();
		  
		  Thread.sleep(1000);
		  
		  //check before count
		  int afteritems = 0;
		  while(isElementPresent(By.xpath("//ul[@class='styles_docitembox__33bKg undefined']/li[" + (afteritems + 1) + "]")))
		  {
			  afteritems++;
		  }
		  
		  if ((items-2) != afteritems)
		  {
			  Exception e =  new Exception("Delete Error(delete 2 items) : before delete count " + (items)  + ", after delete count " + (afteritems)  );
			  throw e;
		  }
	  }

	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
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



