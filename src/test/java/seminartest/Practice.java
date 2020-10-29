package seminartest;


import static org.testng.Assert.fail;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.openqa.selenium.StaleElementReferenceException;
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

/*
 * 0.로그인
 * 1.연습하기 가능한 세미나 생성(2윌 뒤 예정 세미나)
 * 2.세미나 세팅(채널 변경,멤버 설정(마스터,게시자,발표자,운영자))
 * 3.게시자 리허설룸 진입 시도(RST-876)
 * 4.마스터 리허설룸 진입 시도(RST-877)
 * 5.빌표자 리허설룸 진입 (/view)(RST-748)
 * 6.운영자 리허설룸 진입(/view)(RST-750)
 * 7.리허설룸 UI 체크 (RST-753)
 * 8.리허설 뱃지 확인 (/view)
 * 9.리허설 시작 > 11분 대기 (RST-756)
 * 10.리허설 종료 url확인(RST-758)
 * 11.발표자 리허설 종료 화면 UI 체크(RST-907)
 * 12.운영자 리허설 종료 화면 UI 체크(RST-902)
 * 13.리허설 종료 후 뱃지 확인(/view)
 * 14.연습하기 가능한 세미나 생성2
 * 15.세미나 세팅(채널 변경,멤버설정(발표자, 운영자=게시자))
 * 16.발표자료 세팅 > 일부 spec out으로 주석처리
 * 17.운영자 리허설룸 진입(/list)(RST-738)
 * 18.발표자 리허설룸 진입(/view)
 * 19.기존 발표자료 삭제 시도 > 일부 spec out으로 주석처리
 * 20.유튜브 발표자료 등록 및 일부 삭제(연습모드 내 다수)
 * 21.문서 발표자료 등록 및 일부 삭제(연습모드 내 다수) > 사용안함처리
 * 22.연습모드 종료
 * 23.해당 세미나 수정 페이지에서 발표자료 확인(RST-911) > docs size 2>0로 수정
*/

public class Practice {
	
	public static WebDriver Publisher_driver;
	public static WebDriver Present_driver;
	public static WebDriver Organizer_driver;
	public static WebDriver Master_driver;
	
	public String seminarTitle = "Practice test";
	public String createViewURL = "";
	public String seminarID = "";
	public String seminarAuthor = "";
	public String seminarAuthor2 = "";
	public String seminarDate = "";
	public String seminarDetailURL = "";
	public String seminarRoomURL = "";
	public String closedURL = "";
	public String Present = "";
	public String Publisher = "";
	public String Organizer = "";
	public String Master = "";
	public String Channelname = "";
	public String filePath = "";
	
	public static String REHEARSAL_END_MSG = "Seminar rehearsal has ended.";
	public static String REHEARSAL_END_TITLE = "Rehearsal has ended.";
	public static String REHEARSAL_CANNOTENTER_MSG = "Currently, rehearsal is not allowed.";
	public static String CANNOT_DELETE_MSG = "In the rehearsal, only the presentation materials registered in the rehearsal can be deleted.";
	
	public static String XPATH_SEMINARlIST_PRACTICEBTN = "//button[@class='btn btn-basic btn-m ']";
	public static String XPATH_SEMINARVIEW_ENTERBTN = "//button[@class='btn btn-primary btn-auto actionButton']";
	public static String XPATH_SEMINARVIEW_PRACTICEBTN = "//button[@class='btn btn-basic btn-auto SeminarView_actionButton__3tFHP']";
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private ArrayList<String> addedItem = new ArrayList();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		Publisher_driver = comm.setDriver(Publisher_driver, browsertype, "lang=en_US");
		Present_driver = comm.setDriver(Present_driver, browsertype, "lang=en_US");
		Organizer_driver = comm.setDriver(Organizer_driver, browsertype, "lang=en_US");
		Master_driver = comm.setDriver(Master_driver, browsertype, "lang=en_US");
		
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = CommonValues.TESTFILE_PATH_MAC;
		else
			filePath = CommonValues.TESTFILE_PATH;
	
		context.setAttribute("webDriver", Publisher_driver);
		context.setAttribute("webDriver2", Present_driver);
		context.setAttribute("webDriver3", Organizer_driver);
		context.setAttribute("webDriver4", Master_driver);
		
        System.out.println("End BeforeTest!!!");

	}
	
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(Publisher_driver, CommonValues.USEREMAIL_PRES);

		Thread.sleep(1000);
	}
	
	@Test(priority=1)
	  public void createCanPracticeSeminar() throws Exception {
		
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		Publisher_driver.get(createViewURL);
		Thread.sleep(1000); 
		if(!Publisher_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  Publisher_driver.getCurrentUrl());
			throw e;
		}
		
		setRehearsal(Publisher_driver, seminarTitle, true);
		
	}
	
	@Test(priority=2)
	  public void CreateRehearsalSeminar_Setting() throws Exception{
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Publisher_driver);
		comm.setCreateSeminar_setMember(Publisher_driver);
		
		Channelname = comm.Channelname;
		Present = comm.Present; //rsrsup8@gmail.com
		Publisher = comm.Publisher; //rsrsup2@gmail.com
		Organizer = comm.Organizer; //rsrsup3@gmail.com
		Master = comm.Master; //rsrsup1@gmail.com
		
		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")));
		//save
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		//post
		List<WebElement> seminarlist1 = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist1.get(0).findElement(By.xpath("//div[2]/button[@class='btn btn-secondary-light btn-m ']")).click();
		WebDriverWait wait = new WebDriverWait(Publisher_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//div[@class='modal-body']")), "The seminar will be posted on the channel rsrsup1.\n" + 
	    		"Once posted, it cannot be changed."));
	    Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']//button[1]")).click();
		Thread.sleep(2000);
		
	}
	
	@Test(priority=3)
	  public void Publisher_Enter_RehearsalSeminar() throws Exception {
		String failMsg = "";
		
		List<WebElement> seminarlist = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist.get(0).findElement(By.xpath(XPATH_SEMINARlIST_PRACTICEBTN)).click();
		
	    WebDriverWait wait = new WebDriverWait(Publisher_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//div[@class='modal-body']")), "Only the presenter or creator of the seminar can\n" + 
	  		"rehearse."));
	  
	    Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();;
	
	    seminarlist.get(0).click();
	    Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	    String[] info = Publisher_driver.findElement(By.xpath("//p[@class='info-date-author date']")).getText().split("\n");
	    System.out.println(info[0]);
	    seminarDate = info[0];
	    seminarAuthor = info[1];
	    seminarAuthor2 = seminarAuthor.replace(",", ""); //쉼표 없음
	    
	    
	    System.out.println(seminarAuthor);
	    seminarID = Publisher_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW , "");
	    
	    System.out.println(seminarID);
	    
	    seminarDetailURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
	  
	    if(!Publisher_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Publisher_driver.getCurrentUrl()
			+" [Actual]" + seminarDetailURL;	
		}
	    
	    WebElement EnterBTN = Publisher_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER));
	    JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", EnterBTN);
		
		if(EnterBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority=4)
	  public void Master_Enter_RehearsalSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(Master_driver, Master);
		Thread.sleep(1000);
		
		Master_driver.get(seminarDetailURL);
		Master_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		if(!Master_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Master_driver.getCurrentUrl()
			+" [Actual]" + seminarDetailURL;	
		}
	    
	    WebElement EnterBTN = Master_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER));
	    JavascriptExecutor js = (JavascriptExecutor) Master_driver;
		js.executeScript("arguments[0].scrollIntoView();", EnterBTN);
		
		if(EnterBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}	
	
	@Test(priority=5)
	  public void Present_Enter_RehearsalSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(Present_driver, Present);
		Thread.sleep(1000);
		
		Present_driver.get(seminarDetailURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		if(!Present_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Present_driver.getCurrentUrl()
			+" [Actual]" + seminarDetailURL;	
		}
	    
	    WebElement PracticeBTN = Present_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		
		if(!PracticeBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}
		
		PracticeBTN.click();
		Thread.sleep(1000);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=6)
	  public void Oraganizer_Enter_RehearsalSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(Organizer_driver, Organizer);
		Thread.sleep(1000);
		
		Organizer_driver.get(seminarDetailURL);
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Organizer_driver.getCurrentUrl()
			+" [Actual]" + seminarDetailURL;	
		}
	    
	    WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		
		if(!PracticeBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}
		PracticeBTN.click();
		Thread.sleep(1000);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=7)
	  public void checkRehearsalRoom() throws Exception {
		String failMsg = "";
		
		String parentHandle = Present_driver.getWindowHandle();
		
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
		
		WebElement SettingBTN = Present_driver.findElement(By.xpath("//div[@class='buttons align-center']/button"));
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", SettingBTN);
		SettingBTN.click();
		Thread.sleep(2000);
		
		String headerlogo = Present_driver.findElement(By.id("user-type")).getText();
		String title = Present_driver.findElement(By.id("title")).getText();
		String[] rehearsalinfo = Present_driver.findElement(By.xpath("//p[@class='info-date-author date']")).getText().split("\n");
		String date = rehearsalinfo[0];
		String author = rehearsalinfo[1];

		if(!headerlogo.contentEquals("Rehearsal"))
		{
			failMsg = failMsg + "1. header is not Rehearsal ";
			
		}
		if(!title.contentEquals(seminarTitle))
		{
			failMsg = failMsg + "\n 2.different seminar title [Expected]" + seminarTitle + 
				"[Actual]" + title;
		}
		if(!date.contentEquals(seminarDate))
		{
			failMsg = failMsg + "\n 3.different seminar time [Expected]" + seminarDate + 
					"[Actual]" + date;
		}
		if(!author.contains(seminarAuthor)) 
		{
			failMsg = failMsg + "\n 4.different seminar author [Expected]" + seminarAuthor + 
					" [Actual]" + author;
		}
		
		Present_driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=8)
	  public void checkRehearsalBadge() throws Exception {
		String failMsg = "";
		
		Present_driver.get(seminarDetailURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath("//div[@class='contents']")));
		String BadgeinView = Present_driver.findElement(By.xpath("//div[@class='contents']")).getText();
		if(!BadgeinView.contentEquals("Rehearsal")) 
		{
			failMsg = failMsg + "\n 1.different seminar Badge [Expected] Rehearsal"  + 
					" [Actual]" + BadgeinView;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=9)
	  public void startRehearsal() throws Exception {
		
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
		
		WebElement StartBTN = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl seminar-start']"));
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", StartBTN);
		
		StartBTN.click();
		WebDriverWait wait = new WebDriverWait(Present_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)), "Do you want to start the seminar?"));
	    Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		TimeUnit.MINUTES.sleep(12);
	}
	
	@Test(priority=10)
	  public void checkRehearsalEndAlert() throws Exception {
		String failMsg = "";
		
		closedURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		
		if(!Present_driver.getCurrentUrl().contentEquals(closedURL)) {
			Exception e =  new Exception("Seminar not exit : " +  Present_driver.getCurrentUrl());
			throw e;
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=11)
	  public void checkPresentRehearsalExitUI() throws Exception {
		String failMsg = "";
		String parentHandle = Present_driver.getWindowHandle();
		
		String endtitle = Present_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		String title = Present_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText();
		String date = Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[1]")).getText();
		String author = Present_driver.findElement(By.xpath("//div[@class='wrap-text']/div[2]")).getText();
		WebElement GOviewBTN = Present_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']"));
	
		if(!endtitle.contentEquals(REHEARSAL_END_TITLE)) 
		{
			failMsg = failMsg + "\n 1.different End Title  [Expected]" + REHEARSAL_END_TITLE
					+" [Actual]" + endtitle;
		}
		if(!title.contentEquals(seminarTitle)) 
		{
			failMsg = failMsg + "\n 2.different Seminar Title  [Expected]" + seminarTitle
					+" [Actual]" + title;
		}
		if(!date.contentEquals(seminarDate)) 
		{
			failMsg = failMsg + "\n 3.different Seminar Date  [Expected]" + seminarDate
					+" [Actual]" + date;
		}
		if(!author.contentEquals(seminarAuthor) && !author.contentEquals(seminarAuthor2)) 
		{
			failMsg = failMsg + "\n 4.different Seminar Author  [Expected]" + seminarAuthor
					+" [Actual]" + author;
		}
		Thread.sleep(1000);
		GOviewBTN.click();
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(!Present_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 5.do not go seminar view   [Expected]" + seminarDetailURL
					+" [Actual]" + Present_driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority=12)
	  public void checkOrganizerRehearsalExitUI() throws Exception {
		String failMsg = "";
		
		String parentHandle = Organizer_driver.getWindowHandle();
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		String endtitle = Organizer_driver.findElement(By.xpath("//h2[@class='center']")).getText();
		String title = Organizer_driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText();
		String date = Organizer_driver.findElement(By.xpath("//div[@class='wrap-text']/div[1]")).getText();
		String author = Organizer_driver.findElement(By.xpath("//div[@class='wrap-text']/div[2]")).getText();
		WebElement GOviewBTN = Organizer_driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-xl ']"));
		
		if(!endtitle.contentEquals(REHEARSAL_END_TITLE)) 
		{
			failMsg = failMsg + "\n 1.different End Title  [Expected]" + REHEARSAL_END_TITLE
					+" [Actual]" + endtitle;
		}
		if(!title.contentEquals(seminarTitle)) 
		{
			failMsg = failMsg + "\n 2.different Seminar Title  [Expected]" + seminarTitle
					+" [Actual]" + title;
		}
		if(!date.contentEquals(seminarDate)) 
		{
			failMsg = failMsg + "\n 3.different Seminar Date  [Expected]" + seminarDate
					+" [Actual]" + date;
		}
		if(!author.contentEquals(seminarAuthor) && !author.contentEquals(seminarAuthor2))
		{
			failMsg = failMsg + "\n 4.different Seminar Author  [Expected]" + seminarAuthor
					+" [Actual]" + author;
		}
		Thread.sleep(1000);
		GOviewBTN.click();
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 5.do not go seminar view   [Expected]" + seminarDetailURL
					+" [Actual]" + Organizer_driver.getCurrentUrl();
		}
		
		Organizer_driver.close();
		Organizer_driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority=13 ,dependsOnMethods = { "checkRehearsalBadge"} )
	  public void checkAfterRehearsalBadge() throws Exception {
		Present_driver.get(seminarDetailURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(!Present_driver.findElement(By.xpath("//div[@class='no-tag wrap-marks']")).isDisplayed()
				&& (Present_driver.findElements(By.xpath("//div[@class='wrap-marks']")).isEmpty()) ){
			System.out.println("Badge is not displayed");
		}
		else{
			String BadgeText = Present_driver.findElement(By.xpath("//div[@class='contents']")).getText();
			Exception e = new Exception("Badge is displayed : " + BadgeText );
			throw e;
			}
		}
	
	@Test(priority=14)
	  public void createCanPracticeSeminar2() throws Exception {
		
		Publisher_driver.get(createViewURL);
		Thread.sleep(1000); 
		if(!Publisher_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  Publisher_driver.getCurrentUrl());
			throw e;
		}
		
		setRehearsal(Publisher_driver, seminarTitle, true);
		
	}
	
	@Test(priority=15)
	  public void CreateRehearsalSeminar_SettingMember() throws Exception{
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Publisher_driver);
		//발표자 추가
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB3)).click();
		Thread.sleep(500);
		Publisher_driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']/div/button")).click();
		Thread.sleep(1000);
		List<WebElement> members_present = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		
		members_present.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		Thread.sleep(500);
		Present = members_present.get(0).findElement(By.xpath("./span[@class='member-email']")).getText();
		System.out.println(Present);
		Thread.sleep(1000);
		Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		//Default 발표자 삭제
		List<WebElement> member_pres = Publisher_driver.findElements(By.xpath("//div[@role='presentation']"));
		Thread.sleep(500);
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		Publisher_driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);
		List<WebElement> members_organized = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		
		members_organized.get(1).findElement(By.xpath("./span[@class='member-name']")).click();
		Thread.sleep(500);
		Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
	}
	
	@Test(priority=16)
	  public void CreateRehearsalSeminar_SettingPresentation() throws Exception{
		String failMsg = "";
		/*
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB4)).click();
		
		int docs_beforeS = Publisher_driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']")).size();
		
		String testpng = filePath + CommonValues.TESTFILE_LIST[0];
		Publisher_driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testpng);
		Thread.sleep(1000);
		
		List<WebElement> docs_after = Publisher_driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if((docs_beforeS+1) != docs_after.size()) {
			failMsg = "1. added file count error [Expected]" + (docs_beforeS+1) + " [Actual]" + docs_after.size();
		}  else {
			addedItem.add(CommonValues.TESTFILE_LIST[0]);
		}
		*/
		Publisher_driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		Publisher_driver.findElement(By.xpath("//label[@for='input-youtube-link']/button")).click();
		Thread.sleep(500);
		
		List<WebElement> docs = Publisher_driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if(docs.size() != 2) {
			failMsg = "1. added Presentation file count error [Expected]2 [Actual]" + docs.size();
		} 
		
		if(!docs.get(1).findElement(By.xpath(".//h4[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[0])) {
			failMsg = failMsg + "\n 2. added youtube titie error [Expected]" + CommonValues.YOUTUBE_TITLE[0] 
					+ " [Actual]" + docs.get(1).findElement(By.xpath(".//h4[@class='title']")).getText();
		} else {
			addedItem.add(CommonValues.YOUTUBE_TITLE[0]);
		}

		JavascriptExecutor js = (JavascriptExecutor) Publisher_driver;
		js.executeScript("arguments[0].scrollIntoView();", Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")));
		//save
		Publisher_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		//post
		List<WebElement> seminarlist1 = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist1.get(0).findElement(By.xpath("//div[2]/button[@class='btn btn-secondary-light btn-m ']")).click();
		WebDriverWait wait = new WebDriverWait(Publisher_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Publisher_driver.findElement(By.xpath("//div[@class='modal-body']")), "The seminar will be posted on the channel rsrsup1.\n" + 
	    		"Once posted, it cannot be changed."));
	    Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Publisher_driver.findElement(By.xpath("//div[@class='modal-footer']//button[1]")).click();
		Thread.sleep(2000);
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority=17)
	public void Organizer_Enter_CanRehearsalSeminarinList() throws Exception {
		String failMsg = "";
		String parentHandle = Publisher_driver.getWindowHandle();
		
		List<WebElement> seminarlist = Publisher_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist.get(0).findElement(By.xpath(XPATH_SEMINARlIST_PRACTICEBTN)).click();
		Thread.sleep(1000);

		for (String winHandle : Publisher_driver.getWindowHandles()) {
			Publisher_driver.switchTo().window(winHandle); }
		
		seminarID = Publisher_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM, "");
		seminarDetailURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		System.out.println(seminarID);
		
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//list에서 입장확인
		if(!Publisher_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Publisher_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
		
		Publisher_driver.close();
		Publisher_driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=18)
	  public void Present_Enter_CanRehearsalSemiar() throws Exception{
		String failMsg = "";
		String parentHandle = Present_driver.getWindowHandle();

		
		Present_driver.get(seminarDetailURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(!Present_driver.getCurrentUrl().contentEquals(seminarDetailURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Present_driver.getCurrentUrl()
			+" [Actual]" + seminarDetailURL;	
		}
	    
	    WebElement PracticeBTN = Present_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		
		if(!PracticeBTN.isEnabled()) {
			Exception e = new Exception("button is enabled");
			throw e;
		}
		
		PracticeBTN.click();
		Thread.sleep(1000);
		
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
		
		if(!Present_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 2.Do not access Seminar Room :" + Present_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;
		}
		
		WebElement StartSeminarSettingBtn = Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']"));
		JavascriptExecutor js2 = (JavascriptExecutor) Present_driver;
		js2.executeScript("arguments[0].scrollIntoView();", StartSeminarSettingBtn);
		StartSeminarSettingBtn.click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=19)
	  public void CheckAndTryToDelete_defaultPresentation() throws Exception{
		String failMsg = "";

		Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();
		
		List<WebElement> youtube = Present_driver.findElements(By.xpath("//li[@class='youtube-item']"));
		
		if(youtube.size() != 1) {
			failMsg = "1. added youtube count error [Expected]1 [Actual]" + youtube.size();
		} 
		
		if(!youtube.get(0).findElement(By.xpath("//div[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[0])) {
			failMsg = failMsg + "\n 2. added youtube titie error [Expected]" + CommonValues.YOUTUBE_TITLE[0] 
					+ " [Actual]" + youtube.get(0).findElement(By.xpath("//div[@class='title']")).getText();
		}
		
		TimeUnit.SECONDS.sleep(2);
		
		//delete youtube
		Actions action = new Actions(Present_driver);
		Thread.sleep(1000);
		action.moveToElement(youtube.get(0)).build().perform();
		Thread.sleep(3000);
		Present_driver.findElement(By.xpath("//i[@class='ricon-close']")).click();
		
		WebDriverWait wait = new WebDriverWait(Present_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//section[@class='dialog-body']")), "Do you want to delete?"));
		
	    Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
	    
	    String Toast = Present_driver.findElement(By.xpath("//div[@class='wrap-toast-outer']/div/div/span")).getAttribute("innerHTML");
	    System.out.println(Toast);
	    
	    if(!Toast.contentEquals(CANNOT_DELETE_MSG)) {
	    	failMsg = failMsg + "\n 3. Wrong Toast msg [Expected]" + CANNOT_DELETE_MSG 
					+ " [Actual]" + Toast;
	    }
	    
	    Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();			
		List<WebElement> youtube_after = Present_driver.findElements(By.xpath("//li[@class='youtube-item']"));
		
		if(youtube_after.size() != 1) {
			failMsg = "\n 4. Delete youtube [Expected]1 [Actual]" + youtube_after.size();
		}
		
		TimeUnit.SECONDS.sleep(10);
		/*
		//delete document
		Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();
		String Documenttitle = Present_driver.findElement(By.xpath("//div[@class='title']")).getAttribute("title");
		System.out.println(Documenttitle);
		
		List<WebElement> doc = Present_driver.findElements(By.xpath("//li[@class='document-item']"));
		
		if(doc.size() != 1) {
			failMsg = "\n 5. added document count error [Expected]1 [Actual]" + doc.size();
		} 
		
		if(!doc.get(0).findElement(By.xpath("//div[@class='title']")).getText().contains(CommonValues.TESTFILE_LIST[0])) {
			failMsg = failMsg + "\n 6. added document titie error [Expected]" + CommonValues.TESTFILE_LIST[0] 
					+ " [Actual]" + doc.get(0).findElement(By.xpath("//div[@class='title']")).getText();
		}
		
		Actions action2 = new Actions(Present_driver);
		Thread.sleep(1000);
		action2.moveToElement(doc.get(0)).build().perform();
		Thread.sleep(3000);
		Present_driver.findElement(By.xpath("//i[@class='ricon-close']")).click();
		
		WebDriverWait wait2 = new WebDriverWait(Present_driver, 20);
	    wait2.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//section[@class='dialog-body']")), "Do you want to delete?"));
	    
	    Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
	    
	    String Toast2 = Present_driver.findElement(By.xpath("//div[@class='wrap-toast-outer']/div/div/span")).getAttribute("innerHTML");
	    System.out.println(Toast2);
	    
	    if(!Toast.contentEquals(CANNOT_DELETE_MSG)) {
	    	failMsg = failMsg + "\n 7. Wrong Toast msg [Expected]" + CANNOT_DELETE_MSG 
					+ " [Actual]" + Toast;
	    }
		
	    Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();			
		List<WebElement> doc_after = Present_driver.findElements(By.xpath("//li[@class='document-item']"));
		
		if(doc_after.size() != 1) {
			failMsg = "\n 8. Delete document [Expected]1 [Actual]" + doc_after.size();
		}
		
		TimeUnit.SECONDS.sleep(10);
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=20, dependsOnMethods = { "CheckAndTryToDelete_defaultPresentation"})
	  public void InsertPresentation_youtube() throws Exception{
		String failMsg = "";
		//add 1
		Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//button[@class='btn-rect plus']/span")).click();
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[1]);
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
		
		TimeUnit.SECONDS.sleep(10);
		
		//add 2
		Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//button[@class='btn-rect plus']/span")).click();
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[2]);
		Thread.sleep(1000);
		Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
		
		TimeUnit.SECONDS.sleep(2);
		
		Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();
		
		TimeUnit.SECONDS.sleep(2);
		
		List<WebElement> add_youtube = Present_driver.findElements(By.xpath("//li[@class='youtube-item']"));
		
		if(add_youtube.size() != 3) {
			failMsg = "1. added youtube count error [Expected]3 [Actual]" + add_youtube.size();
		} 
		/*
		if(!add_youtube.get(1).findElement(By.xpath("//div[1]/div[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[1])) {
			failMsg = failMsg + "\n 2. added youtube titie error [Expected]" + CommonValues.YOUTUBE_TITLE[1] 
					+ " [Actual]" + add_youtube.get(1).findElement(By.xpath("//div[@class='title']")).getText();
		}
	
		
		if(!add_youtube.get(2).findElement(By.xpath("//div[1]/div[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[2])) {
			failMsg = failMsg + "\n 3. added youtube titie error [Expected]" + CommonValues.YOUTUBE_TITLE[2] 
					+ " [Actual]" + add_youtube.get(2).findElement(By.xpath("//div[@class='title']")).getText();
		}
		*/
		TimeUnit.SECONDS.sleep(5);
		
		//delete 1
		Actions action = new Actions(Present_driver);
		Thread.sleep(1000);
		action.moveToElement(add_youtube.get(2)).build().perform();
		Thread.sleep(3000);
		Present_driver.findElement(By.xpath("//*[@id=\"presentation-buttons\"]/section[3]/ul/span[3]/li/button/span[2]/button[2]/i")).click();
		
		WebDriverWait wait2 = new WebDriverWait(Present_driver, 20);
	    wait2.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//section[@class='dialog-body']")), "Do you want to delete?"));
	    
	    Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
	    Thread.sleep(1000);
	    
	    Present_driver.findElement(By.xpath("//button[@title='Share on YouTube']")).click();
		Thread.sleep(1000);
		
		List<WebElement> delete_youtube_after = Present_driver.findElements(By.xpath("//li[@class='youtube-item']"));
		
		if(delete_youtube_after.size() != 2) {
			failMsg = "\n 4. all youtube count error [Expected]2 [Actual]" + delete_youtube_after.size();
		}
		
		TimeUnit.SECONDS.sleep(5);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=21, dependsOnMethods = { "CheckAndTryToDelete_defaultPresentation"}, enabled = false)
	  public void InsertPresentation_document() throws Exception{
		String failMsg = "";
		
		//add 1
		Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();
		Thread.sleep(1000);
		String testpng = filePath + CommonValues.TESTFILE_LIST[1];
		Present_driver.findElement(By.xpath("//button[@class='btn-rect plus']/input[1]")).sendKeys(testpng);
		
		TimeUnit.SECONDS.sleep(10);
		//add 2
		String testpng2 = filePath + CommonValues.TESTFILE_LIST[2];
		Present_driver.findElement(By.xpath("//button[@class='btn-rect plus']/input[1]")).sendKeys(testpng2);
		
		Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();
		TimeUnit.SECONDS.sleep(5);
		Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();
		
		List<WebElement> add_document = Present_driver.findElements(By.xpath("//li[@class='document-item']"));
		
		if(add_document.size() != 3) {
			failMsg = "1. added document count error [Expected]3 [Actual]" + add_document.size();
		} 
		/*
		if(!add_document.get(1).findElement(By.xpath("//div[@class='title']")).getText().contains(CommonValues.TESTFILE_LIST[1])) {
			failMsg = failMsg + "\n 2. added document titie error [Expected]" + CommonValues.TESTFILE_LIST[1] 
					+ " [Actual]" + add_document.get(1).findElement(By.xpath("//div[@class='title']")).getText();
		}
		
		if(!add_document.get(2).findElement(By.xpath("//div[@class='title']")).getText().contains(CommonValues.TESTFILE_LIST[2])) {
			failMsg = failMsg + "\n 3. added document titie error [Expected]" + CommonValues.TESTFILE_LIST[2] 
					+ " [Actual]" + add_document.get(2).findElement(By.xpath("//div[@class='title']")).getText();
		}
		*/
		TimeUnit.SECONDS.sleep(5);
		
		//delete 1
		Actions action = new Actions(Present_driver);
		Thread.sleep(1000);
		action.moveToElement(add_document.get(2)).build().perform();
		Thread.sleep(3000);
		Present_driver.findElement(By.xpath("//*[@id=\"presentation-buttons\"]/section[4]/ul/span[3]/li/div[1]/span/button[2]/i")).click();
		
		WebDriverWait wait2 = new WebDriverWait(Present_driver, 20);
	    wait2.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//section[@class='dialog-body']")), "Do you want to delete?"));
	    
	    Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']")).click();
	    Thread.sleep(1000);
	    
	    Present_driver.findElement(By.xpath("//button[@title='Share document']")).click();
	    
	    TimeUnit.SECONDS.sleep(5);
	    
		List<WebElement> delete_document_after = Present_driver.findElements(By.xpath("//li[@class='document-item']"));
		
		if(delete_document_after.size() != 2) {
			failMsg = "\n 4. all document count error [Expected]2 [Actual]" + delete_document_after.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=22)
	  public void RehearsalEnd() throws Exception{
		
		Present_driver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		WebDriverWait exit_popup = new WebDriverWait(Present_driver, 20);
		exit_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		Present_driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		WebDriverWait wait = new WebDriverWait(Present_driver, 20);
		wait.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//section[@class='dialog-body']/p")), "Do you want to end the seminar for all attendees?"));
		
		Present_driver.findElement(By.xpath("//div[@class='buttons']/div/button[1]")).click();
		Thread.sleep(1000);
		
		closedURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		
		if(!Present_driver.getCurrentUrl().contentEquals(closedURL)) {
			Exception e =  new Exception("Seminar not exit : " +  Present_driver.getCurrentUrl());
			throw e;
		}
		
		Present_driver.close();
		Thread.sleep(1000);
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
	
	}
	
	@Test(priority=23)
	  public void CheckPresentationinEdit() throws Exception{
		String failMsg = "";
		
		Publisher_driver.get(createViewURL + seminarID);
		Publisher_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		Publisher_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB4)).click();
		
		List<WebElement> docs = Publisher_driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if(docs.size() != 0) {
			failMsg = "Presentation count error [Expected]1 [Actual]" + docs.size();
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
	
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	} 
	
	public void setRehearsal(WebDriver driver, String title, boolean isnow) throws Exception  {
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).sendKeys(title);

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		if(isnow)
			cal.add(Calendar.DAY_OF_MONTH, 2);
		else
			cal.add(Calendar.MINUTE, 20);
		
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String seminarTime = format1.format(cal.getTime());

		driver.findElement(By.name("startTime")).sendKeys(seminarTime);
		
		if (isnow) {
			//click timepicker
			driver.findElement(By.xpath("//div[@class='react-datepicker__input-container']")).click();
			Thread.sleep(500);
			if (cal_today.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
				driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
				Thread.sleep(500);
			}
		
			CommonValues comm = new CommonValues();
			//Choose Thursday, March 26th, 2020
			String seminaDayForm = "//div[@aria-label='Choose %s, %s %s, %s']";
			String seminarDayPath = String.format(seminaDayForm, comm.getDayofWeek(cal.get(Calendar.DAY_OF_WEEK))
					, comm.getMonth(cal.get(Calendar.MONTH)), comm.getDay(cal.get(Calendar.DAY_OF_MONTH)), cal.get(Calendar.YEAR));
			
			System.out.println("******************seminarDayPath : " + seminarDayPath);
			if (isElementPresent(driver, By.xpath(seminarDayPath))) {
				if (driver.findElements(By.xpath(seminarDayPath)).size() == 2)
					driver.findElements(By.xpath(seminarDayPath)).get(1).click();
				else
					driver.findElement(By.xpath(seminarDayPath)).click();
			} else {
				System.out.println("@@@@@can not find day");
			}
		}
	}
	
	private boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}	
}