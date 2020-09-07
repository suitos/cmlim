package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* AttendeesTest
 * part1 게시자 rsrsup2
 * 1. 게시자가 세미나 생성(게시자 rsrsup2, 발표자 rsrsup2, 운영자 rsrsup6), 2명정원
 * 2. 발표자가 참석자 링크로 세미나 입장 rsrsup3
 * 3. 발표자가 채널 리스트 통해 세미나 입장 rsrsup3
 * 4. 발표자가 상세화면에서 세미나 입장 rsrsup3
 * 
 * 5. 게시자 상세화면에서 입장(rsrsup2). 게시자는 ghost user
 * 6. 게시자가 참석자 링크를 통해서 입장(rsrsup2). 게시자는 ghost user
 * 7. 게시자가 채널 리스트를 통해서 입장(rsrsup2). 게시자는 ghost user
 * 
 * 8. 운영자  참석자 링크를 통해서 화면에서 입장(rsrsup6)
 * 9. 운영자 채널 리스트를 통해서  화면에서 입장(rsrsup6)
 * 10. 운영자 상세 화면에서 입장(rsrsup6)
 * 
 * 11. 채널마스터 참석자 링크를 통해 입장(rsrsup1) 채널마스터 ghost user
 * 12. 채널마스터 리스트를 통해 입장(rsrsup1) 채널마스터 ghost user
 * 
 * 참석자 파트
 * 11. 참석자가 세미나 입장 정보 입력 화면 확인(세미나 정보 확인)
 * 12. 닉네임 필드 확인
 * 13. 이메일 필드 확인
 * 14. 회사 필드 확인
 * 15. 직책 필드 확인
 * 16. 전화번호 필드 확인
 * 17. 동의 체크박스 확인
 * 18. 일반 유저 입정정보 입력 후 세미나 입장
 * 19. 로그인한 유저가 세미나 입장 시 입장정보 확인  후 입장(rsrsup4) 
 * 20. 로그인한 유저  정보 수정하고 세미나 입장 (rsrsup4)
 * 21. 2명정원 세미나에  3번째 입장 시도
 * 
 * 22. 비회원 참석자 재입장
 * 
 * 31. 세미나 시작하기
 * 32. 세미나 종료하기
 * 33. 종료화면 확인
 * 34. 종료 된 세미나 삭제
 */

public class AttendeesTest{
	public static String XPATH_VIEW_JOIN = "//button[@class='btn btn-primary btn-auto actionButton']";
	public static String XPATH_ATTEND_ENTER = "//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']";
	
	public static String XPATH_ATTEND_NICKNAME = "//input[@name='nickname']";
	public static String XPATH_ATTEND_EMAIL = "//input[@name='email']";
	public static String XPATH_ATTEND_COMPANY = "//input[@name='companyName']";
	public static String XPATH_ATTEND_POSITION = "//input[@name='jobPosition']";
	public static String XPATH_ATTEND_PHONE = "//input[@name='phone']";
	public static String XPATH_ATTEND_ERROR = "/../../span[@class='public-apply__errorMsg']";
	
	public static String XPATH_ATTEND_AGREE_CHECKBOX1 = "//div[@class='public-apply__attendeeInfo__terms']/div[@class='checkbox'][1]";
	public static String XPATH_ATTEND_AGREE_CHECKBOX2 = "//div[@class='public-apply__attendeeInfo__terms']/div[@class='checkbox'][2]";
	public static String XPATH_ATTEND_AGREE_ERROR = "//div[@class='public-apply__attendeeInfo__terms']/span[@class='public-apply__attendeeInfo__terms__error-msg']";
	
	public static String MSG_ATTEND_AGREE_ERROR = "blabla";
	
	public static String USER_PUBLISHER = "rsrsup2";
	public static String USER_PRESENTER = "rsrsup3";
	public static String USER_ORGANIZER = "rsrsup6";
	public static String USER_MASTER = "rsrsup1";
	public static String MSG_SEMINAR_FULL = "This seminar is full.";
	public static String URI_SEMINAR_CLOSED = "/seminar/close";
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	public static String ROLE_PRESENER = "Presenter";
	public static String ROLE_ORGANIAER = "Organizer";
	
	public static WebDriver publisherDriver;
	public static WebDriver driver; //발표자
	public static WebDriver attendeesDriver;
	public static WebDriver memberDriver;
	public static WebDriver sameuserDriver;
	
	public static String seminarTitle = "";
	public static String seminarDate_T1 = "";
	public static String seminarDate_T2 = "";

	public static List<String> members = null;
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarID = "";
	public String seminarLink = "";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		members = new ArrayList<String>();
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		publisherDriver = comm.setDriver(publisherDriver, browsertype, "lang=en_US");
		attendeesDriver = comm.setDriver(attendeesDriver, browsertype, "lang=en_US");
		memberDriver = comm.setDriver(memberDriver, browsertype, "lang=en_US");
		sameuserDriver = comm.setDriver(sameuserDriver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", publisherDriver);
		context.setAttribute("webDriver3", attendeesDriver);
		context.setAttribute("webDriver4", memberDriver);
		context.setAttribute("webDriver5", sameuserDriver);
		
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");
	}

	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(publisherDriver, CommonValues.USEREMAIL_PRES);
	}
	
	@Test(priority = 1)
	public void createseminar() throws Exception {
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		startTime.add(Calendar.MINUTE, 5);
		//startTime.add(Calendar.HOUR, 2);
		SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");
		String seminarTime = format1.format(startTime.getTime());
		
		//get seminar end time
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(time);
		endTime.add(Calendar.MINUTE, 30 + 5);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		seminarDate_T1 = format2.format(startTime.getTime()) + " ~ " + format1.format(endTime.getTime());
		seminarDate_T2 = format2.format(startTime.getTime()) + " ~ " + format2.format(endTime.getTime());
		
		//create seminar after 5minute
		seminarTitle = format2.format(startTime.getTime());
		String seminarUri = createSeminar(seminarTitle, seminarTime, "2");
		seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarUri;
		seminarID = seminarUri;
	
		String failMsg = "";
		
		/*
		//compleate view banner
		String compleateUri = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI + seminarID;

		if(publisherDriver.getCurrentUrl().contentEquals(compleateUri))
		{
			if(!publisherDriver.findElement(By.xpath("//div[@class='wrap-text']/p[@class='title']")).getText().trim().contentEquals(seminarTitle))
			{
				failMsg = "seminar title error in compleate view [expected] " + seminarTitle
						+ " [actual] " + publisherDriver.findElement(By.xpath("//div[@class='wrap-text']/p[@class='title']")).getText().trim();
				
			}
			//date : node name error!!!
			if(!publisherDriver.findElement(By.xpath("//div[@class='author']")).getText().trim().contentEquals(seminarDate_T1))
			{
				failMsg = failMsg + "\n" + "seminar date error in compleate view [expected] " + seminarDate_T1
						+ " [actual] " + publisherDriver.findElement(By.xpath("//div[@class='author']")).getText().trim();
			}
			//author
			if(!publisherDriver.findElement(By.xpath("//div[@class='date']")).getText().trim().contentEquals("rsrsup2"))
			{
				failMsg = failMsg + "\n" + "seminar date-author error in compleate view [expected] " + "rsrsup2"
						+ " [actual] " + publisherDriver.findElement(By.xpath("//div[@class='date']")).getText().trim();
			}
		}
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	// 2. 발표자가 참석자 링크로 세미나 입장 rsrsup3
	@Test(priority = 2, dependsOnMethods = { "createseminar"})
	public void presenter_attendeesLink() throws Exception {
		String failMsg = "";
		
		//발표자가 login(rsrsup3)
		driver.get(CommonValues.SERVER_URL);;
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_PRESENTER + "@gmail.com");
		
	    Thread.sleep(1000); 
		
		//go to link
		driver.get(seminarLink);
		Thread.sleep(500);

		failMsg = roomCheck_OP(driver, ROLE_PRESENER);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 3. 발표자가 채널 리스트 통해 세미나 입장 rsrsup3
	@Test(priority = 3, dependsOnMethods = { "createseminar"})
	public void presenter_channelList() throws Exception {
		String failMsg = "";
		
		//go to seminar by channel
		seminarInfo_ChannelList(driver, seminarTitle);
		Thread.sleep(500);

		failMsg = roomCheck_OP(driver, ROLE_PRESENER);
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 4. 발표자 입장(rsrsup3)
	@Test(priority = 4, enabled = true)
	public void presenterEnter() throws Exception {

		String failMsg = "";
	
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
	    driver.get(detailview);
	    Thread.sleep(1000);
	    
	    failMsg = roomCheck_OP(driver, ROLE_PRESENER);
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 5. 게시자 상세화면에서 입장(rsrsup2). 게시자는 ghost user
	@Test(priority = 5, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void publisherRoom() throws Exception {
		String failMsg = "";
		
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
	   
		publisherDriver.get(detailview);
		Thread.sleep(1000);

		failMsg = roomCheck_ghost(publisherDriver, "publisher");
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	// 6. 게시자가 참석자 링크를 통해서 입장(rsrsup2). 게시자는 ghost user
	@Test(priority = 6, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void publisherRoom_attendeesLink() throws Exception {
		String failMsg = "";
		
		//go to link
		publisherDriver.get(seminarLink);
		Thread.sleep(500);

		failMsg = roomCheck_ghost(publisherDriver, "publisher");
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 7. 게시자가 채널 리스트를 통해서 입장(rsrsup2). 게시자는 ghost user
	@Test(priority = 7, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void publisherRoom_channelInfo() throws Exception {
		String failMsg = "";
		
		//go to link
		seminarInfo_ChannelList(publisherDriver, seminarTitle);
		Thread.sleep(2000);

		failMsg = roomCheck_ghost(publisherDriver, "publisher");
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 8. 운영자 상세화면에서 입장(rsrsup6).
	@Test(priority = 8, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void organizerRoom() throws Exception {
		String failMsg = "";
		
		//운영자 login(rsrsup6)
		sameuserDriver.get(CommonValues.SERVER_URL);
		CommonValues comm = new CommonValues();
		comm.loginseminar(sameuserDriver, USER_ORGANIZER + "@gmail.com");
		
		Thread.sleep(1000);		
		
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
	   
		sameuserDriver.get(detailview);
		Thread.sleep(500);

		failMsg = roomCheck_OP(sameuserDriver, ROLE_ORGANIAER);
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	// 9. 운영자 참석자 링크를 통해 입장(rsrsup6).
	@Test(priority = 9, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void organizerRoom_attendeesLink() throws Exception {
		String failMsg = "";
		
		//go to link
		sameuserDriver.get(seminarLink);
		Thread.sleep(500);

		failMsg = roomCheck_OP(sameuserDriver, ROLE_ORGANIAER);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 10. 운영자 채널 리스트를 통해서 입장(rsrsup6). 
	@Test(priority = 10, dependsOnMethods = { "createseminar", "presenterEnter" }, alwaysRun = true, enabled = true)
	public void organizerRoom_channelInfo() throws Exception {
		String failMsg = "";
		
		//go to link
		seminarInfo_ChannelList(sameuserDriver, seminarTitle);
		Thread.sleep(500);

		failMsg = roomCheck_OP(sameuserDriver, ROLE_ORGANIAER);
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
		sameuserDriver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
	}

	// 11. 채널마스터 참석자 링크를 통해 입장(rsrsup1).
	@Test(priority = 11, dependsOnMethods = { "createseminar", "organizerRoom_channelInfo" }, alwaysRun = true, enabled = true)
	public void masterRoom_attendeesLink() throws Exception {
		String failMsg = "";
		
		sameuserDriver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
		//채널마스터  login(rsrsup1)
		sameuserDriver.get(CommonValues.SERVER_URL);
		CommonValues comm = new CommonValues();
		comm.loginseminar(sameuserDriver, USER_MASTER + "@gmail.com");
		
		Thread.sleep(1000);
		
		//go to link
		sameuserDriver.get(seminarLink);
		Thread.sleep(500);

		failMsg = roomCheck_ghost(sameuserDriver, "master");
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 12. 채널마스터 채널 리스트를 통해서 입장(rsrsup1). 
	@Test(priority = 12, dependsOnMethods = { "createseminar", "masterRoom_attendeesLink" }, alwaysRun = true, enabled = true)
	public void masterRoom_channelInfo() throws Exception {
		String failMsg = "";
		
		//go to link
		seminarInfo_ChannelList(sameuserDriver, seminarTitle);
		Thread.sleep(500);

		failMsg = roomCheck_ghost(sameuserDriver, "master");
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
		sameuserDriver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
	}	
	
	
	// 11. 참석자가 세미나 입장 정보 입력 화면 확인(세미나 정보 확인)
	@Test(priority = 11, enabled = true)
	public void attendeesInfo() throws Exception {

		attendeesDriver.get(seminarLink);
		Thread.sleep(2000);
		
		//click join
		attendeesDriver.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(500);
		
		String infoFailMsg = "";
		
		//view title
		if(!attendeesDriver.findElement(By.xpath("//h2[@class='PublicApply_public-apply__title__oUTIx']")).getText().trim().contentEquals(CommonValues.ATTENDEES_VIEW_TITLE)) {
			
			infoFailMsg = "1. Attendees View title : [expected]" + CommonValues.ATTENDEES_VIEW_TITLE + " [actual]" + attendeesDriver.findElement(By.xpath("//h2[@class='PublicApply_public-apply__title__oUTIx']")).getText().trim();
		
		}
		
		//check seminar info
		if(!attendeesDriver.findElement(By.xpath("//h3[@class='public-apply__seminarInfo__title']")).getText().trim().contentEquals(seminarTitle)) {
			
			infoFailMsg = infoFailMsg + "\n 2.wrong Seminar msg : " + attendeesDriver.findElement(By.xpath("//h3[@class='public-apply__seminarInfo__title']")).getText().trim();
		
		}
		if(!attendeesDriver.findElement(By.xpath("//p[@class='public-apply__seminarInfo__date']")).getText().trim().contentEquals(seminarDate_T2)) {
			
			infoFailMsg = infoFailMsg + "\n 3.wrong Seminar date : [expected]" + seminarDate_T2 
					+ " [actual]" +  attendeesDriver.findElement(By.xpath("//p[@class='public-apply__seminarInfo__date']")).getText().trim();
		
		}
		if(!attendeesDriver.findElement(By.xpath("//p[@class='public-apply__seminarInfo__presenters']")).getText().trim().contentEquals(USER_PRESENTER)) {
			infoFailMsg = infoFailMsg + "\n 4.wrong Seminar presenter : " + attendeesDriver.findElement(By.xpath("//p[@class='public-apply__seminarInfo__presenters']")).getText().trim();
	
		}
		
		if(infoFailMsg !=null && !infoFailMsg.isEmpty())
		{
			Exception e =  new Exception(infoFailMsg);
	    	throw e;
		}
	}
	
	// 12. 닉네임 필드 확인
	@Test(priority=12, enabled = true)
	public void checkNickNamefield() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		
		String infoFailMsg = "";

	    //check empty values
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(500);
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME + XPATH_ATTEND_ERROR)).getText().contentEquals(CommonValues.ATTENDEES_NICKNAME_MSG))
		{
			infoFailMsg = "wrong error msg(empty nickname) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME + XPATH_ATTEND_ERROR)).getText();
		}
		
	
	    //long case (max 20)
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys(CommonValues.TWENTY_A + "111");
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).click();

	    Thread.sleep(500);
	    if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
			infoFailMsg = infoFailMsg + "\n" + "wrong error msg(long nickname(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value");
	    }
	  
	    //check wrong format
	    for(int i=0 ; i < CommonValues.NICKNAME_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)));
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys(CommonValues.NICKNAME_WRONG_CASE[i]);
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).click();
		    Thread.sleep(500);
		    if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value").contentEquals(CommonValues.USERNICKNAME_JOIN))
		    {
				infoFailMsg = infoFailMsg + "\n" + "fail worng nickname format : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value");
			
		    }
		    
	    }
	    
	    if(infoFailMsg != null && !infoFailMsg.isEmpty())
		{
			Exception e =  new Exception(infoFailMsg);
	    	throw e;
		}
	}
	
	// 13. 이메일 필드 확인
	@Test(priority=13, enabled = true)
	public void checkEmailNamefield() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).clear();
	    
		boolean emailtest = true;
		String infoFailMsg = "";

		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(500);
		
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL + XPATH_ATTEND_ERROR)).getText().contentEquals(CommonValues.PLACEHOLDER_EMAIL))
		{
			emailtest = false;
			infoFailMsg =  "wrong error msg(empty email) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL + XPATH_ATTEND_ERROR)).getText();
		
		}
		
	
		/* 길이 처리 안함
	    //long case (max 20)
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).sendKeys(CommonValues.TWENTY_A + "111");
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).click();
	    Thread.sleep(500);
	    if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	emailtest = false;
			infoFailMsg = infoFailMsg + "\n" + "wrong error msg(long email(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value");
	    	
	    }
	    */
	  
	    //check wrong format
	    for(int i=0 ; i < CommonValues.EMAIL_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)));
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).clear();
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).sendKeys(CommonValues.EMAIL_WRONG_CASE[i]);
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
			Thread.sleep(500);
		    if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL + XPATH_ATTEND_ERROR)).getText().contentEquals(CommonValues.ATTENDEES_EMAIL_MSG))
		    {
		    	emailtest = false;
				infoFailMsg = infoFailMsg + "\n" + "fail worng email format : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value");
		    	
		    }
		    
	    }
	    if(!emailtest)
		{
			Exception e =  new Exception(infoFailMsg);
	    	throw e;
		}
		
	}
	
	// 14. 회사 필드 확인
	@Test(priority=14, enabled = true)
	public void checkCompanyfield() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();

		String failMsg = "";
	
	    //long case (max 20)
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).sendKeys(CommonValues.TWENTY_A + "111");
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
	
	    Thread.sleep(500);
	    if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	failMsg = "wrong error msg(long companyName(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).getAttribute("value");
	    }
	  
	    //check wrong format
	    for(int i=0 ; i < CommonValues.COMPANY_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)));
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).sendKeys(CommonValues.COMPANY_WRONG_CASE[i]);
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
			Thread.sleep(500);
			if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).getAttribute("value").contentEquals(CommonValues.USER_COMPANY))
		    {
		    	failMsg = failMsg + "\n" + "wrong company name : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).getAttribute("value");

		    }
		    
	    }
	    
	    if(failMsg !=null && !failMsg.isEmpty())
		{
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 15. 직책 필드 확인
	@Test(priority=15, enabled = true)
	public void checkPositionfield() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
	
		boolean isPass = true;
		String failMsg = "";
		
	    //long case (max 20)
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).sendKeys(CommonValues.TWENTY_A + "111");
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).click();
	    Thread.sleep(500);
	    if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	isPass = false;
	    	failMsg = "wrong error msg(long position(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).getAttribute("value");
	    
	    }
	  
	    //check wrong format
	    for(int i=0 ; i < CommonValues.POSITION_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)));
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).sendKeys(CommonValues.POSITION_WRONG_CASE[i]);
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).click();
			Thread.sleep(500);
			if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).getAttribute("value").contentEquals(CommonValues.USER_POSITION))
		    {
				isPass = false;
		    	failMsg = failMsg + "\n" + "wrong position name : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).getAttribute("value");
		    	//Exception e =  new Exception("wrong position name : " + attendeesDriver.findElement(By.id("position")).getAttribute("value"));
		    	//throw e;
		    }
		    
	    }
	    if(!isPass)
		{
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 16. 전화번호 필드 확인
	@Test(priority=16, enabled = true)
	public void checkPhonefield() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
	    
		boolean isPass = true;
		String failMsg = "";
		
	
	    //long case (max 20)
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).sendKeys(CommonValues.TWENTY_ONE + "222");
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
	    Thread.sleep(500);
	    if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value").contentEquals(CommonValues.TWENTY_ONE))
	    {
	    	isPass = false;
	    	failMsg = "wrong error msg(long phone(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value");
	    	//Exception e =  new Exception("wrong error msg(long phone(23bytes) : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value"));
	    	//throw e;
	    }
	  
	    //check wrong format
	    for(int i=0 ; i < CommonValues.PHONE_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)));
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).sendKeys(CommonValues.PHONE_WRONG_CASE[i]);
	    	attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
			Thread.sleep(500);
			if (!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value").contentEquals(CommonValues.USER_PHONE))
		    {
				isPass = false;
		    	failMsg = failMsg + "\n" + "wrong phone name : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value");
		    	//Exception e =  new Exception("wrong phone name : " + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).getAttribute("value"));
		    	//throw e;
		    }
		    
	    }
	    if(!isPass)
		{
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	//17. 동의 체크박스 확인
	@Test(priority=17, enabled = true)
	public void userAgreement() throws Exception 
	{
		String failMsg = "";
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys(ATTENDEES_NICKNAME);
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).sendKeys(ATTENDEES_EMAIL);
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(500);
		
		// defalut 
		if(attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1 + "//input")).isSelected() 
				|| attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected()) {
			failMsg = failMsg + "1. user agreement checkbox state. [Exepcted] unchecked, checkbox1:" 
				+ attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected() 
					+ " checkbox2:" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected();
		}
		
		if(isElementPresent_wd(attendeesDriver, By.xpath(XPATH_ATTEND_AGREE_ERROR))) {
			if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText().contentEquals(MSG_ATTEND_AGREE_ERROR)) {
				failMsg = failMsg + "\n2. user agreement error message. [Exepcted]" + MSG_ATTEND_AGREE_ERROR 
						+ " [Actual]" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText();
			}
		} else {
			failMsg = failMsg + "\n3. not find error message.";
		}
		//1check
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		Thread.sleep(100);
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(500);
		if(isElementPresent_wd(attendeesDriver, By.xpath(XPATH_ATTEND_AGREE_ERROR))) {
			if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText().contentEquals(MSG_ATTEND_AGREE_ERROR)) {
				failMsg = failMsg + "\n4. user agreement error message. [Exepcted]" + MSG_ATTEND_AGREE_ERROR 
						+ " [Actual]" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText();
			}
		} else {
			failMsg = failMsg + "\n5. not find error message.";
		}
		
		// upcheck a, check b
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(500);
		if(isElementPresent_wd(attendeesDriver, By.xpath(XPATH_ATTEND_AGREE_ERROR))) {
			if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText().contentEquals(MSG_ATTEND_AGREE_ERROR)) {
				failMsg = failMsg + "\n6. user agreement error message. [Exepcted]" + MSG_ATTEND_AGREE_ERROR 
						+ " [Actual]" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_ERROR)).getText();
			}
		} else {
			failMsg = failMsg + "\n7. not find error message.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 18. 일반 유저 입정정보 입력 후 세미나 입장 시도
	@Test(priority=18, enabled = true)
	public void joinAttendeesNormalInfo() throws Exception 
	{
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys(ATTENDEES_NICKNAME);
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).sendKeys(ATTENDEES_EMAIL);
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
		
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2)).click();
		
		//join seminar
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(1000);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendeesDriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("fail to join Seminar : " + attendeesDriver.getCurrentUrl());
	    	throw e;
	    }
		members.add(ATTENDEES_NICKNAME);
	}
	
	
	// 19. 로그인한 유저가 세미나 입장 시 입장정보 확인  후 입장(rsrsup4) 
	@Test(priority = 19, enabled = true)
	public void memberJoinInfo() throws Exception {
		//login seminar
		memberDriver.get(CommonValues.SERVER_URL);
		Thread.sleep(500);
		CommonValues comm = new CommonValues();
		comm.loginseminar(memberDriver, CommonValues.USEREMAIL_RSUP4);
		
	    Thread.sleep(1000); 
	    
		
	    // go seminar
		memberDriver.get(seminarLink);
		Thread.sleep(500);
		
		JavascriptExecutor js = (JavascriptExecutor) memberDriver;
		js.executeScript("arguments[0].scrollIntoView();", memberDriver.findElement(By.xpath(XPATH_VIEW_JOIN)));
		
		memberDriver.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(500);

		//check member info
		//nickname, company, position, phone field is enabled

		String infoFailMsg = "";
		
		String nickname = CommonValues.USEREMAIL_RSUP4.split("@")[0];
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value").contentEquals(nickname) 
				|| !memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).isEnabled())
		{
			infoFailMsg = "member info(nickname) [default] " + memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value")
					+ " [enable]" + memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).isEnabled();
			
		}
		
		//email field is disabled
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value").contentEquals(CommonValues.USEREMAIL_RSUP4) 
				|| memberDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).isEnabled())
		{
			infoFailMsg = "\n" + "member info to join(email) [default] " + memberDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value")
					+ " [enable]" + memberDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).isEnabled();
	
		}
		
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2)).click();
		
		// 입장
		// join seminar
		memberDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(1000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		// check room uri
		if (!roomuri.equalsIgnoreCase(memberDriver.getCurrentUrl())) {
			Exception e = new Exception("fail to join Seminar : " + memberDriver.getCurrentUrl());
			throw e;
		}
		
		if(infoFailMsg !=null && !infoFailMsg.isEmpty())
		{
			Exception e =  new Exception(infoFailMsg);
			throw e;
		}
		
	}
	
	// 20. 로그인한 유저  정보 수정하고 세미나 입장 (rsrsup4)
	@Test(priority = 20, enabled = true)
	public void memberJoinSeminar() throws Exception {

		// go seminar
		memberDriver.get(seminarLink);
		Thread.sleep(500);
		
		JavascriptExecutor js = (JavascriptExecutor) memberDriver;
		js.executeScript("arguments[0].scrollIntoView();", memberDriver.findElement(By.xpath(XPATH_VIEW_JOIN)));
		
		memberDriver.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(1000);

		memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).click();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).clear();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys("rsrsup4");
		
		String username = "member!";
		String userCompany = username + CommonValues.USER_COMPANY;
		memberDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).click();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).sendKeys(userCompany);
		
		String userPosition = username + CommonValues.USER_POSITION;
		memberDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).sendKeys(userPosition);
		
		memberDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
		
		js.executeScript("arguments[0].scrollIntoView();", memberDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)));
		
		memberDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).click();
		memberDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).sendKeys("01012345678");
		
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		if(!memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			memberDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2)).click();
		
		// join seminar
		memberDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(1000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		// check room uri
		if (!roomuri.equalsIgnoreCase(memberDriver.getCurrentUrl())) {
			Exception e = new Exception("fail to join Seminar : " + memberDriver.getCurrentUrl());
			throw e;
		}

		members.add("rsrsup4");
	}

	// 21. 2명정원 세미나에  3번째 입장 시도
	@Test(priority = 21, dependsOnMethods = { "memberJoinSeminar", "masterRoom_channelInfo" }, alwaysRun = true, enabled = true)
	public void joinfullseminar() throws Exception {

		//seminar link
		sameuserDriver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		
		sameuserDriver.get(seminarLink);
		Thread.sleep(1000);
		sameuserDriver.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(1000);
		
		assertEquals(closeAlertAndGetItsText_webdriver(sameuserDriver), MSG_SEMINAR_FULL);
		
	}

	
	// 21. 발표자 룸에서 멤버 확인
	@Test(priority = 21, enabled = true)
	public void memberCheck() throws Exception {
		String failMsg = "";


		// 발표자가 인원탭에서 발표자 확인
		if (!driver.findElement(By.xpath("//div[@class='Participants_header__fsxhk']")).isDisplayed()) {
			driver.findElement(By.xpath("//i[@class='ricon-user-friends']")).click();
		}
		Thread.sleep(1000);
		List<WebElement> membersList = driver.findElements(By.xpath("//div[@class='ParticipantsList_container-list__29aAV']/div"));

		for (int i = 0; i < membersList.size(); i++) {
			// 발표자가 인원탭에서 발표자 확인
			if (membersList.get(i).findElement(By.xpath(".//p[@class='ParticipantsList_title__TiBqy']")).getText()
					.contentEquals(ROLE_PRESENER)) {
				List<WebElement> pres = membersList.get(i).findElements(By.xpath(".//li[@class='ParticipantsItem_list-item__IAFQA']"));
				boolean findPres = false;
				if (pres.size() >= 1) {
					for (int j = 0; j < pres.size(); j++) {
						if (pres.get(j).findElement(By.xpath(".//p[@class='ParticipantsItem_nickname__215Mz ParticipantsItem_oneself__2hdsv']/span[1]"))
								.getText().contentEquals(USER_PRESENTER)) {
							findPres = true;
							
						}
					}
				}
				if (!findPres) {
					failMsg = failMsg + "\n 1. cannot find Presenter in user list";
				}
			// 발표자가 인원탭에서 발표자 확인
			} else if (membersList.get(i).findElement(By.xpath(".//p[@class='ParticipantsList_title__TiBqy']"))
					.getText().contentEquals("Attendees")) {
				List<WebElement> users = membersList.get(i).findElements(By.xpath(".//li[@class='ParticipantsItem_list-item__IAFQA']"));
				boolean findPres = false;
				List<String> username = new ArrayList<String>();
				if (users.size() >= 1) {
					for (int j = 0; j < users.size(); j++) {
						username.add(users.get(j).findElement(By.xpath(".//p[@class='ParticipantsItem_nickname__215Mz false']/span")).getText());
						if (username.get(j).contentEquals(members.get(j))) {
							
							findPres = true;
						} else {
							findPres = false;
						}
					}
				}
				if (!findPres) {
					failMsg = failMsg + "\n 2. cannot find Attendees in user list [Expected]" + members + " [Actual]" + username;
				}
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 비회원 참석자 재입장
	@Test(priority = 22, dependsOnMethods = {"joinAttendeesNormalInfo"}, alwaysRun = true, enabled = true)
	public void reentry_nonmember() throws Exception {
		String failMsg = "";
		
		//click exit
		attendeesDriver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		Thread.sleep(500);
		
		//click exit in popup
		attendeesDriver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(3000);
		
		if(!attendeesDriver.getCurrentUrl().contains(seminarLink)) {
			failMsg = "1. not seminar link view current url : " + attendeesDriver.getCurrentUrl();
			Thread.sleep(1000);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) attendeesDriver;
		js.executeScript("arguments[0].scrollIntoView();", attendeesDriver.findElement(By.xpath(XPATH_VIEW_JOIN)));
		
		// click join
		attendeesDriver.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(500);
		

		//name check
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value").contentEquals(ATTENDEES_NICKNAME)) {
			failMsg = failMsg + "\n 2. nickname missmatch [Expected]" + ATTENDEES_NICKNAME 
					+ " [Actual]" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value");
		}
		//email check
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_EMAIL)).getAttribute("value").contentEquals(ATTENDEES_EMAIL)) {
			failMsg = failMsg + "\n 2. email missmatch [Expected]" + ATTENDEES_EMAIL 
					+ " [Actual]" + attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).getAttribute("value");
		}	
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_COMPANY)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_NICKNAME)).sendKeys("test company");
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).clear();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_POSITION)).sendKeys("test position");
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).clear();
		
		js.executeScript("arguments[0].scrollIntoView();", attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)));
		
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).click();
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_PHONE)).sendKeys("01012345678");
		
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX1)).click();
		if(!attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			attendeesDriver.findElement(By.xpath(XPATH_ATTEND_AGREE_CHECKBOX2)).click();
		
		//join seminar
		attendeesDriver.findElement(By.xpath(XPATH_ATTEND_ENTER)).click();
		Thread.sleep(1000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		// check room uri
		if (!roomuri.equalsIgnoreCase(attendeesDriver.getCurrentUrl())) {
			failMsg = failMsg + "\n 3. fail to join Seminar : " + attendeesDriver.getCurrentUrl();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 31. 세미나 시작하기
	@Test(priority = 31, enabled = true)
	public void startSeminar() throws Exception {
		boolean isPass = true;
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		String xpath_onair = "//strong[@id='user-type']";
		// on air tag  : presenter
		if (!driver.findElement(By.xpath(xpath_onair)).getAttribute("class").contains("onair")) {
			isPass = false;
			failMsg = "[presenter]Seminar Tag is wrorng : " + driver.findElement(By.xpath(xpath_onair)).getAttribute("class");
			//Exception e = new Exception("Seminar Tag is wrorng : " + driver.findElement(By.id("user-type")).getText());
			//throw e;
		}
		
		// on air tag : attendees 
		if (!attendeesDriver.findElement(By.xpath(xpath_onair)).getAttribute("class").contains("onair")) {
			isPass = false;
			failMsg = failMsg + "\n" + "[attendees : no memeber ]Seminar Tag is wrorng : " + attendeesDriver.findElement(By.xpath(xpath_onair)).getAttribute("class");
			//Exception e = new Exception("Seminar Tag is wrorng : " + attendeesDriver.findElement(By.id("user-type")).getText());
			//throw e;
		}
		if (!memberDriver.findElement(By.xpath(xpath_onair)).getAttribute("class").contains("onair")) {
			
			isPass = false;
			failMsg = failMsg + "\n" + "[attendees : memeber ]Seminar Tag is wrorng : " + memberDriver.findElement(By.xpath(xpath_onair)).getAttribute("class");
			//Exception e = new Exception("Seminar Tag is wrorng : " + memberDriver.findElement(By.id("user-type")).getText());
			//throw e;
		}
		
		if(!isPass)
		{
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 32. 세미나 종료하기
	@Test(priority = 32, enabled = true)
	public void closeSeminar() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(4000);
		//presenter
		//assertEquals(closeAlertAndGetItsText(), CommonValues.SEMINAR_CLOSE_MSG);
		if (!findAlert(driver,CommonValues.SEMINAR_CLOSE_MSG )) failMsg = "0. cannot find alert (presenter)";
		if (!findAlert(publisherDriver,CommonValues.SEMINAR_CLOSE_MSG )) failMsg = "1. cannot find alert (publisher)";
		if (!findAlert(attendeesDriver,CommonValues.SEMINAR_CLOSE_MSG )) failMsg = failMsg + "\n 2. cannot find alert (attend)";
		if (!findAlert(memberDriver,CommonValues.SEMINAR_CLOSE_MSG )) failMsg = failMsg + "\n 3. cannot find alert (member)";


		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 33. 종료화면 확인
	@Test(priority = 33, enabled = true)
	public void closedSeminarView() throws Exception {
		String failMsg = "";
		
		// 종료 화면 확인
		String closedurl = CommonValues.SERVER_URL + URI_SEMINAR_CLOSED + seminarID;
		if(!driver.getCurrentUrl().contentEquals(closedurl)) {
			failMsg = failMsg +"1. no closed view after close seminar [Expected]" + closedurl 
					+ " [Actual]" + publisherDriver.getCurrentUrl();
			driver.get(closedurl);
			Thread.sleep(500);
		}
		
		// title
		if (!driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText().contentEquals(seminarTitle)) {
			failMsg = failMsg +"\n 2. seminar title error in closed view [Expected]" + seminarTitle 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='wrap-text']/p")).getText();
		}
		// date
		if (!driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText().contentEquals(seminarDate_T1)) {
			failMsg = failMsg +"\n 3. seminar date error in closed view [Expected]" + seminarDate_T1 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='author']")).getText();
		}
		// author
		if (!driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='date']")).getText().contentEquals(USER_PRESENTER)) {
			failMsg = failMsg +"\n 4. seminar author error in closed view [Expected]" + USER_PRESENTER 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='wrap-text']/div[@class='date']")).getText();
		}		
		
		// go to preview : presenter
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons-wrap']/button[2]")).click();
		
		String closeduri = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		//status after close seminar
		if(!attendeesDriver.getCurrentUrl().contentEquals(closeduri))
		{
			failMsg = failMsg + "\n 5. [Fail] no member, after closed seminar : (expected)" + closeduri 
					+ ", (actual) " + attendeesDriver.getCurrentUrl();
		}
		
		//status after close seminar : member
		
		if (!memberDriver.getCurrentUrl().contentEquals(closeduri)) {
			failMsg = failMsg + "\n" + "6.[Fail] member, after closed seminar : (expected)" + closeduri+ ", (actual) "+ memberDriver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public String createSeminar(String seminarName, String seminarTime, String attendees) throws Exception {
		
		if(!publisherDriver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			publisherDriver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(500);
		}
		publisherDriver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
	    Thread.sleep(2000);
	    
		//channel
		//click channel select
		publisherDriver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(500);

		// channel popup : channel list
		List<WebElement> channelList = publisherDriver
				.findElements(By.xpath("//div[@class='radio-channelId']/div[@class='Radio_radioBox__2VtPF radio']"));

		for (int i = 0; i < channelList.size(); i++) {
			if (channelList.get(i).findElement(By.xpath(".//span[1]")).getText().contentEquals("rsrsup1")) {
				// click second channel
				channelList.get(i).findElement(By.xpath(".//span[1]")).click();
			}
		}
		Thread.sleep(500);
		
		// click confirm
		publisherDriver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(2000);
		
	    publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).click();
	    publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
	    publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(seminarName);
	    
	    //set attendees
	    publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).click();
		publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).clear();
		CommonValues comm = new CommonValues();
		comm.selectAll(publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)));
		//publisherDriver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).sendKeys(Keys.CONTROL, "a");
		publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).sendKeys(attendees);
		Thread.sleep(500);


	    publisherDriver.findElement(By.name("startTime")).sendKeys(seminarTime);
	    Thread.sleep(1000);
	    
			
		// 세미나 멤버 
		publisherDriver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]")).click();
		Thread.sleep(500);
		//presenter popup
		publisherDriver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).clear();
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_PRESENTER);
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		//select rsrsup1
		publisherDriver.findElement(By.xpath("//li[@role='presentation']/span[@class='member-email']")).click();
		Thread.sleep(500);

		// click selected
		publisherDriver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
		
		//delete member 
		List<WebElement> member_pres = publisherDriver.findElements(By.xpath("//div[@role='presentation']"));
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(1000);
		
		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		publisherDriver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).clear();
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_ORGANIZER);
		publisherDriver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		// select 1 (defalut 0 + 1)
		publisherDriver.findElement(By.xpath("//li[@role='presentation']//span[@class='member-name']")).click();
		
		// click selected
		publisherDriver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(1000);
		
		
		// save seminar
		publisherDriver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(publisherDriver, seminarName);
		comm.postSeminar(publisherDriver, seminarUri);

	    if(seminarUri == null || seminarUri.isEmpty())
	    {
	    	Exception e =  new Exception("fail to create seminar : " + publisherDriver.getCurrentUrl());
	    	throw e;
	    }
	    return seminarUri;
	}

	public void seminarInfo_ChannelList(WebDriver wd, String seminarname) throws InterruptedException {
		wd.get(CommonValues.SERVER_URL);
		Thread.sleep(500);
		wd.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		//find channel in list
		List<WebElement> channels = wd.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		for(int i = 0 ; i < channels.size() ; i++) {
			if(channels.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(USER_MASTER)) {
				JavascriptExecutor js = (JavascriptExecutor) wd;
				js.executeScript("arguments[0].scrollIntoView();", wd.findElement(By.xpath(".//div[@class='img-box']")));
				channels.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarname + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) wd;
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent_wd(wd, By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		wd.findElement(By.xpath(listitem)).click();
	}
	
	public String roomCheck_OP(WebDriver wd, String user) throws InterruptedException {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) wd;
		js.executeScript("arguments[0].scrollIntoView();", wd.findElement(By.xpath(XPATH_VIEW_JOIN)));
		// click enter(new tab)
		wd.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String>(wd.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!wd.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + wd.getCurrentUrl();
				return failMsg;
			}
		} else {
			wd.switchTo().window(tabs2.get(0));
			// close 1 tab
			wd.close();
			// switch room tab
			wd.switchTo().window(tabs2.get(1));
		}
		
		//설정팝업 확인
		if(isElementPresent_wd(wd, By.xpath("//div[@id='device-setting-wrap']"))) {
			if(user.contentEquals(ROLE_PRESENER)) {
				wd.findElement(By.xpath("//div[@class='buttons align-center']/button")).click();
				Thread.sleep(500);
			} else {
				failMsg = failMsg + "\n 0-1. find seminar setting popup " + user;
			}
			
		} else {
			if(user.contentEquals(ROLE_PRESENER)) {
				failMsg = failMsg + "\n 0-1. cannot find seminar setting popup " + user;
			}
		}
		
		//발표아이콘
		if(isElementPresent_wd(wd, By.xpath(OnAirRoom.XPATH_ROOM_SCREEN_BTN + "/button"))) {
			if(wd.findElement(By.xpath(OnAirRoom.XPATH_ROOM_SCREEN_BTN + "/button")).isEnabled()) {
				failMsg = failMsg + "\n 1-1. screen share icon is enabled. " + user;
			}
		} else {
			failMsg = failMsg + "\n 1-2. cannot find screen share icon." + user;
		}
		
		if(isElementPresent_wd(wd, By.xpath(OnAirRoom.XPATH_ROOM_CAM_BTN + "/button"))) {
			//do not anything
		} else {
			if(user.contentEquals(ROLE_PRESENER)) {
				failMsg = failMsg + "\n 1-4. cannot find cam setting icon." + user;
			}
		}
		
		if(isElementPresent_wd(wd, By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN + "/button"))) {
			//do not anything
		} else {
			failMsg = failMsg + "\n 1-6. cannot find youtube share icon." + user;
		}
		
		if(isElementPresent_wd(wd, By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN + "/button"))) {
			//do not anything
		} else {
			failMsg = failMsg + "\n 1-8. cannot find doc share icon." + user;
		}
		
		
		// 시작하기 버튼 확인
		try {
			wd.findElement(By.xpath("//button[@class='btn btn-primary btn-xl seminar-start']"));
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 2. cannot find start seminar button " + user;
		}

		// 참석자 인원 탭 확인
		List<WebElement> roomTabs = wd.findElements(By.xpath("//div[@id='timeline-viewmode']/button"));
		if (roomTabs.size() != 4) {
			failMsg = failMsg + "\n 3. tab size error (presenter) [Expected]4 [Actual]" + roomTabs.size();
		} else {
			if (!roomTabs.get(0).getAttribute("class").contentEquals("chat active")) {
				failMsg = failMsg + "\n 4. 1st tab error (" + user + ") [Expected]chat [Actual]"
						+ roomTabs.get(0).getAttribute("class");
			}
			if (!roomTabs.get(1).getAttribute("class").contentEquals("qna false")) {
				failMsg = failMsg + "\n 5. 2nd tab error (" + user + ") [Expected]chat [Actual]"
						+ roomTabs.get(1).getAttribute("class");
			}
			if (!roomTabs.get(2).getAttribute("class").contentEquals("download false")) {
				failMsg = failMsg + "\n 6. 3rd tab error (" + user + ") [Expected]chat [Actual]"
						+ roomTabs.get(2).getAttribute("class");
			}
			if (!roomTabs.get(3).getAttribute("class").contentEquals("attendee")) {
				failMsg = failMsg + "\n 7. 4st tab error (" + user + ") [Expected]attendee [Actual]"
						+ roomTabs.get(3).getAttribute("class");
			}
		}

		// 발표자가 인원탭에서 발표자 확인
		if (!wd.findElement(By.xpath("//div[@class='Participants_header__fsxhk']")).isDisplayed()) {
			wd.findElement(By.xpath("//i[@class='ricon-user-friends']")).click();
		}
		Thread.sleep(1000);
		List<WebElement> membersList = wd.findElements(By.xpath("//div[@class='ParticipantsList_container-list__29aAV']/div"));

		boolean findPres = false;
		String findUser = user==ROLE_PRESENER?USER_PRESENTER:USER_ORGANIZER;
		for (int i = 0; i < membersList.size(); i++) {
			if (membersList.get(i).findElement(By.xpath(".//p[@class='ParticipantsList_title__TiBqy']")).getText().contentEquals(user)) {
				List<WebElement> pres = membersList.get(i).findElements(By.xpath(".//li[@class='ParticipantsItem_list-item__IAFQA']"));
				if (pres.size() >= 1) {
					for (int j = 0; j < pres.size(); j++) {
						if (pres.get(j).findElement(By.xpath(".//p[@class='ParticipantsItem_nickname__215Mz ParticipantsItem_oneself__2hdsv']/span[1]"))
								.getText().contentEquals(findUser) && pres.get(j).findElement(By.xpath(".//p[@class='ParticipantsItem_nickname__215Mz ParticipantsItem_oneself__2hdsv']/span[2]"))
								.getText().contentEquals("(me)")) {
							findPres = true;
							
						}
					}
				}
				
			}
		}	
		
		if (!findPres) {
			failMsg = failMsg + "\n 8. cannot find " + user + " in user list";
		}
		
		return failMsg;
	}
	
	public String roomCheck_ghost(WebDriver wd, String user) throws InterruptedException
	{
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) wd;
		js.executeScript("arguments[0].scrollIntoView();", wd.findElement(By.xpath(XPATH_VIEW_JOIN)));
		
		//click enter(new tab)
		wd.findElement(By.xpath(XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String> (wd.getWindowHandles());
		if(tabs2.size() != 2) {
			if(!wd.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + wd.getCurrentUrl();
				return failMsg;
			} 
		} else {
			wd.switchTo().window(tabs2.get(0));
			//close 1 tab
			wd.close();
			//switch room tab
		    wd.switchTo().window(tabs2.get(1));
		}
	
		// 시작하기 버튼 없음 확인
		try {
			wd.findElement(By.xpath("//button[@class='btn btn-primary btn-xl seminar-start']"));
			failMsg = failMsg + "\n 1. find start seminar button " + user;
		} catch (NoSuchElementException e) {

		}

		// 참석자 인원 탭 없음 확인
		List<WebElement> roomTabs = wd.findElements(By.xpath("//div[@id='timeline-viewmode']/button"));
		if(roomTabs.size() != 3) {
			failMsg = failMsg + "\n 2. tab size error (" + user +") [Expected]3 [Actual]" + roomTabs.size();
		} else {
			if(!roomTabs.get(0).getAttribute("class").contentEquals("chat active")) {
				failMsg = failMsg + "\n 3. 1st tab error (" + user + ") [Expected]chat [Actual]" + roomTabs.get(0).getAttribute("class");
			}
			if(!roomTabs.get(1).getAttribute("class").contentEquals("qna false")) {
				failMsg = failMsg + "\n 4. 2nd tab error (" + user + ") [Expected]chat [Actual]" + roomTabs.get(0).getAttribute("class");
			}
			if(!roomTabs.get(2).getAttribute("class").contentEquals("download false")) {
				failMsg = failMsg + "\n 5. 3rd tab error (" + user + ") [Expected]chat [Actual]" + roomTabs.get(0).getAttribute("class");
			}
		}
		
		// 발표자가 인원탭에 게시자 찾을수 없음 확인
		if(!driver.findElement(By.xpath("//div[@class='Participants_header__fsxhk']")).isDisplayed()) {
			driver.findElement(By.xpath("//i[@class='ricon-user-friends']")).click();
		}
		Thread.sleep(1000);
		List<WebElement> membersList = driver.findElements(By.xpath("//div[@class='ParticipantsList_container-list__29aAV']/div"));
		
		for (int i = 0 ; i < membersList.size() ; i++) {
			if(membersList.get(i).findElement(By.xpath(".//p[@class='ParticipantsList_title__TiBqy']")).getText().contentEquals("Attendees")) {
				List<WebElement> attendees = membersList.get(i).findElements(By.xpath(".//li[@class='ParticipantsItem_list-item__IAFQA']"));
				
				if(attendees.size() > 1) {
					for (int j = 0 ; j < attendees.size() ; j++) {
						if(attendees.get(j).findElement(By.xpath(".//p[@class='ParticipantsItem_nickname__215Mz false']/span")).getText().contentEquals(USER_PUBLISHER)) {
							failMsg = failMsg + "\n 6. publisher is not " + user + " user (find in attenddes list)";
						}
						
						
					}
				}
			}
		}	
		
		return failMsg;
	}
	
	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {

	    driver.quit();
	    publisherDriver.quit();
	    attendeesDriver.quit();
	    memberDriver.quit();
	    sameuserDriver.quit();
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

	private boolean isElementPresent_wd(WebDriver wd, By by) {
		try {
			wd.findElement(by);
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

	private boolean findAlert(WebDriver wd, String msg) {
		// attendees
		if (ExpectedConditions.alertIsPresent().apply(wd) == null) {
			return false;

		} else {
			// 알림창이 존재하면 알림창 확인을 누를것
			assertEquals(closeAlertAndGetItsText_webdriver(wd), msg);
			return true;
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

	private String closeAlertAndGetItsText_webdriver(WebDriver wd) {
		try {
			Alert alert = wd.switchTo().alert();
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