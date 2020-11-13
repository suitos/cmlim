package seminartest;

import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* 설문있는 경우
 * 0.로그인
 * 1.세미나 생성
 * 2.세미나 룸 멤버 설정
 * 3.세미나 설문 설정
 * 4.게시자 세미나 입장
 * 5.발표자 세미니 입장
 * 6.운영자 세미나 입장
 * 7.마스터 세미나 입장
 * 8.로그인 참석자 세미나 입장
 * 9.로그인x참석자 세미나 입장
 * 10.세미나 시작
 * 11.세미나 종료
 * 12.발표자 종료 UI 확인
 * 13.게시자 종료 UI 혹인
 * 14.운영자 종료 UI 확인
 * 15.마스터 종료 UI 확인
 * 16.로그인 참석자 종료 UI 확인
 * 17.로그인X 참석자 종료 UI 확인
 */
public class ExitSeminar2 {
	
	public static WebDriver Publisher_driver;
	public static WebDriver Present_driver;
	public static WebDriver Organizer_driver;
	public static WebDriver Master_driver;
	public static WebDriver LoginMember_driver;
	public static WebDriver NotLoginMember_driver;
	
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	public String seminarTitle = "ExitSeminar test2";
	public String seminarID = "";
	public String seminarTime = "";
	public String createViewURL = "";
	public String seminarRoomURL = "";
	public String seminarLinkURL = "";
	public String seminarViewURL = "";
	public String closedurl = "";
	public String Present = "";
	public String Publisher = "";
	public String Organizer = "";
	public String Master = "";
	public String Channelname = "";
	
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
		LoginMember_driver = comm.setDriver(LoginMember_driver, browsertype, "lang=en_US");
		NotLoginMember_driver = comm.setDriver(NotLoginMember_driver, browsertype, "lang=en_US");
			
		context.setAttribute("webDriver", Publisher_driver);
		context.setAttribute("webDriver2", Present_driver);
		context.setAttribute("webDriver3", Organizer_driver);
		context.setAttribute("webDriver4", Master_driver);
		context.setAttribute("webDriver5", LoginMember_driver);
		context.setAttribute("webDriver6", NotLoginMember_driver);
		
		Publisher_driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");

	}
	//로그인
	@Test(priority = 0)
	public void LoginSeminar() throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(Publisher_driver, CommonValues.USEREMAIL_PRES, CommonValues.USERPW);
		
		Thread.sleep(1000);

	}
	@Test(priority=1)
	  public void CreateSeminar() throws Exception {
		

		String createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		Publisher_driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000); 
		if(!Publisher_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  Publisher_driver.getCurrentUrl());
			throw e;
		}
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(Publisher_driver, seminarTitle, true);
		
		
	}
	
	@Test(priority=2)
	  public void CreateSeminar_SettingMember() throws Exception{
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Publisher_driver);
		comm.setCreateSeminar_setMember(Publisher_driver);
		Present = comm.Present; //rsrsup8@gmail.com
		Publisher = comm.Publisher; //rsrsup2@gmail.com
		Organizer = comm.Organizer; //rsrsup3@gmail.com
		Master = comm.Master; //rsrsup1@gmail.com
		Channelname = comm.Channelname;
		
		
	}
	@Test(priority=3)
	  public void CreateSeminar_SettingSurvey() throws Exception{
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setSurvey(Publisher_driver);
		
		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)));
		//save
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		List<WebElement> seminarlist = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		
		String startTime = seminarlist.get(0).findElement(By.xpath("//time[@class='seminarListItem__content__desc__startTime']")).getText();
		String endTime = seminarlist.get(0).findElement(By.xpath("//time[@class='seminarListItem__content__desc__endTime']")).getText();
		seminarTime = startTime + " ~ " + endTime;
		System.out.println(seminarTime);
		Thread.sleep(1000);
		
		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(Publisher_driver, seminarTitle);
		comm.postSeminar(Publisher_driver, seminarUri);

	    if(seminarUri == null || seminarUri.isEmpty())
	    {
	    	Exception e =  new Exception("fail to create seminar : " + Publisher_driver.getCurrentUrl());
	    	throw e;
	    }
				
		seminarID = Publisher_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW , "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
	}
	
	@Test(priority=4)
	  public void EnterSeminar_Publisher() throws Exception{
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Publisher_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(Publisher_driver.getWindowHandles());
		Publisher_driver.close();
		Publisher_driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_publisher = new WebDriverWait(Publisher_driver, 40);
			room_publisher.until(ExpectedConditions.textToBePresentInElement(
					Publisher_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=5)
	  public void EnterSeminar_Present() throws Exception{
		String failMsg = "";

		Present_driver.get(CommonValues.SERVER_URL);

		CommonValues comm = new CommonValues();
		comm.loginseminar(Present_driver, Present);
		try {
			WebDriverWait login_present = new WebDriverWait(Present_driver, 20);
			login_present.until(ExpectedConditions.textToBePresentInElement(
					Present_driver.findElement(By.xpath("//div[@id='profile-drop-down']")),
					Present.replace("@gmail.com", "")));

		} catch (Exception e) {
			failMsg = failMsg + "not login";
		}

		Present_driver.get(seminarViewURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Present_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		Present_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(Present_driver.getWindowHandles());
		Present_driver.close();
		Present_driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_present = new WebDriverWait(Present_driver, 40);
			room_present.until(ExpectedConditions.textToBePresentInElement(
					Present_driver.findElement(By.xpath("//div[@class='dialog-header']")), "Seminar start settings"));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=6)
	  public void EnterSeminar_Organizer() throws Exception{
		String failMsg = "";

		Organizer_driver.get(CommonValues.SERVER_URL);

		CommonValues comm = new CommonValues();
		comm.loginseminar(Organizer_driver, Organizer);
		try {
			WebDriverWait login_present = new WebDriverWait(Organizer_driver, 20);
			login_present.until(ExpectedConditions.textToBePresentInElement(
					Organizer_driver.findElement(By.xpath("//div[@id='profile-drop-down']")),
					Organizer.replace("@gmail.com", "")));

		} catch (Exception e) {
			failMsg = failMsg + "not login";
		}

		Organizer_driver.get(seminarViewURL);
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Organizer_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		Organizer_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(Organizer_driver.getWindowHandles());
		Organizer_driver.close();
		Organizer_driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_organizer = new WebDriverWait(Organizer_driver, 40);
			room_organizer.until(ExpectedConditions.textToBePresentInElement(
					Organizer_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=7)
	  public void EnterSeminar_Master() throws Exception{
		String failMsg = "";

		Master_driver.get(CommonValues.SERVER_URL);

		CommonValues comm = new CommonValues();
		comm.loginseminar(Master_driver, CommonValues.USEREMAIL);
		try {
			WebDriverWait login_present = new WebDriverWait(Master_driver, 20);
			login_present.until(ExpectedConditions.textToBePresentInElement(
					Master_driver.findElement(By.xpath("//div[@id='profile-drop-down']")),
					CommonValues.USERNICKNAME_JOIN));

		} catch (Exception e) {
			failMsg = failMsg + "not login";
		}

		Master_driver.get(seminarViewURL);
		Master_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) Master_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				Master_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		Master_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		Master_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(Master_driver.getWindowHandles());
		Master_driver.close();
		Master_driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_master = new WebDriverWait(Master_driver, 40);
			room_master.until(ExpectedConditions.textToBePresentInElement(
					Master_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=8)
	  public void EnterSeminar_LoginMember() throws Exception{
		String failMsg = "";

		LoginMember_driver.get(CommonValues.SERVER_URL);

		CommonValues comm = new CommonValues();
		comm.loginseminar(LoginMember_driver, CommonValues.USEREMAIL_RSUP9);
		try {
			WebDriverWait login_present = new WebDriverWait(LoginMember_driver, 20);
			login_present.until(ExpectedConditions.textToBePresentInElement(
					LoginMember_driver.findElement(By.xpath("//div[@id='profile-drop-down']")),
					CommonValues.USEREMAIL_RSUP9.replace("@gmail.com", "")));

		} catch (Exception e) {
			failMsg = failMsg + "not login";
		}

		LoginMember_driver.get(seminarViewURL);
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		LoginMember_driver.findElement(By.xpath("//label[@for='serviceAgreement']/div")).click();
		LoginMember_driver.findElement(By.xpath("//label[@for='infoAgreement']/div")).click();
		Thread.sleep(1000);
		LoginMember_driver
				.findElement(
						By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']"))
				.click();
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		try {
			WebDriverWait room_loginmember = new WebDriverWait(LoginMember_driver, 40);
			room_loginmember.until(ExpectedConditions.textToBePresentInElement(
					LoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=9)
	  public void EnterSeminar_NotLoginMember() throws Exception{
		String failMsg = "";
		
		NotLoginMember_driver.get(CommonValues.SERVER_URL);

		NotLoginMember_driver.get(seminarLinkURL);
		NotLoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) NotLoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				NotLoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		NotLoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).sendKeys(ATTENDEES_NICKNAME);

		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).sendKeys(ATTENDEES_EMAIL);

		NotLoginMember_driver.findElement(By.xpath("//label[@for='serviceAgreement']/div")).click();
		NotLoginMember_driver.findElement(By.xpath("//label[@for='infoAgreement']/div")).click();
		Thread.sleep(1000);
		NotLoginMember_driver
				.findElement(
						By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']"))
				.click();
		NotLoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		try {
			WebDriverWait room_notloginmember = new WebDriverWait(NotLoginMember_driver, 40);
			room_notloginmember.until(ExpectedConditions.textToBePresentInElement(
					NotLoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=10)
	  public void StartSeminar() throws Exception{
		String failMsg = "";
		
		WebElement StartSeminarSettingBtn = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']"));
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", StartSeminarSettingBtn);
		
		StartSeminarSettingBtn.click();
		
		WebElement StartSeminarBtn = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl seminar-start']"));
		StartSeminarBtn.click();
		WebDriverWait start_popup = new WebDriverWait(Present_driver, 20);
		start_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='modal-footer']/button[1]")));
		Present_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();

		TimeUnit.MINUTES.sleep(1);
		String[] a = { Publisher_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class"),
				       Present_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class"),
				       Organizer_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class"),
				       Master_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class"),
				       LoginMember_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class"),
				       NotLoginMember_driver.findElement(By.xpath("//strong[@id='user-type']")).getAttribute("class") };
		
		if(!a[0].contains("onair")) {
			failMsg = "NOT ON AIR : " + a[0];
		}
		if(!a[1].contains("onair")) {
			failMsg = failMsg + "\n NOT ON AIR : " + a[1];
		}
		if(!a[2].contains("onair")) {
			failMsg = failMsg + "\n NOT ON AIR : " + a[2];
		}
		if(!a[3].contains("onair")) {
			failMsg = failMsg + "\n NOT ON AIR : " + a[3];
		}
		if(!a[4].contains("onair")) {
			failMsg = failMsg + "\n NOT ON AIR : " + a[4];
		}
		if(!a[5].contains("onair")) {
			failMsg = failMsg + "\n NOT ON AIR : " + a[5]+"onair";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=11)
	  public void EndSeminar() throws Exception{
		String failMsg = "";

		
		closedurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath("//button[@id='btn-exit']/i")));
		
		Present_driver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		WebDriverWait exit_popup = new WebDriverWait(Present_driver, 20);
		exit_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		Present_driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//div[@class='buttons']/div/button[1]")).click();
		TimeUnit.MINUTES.sleep(1);		
		
		if(!Present_driver.getCurrentUrl().contentEquals(closedurl) && 
		   !Publisher_driver.getCurrentUrl().contentEquals(closedurl) &&
		   !Organizer_driver.getCurrentUrl().contentEquals(closedurl) &&
		   !Master_driver.getCurrentUrl().contentEquals(closedurl) &&
		   !LoginMember_driver.getCurrentUrl().contentEquals(closedurl) &&
		   !NotLoginMember_driver.getCurrentUrl().contentEquals(closedurl)) {
			
			failMsg = failMsg + "Not close url" ;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=12)
	  public void checkPresentExitUI() throws Exception{
		String failMsg = "";
		if (!Present_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + Present_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}	
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title incorrect in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + Present_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText();
		}
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date incorrect in closed view [Expected]" + seminarTime 
					+ " [Actual]" + Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText();
		}
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents incorrect in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText();
		}
		if(!Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()){
			failMsg = failMsg +"\n 5. Seminar statistics BTN not enabled " ;
		}
		if(!Present_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=13)
	  public void checkPublisherExitUI() throws Exception{
		String failMsg = "";
		if (!Publisher_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + Publisher_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}
		if (!Publisher_driver.findElement(By.xpath("//p[@class='title']")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title incorrect in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + Publisher_driver.findElement(By.xpath("//p[@class='title']")).getText();
		}
		if (!Publisher_driver.findElement(By.xpath("//div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date incorrect in closed view [Expected]" + seminarTime 
					+ " [Actual]" + Publisher_driver.findElement(By.xpath("//div[@class='author']")).getText();
		}
		if (!Publisher_driver.findElement(By.xpath("//div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents incorrect in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + Publisher_driver.findElement(By.xpath("//div[@class='date']")).getText();
		}
		if(!Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()){
			failMsg = failMsg +"\n 5. Seminar statistics BTN not enabled " ;
		}
		if(!Publisher_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		
		if(!Publisher_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID)) {
			failMsg = failMsg +"\n 7. Don't Go to seminar list [Expected]" + CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID
					+ " [Actual]" + Publisher_driver.getCurrentUrl();
		}
		Thread.sleep(2000);
		
		Publisher_driver.get(closedurl);
		Thread.sleep(1000);
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']")).click();
	
		if(!Publisher_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			failMsg = failMsg +"\n 7. Don't Go to seminar list [Expected]" + CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID
					+ " [Actual]" + Publisher_driver.getCurrentUrl();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=14)
	  public void checkOrganizerExitUI() throws Exception{
		String failMsg = "";
		if (!Organizer_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + Organizer_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}
		if (!Master_driver.findElement(By.xpath("//p[@class='title']")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title incorrect in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + Organizer_driver.findElement(By.xpath("//p[@class='title']")).getText();
		}
		if (!Organizer_driver.findElement(By.xpath("//div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date incorrect in closed view [Expected]" + seminarTime 
					+ " [Actual]" + Organizer_driver.findElement(By.xpath("//div[@class='author']")).getText();
		}
		if (!Organizer_driver.findElement(By.xpath("//div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents incorrect in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + Organizer_driver.findElement(By.xpath("//div[@class='date']")).getText();
		}
		if(!Organizer_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()){
			failMsg = failMsg +"\n 5. Seminar statistics BTN not enabled " ;
		}
		if(!Organizer_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=15)
	  public void checkMasterExitUI() throws Exception{
		String failMsg = "";
		if (!Master_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + Master_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}
		if (!Master_driver.findElement(By.xpath("//p[@class='title']")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title incorrect in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + Master_driver.findElement(By.xpath("//p[@class='title']")).getText();
		}
		if (!Master_driver.findElement(By.xpath("//div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date incorrect in closed view [Expected]" + seminarTime 
					+ " [Actual]" + Master_driver.findElement(By.xpath("//div[@class='author']")).getText();
		}
		if (!Master_driver.findElement(By.xpath("//div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents incorrect in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + Master_driver.findElement(By.xpath("//div[@class='date']")).getText();
		}
		if (!Master_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value").contentEquals(CommonValues.SURVEY_TITLE)) {
			failMsg = failMsg +"\n 5. seminar survey title incorrect in closed view [Expected]" + CommonValues.SURVEY_TITLE 
					+ " [Actual]" + Master_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value");
		}
		
		JavascriptExecutor js = (JavascriptExecutor) Master_driver;
		js.executeScript("arguments[0].scrollIntoView();", Master_driver.findElement(By.xpath("//div[@name='surveyDescription']")));
		
		if (!Master_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText().contentEquals(CommonValues.SURVEY_DESCRIPTION)) {
			failMsg = failMsg +"\n 6. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_DESCRIPTION 
					+ " [Actual]" + Master_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText();
		}
		
		List<WebElement> surveyquestion = Master_driver.findElements(By.xpath("//div[@class='box-question']"));
		
		if (!surveyquestion.get(0).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[0])) {
			failMsg = failMsg +"\n 7. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[0] 
					+ " [Actual]" + surveyquestion.get(0).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[1])) {
			failMsg = failMsg +"\n 8. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[1] 
					+ " [Actual]" + surveyquestion.get(1).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.xpath("//span[@class='ricon-star']")).isDisplayed()) {
			failMsg = failMsg +"\n 9. seminar survey necessary icon not displayed";
		}
		
		if (!surveyquestion.get(2).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[2])) {
			failMsg = failMsg +"\n 10. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[2] 
					+ " [Actual]" + surveyquestion.get(2).findElement(By.id("question1")).getText();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=16)
	  public void checkLoginMemberExitUI() throws Exception{
		String failMsg = "";
		if (!LoginMember_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}
		if (!LoginMember_driver.findElement(By.xpath("//p[@class='title']")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title error in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//p[@class='title']")).getText();
		}
		if (!LoginMember_driver.findElement(By.xpath("//div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date error in closed view [Expected]" + seminarTime 
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//div[@class='author']")).getText();
		}
		if (!LoginMember_driver.findElement(By.xpath("//div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents error in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//div[@class='date']")).getText();
		}
		if (!LoginMember_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value").contentEquals(CommonValues.SURVEY_TITLE)) {
			failMsg = failMsg +"\n 5. seminar survey title incorrect in closed view [Expected]" + CommonValues.SURVEY_TITLE 
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value");
		}
		
		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", LoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")));
		
		if (!LoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText().contentEquals(CommonValues.SURVEY_DESCRIPTION)) {
			failMsg = failMsg +"\n 6. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_DESCRIPTION 
					+ " [Actual]" + LoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText();
		}
		
		List<WebElement> surveyquestion = LoginMember_driver.findElements(By.xpath("//div[@class='box-question']"));
		
		if (!surveyquestion.get(0).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[0])) {
			failMsg = failMsg +"\n 7. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[0] 
					+ " [Actual]" + surveyquestion.get(0).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[1])) {
			failMsg = failMsg +"\n 8. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[1] 
					+ " [Actual]" + surveyquestion.get(1).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.xpath("//span[@class='ricon-star']")).isDisplayed()) {
			failMsg = failMsg +"\n 9. seminar survey necessary icon not displayed";
		}
		
		if (!surveyquestion.get(2).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[2])) {
			failMsg = failMsg +"\n 10. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[2] 
					+ " [Actual]" + surveyquestion.get(2).findElement(By.id("question1")).getText();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=17)
	  public void checkNotLoginMemberExitUI() throws Exception{
		String failMsg = "";
		if (!NotLoginMember_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}
		if (!NotLoginMember_driver.findElement(By.xpath("//p[@class='title']")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title error in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//p[@class='title']")).getText();
		}
		if (!NotLoginMember_driver.findElement(By.xpath("//div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date error in closed view [Expected]" + seminarTime 
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//div[@class='author']")).getText();
		}
		if (!NotLoginMember_driver.findElement(By.xpath("//div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents error in closed view [Expected]" + CommonValues.PRESENTS_LIST 
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//div[@class='date']")).getText();
		}
		if (!NotLoginMember_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value").contentEquals(CommonValues.SURVEY_TITLE)) {
			failMsg = failMsg +"\n 5. seminar survey title incorrect in closed view [Expected]" + CommonValues.SURVEY_TITLE 
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//input[@id='surveyTitle']")).getAttribute("value");
		}
		
		JavascriptExecutor js = (JavascriptExecutor) NotLoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", NotLoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")));
		
		if (!NotLoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText().contentEquals(CommonValues.SURVEY_DESCRIPTION)) {
			failMsg = failMsg +"\n 6. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_DESCRIPTION 
					+ " [Actual]" + NotLoginMember_driver.findElement(By.xpath("//div[@name='surveyDescription']")).getText();
		}
		
		List<WebElement> surveyquestion = NotLoginMember_driver.findElements(By.xpath("//div[@class='box-question']"));
		
		if (!surveyquestion.get(0).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[0])) {
			failMsg = failMsg +"\n 7. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[0] 
					+ " [Actual]" + surveyquestion.get(0).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[1])) {
			failMsg = failMsg +"\n 8. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[1] 
					+ " [Actual]" + surveyquestion.get(1).findElement(By.id("question1")).getText();
		}
		if (!surveyquestion.get(1).findElement(By.xpath("//span[@class='ricon-star']")).isDisplayed()) {
			failMsg = failMsg +"\n 9. seminar survey necessary icon not displayed";
		}
		
		if (!surveyquestion.get(2).findElement(By.id("question1")).getText().contentEquals(CommonValues.SURVEY_QUESTION[2])) {
			failMsg = failMsg +"\n 10. seminar survey description incorrect in closed view  [Expected]" + CommonValues.SURVEY_QUESTION[2] 
					+ " [Actual]" + surveyquestion.get(2).findElement(By.id("question1")).getText();
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
		LoginMember_driver.quit();
		NotLoginMember_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	} 
}
