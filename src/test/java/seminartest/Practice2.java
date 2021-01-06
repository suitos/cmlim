package seminartest;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
* Scenario.1 
* 0.로그인 
* 1.연습하기 불가능한  세미나 생성(Stanby 10분 전 세미나 생성)
* 2.세미나 세팅1(채널 변경,멤버 설정(발표자1,운영자2))
* 3.발표자 연습하기 불가능한 세미나 입장 시도(/list,/view)
* 4.운영자 연습하기 불가능한 세미나 입장 시도(/view)
* Scenario.2
* 5.연습하기 가능한 세미나 생성
* 6.세미나 세팅2(채널 변경,멤버 설정(발표자1,운영자2))
* 7.발표자 연습하기 가능한 세미나 입장 시도(/list)
* 8.리허설 뱃지 확인 (/list)
* 9.리허설 뱃지 확인 (/channel/list)
* 10.발표자 연습하기 가능한 세미나 입장 시도(/view)
* 11.운영자 연습하기 가능한 세미나 입장 시도(/view)
* 12.리허설 종료 url 확인
* 13.종료 후 리허설 뱃지 확인(/list)
* 14.종료 후 뱃지 확인(/channel/list)
* Scenario.3
* 15.운영자 재입장(/view)
* 16.해당 세미나 리허설 중 수정
* 17.수정 후 리허설 강제종료 확인
* 18.운영자 재입장(/view)
* 19.해당 세미나 리허설 중 삭제
* 20.삭제 후 리허설 강제종료 확인
*/
public class Practice2 {
	
	public static WebDriver Present_driver;
	public static WebDriver Organizer_driver;
	
	public String seminarTitle = "Can not Enter Rehearsal_Practice test";
	public String seminarTitle2 = "Can Enter Rehearsal_Practice test";
	public String createViewURL = "";
	public String seminarID = "";
	public String seminarAuthor = "";
	public String seminarDate = "";
	public String seminarViewURL = "";
	public String seminarLinkURL = "";
	public String seminarRoomURL = "";
	public String closedURL = "";
	public List<String> Presenters;
	public List<String> Organizers;
	public String Channelname = "";
	
	public static String REHEARSAL_END_MSG = "Seminar rehearsal has ended.";
	public static String REHEARSAL_END_TITLE = "Rehearsal has ended.";
	public static String REHEARSAL_CANNOTENTER_MSG = "Currently, rehearsal is not allowed.";
	public static String REHEARSAL_TERMINATED_MSG = "The seminar was revised or deleted and the rehearse has over.";
	public static String REHEARSAL_EDIT_MSG = "The seminar is currently in rehearse mode.\n" + 
			"This mode will end if you modify it.\n" + 
			"Do you want to edit it anyway?";
	
	public static String XPATH_SEMINARlIST_PRACTICEBTN = "//button[@class='btn btn-basic btn-m ']";
	public static String XPATH_SEMINARVIEW_PRACTICEBTN = "//button[@class='btn btn-basic btn-auto SeminarView_actionButton__3tFHP']";
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		Present_driver = comm.setDriver(Present_driver, browsertype, "lang=en_US", true);
		Organizer_driver = comm.setDriver(Organizer_driver, browsertype, "lang=en_US");
	
		context.setAttribute("webDriver", Present_driver);
		context.setAttribute("webDriver2", Organizer_driver);
		
		Presenters = new ArrayList<>(Arrays.asList("rsrsup8@gmail.com"));
		Organizers = new ArrayList<>(Arrays.asList("rsrsup3@gmail.com", "rsrsup6@gmail.com"));

        System.out.println("End BeforeTest!!!");

	}
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(Present_driver, CommonValues.USEREMAIL_PRES);
		comm.loginseminar(Organizer_driver, CommonValues.USEREMAIL_PRES2);

		Thread.sleep(1000);
	}
	
	@Test(priority=1)
	public void createCanNotPracticeSeminar() throws Exception {
		
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		Present_driver.get(createViewURL);
		Thread.sleep(1000); 
		if(!Present_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  Present_driver.getCurrentUrl());
			throw e;
		}
		
		setRehearsal(Present_driver, seminarTitle, false);
		
	}

	@Test(priority=2)
	public void CreateRehearsalSeminar_Setting() throws Exception{
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Present_driver);
		Channelname = "rsrsup1";
		
		Present_driver.findElement(By.xpath("//ul[@class='tab ']/li[3]")).click();
		Thread.sleep(500);
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		Present_driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);
		List<WebElement> members_organized = Present_driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_MEMBERPOPUP_LIST));
		
		//운영자 select 2 //rsrsup3,rsrsup6(알파 기준)
		if(members_organized.size() > 0) {
			for (String organizer : Organizers) {
				for (WebElement member : members_organized) {
					if(member.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_MEMBERPOPUP_EMAIL)).getText().contains(organizer)) {
						member.click();
					}
				}
			}
		} 
		
		Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)));
		//save
		Present_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(Present_driver, seminarTitle);
		comm.postSeminar(Present_driver, seminarUri);

	    if(seminarUri == null || seminarUri.isEmpty())
	    {
	    	Exception e =  new Exception("fail to create seminar : " + Present_driver.getCurrentUrl());
	    	throw e;
	    }
	    
	    seminarID = Present_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW , "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
	}

	@Test(priority=3)
	public void Present_Enter_CanNotRehearsalSeminar() throws Exception {
		String failMsg = "";
		//list에서 확인
		
		Present_driver.get(CommonValues.SERVER_URL+CommonValues.LIST_URI);
		
		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist.get(0).findElement(By.xpath(XPATH_SEMINARlIST_PRACTICEBTN)).click();
		
	    WebDriverWait wait = new WebDriverWait(Present_driver, 20);
	    wait.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//div[@class='modal-body']")), "Currently, rehearsal is not allowed."));

	    Present_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();;
	    //view에서 확인
	    seminarlist.get(0).click();
	    Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	  
	    WebElement PracticeBTN = Present_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		Alert alert = Present_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(REHEARSAL_CANNOTENTER_MSG)) {
			failMsg = failMsg + "\n 1.different alertMSG  [Expected]" + REHEARSAL_CANNOTENTER_MSG
					+" [Actual]" + alert_msg;
		}
		
	    if(!Present_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
	  	failMsg = failMsg + "\n 2.Do not access Seminar detail view :" + Present_driver.getCurrentUrl()
					+" [Actual]" + seminarViewURL;	
		}
	  
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority=4)
	public void Organizer_Enter_CanNotRehearsalSeminar() throws Exception {
		String failMsg = "";
		
		Organizer_driver.get(seminarViewURL);
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
			failMsg = failMsg + "\n 1.Do not access Seminar detail view :" + Organizer_driver.getCurrentUrl()
			+" [Actual]" + seminarViewURL;	
		}
	  
	    WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		Alert alert = Organizer_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(REHEARSAL_CANNOTENTER_MSG)) {
			failMsg = failMsg + "\n 2.different alertMSG  [Expected]" + REHEARSAL_CANNOTENTER_MSG
					+" [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	@Test(priority=5)
	public void createCanPracticeSeminar() throws Exception {
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
		Present_driver.get(createViewURL);
		Thread.sleep(1000); 
		if(!Present_driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  Present_driver.getCurrentUrl());
			throw e;
		}
		System.out.println(createViewURL);
		setRehearsal(Present_driver, seminarTitle2, true);
	}
	
	@Test(priority=6)
	public void CreateRehearsalSeminar_Setting2() throws Exception{
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(Present_driver);

		Present_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB3)).click();
		Thread.sleep(500);
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		Present_driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);
		List<WebElement> members_organized = Present_driver.findElements(By.xpath(CommonValues.XPATH_CREATESEMINAR_MEMBERPOPUP_LIST));
		
		//운영자 select 2 //rsrsup3,rsrsup6(알파 기준)
		if(members_organized.size() > 0) {
			for (String organizer : Organizers) {
				for (WebElement member : members_organized) {
					if(member.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_MEMBERPOPUP_EMAIL)).getText().contains(organizer)) {
						member.click();
					}
				}
			}
		} 
		
		Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)));
		//save
		Present_driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
		
		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(Present_driver, seminarTitle2);
		comm.postSeminar(Present_driver, seminarUri);

	    if(seminarUri == null || seminarUri.isEmpty())
	    {
	    	Exception e =  new Exception("fail to create seminar : " + Present_driver.getCurrentUrl());
	    	throw e;
	    }
				
		seminarID = Present_driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW , "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
	}
	
	@Test(priority=7)
	public void Present_Enter_CanRehearsalSeminarinList() throws Exception {
		String failMsg = "";
		
		String parentHandle = Present_driver.getWindowHandle();
	
		//list에서 확인
		Present_driver.get(CommonValues.SERVER_URL+CommonValues.LIST_URI);
				
		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		seminarlist.get(0).findElement(By.xpath(XPATH_SEMINARlIST_PRACTICEBTN)).click();
		
		Thread.sleep(1000);
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
		Thread.sleep(1000);
		
		if(!Present_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Present_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
		
		Present_driver.close();
		Present_driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=8)
	public void checkRehearsalBadgeinList() throws Exception {
		String failMsg = "";
		
		Present_driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		String BadgeinList = seminarlist.get(0).findElement(By.xpath("//div[@class='contents']")).getText();
		if(!BadgeinList.contentEquals("Rehearsal")) 
		{
			failMsg = failMsg + "\n 1.different seminar Badge [Expected] Rehearsal"  + 
					" [Actual]" + BadgeinList;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority=9)
	public void checkRehearsalBadgeinChannelList() throws Exception {
		String failMsg = "";
		
		Present_driver.get(CommonValues.SERVER_URL + CommonValues.CHANNEL_VIEW_URL + "/" + Channelname);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		String BadgeinList = seminarlist.get(0).findElement(By.xpath("//div[@class='contents']")).getText();
		if(!BadgeinList.contentEquals("Rehearsal")) 
		{
			failMsg = failMsg + "\n 1.different seminar Badge [Expected] Rehearsal"  + 
					" [Actual]" + BadgeinList;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}		
	}
	
	@Test(priority=10)
	public void Present_Enter_CanRehearsalSeminarinView() throws Exception {
		String failMsg = "";
		String parentHandle = Present_driver.getWindowHandle();

		Present_driver.get(seminarViewURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		if(!Present_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
		  	failMsg = failMsg + "\n 2.Do not access Seminar View :" + Present_driver.getCurrentUrl()
						+" [Actual]" + seminarViewURL;	
		}
		
		WebElement PracticeBTN = Present_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		for (String winHandle : Present_driver.getWindowHandles()) {
			Present_driver.switchTo().window(winHandle); }
		
		if(!Present_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Present_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=11)
	public void Organizer_Enter_CanRehearsalSeminarinView() throws Exception {
		String failMsg = "";
		String parentHandle = Organizer_driver.getWindowHandle();
		
		Organizer_driver.get(seminarViewURL);
		Organizer_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarViewURL)) {
		  	failMsg = failMsg + "\n 2.Do not access Seminar View :" + Organizer_driver.getCurrentUrl()
						+" [Actual]" + seminarViewURL;	
		}
		
		WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Organizer_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
		
		Organizer_driver.close();
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=12)
	public void EndRehearsal() throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(Present_driver);
		
		JavascriptExecutor js = (JavascriptExecutor) Present_driver;
		
		js.executeScript("arguments[0].scrollIntoView();", Present_driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_BTN)));
		
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
	
	@Test(priority=13 ,dependsOnMethods = { "checkRehearsalBadgeinList"})
	public void checkAfterRehearsalBadgeinList() throws Exception {
		String failMsg = "";
		
		Present_driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		String BadgeinList = seminarlist.get(0).findElement(By.xpath("//div[@class='contents']")).getText();
		if(BadgeinList.contentEquals("Rehearsal")) 
		{
			failMsg = failMsg + "\n 1.different seminar Badge [Expected] D-2"  + 
					" [Actual]" + BadgeinList;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=14 ,dependsOnMethods = { "checkRehearsalBadgeinChannelList"})
	public void checkAfterRehearsalBadgeinChannelList() throws Exception {
		String failMsg = "";
		
		Present_driver.get(CommonValues.SERVER_URL + CommonValues.CHANNEL_VIEW_URL + "/" + Channelname);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		List<WebElement> seminarlist = Present_driver.findElements(By.xpath("//li[@role='presentation']"));
		String BadgeinList = seminarlist.get(0).findElement(By.xpath("//div[@class='contents']")).getText();
		if(BadgeinList.contentEquals("Rehearsal")) 
		{
			failMsg = failMsg + "\n 1.different seminar Badge [Expected] D-2"  + 
					" [Actual]" + BadgeinList;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}		
	}
	
	@Test(priority=15)
	public void Organizer_ReEnter_CanRehearsalSeminarinView() throws Exception {
		String failMsg = "";
		String parentHandle = Organizer_driver.getWindowHandle();
		
		WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Organizer_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=16)
	public void Edit_Seminar() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(Present_driver, 20);
		
		Present_driver.get(seminarViewURL);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='ricon ricon-edit']")));
		Present_driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_MODAL_BODY)));
		
		if(!Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(REHEARSAL_EDIT_MSG)) {
			failMsg = "1. edit warring msg [Expected]" + REHEARSAL_EDIT_MSG 
					+ " [Actual]" + Present_driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
		}
	
		Present_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(2000);
		Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Thread.sleep(2000);
		Present_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}
	
	@Test(priority=17)
	public void Organizer_Terminated_CanRehearsalSeminarinView() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1000);
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(closedURL)) {
			Exception e =  new Exception("1.Rehearsal do not terminated. : " +  Organizer_driver.getCurrentUrl());
			throw e;
		}
		
		String Toast = Organizer_driver.findElement(By.xpath("//div[@class='wrap-toast-outer']/div/div/span")).getAttribute("innerHTML");
	    System.out.println(Toast);
	    
	    if(!Toast.contentEquals(REHEARSAL_TERMINATED_MSG)) {
	    	failMsg = failMsg + "\n 2. Wrong Toast msg [Expected]" + REHEARSAL_TERMINATED_MSG 
					+ " [Actual]" + Toast;
	    }
		
		Organizer_driver.close();
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=18)
	public void Organizer_ReEnter_CanRehearsalSeminarinView2() throws Exception {
		String failMsg = "";
		String parentHandle = Organizer_driver.getWindowHandle();
		
		WebElement PracticeBTN = Organizer_driver.findElement(By.xpath(XPATH_SEMINARVIEW_PRACTICEBTN));
	    JavascriptExecutor js = (JavascriptExecutor) Organizer_driver;
		js.executeScript("arguments[0].scrollIntoView();", PracticeBTN);
		PracticeBTN.click();
		Thread.sleep(1000);
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		if(!Organizer_driver.getCurrentUrl().contentEquals(seminarRoomURL)) {
		  	failMsg = failMsg + "\n 1.Do not access Seminar Room :" + Organizer_driver.getCurrentUrl()
						+" [Actual]" + seminarRoomURL;	
		}
	}
	
	@Test(priority=19)
	public void Delete_Seminar() throws Exception {
		
		Present_driver.get(seminarViewURL);
		Present_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		Present_driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		WebDriverWait wait = new WebDriverWait(Present_driver, 20);
		wait.until(ExpectedConditions.textToBePresentInElement(Present_driver.findElement(By.xpath("//div[@class='modal-body']")), "Do you want to delete the seminar?"));
		
		Present_driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=20)
	public void Organizer_Terminated_CanRehearsalSeminarinView2() throws Exception {
		String failMsg = "";

		if(!Organizer_driver.getCurrentUrl().contentEquals(closedURL)) {
			failMsg = failMsg + "1.Rehearsal do not terminated.";
		}
		
		String Toast = Organizer_driver.findElement(By.xpath("//div[@class='wrap-toast-outer']/div/div/span")).getAttribute("innerHTML");
	    System.out.println(Toast);
	    
	    if(!Toast.contentEquals(REHEARSAL_TERMINATED_MSG)) {
	    	failMsg = failMsg + "\n 2. Wrong Toast msg [Expected]" + REHEARSAL_TERMINATED_MSG 
					+ " [Actual]" + Toast;
	    }
		
		Organizer_driver.close();
		
		for (String winHandle : Organizer_driver.getWindowHandles()) {
			Organizer_driver.switchTo().window(winHandle); }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {
	  
		Present_driver.quit();
		Organizer_driver.quit();
	
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
	
