package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RoomTest {

	public static WebDriver driver;
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
		context.setAttribute("webDriver", driver);
		driver.manage().window().maximize();
		driver.get(CommonValues.SERVER_URL);

		System.out.println("End BeforeTest!!!");
	}

	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver);
	}

	public String createSeminar(String seminarName, String seminarTime) throws Exception {

		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		String seminarUri = driver.getCurrentUrl().replace(createViewUri, "");
		System.out.println(" currentURL : " + driver.getCurrentUrl());
		System.out.println(" seminarUri : " + seminarUri);

		Thread.sleep(500);

		// check seminar type
		driver.findElement(By.id("seminar-type")).click();
		// driver.findElement(By.xpath("(.//*[normalize-space(text()) and
		// normalize-space(.)='��'])[1]/following::div[2]")).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[1]")).click();

		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).sendKeys(seminarName);

		// driver.findElement(By.name("startTime")).clear();
		driver.findElement(By.name("startTime")).sendKeys(Keys.TAB);
		driver.findElement(By.name("startTime")).sendKeys(seminarTime);
		Thread.sleep(2000);

		// save seminar
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(1000);

		// post seminar
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[2]")).click();
		Thread.sleep(1000);

		// post popup : click post
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(2000);

		String compleateUri = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI + seminarUri;
		if (!compleateUri.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("fail to create seminar : " + driver.getCurrentUrl());
			throw e;
		}
		return seminarUri;
	}

	@Test(priority = 1)
	public void createNormalSeminar() throws Exception {

	}

	@Test (priority=1)
	public void checkStandbyRoom() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 10);
		SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");
		String time1 = format1.format(cal.getTime());
		
		//create seminar after 10minute
		String seminarUri = createSeminar(Thread.currentThread().getStackTrace()[1].getMethodName(), time1);
		
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarUri;
		driver.get(detailview);
		Thread.sleep(1000);
		//click join button
		//driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/button[1]")).click();
		//Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/a[1]")).click();
		Thread.sleep(2000);
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(1));
	    
		Thread.sleep(1000);
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarUri;
		//check room uri
		if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
	    {
			failMsg = "fail to join Seminar : " + driver.getCurrentUrl();
	    }
		
		//check readyview & close seminar
		
		//check Seminar name
		String seminarname = Thread.currentThread().getStackTrace()[1].getMethodName();
		if(!seminarname.equalsIgnoreCase(driver.findElement(By.xpath("//div[@class='title ql-editor']")).getText()))
		{
			failMsg =  failMsg + "\n ReadyView > Seminar name error : " + driver.findElement(By.xpath("//div[@class='title ql-editor']")).getText();
		}
		//check presenter
		/*
		String presenter = driver.findElement(By.xpath("//div[@class='cover-view view-content']/p[2]")).getText().split("\\|")[1].trim();
		if(!presenter.contentEquals(CommonValues.USERNICKNAME_JOIN))
		{
			failMsg =  failMsg + "\n ReadyView > presenter error : " + presenter;
	  
		}
		*/
		//check start button
		if(!driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l seminar-start']")).isEnabled())
		{
			failMsg =  failMsg + "\n Ready > start seminar is not active ";
		}
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l seminar-start']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(500);
		
		//top bar screen share : disable, youtube : add, delete, doc : add, delete only
		//screen share 
		if(driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[1]/button[1]")).isEnabled())
		{
			Exception e =  new Exception("screen share button is enabled : " + driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[1]/button[1]")).isEnabled());
	    	throw e;
		}
		//youtube
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//button[@class='btn-rect plus']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-s']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[2]")).click();
		WebElement box = driver.findElement(By.xpath("//button[@class='btn-rect']"));
		if(!box.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled() && box.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled())
		{
			failMsg =  failMsg + "\n youtube item button error [share] :  " +box.findElement(By.xpath(".//span[@class='overlay-menu]/button[1]'")).isEnabled()
					+ "[delete] : " + box.findElement(By.xpath(".//span[@class='overlay-menu]/button[2]'")).isEnabled();
		}
		//doc
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[3]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0]);
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[3]")).click();
		WebElement box2 = driver.findElement(By.xpath("//div[@class='btn-rect']"));
		if(!box2.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled() && box2.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled())
		{
			failMsg =  failMsg + "\n youtube item button error [share] :  " +box.findElement(By.xpath(".//span[@class='overlay-menu]/button[1]'")).isEnabled()
					+ "[delete] : " + box.findElement(By.xpath(".//span[@class='overlay-menu]/button[2]'")).isEnabled();
		}
		
		//check chat tab
		if(!driver.findElement(By.xpath("//button[@class='chat active']")).isEnabled())
		{
			failMsg =  failMsg + "\n Ready > chat button is not active ";
		}
		if(!driver.findElement(By.xpath("//button[@class='qna false']")).isEnabled())
		{
			failMsg =  failMsg + "\n Ready > qna button is not active ";
		}
		if(!driver.findElement(By.xpath("//button[@class='download false']")).isEnabled())
		{
			failMsg =  failMsg + "\n Ready > download button is not active ";
		}
		if(!driver.findElement(By.xpath("//button[@class='attendee']")).isEnabled())
		{
			failMsg =  failMsg + "\n Ready > attendee button is not active ";
		}
		
		//check exit button
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[3]")).click();
		Thread.sleep(500);
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(100);
		//exit seminar
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[2]")).click();
		Thread.sleep(500);
		if(!detailview.equalsIgnoreCase(driver.getCurrentUrl()))
	    {
			failMsg =  failMsg + "\n fail exit button2][2]" + driver.getCurrentUrl();
	    }
		
		//close 1tab 
		driver.close();
		
		//switch 
		driver.switchTo().window(tabs2.get(0));
		
		//go to ready room
		driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/a[1]")).click();
		Thread.sleep(500);

		//close tab1 
		tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(0));
	    driver.close();
	    driver.switchTo().window(tabs2.get(1));
	    Thread.sleep(500);

		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-s']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
	    
		//close ready seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(2000);
		assertEquals(closeAlertAndGetItsText(), CommonValues.SEMINAR_CLOSE_MSG);
		
		//go to preview
		Thread.sleep(500);
	    driver.findElement(By.xpath("//div[@class='buttons-wrap']/button[2]")).click();
	    Thread.sleep(500);
	   
	    
	    //check [View statistics] button
	    if(!driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/button[1]")).getText().trim().contentEquals("View statistics"))
	    {
	    	failMsg =  failMsg + "\n Close Seminar Detail View : View statistics button error" ;
	    }
	   
	    //detail view : edit, trash
	    /* 
	    if(isElementPresent(By.xpath("//div[@class='ricon ricon-link']")))
	    {
	    	Exception e =  new Exception("Close Seminar Detail View : Link icon button is shown" );
	    	throw e;
	    }
	    */
	    if(!isElementPresent(By.xpath("//div[@class='ricon ricon-edit']")))
	    {
	    	failMsg =  failMsg + "\n Close Seminar Detail View : Edit icon button is not shown";
	    }
	    if(!isElementPresent(By.xpath("//div[@class='ricon ricon-trash']")))
	    {
	    	failMsg =  failMsg + "\n Close Seminar Detail View : Trash icon button is not shown" ;
	    }
	    
	    if(isElementPresent(By.xpath("//button[@class='btn btn-primary-light btn-s ']")))
	    {
	    	failMsg =  failMsg + "\n Close Seminar Detail View : Invite button is shown";
	    }
	    
		//delete seminar
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		

	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}

	@Test(priority = 2, enabled = false)
	public void checkNoOpenSeminar() throws Exception {
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 40);
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String time1 = format1.format(cal.getTime());

		// create seminar after 40minute
		String seminarUri = createSeminar(Thread.currentThread().getStackTrace()[1].getMethodName(), time1);

		// go to preview
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarUri;
		driver.get(detailview);
		Thread.sleep(500);
		// click Enter button
		driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/a[1]")).click();

		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarUri;
		// check detail view uri
		if (!roomuri.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("check fail No open Seminar : " + driver.getCurrentUrl());
			throw e;
		}

		// delete seminar
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
	}

	@Test(priority = 3)
	public void checkOnAirSeminar() throws Exception {
		System.out.println("##start onairseminar");
		String failMsg = "";

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 10);
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String time1 = format1.format(cal.getTime());

		// create seminar after 10minute
		String seminarUri = createSeminar(Thread.currentThread().getStackTrace()[1].getMethodName(), time1);

		// go to preview
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarUri;
		driver.get(detailview);
		Thread.sleep(2000);
		// go to ready room (new tab)
		driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/a[1]")).click();

		Thread.sleep(2000);
		System.out.println("##try go to room");

		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());

		// switch room tab
		driver.switchTo().window(tabs2.get(1));
		Thread.sleep(500);

		System.out.println("##go to room");
		// start seminar
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l seminar-start']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);

		// on air tag
		if (!driver.findElement(By.id("user-type")).getText().contentEquals(CommonValues.SEMINAR_STATUS_ONAIR)) {
			failMsg = "Seminar Tag is wrorng : " + driver.findElement(By.id("user-type")).getText();
		}

		// top bar screen share : enable, youtube : all, doc : all
		// screen share
		if (!driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[1]/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n screen share button is disabled : " + driver
					.findElement(By.xpath("//div[@id='presentation-buttons']/section[1]/button[1]")).isEnabled();
		}
		// youtube
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//button[@class='btn-rect plus']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-s']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[2]")).click();
		WebElement box = driver.findElement(By.xpath("//button[@class='btn-rect']"));
		if (!box.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled()
				&& !box.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n youtube item button error [share] :  "
					+ box.findElement(By.xpath(".//span[@class='overlay-menu]/button[1]'")).isEnabled() + "[delete] : "
					+ box.findElement(By.xpath(".//span[@class='overlay-menu]/button[2]'")).isEnabled();
		}
		// doc
		driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[3]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//input[@id='doc-upload-input']"))
				.sendKeys(CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0]);
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.findElement(By.xpath("//div[@id='presentation-buttons']/section[3]")).click();
		WebElement box2 = driver.findElement(By.xpath("//div[@class='btn-rect']"));
		if (!box2.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled()
				&& !box2.findElement(By.xpath(".//span[@class='overlay-menu']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n youtube item button error [share] :  "
					+ box.findElement(By.xpath(".//span[@class='overlay-menu]/button[1]'")).isEnabled() + "[delete] : "
					+ box.findElement(By.xpath(".//span[@class='overlay-menu]/button[2]'")).isEnabled();

		}

		// check chat tab
		if (!driver.findElement(By.xpath("//button[@class='chat active']")).isEnabled()) {
			failMsg = failMsg + "\n Ready > chat button is not active ";
		}
		if (!driver.findElement(By.xpath("//button[@class='qna false']")).isEnabled()) {
			failMsg = failMsg + "\n Ready > qna button is not active ";
		}
		if (!driver.findElement(By.xpath("//button[@class='download false']")).isEnabled()) {
			failMsg = failMsg + "\n Ready > download button is not active ";
		}
		if (!driver.findElement(By.xpath("//button[@class='attendee']")).isEnabled()) {
			failMsg = failMsg + "\n Ready > attendee button is not active ";
		}

		// check exit button
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[3]")).click();
		Thread.sleep(500);
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(1000);
		// exit seminar
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[2]")).click();
		Thread.sleep(1000);
		if (!detailview.equalsIgnoreCase(driver.getCurrentUrl())) {
			failMsg = failMsg + "\n fail exit button2][2]" + driver.getCurrentUrl();
		}

		// close 1tab
		driver.close();

		// switch
		driver.switchTo().window(tabs2.get(0));
		driver.navigate().refresh();
		// detail view tag : ON AIR
		if (!driver.findElement(By.xpath("//div[@class='tag onair']")).getText().trim()
				.contentEquals(CommonValues.SEMINAR_STATUS_ONAIR)) {
			failMsg = failMsg + "\n Detail View Tag Error :  "
					+ driver.findElement(By.xpath("//div[@class='tag onair']")).getText();
		}

		// detail view : edit, trash
		if (!isElementPresent(By.xpath("//div[@class='ricon ricon-link']"))) {
			failMsg = failMsg + "\n Close Seminar Detail View : Link icon button is not shown";
		}
		if (isElementPresent(By.xpath("//div[@class='ricon ricon-edit']"))) {
			failMsg = failMsg + "\n Close Seminar Detail View : Edit icon button is shown";
		}
		if (isElementPresent(By.xpath("//div[@class='ricon ricon-trash']"))) {
			failMsg = failMsg + "\n Close Seminar Detail View : Trash icon button is shown";
		}

		if (!isElementPresent(By.xpath("//button[@class='btn btn-primary-light btn-s ']"))) {
			failMsg = failMsg + "\n Close Seminar Detail View : Invite button is not shown";
		}

		// go to room(new tab)
		driver.findElement(By.xpath("//div[@class='SeminarView_wrapJoin__uJG5Q']/a[1]")).click();
		Thread.sleep(500);

		tabs2 = new ArrayList<String>(driver.getWindowHandles());

		// switch room tab
		driver.switchTo().window(tabs2.get(1));
		System.out.println("##go to room2222");
		// close on air seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]"))
				.click();
		Thread.sleep(2000);
		assertEquals(closeAlertAndGetItsText(), CommonValues.SEMINAR_CLOSE_MSG);

		Thread.sleep(500);

		// close 1tab
		driver.close();

		// switch
		driver.switchTo().window(tabs2.get(0));
		driver.navigate().refresh();

		// delete seminar
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

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
