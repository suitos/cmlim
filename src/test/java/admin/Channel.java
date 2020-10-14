package admin;

import seminartest.CommonValues;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
/*
 * 0.채널 관리 입장
 * 1.검색란 ui확인 및 디폴트 확인
 * 2.날짜 검색(미완..)
 * 10.키워드 검색
 * 11.키워드 검색 invalid1
 * 12.키워드 검색 invalid2
 * 
 * 20.채널 추가
 * 21.채널 추가 시 빈값 입력
 * 22.채널명 입력 하여 에러메세지 미노출 확인
 * 24.채널명 문자+숫자+특수문자 입력 후 길이 체크
 * 25.채널 마스터 빈값, 잘못된 값 입력 후 검색 클릭
 * 26.채널 마스터 입력
 * 
 */
public class Channel {
	public String Standarddate = "";
	//채널 등록 시 
	public static String XPATH_CHANNEL_INFO_NAME = "//input[@id='channelName']";
	public static String XPATH_CHANNEL_INFO_NAME_ERROR = "//input[@id='channelName']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_INFO_MASTER = "//input[@id='channelMaster']";
	public static String XPATH_CHANNEL_INFO_MASTER_ERROR = "//input[@id='channelMaster']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_INFO_PRIVATE = "//input[@value='PRIVATE']";
	public static String XPATH_CHANNEL_INFO_PUBLIC = "//input[@value='PUBLIC']";
	public static String XPATH_CHANNEL_INFO_DESCRIPTION = "//input[@id='channelDescription']";
	
	public static String XPATH_CHANNEL_INFO_SAVE_BTN = "//button[@type='submit']";
	public static String XPATH_CHANNEL_INFO_CANCEL_BTN = "//button[@class='ant-btn']";
	//채널 등록 시 회원검색
	public static String XPATH_CHANNEL_USERSEARCH_EMAIL = "//input[@name='email']";
	public static String XPATH_CHANNEL_SEARCH_EMAIL_ERROR = "//div[@class='ant-form-item-explain']";
	
	public static String XPATH_CHANNEL_USERSEARCH_SEARCH_BTN = "//div[@class='ant-col ant-col-6']/button[1]";
	public static String XPATH_CHANNEL_USERSEARCH_SAVE_BTN = "//div[@class='ant-modal-footer']/button[1]";
	public static String XPATH_CHANNEL_USERSEARCH_CANCEL_BTN = "//div[@class='ant-modal-footer']/button[2]";
	//토스트메세지
	public static String XPATH_TOAST = "//span[@class='anticon anticon-close-circle']";
	//키워드검색
	public static String KEYWORD_SEARCH_TEXTBOX = "//input[@id='keyword']";
	public static String KEYWORD_SEARCH_BTN = "//button[@id='search']";
	public static String KEYWORD_PLACEHOLDER = "채널명, 채널 마스터의 닉네임, 이메일을 입력하세요.";
	
	public static String MSG_CHANNEL_NAME_EMPTY = "채널명을 기입해주세요.";
	public static String MSG_CHANNEL_MASTER_EMPTY = "채널마스터를 설정해주세요.";
	public static String MSG_CHANNEL_SEARCH_EMPTY = "이메일을 입력해주세요.";
	public static String MSG_CHANNEL_SEARCH_WRONG = "등록된 사용자가 없습니다.";
	public static String MSG_TOAST = "올바르지 않습니다.";
	public static String MSG_TOAST2 = "사용자 검색에 실패하였습니다.";

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
		
		comm.logintadmin(driver, admin.CommonValues.USER_PARTNER_KR);

		System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void EnterChannelList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(admin.CommonValues.XPATH_MENU_CHANNEL)).click();
		
		if(!driver.getCurrentUrl().contentEquals(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELLIST)) {
			failMsg = "1. not channel info view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
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
		
		seminartest.CommonValues comm2 = new seminartest.CommonValues();
		
		//채널명 일부 입력 후 검색
		insertKeywordSearch("rsrsup");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("rsrsup");
		
		Thread.sleep(1000);
		
		//채널 마스터 닉네임 입력 후 검색
		insertKeywordSearch(comm2.USERNICKNAME_JOIN);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(comm2.USERNICKNAME_JOIN);
		
		Thread.sleep(1000);

		//채널 마스터 이메일 입력 후 검색
		insertKeywordSearch(comm2.USEREMAIL_PRES);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(comm2.USEREMAIL_PRES);
			
	}
	
	@Test(priority=11)
	public void keywordSearch_invalid1() throws Exception {
		
		seminartest.CommonValues comm2 = new seminartest.CommonValues();
		
		insertKeywordSearch(comm2.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(comm2.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(comm2.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(comm2.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(comm2.USEREMAIL_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(comm2.USEREMAIL_JP);
		
		Thread.sleep(1000);
	}
	
	@Test(priority=12)
	public void keywordSearch_invalid2() throws Exception {
		/*개발예정
		insertKeywordSearch("");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("");
		
		Thread.sleep(1000);
		
		insertKeywordSearch("@");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("@");
	}
	
	@Test(priority=13)
	public void test() throws Exception {
	
		
	}
		
	
	
	@Test(priority=20)
	public void addChannel() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);
		
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_CHANNELREGISTER_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW)) {
			failMsg = "1.not new channel view. current url : " + driver.getCurrentUrl();
			driver.get(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME)).getAttribute("placeholder").contentEquals("channelName")) {
			failMsg = failMsg + "\n 2.name placeholder. [Expected]channelName [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER)).getAttribute("placeholder").contentEquals("채널 마스터를 선택해주세요")) {
			failMsg = failMsg + "\n 3.master placeholder. [Expected]채널 마스터를 선택해주세요 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_DESCRIPTION)).getAttribute("placeholder").contentEquals("channelDescription")) {
			failMsg = failMsg + "\n 4.description placeholder. [Expected]channelDescription [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_INFO_DESCRIPTION)).getAttribute("placeholder");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=21)
	public void addChannel_empty() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW)) {
			driver.get(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELNEW);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText().contentEquals(MSG_CHANNEL_NAME_EMPTY)) {
			failMsg = "1.channel name error(empty) [Expected]" + MSG_CHANNEL_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER_ERROR)).getText().contentEquals(MSG_CHANNEL_MASTER_EMPTY)) {
			failMsg = failMsg + "\n 2.channel master error(empty) [Expected]" + MSG_CHANNEL_MASTER_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=22)
	public void addChannel_insertChannelname() throws Exception {
		String failMsg = "";	

		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME));
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(CHANNEL_NAME, 1);

		 if(!driver.findElements(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).isEmpty()){
			 failMsg = "1.error MSG displayed [Expected] empty [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText();   
		 }
		 
		 if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		}
	}
	
	@Test(priority=24)
	public void addChannel_lengthCheck() throws Exception {
		String failMsg = "";	

		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME));
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(CHANNEL_NAME, 2);
		
		String a = CHANNEL_NAME.getAttribute("value");
		System.out.println(a);
		System.out.println(a.length());
		if(a.length() != 30) {
			failMsg = "1.Channel Name is wrong length [Actual]" + a.length();
		}
		
		Thread.sleep(1000);
		
		WebElement CHANNEL_DESCRIPTION = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_DESCRIPTION));
		comm.insertData(CHANNEL_DESCRIPTION, 3);
		
		String b = CHANNEL_DESCRIPTION.getAttribute("value");
		System.out.println(b);
		System.out.println(b.length());
		
		if(b.length() != 500) {
			failMsg = failMsg + "\n 2.Channel Description is wrong length [Actual]" + b.length();
		}
		
		 if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		}
	}
	
	@Test(priority=25) //테스트 순서 추후에 변경
	public void addChannel_insertChannelMaster() throws Exception {
		String failMsg = "";	
		
		WebElement CHANNEL_MASTER = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER));
		CHANNEL_MASTER.click();
		
		//placeholder 체크
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL)).getAttribute("placeholder").contentEquals("email")) {
			failMsg = "1.email placeholder. [Expected]email [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_EMAIL)).getAttribute("placeholder");
		}
		
		//빈값 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_EMPTY)) {
			failMsg = failMsg + "\n 2.master email error(empty value search) [Expected]" + MSG_CHANNEL_SEARCH_EMPTY; 
		}
		
		//빈값 입력 후 등록 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
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
		comm.insertData(EMAIL_NAME, 1);
		
		//이메일포맷이 아닌 문자열 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SEARCH_BTN)).click();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 5.master email error(wrong value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST2)) {
			failMsg = failMsg + "\n 6.toast msg error(wrong valuesearch) [Expected]" + MSG_TOAST2 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		//이메일포맷이 아닌 문자열 저장 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 7.master email error(wrong value save) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST)) {
			failMsg = failMsg + "\n 8.toast msg error(wrong value save) [Expected]" + MSG_TOAST 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		
		comm.insertData(EMAIL_NAME, 5);
		
		//등록되지 않은 유저 이메일 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 9.master email error(do not register value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		
		comm.insertData(EMAIL_NAME, 6);
		
		//파트너 관리자 권한으로 등록된 유저 이메일 입력 후 검색 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_USERSEARCH_SAVE_BTN)).click();
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n 10.master email error(partner email value search) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
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
	
	private void CountCheckandDataCheck(String data) throws Exception{
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
			List<WebElement> a = rows.get(i).findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			String b = a.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			String c = a.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
			
			if(!(b.contains(data) || c.contains(data))) {
				failMsg = failMsg + "\n 2.wrong Data Search";}
				}
			}
		
		 if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		 	}	
		}
	
}