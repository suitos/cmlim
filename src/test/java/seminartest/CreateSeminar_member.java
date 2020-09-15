package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* CreateSeminar2
 * user : rsrsup2
 * Part1
 * 1. 채널 멤버 없는 채널의 세미나 멤버 팝업 확인 (rsrsup2의 rsrsup2 채널) - 발표자, 운영자 모두 확인
 * 2. 채널 멤버 있는 채널(rsrsup1)의 채널 팝업 멤버 확인 - 발표자, 운영자 모두 확인
 * 3. 멤버설정 - default 발표자:본인, 운영자 없음
 * 4. 발표자 선택팝업 검색1 - 일치하지 않은 검색어
 * 5. 발표자 선택팝업 검색2 - 닉네임 검색
 * 6. 발표자 선택팝업 검색3 - 이메일 검색
 * 7. 발표자 선택팝업 - 4명 초과 선택 시도
 * 8. 발표자 1명 선택완료 - 총 발표자 2명임을 확인
 * 9. 발표자 1명 삭제 - 남은 발표자 삭제 시도(삭제 안됨 확인)
 * 
 * Part2
 * 11. 운영자 선택팝업 - 멤버 확인 (발표자 선택 멤버 제외)
 * 12. 운영자 선택팝업 검색1 - 일치하지 않은 검색어
 * 13. 운영자 선택팝업 검색2 - 닉네임 검색
 * 14. 운영자 선택팝업 검색3 - 이메일 검색
 * 15. 운영자 선택 - 6명 초과 선택 시도
 * 16. 운영자 선택 1명 선택완료 - 총 운영자 1명 확인
 * 17. 운영자 1명 삭제 - 운영자 1명 추가 
 * 
 * part3
 * 21. 발표자 프로필 확인 : 기존 프로필 비어 있는 유저
 * 22. 발표자 프로필 확인 : 기존 프로필 채워져 있는 유저
 * 23. 발표자 프로필 지우고 빈값으로 저장  
 * 24. 발표자 프로필 : 입력 확인 (허용 포맷)
 * 25. 발표자 프로필 : 최대 길이 확인
 * 26. 발표자 프로필 : 정상 입력 케이스
 * 
 * part4
 * 31. 발표자 프로필 설정된 임시저장 세미나 상세화면 확인
 * 32. 발표자 프로필 설정된 게시완료 세미나 상세 화면 확인
 * 
 * 100. 세미나 삭제
 */

public class CreateSeminar_member {

	public static String MEMBER_SELECTED_PRESENTER = "4 presenters | %d people selected.";
	public static String MEMBER_SELECTED_ORGANIZER = "6 organizers | %d people selected.";
	public int channelMembers = CommonValues.CHANNEL_MEMBERS.length;
	
	public static String XPATH_CREATEVIEW_TAB_MEMBER = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	public String seminarTitle = "";
	public String totleMember = "Total (%d) members";
	
	public String mem_pres1 = "";
	public String mem_pres2 = "";
	public String mem_org1 = "";
	
	public String company = "member company";
	public String position = "member position";
	public String description = "member desc";

	@Parameters({ "browser" })
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

	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	}

	// 1. 채널 멤버 없는 채널의 세미나 멤버 팝업 확인 (rsrsup2의 rsrsup2 채널)
	@Test(priority = 1, enabled = true)
	public void seminarmember_empty() throws Exception {
		String failMsg = "";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			failMsg = "1. not create view" + driver.getCurrentUrl();
			driver.get(createViewUri);
		}
		
		//channel : rsrsup2
		if(!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText().contentEquals("rsrsup2")) {
			//click channel select
			driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
			Thread.sleep(500);
			
			String channelname = "//span[contains(text(), ' " + "rsrsup2" + "')]";
			driver.findElement(By.xpath(channelname)).click();
			//click confirm
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		//click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		Thread.sleep(500);
		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']//span")).getText().contentEquals(CommonValues.MSG_EMPTY_MEMBER_POPUP)) {
			failMsg = failMsg + "\n 2. worng msg(member setting popup) [Expected]" +  CommonValues.MSG_EMPTY_MEMBER_POPUP
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']//span")).getText();
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		
		//organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B + "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);

		if (!driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']//span")).getText()
				.contentEquals(CommonValues.MSG_EMPTY_MEMBER_POPUP)) {
			failMsg = failMsg + "\n 3. worng msg(member setting popup) [Expected]" +  CommonValues.MSG_EMPTY_MEMBER_POPUP
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']//span")).getText();
		}

		// click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
			
	}

	
	// 2. 채널 멤버 있는 채널(rsrsup1)의 채널 팝업 멤버 확인
	@Test(priority = 2, enabled = true)
	public void seminarmember_channelMember() throws Exception {
		String failMsg = "";

		//tab1
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[1]")).click();
		Thread.sleep(500);
		
		//change channel : rsrsup1
		if(!driver.findElement(By.xpath("//span[@class='selected-channel']")).getText().contentEquals("rsrsup1")) {
			//click channel select
			driver.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
			Thread.sleep(500);
			
			String channelname = "//span[contains(text(), '" + "rsrsup1" + "')]";
			driver.findElement(By.xpath(channelname)).click();
			//click confirm
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		}
		
		//click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		Thread.sleep(500);
		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, 8))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, 8)
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != 8) {
			failMsg = failMsg + "\n 2. channel member list (member setting popup) [Expected]" +  8
					+ " [Actual]"+ members.size();
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		
		//organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B + "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);

		//total member
		if (!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, channelMembers))) {
			failMsg = failMsg + "\n 3. channel member count (member setting popup) [Expected]"
					+ String.format(totleMember, channelMembers) + " [Actual]" + driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if (members.size() != channelMembers) {
			failMsg = failMsg + "\n 4. channel member list (member setting popup) [Expected]" + channelMembers + " [Actual]"+ members.size();
		}

		// click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 3. 멤버설정 - default 발표자:본인, 운영자 없음
	@Test(priority = 3, enabled = true)
	public void seminarmember_pres_defalut() throws Exception {
		String failMsg = "";

		//defalut presenter
		List<WebElement> member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if (member_pres.size() != 1
				|| !member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "1. Defalut Presenter error [Expected]" + CommonValues.USEREMAIL_PRES + " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}
			
		
		// 운영자확인
		List<WebElement> organizers = driver.findElements(By.xpath("//div[@class='MemberSetting_member-item__OPERATOR__2Y0RN']"));
		
		if(organizers.size() != 0 ) {
			failMsg = failMsg + "\n 2. Defalut Organiaer Error [Expected]" +  0
					+ " [Actual]" + organizers.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 4. 발표자 선택팝업 검색1 - 일치하지 않은 검색어
	@Test(priority = 4, enabled = true)
	public void seminarmember_pres_search1() throws Exception {
		String failMsg = "";

		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, channelMembers))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, channelMembers)
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("invalid keyword");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']")).getText()
				.contentEquals(CommonValues.MSG_NORESULT_MEMBER_POPUP)) {
			failMsg = failMsg + "\n 2. worng msg(member setting popup, no result)" + driver
					.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']")).getText();
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 5. 발표자 선택팝업 검색2 - 닉네임 검색
	@Test(priority = 5, enabled = true)
	public void seminarmember_pres_search2() throws Exception {
		String failMsg = "";

		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, channelMembers))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, channelMembers)
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		String nickname = "NickName";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(nickname);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != 1) {
			failMsg = failMsg + "\n 2. searched member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ members.size();
		} else {
			if(!members.get(0).findElement(By.xpath("./span[@class='member-name']")).getText().contains(nickname)) {
				failMsg = failMsg + "\n 3. searched member Nickname (member setting popup) [Expected]" +  nickname
						+ " [Actual]"+ members.get(0).findElement(By.xpath("./span[@class='member-name']")).getText();
			}
			
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 6. 발표자 선택팝업 검색3 - 이메일 검색
	@Test(priority = 6, enabled = true)
	public void seminarmember_pres_search3() throws Exception {
		String failMsg = "";

		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, channelMembers))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, channelMembers)
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		String email = "rsrsup3";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != 1) {
			failMsg = failMsg + "\n 2. searched member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ members.size();
		} else {
			if(!members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText().contains(email)) {
				failMsg = failMsg + "\n 3. searched member Nickname (member setting popup) [Expected]" +  email
						+ " [Actual]"+ members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText();
			}
			
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 7. 발표자 선택팝업 - 4명 초과 선택 시도
	@Test(priority = 7, enabled = true)
	public void seminarmember_pres_max() throws Exception {
		String failMsg = "";

		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, channelMembers))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, channelMembers)
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "2. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		//selected member
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 1))) {
			failMsg = failMsg + "\n 3. selected member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//select 3 (defalut 1 + 3)
		members.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(1).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(2).findElement(By.xpath("./span[@class='member-name']")).click();
		
		//selected member (4)
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 4 ))) {
			failMsg = failMsg + "\n 4. selected member count (member setting popup) [Expected]" +  "4"
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		// over click
		members.get(3).findElement(By.xpath("./span[@class='member-name']")).click();
		
		// selected member (4)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 4))) {
			failMsg = failMsg + "\n 5. selected member count (member setting popup) [Expected]" + "4" + " [Actual]"
					+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//release selected member
		members.get(2).findElement(By.xpath("./span[@class='member-name']")).click();
		
		// selected member (3)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 3))) {
			failMsg = failMsg + "\n 5. selected member count (member setting popup) [Expected]" + "3" + " [Actual]"
					+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//add 1
		members.get(3).findElement(By.xpath("./span[@class='member-name']")).click();

		// selected member (4)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 4))) {
			failMsg = failMsg + "\n 5. selected member count (member setting popup) [Expected]" + "4" + " [Actual]"
					+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			

	// 8. 발표자 1명 선택완료 - 총 발표자 2명임을 확인
	@Test(priority = 8, enabled = true)
	public void seminarmember_pres_add1() throws Exception {
		String failMsg = "";

		//defalut presenter
		List<WebElement> member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if(member_pres.size() != 1 || !member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "1. Defalut Presenter error [Expected]" +  CommonValues.USEREMAIL_PRES
			+ " [Actual]"+ member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}
		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		

		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "2. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		//selected member
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 1))) {
			failMsg = failMsg + "\n 3. selected member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//select 1 (defalut 1 + 1)
		members.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		String addedPres = members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText();
		
		//selected member (2)
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_PRESENTER, 2))) {
			failMsg = failMsg + "\n 4. selected member count (member setting popup) [Expected]" +  "2"
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
		
		
		//presenter check
		member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if (member_pres.size() != 2 || !member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText().contentEquals(addedPres)) {
			failMsg = failMsg + "5. Added Presenter error [Expected]" + addedPres + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 9. 발표자 1명 삭제 - 남은 발표자 삭제 시도(삭제 안됨 확인)
	@Test(priority = 9, enabled = true)
	public void seminarmember_pres_delete1() throws Exception {
		String failMsg = "";

		// added presenter
		List<WebElement> member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if (member_pres.size() != 2
				|| !member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "1. Added Presenter error [Expected]" + CommonValues.USEREMAIL_PRES + " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
							.getText();
		}

		// delete 1 (default user)
		String presEmail = member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if (member_pres.size() != 1
				|| !member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(presEmail)) {
			failMsg = failMsg + "\n 2. Added Presenter error [Expected]" + presEmail + " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
							.getText();
		}
		
		// try delete 
		if(isElementPresent(By.xpath("//div[@role='presentation']//i[@class='ricon-close']"))) {
			failMsg = failMsg + "\n 3. remaining presenter menu has an x button ";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 9. 발표자 1명 추가 - 프로필 확인(기본 빈 프로필, 채워져있는 프로필) 
	@Test(priority = 9, enabled = true)
	public void seminarmember_pres_profile() throws Exception {
		String failMsg = "";
		
		mem_pres1 = driver.findElement(By.xpath("//div[@role='presentation']//span[@class='member-item__user-info__email']")).getText();
		
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);


		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != channelMembers) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  channelMembers
					+ " [Actual]"+ members.size();
		}
		
		String member2 = "rsrsup1@gmail.com";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(member2);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		//select rsrsup1
		driver.findElement(By.xpath("//li[@role='presentation']/span[@class='member-email']")).click();
		Thread.sleep(500);

		// click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
		
		//added member 
		List<WebElement> member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		if (member_pres.size() != 2
				|| !member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(member2)) {
			failMsg = failMsg + "\n2. Added Presenter error [Expected]" + member2 + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
							.getText();
		} else {
			//member2 
			mem_pres2 = member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 11. 운영자 선택팝업 - 멤버 확인 (발표자 선택 멤버 제외)
	@Test(priority = 11, enabled = true)
	public void seminarmember_org() throws Exception {
		String failMsg = "";

		//organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B + "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);


		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != (channelMembers-1)) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  (channelMembers-1)
					+ " [Actual]"+ members.size();
		}
		
		//selected member
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 0))) {
			failMsg = failMsg + "\n 2. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 0)
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		Thread.sleep(500);
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}				

	// 12. 운영자 선택팝업 검색1 - 일치하지 않은 검색어
	@Test(priority = 12, enabled = true)
	public void seminarmember_org_search1() throws Exception {
		String failMsg = "";

		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);

		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if (members.size() != (channelMembers - 1)) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" + (channelMembers - 1)
					+ " [Actual]" + members.size();
		}

		// selected member
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 0))) {
			failMsg = failMsg + "\n 2. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 0) 
			+ " [Actual]" + driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}

		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("invalid keyword");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']")).getText()
				.contentEquals(CommonValues.MSG_NORESULT_MEMBER_POPUP)) {
			failMsg = failMsg + "\n 3. worng msg(member setting popup, no result)" + driver
					.findElement(By.xpath("//div[@class='MemberSettingModal_empty-list__3S2wE']")).getText();
		}
		
		// click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 13. 운영자 선택팝업 검색2 - 닉네임 검색
	@Test(priority = 13, enabled = true)
	public void seminarmember_org_search2() throws Exception {
		String failMsg = "";

		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != (channelMembers - 1)) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  (channelMembers - 1)
					+ " [Actual]"+ members.size();
		}
		
		String nickname = "rsrsup11";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(nickname);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != 1) {
			failMsg = failMsg + "\n 2. searched member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ members.size();
		} else {
			if(!members.get(0).findElement(By.xpath("./span[@class='member-name']")).getText().contains(nickname)) {
				failMsg = failMsg + "\n 3. searched member Nickname (member setting popup) [Expected]" +  nickname
						+ " [Actual]"+ members.get(0).findElement(By.xpath("./span[@class='member-name']")).getText();
			}
			
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 14. 운영자 선택팝업 검색3 - 이메일 검색
	@Test(priority = 14, enabled = true)
	public void seminarmember_org_search3() throws Exception {
		String failMsg = "";

		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != (channelMembers - 1)) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  (channelMembers - 1)
					+ " [Actual]"+ members.size();
		}
		
		String email = "rsrsup7@";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != 1) {
			failMsg = failMsg + "\n 2. searched member count (member setting popup) [Expected]" +  "1"
					+ " [Actual]"+ members.size();
		} else {
			if(!members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText().contains(email)) {
				failMsg = failMsg + "\n 3. searched member Email (member setting popup) [Expected]" +  email
						+ " [Actual]"+ members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText();
			}
			
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			

	// 15. 운영자 선택 - 6명 초과 선택 시도
	@Test(priority = 15, enabled = true)
	public void seminarmember_org_max() throws Exception {
		String failMsg = "";

		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		//total member
		if(!driver.findElement(By.xpath("//span[@class='user-all']")).getText().contentEquals(String.format(totleMember, (channelMembers-1) ))) {
			failMsg = failMsg + "1. channel member count (member setting popup) [Expected]" +  String.format(totleMember, (channelMembers-1) )
					+ " [Actual]"+ driver.findElement(By.xpath("//span[@class='user-all']")).getText();
		}
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != (channelMembers-1) ) {
			failMsg = failMsg + "\n 2. channel member list (member setting popup) [Expected]" +  (channelMembers-1)
					+ " [Actual]"+ members.size();
		}
		
		//selected member 0
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 0))) {
			failMsg = failMsg + "\n 3. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 0)
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//select 6 (defalut 0 + 6)
		members.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(1).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(2).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(3).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(4).findElement(By.xpath("./span[@class='member-name']")).click();
		members.get(5).findElement(By.xpath("./span[@class='member-name']")).click();
		
		//selected member (6)
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 6 ))) {
			failMsg = failMsg + "\n 4. selected member count (member setting popup) [Expected]" +  String.format(MEMBER_SELECTED_ORGANIZER, 6 )
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		// over click
		members.get(6).findElement(By.xpath("./span[@class='member-name']")).click();
		
		// selected member (4)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 6))) {
			failMsg = failMsg + "\n 5. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 6 ) 
			+ " [Actual]" + driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//release selected member
		members.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		
		// selected member (3)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 5))) {
			failMsg = failMsg + "\n 6. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 5) 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//add 1
		members.get(6).findElement(By.xpath("./span[@class='member-name']")).click();

		// selected member (6)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 6))) {
			failMsg = failMsg + "\n 7. selected member count (member setting popup) [Expected]" + String.format(MEMBER_SELECTED_ORGANIZER, 6) 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//click x
		driver.findElement(By.xpath("//button[@class='backdrop__close-btn']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			
	
	// 16. 운영자 선택 1명 선택완료 - 총 운영자 1명 확인
	@Test(priority = 16, enabled = true)
	public void seminarmember_org_add1() throws Exception {
		String failMsg = "";

		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		//total member
		List<WebElement> members = driver.findElements(By.xpath("//li[@role='presentation']"));
		if(members.size() != (channelMembers-1) ) {
			failMsg = failMsg + "1. channel member list (member setting popup) [Expected]" +  (channelMembers-1)
					+ " [Actual]"+ members.size();
		}
		
		//selected member 0
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 0))) {
			failMsg = failMsg + "\n 2. selected member count (member setting popup) [Expected]" +  0
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
		
		//select 1 (defalut 0 + 1)
		members.get(0).findElement(By.xpath("./span[@class='member-name']")).click();
		String addedMem = members.get(0).findElement(By.xpath("./span[@class='member-email']")).getText();
		
		//selected member (1)
		if(!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 1 ))) {
			failMsg = failMsg + "\n 3. selected member count (member setting popup) [Expected]" +  1
					+ " [Actual]"+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}
	
		//click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
		
		List<WebElement> organizers = driver.findElements(By.xpath("//div[@class='MemberSetting_member-item__OPERATOR__2Y0RN']"));
		
		if(organizers.size() != 1 
				|| !organizers.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText().contentEquals(addedMem)) {
			failMsg = failMsg + "\n 4. Added Organiaer Error [Expected]" +  addedMem
					+ " [Actual]"+ organizers.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 17. 운영자 1명 삭제 - 운영자 1명 추가 
	@Test(priority = 17, enabled = true)
	public void seminarmember_org_del1() throws Exception {
		String failMsg = "";

		List<WebElement> organizers = driver.findElements(By.xpath("//div[@class='MemberSetting_member-item__OPERATOR__2Y0RN']"));
		
		if(organizers.size() != 1 ) {
			failMsg = failMsg + "1. Added Organiaer Error [Expected]" + 1 + " [Actual]"+ organizers.size();
		}
		
		//delete 1
		organizers.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath("//div[@class='MemberSetting_member-item__OPERATOR__2Y0RN']"))) {
			failMsg = failMsg + "\n 2. find organizer after delete all";
		}
		
		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B
				+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);

		String email = "rsrsup6@";
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		// select 1 (defalut 0 + 1)
		driver.findElement(By.xpath("//li[@role='presentation']//span[@class='member-name']")).click();
		String addedMem = driver.findElement(By.xpath("//li[@role='presentation']//span[@class='member-email']")).getText();

		// selected member (1)
		if (!driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText()
				.contentEquals(String.format(MEMBER_SELECTED_ORGANIZER, 1))) {
			failMsg = failMsg + "\n 3. selected member count (member setting popup) [Expected]" + 1 + " [Actual]"
					+ driver.findElement(By.xpath("//div[@class='user']/span[@class='user-count']")).getText();
		}

		// click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);

		organizers = driver.findElements(By.xpath("//div[@class='MemberSetting_member-item__OPERATOR__2Y0RN']"));

		if (organizers.size() != 1
				|| !organizers.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText()
						.contentEquals(addedMem)) {
			failMsg = failMsg + "\n 4. Added Organiaer Error [Expected]" + addedMem + " [Actual]" + organizers.get(0)
					.findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		} else {
			//organizer1
			mem_org1 = organizers.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']")).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 21. 발표자 프로필 확인 : 기존 프로필 비어 있는 유저
	@Test(priority = 21, enabled = true)
	public void seminarmember_profile_user1() throws Exception {
		String failMsg = "";
		
		//check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		if (member_pres.size() != 2
				|| !member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(mem_pres1)) {
			failMsg = failMsg + "1. Added Presenter error [Expected]" + mem_pres1 + " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
							.getText();
		} 
		
		// open profile
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);
		
		//company name
		if(!member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").isEmpty()){
			failMsg = failMsg + "\n 2. Company name error [Expected] empty [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}
		
		//jobPosition
		if (!member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").isEmpty()) {
			failMsg = failMsg + "\n 3. jobPosition error [Expected] empty [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}
		
		//description
		if (!member_pres.get(0).findElement(By.xpath(".//textarea[@id='description']")).getText().isEmpty()) {
			failMsg = member_pres.get(0) + "\n 4. description error [Expected] empty [Actual]"
					+ driver.findElement(By.xpath(".//textarea[@id='description']")).getText();
		}
		
		// close profile
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 22. 발표자 프로필 확인 : 기존 프로필 채워져 있는 유저
	@Test(priority = 22, enabled = true)
	public void seminarmember_profile_user2() throws Exception {
		String failMsg = "";
		
		//check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		if (member_pres.size() != 2
				|| !member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
						.getText().contentEquals(mem_pres2)) {
			failMsg = failMsg + "1. Added Presenter error [Expected]" + mem_pres2 + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//span[@class='member-item__user-info__email']"))
							.getText();
		} 
		
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);
		
		//company name
		if(!member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']"))
				.getAttribute("value").contentEquals(CommonValues.USER_COMPANY)){
			failMsg = failMsg + "\n 2. Company name error [Expected]Company name [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}
		
		//jobPosition
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']"))
				.getAttribute("value").contentEquals(CommonValues.USER_POSITION)) {
			failMsg = failMsg + "\n 3. jobPosition error [Expected]position [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value");
		}
		
		//description
		if (!member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().isEmpty()) {
			failMsg = failMsg + "\n 4. description error [Expected] empty [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText();
		}
		
		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 23. 발표자 프로필 지우고 빈값으로 저장  
	@Test(priority = 23, enabled = true)
	public void seminarmember_profile_deleteall() throws Exception {
		String failMsg = "";

		// check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		//delete profile user2
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name clear clear로 하면 제대로 반영이 안되네..
		clearAttributeValue(member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")));
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).click();
		Thread.sleep(500);
		
		// jobPosition clear
		clearAttributeValue(member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")));
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).click();
		Thread.sleep(500);

		// description clear
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).clear();
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).click();
		Thread.sleep(500);
		
		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
		
		// save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);
		
		CommonValues comm = new CommonValues();
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver.getCurrentUrl().contains(detailview)) {
			driver.get(detailview);
			Thread.sleep(1000);
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_EDIT)).click();
		Thread.sleep(1000);
		//click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		
		member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").isEmpty()) {
			failMsg = failMsg + "1. Company name error [Expected] empty [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
			member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).clear();
		}

		// jobPosition
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value").isEmpty()) {
			failMsg = failMsg + "\n 2. jobPosition error [Expected] empty [Actual]"	
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value");
			member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		}

		// description
		if (!member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().isEmpty()) {
			failMsg = failMsg + "\n 3. description error [Expected] empty [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText();
			member_pres.get(1).findElement(By.xpath(".//input[@id='description']")).clear();
		}

		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 24. 발표자 프로필 : 입력 확인 
	@Test(priority = 24, enabled = true)
	public void seminarmember_profile_invalid() throws Exception {
		String failMsg = "";

		// check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		//delete profile user2
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name clear
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).sendKeys(company + "!@#");
		Thread.sleep(500);
		
		// jobPosition clear
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).sendKeys(position + "$%^");
		Thread.sleep(500);

		// description clear
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).clear();
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).sendKeys(description + "&*()");
		Thread.sleep(500);
		
		// company name
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").contentEquals(company)) {
			failMsg = "1. Company name error(invalid characters) [Expected]" + company + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}

		// jobPosition
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value").contentEquals(position)) {
			failMsg = failMsg + "\n 2. jobPosition error(invalid characters) [Expected]" + position + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value");
		}

		// description
		if (!member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().contentEquals(description + "&*()")) {
			failMsg = failMsg + "\n 3. description error(invalid characters) [Expected]" + description + "&*()" + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText();
		}

		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
		
		// save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver.getCurrentUrl().contains(detailview)) {
			driver.get(detailview);
			Thread.sleep(1000);
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_EDIT)).click();
		Thread.sleep(1000);
		
		//click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		Thread.sleep(1000);
		
		member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").contentEquals(company)) {
			failMsg = failMsg + "\n 4. Company name error(invalid characters) [Expected]" + company + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}

		// jobPosition
		if (!member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value").contentEquals(position)) {
			failMsg = failMsg + "\n 5. jobPosition error(invalid characters) [Expected]" + position + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value");
		}

		// description
		if (!member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().contentEquals(description + "&*()")) {
			failMsg = failMsg + "\n 6. description error(invalid characters) [Expected]" + description + "&*()" + " [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText();
		}

		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 25. 발표자 프로필 : 최대 길이 확인
	@Test(priority = 25, enabled = true)
	public void seminarmember_profile_max() throws Exception {
		String failMsg = "";

		// check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name clear & 40 characters
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).sendKeys(CommonValues.TWENTY_A + CommonValues.TWENTY_A );
		Thread.sleep(500);
		
		// jobPosition clear & 40 characters
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).sendKeys(CommonValues.TWENTY_A + CommonValues.TWENTY_A);
		Thread.sleep(500);
		
		// description clear & 600
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).clear();
		for(int i = 0 ; i < 15 ; i++) {
			member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).sendKeys(CommonValues.TWENTY_A + CommonValues.TWENTY_A);
		}
		Thread.sleep(500);
		
		// company name
		if (member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length() > 20) {
			failMsg = failMsg + "1. Company name error(length) [Expected]20 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length();
		}

		// jobPosition
		if (member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n 2. jobPosition error(length) [Expected]20 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length();
		}

		// description
		if (member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().length() > 500 ) {
			failMsg = failMsg + "\n 3. description error(length) [Expected]500 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().length();
		}

		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
		
		// save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver.getCurrentUrl().contains(detailview)) {
			driver.get(detailview);
			Thread.sleep(1000);
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_EDIT)).click();
		Thread.sleep(1000);
		
		// click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		Thread.sleep(1000);
		
		member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name
		if (member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n 4. Company name error(length) [Expected]20 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length();
		}

		// jobPosition
		if (member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n 5. jobPosition error(length) [Expected]20 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").length();
		}

		// description
		if (member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().length() > 500 ) {
			failMsg = driver + "\n 6. description error(length) [Expected] 500 [Actual]"
					+ member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).getText().length();
		}

		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 26. 발표자 프로필 : 정상 입력 케이스
	@Test(priority = 26, enabled = true)
	public void seminarmember_profile_valid() throws Exception {
		String failMsg = "";

		// check member
		List<WebElement> member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile member1
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);
		
		// company name clear
		member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).clear();
		member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).sendKeys(company + "0");
		Thread.sleep(500);
		
		// jobPosition clear
		member_pres.get(0).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		member_pres.get(0).findElement(By.xpath(".//input[@id='jobPosition']")).sendKeys(position + "0");
		Thread.sleep(500);

		// description clear
		member_pres.get(0).findElement(By.xpath(".//textarea[@id='description']")).clear();
		member_pres.get(0	).findElement(By.xpath(".//textarea[@id='description']")).sendKeys(description + "0");
		Thread.sleep(1000);

		// close profile
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);	
		
		
		// open profile member2
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		CommonValues comm = new CommonValues();
		// company name clear
		comm.selectAll(member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")));
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).sendKeys(Keys.BACK_SPACE);
		member_pres.get(1).findElement(By.xpath(".//input[@id='companyName']")).clear();
		Thread.sleep(500);
	
		// jobPosition clear
		comm.selectAll(member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")));
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).sendKeys(Keys.BACK_SPACE);
		member_pres.get(1).findElement(By.xpath(".//input[@id='jobPosition']")).clear();
		Thread.sleep(500);

		// description clear
		comm.selectAll(member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")));
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).sendKeys(Keys.BACK_SPACE);
		member_pres.get(1).findElement(By.xpath(".//textarea[@id='description']")).clear();
	
		Thread.sleep(1000);
		
		// close profile
		member_pres.get(1).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(500);	
		
		// save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver.getCurrentUrl().contains(detailview)) {
			driver.get(detailview);
			Thread.sleep(1000);
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_EDIT)).click();
		Thread.sleep(1000);
		
		// click member tab
		driver.findElement(By.xpath(XPATH_CREATEVIEW_TAB_MEMBER)).click();
		Thread.sleep(1000);
		
		member_pres = driver.findElements(By.xpath("//li[@class='MemberSetting_member-list__SPEAKER__MuQEG']"));
		
		// open profile member1
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick']")).click();
		Thread.sleep(1000);

		// company name
		if (!member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value").contentEquals(company + "0")) {
			failMsg = failMsg + "1. Company name error(valid) [Expected]" + (company + "0") +  " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//input[@id='companyName']")).getAttribute("value");
		}

		// jobPosition
		if (!member_pres.get(0).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value").contentEquals(position+"0")) {
			failMsg = failMsg + "\n 2. jobPosition error(valid) [Expected]" + (position + "0") +" [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//input[@id='jobPosition']")).getAttribute("value");
		}

		// description
		if (!member_pres.get(0).findElement(By.xpath(".//textarea[@id='description']")).getText().contentEquals(description + "0") ) {
			failMsg = failMsg + "\n 3. description error(valid) [Expected]" + (description + "0") + " [Actual]"
					+ member_pres.get(0).findElement(By.xpath(".//textarea[@id='description']")).getText();
		}

		// close profile
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-angle-left-thick isUnfold']")).click();
		Thread.sleep(1000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 31. 발표자 프로필 설정된 임시저장 세미나 상세화면 확인
	@Test(priority = 31, enabled = true)
	public void seminarmember_detailview_profile() throws Exception {
		String failMsg = "";

		// detailview
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailview);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(1000);
		
		// 배너 발표자 확인
		String bannerMem = driver.findElement(By.xpath("//p[@class='info-date-author date']")).getText().split("\n")[1];
		String memInfo = mem_pres1.replace("@gmail.com", "") + ", " + CommonValues.USERNICKNAME_JOIN;
		if(!bannerMem.contentEquals(memInfo)) {
			failMsg = failMsg + "1. Presenters error (detail view Banner) [Expected]" + memInfo + " [Actual]" + bannerMem;
		}
		
		List<WebElement> presProfile = driver.findElements(By.xpath("//div[@class='ProfileInfoBox_boxProfile__wmWoW ProfileInfoBox_view__1BdkY undefined']"));
		
		if(presProfile.size() != 2) {
			failMsg = failMsg + "\n 2. Presenters error (detail view) [Expected]2 [Actual]" + presProfile.size();
		}
	
		//member1 profile : 설정값 
		String profile_mem1 = company + "0. " + position +"0";
 		if(!presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText().contentEquals(company + "0. ")
 				|| !presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[2]")).getText().contentEquals(position+"0")) {
 			failMsg = failMsg + "\n 3. Presenters1's profile error (detail view) [Expected]" + profile_mem1 
 					+ " [Actual]" + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText()
 					+ presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[2]")).getText();
 		}
 		System.out.println("@@@@@name!!!: " + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText());
 		
 		if(!presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText().contentEquals(description+"0")) {
 			failMsg = failMsg + "\n 4. Presenters1's description error (detail view) [Expected]" + (description+"0")
 					+ " [Actual]" + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText();
 		}
		
 		//member1 profile : 프로필 빈값, 설명 빈값
		try {
			presProfile.get(1).findElement(By.xpath("ProfileInfoDisplay_companyPosition__3yQyl"));
			failMsg = failMsg + "\n 5. Presenters2's error (detail view) [Expected]not find element [Actual] find element" ;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!presProfile.get(1).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText().contentEquals("There is no description.")) {
 			failMsg = failMsg + "\n 6. Presenters2's description error (detail view) [Expected]" + "There is no description."
 					+ " [Actual]" + presProfile.get(1).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText();
 		}
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-auto actionButton']")));
		
		
		//post
		driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-auto actionButton']")).click();
		Thread.sleep(1000);
		
		if(isElementPresent(By.xpath("//div[@class='modal-body']"))) {
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		} 
		
		presProfile = driver.findElements(By.xpath("//div[@class='ProfileInfoBox_boxProfile__wmWoW ProfileInfoBox_view__1BdkY undefined']"));
		
		if(presProfile.size() != 2) {
			failMsg = failMsg+  "\n 7. Presenters error (detail view, post) [Expected]2 [Actual]" + presProfile.size();
		}
		
		//member1 profile : 설정값 
		profile_mem1 = company + "0. " + position +"0";
 		if(!presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText().contentEquals(company + "0. ")
 				|| !presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[2]")).getText().contentEquals(position+"0")) {
 			failMsg = failMsg + "\n 8. Presenters1's profile error (detail view, post) [Expected]" + profile_mem1 
 					+ " [Actual]" + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText()
 					+ presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[2]")).getText();
 		}
 		System.out.println("@@@@@name!!!: " + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_companyPosition__3yQyl']/span[1]")).getText());
 		
 		if(!presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText().contentEquals(description+"0")) {
 			failMsg = failMsg + "\n 9. Presenters1's description error (detail view, post) [Expected]" + (description+"0")
 					+ " [Actual]" + presProfile.get(0).findElement(By.xpath(".//div[@class='ProfileInfoDisplay_description__2AuPi']")).getText();
 		}
		
 		//member1 profile : 프로필 빈값, 설명 빈값
		try {
			presProfile.get(1).findElement(By.xpath("ProfileInfoDisplay_companyPosition__3yQyl"));
			failMsg = failMsg + "\n 10. Presenters2's error (detail view, post) [Expected]not find element [Actual] find element" ;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 100. 세미나 삭제
	@Test(priority = 100, enabled = true)
	public void seminarmember_deleteseminar() throws Exception {
		
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		
	}
	
	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
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
