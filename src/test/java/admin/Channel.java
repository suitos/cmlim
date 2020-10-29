package admin;

import seminartest.CommonValues;
import seminartest.DBConnection;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
/*
 * 0.채널 관리 입장
 * 1.검색란 ui확인 및 디폴트 확인
 * 2.***********************날짜 검색(미완)
 * 
 * 
 * 10.키워드 검색
 * 11.키워드 검색 invalid1
 * 12.키워드 검색 invalid2
 * 13.리스트 항목 셀 클릭 시 이동
 * 14.*********************페이징(미완)
 * 
 * 
 * 20.채널 등록 UI 확인
 * 21.채널 등록 시 빈값 입력 후 등록
 * 22.채널 등록 내 채널명 입력 후 에러메세지 미노출 확인 및 채널명,채널소개 placeholder 및  최대 길이 체크
 * 23.채널 등록 내 마스터 빈값, 잘못된 값 입력 후 검색 클릭
 * 24.채널 등록 내 채널 마스터 입력
 * 25.채널 등록 내 채널공개여부 default 확인
 * 26.채널등록-valid
 * 27.채널등록-valid2
 * 28.채널정보 값 확인
 * 
 */
public class Channel {
	public String Standarddate = "";
	
	//채널 정보
	public static String XPATH_CHANNEL_INFO_BIGNAME = "//div[@class='__profile__desc']/h5/span";
	public static String XPATH_CHANNEL_INFO_CHANNELNAME = "//tbody/tr[1]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELURL = "//tbody/tr[2]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELMASTER = "//tbody/tr[3]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELDESCRIPTION = "//tbody/tr[4]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELDISCLOSURE = "//tbody/tr[5]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELPLAN = "//tbody/tr[6]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS = "//tbody/tr[7]/th";
	public static String XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME = "//tbody/tr[8]/th";
	//채널 정보 하단탭
	public static String XPATH_CHANNEL_INFO_TAB1 = "//div[@id='rc-tabs-0-tab-1']";
	public static String XPATH_CHANNEL_INFO_TAB2 = "//div[@id='rc-tabs-0-tab-2']";
	public static String XPATH_CHANNEL_INFO_TAB3 = "//div[@id='rc-tabs-0-tab-3']";
	public static String XPATH_CHANNEL_INFO_TAB4 = "//div[@id='rc-tabs-0-tab-4']";
	public static String XPATH_CHANNEL_INFO_TAB1_PANEL = "//div[@id='rc-tabs-0-panel-1']";
	public static String XPATH_CHANNEL_INFO_TAB2_PANEL = "//div[@id='rc-tabs-0-panel-2']";
	public static String XPATH_CHANNEL_INFO_TAB3_PANEL = "//div[@id='rc-tabs-0-panel-3']";
	public static String XPATH_CHANNEL_INFO_TAB4_PANEL = "//div[@id='rc-tabs-0-panel-4']";
	
	public static String XPATH_CHANNEL_TAB_NODATA = "//p[@class='ant-empty-description']";
	
	//채널 등록 시 
	public static String XPATH_CHANNEL_REGIST_NAME = "//input[@id='channelName']";
	public static String XPATH_CHANNEL_REGIST_NAME_ERROR = "//input[@id='channelName']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_REGIST_MASTER = "//input[@id='channelMaster']";
	public static String XPATH_CHANNEL_REGIST_MASTER_ERROR = "//input[@id='channelMaster']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_REGIST_PRIVATE = "//input[@value='PRIVATE']";
	public static String XPATH_CHANNEL_REGIST_PUBLIC = "//input[@value='PUBLIC']";
	public static String XPATH_CHANNEL_REGIST_DESCRIPTION = "//input[@id='channelDescription']";
	public static String XPATH_CHANNEL_REGIST_SAVE_BTN = "//button[@type='submit']";
	public static String XPATH_CHANNEL_REGIST_CANCEL_BTN = "//button[@class='ant-btn']";
	//채널 등록 시 회원검색
	public static String XPATH_CHANNEL_USERSEARCH_EMAIL = "//input[@name='email']";
	public static String XPATH_CHANNEL_SEARCH_EMAIL_ERROR = "//div[@id='email']/../../../div[@class='ant-form-item-explain']"; //하단 에러메세지
	
	public static String XPATH_CHANNEL_USERSEARCH_SEARCH_BTN = "//div[@class='ant-col ant-col-6']/button[1]";
	public static String XPATH_CHANNEL_USERSEARCH_SAVE_BTN = "//div[@class='ant-modal-footer']/button[1]";
	public static String XPATH_CHANNEL_USERSEARCH_CANCEL_BTN = "//div[@class='ant-modal-footer']/button[2]";
	//토스트메세지
	public static String XPATH_TOAST = "//div[@class='ant-message-custom-content ant-message-error']/span[2]";
	public static String XPATH_TOAST2 = "//div[@class='ant-message-custom-content ant-message-success']/span[2]";
	//키워드검색
	public static String KEYWORD_SEARCH_TEXTBOX = "//input[@id='keyword']";
	public static String KEYWORD_SEARCH_BTN = "//button[@id='search']";
	public static String KEYWORD_PLACEHOLDER = "채널명, 채널 마스터의 닉네임, 이메일을 입력하세요.";
	
	public static String MSG_CHANNEL_NAME_EMPTY = "채널명을 기입해주세요.";
	public static String MSG_CHANNEL_MASTER_EMPTY = "채널마스터를 설정해주세요.";
	public static String MSG_CHANNEL_SEARCH_EMPTY = "이메일을 입력해주세요.";
	public static String MSG_CHANNEL_SEARCH_WRONG = "등록된 사용자가 없습니다.";
	public static String MSG_CHANNEL_SEARCH_WRONG2 = "이메일을 검색해주세요.";
	public static String MSG_TOAST = "올바르지 않습니다.";
	public static String MSG_TOAST2 = "사용자 검색에 실패하였습니다.";
	public static String MSG_TOAST3 = "사용자 정보가 검색되었습니다.";
	public static String MSG_TOAST4 = "등록 되었습니다.";
	public static String MSG_TOAST5 = "채널이 생성되었습니다.";
	
	public String channelName = "";
	public String channelMaster = "";
	public String channelDescription = "";
	public String channelDisclosure = "";
	public String Plan = "";
	public String runningTime = "";
	public String channelMember = "";
	public String numberofSeminars = "";
	public String channelID = "";
	public String channelURL = "";
	

	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		admin.CommonValues comm = new admin.CommonValues();
		comm.setDriverProperty(browsertype);
		
		//driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR");

		context.setAttribute("webDriver", driver);
		driver.get(admin.CommonValues.ADMIN_URL);
		
		comm.logintadmin(driver, admin.CommonValues.USER_PARTNER_KR2);

		System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void EnterChannelList() throws Exception {
	
		driver.findElement(By.xpath(admin.CommonValues.XPATH_MENU_CHANNEL)).click();
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST);
		
	}
	
	@Test(priority=1)
	public void DefaultCheck() throws Exception {
		String failMsg = "";
		
		String allmonth = driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[1]/span[1]/input")).getAttribute("value");
		
		if(!allmonth.contentEquals("all-months")) {
			failMsg = "1.all-months is not exist";
		}
		
		String last3month = driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[2]/span[1]/input")).getAttribute("value");
		
		if(!last3month.contentEquals("last-3-months")) {
			failMsg = failMsg + "\n 2.last-3-months is not exist";
		}
		
		String lastmonth = driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[3]/span[1]/input")).getAttribute("value");
		
		if(!lastmonth.contentEquals("last-month")) {
			failMsg = failMsg + "\n 3.last-month is not exist";
		}
		
		String themonth = driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[4]/span[1]/input")).getAttribute("value");
		
		if(!themonth.contentEquals("the-month")) {
			failMsg = failMsg + "\n 4.the-month is not exitst";
		}
		
		Boolean isChecked = driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[4]/span[1]/input")).isSelected();
		
		if(isChecked == false) {
			failMsg = failMsg + "\n 5.the-month is not default";
			System.out.println(isChecked);
		}
		
		String keywordplaceholder = driver.findElement(By.xpath("//*[@id=\"keyword\"]")).getAttribute("placeholder");
		
		if(!keywordplaceholder.contentEquals(KEYWORD_PLACEHOLDER)) {
			failMsg = failMsg + "\n 6.placeholder is wrong [Expected]" + KEYWORD_PLACEHOLDER +  " [Actual]" + keywordplaceholder;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=2)
	public void dateSearch() throws Exception{
		String failMsg = "";
		//기준날짜
		Standarddate = driver.findElement(By.xpath("//*[@id=\"datePicker\"]")).getAttribute("value").substring(0,7);
		System.out.println(Standarddate);
		
		
	}
	
	@Test(priority=10)
	public void keywordSearch() throws Exception {
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.setCalender(driver);
		
		
		//채널명 일부 입력 후 검색
		insertKeywordSearch("rsrsup");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("rsrsup");
		
		Thread.sleep(1000);
		
		//채널 마스터 닉네임 입력 후 검색
		insertKeywordSearch(seminartest.CommonValues.USERNICKNAME_JOIN);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USERNICKNAME_JOIN);
		
		Thread.sleep(1000);

		//채널 마스터 이메일 입력 후 검색
		insertKeywordSearch(seminartest.CommonValues.USEREMAIL_PRES);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USEREMAIL_PRES);
			
	}
	
	@Test(priority=11)
	public void keywordSearch_invalid1() throws Exception {
		
		insertKeywordSearch(seminartest.CommonValues.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(seminartest.CommonValues.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(seminartest.CommonValues.USEREMAIL_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USEREMAIL_JP);
		
		Thread.sleep(1000);
	}
	
	@Test(priority=12)
	public void keywordSearch_invalid2() throws Exception {
		/*개발예정
		insertKeywordSearch(" ");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(" ");
		*/
		Thread.sleep(1000); 
		
		insertKeywordSearch("@");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("@");
	}
	
	@Test(priority=13)
	public void listLinkCheck() throws Exception {
		
		checkListView(driver);
		
		ListDataNullCheck();
		
		cellClick("[1]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		ListDataNullCheck();
		
		cellClick("[2]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		cellClick("[3]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		cellClick("[4]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		cellClick("[5]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		cellClick("[6]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		checkListView(driver);
		
		cellClick("[7]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
	}
	
	@Test(priority=14)
	public void listPaging() throws Exception {
		
	}
		
		
	@Test(priority=20)
	public void addChannel() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);
		
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_CHANNELREGISTER_BTN)).click();
		Thread.sleep(500);
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);

		
		if(!driver.findElement(By.xpath("//label[@for='channelName']")).getAttribute("title").contentEquals("채널명")) {
			failMsg =  "1.name ui. [Expected]채널명 [Actual]" 
					+ driver.findElement(By.xpath("//label[@for='channelName']")).getAttribute("title");
		}
		
		if(!driver.findElement(By.xpath("//label[@for='channelMaster']")).getAttribute("title").contentEquals("채널마스터")) {
			failMsg = failMsg + "\n 2.master ui. [Expected]채널마스터 [Actual]" 
					+ driver.findElement(By.xpath("//label[@for='channelMaster']")).getAttribute("title");
		}
		
		if(!driver.findElement(By.xpath("//label[@for='channelType']")).getAttribute("title").contentEquals("채널공개여부")) {
			failMsg = failMsg + "\n 4.channelType ui. [Expected]채널공개여부 [Actual]" 
					+ driver.findElement(By.xpath("//label[@for='channelType']")).getAttribute("title");
		}
		
		if(!driver.findElement(By.xpath("//label[@for='channelDescription']")).getAttribute("title").contentEquals("채널설명")) {
			failMsg = failMsg + "\n 4.description ui. [Expected]채널설명 [Actual]" 
					+ driver.findElement(By.xpath("//label[@for='channelDescription']")).getAttribute("title");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=21)
	public void addChannel_empty() throws Exception {
		String failMsg = "";	
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME_ERROR)).getText().contentEquals(MSG_CHANNEL_NAME_EMPTY)) {
			failMsg = "1.channel name error(empty) [Expected]" + MSG_CHANNEL_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER_ERROR)).getText().contentEquals(MSG_CHANNEL_MASTER_EMPTY)) {
			failMsg = failMsg + "\n 2.channel master error(empty) [Expected]" + MSG_CHANNEL_MASTER_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_CANCEL_BTN)).click();
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=22)
	public void addChannel_insertChannelandlengthCheck() throws Exception {
		String failMsg = "";	
		
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_CHANNELREGISTER_BTN)).click();
		Thread.sleep(500);

		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME));
		
		if(!CHANNEL_NAME.getAttribute("placeholder").contentEquals("채널명을 입력해주세요.")){
			failMsg = "1. [Expected] channelName placeholder [Actual]" + CHANNEL_NAME.getAttribute("placeholder");
		}
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(CHANNEL_NAME, 1, admin.CommonValues.SPECIAL_10);

		if(!driver.findElements(By.xpath(XPATH_CHANNEL_REGIST_NAME_ERROR)).isEmpty()){
			 failMsg = "2.error MSG displayed [Expected] empty [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME_ERROR)).getText();   
		}
		
		comm.insertData(CHANNEL_NAME, 2, admin.CommonValues.SPECIAL_10);
		
		String a = CHANNEL_NAME.getAttribute("value");
		System.out.println(a);
		System.out.println(a.length());
		if(a.length() != 30) {
			failMsg = failMsg + "\n 3.Channel Name is wrong length [Actual]" + a.length();
		}
		
		Thread.sleep(1000);
		
		WebElement CHANNEL_DESCRIPTION = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_DESCRIPTION));
		
		if(!CHANNEL_DESCRIPTION.getAttribute("placeholder").contentEquals("채널설명을 입력해주세요.")){
			failMsg = failMsg + "\n 4 [Expected] channelDescription placeholder [Actual]" + CHANNEL_DESCRIPTION.getAttribute("placeholder");
		}
		
		comm.insertData(CHANNEL_DESCRIPTION, 3, admin.CommonValues.SPECIAL_10);
		
		String b = CHANNEL_DESCRIPTION.getAttribute("value");
		System.out.println(b);
		System.out.println(b.length());
		
		if(b.length() != 500) {
			failMsg = failMsg + "\n 3.Channel Description is wrong length [Actual]" + b.length();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		}
	}
	
	@Test(priority=23)
	public void addChannel_insertChannelMaster() throws Exception {
		String failMsg = "";	
		
		WebElement CHANNEL_MASTER = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER));
		CHANNEL_MASTER.click();
		
		//placeholder 체크
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL)).getAttribute("placeholder").contentEquals("email")) {
			failMsg = "1.email placeholder. [Expected]email [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL)).getAttribute("placeholder");
		}
	
		//빈값 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(500);
		if(driver.findElements(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).isEmpty()) {
			failMsg = failMsg + "error MSG is empty!";
		}
		else {
		driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_EMPTY)) {
			failMsg = failMsg + "\n 2.master email error(empty value search) [Expected]" + MSG_CHANNEL_SEARCH_EMPTY
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
			}
		}
		
		TimeUnit.SECONDS.sleep(4);

		//빈값 입력 후 등록 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_EMPTY)) {
			failMsg = failMsg + "\n 3.master email error(empty value save) [Expected]" + MSG_CHANNEL_SEARCH_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST)) {
			failMsg = failMsg + "\n 4.toast msg error(empty value save) [Expected]" + MSG_TOAST 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		WebElement EMAIL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(EMAIL_NAME, 1, admin.CommonValues.SPECIAL_10);
		
		TimeUnit.SECONDS.sleep(4);
		
		//이메일포맷이 아닌 문자열 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 5.master email error(wrong value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST2)) {
			failMsg = failMsg + "\n 6.toast msg error(wrong valuesearch) [Expected]" + MSG_TOAST2 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		TimeUnit.SECONDS.sleep(4);
		
		//이메일포맷이 아닌 문자열 저장 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		Thread.sleep(1000);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 7.master email error(wrong value save) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST)) {
			failMsg = failMsg + "\n 8.toast msg error(wrong value save) [Expected]" + MSG_TOAST
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		comm.insertData(EMAIL_NAME, 1, admin.CommonValues.WRONG_EMAIL);
		
		TimeUnit.SECONDS.sleep(4);
		
		//등록되지 않은 유저 이메일 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(1000);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 9.master email error(do not register email value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST2)) {
			failMsg = failMsg + "\n 10.toast msg error(do not register email value search) [Expected]" + MSG_TOAST2
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		comm.insertData(EMAIL_NAME, 1, admin.CommonValues.USER_PARTNER_KR);
		
		TimeUnit.SECONDS.sleep(4);
		
		//파트너 관리자 권한으로 등록된 유저 이메일 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(1000);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 11.master email error(partner email value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST2)) {
			failMsg = failMsg + "\n 12.master email error(partner email value search) [Expected]" + MSG_TOAST2
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		comm.insertData(EMAIL_NAME, 1, seminartest.CommonValues.USEREMAIL_ADMIN);
		
		TimeUnit.SECONDS.sleep(4);
		
		//등록된 정상 유저 이메일 입력 후 검색 없이 저장 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		Thread.sleep(1000);
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG2)) {
			failMsg = failMsg + "\n 13.master email error(Registered email value save) [Expected]" + MSG_CHANNEL_SEARCH_WRONG2
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST)) {
			failMsg = failMsg + "\n 14.toast msg error((Registered email value save) [Expected]" + MSG_TOAST
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		TimeUnit.SECONDS.sleep(4);
		
		//등록된 정상 유저 이메일 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		if(driver.findElements(By.xpath(XPATH_TOAST2)).isEmpty()) {
			driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		}
		
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST3)) {
			failMsg = failMsg + "\n 15.toast msg error(Registerd email value search) [Expected]" + MSG_TOAST3
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		if(!driver.findElements(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).isEmpty()) {
			failMsg = failMsg + "\n 16.master email error(Registerd email value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG2
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		
		//팝업 취소 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_CANCEL_BTN)).click();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER)).getAttribute("value").isEmpty()) {
			failMsg = failMsg + "\n 17.Channel Master enter data";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		}		
	}
	
	@Test(priority=24) 
	public void addChannel_insertChannelMaster2() throws Exception {
		String failMsg = "";	
		
		WebElement CHANNEL_MASTER = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER));
		CHANNEL_MASTER.click();
		
		WebElement EMAIL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(EMAIL_NAME, 1, seminartest.CommonValues.USEREMAIL_ADMIN);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		if(driver.findElements(By.xpath(XPATH_TOAST2)).isEmpty()) {
			driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		}
		
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST3)) {
			failMsg = "1.toast msg error(Registerd email value search) [Expected]" + MSG_TOAST3
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		TimeUnit.SECONDS.sleep(4);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST4)) {
			failMsg = failMsg + "\n 2.toast msg error(Registerd email value search) [Expected]" + MSG_TOAST4
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		if(!CHANNEL_MASTER.getAttribute("value").contentEquals(seminartest.CommonValues.USEREMAIL_ADMIN)){
			failMsg = failMsg + "\n 3.Channel Master is not correct [Expected]" + seminartest.CommonValues.USEREMAIL_ADMIN +" [Actual]" 
					+ CHANNEL_MASTER.getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=25) 
	public void addChannel_channelType() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//span[@class='ant-radio ant-radio-checked']")).isSelected()
				&& driver.findElement(By.xpath("//span[@class='ant-radio']")).isSelected()) {
			failMsg = "default is Public";	
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=26) 
	public void addChannel_valid() throws Exception {
		String failMsg = "";
		
		channelName = "ChannelTest";
		channelMaster = seminartest.CommonValues.USEREMAIL_ADMIN;
		Plan = "";
		runningTime = "00:00:00";
		channelMember = "1";
		numberofSeminars = "0";
		
		driver.get(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(CHANNEL_NAME, 1, channelName);
		
		WebElement CHANNEL_MASTER = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER));
		CHANNEL_MASTER.click();
		
		WebElement EMAIL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL));
		
		comm.insertData(EMAIL_NAME, 1, channelMaster);
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		TimeUnit.SECONDS.sleep(4);
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		TimeUnit.SECONDS.sleep(4);
		driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(driver.findElements(By.xpath(XPATH_TOAST2)).isEmpty()) {
			failMsg = "can not find toast msg";
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST5)) {
			failMsg = failMsg + "\n 2.toast msg error(Register Channel) [Expected]" + MSG_TOAST5
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST);
		
		ListDataNullCheck();
		
		SingleRowDataCheck();
		
		cellClick("[1]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		System.out.println(channelID);
		seminartest.DBConnection DB = new seminartest.DBConnection();
		DB.deleteChannel(channelID);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=27) 
	public void addChannel_valid2() throws Exception {
		String failMsg = "";
		
		channelName = "ChannelTest";
		channelMaster = seminartest.CommonValues.USEREMAIL_ADMIN;
		Plan = "";
		runningTime = "00:00:00";
		channelMember = "1";
		numberofSeminars = "0";
		
		driver.get(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_NAME));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(CHANNEL_NAME, 1, channelName);
		
		WebElement CHANNEL_DESCRIPTION = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_DESCRIPTION));
		
		comm.insertData(CHANNEL_DESCRIPTION, 1, admin.CommonValues.SPECIAL_10);
		
		WebElement CHANNEL_MASTER = driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_MASTER));
		CHANNEL_MASTER.click();
		
		WebElement EMAIL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL));
		
		comm.insertData(EMAIL_NAME, 1, channelMaster);

		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		TimeUnit.SECONDS.sleep(4);
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		TimeUnit.SECONDS.sleep(4);
		driver.findElement(By.xpath(XPATH_CHANNEL_REGIST_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(driver.findElements(By.xpath(XPATH_TOAST2)).isEmpty()) {
			failMsg = "can not find toast msg";
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST5)) {
			failMsg = failMsg + "\n 2.toast msg error(Register Channel) [Expected]" + MSG_TOAST5
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST);
		
		ListDataNullCheck();
		
		SingleRowDataCheck();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=28) 
	public void Channel_info() throws Exception {
		String failMsg = "";
		
		channelName = "ChannelTest";
		channelMaster = seminartest.CommonValues.USERNICKNAME_ADMIN + "(" +seminartest.CommonValues.USEREMAIL_ADMIN + ")";
		channelDescription = admin.CommonValues.SPECIAL_10; 
		channelDisclosure = "PRIVATE";
		Plan = "";
		numberofSeminars = "0건";
		runningTime = "00:00:00";
		
		cellClick("[1]");
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		/*
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_BIGNAME)).getText().contentEquals(channelName)){
			failMsg = "1.Channel info big name is wrong [Expected]" + channelName + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_BIGNAME)).getText();
		}
		*/
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText().contentEquals(channelName)){
			failMsg = failMsg + "\n 2.Channel info channel name is wrong [Expected]" + channelName + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELMASTER)).getText().contentEquals(channelMaster)){
			failMsg = failMsg + "\n 3.Channel info channel master is wrong [Expected]" + channelMaster + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELMASTER)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText().contentEquals(channelDescription)){
			failMsg = failMsg + "\n 4.Channel info channel description is wrong [Expected]" + channelDescription + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDISCLOSURE)).getText().contentEquals(channelDisclosure)){
			failMsg = failMsg + "\n 5.Channel info channel disclosure is wrong [Expected]" + channelDisclosure + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDISCLOSURE)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELPLAN)).getText().contentEquals(Plan)){
			failMsg = failMsg + "\n 6.Channel info channel Plan is wrong [Expected]" + Plan + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELPLAN)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME)).getText().contentEquals(runningTime)){
			failMsg = failMsg + "\n 7.Channel info channel Runningtime is wrong [Expected]" + runningTime + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME)).getText();
		}
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS)).getText().contentEquals(numberofSeminars)){
			failMsg = failMsg + "\n 8.Channel info channel Number of Seminars is wrong [Expected]" + numberofSeminars + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS)).getText();
		}
		/*
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB1)).isSelected()) {
			failMsg = failMsg + "\n 8. seminar tab is not default Channel info tab ";
		}
		*/
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=29) 
	public void Channel_infoTAB() throws Exception {
		String failMsg = "";
		
		channelDisclosure = "PRIVATE";
		
		CheckURL(1, admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO + channelID);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB1)));
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB1)).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB1_PANEL + XPATH_CHANNEL_TAB_NODATA)).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = "1. seminar tab exist data" ; 
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2)).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2_PANEL + XPATH_CHANNEL_TAB_NODATA)).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = failMsg + "\n 2. channel Member tab exist data" ; 
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB3)).click();

		/*개발 진행중
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB3 + XPATH_CHANNEL_TAB_NODATA)).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = failMsg + "\n 3. payment tab exist data" ; 
		}
		*/
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB4)).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB4_PANEL + "//span[@class='ant-radio ant-radio-checked']/input")).getAttribute("value").contentEquals(channelDisclosure)) {
			failMsg = failMsg + "\n 4.channelDisclosure is different + [Expected]" + channelDisclosure
					+ " [Acutal]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB4_PANEL + "//span[@class='ant-radio ant-radio-checked']/input")).getAttribute("value");
			
		}
		
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		seminartest.DBConnection DB = new seminartest.DBConnection();
		DB.deleteChannel(channelID);
		
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
		
	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST)) {
			wd.get(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST);
			Thread.sleep(500);
		}
	}
	
	private void insertKeywordSearch(String data) throws Exception {
		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));
		WebElement keywordsearchBtn = driver.findElement(By.xpath(KEYWORD_SEARCH_BTN));
		
		while(!keywordtextbox.getAttribute("value").isEmpty() || !keywordtextbox.getText().isEmpty())
			keywordtextbox.sendKeys(Keys.BACK_SPACE);
		
		keywordtextbox.sendKeys(data);
		Thread.sleep(1000);
		keywordsearchBtn.click();
	}
	
	private void CountCheckandDataCheck(String data) throws Exception {
		String failMsg = "";	
	
		List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		
		int ROWcount = rows.size();
		
		String N2 = driver.findElement(By.xpath("//span[@class='total']")).getText().replace("건", "");
		int Realcount = Integer.parseInt(N2);
		
		Thread.sleep(1000);
		
		if(Realcount == 0) {
			System.out.println("no data");
			if(!rows.isEmpty()) {
				failMsg = failMsg + "no data but row is not empty!";
			}
		}else if(Realcount <= 100) {
			if(Realcount >= 31) {
				driver.findElement(By.xpath("//*[@id=\"index-page-wrap\"]/div/div[2]/div/label[3]/span[1]")).click();
				Thread.sleep(1000);
			}
			
			for(int i=0; i<ROWcount; i++){
			List<WebElement> a = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			String b = a.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			String c = a.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
			
			if(data == " ") {
				
			}
			else {
				if(!(b.contains(data) || c.contains(data))) {
					failMsg = failMsg + "\n 2.wrong Data Search";}
				 }
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		 }	
	}
	
	private void SingleRowDataCheck() throws Exception  {
		String failMsg = "";
		
		List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		String openingdate = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[1]")).getText();
		String channel_name = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
		String channel_master = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
		String plan = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[4]")).getText();
		String runningtime = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText();
		String channel_member = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText();
		String numberofseminars = rows.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[7]")).getText();
		
		if(!openingdate.contentEquals(date())) {
			failMsg = "1.Opening date is wrong";
			}
		
		if(!channel_name.contentEquals(channelName) ) {
			failMsg = failMsg + "\n 1-2.channelName is wrong [Expected]" + channelName + " [Actual]"  + channel_name;
		}
		
		if(!channel_master.contains(channelMaster) ) {
			failMsg = failMsg + "\n 1-3.channelMaster is wrong [Expected]" + channelMaster + " [Actual]"  + channel_master;
		}
		
		if(!plan.contentEquals(Plan) ) {
			failMsg = failMsg + "\n 1-4.Plan is wrong [Expected]" + Plan + " [Actual]"  + plan;
		}
		
		if(!runningtime.contentEquals(runningTime) ) {
			failMsg = failMsg + "\n 1-5.runningTime is wrong [Expected]" + runningTime + " [Actual]"  + runningtime;
		}
		
		if(!channel_member.contentEquals(channelMember) ) {
			failMsg = failMsg + "\n 1-6.channelMember is wrong [Expected]" + channelMember + " [Actual]"  + channel_member;
		}
		
		if(!numberofseminars.contentEquals(numberofSeminars) ) {
			failMsg = failMsg + "\n 1-7.numberofSeminars is wrong [Expected]" + numberofSeminars + " [Actual]"  + numberofseminars;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		 }
		
	}
	
	private void ListDataNullCheck() throws Exception {
		List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		int ROWcount = rows.size();
		String N2 = driver.findElement(By.xpath("//span[@class='total']")).getText().replace("건", "");
		int Realcount = Integer.parseInt(N2);
		
		if(ROWcount == 0 && Realcount == 0) {
			driver.findElement(By.xpath("//*[@id=\"dateRadio\"]/label[1]/span[2]")).click();
			driver.findElement(By.xpath(KEYWORD_SEARCH_BTN)).click();
		}
	}
	
	private void cellClick(String Cell) throws Exception {
		List<WebElement> row = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		row.get(0).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + Cell)).click();
		Thread.sleep(1000);
	}
	
	private void CheckURL(int N, String URL) throws Exception {
		String failMsg = "";
	
		switch(N) {
		case 1 : if(!driver.getCurrentUrl().contentEquals(URL)) {
			failMsg = failMsg + "\n Wrong URL [Expected]" + URL + " [Actual]" + driver.getCurrentUrl();
			}
		case 2 :  if(!driver.getCurrentUrl().contains(URL)) {
			failMsg = failMsg + "\n Wrong URL [Expected]" + URL + " [Actual]" + driver.getCurrentUrl();
			}
		
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
	 	}	
	}
	
	private String date() {
	
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String today = sdf.format(cal.getTime());
		
		return today;
		
	}
	
}