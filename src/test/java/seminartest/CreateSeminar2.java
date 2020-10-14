package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
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
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* CreateSeminar2
 * user : rsrsup2
 * Part1
 * 1. 제목없이 저장시도
 * 2. 현재시간 이전시간으로 저장시도
 * 3. 저장완료 후 데이터 확인
 * 4. 상세정보 파일 첨부 3개 후 저장완료 3회 클릭 - 상세에서 파일 정상 확인
 * 5. 저장완료 세미나 상세 - 삭제
 * 
 * part2
 * 11. 게시하기
 * 12. 게시된 세미나(예정세미나) 상세 화면에서 수정 클릭 - 수정화면에 저장완료 버튼 없음 확인 (수정하기1포함)
 * 13. 게시된 세미나 수정하기2 : 메일 보내지 않음
 * 14. 게시된 세미나 수정하기 - 메일 보내기
 * 15. 게시된 세미나 수정 후  저장없이 이동. 내용 확인
 * 16. 게시된 세미나(예정세미나) 상세 화면에서 세미나 삭제
 * 
 * part2
 * 20. 제목 최소값(1bytes) 저장 시도
 * 21. 대기화면 default
 * 22. 대기화면 문구 Max 입력(40자 초과)
 * 23. 대기화면 문구 빈채로 저장 - 확인
 * 24. 대기화면 문구 기본 입력(40자 이내)-대기 시간 선택(1시간) - 저장 - 리스트에서 수정 - 값 확인
 * 25. 영상으로 변경 선택 후 문구 및 시간 확인
 * 26. 영상 url 빈값으로 저장완료, 게시하기 시도
 * 27. 영상 잘못된 링크 입력 후 alert 확인
 * 28. 영상 정상 링크 입력 
 * 29. 영상 문구 수정 저장완료 - 저장완료된 세미나 불러와서 확인
 * 30. 리스트에서 저장완료 세미나 삭제
 * 
 */

public class CreateSeminar2 {
	public static String MSG_SAVE_TIME_ERROR = "Please confirm the date and time of seminar. Enter a later time from now.";
	public static String PLACEHOLDER_MSG_STANDBY = "Write a phrase to display on the standby screen.";
	public static String PLACEHOLDER_YOUTUBE_LINK = "Enter Youtube link";
	public static String MSG_NO_TITLE = "There is no title for the seminar. Please, enter a title.";
	public static String MSG_NO_YOUTUBE_URL = "Please, enter a valid YouTube link.";
	
	public  String standby_time_selector = "Seminar starts in %d minutes.";

	public static String STANDBY_IMG_TXT = "standbyview_IMAGE123!";
	public static String STANDBY_VIDEO_TXT = "standbyview_VIDEO123!";
	
	public static String XPATH_CREATESEMINAR_TAB1 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[1]";
	public static String XPATH_CREATESEMINAR_TAB2 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[2]";
	public static String XPATH_CREATESEMINAR_TAB3 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[2]";
	
	public static String XPATH_CREATESEMINAR_TITLE = "//div[@class='seminar-title']/input[1]";
	public static String XPATH_SEMINARVIEW_EDIT_BTN = "//div[@class='ricon ricon-edit']";
	public static String XPATH_SEMINARVIEW_DELETE_BTN = "//div[@class='ricon ricon-trash']";
	public static String XPATH_SEMINARVIEW_WAITING_MSG = "//div[@class='ql-container ql-snow']";
	public static String XPATH_SEMINARVIEW_WAITING_TIME = "//div[@id='waiting-display-time']/div[1]";
	
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	public String seminarTitle = "";

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

	@Test(priority = 1, enabled = true)
	public void createview() throws Exception {

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e = new Exception("Seminar Edit view : " + driver.getCurrentUrl());
			throw e;
		}
	}

	// 1. 제목없이 저장시도(버튼 비활성화)
	@Test(priority = 2, dependsOnMethods = { "createview" }, enabled = true)
	public void createview_emtpytitle() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();

		if (driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN))
				.isEnabled()) {
			failMsg = "1. save button is enabled(empty title)";
		}


		if (isElementPresent(By.xpath("//div[@class='updatedTime']"))) {
			failMsg = failMsg + "\n 3. update time is visible : "
					+ driver.findElement(By.xpath("//div[@class='updatedTime']")).getText();
		}
		
		//제목없이 다른탭 이동 시도
		// click standby tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_NO_TITLE)) {
			failMsg = failMsg + "\n 4. popup msg error(without title) : "
					+ driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 2. 현재시간 이전시간으로 저장시도
	@Test(priority = 3, dependsOnMethods = { "createview" }, enabled = true)
	public void createview_timecheck() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys("Time Test");

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, -10);
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String seminarTime = format1.format(cal.getTime());

		driver.findElement(By.name("startTime")).sendKeys(seminarTime);

		//check save as 
		if (driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).isEnabled()) {
			
			driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
			Thread.sleep(500);
			
			//msg
			if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_SAVE_TIME_ERROR)) {
				failMsg = "1. save as msg error(invalid time) [Expected]" + MSG_SAVE_TIME_ERROR
						+ " [Actual]" +driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			//click confirm
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
			Thread.sleep(500);
		
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 3. 저장완료 - 세미나 상세 화면 - 수정 - 데이터 확인
	@Test(priority = 4, dependsOnMethods = { "createview" }, enabled = true)
	public void createview_saveas() throws Exception {
		String failMsg = "";

		String seminarTitle = "Save as test";
		//setSeminar(seminarTitle);
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);

		// click save as..
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = "no list view after save seminar. current url : " + driver.getCurrentUrl();
		}

		// 저장완료 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		
		if(!driver.getCurrentUrl().contentEquals(detailView)) {
			driver.get(detailView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);

		String createEditUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if (!driver.getCurrentUrl().contentEquals(createEditUri)) {
			failMsg = failMsg + "\n 2. can't go edit view : " + driver.getCurrentUrl();
		} else {
			if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value")
					.contentEquals(seminarTitle)) {
				failMsg = failMsg + "\n 3. saved seminar title : "
						+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");
			}

			if (!driver.findElement(By.xpath("//div[@class='updatedTime']")).getText().contains("Saved.")) {
				failMsg = failMsg + "\n 4. update time check : [Expected] yyyy.MM.dd HH:mm:ss Saved." + " [Actual]"
						+ driver.findElement(By.xpath("//div[@class='updatedTime']")).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 4. 상세정보 파일 첨부 3개 후 저장완료 3회 클릭 - 상세에서 파일 정상 확인
	@Test(priority = 5, dependsOnMethods = { "createview" }, enabled = true)
	public void createview_saveas_file() throws Exception {
		String failMsg = "";
		
		String xpath_createview_fileitem = "//div[@class='DocumentItem_previewItem__156u8']";
		
		String createEditUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if (!driver.getCurrentUrl().contentEquals(createEditUri)) {
			driver.get(createEditUri);
		} 
		
		String filePath = CommonValues.TESTFILE_PATH;
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = CommonValues.TESTFILE_PATH_MAC;
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CreateSeminar.XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)));
		
		//add 3 files
		String[] addedfils = {CommonValues.TESTFILE_LIST[0], CommonValues.TESTFILE_LIST[5], CommonValues.TESTFILE_LIST[7]};
		
		driver.findElement(By.xpath(CreateSeminar.XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)).sendKeys(filePath+ addedfils[0]);
		Thread.sleep(1000);
		driver.findElement(By.xpath(CreateSeminar.XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)).sendKeys(filePath + addedfils[1]);
		Thread.sleep(1000);
		driver.findElement(By.xpath(CreateSeminar.XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)).sendKeys(filePath + addedfils[2]);
		Thread.sleep(1000);


		List<WebElement> items = driver.findElements(By.xpath(xpath_createview_fileitem));
		if(items.size() != 3) {
			failMsg = "1. added file count error : [Expected]3 [Actual]" + items.size();
		} else {
			
			for(int i = 0 ; i < items.size() ; i++) {
				if(!items.get(i).findElement(By.xpath("./span")).getText().contentEquals(addedfils[i])) {
					failMsg = failMsg + "\n 2. added file name error : [Expected]" + addedfils[i] 
							+ " [Actual]" + items.get(i).findElement(By.xpath("./span")).getText();
				}
			}
		}

		// save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);
		
		//go to detail view
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailview);
		Thread.sleep(500);
		
		//check files..
		items = driver.findElements(By.xpath(xpath_createview_fileitem));
		if(items.size() != 3) {
			failMsg = failMsg + "\n 3. added file count error(detail view): " + items.size();
		} else {
			for(int i = 0 ; i < items.size() ; i++) {
				if(!items.get(i).findElement(By.xpath("./span")).getText().contentEquals(addedfils[i])) {
					failMsg = failMsg + "\n 4. added file name error : [Expected]" + addedfils[i] 
							+ " [Actual]" + items.get(i).findElement(By.xpath("./span")).getText();
				}
			}
		}
		
		//edit
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CreateSeminar.XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)));
		
		items = driver.findElements(By.xpath(xpath_createview_fileitem));
		if(items.size() != 3) {
			failMsg = failMsg + "\n 5. added file count error(edit view): " + items.size();
		} else {
			for(int i = 0 ; i < items.size() ; i++) {
				if(!items.get(i).findElement(By.xpath("./span")).getText().contentEquals(addedfils[i])) {
					failMsg = failMsg + "\n 6. added file name error : [Expected]" + addedfils[i] 
							+ " [Actual]" + items.get(i).findElement(By.xpath("./span")).getText();
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 5. 저장완료 세미나 상세 화면 - 삭제
	@Test(priority = 6, dependsOnMethods = { "createview" }, enabled = true)
	public void createview_draft_del() throws Exception {
		String failMsg = "";

		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_DELETE_BTN)).click();
		Thread.sleep(500);

		if (!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText()
				.contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
			failMsg = "1. delete popup msg : [Expected]" + CommonValues.MSG_DELETE_SEMINAR + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}

		// click cancel
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		Thread.sleep(500);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_DELETE_BTN)).click();
		Thread.sleep(500);

		// click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "\n 2. can't go list view after delete serminar : " + driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	// 11. 게시하기 (예정세미나)
	@Test(priority = 11, enabled = true)
	public void postSeminar() throws Exception {
		String failMsg = "";

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e = new Exception("Seminar Edit view : " + driver.getCurrentUrl());
			throw e;
		}
		seminarTitle = "Post Test";
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);

		// click save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		
		//post
		comm.postSeminar(driver, seminarID);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 12. 게시된 세미나(예정세미나) 상세 화면에서 수정 클릭
	@Test(priority = 12, enabled = true)
	public void regiesterdSeminar_edit1() throws Exception {
		String failMsg = "";

		// go to detail view
		String detailV = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailV);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);

		// edit view
		String createEditUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if (!driver.getCurrentUrl().contentEquals(createEditUri)) {
			failMsg = failMsg + "\n 1. can't go edit view : " + driver.getCurrentUrl();
			driver.get(createEditUri);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}

		// channel
		if (isElementPresent(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']"))) {
			failMsg = "1. channel edit buttion is enabled.";
		}

		// check button(save only)
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).getText()
				.contentEquals("Save")) {
			failMsg = failMsg + "\n 2. button name check [Expected]Save [Actual]" + driver
					.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 13. 게시된 세미나 수정하기2 : 메일 보내지 않음
	@Test(priority = 13, dependsOnMethods = { "regiesterdSeminar_edit1" }, enabled = true)
	public void regiesterdSeminar_edit2() throws Exception {
		String failMsg = "";

		// edit title
		seminarTitle = seminarTitle + " edit2";
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(" edit2");

		// click save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		if (!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText()
				.contentEquals(CommonValues.MSG_SAVE_REGISTERED_SEMINAR)) {
			failMsg = "1. save msg error [Expected]" + CommonValues.MSG_SAVE_REGISTERED_SEMINAR + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}

		// click don't send
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		Thread.sleep(1000);

		String listUri = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if (!driver.getCurrentUrl().contains(listUri)) {
			failMsg = failMsg + "\n 2. not list view(after click save) : " + driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 14. 게시된 세미나 수정하기 - 메일 보내기
	@Test(priority = 14, enabled = true)
	public void regiesterdSeminar_sendEmail() throws Exception {
		String failMsg = "";

		// go to detail view
		String detailV = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailV);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();
		Thread.sleep(500);

		// edit view
		String createEditUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if (!driver.getCurrentUrl().contentEquals(createEditUri)) {
			failMsg = failMsg + "\n 1. can't go edit view : " + driver.getCurrentUrl();
			driver.get(createEditUri);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}

		// check title
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value")
				.contentEquals(seminarTitle)) {
			failMsg = failMsg + "\n 2. title error. [Expected]" + seminarTitle + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");
		}

		// click save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		if (!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText()
				.contentEquals(CommonValues.MSG_SAVE_REGISTERED_SEMINAR)) {
			failMsg = failMsg + "\n 3. save msg error [Expected]" + CommonValues.MSG_SAVE_REGISTERED_SEMINAR
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}

		// click send
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);

		String listUri = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if (!driver.getCurrentUrl().contains(listUri)) {
			failMsg = failMsg + "\n 4. not list view(after click save) : " + driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 15. 게시된 세미나 수정 후 저장없이 이동. 내용 확인
	@Test(priority = 15, enabled = true)
	public void regiesterdSeminar_dontSave() throws Exception {
		String failMsg = "";

		// go to detail view
		String detailV = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailV);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);

		// edit view
		String createEditUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if (!driver.getCurrentUrl().contentEquals(createEditUri)) {
			failMsg = failMsg + "\n 1. can't go edit view : " + driver.getCurrentUrl();
			driver.get(createEditUri);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}

		// check title
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value")
				.contentEquals(seminarTitle)) {
			failMsg = failMsg + "\n 2. title error. [Expected]" + seminarTitle + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");
		}

		// edit title
		String tempTitle = seminarTitle + " don't save";
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(" don't save");
		Thread.sleep(500);

		// click standby tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);
		// click detail tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB1)).click();
		Thread.sleep(500);
		// check title(temp title)
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value")
				.contentEquals(tempTitle)) {
			failMsg = failMsg + "\n 2. title error. [Expected]" + tempTitle + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");
		}

		// go to list view
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// go to editview
		driver.get(createEditUri);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// check title
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value")
				.contentEquals(seminarTitle)) {
			failMsg = failMsg + "\n 2. title error. [Expected]" + seminarTitle + " [Actual]"
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 16. 게시된 세미나(예정세미나) 상세 화면에서 세미나 삭제
	@Test(priority = 16, enabled = true)
	public void regiesterdSeminar_delete() throws Exception {
		String failMsg = "";

		// go to detail view
		String detailV = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailV);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_DELETE_BTN)).click();
		Thread.sleep(500);

		// click cancel
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
		Thread.sleep(500);

		driver.findElement(By.xpath(XPATH_SEMINARVIEW_DELETE_BTN)).click();
		Thread.sleep(500);

		// click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "1. can't go list view after delete serminar : " + driver.getCurrentUrl();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 20. 제목 최소값(1bytes) 저장 시도
	@Test(priority = 20)
	public void createSeminar_title1() throws Exception {
		String failMsg = "";

		// goto Create seminar
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(500);
		}
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e = new Exception("Seminar Edit view : " + driver.getCurrentUrl());
			throw e;
		}

		seminarTitle = "a";
		//setSeminar(seminarTitle);
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);

		// click save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		Thread.sleep(500);

		// set title
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(seminarTitle);

		Thread.sleep(500);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 21. 대기화면 default
	@Test(priority = 21, dependsOnMethods = "createSeminar_title1")
	public void standbyview_defalut() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI)) {
			// click standby tab
			driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
			Thread.sleep(1000);
		}

		if(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div"))
				.getAttribute("data-placeholder").contentEquals(PLACEHOLDER_MSG_STANDBY)) {
			failMsg = "1. standby view placeholder error : [Expected]" + PLACEHOLDER_MSG_STANDBY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).getAttribute("data-placeholder");
		}

		if(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME))
				.getText().contentEquals(String.format(standby_time_selector, 10))) {
			failMsg = failMsg + "\n 2. standby view time selector error : [Expected]" + String.format(standby_time_selector, 10) 
					+ " [Actual]" +driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME)).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 22. 대기화면 문구 Max 입력(40자 초과)
	@Test(priority = 22, dependsOnMethods = "createSeminar_title1")
	public void standbyview_maxtext() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI)) {
			// click standby tab
			driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
			Thread.sleep(1000);
		}

		// 60 characters
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).click();;
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(CommonValues.TWENTY_A);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(CommonValues.TWENTY_A);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(CommonValues.TWENTY_A);
		Thread.sleep(500);

		// click detail tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB1)).click();
		Thread.sleep(500);
		// click standby tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);

		// check editor
		if (driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().length() > 40) {
			failMsg = "1. standby view input text length error [Expected]40 characters, [Actual]"
					+ driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().length();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 23.대기화면 문구 빈채로 저장 - 확인
	@Test(priority = 23, dependsOnMethods = "createSeminar_title1")
	public void standbyview_emptytext() throws Exception {
		String failMsg = "";

		// clear text
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).click();
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).clear();
		Thread.sleep(500);
		seminarTitle = "a";
		
		// click save..
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);


		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(1000);

		// click saved tab
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARLIST_TAB + "[1]")).click();
		Thread.sleep(1000);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in saved tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement el = driver.findElement(By.xpath(listitem));
			el.findElement(By.xpath("../../..//i[@class='ricon-edit']")).click();
			Thread.sleep(500);

			String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

			if (!driver.getCurrentUrl().contains(createViewUrl)) {
				failMsg = failMsg + "\n 2. no create view after click edit button : " + driver.getCurrentUrl();
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			}
		}

		// click waitthing view tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);

		// check standby text
		if (!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().isEmpty()) {
			failMsg = failMsg + "\n 3. saved text error [Expected]empty [Actual]"
					+ driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText();

		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 24. 대기화면 문구 기본 입력(40자 이내)-대기 시간 선택(1시간) - 저장 - 리스트에서 수정 - 값 확인
	@Test(priority = 24, dependsOnMethods = "createSeminar_title1")
	public void standbyview_validtext() throws Exception {
		String failMsg = "";

		// valid text
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.BACK_SPACE);
		while(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).getText().isEmpty())
			driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.BACK_SPACE);
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(STANDBY_IMG_TXT);
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).click();
		Thread.sleep(2000);

		// set standby time(1hour)
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME)).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[4]")).click();
		Thread.sleep(1000);
		
		// click save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		// go to listview
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.findElement(By.xpath("//div[@class='logo-wrap']/a")).click();
			Thread.sleep(500);
			//String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
			//driver.get(listView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(500);
		}
		
		// click saved tab
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARLIST_TAB + "[1]")).click();
		Thread.sleep(1000);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement el = driver.findElement(By.xpath(listitem));
			el.findElement(By.xpath("../../..//i[@class='ricon-edit']")).click();
			Thread.sleep(500);

			String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

			if (!driver.getCurrentUrl().contains(createViewUrl)) {
				failMsg = failMsg + "\n 2. no create view after click edit button : " + driver.getCurrentUrl();
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			}
		}

		// click wating view tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);

		//check wating text
		if(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().contentEquals(STANDBY_IMG_TXT)) {
			failMsg = failMsg + "\n 3. saved text error [Expected]" +  STANDBY_IMG_TXT
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText();
			
		}
		
		String standbyTime = "Seminar starts in 1 hours.";
		//check standby time
		if (!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME + "/div[@class='text']")).getText().contentEquals(standbyTime)) {
			failMsg = failMsg + "\n 4. saved standby time [Expected]" + standbyTime 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME + "/div[@class='text']")).getText();

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 25. 영상으로 변경 선택 후 문구 및 시간 확인
	@Test(priority = 25, dependsOnMethods = "createSeminar_title1")
	public void standbyview_changeVideo() throws Exception {
		String failMsg = "";

		String standbyText = driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText();
		String standbyTime = driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME + "/div")).getText();
		
		//change cover mode : video
		driver.findElement(By.xpath("//div[@id='cover-mode']")).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(500);
		
		//check standby text
		if (!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().contentEquals(standbyText)) {
			failMsg = failMsg + "1. saved text error [Expected]" + standbyText + " [Actual]"
					+ driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText();

		}
		
		// check standby time
		if (!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME + "/div")).getText()
				.contentEquals(standbyTime)) {
			failMsg = failMsg + "\n 2. saved standby time [Expected]" + standbyTime + " [Actual]"
					+ driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_TIME + "/div")).getText();

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 26. 영상 url 빈값으로 저장완료, 게시하기 시도
	@Test(priority = 26, dependsOnMethods = "createSeminar_title1")
	public void standbyview_videoEmpty() throws Exception {
		String failMsg = "";

		String standbytab = CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI + seminarID;
		
		if(!driver.getCurrentUrl().contentEquals(standbytab)) {
			driver.get(standbytab);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath("//div[@id='cover-mode']/div[@class='text']")).getText().contentEquals("Video")) {
			//change cover mode : video
			driver.findElement(By.xpath("//div[@id='cover-mode']")).click();
			driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(500);
		}
		
		//placeholder
		if(!driver.findElement(By.xpath("//div[@class='linkBox']/input")).getAttribute("placeholder")
				.contentEquals(PLACEHOLDER_YOUTUBE_LINK)) {
			failMsg = "1. youtube link placeholder [Expected]" + PLACEHOLDER_YOUTUBE_LINK 
						+ " [Actual]" + driver.findElement(By.xpath("//div[@class='linkBox']/input")).getAttribute("placeholder");
		}
		
		// save as
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_NO_YOUTUBE_URL)) {
			failMsg = failMsg + "\n 2. empty youtube link msg [Expected]" + MSG_NO_YOUTUBE_URL 
						+" [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}
		//click confirm
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		Thread.sleep(500);
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 27. 영상 잘못된 링크 입력 후 alert 확인
	@Test(priority = 27, dependsOnMethods = {"standbyview_videoEmpty"}, alwaysRun = true, enabled = true)
	public void standbyview_videoInvalid() throws Exception {
		String failMsg = "";

		String standbytab = CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI + seminarID;
		
		if(!driver.getCurrentUrl().contentEquals(standbytab)) {
			driver.get(standbytab);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath("//div[@id='cover-mode']/div[@class='text']")).getText().contentEquals("Video")) {
			//change cover mode : video
			driver.findElement(By.xpath("//div[@id='cover-mode']")).click();
			driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).clear();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).sendKeys("invalid url");
		Thread.sleep(2000);

		if(!findAlert(driver, MSG_NO_YOUTUBE_URL)) {
			failMsg = "1. cannot find alert.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	

	// 28. 영상 정상 링크 입력 
	@Test(priority = 28, dependsOnMethods = {"standbyview_videoInvalid"}, alwaysRun = true, enabled = true)
	public void standbyview_videoValid() throws Exception {
		String failMsg = "";

		String standbytab = CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI + seminarID;
		
		if(!driver.getCurrentUrl().contentEquals(standbytab)) {
			driver.get(standbytab);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath("//div[@id='cover-mode']/div[@class='text']")).getText().contentEquals("Video")) {
			//change cover mode : video
			driver.findElement(By.xpath("//div[@id='cover-mode']")).click();
			driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).clear();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		Thread.sleep(2000);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	//29. 영상 문구 수정 저장완료 - 저장완료된 세미나 불러와서 확인
	@Test(priority = 29, dependsOnMethods = {"standbyview_videoValid"}, alwaysRun = true, enabled = true)
	public void standbyview_videoText() throws Exception {
		String failMsg = "";

		String standbytab = CommonValues.SERVER_URL + CommonValues.CREATE_WAITING_URI + seminarID;
		
		if(!driver.getCurrentUrl().contentEquals(standbytab)) {
			driver.get(standbytab);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath("//div[@id='cover-mode']/div[@class='text']")).getText().contentEquals("Video")) {
			//change cover mode : video
			driver.findElement(By.xpath("//div[@id='cover-mode']")).click();
			driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).click();
		Thread.sleep(500);
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")));
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.BACK_SPACE);
		while(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).getText().isEmpty())
			driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(Keys.BACK_SPACE);
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).sendKeys(STANDBY_VIDEO_TXT);
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG + "/div")).click();
		Thread.sleep(2000);

		
		// click save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);

		// go to listview
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.findElement(By.xpath("//div[@class='logo-wrap']/a")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(500);
		}

		// click saved tab
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARLIST_TAB + "[1]")).click();
		Thread.sleep(1000);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement el = driver.findElement(By.xpath(listitem));
			el.findElement(By.xpath("../../..//i[@class='ricon-edit']")).click();
			Thread.sleep(500);

			String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

			if (!driver.getCurrentUrl().contains(createViewUrl)) {
				failMsg = failMsg + "\n 2. no create view after click edit button : " + driver.getCurrentUrl();
				driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			}
		}

		// click standby tab
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB2)).click();
		Thread.sleep(500);

		//check standby text
		if(!driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText().contentEquals(STANDBY_VIDEO_TXT)) {
			failMsg = failMsg + "\n 3. saved text error [Expected]" +  STANDBY_VIDEO_TXT
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_SEMINARVIEW_WAITING_MSG)).getText();
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	
	// 30. 리스트에서 저장완료 세미나 삭제
	@Test(priority = 30, dependsOnMethods = "standbyview_videoText", alwaysRun = true, enabled = true)
	public void draftSeminar_deleteList() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click draft tab
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARLIST_TAB + "[1]")).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			//click trash icon 
			WebElement el = driver.findElement(By.xpath(listitem));
			el.findElement(By.xpath("../../..//i[@class='ricon-trash']")).click();
			Thread.sleep(500);

			if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
				failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
						+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//cancel
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			
			el.findElement(By.xpath("../../..//i[@class='ricon-trash']")).click();
			Thread.sleep(500);
			
			//confirm
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 3. find deleted seminsr in list";
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
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
	
	private boolean findAlert(WebDriver wd, String msg) {
		if (ExpectedConditions.alertIsPresent().apply(wd) == null) {
			return false;

		} else {
			// 알림창이 존재하면 알림창 확인을 누를것
			assertEquals(closeAlertAndGetItsText_webdriver(wd), msg);
			return true;
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
