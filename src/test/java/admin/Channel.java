package admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Channel {
	public static String XPATH_CHANNEL_INFO_NAME = "//input[@id='channelName']";
	public static String XPATH_CHANNEL_INFO_NAME_ERROR = "//input[@id='channelName']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_INFO_MASTER = "//input[@id='channelMaster']";
	public static String XPATH_CHANNEL_INFO_MASTER_ERROR = "//input[@id='channelMaster']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_CHANNEL_INFO_PRIVATE = "//input[@value='PRIVATE']";
	public static String XPATH_CHANNEL_INFO_PUBLIC = "//input[@value='PUBLIC']";
	public static String XPATH_CHANNEL_INFO_DESCRIPTION = "//input[@id='channelDescription']";
	
	public static String XPATH_CHANNEL_INFO_SAVE_BTN = "//button[@type='submit']";
	public static String XPATH_CHANNEL_INFO_CANCEL_BTN = "//button[@class='ant-btn']";
	
	public static String XPATH_CHANNEL_SEARCH_EMAIL = "//input[@name='email']";
	public static String XPATH_CHANNEL_SEARCH_EMAIL_ERROR = "//div[@class='ant-form-item-explain']";
	
	public static String XPATH_CHANNEL_SEARCH_SEARCH_BTN = "//div[@class='ant-col ant-col-6']/button[1]";
	public static String XPATH_CHANNEL_SEARCH_SAVE_BTN = "//div[@class='ant-modal-footer']/button[1]";
	public static String XPATH_CHANNEL_SEARCH_CANCEL_BTN = "//div[@class='ant-modal-footer']/button[2]";
	
	public static String XPATH_TOAST = "//span[@class='anticon anticon-close-circle']";

	public static String MSG_CHANNEL_NAME_EMPTY = "채널명을 기입해주세요.";
	public static String MSG_CHANNEL_MASTER_EMPTY = "채널마스터를 설정해주세요.";
	public static String MSG_CHANNEL_SEARCH_EMPTY = "이메일을 입력해주세요.";
	public static String MSG_CHANNEL_SEARCH_WRONG = "사용자 검색에 실패하였습니다.";
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

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		//driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR");

		context.setAttribute("webDriver", driver);
		driver.get(CommonValues.ADMIN_URL);
		
		comm.logintadmin(driver, CommonValues.USER_PARTNER_KR);

		System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority = 0)
	public void EnterChannelList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_MENU_CHANNEL)).click();
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELLIST)) {
			failMsg = "1. not channel info view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=10)
	public void keywordSearch() throws Exception {
		String failMsg = "";
		
		
	}
	
	@Test(priority=20)
	public void addChannel() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_CHANNELREGISTER_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELNEW)) {
			failMsg = "1. not new channel view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELNEW);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME)).getAttribute("placeholder").contentEquals("channelName")) {
			failMsg = failMsg + "\n2. name placeholder. [Expected]channelName [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER)).getAttribute("placeholder").contentEquals("채널 마스터를 선택해주세요")) {
			failMsg = failMsg + "\n3. master placeholder. [Expected]채널 마스터를 선택해주세요 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_DESCRIPTION)).getAttribute("placeholder").contentEquals("channelDescription")) {
			failMsg = failMsg + "\n4. description placeholder. [Expected]channelDescription [Actual]" 
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
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELNEW);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText().contentEquals(MSG_CHANNEL_NAME_EMPTY)) {
			failMsg = "1. channel name error(empty) [Expected]" + MSG_CHANNEL_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_MASTER_ERROR)).getText().contentEquals(MSG_CHANNEL_MASTER_EMPTY)) {
			failMsg = failMsg + "\n2. channel master error(empty) [Expected]" + MSG_CHANNEL_MASTER_EMPTY 
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
		CommonValues comm = new CommonValues();
		comm.insertData(CHANNEL_NAME, 1);

		 if(!driver.findElements(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).isEmpty()){
			 failMsg = "1. error MSG displayed [Expected] empty [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME_ERROR)).getText();   
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
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL)).getAttribute("placeholder").contentEquals("email")) {
			failMsg = "1. email placeholder. [Expected]email [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL)).getAttribute("placeholder");
		}
		
		/*빈값 입력 후 검색 클릭 TO-DO
		driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_SEARCH_BTN)).click();
		--TO-DO--
		
		*/
		
		//빈값 입력 후 저장 클릭
		driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_SAVE_BTN)).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_EMPTY)) {
			failMsg = failMsg + "\n2. master email error(empty) [Expected]" + MSG_CHANNEL_SEARCH_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST)) {
			failMsg = failMsg + "\n3. toast msg error(empty) [Expected]" + MSG_TOAST 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
		
		//잘못된 값 입력 후 검색 클릭
		WebElement EMAIL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL));
		CommonValues comm = new CommonValues();
		comm.insertData(EMAIL_NAME, 1);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_SAVE_BTN)).click();

		if(!driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText().contentEquals(MSG_CHANNEL_SEARCH_WRONG)) {
			failMsg = failMsg + "\n4. master email error(wrong) [Expected]" + MSG_CHANNEL_SEARCH_WRONG 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_SEARCH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_TOAST)).getText().contentEquals(MSG_TOAST2)) {
			failMsg = failMsg + "\n4. toast msg error(wrong) [Expected]" + MSG_TOAST2 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST)).getText();
		}
	}
	
	@Test(priority=24)
	public void addChannel_lengthCheck() throws Exception {
		String failMsg = "";	

		WebElement CHANNEL_NAME = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_NAME));
		CommonValues comm = new CommonValues();
		comm.insertData(CHANNEL_NAME, 2);
		
		String a = CHANNEL_NAME.getAttribute("value");
		System.out.println(a);
		System.out.println(a.length());
		if(a.length() != 30) {
			failMsg = "1.Channel Name is wrong length [Actual]" + a.length();
		}
		
		WebElement CHANNEL_DESCRIPTION = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_DESCRIPTION));
		comm.insertData(CHANNEL_DESCRIPTION, 3);
		
		String b = CHANNEL_DESCRIPTION.getAttribute("value");
		System.out.println(b);
		System.out.println(b.length());
		
		if(b.length() != 300) {
			failMsg = failMsg + "\n2.Channel Description is wrong length [Actual]" + b.length();
		}
		
		 if (failMsg != null && !failMsg.isEmpty()) {
				Exception e = new Exception(failMsg);
				throw e;
		}
	}
	
	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELLIST)) {
			wd.get(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELLIST);
			Thread.sleep(500);
		}
	}
	
}
