package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * 1. 세미나 만들기 (저장된 세미나). 만든 세미나 초대관리화면 이동
 * 2. 세미나 리스트에서 이동. 세미나 로고 클릭
 * 3. 초대관리 - 이메일 추가1
 * 4. 초대관리 - 이메일 추가2 - 다중추가
 * 5. 초대관리 - 이메일 검색
 * 6. 초대관리 - 이메일 삭제
 * 7. 초대관리 - 이메일 저장
 * 8. 초대관리 - 게시완료후 초대화면 확인
 * 11. 세미나 초대화면 확인. 타이틀 클릭 확인
 * 12. 세미나 초대. valid email case
 * 13. 세미나 초대. invalid email case1
 * 14. 세미나 초대. invalid email case2
 * 15. 세미나 초대. 리스트 빈채로 발송 시도
 * 16. 세미나 초대. 리스트 추가하고 발송 클릭 - 발송 취소
 * 17. 세미나 초대. 리스트 추가하고 발송 클릭 - 발송
 * 18. 세미나 초대. 발송리스트 확인, 검색
 * 19. 세미나 초대. 발송리스트. 체크없이 재발송 시도
 * 20. 세미나 초대. 발송리스트. 이메일 체크 후 재발송 시도
 * 100. clear - delete seminar
 * 
 */

public class SeminarInvite {
	public static String XPATH_INVITETEMP_EMAIL_INPUT = "//div[@class='inviteTempAttendee__add__input']/input";
	public static String XPATH_INVITETEMP_EMAIL_ADD_BTN = "//div[@class='inviteTempAttendee__add__input']/button";
	public static String XPATH_INVITETEMP_EMAIL_ERROR = "//div[@class='inviteTempAttendee__add__input']/span[@class='inviteTempAttendee__add__input__error-msg']";
	public static String XPATH_INVITETEMP_EMAIL_LIST = "//li[@class='InviteTempAttendeeListItem_inviteTempAttendeeListItem__3aox4']";
	public static String XPATH_INVITETEMP_EMAIL_SEARCH_INPUT = "//input[@id='search']";
	public static String XPATH_INVITETEMP_EMAIL_DELETEALL_BTN = "//button[@class='btn btn-danger btn-s ']";
	public static String XPATH_INVITETEMP_EMAIL_SAVE_BTN = "//div[@class='InviteTempAttendee_inviteTempAttendee__buttons__weaJs']/button";
	public static String XPATH_INVITETEMP_TOAST = "//div[@class='wrap-toast-outer']//span";
	
	public static String XPATH_INVITE_EMAIL_INPUT = "//div[@class='inviteAttendeeToEmail__add__input']/input";
	public static String XPATH_INVITE_EMAIL_ADD_BTN = "//div[@class='inviteAttendeeToEmail__add__input']/button";
	public static String XPATH_INVITE_EMAIL_ERROR = "//div[@class='inviteAttendeeToEmail__add__input']/span[@class='inviteAttendeeToEmail__add__input__error-msg']";
	public static String XPATH_INVITE_EMAIL_LIST = "//li[@class='InviteAttendeeToEmailListItem_inviteAttendeeToEmailListItem__qX_6g']";
	public static String XPATH_INVITE_SENDEMAIL_LIST = "//li[@class='inviteAttendeeList__table__list__item']";
	public static String XPATH_INVITE_SENDEMAIL_LIST_EMAIL = "./span[@class='inviteAttendeeList__table__email']";
	public static String XPATH_INVITE_EMAIL_SEND_BTN = "//div[@class='InviteAttendeeToEmail_inviteAttendeeToEmail__buttons__1SJa5']/button";
	public static String XPATH_INVITE_EMAIL_INVITE_TAB = "//ul[@class='InviteAttendee_inviteAttendee__tab__dYK8O']/li[1]";
	public static String XPATH_INVITE_EMAIL_SEND_TAB = "//ul[@class='InviteAttendee_inviteAttendee__tab__dYK8O']/li[2]";
	public static String XPATH_INVITE_SEARCH_COUNT = "//span[@class='inviteAttendeeToEmail__search__count']";
	public static String XPATH_INVITE_STATUS_TOTAL = "//span[@class='inviteAttendeeMailStatus__list__item__statistic--highlight']/span";
	public static String XPATH_INVITE_STATUS_SUCCESS = "//ul[@class='InviteAttendeeMailStatus_inviteAttendeeMailStatus__list__1oGa9']/li[2]/p/span";
	public static String XPATH_INVITE_EMAIL_SEARCH = "//input[@id='search-email']";
	public static String XPATH_INVITE_RESEND_BTN = "//div[@class='InviteAttendeeList_inviteAttendeeList__resend__3ydkh']/button";
	
	
	public static String PLACEHOLDER_INPUTBOX_ADD_EMAIL = "Enter your email address.";
	public static String MSG_INVITE_EMAIL_INCORRECT = "Incorrect email format.";
	public static String MSG_INVITE_EMAIL_DUPLICATE = "This email is already in use.";
	public static String MSG_INVITE_EMAIL_DELETEALL = "The entire email list will be deleted.\nDo you want to delete?";
	public static String MSG_INVITE_EMAIL_SAVE = "The invitation list has been saved.";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarTitle = "";
	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	
	private List<String> addedEmail;
	private List<String> sendoutEmail;
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
		driver.get(CommonValues.SERVER_URL);
		
		addedEmail = new ArrayList<String>();
		sendoutEmail = new ArrayList<String>();
		
        System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	  }	

	//1. 세미나 만들기 (저장된 세미나)
	@Test (priority=1)
	public void seminarSave() throws Exception {
		String failMsg = "";

		//create seminar after 10minute
		seminarTitle = Thread.currentThread().getStackTrace()[1].getMethodName();
		seminarID = createSeminar(seminarTitle, "3");
		
	
		if (seminarID.isEmpty()) {
			failMsg = "fail to create seminar" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//2. 세미나 리스트에서 이동. 세미나 로고 클릭
	@Test (priority=2, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void seminarInviteTempView() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem))) {
				
				WebElement seminarItem = driver.findElement(By.xpath(listitem));
				WebElement we = seminarItem.findElement(By.xpath("../../../."));
				
				ListTest list = new ListTest();
				if(list.buttonTest(we, "invite-temp", true)) {
					Thread.sleep(500);
					
				} else {
					failMsg = "1. can not find invite-temp button";
				}
				break;	
			}
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID)) {
			failMsg = failMsg + "\n2. not temp invite view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		//title click
		driver.findElement(By.xpath("//h2[@class='InviteTempAttendee_inviteTempAttendee__title__nNz_-']/button")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			failMsg = failMsg + "\n2. not detail invite view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//3. 초대관리 - 이메일 추가1
	@Test (priority=3, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_addemail() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).getAttribute("placeholder").contentEquals(PLACEHOLDER_INPUTBOX_ADD_EMAIL)) {
			failMsg = "1. add email inputbox placeholder [Expected]" + PLACEHOLDER_INPUTBOX_ADD_EMAIL 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).getAttribute("placeholder");
		}
		
		//invalid format
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("invalid.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_INCORRECT)) {
				failMsg = failMsg + "\n2. add email error(invalid format) [Expected]" + MSG_INVITE_EMAIL_INCORRECT
						+ " [Actual] + driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText()";
			}
		} else {
			failMsg = failMsg + "\n3. can not find error message.";
		}
		
		//valid email
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup1@gmail.com");
		addedEmail.add("rsrsup1@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() == 1) {
			if(!emailList.get(0).findElement(By.xpath("./div/span[2]")).getText().contentEquals(addedEmail.get(0))) {
				failMsg = failMsg + "\n4. added email error. [Expected]" + addedEmail.get(0) 
				+ " [Actual]" + emailList.get(0).findElement(By.xpath("./div/span[2]")).getText();
				addedEmail.remove(0);
			}
		} else {
			failMsg = failMsg + "\n5. email list size error. [Expected]1 [Actual]" + emailList.size();
		}
		
		//duplicated email
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup1@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_DUPLICATE)) {
				failMsg = failMsg + "\n6. add email error(duplicated email) [Expected]" + MSG_INVITE_EMAIL_DUPLICATE
						+ " [Actual] + driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText()";
			}
		} else {
			failMsg = failMsg + "\n7. can not find error message.";
		}	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//4. 초대관리 - 이메일 추가2 - 다중추가
	@Test (priority=4, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_addemail2() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		//invalid format
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("invalid.com hello@test");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_INCORRECT)) {
				failMsg = failMsg + "1. add email error(invalid format) [Expected]" + MSG_INVITE_EMAIL_INCORRECT
						+ " [Actual] + driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText()";
			}
		} else {
			failMsg = failMsg + "\n2. can not find error message.";
		}

		//duplicated email
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup1@gmail.com hello@test.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_DUPLICATE)) {
				failMsg = failMsg + "\n3. add email error(duplicated email) [Expected]" + MSG_INVITE_EMAIL_DUPLICATE
						+ " [Actual] + driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ERROR)).getText()";
			}
		} else {
			failMsg = failMsg + "\n4. can not find error message.";
		}	
		
		//vaoid 2 email
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup2@gmail.com rsrsup3@gmail.com");
		addedEmail.add("rsrsup2@gmail.com");
		addedEmail.add("rsrsup3@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() == 3) {
			
			for(int i = 0 ; i < emailList.size() ; i ++) {
				if(!emailList.get(i).findElement(By.xpath("./div/span[2]")).getText().contentEquals(addedEmail.get(i))) {
					failMsg = failMsg + "\n4" + i + ". added email error. [Expected]" + addedEmail.get(i) 
					+ " [Actual]" + emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
				}
			}
			
		} else {
			failMsg = failMsg + "\n5. email list size error. [Expected]3 [Actual]" + emailList.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//5. 초대관리 - 이메일 검색
	@Test (priority=5, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_search() throws Exception {
		String failMsg = "";
		
		List<WebElement> emailListAll = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		List<WebElement> emailListSearch = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailListAll.size() != emailListSearch.size()) {
			failMsg = failMsg + "1. searched email [Expected]" + emailListAll.size() + " [Actual]" + emailListSearch.size();
		}
		
		String searchKeyword = "rsrsup3";
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys(searchKeyword);
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		emailListSearch = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		
		for(WebElement we : emailListSearch) {
			if(!we.findElement(By.xpath("./div/span[2]")).getText().contains(searchKeyword)) {
				failMsg = failMsg + "\n2. searched email. [Expected] contains " + searchKeyword
				+ " [Actual]" + we.findElement(By.xpath("./div/span[2]")).getText();
			}
		}
		
		// invalid keyword
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys("invalid");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		emailListSearch = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		
		if(emailListSearch.size() != 0) {
			failMsg = failMsg + "\n3. searched email. [Expected] 0 " + " [Actual]" + emailListSearch.size();
		}
		
		
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SEARCH_INPUT)).sendKeys(Keys.ENTER);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//6. 초대관리 - 이메일 삭제
	@Test (priority=6, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_delete() throws Exception {
		String failMsg = "";
		
		List<WebElement> emailListAll = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		
		//remove 2nd item
		emailListAll.get(1).findElement(By.xpath("./button")).click();
		addedEmail.remove(1);
		Thread.sleep(500);
		
		emailListAll = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		
		for(int i = 0 ; i < emailListAll.size() ; i++) {
			if(!emailListAll.get(i).findElement(By.xpath("./div/span[2]")).getText().contentEquals(addedEmail.get(i))) {
				failMsg = failMsg + "1-" + i + ". added list error. [Expected]" + addedEmail.get(i) 
				+ " [Actual]" + emailListAll.get(i).findElement(By.xpath("./div/span[2]")).getText();
			}
		}
		
		//delete all
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_INVITE_EMAIL_DELETEALL)) {
			failMsg = failMsg + "\n2. popup message. [Expected]" + MSG_INVITE_EMAIL_DELETEALL
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}
		
		//cancel
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		Thread.sleep(500);
		
		emailListAll = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailListAll.size() == 0) {
			failMsg = failMsg + "\n3. email list size [Expected]" + addedEmail.size() + " [Actual]" + emailListAll.size();
		}
		
		//delete all - confirm
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);
		addedEmail.clear();
		
		emailListAll = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailListAll.size() != 0) {
			failMsg = failMsg + "\n3. email list size [Expected]0" + " [Actual]" + emailListAll.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	//7. 초대관리 - 이메일 저장
	@Test (priority=7, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_save() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID);
			Thread.sleep(500);
		} else {
			driver.navigate().refresh();
			Thread.sleep(500);
		}
		
		//save empty list
		addedEmail.clear();
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 0) {
			driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SAVE_BTN)).click();
		Thread.sleep(300);
		
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_TOAST))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText().contentEquals(MSG_INVITE_EMAIL_SAVE)) {
				failMsg = "1. save toast message [Expected]" + MSG_INVITE_EMAIL_SAVE
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText();
			}
		} else {
			failMsg = "2. cannot find save toast";
			
			if(isElementPresent(By.xpath(CommonValues.XPATH_MODAL_BODY))){
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
			}
		}
		//refresh
		driver.navigate().refresh();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 0) {
			failMsg = failMsg + "\n3. saved list [Expected]0"
					+ " [Actual]" + emailList.size();
		}
		
		//add 3
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup1@gmail.com rsrsup2@gmail.com rsrsup3@gmail.com");
		addedEmail.add("rsrsup1@gmail.com");
		addedEmail.add("rsrsup2@gmail.com");
		addedEmail.add("rsrsup3@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		//save
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SAVE_BTN)).click();
		Thread.sleep(500);
		if(isElementPresent(By.xpath(XPATH_INVITETEMP_TOAST))) {
			if(!driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText().contentEquals(MSG_INVITE_EMAIL_SAVE)) {
				failMsg = failMsg + "\n4. save toast message [Expected]" + MSG_INVITE_EMAIL_SAVE
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n5. cannot find save toast";
			
			if(isElementPresent(By.xpath(CommonValues.XPATH_MODAL_BODY))){
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 3) {
			failMsg = failMsg + "\n6. saved list [Expected]3"
					+ " [Actual]" + emailList.size();
		} else {
			for(int i = 0 ; i < emailList.size() ; i++) {
				if(!addedEmail.contains(emailList.get(i).findElement(By.xpath("./div/span[2]")).getText())) {
					failMsg = failMsg + "\n7-" + i + ". added list error. [Expected]" + addedEmail
					+ " [Actual]" + emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
				}
			}
		}
		
		//delete 1(1st email)
		emailList.get(0).findElement(By.xpath("./button")).click();
		addedEmail.remove(0);
		Thread.sleep(500);
		//save
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SAVE_BTN)).click();
		Thread.sleep(500);
		
		driver.navigate().refresh();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 2) {
			failMsg = failMsg + "\n8. saved list [Expected]2"
					+ " [Actual]" + emailList.size();
		} else {
			for(int i = 0 ; i < emailList.size() ; i++) {
				if(!addedEmail.contains(emailList.get(i).findElement(By.xpath("./div/span[2]")).getText())) {
					failMsg = failMsg + "\n9-" + i + ". added list error. [Expected]" + addedEmail
					+ " [Actual]" + emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
				}
			}
		}
		
		//delete all
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);
		addedEmail.clear();
		//save
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SAVE_BTN)).click();
		Thread.sleep(500);
		
		driver.navigate().refresh();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 0) {
			failMsg = failMsg + "\n10. saved list [Expected]0"
					+ " [Actual]" + emailList.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//8. 초대관리 - 게시완료후 초대화면 확인
	@Test (priority=8, dependsOnMethods = {"seminarInvitationView"}, enabled = true)
	public void inviteTemp_post() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.INVITETEMP_VIEW + seminarID);
			Thread.sleep(500);
		}
		//save empty list
		addedEmail.clear();
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITETEMP_EMAIL_LIST));
		if(emailList.size() != 0) {
			driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		//add 2 & save
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_INPUT)).sendKeys("rsrsup1@gmail.com rsrsup2@gmail.com");
		addedEmail.add("rsrsup1@gmail.com");
		addedEmail.add("rsrsup2@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		
		//save
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_SAVE_BTN)).click();
		Thread.sleep(500);
		
		//post seminar
		CommonValues comm = new CommonValues();
		comm.postSeminar(driver, seminarID);
		
		//seminar Invite view
		driver.get(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID);
		Thread.sleep(500);
		
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if(emailList.size() != 2) {
			failMsg = "1. added email list error. [Expected]" + addedEmail.size()
					+ " [Actual]" + emailList.size();
		} else {
			for(int i = 0 ; i < emailList.size() ; i++) {
				if(!addedEmail.contains(emailList.get(i).findElement(By.xpath("./div/span[2]")).getText())) {
					failMsg = failMsg + "\n2-" + i + ". added list error. [Expected]" + addedEmail
					+ " [Actual]" + emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
				}
			}
		}
		
		// add 1 
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("rsrsup3@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if(emailList.size() != 3) {
			failMsg = failMsg + "\n3. added email list error.(2 + 1(temp) [Expected]3"
					+ " [Actual]" + emailList.size();
		} 
		
		driver.navigate().refresh();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if(emailList.size() != 2) {
			failMsg = failMsg + "\n4. added email list error. [Expected]" + addedEmail.size()
					+ " [Actual]" + emailList.size();
		} else {
			for(int i = 0 ; i < emailList.size() ; i++) {
				if(!addedEmail.contains(emailList.get(i).findElement(By.xpath("./div/span[2]")).getText())) {
					failMsg = failMsg + "\n5-" + i + ". added list error. [Expected]" + addedEmail
					+ " [Actual]" + emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
				}
			}
		}
		
		// add 1 & send email
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("rsrsup3@gmail.com");
		addedEmail.add("rsrsup3@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		sendoutEmail.addAll(addedEmail);
		addedEmail.clear();
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if(emailList.size() != 0) {
			failMsg = failMsg + "\n6. email list is not cleared [Expected]" + addedEmail.size()
					+ " [Actual]" + emailList.size();
		} 
		
		//check send list
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> sendEmail = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		if(sendEmail.size() != sendoutEmail.size()) {
			failMsg = failMsg + "\n7. added email list error. [Expected]" + sendoutEmail.size()
					+ " [Actual]" + sendEmail.size();
		} else {
			for(int i = 0 ; i < emailList.size() ; i++) {
				if(!addedEmail.contains(sendEmail.get(i).findElement(By.xpath(XPATH_INVITE_SENDEMAIL_LIST_EMAIL)).getText())) {
					failMsg = failMsg + "\n8-" + i + ". added list error. [Expected]" + sendoutEmail
					+ " [Actual]" + sendEmail.get(i).findElement(By.xpath(XPATH_INVITE_SENDEMAIL_LIST_EMAIL)).getText();
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//11. 세미나 초대화면 확인. 타이틀 클릭 확인
	@Test (priority=11, enabled = true)
	public void seminarInvitation_title() throws Exception {
		String failMsg = "";
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		driver.findElement(By.xpath("//h2[@class='InviteAttendee_inviteAttendee__title__8DMdU']//button")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		String detail = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if (!detail.equalsIgnoreCase(driver.getCurrentUrl())) {
			failMsg = "detail view : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//12. 세미나 초대. valid email case
	@Test (priority=12, enabled = true)
	public void seminarInvitation_valid1() throws Exception {
		String failMsg = "";
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			Thread.sleep(500);
		} else {
			driver.navigate().refresh();
			Thread.sleep(500);
		}
		
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).getAttribute("placeholder").contentEquals(PLACEHOLDER_INPUTBOX_ADD_EMAIL)) {
			failMsg = "1. inputbox placeholder. [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).getAttribute("placeholder");
		}
		
		//add 1
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abc@rsupport.com");
		addedEmail.add("abc@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(500);
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		
		//added item
		if(!driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 2. add 1 : selected persons [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText();
		}
		
		if((emailList.size() != 1) || !emailList.get(0).findElement(By.xpath("./div/span[2]")).getText().contentEquals(addedEmail.get(0))) {
			failMsg = failMsg + "\n 3. add 1 : added list [Actual]" + emailList.size() + ", " + emailList.get(0).findElement(By.xpath("./div/span[2]")).getText();
		}
		
		//add clipboard data(vaild 10 email)
		String ctc = CommonValues.CHANNEL_INVITE_CLIPBOARD_EMAIL;
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
		String[] emails = CommonValues.CHANNEL_INVITE_CLIPBOARD_EMAIL.split(" ");
		for(int i = 0 ; i < emails.length ; i++) {
			addedEmail.add(emails[i]);
		}
		
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(ctc);
		else
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.CONTROL, "v");
		
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		
		//added item
		if (!driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText().contentEquals(addedEmail.size() + " people")) {
			failMsg = failMsg + "\n 4. added user cound [Expected]" + addedEmail.size() + " people"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText();
		}

		if (emailList.size() != addedEmail.size()) {
			failMsg = failMsg + "\n 5. added user list count [Expected]" + addedEmail.size()
					+ " [Actual]" +  emailList.size();
		} 

		//delete 1
		emailList.get(0).findElement(By.xpath("./button")).click();
		addedEmail.remove(0);
		Thread.sleep(100);
		
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		
		if (!driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText().contentEquals(addedEmail.size() + " people")) {
			failMsg = failMsg + "\n 6. delete 1. user count [Expected]"+ addedEmail.size() + " people"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText();
		}

		if (emailList.size() != addedEmail.size()) {
			failMsg = failMsg + "\n 7. delete 1 : email list [Expected]" + addedEmail.size()
					+ " [Actual]" +  emailList.size();
		} 
		//delete all - cancel
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if (!driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText().contentEquals(addedEmail.size() + " people")) {
			failMsg = failMsg + "\n 8. user count [Expected]"+ addedEmail.size() + " people"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText();
		}
		if (emailList.size() != addedEmail.size()) {
			failMsg = failMsg + "\n 9. delete 1 : email list [Expected]" + addedEmail.size()
					+ " [Actual]" +  emailList.size();
		} 

		
		//delete all - confirm
		driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		addedEmail.clear();
		Thread.sleep(500);
		emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		if (!driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText().contentEquals(addedEmail.size() + " people")) {
			failMsg = failMsg + "\n 10. delete all. user count [Expected]"+ addedEmail.size() + " people"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_SEARCH_COUNT)).getText();
		}
		if (emailList.size() != addedEmail.size()) {
			failMsg = failMsg + "\n 11. delete all : email list [Expected]" + addedEmail.size()
					+ " [Actual]" +  emailList.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//13. 세미나 초대. invalid email case1
	@Test (priority=13, enabled = true)
	public void seminarInvitation_invalid() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//add valid 1
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abc@rsupport.com");
		addedEmail.add("abc@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));

		if ((emailList.size() != 1) || !emailList.get(0).findElement(By.xpath("./div/span[2]")).getText()
				.contentEquals(addedEmail.get(0))) {
			failMsg = failMsg + "\n 1. add 1 : added list [Actual]" + emailList.size() + ", "
					+ emailList.get(0).findElement(By.xpath("./div/span[2]")).getText();
		}
		
		//add empty
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
		
		if (!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText().contentEquals(CommonValues.INVITE_ERROR_EMAIL)) {
			failMsg = failMsg + "\n 2. add empty : error msg [Expected]" +CommonValues.INVITE_ERROR_EMAIL + "[Actual]"
					+ driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText();
		}
		
		//add invalid email
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abcdefg");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
	
		if (!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText().contentEquals(CommonValues.INVITE_ERROR_EMAIL)) {
			failMsg = failMsg + "\n 3. add invalid email : error msg [Expected]" + CommonValues.INVITE_ERROR_EMAIL + "[Actual]"
					+ driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText();
		}
		
		//add duplicated email
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(addedEmail.get(0));
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
		
		if (!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_DUPLICATE)) {
			failMsg = failMsg + "\n 4. add duplicated email : error msg [Actual]" + MSG_INVITE_EMAIL_DUPLICATE
					+ "[Actual]" + driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText();
		}
		
		//if 
		if(isElementPresent(By.xpath("//div[@class='modal-footer']/button[1]")))
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	//14. 세미나 초대. invalid email case2
	@Test (priority=14, enabled = true)
	public void seminarInvitation_invalid2() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//add valid email with invalid form
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abcdefg abcd@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
	
		if (!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText().contentEquals(CommonValues.INVITE_ERROR_EMAIL)) {
			failMsg = failMsg + "1. add invalid email : error msg [Expected]" + CommonValues.INVITE_ERROR_EMAIL + "[Actual]"
					+ driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText();
		}	
		
		//add valid with duplicated email
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(addedEmail.get(0) + " abcd@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);

		if (!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText().contentEquals(MSG_INVITE_EMAIL_DUPLICATE)) {
			failMsg = failMsg + "\n2. add invalid email : error msg [Expected]" + MSG_INVITE_EMAIL_DUPLICATE
					+ "[Actual]" + driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ERROR)).getText();
		} 
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));
		for(int i = 0 ; i < emailList.size() ; i++) {
			if(!addedEmail.contains(emailList.get(i).findElement(By.xpath("./div/span[2]")).getText())) {
				failMsg = failMsg + "\n3. added email Error. check email : " 
						+ emailList.get(i).findElement(By.xpath("./div/span[2]")).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//15. 세미나 초대. 리스트 빈채로 발송 시도
	@Test (priority=15, enabled = true)
	public void seminarInvitation_sendEmpty() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		addedEmail.clear();
		if(driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).isEnabled()) {
			//delete all - confirm
			driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		//click send
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.INVITE_ERROR_EMPTYLIST))
			failMsg = "\n 1. error email box msg [Expected]" + CommonValues.INVITE_ERROR_EMPTYLIST 
				+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		
		//click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//16. 세미나 초대. 리스트 추가하고 발송 클릭 - 발송 취소
	@Test (priority=16, enabled = true)
	public void seminarInvitation_send() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		if(driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).isEnabled()) {
			//delete all - confirm
			driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		
		//add 1
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abc@rsupport.com");
		addedEmail.add("abc@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		Thread.sleep(100);
		
		//add clipboard 2
		String ctc = "sinhyekim@rsupport.com rsrsup10@gmail.com";
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		addedEmail.add("sinhyekim@rsupport.com");
		addedEmail.add("rsrsup10@gmail.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(ctc);
		else
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.CONTROL, "v");
		
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.ENTER);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(500);
		
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));

		if ((emailList.size() != 3) ) {
			failMsg = failMsg + "1. add count [Expected]" + addedEmail.size()
					+ "[Actual]" + emailList.size();
		}
		
		//click send..
		WebElement element = driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_BTN));
		//driver.findElement(By.xpath("//div[@class='email-send-type']/button")).sendKeys(Keys.ENTER);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();",element );
		Thread.sleep(500);
		
		//msg
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.INVITE_MSG_SEND))
			failMsg = "\n 1. send email box msg [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		
		//click cancel
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		
		//send total count 
		if(!driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText().contentEquals(String.valueOf(sendoutEmail.size())))
			failMsg = failMsg + "\n 3. send cancel : total mail count. [Expected]" + sendoutEmail.size() 
				+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText();
		
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INVITE_TAB)).getAttribute("class").contentEquals("active"))
			failMsg = failMsg + "\n 4. send list tab is not active(Expected active list tab)";
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//17. 세미나 초대. 리스트 추가하고 발송 클릭 - 발송
	@Test (priority=17, enabled = true)
	public void seminarInvitation_send2() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		addedEmail.clear();
		if (driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).isEnabled()) {
			// delete all - confirm
			driver.findElement(By.xpath(XPATH_INVITETEMP_EMAIL_DELETEALL_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		
		//add 1
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_ADD_BTN)).click();
		addedEmail.add("abc@rsupport.com");
		Thread.sleep(100);
		
		//add clipboard 1
		String ctc = "sinhyekim@rsupport.com rsrsup10@gmail.com";
		addedEmail.add("sinhyekim@rsupport.com");
		addedEmail.add("rsrsup10@gmail.com");
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INPUT)).sendKeys(Keys.ENTER);

		Thread.sleep(500);
		
		List<WebElement> emailList = driver.findElements(By.xpath(XPATH_INVITE_EMAIL_LIST));

		if ((emailList.size() != 3) ) {
			failMsg = failMsg + "\n 1. add 3 : added list [Actual]" + addedEmail.size() + ", "
					+ emailList.get(0).findElement(By.xpath("./div/span[2]")).getText();
		}
		
		//click send...
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_BTN)).click();
		Thread.sleep(500);
		sendoutEmail.addAll(addedEmail);
		//msg
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.INVITE_MSG_SEND))
			failMsg = "\n 2. send email box msg [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		
		//click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(2000);
		
		if(!driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText().contentEquals(String.valueOf(sendoutEmail.size())))
			failMsg = failMsg + "\n 3. send 3 : total mail count [Exepcted]" + sendoutEmail.size() 
				+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText();
		
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_INVITE_TAB)).getAttribute("class").contentEquals("active"))
			failMsg = failMsg + "\n 4. send list tab is active(Expected active send tab)";
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//18. 세미나 초대. 발송리스트 확인, 검색
	@Test (priority=18, enabled = true)
	public void seminarInvitation_list() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).getAttribute("class").contentEquals("active")) {
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		
		//sent email
		if(!driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText().contentEquals(String.valueOf(sendoutEmail.size())))
			failMsg = failMsg + "\n 1. total mail count [Expected]" + String.valueOf(sendoutEmail.size())
				+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText();
		
		//emaillist : title 1 + email...
		if(emaillist.size() != sendoutEmail.size()) {
			failMsg = failMsg + "\n 2. list count [Expected] " + sendoutEmail.size() +" [Actual]" + emaillist.size();
		}
			
			
		//search empty
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).sendKeys(Keys.ENTER);

		emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		if (emaillist.size() != sendoutEmail.size())
			failMsg = failMsg + "\n 3. searched list count [Expected] " + sendoutEmail.size() +" [Actual]" + emaillist.size();
		
		//search invalid
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).sendKeys("invalid");
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		if (emaillist.size() != 0)
			failMsg = failMsg + "\n 4. search invalid keyworkd. list count : [Expected]0, [Actual]" + emaillist.size();
		
		//search valid
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).clear();
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).sendKeys(sendoutEmail.get(4).split("@")[0]);
		driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEARCH)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		if (emaillist.size() != 1) {
			failMsg = failMsg + "\n 5. search valid keyworkd : " + sendoutEmail.get(4).split("@")[0] 
					+" , list count [Expected]1, [Actual]" + emaillist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//19. 세미나 초대. 발송리스트. 체크없이 재발송 시도
	@Test (priority=19, enabled = true)
	public void seminarInvitation_resend_Empty() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).getAttribute("class").contentEquals("active")) {
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).click();
			Thread.sleep(500);
		}
		
		//click resend
		driver.findElement(By.xpath(XPATH_INVITE_RESEND_BTN)).click();
		Thread.sleep(500);
		
		//dialog msg
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_SELCECT_EMAIL))
			failMsg = "\n 1. popup msg [Expected]" + CommonValues.MSG_SELCECT_EMAIL 
			+ "[Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		
		//click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//20. 세미나 초대. 발송리스트. 이메일 체크 후 재발송 시도
	@Test (priority=20, enabled = true)
	public void seminarInvitation_resend() throws Exception {
		String failMsg = "";
		
		driver.navigate().refresh();
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			Thread.sleep(500);
		}

		if(!driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).getAttribute("class").contentEquals("active")) {
			driver.findElement(By.xpath(XPATH_INVITE_EMAIL_SEND_TAB)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		
		for(int i = 0 ; i < emaillist.size() ; i ++) {
			if(emaillist.get(i).findElement(By.xpath("./span[@class='inviteAttendeeList__table__email']")).getText().contentEquals("sinhyekim@rsupport.com")) {
				emaillist.get(i).findElement(By.xpath("./div[@class='checkbox']")).click();
				
				break;
			}
		}
		
		//click resend (resend 1)
		driver.findElement(By.xpath(XPATH_INVITE_RESEND_BTN)).click();
		Thread.sleep(500);
		
		//checktoast
		if(!driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText().contentEquals(CommonValues.MSG_SEND_EMAIL))
			failMsg = "\n 1. toast msg [Expected]" + CommonValues.MSG_SEND_EMAIL 
			+ "[Actual]" + driver.findElement(By.xpath(XPATH_INVITETEMP_TOAST)).getText();
		
	
		//total 
		if(!driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText().contentEquals(String.valueOf(sendoutEmail.size())))
			failMsg = failMsg + "\n 2. total mail count [Expected]" + String.valueOf(sendoutEmail.size())
			+ " [Actual]" + driver.findElement(By.xpath(XPATH_INVITE_STATUS_TOTAL)).getText();
		
		emaillist = driver.findElements(By.xpath(XPATH_INVITE_SENDEMAIL_LIST));
		if (emaillist.size() != sendoutEmail.size()) {
			failMsg = failMsg + "\n 3. search valid keyworkd : list count [Expected]" + sendoutEmail.size() 
				+ " [Actual]" + emaillist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//100. clear - delete seminar
	@Test (priority=100, enabled = true)
	public void seminarInvitation_fin() throws Exception {
		String detail = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detail);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		// delete seminar
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
	}
	
	public void clearAttributeValue(WebElement el) 
	{
		while(!el.getAttribute("value").isEmpty())
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

	public String createSeminar(String seminarName, String attendees) throws Exception {

		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		// check seminar type
		driver.findElement(By.id("seminar-type")).click();

		driver.findElement(By.xpath("//div[@class='box-option open']/div[1]")).click();

		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).sendKeys(seminarName);

		// set attendees
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).sendKeys(attendees);
		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarName, false);

		Thread.sleep(500);
	
		// save seminar
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		String seminarUri = comm.findSeminarIDInList(driver, seminarName);
		
		return seminarUri;
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
