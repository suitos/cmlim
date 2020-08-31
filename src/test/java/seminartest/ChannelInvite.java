package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* ChannelInvite
 * part1
 * 1. 채널 관리 화면 이동
 * 2. 채널 초대 팝업 확인
 * 3. 채널 초대 팝업에 정상 이메일 추가(2개 이메일 추가-1개 이메일 리스트에서 삭제-남은 1개 이메일(RSUP4) 초대 클릭)
 * 4. 채널 초대 팝업에서 잘못된 형식의 이메일 추가시도
 * 6. 채널 초대 팝업 클립보드 데이터 붙여넣기로 리스트 추가 (10개 붙여넣기, 1개 삭제, 모두삭제로 삭제)
 * 7. 채널 초대 팝업 클립보드 데이터 잘못된 이메일 폼 추가된 데이터로 붙여넣기 시도
 * 
 * 10. rsrsup4로그인
 * 11. 채널 초대(가입)팝업 x 클릭
 * 12. 새로 로그인(팝업 새로 뜸 확인), 채널 초대 거절하기
 * 13. 채널 마스터가 거부한 멤버 상태 확인, 재초대 
 * 14. 재 초대한 멤버 초대 수락하기
 * 16. 가입한 멤버 마스터가 채널에서 삭제
 * 
 * 20. 삭제한 멤버 재 초대
 * 21. 멤버 채널 가입(재가입)
 * 22. 멤버가 채널에서 직접 탈퇴
 * 
 * 다중초대
 * 31. 준비 : rsrsup2, rsrsup8, rsrsup6 순서로 rsrsup4 를 멤버로 초대한다.
 * 32. 멤버 초대 팝업 확인. 순서대로 뜨는지 확인, rsrsup2 거절/rsrsup8 가입/ rsrsup6 대기
 * 33. 마스터가 거절한 멤버 삭제 rsrsup2
 * 34. 마스터가 대기중인 멤버 상태 확인 후 메일전송, 멤버 삭제 rsrsup6
 * 39. 정리 : rsrsup2, rsrsup8, rsrsup6가 rsrsup4를 삭제
 */
public class ChannelInvite {
	
	public static String INVITE_TRY_USER = "rsrsup7@gmail.com";
	public static String INVITE_MSG = "You have been invited to the channel %s"
			+ "\nJoin the invited channel to start or participate the seminars\non the channel.";
	public static String DELETE_INVITE_MEMBER = "Do you want to delete it from member invites?";
	public static String INVITE_RESEND_EMAIL = "Member invitation has been resent.";
	
	public static String INVITED_USER = "rsrsup4@gmail.com";
	
	public static WebDriver driver;
	public static WebDriver memberdriver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	
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
		
        System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	  }	
	
	
	// 1. 채널 관리 화면 이동
	@Test (priority=1)
	public void channelManageView() throws Exception {
	
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL)) {
			Exception e =  new Exception("not channel management view. current url : " + driver.getCurrentUrl());
	    	throw e;
		}
	}
	
	// 2. 채널 초대 팝업 확인
	@Test (priority=2, dependsOnMethods={"channelManageView"})
	public void channelInvite_popup() throws Exception {
		String failMsg = "";
		
		//click invite button
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//h2[@class='title']")).getText().contentEquals("Invite channel members")) {
			failMsg = "invite popup title error. popup title : " + driver.findElement(By.xpath("//h2[@class='title']")).getText();
		}
		
		//check email inputbox placeholder
		if(!driver.findElement(By.xpath("//input[@id='invite-input-email']")).getAttribute("placeholder").contentEquals("E-mail")) {
			failMsg = failMsg + "\n email input box placeholder : " + driver.findElement(By.xpath("//input[@id='invite-input-email']")).getAttribute("placeholder");
		}
		
		if(driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n Invite button is enabled.(empty email)";
		}
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 채널 초대 팝업에 정상 이메일 추가(2개 이메일 추가-1개 이메일 리스트에서 삭제-남은 1개 이메일(RSUP4) 초대 클릭)
	@Test (priority=3, dependsOnMethods={"channelManageView"})
	public void channelInvite_addemailBasic() throws Exception {
		String failMsg = "";
		
		// click invite button
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);

		// add valid 2 email
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(INVITED_USER);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();

		// check added item
		List<WebElement> invitelist = driver.findElements(By.xpath("//li[@class='invite-list-item']"));

		if (invitelist.size() != 2
				|| !invitelist.get(0).findElement(By.xpath("./span[@class='item-email']")).getText()
						.contentEquals(INVITED_USER)
				|| !invitelist.get(1).findElement(By.xpath("./span[@class='item-email']")).getText()
						.contentEquals(CommonValues.USEREMAIL_JOIN_TRY)) {
			failMsg = "added email [expected] 2item, " + INVITED_USER + "[Actual] " + invitelist.size()
					+ "item ";
			for (int i = 0; i < invitelist.size(); i++) {
				failMsg = failMsg + invitelist.get(i).findElement(By.xpath("./span[@class='item-email']")).getText();
			}
		}

		// delete 1 email
		invitelist.get(1).findElement(By.xpath("./button[1]")).click();
		Thread.sleep(500);

		// check added item(1item)
		invitelist = driver.findElements(By.xpath("//li[@class='invite-list-item']"));

		if (invitelist.size() != 1 || !invitelist.get(0).findElement(By.xpath("./span[@class='item-email']")).getText()
				.contentEquals(INVITED_USER)) {
			failMsg = "added email [expected] 1item, " + INVITED_USER + "[Actual] " + invitelist.size()
					+ "item " + invitelist.get(0).findElement(By.xpath("./span[@class='item-email']")).getText();
		}

		// invite
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();

		Thread.sleep(500);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 4. 채널 초대 팝업에서 잘못된 형식의 이메일 추가시도
	@Test (priority=4, dependsOnMethods={"channelManageView"})
	public void channelInvite_addemail_invalid() throws Exception {
		String failMsg = "";
		
		//click invite button
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);
		
		//add empty 
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		if(!driver.findElement(By.xpath("//p[@class='error-msg']")).getText().contentEquals(CommonValues.CHANNEL_INVITE_EMAIL_ERROR)) {
			failMsg = "1. email error msg(empty value) : " + driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		//add wrong format
		for(int i = 0 ; i < CommonValues.EMAIL_WRONG_CASE.length ; i++) {
			driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
			driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
			driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(CommonValues.EMAIL_WRONG_CASE[i]);
			driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
			Thread.sleep(100);
			if(!driver.findElement(By.xpath("//p[@class='error-msg']")).getText().contentEquals(CommonValues.CHANNEL_INVITE_EMAIL_ERROR)) {
				failMsg = failMsg + "\n 2. email error msg(wrong format) : " + driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
			}
		}
		
		//add master
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(CommonValues.USEREMAIL_PRES);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		if(!driver.findElement(By.xpath("//p[@class='error-msg']")).getText().contentEquals(CommonValues.CHANNEL_INVITE_MASTER_EMAIL_ERROR)) {
			failMsg = failMsg + "\n 3. email error msg(master email) : " + driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		//add invited email
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(INVITED_USER);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//p[@class='error-msg']")).getText()
				.contentEquals(CommonValues.CHANNEL_INVITE_ALREADY_EMAIL_ERROR)) {
			failMsg = failMsg + "\n 4. email error msg(invited email) : "
					+ driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		//add duplicate
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(CommonValues.USEREMAIL_RSUP9);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(CommonValues.USEREMAIL_RSUP9);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//p[@class='error-msg']")).getText()
				.contentEquals(CommonValues.CHANNEL_INVITE_DUPLICATE_EMAIL_ERROR)) {
			failMsg = failMsg + "\n 5. email error msg(duplicate email) : "
					+ driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		// cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 6. 채널 초대 팝업 클립보드 데이터 붙여넣기로 리스트 추가 (10개 붙여넣기, 1개 삭제, 모두삭제로 삭제)
	@Test (priority=6, dependsOnMethods={"channelManageView"})
	public void channelInvite_clipboard() throws Exception {
		String failMsg = "";
		
		// click invite button
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);

		//add clipboard data(vaild 10 email)
		String ctc = CommonValues.CHANNEL_INVITE_CLIPBOARD_EMAIL;
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
		Thread.sleep(100);

		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();

		List<WebElement> invitelist = driver.findElements(By.xpath("//li[@class='invite-list-item']"));

		if (invitelist.size() != 10 ) {
			failMsg = "added email [expected] 10item, " + INVITE_TRY_USER + "[Actual] " + invitelist.size()
					+ "item ";
			for (int i = 0; i < invitelist.size(); i++) {
				failMsg = failMsg + invitelist.get(i).findElement(By.xpath("./span[@class='item-email']")).getText()
						+ " ";
			}
		}
		
		//delete 1 email
		driver.findElement(By.xpath("//li[@class='invite-list-item']/button[1]")).click();
		Thread.sleep(500);
		
		invitelist = driver.findElements(By.xpath("//li[@class='invite-list-item']"));
		//expected 9
		if (invitelist.size() != 9 ) {
			failMsg = "added email [expected] 9item, " + INVITE_TRY_USER + "[Actual] " + invitelist.size()
					+ "item ";
			for (int i = 0; i < invitelist.size(); i++) {
				failMsg = failMsg + invitelist.get(i).findElement(By.xpath("./span[@class='item-email']")).getText()
						+ " ";
			}
		}
	
		// delete All click
		driver.findElement(By.xpath("//*/text()[normalize-space(.)='Delete all']/parent::*")).click();
		Thread.sleep(500);
		
		// check added item(expected : 0)
		if (isElementPresent(By.xpath("//li[@class='invite-list-item']"))) {
			failMsg = "find email after delete all : "
					+ driver.findElements(By.xpath("/il[@class=''invite-list-item]")).size();
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	
	}
	
	// 7. 채널 초대 팝업 클립보드 데이터 잘못된 이메일 폼 추가된 데이터로 붙여넣기 시도
	@Test (priority=7, dependsOnMethods={"channelManageView"})
	public void channelInvite_clipboard_invalid() throws Exception {
		String failMsg = "";
		
		// add clipboard data(with invalid format)
		String ctc = INVITE_TRY_USER + "\n" + "testhello";
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();

		// error msg
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//p[@class='error-msg']")).getText()
				.contentEquals(CommonValues.CHANNEL_INVITE_CLIPBOARD_ERROR)) {
			failMsg = failMsg + "\n email error msg(master email) : "
					+ driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		// add clipboard data(with master)
		ctc = INVITE_TRY_USER + "\n" + CommonValues.USEREMAIL_PRES;
		stringSelection = new StringSelection(ctc);
		clpbrd.setContents(stringSelection, null);
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();

		// error msg
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//p[@class='error-msg']")).getText()
				.contentEquals(CommonValues.CHANNEL_INVITE_CLIPBOARD_ERROR)) {
			failMsg = failMsg + "\n email error msg(master email) : "
					+ driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		
		
		// add clipboard data(with already invited)
		ctc = INVITE_TRY_USER + "\n" + INVITED_USER;
		stringSelection = new StringSelection(ctc);
		clpbrd.setContents(stringSelection, null);
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();

		// error msg
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//p[@class='error-msg']")).getText()
				.contentEquals(CommonValues.CHANNEL_INVITE_CLIPBOARD_ERROR)) {
			failMsg = failMsg + "\n email error msg(master email) : "
					+ driver.findElement(By.xpath("//p[@class='error-msg']")).getText();
		}
		
		// cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 8. 채널 멤버 리스트 확인 (마스터 rsrsup2, 멤버 rsrsup4 : 멤버초대 날짜 확인)
	@Test (priority=8, dependsOnMethods={"channelInvite_addemailBasic"})
	public void channelInvite_checkInvite() throws Exception {
		String failMsg = "";
		
		// check invited member (master1, memeber1, total2)
		List<WebElement> memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText()
				.contentEquals(INVITED_USER)) {
			failMsg = "added 1email [expected] 2item, " + INVITED_USER + "[Actual] " + memberlist.size()
					+ "item " + memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText();
		}
		
		//invite date
		 Date time = new Date();
		 Calendar cal = Calendar.getInstance(); 
		 cal.setTime(time);

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		String inviteDate = format2.format(cal.getTime());
		
		//check invite date
		if (!memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-joinDate']")).getText().contentEquals(inviteDate)) {
			failMsg = failMsg + "\n invite date error [Expected] " + inviteDate + " [Actual] " + memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-joinDate']")).getText();
		}
		
		//check send email button
		try {
			memberlist.get(1).findElement(By.xpath(".//i[@class='ricon-send-email']"));

		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n can not find send email button ";
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	

	//10. rsrsup4로그인
	@Parameters({"browser"})
	@Test(priority = 10, dependsOnMethods={"channelInvite_addemailBasic"})
	public void joinChannel_login(ITestContext context, String browsertype) throws Exception {
		memberdriver = null;
		
		CommonValues comm = new CommonValues();
		memberdriver = comm.setDriver(memberdriver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver2", memberdriver);
		memberdriver.manage().window().maximize();
		
		comm.loginseminar(memberdriver, INVITED_USER);

	}
	
	// 11. 채널 초대(가입)팝업 x 클릭
	@Test(priority = 11, dependsOnMethods={"joinChannel_login"})
	public void joinChannel_join_x() throws Exception {
		String failMsg = "";
		
		// check invite layer popup title
		if(!memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText().contentEquals("Join the invited channel.")) {
			failMsg = "invite layer popup title error : (actual) " + memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText();
		}
		
		//click x button
		memberdriver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		Thread.sleep(500);
	
		//logout
		memberdriver.findElement(By.id("profile-drop-down")).click();
		memberdriver.findElement(By.linkText("Log out")).click();
		
		Thread.sleep(1000);
		
		//login
		CommonValues comm = new CommonValues();
		comm.loginseminar(memberdriver, INVITED_USER);
		// check invite layer popup title
		if(!memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText().contentEquals("Join the invited channel.")) {
			failMsg = "invite layer popup title error : (actual) " + memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText();
		}
		
		//click x button
		memberdriver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		//click GNB logo
		memberdriver.findElement(By.xpath("//div[@class='logo-wrap']/a")).click();
		Thread.sleep(500);

		// check invite layer popup title
		if(!memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText().contentEquals("Join the invited channel.")) {
			failMsg = "invite layer popup title error : (actual) " + memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]")).getText();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 12. 새로 로그인(팝업 새로 뜸 확인), 채널 초대 거절하기
	@Test(priority = 12, dependsOnMethods={"channelInvite_addemailBasic", "joinChannel_join_x"})
	public void joinChannel_join_decline() throws Exception {
		String failMsg = "";
		//login
		//memberlogin();

		try {
			memberdriver.findElement(By.xpath("//div[@class='modal-header']/p[1]"));
		} catch (NoSuchElementException e) {
			memberdriver.findElement(By.xpath("//div[@class='logo-wrap']/a")).click();
			Thread.sleep(500);
		}

		//click decline
		memberdriver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[2]")).click();
		Thread.sleep(1000);
		//check channel list
		memberdriver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(1000);
		
		List<WebElement> channelList = memberdriver.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		//rsrsup4's channel 
		if (channelList.size() != 2) {
			failMsg = "channel count [expected]2 [actual] " + channelList.size();
		}
		
		//logout
		memberdriver.findElement(By.id("profile-drop-down")).click();
		memberdriver.findElement(By.linkText("Log out")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//13. 채널 마스터가 거부한 멤버 상태 확인, 재초대 
	@Test(priority = 13, dependsOnMethods={"joinChannel_join_decline"})
	public void joinChannel_checkMaster_decline() throws Exception {
		String failMsg = "";
		Thread.sleep(1000);
		//refresh master view
		driver.navigate().refresh();
		
		Thread.sleep(3000);
		// check invited member (master1, memeber1, total2)
		List<WebElement> memberlist = driver
				.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals(INVITED_USER)) {
			failMsg = "1. added 1email [expected] 2item, " + INVITED_USER + "[Actual] "
					+ memberlist.size() + "item "
					+ memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText();
		}
				
		// invite date
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		String inviteDate = format2.format(cal.getTime());

		// check invite date
		if (!memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-joinDate']")).getText()
				.contentEquals(inviteDate)) {
			failMsg = failMsg + "\n 2. invite date error [Expected] " + inviteDate + " [Actual] "
					+ memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-joinDate']")).getText();
		}
		
		// check member state. expected : Decline
		if (!memberlist.get(1).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText().contentEquals("Decline")) {
			failMsg = failMsg + "\n 3. invited member state [Expected] Decline" + " [Actual] "
					+ memberlist.get(1).findElement(By.xpath(".//div[@class='invite-status deny']/span[1]")).getText();
		}

		// check send email button
		try {
			memberlist.get(1).findElement(By.xpath(".//i[@class='ricon-send-email']"));

		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 4. can not find send email button ";
		}

		// click send email button
		memberlist.get(1).findElement(By.xpath(".//i[@class='ricon-send-email']")).click();
		Thread.sleep(100);
		// click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']/i[1]")).click();
		Thread.sleep(500);
		// check member state. expected : Decline
		if (!memberlist.get(1).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText()
				.contentEquals("Decline")) {
			failMsg = failMsg + "\n 5. invited member state [Expected] Decline" + " [Actual] "
					+ memberlist.get(1).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}

		// click send email button
		memberlist.get(1).findElement(By.xpath(".//i[@class='ricon-send-email']")).click();
		Thread.sleep(1000);
		// click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(1000);
		memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		Thread.sleep(500);
		// check member state. expected : Waiting
		if (!memberlist.get(1).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText()
				.contentEquals("Waiting")) {
			failMsg = failMsg + "\n 6. invited member state [Expected] Waiting" + " [Actual] "
					+ memberlist.get(1).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}
		
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 14. 재 초대한 멤버 초대 수락하기
	@Test(priority = 14, dependsOnMethods={"joinChannel_checkMaster_decline"})
	public void joinChannel_join_joinchannel() throws Exception {
		
		String failMsg = "";
		//login
		CommonValues comm = new CommonValues();
		comm.loginseminar(memberdriver, INVITED_USER);
		
		//click join
		memberdriver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[1]")).click();
		
		//check channel list
		memberdriver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(1000);
		
		List<WebElement> channelList = memberdriver.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		if (channelList.size() != 3) {
			failMsg = "channel count [expected] 3 [actual] " + channelList.size();
			
		}
		System.out.println("%%%% channelsize" + channelList.size());
		String[] userinfo = CommonValues.USEREMAIL_PRES.split("@");
		
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']/strong")).getText().contentEquals(userinfo[0])) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			} 
		}
		
		//check channel master profile
		if(!memberdriver.findElement(By.xpath("//div[@class='manager-info-wrap']/span[2]")).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "\n channel master email error [expected] " + CommonValues.USEREMAIL_PRES + " [actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='manager-profile']/span[@class='email']"))
							.getText();
		}
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	// 16. 가입한 멤버 마스터가 채널에서 삭제
	@Test(priority = 16, dependsOnMethods={"joinChannel_join_joinchannel"})
	public void joinChannel_deleteMember() throws Exception {
		String failMsg = "";
		
		//refresh master view
		driver.navigate().refresh();
		Thread.sleep(500);
		int memberindex = 0;
		// check invited member (master1, memeber1, total2)
		List<WebElement> memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() == 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals(INVITED_USER)) {
			
			if(memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(INVITED_USER)) {
				memberindex = 0;
			} else if(memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(INVITED_USER)) {
				memberindex = 1;
			} else {
				Exception e = new Exception("cannot find added member in list ");
				throw e;
			}
			
		} else {
			failMsg = "added 1email [expected] 2item, " + "[Actual] "
					+ memberlist.size() + "item ";
		}


		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/button[1]")).click();
		Thread.sleep(500);
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(500);
		
		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/button[1]")).click();
		Thread.sleep(500);

		// click ok
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);

		// check invited member (master1, total1)
		memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 1) {
			failMsg = "added 1email [expected] 1item, " + INVITED_USER + "[Actual] "
					+ memberlist.size() + "item ";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	// 20. 삭제한 멤버 재 초대
	@Test (priority=20)
	public void channelInvite_invite2() throws Exception {
		String failMsg = "";
		
		// refresh master view
		driver.navigate().refresh();
		Thread.sleep(500);

		// click invite button
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);

		// add email already invite
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		driver.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(INVITED_USER);
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);

		// click Invite
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(100);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. 멤버 채널 가입(재가입)
	@Test(priority = 21, dependsOnMethods={"channelInvite_invite2"})
	public void joinChannel_join_joinchannel2() throws Exception {
		String failMsg = "";
		//login
		CommonValues comm = new CommonValues();
		comm.loginseminar(memberdriver, INVITED_USER);
		
		//click decline
		memberdriver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[1]")).click();
		
		//check channel list
		memberdriver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(1000);
		
		List<WebElement> channelList = memberdriver.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		if (channelList.size() != 3) {
			failMsg = "channel count [expected] 3 [actual] " + channelList.size();
		}
		
		//click rsrsup2 channel 
		String[] userinfo = CommonValues.USEREMAIL_PRES.split("@");
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(userinfo[0])) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}		
		}
		
		//check channel master profile
		if(!memberdriver.findElement(By.xpath("//div[@class='manager-info-wrap']/span[2]")).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "\n channel master email error [expected] " + CommonValues.USEREMAIL_PRES + " [actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='manager-profile']/span[@class='email']"))
							.getText();
		}
		
		//logout
		//memberdriver.findElement(By.id("profile-drop-down")).click();
		//memberdriver.findElement(By.linkText("Log out")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 멤버가 채널에서 직접 탈퇴
	@Test(priority = 22, dependsOnMethods={"joinChannel_join_joinchannel2"})
	public void joinChannel_withdrawal() throws Exception {
		String failMsg = "";
		
		//click Leave button
		memberdriver.findElement(By.xpath("//button[@class='btn btn-withdrawal']")).click();
		Thread.sleep(500);
		
		// click cancel
		memberdriver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		
		// click Leave button
		memberdriver.findElement(By.xpath("//button[@class='btn btn-withdrawal']")).click();
		Thread.sleep(500);
		
		// click confirm
		memberdriver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		memberdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		Thread.sleep(2000);
		if (!memberdriver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText().contentEquals("You have unsubscribed to the channel.")) {
			failMsg = "1. Channel Leave toast message error : [Actual] " + driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText();
		}
		
		//check url. expect channel list view
		if (!memberdriver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/channel/list")) {
			failMsg = failMsg + "\n 2. not channel list view : [Actual] " + memberdriver.getCurrentUrl();
		}
		
		//check channel list (channel 2)
		List<WebElement> channelList = memberdriver.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		if(channelList.size() != 2) {
			failMsg = failMsg + "\n 3. channel list count is not 2 [Actual] " + channelList.size();
		}
		
		
		// master : check member list
		// check invited member (master1, total)
		driver.navigate().refresh();
		Thread.sleep(1000);
		List<WebElement> memberlist = driver
				.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		memberlist = driver
				.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 1) {
			failMsg = "added 1email [expected] 1item, " + INVITED_USER + "[Actual] "
					+ memberlist.size() + "item ";
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 31. 준비 : rsrsup2, rsrsup8, rsrsup6 순서로 rsrsup4를 멤버로 초대한다.
	@Test(priority = 31)
	public void invite_multi() throws Exception {
	
		//login rsrsup2
		CommonValues comm = new CommonValues();
		//comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
		
		inviteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
		
		//login rsrsup8
		comm.loginseminar(driver, "rsrsup8@gmail.com");
		
		inviteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(1000);

		//login rsrsup6
		comm.loginseminar(driver, "rsrsup6@gmail.com");
		
		inviteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(1000);
		
	}	
	
	public void inviteMember(WebDriver wd, String inviteMem) throws InterruptedException {
		
		//channel info
		wd.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		wd.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		wd.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		
		
		// click invite button
		wd.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		Thread.sleep(500);

		// invite member
		wd.findElement(By.xpath("//input[@id='invite-input-email']")).click();
		wd.findElement(By.xpath("//input[@id='invite-input-email']")).clear();
		wd.findElement(By.xpath("//input[@id='invite-input-email']")).sendKeys(inviteMem);
		wd.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(100);
		
		// invite
		wd.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(100);
		
	}
	
	// 32. 멤버 초대 팝업 확인. 순서대로 뜨는지 확인
	@Test(priority = 32, dependsOnMethods={"invite_multi"})
	public void member_multipopup() throws Exception {
		String failMsg = "";
		
		if(!memberdriver.getCurrentUrl().contains(CommonValues.SERVER_URL + "/login")) {
			memberdriver.get(CommonValues.SERVER_URL + "/logout");
			Thread.sleep(500);
			CommonValues comm = new CommonValues();
			comm.loginseminar(memberdriver, INVITED_USER);
			Thread.sleep(500);
		}
		
		String msg1 = String.format(INVITE_MSG, "rsrsup2");
		// check invite layer popup msg
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg1)) {
			failMsg = "1. 1st invite layer popup msg error [Expected]" + msg1 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}
		// click x button
		memberdriver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();			
		Thread.sleep(500);
		
		String msg2 = String.format(INVITE_MSG, "rsrsup8");
		// check invite layer popup msg
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg2)) {
			failMsg = failMsg + "\n 2. 2nd invite layer popup msg error [Expected]" + msg2 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}
		// click join button
		memberdriver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
		// check invite layer popup msg
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg1)) {
			failMsg = failMsg + "\n 3. 1st invite layer popup msg error [Expected]" + msg1 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}		
		//refresh
		memberdriver.navigate().refresh();
		Thread.sleep(500);
		// check invite layer popup msg
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg1)) {
			failMsg = failMsg + "\n 4. 1st invite layer popup msg error [Expected]" + msg1 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}		
		//decline
		memberdriver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[2]")).click();
		Thread.sleep(500);
		
		// check invite layer popup msg
		String msg3 = String.format(INVITE_MSG, "rsrsup6");
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg3)) {
			failMsg = failMsg + "\n 5. 3rd invite layer popup msg error [Expected]" + msg3 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}
		// click x button
		memberdriver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		Thread.sleep(500);
		
		//click GNB channel
		memberdriver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		List<WebElement> channels = memberdriver.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		if(channels.size() != 3) {
			failMsg = failMsg + "\n 6. channel size error [Expected] 3 [Actual]" + channels.size() ;
		} else {
			if(!channels.get(2).findElement(By.xpath(".//div[@class='content_title']/strong")).getText().contentEquals("rsrsup8")) {
				
			}
		}
		Thread.sleep(500);
		// click GNB logo
		memberdriver.findElement(By.xpath("//div[@class='logo-wrap']/a")).click();
		Thread.sleep(500);
		
		if (!memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText().contentEquals(msg3)) {
			failMsg = failMsg + "\n 7. 3rd invite layer popup msg error [Expected]" + msg3 + " [Actual]"
					+ memberdriver.findElement(By.xpath("//div[@class='modal-body']/p")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 33. 마스터가 거절한 멤버 삭제 rsrsup2
	@Test(priority = 33, dependsOnMethods={"invite_multi"})
	public void delete_declineMember() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(500);
		
		//login rsrsup2
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
		Thread.sleep(1000);
		
		//channel info
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		
		int memberindex = 0;
		String member = INVITED_USER;
		List<WebElement> memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() == 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals(INVITED_USER)) {
			
			if(memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 0;
			} else if(memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 1;
			} else {
				Exception e = new Exception("cannot find added member in list ");
				throw e;
			}
			
		}	
		
		// check member state. expected : Decline
		if (!memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText().contentEquals("Decline")) {
			failMsg = failMsg + "\n 1. invited member state [Expected] Decline" + " [Actual] "
					+ memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}
		
		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		//check popup msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(DELETE_INVITE_MEMBER)) {
			failMsg = failMsg + "\n 2. invite member delete popup msg [Expected]" +  DELETE_INVITE_MEMBER  + " [Actual] "
					+ driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(100);
		
		// check member state. expected : Decline
		if (!memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText().contentEquals("Decline")) {
			failMsg = failMsg + "\n 3. invited member state [Expected] Decline" + " [Actual] "
					+ memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}		

		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);	
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(100);

		memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 1 || !memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals("rsrsup2@gmail.com")) {
			
			failMsg = failMsg + "\n 4. channel member count error [Expected] 1" + " [Actual] "+ memberlist.size();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 34. 마스터가 대기중인 멤버 상태 확인 후 메일전송, 멤버 삭제 rsrsup6
	@Test(priority = 34, dependsOnMethods={"invite_multi", "member_multipopup"})
	public void delete_waitMember() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(500);
		
		//login rsrsup6
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, "rsrsup6@gmail.com");
		Thread.sleep(1000);
		
		//channel info
		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		
		int memberindex = 0;
		String member = INVITED_USER;
		List<WebElement> memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() == 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals(INVITED_USER)) {
			
			if(memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 0;
			} else if(memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 1;
			} else {
				Exception e = new Exception("cannot find added member in list ");
				throw e;
			}
			
		}	
		
		// check member state. expected : Waiting
		if (!memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText().contentEquals("Waiting")) {
			failMsg = failMsg + "\n 1. invited member state [Expected] Waiting" + " [Actual] "
					+ memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}
		
		//click re-send
		memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-send-email']")).click();
		Thread.sleep(500);
		
		// check popup msg
		if (!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(INVITE_RESEND_EMAIL)) {
			failMsg = failMsg + "\n 2. invite member resend email popup msg [Expected]" + INVITE_RESEND_EMAIL + " [Actual] "
					+ driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(1000);
		
		memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() == 2 || !memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals(INVITED_USER)) {
			
			if(memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 0;
			} else if(memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 1;
			} else {
				Exception e = new Exception("cannot find added member in list ");
				throw e;
			}
			
		}			
	
		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		//check popup msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(DELETE_INVITE_MEMBER)) {
			failMsg = failMsg + "\n 3. invite member delete popup msg [Expected]" +  DELETE_INVITE_MEMBER  + " [Actual] "
					+ driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(100);
		
		// check member state. expected : Decline
		if (!memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText().contentEquals("Waiting")) {
			failMsg = failMsg + "\n 4. invited member state [Expected] Waiting" + " [Actual] "
					+ memberlist.get(memberindex).findElement(By.xpath(".//div[@class='item-btn-box']/span[1]")).getText();
		}		

		//click delete button
		memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);	
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(100);

		memberlist = driver.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() != 1 || !memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']"))
				.getText().contentEquals("rsrsup6@gmail.com")) {
			
			failMsg = failMsg + "\n 5. channel member count error [Expected] 1" + " [Actual] "+ memberlist.size();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	// 39. 정리 : rsrsup2, rsrsup8, rsrsup6가 rsrsup4를 삭제
	@Test(priority = 39, dependsOnMethods = {"delete_waitMember"}, alwaysRun = true, enabled = true)
	public void delete_multi() throws Exception {
	
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(500);
				
		CommonValues comm = new CommonValues();
		
		//login rsrsup2
		comm.loginseminar(driver, "rsrsup2@gmail.com");
		Thread.sleep(1000);
		deleteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);

		//login rsrsup8
		comm.loginseminar(driver, "rsrsup8@gmail.com");
		Thread.sleep(1000);
		deleteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");		
		Thread.sleep(1000);

		//login rsrsup6
		comm.loginseminar(driver, "rsrsup6@gmail.com");
		Thread.sleep(1000);
		deleteMember(driver, INVITED_USER);
		Thread.sleep(1000);
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(1000);
	}	
	
	public void deleteMember(WebDriver wd, String member) throws Exception {
		
		//channel info
		wd.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		wd.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(1000);
		wd.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		
		int memberindex = 0;
		List<WebElement> memberlist = wd.findElements(By.xpath("//li[@class='ChannelMemberListItem_channel-member-item-box__EvrY0']"));
		if (memberlist.size() == 2 ) {
			
			if(memberlist.get(0).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 0;
			} else if(memberlist.get(1).findElement(By.xpath(".//span[@class='user-info-email']")).getText().contentEquals(member)) {
				memberindex = 1;
			} else {
				Exception e = new Exception("cannot find added member in list ");
				throw e;
			}
			
			//click delete button
			memberlist.get(memberindex).findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			// click ok
			wd.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		} 		
	}	
	
	public void clearAttributeValue(WebElement el) 
	{
		while(!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}
	  
	  @AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {

	    driver.quit();
	    memberdriver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
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
