package seminartest;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

/* 0.로그인
 * 1.세미나 생성
 * 2.ShortURL 복사
 * 3.게시자 ShortURL 접근
 * 4.발표자 ShortURL 접근
 * 5.운영자 ShortURL 접근
 * 6.마스터 ShortURL 접근
 * 7.로그인x참석자 ShortURL 접근
 */
public class ShortURL {
		
	public static WebDriver Publisher_driver;
	public static WebDriver Present_driver;
	public static WebDriver Organizer_driver;
	public static WebDriver Master_driver;
	public static WebDriver NotLoginMember_driver;
	
	public String seminarTitle = "ShortURL Test";
	
	public String seminarID = "";
	public String createViewURL = "";
	public String seminarRoomURL = "";
	public String seminarLinkURL = "";
	public String seminarViewURL = "";
	public String Present = "";
	public String Publisher = "";
	public String Organizer = "";
	public String Channelname = "";
	public String shortURL = "";
	
	public static String XPATH_SEMINARVIEW_PRACTICEBTN = "//button[@class='btn btn-basic btn-auto SeminarView_actionButton__3tFHP']";
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
		
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		Publisher_driver = comm.setDriver(Publisher_driver, browsertype, "lang=en_US");
		Present_driver = comm.setDriver(Present_driver, browsertype, "lang=en_US");
		Organizer_driver = comm.setDriver(Organizer_driver, browsertype, "lang=en_US");
		Master_driver = comm.setDriver(Master_driver, browsertype, "lang=en_US");
		NotLoginMember_driver = comm.setDriver(NotLoginMember_driver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", Publisher_driver);
		context.setAttribute("webDriver2", Present_driver);
		context.setAttribute("webDriver3", Organizer_driver);
		context.setAttribute("webDriver4", Master_driver);
		context.setAttribute("webDriver5", NotLoginMember_driver);
		
        System.out.println("End BeforeTest!!!");

	}
	
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(Publisher_driver, CommonValues.USEREMAIL_PRES);
	}

	@Test(priority = 1)
	public void createSeminar() throws Exception {

		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		Publisher_driver.get(createViewURL);
		Thread.sleep(1000);
		if (!Publisher_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e = new Exception("Create Seminar view : " + Publisher_driver.getCurrentUrl());
			throw e;
		}
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(Publisher_driver, seminarTitle, false);
		comm.setCreateSeminar_setChannel(Publisher_driver);
		comm.setCreateSeminar_setMember(Publisher_driver);

		Channelname = comm.Channelname;
		Present = comm.Present; // rsrsup8@gmail.com
		Publisher = comm.Publisher; // rsrsup2@gmail.com
		Organizer = comm.Organizer; // rsrsup3@gmail.com

		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)));
		// save
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(Publisher_driver, seminarTitle);
		comm.postSeminar(Publisher_driver, seminarUri);

		if (seminarUri == null || seminarUri.isEmpty()) {
			Exception e = new Exception("fail to create seminar : " + Publisher_driver.getCurrentUrl());
			throw e;
		}

		seminarID = Publisher_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW, "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;

	}

	@Test(priority = 2)
	public void copyShortURL() throws Exception {

		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Publisher_driver.findElement(By.xpath("//div[@class='ricon ricon-link']")));

		Thread.sleep(500);

		Publisher_driver.findElement(By.xpath("//div[@class='ricon ricon-link']")).click();

		WebDriverWait wait = new WebDriverWait(Publisher_driver, 40);
		wait.until(ExpectedConditions.textToBePresentInElement(
				Publisher_driver.findElement(By.xpath("//div[@class='modal-body']")), "Seminar link copied."));
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();

		shortURL = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		System.out.println(shortURL);
	}

	@Test(priority = 3)
	public void PublisherGetShortURL() throws Exception {
		String failMsg = "";

		Publisher_driver.get(shortURL);
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		if (!Publisher_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
			failMsg = "1.Short URL do not Redirect [Expected]" + seminarViewURL + " [Actual]"
					+ Publisher_driver.getCurrentUrl();
		}

		WebElement EnterBTN = Publisher_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER));
		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", EnterBTN);

		if (EnterBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 4)
	public void PresentGetShortURL() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		comm.loginseminar(Present_driver, Present);
		Thread.sleep(1000);
		Present_driver.get(shortURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		if (!Present_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
			failMsg = "1.Short URL do not Redirect [Expected]" + seminarViewURL + " [Actual]"
					+ Present_driver.getCurrentUrl();
		}

		WebElement PracticeBTN = Present_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);

		PracticeBTN.click();
		Thread.sleep(1000);

		ArrayList<String> tabs = new ArrayList<String>(Present_driver.getWindowHandles());
		Present_driver.close();
		Present_driver.switchTo().window(tabs.get(1));

		if (!Present_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
			failMsg = failMsg + "\n 2.Short URL do not Redirect [Expected]" + seminarRoomURL + " [Actual]"
					+ Present_driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 5)
	public void OrganizerGetShortURL() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		comm.loginseminar(Organizer_driver, Organizer);
		Thread.sleep(1000);
		Organizer_driver.get(shortURL);
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		if (!Organizer_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
			failMsg = "1.Short URL do not Redirect [Expected]" + seminarViewURL + " [Actual]"
					+ Organizer_driver.getCurrentUrl();
		}

		WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
		JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);

		PracticeBTN.click();
		Thread.sleep(1000);

		ArrayList<String> tabs = new ArrayList<String>(Organizer_driver.getWindowHandles());
		Organizer_driver.close();
		Organizer_driver.switchTo().window(tabs.get(1));

		if (!Organizer_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
			failMsg = failMsg + "\n 2.Short URL do not Redirect [Expected]" + seminarRoomURL + " [Actual]"
					+ Organizer_driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 6)
	public void MasterGetShortURL() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		comm.loginseminar(Master_driver, CommonValues.USEREMAIL_PRES);
		Thread.sleep(1000);
		Master_driver.get(shortURL);
		Master_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		if (!Master_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
			failMsg = "1.Short URL do not Redirect [Expected]" + seminarViewURL + " [Actual]"
					+ Master_driver.getCurrentUrl();
		}

		WebElement EnterBTN = Master_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER));
		JavascriptExecutor js = (JavascriptExecutor) Master_driver;
		js.executeScript("arguments[0].scrollIntoView();", EnterBTN);

		if (EnterBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 7)
	public void NotLoginMemberGetShortURL() throws Exception {
		String failMsg = "";

		NotLoginMember_driver.get(shortURL);
		NotLoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		if (!NotLoginMember_driver.getCurrentUrl().contentEquals(seminarLinkURL)) {
			failMsg = "1.Short URL do not Redirect [Expected]" + seminarLinkURL + " [Actual]"
					+ NotLoginMember_driver.getCurrentUrl();
		}

		WebElement EnterBTN = NotLoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER));
		JavascriptExecutor js = (JavascriptExecutor) NotLoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", EnterBTN);

		if (EnterBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {
		Publisher_driver.quit();
		Present_driver.quit();
		Organizer_driver.quit();
		Master_driver.quit();
		NotLoginMember_driver.quit();
	
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	} 

}