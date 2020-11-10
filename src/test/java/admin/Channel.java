package admin;

import seminartest.CommonValues;
import seminartest.DBConnection;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * 2.채널관리 - 검색 - 날짜
 * 
 * 
 * 10.키워드 검색
 * 11.키워드 검색 invalid1
 * 12.키워드 검색 invalid2
 * 13.리스트 항목 셀 클릭 시 이동
 * 14.페이징
 * 15.엑셀
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
 * 29.채널수정-채널명
 * 30.채널수정-채널명2
 * 31.채널수정-채널URL 
 * 32.채널수정-채널URL-중복
 * 33.채널수정-채널URL-valid
 * 34.채널수정-채널URL-변경완료
 * 35.채널수정-채널설명
 * 36.채널수정-채널설명2
 * 37.채널정보 탭(빈채널)
 * 
 * 
 * 37.채널정보 -채널멤버탭-검색1
 */
public class Channel {
	
	//임시 XPATH
	public static String XPATH_CHANNELNAME = "/html/body/div[2]";
	public static String XPATH_CHANNELDESCRIPTION = "/html/body/div[2]";
	
	public static String XPATH_CHANNELNAME2 = "/html/body/div[3]";
	public static String XPATH_CHANNELURL2 = "/html/body/div[4]//button[1]";
	public static String XPATH_CHANNELDESCRIPTION2 = "/html/body/div[5]";
	
	//채널 정보
	public static String XPATH_CHANNEL_INFO_BIGNAME = "//div[@class='__profile__desc']/h5/span";
	public static String XPATH_CHANNEL_INFO_CHANNELNAME = "//section[@id='seminar-info-wrap']//tr[1]//div/div[1]";
	public static String XPATH_CHANNEL_INFO_CHANNELURL = "//section[@id='seminar-info-wrap']//tr[2]//div/div[1]";
	public static String XPATH_CHANNEL_INFO_CHANNELMASTER = "//section[@id='seminar-info-wrap']//tr[3]//div/div[1]";
	public static String XPATH_CHANNEL_INFO_CHANNELDESCRIPTION = "//section[@id='seminar-info-wrap']//tr[4]//div/div[1]";
	public static String XPATH_CHANNEL_INFO_CHANNELDISCLOSURE = "//section[@id='seminar-info-wrap']//tr[5]//td";
	public static String XPATH_CHANNEL_INFO_CHANNELPLAN = "//section[@id='seminar-info-wrap']//tr[6]//td";
	public static String XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS = "//section[@id='seminar-info-wrap']//tr[7]//td";
	public static String XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME = "//section[@id='seminar-info-wrap']//tr[8]//td";
	public static String XPATH_CHANNEL_INFO_CHANNELNAMEBTN = "//section[@id='seminar-info-wrap']//div[2]//tr[1]//button";
	public static String XPATH_CHANNEL_INFO_CHANNELURLBTN = "//section[@id='seminar-info-wrap']//div[2]//tr[2]//button";
	public static String XPATH_CHANNEL_INFO_CHANNELDESCRIPTIONBTN = "//section[@id='seminar-info-wrap']//div[2]//tr[4]//button";
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
	public static String XPATH_CHANNEL_REGIST_CANCEL_BTN = "//button[@class='ant-btn']"; // /html/body/div[3]//button[2]
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
	public static String MSG_CHANNELNAME_MODIFY_EMPTY = "올바른 채널명을 입력해주세요.";
	public static String MSG_CHANNELURL_MODIFY_EMPTY = "URL을 입력해주세요.";
	public static String MSG_CHANNELURL_MODIFY_WRONG = "채널 URL은 최대 10자리 영문+숫자만 입력할 수 있습니다";
	public static String MSG_CHANNELURL_MODIFY_DUPLICATE = "는 이미 존재하는 채널 URL입니다.";
	public static String MSG_TOAST = "올바르지 않습니다.";
	public static String MSG_TOAST2 = "사용자 검색에 실패하였습니다.";
	public static String MSG_TOAST3 = "사용자 정보가 검색되었습니다.";
	public static String MSG_TOAST4 = "등록 되었습니다.";
	public static String MSG_TOAST5 = "채널이 생성되었습니다.";
	public static String MSG_TOAST6 = "변경이 되었습니다.";
	
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
	public String adminchannelURL = "";

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
	
	@Test(priority = 2)
	public void dateSearch() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath("//input[@value='all-months']")).click();
		driver.findElement(By.xpath(KEYWORD_SEARCH_BTN)).click();

		CheckDateData(); //전체
		
		checkListView(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath("//input[@value='last-3-months']")).click();
		driver.findElement(By.xpath(KEYWORD_SEARCH_BTN)).click();
		
		CheckDateData(); //최근 3개월
		
		checkListView(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath("//input[@value='last-month']")).click();
		driver.findElement(By.xpath(KEYWORD_SEARCH_BTN)).click();
		
		CheckDateData(); //지난달
		
		checkListView(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		CheckDateData(); //당월
	}
		
	
	@Test(priority=10)
	public void keywordSearch() throws Exception {
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.setCalender(driver);
		
		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));
		
		//채널명 일부 입력 후 검색
		insertKeywordSearch(keywordtextbox, "rsrsup");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck("rsrsup");
		
		Thread.sleep(1000);
		
		//채널 마스터 닉네임 입력 후 검색
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.USERNICKNAME_ADMIN);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USERNICKNAME_ADMIN);
		
		Thread.sleep(1000);

		//채널 마스터 이메일 입력 후 검색
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.USEREMAIL_PRES);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USEREMAIL_PRES);
			
	}
	
	@Test(priority=11)
	public void keywordSearch_invalid1() throws Exception {
		
		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));
		
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.CHANNELNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USERNICKNAME_JP);
		
		Thread.sleep(1000);
		
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.USEREMAIL_JP);
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(seminartest.CommonValues.USEREMAIL_JP);
		
		Thread.sleep(1000);
	}
	
	@Test(priority=12)
	public void keywordSearch_invalid2() throws Exception {
		
		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));
		
		insertKeywordSearch(keywordtextbox, " ");
		
		Thread.sleep(1000);
		
		CountCheckandDataCheck(" ");
		
		Thread.sleep(1000); 
		
		insertKeywordSearch(keywordtextbox, "@");
		
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
		String failMsg = "";

		checkListView(driver);

		admin.CommonValues comm = new admin.CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);

		String N2 = driver.findElement(By.xpath("//span[@class='total']")).getText().replace("건", "");
		int Realcount = Integer.parseInt(N2);

		List<WebElement> paging = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_PAGING));
		String lastP = paging.get(paging.size() - 2).findElement(By.xpath("./a")).getText();
		int lastPnum = Integer.parseInt(lastP);

		// 페이지 수 확인
		if (lastPnum != (int) Math.ceil((double) Realcount / 30)) {
			failMsg = "1. list paging error. paging count [Expected]" + (int) Math.ceil((double) Realcount / 30)
					+ " [Actual]" + lastPnum;
		}
		if (lastPnum > 2) {
			// 다음페이지 클릭
			paging.get(paging.size() - 1).click();
			Thread.sleep(500);

			// 2번 활성화 되어 있는지 확인
			if (!paging.get(2).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2. 2nd page is not actived";
			}

			List<WebElement> rows2 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows2.size() != 30) {
				failMsg = failMsg + "\n 2-1. list rows [Expected]30 [Actual]" + rows2.size();
			}

			// 이전페이지 클릭
			paging.get(0).click();
			Thread.sleep(500);

			// 1번 활성화 되어 있는지 확인
			if (!paging.get(1).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2-2. 1st page is not actived";
			}

			List<WebElement> rows3 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows3.size() != 30) {
				failMsg = failMsg + "\n 2-3. list rows [Expected]30 [Actual]" + rows3.size();
			}

			// 마지막 페이지 클릭
			paging.get(paging.size() - 2).click();
			Thread.sleep(500);

			// 마지막 페이지 활성화 되어 있는지 확인
			if (!paging.get(lastPnum).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2-4. last page is not actived";
			}

			List<WebElement> rows4 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows4.size() != Realcount % 30) {
				failMsg = failMsg + "\n 2-5. list rows [Expected]" + Realcount % 30 + "[Actual]" + rows4.size();
			}

		} else if (lastPnum == 2) {
			// 다음페이지 클릭
			paging.get(paging.size() - 1).click();
			Thread.sleep(500);

			// 2번 활성화 되어 있는지 확인
			if (!paging.get(2).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2. 2nd page is not actived(lastPnum is 2)";
			}

			List<WebElement> rows5 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows5.size() != Realcount % 30) {
				failMsg = failMsg + "\n 2-1. list rows(lastPnum is 2) [Expected]" + Realcount % 30 + "[Actual]"
						+ rows5.size();
			}

			// 이전페이지 클릭
			paging.get(0).click();
			Thread.sleep(500);

			// 1번 활성화 되어 있는지 확인
			if (!paging.get(1).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2-2. 1st page is not actived(lastPnum is 2)";
			}

			List<WebElement> rows6 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows6.size() != 30) {
				failMsg = failMsg + "\n 2-3. list rows(lastPnum is 2) [Expected]30 [Actual]" + rows6.size();
			}

			// 마지막 페이지 클릭
			paging.get(paging.size() - 2).click();
			Thread.sleep(500);

			// 마지막 페이지 활성화 되어 있는지 확인
			if (!paging.get(lastPnum).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2-4. last page is not actived(lastPnum is 2)";
			}

			List<WebElement> rows7 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows7.size() != Realcount % 30) {
				failMsg = failMsg + "\n 2-5. list rows(lastPnum is 2) [Expected]" + Realcount % 30 + "[Actual]"
						+ rows7.size();
			}
		} else {

			if (!paging.get(1).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 2. 1st page is not actived(lastPnum is 1)";
			}

			List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows.size() != Realcount % 30) {
				failMsg = failMsg + "\n 2-1. list rows [Expected]" + Realcount % 30 + "[Actual]" + rows.size();
			}

		}

		// for test
		paging.get(1).click();
		Thread.sleep(500);

		// click 50rows
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROWS + "[2]")).click();
		Thread.sleep(500);

		paging = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_PAGING));
		lastP = paging.get(paging.size() - 2).findElement(By.xpath("./a")).getText();
		lastPnum = Integer.parseInt(lastP);

		if (lastPnum == 1) {
			// 세미나갯수/50(기본 row) = 마지막페이지 번호 -1과 동일

			if (!paging.get(1).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 3. 1st page is not actived(lastPnum is 1)";
			}

			List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows.size() != Realcount % 50) {
				failMsg = failMsg + "\n 3-1. list rows [Expected]" + Realcount % 50 + "[Actual]" + rows.size();
			}

		} else {
			List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows.size() != 50) {
				failMsg = failMsg + "\n 3-2. list rows [Expected]50 [Actual]" + rows.size();
			}
		}

		// click 100rows
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROWS + "[3]")).click();
		Thread.sleep(500);

		paging = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_PAGING));
		lastP = paging.get(paging.size() - 2).findElement(By.xpath("./a")).getText();
		lastPnum = Integer.parseInt(lastP);

		if (lastPnum == 1) {
			// 세미나갯수/100(기본 row) = 마지막페이지 번호 -1과 동일

			if (!paging.get(1).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n 4. 1st page is not actived(lastPnum is 1)";
			}

			List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows.size() != Realcount % 100) {
				failMsg = failMsg + "\n 4-1. list rows [Expected]" + Realcount % 100 + "[Actual]" + rows.size();
			}

		} else {
			List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
			if (rows.size() != 100) {
				failMsg = failMsg + "\n 4-2. list rows [Expected]100 [Actual]" + rows.size();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=15)
	public void checkExcelData() throws Exception {
		String failMsg = "";

		checkListView(driver);

		admin.CommonValues comm = new admin.CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);

		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));

		insertKeywordSearch(keywordtextbox, "rsrsup");

		ListDataNullCheck();

		// Web 행,열 갯수
		List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		int ROWcount = rows.size();
		List<WebElement> column = driver.findElements(By.xpath("//th[@class='ant-table-cell']"));
		int Columncount = column.size();

		// Excel Download
		driver.findElement(By.xpath("//button[@class='ant-btn ant-btn-sub ant-btn-sm']")).click();

		TimeUnit.SECONDS.sleep(5);

		String[][] data = new String[ROWcount][Columncount];

		for (int i = 0; i < ROWcount; i++) {
			for (int j = 0; j < Columncount; j++) {
				data[i][j] = rows.get(i)
						.findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[" + (j + 1) + "]")).getText();
				Thread.sleep(100);

				if (readExcelFile(comm.Excelpath("channel"), i, j).contentEquals("")) {
					readExcelFile(comm.Excelpath("channel"), i, j).replace("", "00:00:00");
				}
				if (!data[i][j].contentEquals(readExcelFile(comm.Excelpath("channel"), i, j))) {
					failMsg = "Not equal data : [WEB]" + data[i][j] + "[Excel]"
							+ readExcelFile(comm.Excelpath("channel"), i, j);
				}
			}
		}

		deleteExcelFile(comm.Excelpath("channel"));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
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
		
		if(!(CHANNEL_NAME.getAttribute("placeholder").contentEquals("채널명을 입력해주세요.") 
			|| CHANNEL_NAME.getAttribute("placeholder").contentEquals("channelName"))){
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
		
		if(!(CHANNEL_DESCRIPTION.getAttribute("placeholder").contentEquals("채널설명을 입력해주세요.") 
			|| CHANNEL_DESCRIPTION.getAttribute("placeholder").contentEquals("channelDescription"))){
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
	public void Channelinfo() throws Exception {
		String failMsg = "";
		
		channelName = "ChannelTest";
		channelMaster = seminartest.CommonValues.USERNICKNAME_ADMIN + "(" +seminartest.CommonValues.USEREMAIL_ADMIN + ")";
		channelDescription = admin.CommonValues.SPECIAL_10; 
		channelDisclosure = "PRIVATE";
		Plan = "";
		numberofSeminars = "0건";
		runningTime = "00:00:00";
		
		cellClick("[1]");
		
		adminchannelURL = driver.getCurrentUrl();
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		CheckURL(1, adminchannelURL);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_BIGNAME)).getText().contentEquals(channelName)){
			failMsg = "1.Channel info big name is wrong [Expected]" + channelName + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_BIGNAME)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText().contentEquals(channelName)){
			failMsg = failMsg + "\n 2.Channel info channel name is wrong [Expected]" + channelName + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELMASTER)).getText().contentEquals(channelMaster)){
			failMsg = failMsg + "\n 3.Channel info channel master is wrong [Expected]" + channelMaster + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELMASTER)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText().contentEquals(channelDescription)){
			failMsg = failMsg + "\n 4.Channel info channel description is wrong [Expected]" + channelDescription + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDISCLOSURE)).getText().contentEquals(channelDisclosure)){
			failMsg = failMsg + "\n 5.Channel info channel disclosure is wrong [Expected]" + channelDisclosure + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDISCLOSURE)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELPLAN)).getText().contentEquals(Plan)){
			failMsg = failMsg + "\n 6.Channel info channel Plan is wrong [Expected]" + Plan + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELPLAN)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME)).getText().contentEquals(runningTime)){
			failMsg = failMsg + "\n 7.Channel info channel Runningtime is wrong [Expected]" + runningTime + " [Actual]" +
						driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELRUNNINGTIME)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS)).getText().contentEquals(numberofSeminars)){
			failMsg = failMsg + "\n 8.Channel info channel Number of Seminars is wrong [Expected]" + numberofSeminars + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNUMBEROFSEMINARS)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB1)).getAttribute("aria-selected").contentEquals("true")) {
			failMsg = failMsg + "\n 8. seminar tab is not default Channel info tab ";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=29) 
	public void ModifyChannelName() throws Exception {
		String failMsg = "";
		
		channelName = "ChannelTest";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAMEBTN)).click();
		WebElement Channelnametextbox = driver.findElement(By.xpath("//input[@id='channelName']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(Channelnametextbox, 1, "");
		
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@role='alert']")).getText().contentEquals(MSG_CHANNELNAME_MODIFY_EMPTY)){
			failMsg = "1.Msg wrong When modify empty ChannelName";
		}
		
		TimeUnit.SECONDS.sleep(4);
		
		comm.insertData(Channelnametextbox, 2, admin.CommonValues.SPECIAL_10);
		
		if(Channelnametextbox.getAttribute("value").length() != 30) {
			failMsg = failMsg + "\n 2.Modified Channel Name is wrong length [Actual]" + Channelnametextbox.getAttribute("value").length();
		}
		
		Thread.sleep(1000);
		
		driver.findElement(By.xpath(XPATH_CHANNELNAME + "//button[2]")).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText().contentEquals(channelName)){
			failMsg = failMsg + "\n 3.Channel name modified [Expected]" + channelName + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=30) 
	public void ModifyChannelName2() throws Exception {
		String failMsg = "";
		
		channelName = "Modify ChannelTest";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAMEBTN)).click();
		WebElement Channelnametextbox = driver.findElement(By.xpath("//input[@id='channelName']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(Channelnametextbox, 1, channelName);
		
		driver.findElement(By.xpath(XPATH_CHANNELNAME +"//button[1]")).click();
	
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST6)) {
			failMsg = "1.toast msg error When Modify Channelname [Expected]" + MSG_TOAST6
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText().contentEquals(channelName)){
			failMsg = failMsg + "\n 2.Channelname not modified [Expected]" + channelName + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELNAME)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=31) 
	public void ModifyChannelURL() throws Exception {
		String failMsg = "";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		channelURL = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText();
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURLBTN)).click();
		WebElement ChannelURLtextbox = driver.findElement(By.xpath("//input[@id='channelUrl']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(ChannelURLtextbox, 1, "");
		
		if(driver.findElement(By.xpath("//div[@role='alert']")).getText().contentEquals(MSG_CHANNELURL_MODIFY_EMPTY)){
			failMsg = "1.Msg wrong When modify empty ChannelURL.";
		}
		
		driver.findElement(By.xpath(XPATH_CHANNELURL2 +"//button[1]")).click();
		
		if(!driver.findElement(By.xpath("//div[@class='ant-modal-content']")).isDisplayed()) {
			failMsg = failMsg + "\n 2.URL change Window is not displayed.";
		}
		
		comm.insertData(ChannelURLtextbox, 1, admin.CommonValues.SPECIAL_10);
		
		if(driver.findElement(By.xpath("//div[@role='alert']")).getText().contentEquals(MSG_CHANNELURL_MODIFY_WRONG)){
			failMsg = failMsg + "\n 3.Msg wrong When modify wrong ChannelURL.";
		}
		
		comm.insertData(ChannelURLtextbox, 1, admin.CommonValues.CHARACTER_20 + admin.CommonValues.NUMBER_10);
		
		if(driver.findElement(By.xpath("//div[@role='alert']")).getText().contentEquals(MSG_CHANNELURL_MODIFY_WRONG)){
			failMsg = failMsg + "\n 4.Msg wrong When modify ChannelURL over 10.";
		}
		
		driver.findElement(By.xpath(XPATH_CHANNELURL2 + "//button[2]")).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText().contentEquals(channelURL)){
			failMsg = failMsg + "\n 5.Channel info channel URL modified [Expected]" + channelURL + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=32) 
	public void ModifyChannelURL_duplicate() throws Exception {
		String failMsg = "";
		String url = "rsrsup";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		channelURL = driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText();
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURLBTN)).click();
		WebElement ChannelURLtextbox = driver.findElement(By.xpath("//input[@id='channelUrl']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(ChannelURLtextbox, 1, url);
		
		driver.findElement(By.xpath(XPATH_CHANNELURL2 + "//button[1]")).click();
		
		if(driver.findElement(By.xpath("//div[@role='alert']")).getText().contentEquals(url + MSG_CHANNELURL_MODIFY_DUPLICATE)){
			failMsg = "1.Msg wrong When modify Duplicate ChannelURL.";
		}
		
		driver.findElement(By.xpath(XPATH_CHANNELURL2 + "//button[2]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=33) 
	public void ModifyChannelURL_valid() throws Exception {
		String failMsg = "";
		String url = "rsrsup0";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		channelURL = seminartest.CommonValues.SERVER_URL + seminartest.CommonValues.CHANNEL_VIEW_URL + url;
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURLBTN)).click();
		
		WebElement ChannelURLtextbox = driver.findElement(By.xpath("//input[@id='channelUrl']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(ChannelURLtextbox, 1, url);
		
		driver.findElement(By.xpath(XPATH_CHANNELURL2 + "//button[1]")).click();
		
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST6)) {
			failMsg = "1.toast msg error(Registerd email value search) [Expected]" + MSG_TOAST6
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText().contentEquals(channelURL)){
			failMsg = failMsg + "\n 2.Channel URL not modified [Expected]" + channelURL + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELURL)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=34) 
	public void ModifyChannelURL_finished() throws Exception {
		String failMsg = "";
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		if(!driver.findElements(By.xpath(XPATH_CHANNEL_INFO_CHANNELURLBTN)).isEmpty()) {
			failMsg = "1.URL Change Btn is displayed";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	
	@Test(priority=35) 
	public void ModifyChannelDescription() throws Exception {
		String failMsg = "";
		
		channelDescription = admin.CommonValues.SPECIAL_10; 
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTIONBTN)).click();
		
		WebElement Channeldescriptiontextbox = driver.findElement(By.xpath("//input[@id='description']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(Channeldescriptiontextbox, 1, "");
		
		driver.findElement(By.xpath(XPATH_CHANNELDESCRIPTION + "//button[1]")).click();
		
		if(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText() != null 
			|| driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText() != "") {
			failMsg = "1.ChannelDescription is not null [Expected]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTIONBTN)).click();
		
		comm.insertData(Channeldescriptiontextbox, 3, admin.CommonValues.SPECIAL_10);
		
		if(Channeldescriptiontextbox.getAttribute("value").length() != 500) {
			failMsg = failMsg + "\n 2.Channeldescriptiontextbox length is wrong [Expected]500"
					+ " [Actual]" + Channeldescriptiontextbox.getAttribute("value").length();
		}
		
		driver.findElement(By.xpath(XPATH_CHANNELDESCRIPTION + "//button[2]")).click();
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText().contentEquals(channelDescription)){
			failMsg = failMsg + "\n 3.Channel description modified [Expected]" + channelDescription + " [Actual]" +
					driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=36) 
	public void ModifyChannelDescription2() throws Exception {
		String failMsg = "";
		
		String str = admin.CommonValues.SPECIAL_10;
		channelDescription = new String(new char[4]).replace("\0", str);
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	 
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTIONBTN)).click();
		
		WebElement Channeldescriptiontextbox = driver.findElement(By.xpath("//input[@id='description']"));
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.insertData(Channeldescriptiontextbox, 2, admin.CommonValues.SPECIAL_10);
		
		driver.findElement(By.xpath(XPATH_CHANNELDESCRIPTION + "//button[1]")).click();
		
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_TOAST2)).getText().contentEquals(MSG_TOAST6)) {
			failMsg = "1.toast msg error When Modify Channeldescription [Expected]" + MSG_TOAST6
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST2)).getText();
		}
		
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText().contentEquals(channelDescription)) {
			failMsg = "1.ChannelDescription not modified [Expected]" + channelDescription 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_INFO_CHANNELDESCRIPTION)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=37) 
	public void ChannelinfoEmptyTAB() throws Exception {
		String failMsg = "";
		
		channelDisclosure = "PRIVATE";
		channelID = driver.getCurrentUrl().replace(admin.CommonValues.ADMIN_URL + admin.CommonValues.URL_CHANNELINFO, "");
		
		driver.get(adminchannelURL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		CheckURL(1, adminchannelURL);
		
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
		System.out.println(channelID);
		seminartest.DBConnection DB = new seminartest.DBConnection();
		DB.deleteChannel(channelID);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=38) 
	public void ChannelinfoSearchTAB() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		admin.CommonValues comm = new admin.CommonValues();
		comm.setCalender(driver);
		
		WebElement keywordtextbox = driver.findElement(By.xpath(KEYWORD_SEARCH_TEXTBOX));
		
		insertKeywordSearch(keywordtextbox, seminartest.CommonValues.USEREMAIL);
		
		ListDataNullCheck();
		
		cellClick("[1]");
		
		driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2)).click();
		
		WebElement searchbox = driver.findElement(By.id("search_searchItem"));
		
		
		insertKeywordSearch(searchbox, "rsrsup2");
		
		if(!(driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2_PANEL + "//td[2]")).getText().contains("rsrsup2") ||
				driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2_PANEL + "//td[3]")).getText().contains("rsrsup2"))) {
			failMsg = "1.Wrong Search Data When insert nickname";
		}
	
		insertKeywordSearch(searchbox, seminartest.CommonValues.USEREMAIL_PRES);
		
		if(!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2_PANEL + "//td[2]")).getText().contains(seminartest.CommonValues.USEREMAIL_PRES) ||
				!driver.findElement(By.xpath(XPATH_CHANNEL_INFO_TAB2_PANEL + "//td[3]")).getText().contains(seminartest.CommonValues.USEREMAIL_PRES)) {
			failMsg = failMsg + "2.Wrong Search Data When insert email";
		}
	
	
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
	
	private void insertKeywordSearch(WebElement e, String data) throws Exception {
		
		while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
			e.sendKeys(Keys.BACK_SPACE);
		
		e.sendKeys(data);
		Thread.sleep(1000);
		e.sendKeys(Keys.ENTER);
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
	
	private void CheckDateData() throws Exception {
		// 기준날짜(년도,월)
		String failMsg = "";
		String StandardStartdate = driver.findElement(By.xpath("//*[@id=\"datePicker\"]")).getAttribute("value").replace("/", "");
		String StandardEnddate = driver.findElement(By.xpath("//div[@class='ant-picker-input']/input")).getAttribute("value").replace("/", "");
		int StandardStartdatevalue = Integer.parseInt(StandardStartdate);
		int StandadEnddatevalue = Integer.parseInt(StandardEnddate);
		System.out.println(StandardStartdate);

		// 리스트 줄수 100 선택
		driver.findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROWS + "[3]")).click();

		Thread.sleep(500);

		List<WebElement> rows = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
		int ROWcount = rows.size();
		String N2 = driver.findElement(By.xpath("//span[@class='total']")).getText().replace("건", "");
		int Realcount = Integer.parseInt(N2);

		if (Realcount <= 100) {
			for (int i = 0; i < ROWcount; i++) {
				String data = rows.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[1]")).getText().replace("/", "");
				int datavalue = Integer.parseInt(data);
				
				if (!(StandardStartdatevalue <= datavalue && datavalue <= StandadEnddatevalue)) {
					failMsg = "0.default Search is wrong [Row num]" + i;
				}
			}
		} else if (Realcount > 100) { // 데이터 100개 초과할경우

			List<WebElement> paging = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_PAGING));
			String lastP = paging.get(paging.size() - 2).findElement(By.xpath("./a")).getText();
			int lastPnum = Integer.parseInt(lastP);

			for (int j = 1; j < lastPnum; j++) {
				paging.get(paging.size() - 1).click(); // 다음페이지 반복 클릭
				Thread.sleep(500);
				if (j + 1 == lastPnum) { // 마지막 페이지 도달할 경우 row갯수 찾고 데이터 검사
					List<WebElement> rows2 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
					int ROWcount2 = rows2.size();

					for (int k = 0; k < ROWcount2; k++) {
						String data = rows.get(k).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[1]")).getText().replace("/", "");
						int datavalue = Integer.parseInt(data);
						if (!(StandardStartdatevalue <= datavalue && datavalue <= StandadEnddatevalue)) {
							failMsg = "\n 2.default Search is wrong [Page num]" + j + 1 + " [Row num]" + k;
						}
					}
				} else { // 마지막 페이지 도달할때까지 데이터 검사
					List<WebElement> rows3 = driver.findElements(By.xpath(admin.CommonValues.XPATH_LIST_ROW_ITEM));
					int ROWcount3 = rows3.size();

					for (int i = 0; i < ROWcount3; i++) {
						String data = rows.get(i).findElement(By.xpath(admin.CommonValues.XPATH_LIST_ROW_CELL + "[1]")).getText().replace("/", "");
						int datavalue = Integer.parseInt(data);
						if (!(StandardStartdatevalue <= datavalue && datavalue <= StandadEnddatevalue)) {
							failMsg = failMsg + "\n 3.default Search is wrong [Page num]" + j + 1 + " [Row num]" + i;
						}
					}
				}
			}
		} else {
			failMsg = "data is null";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	private void readExcelFileRow(String filepath, int rowsize) throws Exception {
		String failMsg = "";
		
		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook testDataWorkBook = new XSSFWorkbook(inputStream);
		Sheet testDataSheet = testDataWorkBook.getSheetAt(0);

		int RealrowCount = testDataSheet.getLastRowNum() - testDataSheet.getFirstRowNum();
		
		if(RealrowCount != rowsize) {
			failMsg = "Row size is wrong [Result] Excel:" + RealrowCount + " Web:" + rowsize;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	private String readExcelFile(String filepath, int x, int y) throws Exception {

		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook testDataWorkBook = new XSSFWorkbook(inputStream);
		Sheet testDataSheet = testDataWorkBook.getSheetAt(0);

		int rowCount = testDataSheet.getLastRowNum();
		int cells = testDataSheet.getRow(0).getPhysicalNumberOfCells();

		DataFormatter formatter = new DataFormatter();

		String[][] data = new String[rowCount][cells];

		for (int i = 0; i < rowCount; i++) {

			// 첫 행 제외
			Row row = testDataSheet.getRow(i + 1);

			for (int j = 0; j < cells; j++) {
				// 1열,2열 제외
				Cell cell = row.getCell(j + 2);
				String a = formatter.formatCellValue(cell);

				data[i][j] = a;
			}
		}
		return data[x][y];
	}

	private static void deleteExcelFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("File is delete");
	        } else {
	            
	            System.out.println("File is not exist");  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
}