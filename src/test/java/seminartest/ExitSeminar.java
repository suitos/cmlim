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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* 설문없는 경우
 * 0.로그인
 * 1.세미나 생성
 * 2.세미나 룸 멤버 설정
 * 3.권한 별 세미나 입장(마스터,게시자,발표자,운영자,로그인참석자,로그인x참석자)
 * 4.세미나 종료
 * 5.발표자 종료 UI 확인(RST-530)
 * 6.게시자 종료 UI 혹인(RST-500,RST-501,RST-502,RST-503)
 * 7.운영자 종료 UI 확인(RST-531)
 * 8.마스터 종료 UI 확인
 * 9.로그인 참석자 종료 UI 확인(RST-513,RST-505,RST-547)
 * 10.로그인X 참석자 종료 UI 확인(RST-522,RST-523,RST-549)
 */

public class ExitSeminar {
	
	public static WebDriver Publisher_driver;
	public static WebDriver Present_driver;
	public static WebDriver Organizer_driver;
	public static WebDriver Master_driver;
	public static WebDriver LoginMember_driver;
	public static WebDriver NotLoginMember_driver;
	
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	public String seminarTitle = "ExitSeminar test";
	public String seminarID = "";
	public String seminarTime = "";
	public String createViewURL = "";
	public String seminarRoomURL = "";
	public String seminarLinkURL = "";
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
		comm.loginseminar(Publisher_driver, CommonValues.USEREMAIL_PRES);
		
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
	  public void CreateSeminar_Setting() throws Exception{
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Publisher_driver);
		comm.setCreateSeminar_setMember(Publisher_driver);
		Present = comm.Present; //rsrsup8@gmail.com
		Publisher = comm.Publisher; //rsrsup2@gmail.com
		Organizer = comm.Organizer; //rsrsup3@gmail.com
		Master = comm.Master; //rsrsup1@gmail.com
		Channelname = comm.Channelname;
		
		
		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")));
		//save
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		List<WebElement> seminarlist = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		
		String startTime = seminarlist.get(0).findElement(By.xpath("//time[@class='seminarListItem__content__desc__startTime']")).getText();
		String endTime = seminarlist.get(0).findElement(By.xpath("//time[@class='seminarListItem__content__desc__endTime']")).getText();
		seminarTime = startTime + " ~ " + endTime;
		System.out.println(seminarTime);
		Thread.sleep(1000);
		//post
		List<WebElement> seminarlist1 = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist1.get(0).findElement(By.xpath("//div[2]/button[@class='btn btn-secondary-light btn-m ']")).click();
		
		WebDriverWait wait = new WebDriverWait(Publisher_driver, 20);
		wait.until(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//div[@class='modal-body']")), "The seminar will be posted on the channel " + Channelname +"."));
		
		Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']//button[1]")).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		seminarlist.get(0).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		seminarID = Publisher_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW , "");
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
		
		
	} 
	@Test(priority=3)
	  public void EnterSeminar() throws Exception{
		String failMsg = "";
		
		Present_driver.get(CommonValues.SERVER_URL);
		Organizer_driver.get(CommonValues.SERVER_URL);
		Master_driver.get(CommonValues.SERVER_URL);
		LoginMember_driver.get(CommonValues.SERVER_URL);
		
		try {
		WebDriverWait wait_present = new WebDriverWait(Present_driver, 20);
		wait_present.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//p[@class='login__desc']")), "Host high-quality online seminars with RemoteSeminars."));
		WebDriverWait wait_oraganizer = new WebDriverWait(Organizer_driver, 20);
		wait_oraganizer.until(ExpectedConditions.textToBePresentInElement(Organizer_driver.findElement(By.xpath("//p[@class='login__desc']")), "Host high-quality online seminars with RemoteSeminars."));
		WebDriverWait wait_master = new WebDriverWait(Master_driver, 20);
		wait_master.until(ExpectedConditions.textToBePresentInElement(Master_driver.findElement(By.xpath("//p[@class='login__desc']")), "Host high-quality online seminars with RemoteSeminars."));
		}catch(Exception e) {
			failMsg = failMsg + "Drivers can't access";
		}
		
		
		CommonValues comm = new CommonValues();
		
		comm.loginseminar(Present_driver, Present);
		comm.loginseminar(Organizer_driver, Organizer);
		comm.loginseminar(Master_driver, Master);
		comm.loginseminar(LoginMember_driver, CommonValues.USEREMAIL_RSUP9);
		
		try {
		WebDriverWait login_present = new WebDriverWait(Present_driver, 20);
		login_present.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//div[@id='profile-drop-down']")), Present.replace("@gmail.com", "") ));
		WebDriverWait login_organizer = new WebDriverWait(Organizer_driver, 20);
		login_organizer.until(ExpectedConditions.textToBePresentInElement(Organizer_driver.findElement(By.xpath("//div[@id='profile-drop-down']")), Organizer.replace("@gmail.com", "") ));
		WebDriverWait login_member = new WebDriverWait(LoginMember_driver, 20);
		login_member.until(ExpectedConditions.textToBePresentInElement(LoginMember_driver.findElement(By.xpath("//div[@id='profile-drop-down']")), CommonValues.USEREMAIL_RSUP9.replace("@gmail.com", "") ));
		WebDriverWait login_master = new WebDriverWait(Master_driver, 20);
		login_master.until(ExpectedConditions.textToBePresentInElement(Master_driver.findElement(By.xpath("//div[@id='profile-drop-down']")), CommonValues.USERNICKNAME_JOIN ));
		}catch(Exception e) {
			failMsg = failMsg + "not login";
		}
		
		Publisher_driver.get(seminarRoomURL);
		Organizer_driver.get(seminarRoomURL);
		Master_driver.get(seminarRoomURL);
		LoginMember_driver.get(seminarLinkURL);
		NotLoginMember_driver.get(seminarLinkURL);
		
		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")));
		LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")).click();
		
		try {
			WebDriverWait wait = new WebDriverWait(LoginMember_driver, 40);
			wait.until(ExpectedConditions.textToBePresentInElementValue(LoginMember_driver.findElement(By.xpath("//input[@name='nickname']")), CommonValues.USEREMAIL_RSUP9.replace("@gmail.com", "")));
			WebDriverWait wait2 = new WebDriverWait(LoginMember_driver, 40);
			wait2.until(ExpectedConditions.textToBePresentInElementValue(LoginMember_driver.findElement(By.xpath("//input[@name='email']")), CommonValues.USEREMAIL_RSUP9));
		}catch(Exception e) {
			failMsg = failMsg + "login Member can't access Room_URL";
		}
		
		LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")).click();
		
		Thread.sleep(1000);
		
		JavascriptExecutor js2 = (JavascriptExecutor) NotLoginMember_driver;
		js2.executeScript("arguments[0].scrollIntoView();", NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")));
		NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")).click();
		Thread.sleep(1000);
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).sendKeys(ATTENDEES_NICKNAME);
		
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).sendKeys(ATTENDEES_EMAIL);
		
		NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")).click();
		
		try {
		WebDriverWait room_publisher = new WebDriverWait(Publisher_driver, 40);
		room_publisher.until(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle ));
		}catch(Exception e) {
			failMsg = failMsg + "1.Drivers can't access Room_URL";}
		
		try {
			WebDriverWait room_organizer = new WebDriverWait(Organizer_driver, 40);
			room_organizer.until(ExpectedConditions.textToBePresentInElement(Organizer_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle ));
		}catch(Exception e) {
			failMsg = failMsg + "2.Drivers can't access Room_URL";}
		
		try{
			WebDriverWait room_master = new WebDriverWait(Master_driver, 40);
			room_master.until(ExpectedConditions.textToBePresentInElement(Master_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle ));
		}catch(Exception e) {
			failMsg = failMsg + "3.Drivers can't access Room_URL";}
		try {
			WebDriverWait room_loginmember = new WebDriverWait(LoginMember_driver, 40);
			room_loginmember.until(ExpectedConditions.textToBePresentInElement(LoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle )); 
		}catch(Exception e) {
			failMsg = failMsg + "4.Drivers can't access Room_URL";}
		
		Present_driver.get(seminarRoomURL);
		
		try{
			WebDriverWait room_present = new WebDriverWait(Present_driver, 40);
			room_present.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//div[@class='dialog-header']")), "Seminar start settings" ));
		}catch(Exception e) {
			failMsg = failMsg + "5.Drivers can't access Room_URL";}
		
		WebElement StartSeminarSettingBtn = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']"));
		StartSeminarSettingBtn.click();
		
		WebElement StartSeminarBtn = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl seminar-start']"));
		StartSeminarBtn.click();
		WebDriverWait start_popup = new WebDriverWait(Present_driver, 20);
		start_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='modal-footer']/button[1]")));
		Present_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		
		
		WebDriverWait start_present = new WebDriverWait(Present_driver, 20);
		start_present.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//strong[@id='user-type']")), "Stan by")));
		try {
			Publisher_driver.findElement(By.xpath("//strong[@id='user-type']")).getText().contentEquals("Stan by");
			Organizer_driver.findElement(By.xpath("//strong[@id='user-type']")).getText().contentEquals("Stan by");
			Master_driver.findElement(By.xpath("//strong[@id='user-type']")).getText().contentEquals("Stan by");
			LoginMember_driver.findElement(By.xpath("//strong[@id='user-type']")).getText().contentEquals("Stan by");
			NotLoginMember_driver.findElement(By.xpath("//strong[@id='user-type']")).getText().contentEquals("Stan by");
		}catch(Exception e) {
			failMsg = failMsg + "NOT ON AIR";
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=4)
	  public void EndSeminar() throws Exception{
		closedurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath("//button[@id='btn-exit']/i")));
		
		Present_driver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		WebDriverWait exit_popup = new WebDriverWait(Present_driver, 20);
		exit_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		Present_driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//div[@class='buttons']/div/button[1]")).click();
		Thread.sleep(1000);
		
		/*
		Present_driver.switchTo().alert().accept();
		Publisher_driver.switchTo().alert().accept();
		Organizer_driver.switchTo().alert().accept();
		Master_driver.switchTo().alert().accept();
		LoginMember_driver.switchTo().alert().accept();
		NotLoginMember_driver.switchTo().alert().accept();
		*/
		
		if(!Present_driver.getCurrentUrl().contentEquals(closedurl) && 
			!Publisher_driver.getCurrentUrl().contentEquals(closedurl) &&
			!Organizer_driver.getCurrentUrl().contentEquals(closedurl) &&
			!Master_driver.getCurrentUrl().contentEquals(closedurl) &&
			!LoginMember_driver.getCurrentUrl().contentEquals(closedurl) &&
			!NotLoginMember_driver.getCurrentUrl().contentEquals(closedurl))
		{
			Exception e =  new Exception("Not close url");
			throw e;
				}
	}
	
	@Test(priority=5)
	  public void checkPresentExitUI() throws Exception{
		String failMsg = "";
		if (!Present_driver.findElement(By.xpath("//h2[@class='center']")).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
			failMsg = failMsg +"\n 1. seminar default text in closed view [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
					+ " [Actual]" + Present_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		}	
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title error in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + Present_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText();
		}
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText().contentEquals(seminarTime)) {
			failMsg = failMsg +"\n 3. seminar date error in closed view [Expected]" + seminarTime 
					+ " [Actual]" + Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText();
		}
		if (!Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='date']")).getText().contentEquals(CommonValues.PRESENTS_LIST)) {
			failMsg = failMsg +"\n 4. seminar presents error in closed view [Expected]" + CommonValues.PRESENTS_LIST 
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
	
	@Test(priority=6)
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
	
	@Test(priority=7)
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
	@Test(priority=8)
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
		if(!Master_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=9)
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
		if(!LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		
		LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);
		if(!LoginMember_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg +"\n 7. Don't Go to seminar list [Expected]" + CommonValues.SERVER_URL + CommonValues.LIST_URI
					+ " [Actual]" + LoginMember_driver.getCurrentUrl();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=10)
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
		if(!NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isEnabled()) {
			failMsg = failMsg +"\n 6. Go to seminar BTN not enabled " ;
		}
		
		NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);
		if(!NotLoginMember_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/")) {
			failMsg = failMsg +"\n 7. Don't Go to seminar list [Expected]" + CommonValues.SERVER_URL
					+ " [Actual]" + NotLoginMember_driver.getCurrentUrl();
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
		