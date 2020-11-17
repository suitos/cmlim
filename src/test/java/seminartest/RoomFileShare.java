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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/* RoomFileShare : rsrsup2
 * 
 * 0. 세미나 만들기 및 룸 입장. 시작
 * 1. 각 유저 입장(발표자, 운영자, 게스트)
 * 2. 발표자,운영자 룸 최초 입장. 설정팝업 확인
 * 3. 발표자,운영자 스탠바이룸 새로고침
 * 
 * 11. 발표자,운영자,참석자 파일다운로드 탭 빈 리스트 확인
 * 12. 발표자 공유 파일 등록
 * 13. 발표자 공유 invalid파일 등록
 * 14. 발표자 공유파일 삭제
 * 15. 운영자 공유파일 등록 삭제
 * 16. guest 공유파일 확인 다운로드
 * 
 * 
 * 21. 발표자 세미나 시작. 모든 다운로드 파일 삭제 후 빈화면 확인
 * 22. 발표자 파일등록
 * 23. 발표자 공유 invalid파일 등록_onair
 * 24. 발표자 공유파일 삭제 onair
 * 25. 운영자 공유파일 등록 삭제
 * 26. guest 공유파일 확인 다운로드
 * 
 * 31. 발표자, 운영자 onair룸 새로 입장 설정팝업 확인
 * 32. 발표자, 운영자 onair룸 새로고침 팝업 확인
 */

public class RoomFileShare {
	public static String XPATH_ROOM_DOWNLOAD_ADDFILE = "//div[@class='box-upload']/input";
	public static String XPATH_ROOM_DOWNLOAD_EMPTY = "//ul[@id='download-list']/li[@class='empty-list']/p";
	public static String XPATH_ROOM_DOWNLOAD_LIST = "//ul[@id='download-list']/li";
	public static String XPATH_ROOM_DOWNLOAD_LIST_FILENAME = ".//span[@class='filename']";
	public static String XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL = ".//i[@class='ricon-close']";
	public static String XPATH_ROOM_DOWNLOAD_LIST_BTN_DOWNLOAD = ".//i[@class='ricon-download']";
	public static String XPATH_ROOM_CONFIRM_DIALOG = "//section[@id='confirm-dialog']/p";
	public static String XPATH_ROOM_CONFIRM_BTN = "//section[@id='confirm-dialog']//div[@class='buttons align-center']";
	
	public static String XPATH_ROOM_SETTINGPOPUP_CAM = "//div[@id='camera-wrap']/strong[1]";
	public static String XPATH_ROOM_SETTINGPOPUP_MIC = "//div[@id='mic-wrap']/strong[1]";
	public static String XPATH_ROOM_SETTINGPOPUP_SPEAKER = "//div[@id='speaker-wrap']//strong[1]";
	
	public static String MSG_ADD_DOWNLOADFILE_CONFIRM = "Do you want to share the selected file?";
	public static String MSG_DOWNLOADFILE_EMPTY = "There is no shared file.";
	public static String MSG_DOWNLOADFILE_INVALID = "blabla";
	public static String MSG_DOWNLOADFILE_DELETE = "blabla";
	public static String MSG_ROOM_SETTINGPOPUP_TITLE = "Seminar settings";
	public static String MSG_ROOM_SETTINGPOPUP_BTN = "Continue the seminar";
	
	public static String USER_PUBLISHER = "rsrsup2";
	public static String USER_PRESENTER = "rsrsup3";
	public static String USER_ORGANIZER = "rsrsup6";
	public static String USER_MASTER = "rsrsup1";
	public static String USER_NOTMEMBER = "rsrsup9";
	
	public static WebDriver driver; //publisher
	public static WebDriver driver_presenter; 
	public static WebDriver driver_organizer; 
	public static WebDriver driver_guest;
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String seminarTitle = "";
	public String seminarID = "";
	public String seminarLink = "";
	
	private List<String> sharedFileList;
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver_presenter = comm.setDriver(driver_presenter, browsertype, "lang=en_US");
		driver_organizer = comm.setDriver(driver_organizer, browsertype, "lang=en_US");
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=en_US");
		
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", driver_presenter);
		context.setAttribute("webDriver3",  driver_organizer);
		context.setAttribute("webDriver4", driver_guest);
		driver.get(CommonValues.SERVER_URL); //publisher
		
		sharedFileList = new ArrayList<String>();
		
        System.out.println("End BeforeTest!!!");
	}
	
	// 0. 세미나 만들기
	@Test(priority=0)
	public void createSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_fileShare";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			failMsg = "1. not create view" + driver.getCurrentUrl();
			driver.get(createViewUri);
		}
		comm.setCreateSeminar_setChannel(driver);
		comm.setCreateSeminar(driver, seminarTitle, true);
		
		// 세미나 멤버 
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]")).click();
		Thread.sleep(500);
		//presenter popup
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_PRESENTER);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		//select 
		driver.findElement(By.xpath("//li[@role='presentation']/span[@class='member-email']")).click();
		Thread.sleep(500);

		// click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(500);
		
		//delete member 
		List<WebElement> member_pres = driver.findElements(By.xpath("//div[@role='presentation']"));
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(1000);
		
		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_ORGANIZER);
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		// select 1 (defalut 0 + 1)
		driver.findElement(By.xpath("//li[@role='presentation']//span[@class='member-name']")).click();
		
		// click selected
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(1000);		
		
		// save seminar
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		Thread.sleep(500);

		// post seminar
		comm.postSeminar(driver, seminarID);
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 1. 각 유저 입장(발표자, 운영자, 게스트)
	@Test(priority=1, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void enterRoom() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		
		//발표자 입장
		comm.loginseminar(driver_presenter, USER_PRESENTER + "@gmail.com");
		driver_presenter.get(detailview);
		Thread.sleep(1000);
		JavascriptExecutor js = (JavascriptExecutor) driver_presenter;
		js.executeScript("arguments[0].scrollIntoView();", driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);

		
		ArrayList<String> tabs2 = new ArrayList<String>(driver_presenter.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver_presenter.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. presenter cannot enter the room. current url : " + driver_presenter.getCurrentUrl();
			}
		} else {
			driver_presenter.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver_presenter.close();
			// switch room tab
			driver_presenter.switchTo().window(tabs2.get(1));
		}
		//운영자 입장
		comm.loginseminar(driver_organizer, USER_ORGANIZER + "@gmail.com");
		driver_organizer.get(detailview);
		Thread.sleep(1000);
		js = (JavascriptExecutor) driver_organizer;
		js.executeScript("arguments[0].scrollIntoView();", driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);

		
		tabs2 = new ArrayList<String>(driver_organizer.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver_organizer.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = failMsg + "\n1. organizer cannot enter the room. current url : " + driver_organizer.getCurrentUrl();
			}
		} else {
			driver_organizer.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver_organizer.close();
			// switch room tab
			driver_organizer.switchTo().window(tabs2.get(1));
		}
		
		//참석자 입장
		driver_guest.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		Thread.sleep(2000);
		
		//click join
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(500);
		
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).click();
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).clear();
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys("guest");
		
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).click();
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).clear();
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).sendKeys("guest@rsupport.com");
	
		if(!driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//div")).click();
		if(!driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//div")).click();
		
		Thread.sleep(500);
		//join seminar
		driver_guest.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_ENTER)).click();
		Thread.sleep(1000);
		
		//check room uri
		if(!roomuri.equalsIgnoreCase(driver_guest.getCurrentUrl())){
			failMsg = failMsg + "\n2. guest cannot enter the room. current url : " + driver_guest.getCurrentUrl();
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 2. 발표자,운영자 룸 최초 입장. 설정팝업 확인
	@Test(priority=2, dependsOnMethods = {"enterRoom"}, enabled = true)
	public void settingpopup_defalut() throws Exception {
		String failMsg = "";
		
		//발표자
		Thread.sleep(3000);
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText().contentEquals(MSG_ROOM_SETTINGPOPUP_TITLE)) {
				failMsg = failMsg + "\n1. setting popup title [Expected]" + MSG_ROOM_SETTINGPOPUP_TITLE 
						+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText();
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_CAM))) {
				failMsg = failMsg + "\n2. cannot find camera menu.";
			}
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_MIC))) {
				failMsg = failMsg + "\n3. cannot find microphone menu.";
			}
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_SPEAKER))) {
				failMsg = failMsg + "\n4. cannot find speaker menu.";
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN))) {
				failMsg = failMsg + "\n5. cannot find confirm button.";
			} else {
				if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).getText().contentEquals(MSG_ROOM_SETTINGPOPUP_BTN)) {
					failMsg = failMsg + "\n6. setting popup button message [Expected]" + MSG_ROOM_SETTINGPOPUP_BTN 
							+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).getText();
				}
				
				driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
				Thread.sleep(500);
			}
			
		} else {
			failMsg = "0. cannot find setting popup.";
		}
		
		// 운영자
		if(isElementPresent(driver_organizer, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			failMsg = failMsg + "\n7. find setting popup.(organizer)";
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
			Thread.sleep(500);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 발표자,운영자 스탠바이룸 새로고침
	@Test(priority=3, dependsOnMethods = {"enterRoom"}, enabled = true)
	public void settingpopup_refrash() throws Exception {
		String failMsg = "";
		
		//발표자 refrash
		driver_presenter.navigate().refresh();
		Thread.sleep(5000);
		
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText().contentEquals(MSG_ROOM_SETTINGPOPUP_TITLE)) {
				failMsg = failMsg + "\n1. setting popup title [Expected]" + MSG_ROOM_SETTINGPOPUP_TITLE 
						+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText();
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN))) {
				failMsg = failMsg + "\n2. cannot find confirm button.";
			} else {
				driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
				Thread.sleep(500);
			}
			
		} else {
			failMsg = "0. cannot find setting popup.";
		}
		
		//운영자 refrash
		driver_organizer.navigate().refresh();
		Thread.sleep(2000);
		
		if(isElementPresent(driver_organizer, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			failMsg = failMsg + "\n3. find setting popup.(organizer)";
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
			Thread.sleep(500);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. 발표자,운영자,참석자 파일다운로드 탭 빈 리스트 확인
	@Test(priority=11, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void empty_sharedFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		//발표자
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
		Thread.sleep(500);
		
		if(isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = "1. empty download file list message(presenter) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = "2. cannot find empty list message.";
		}
		
		//운영자
		driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
		Thread.sleep(500);
		
		if(isElementPresent(driver_organizer, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = failMsg + "\n3. empty download file list message(organizer) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = failMsg + "\n4. cannot find empty list message.";
		}
		
		//참석자
		driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
		Thread.sleep(500);
		
		if(isElementPresent(driver_guest, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_guest.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = failMsg + "\n5. empty download file list message(guest) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_guest.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = failMsg + "\n6. find cannot empty list message.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 12. 발표자 공유 파일 등록
	@Test(priority=12, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void register_sharedFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
		Thread.sleep(500);

		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[0];
		
		driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
		Thread.sleep(500);
		
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_MODAL_BODY))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_ADD_DOWNLOADFILE_CONFIRM)) {
				failMsg = "1. add file confirm popup message [Expected]" + MSG_ADD_DOWNLOADFILE_CONFIRM
						 + " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//click cancel
			driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER+"/button[2]")).click();
			Thread.sleep(500);
			
		} else {
			failMsg = "0. cannot find add file confirm popup.";
		}
		
		//check list
		if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			failMsg = failMsg + "\n2. file list error. cannot find empty message.";
		}
		
		List<WebElement> fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		for (int i = 0; i < CommonValues.TESTFILE_LIST.length; i++) {
			addedfile = filePath + CommonValues.TESTFILE_LIST[i];
			
			driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(1000);
			
			driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if(fileList.size() == (i+1)) {
				if(fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(CommonValues.TESTFILE_LIST[i])) {
					
				}
				sharedFileList.add(CommonValues.TESTFILE_LIST[i]);
			} else {
				failMsg = failMsg + "\n3-" + i + ". download file list count error [Expected]" + (i+1) + " [Actual]" + fileList.size();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 13. 발표자 공유 invalid파일 등록
	@Test(priority=13, dependsOnMethods = {"createSeminar"}, enabled = false)
	public void register_InvalidSharedFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		if(driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = "";
		for (int i = 0; i < CommonValues.TESTFILE_INVALID_LIST.length; i++) {
			addedfile = filePath + CommonValues.TESTFILE_INVALID_LIST[i];
			
			driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(500);
			
			if(isElementPresent(driver_presenter, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
				if(!driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(MSG_DOWNLOADFILE_INVALID)) {
					failMsg = failMsg + "\n0-" + i + ". invalid file message [Expected]" + MSG_DOWNLOADFILE_INVALID
							 + " [Actual]" + driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
				}
			} else {
				failMsg = failMsg + "\n0-" + i + ". cannot find toast.";
			}
			Thread.sleep(2000);
		}
		
		if(sharedFileList.size() != driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST)).size()) {
			failMsg = failMsg + "\n2. download file list count(after invalid action) [Expected]" + sharedFileList.size()
					 + " [Actual]" + driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST)).size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 14. 발표자 공유파일 삭제
	@Test(priority=14, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void delete_SharedFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		if(driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}	
		
		List<WebElement> fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		
		if(fileList.size() > 0) {
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);
			
			if(isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_CONFIRM_DIALOG))) {
				 if(!driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_DIALOG)).getText().contentEquals(MSG_DOWNLOADFILE_DELETE)) {
					 failMsg = failMsg + "\n1. delete popup message [Expected]" + MSG_DOWNLOADFILE_DELETE
							  + " [Actual]" + driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_DIALOG)).getText();
				 }
				 
				// cancel
				driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN+ "/button[2]")).click();
				Thread.sleep(500);
			} else {
				failMsg = failMsg + "0. cannot find delete popup.";
			}

			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if (sharedFileList.size() != fileList.size()) {
				failMsg = failMsg + "\n2. file count error(after cancel delete) [Expected]" + sharedFileList.size()
						+ " [Actual]" + fileList.size();
			}

			// delete
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);

			// cancel
			driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN + "/button[1]")).click();
			Thread.sleep(500);

			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if (sharedFileList.size()-1 != fileList.size()) {
				failMsg = failMsg + "\n3. file count error(after delete) [Expected]" + (sharedFileList.size()-1)
						+ " [Actual]" + fileList.size();
			} else {
				sharedFileList.remove(0);
			}
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 15. 운영자 공유파일 등록 삭제
	@Test(priority=15, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void organizer_SharedFile() throws Exception {
		String failMsg = "";
		
		//check file list
		if(driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		
		if(sharedFileList.size() != fileList.size()) {
			failMsg = failMsg + "\n1. file count error(organizer) [Expected]" + (sharedFileList.size())
					+ " [Actual]" + fileList.size();
		} else {
			//add1
			String filePath = CommonValues.TESTFILE_PATH;
			if (System.getProperty("os.name").toLowerCase().contains("mac")) 
				filePath = CommonValues.TESTFILE_PATH_MAC;
			String addedfile = filePath + CommonValues.TESTFILE_LIST[0];
			
			driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(1000);
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER +"/button[1]")).click();
			Thread.sleep(500);
			
			fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			
			if(sharedFileList.size() + 1 != fileList.size()) {
				failMsg = failMsg + "\n2. file count error(organizer, add 1) [Expected]" + (sharedFileList.size()+1)
						+ " [Actual]" + fileList.size();
			} else {
				sharedFileList.add(CommonValues.TESTFILE_LIST[0]);
				
				if(!fileList.get(fileList.size()-1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(sharedFileList.size()-1))) {
					failMsg = failMsg + "\n3. added file name [Expected]" + sharedFileList.get(sharedFileList.size()-1)
							+ " [Actual]" + fileList.get(fileList.size()-1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
				}
			}
	
			//delete1
			fileList.get(1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);
			
			driver_organizer.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN +"/button[1]")).click();
			Thread.sleep(500);
			sharedFileList.remove(1);
			fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			
			if(sharedFileList.size() != fileList.size()) {
				failMsg = failMsg + "\n4. file count error(organizer, delete1) [Expected]" + (sharedFileList.size())
						+ " [Actual]" + fileList.size();
			} else {
				//file list check
				for (int i = 0; i < sharedFileList.size(); i++) {
					if(!fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(i))){
						failMsg = failMsg + "\n5-" + i + ". file name [Expected]" + sharedFileList.get(i)
								+ " [Actual]" + fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
					}
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 16. guest 공유파일 확인 다운로드
	@Test(priority=16, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void guest_SharedFile() throws Exception {
		String failMsg = "";
		
		//check file list
		if (driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}

		List<WebElement> fileList = driver_guest.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));

		if (sharedFileList.size() != fileList.size()) {
			failMsg = failMsg + "\n1. file count error(guest) [Expected]" + (sharedFileList.size()) + " [Actual]"
					+ fileList.size();
		} else {
			for (int i = 0; i < sharedFileList.size(); i++) {
				if(!fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(i))){
					failMsg = failMsg + "\n2-" + i + ". file name [Expected]" + sharedFileList.get(i)
							+ " [Actual]" + fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
				}
			}
			
			//download 1
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DOWNLOAD)).click();
			Thread.sleep(1000);
			
			boolean downFile = fileCheck(sharedFileList.get(0));
			if(!downFile) {
				failMsg = failMsg + "\n3. cannot find download file";
			} 
			
			//download 2
			
			fileList.get(1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DOWNLOAD)).click();
			Thread.sleep(500);
			
			downFile = fileCheck(sharedFileList.get(1));
			if(!downFile) {
				failMsg = failMsg + "\n4. cannot find download file";
			} 
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 21. 발표자 세미나 시작. 모든 다운로드 파일 삭제 후 빈화면 확인
	@Test(priority=21, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void startSeminar() throws Exception {
		String failMsg = "";
		
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(2000);
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		//check list
		List<WebElement> fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));

		if (sharedFileList.size() != fileList.size()) {
			failMsg = failMsg + "\n1. file count error(onair seminar) [Expected]" + (sharedFileList.size()) + " [Actual]"
					+ fileList.size();
		} else {
			for (int i = 0; i < sharedFileList.size(); i++) {
				if(!fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(i))){
					failMsg = failMsg + "\n2-" + i + ". file name [Expected]" + sharedFileList.get(i)
							+ " [Actual]" + fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
				}
			}
		}
		
		//delete all
		int listSize = fileList.size();
		
		for (int i = 0; i < listSize; i++) {
			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));

			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);
			driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN + "/button[1]")).click();
			Thread.sleep(500);
			sharedFileList.remove(0);
		}
		
		//발표자
		if (driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		if(isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = failMsg + "\n3. empty download file list message(presenter) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = failMsg + "\n4. cannot find empty list message.";
		}
		
		//운영자
		if (driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		if(isElementPresent(driver_organizer, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = failMsg + "\n5. empty download file list message(organizer) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = failMsg + "\n6. cannot find empty list message.";
		}
		
		//참석자
		if (driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		if(isElementPresent(driver_guest, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			if(!driver_guest.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText().contentEquals(MSG_DOWNLOADFILE_EMPTY)) {
				failMsg = failMsg + "\n7. empty download file list message(guest) [Expected]" + MSG_DOWNLOADFILE_EMPTY
						 + " [Actual]" + driver_guest.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY)).getText();
			}
		} else {
			failMsg = failMsg + "\n8. find cannot empty list message.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 발표자 파일등록
	@Test(priority=22, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void register_sharedFileOnAir() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		if (driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[0];
		
		driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
		Thread.sleep(500);
		
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_MODAL_BODY))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_ADD_DOWNLOADFILE_CONFIRM)) {
				failMsg = "1. add file confirm popup message [Expected]" + MSG_ADD_DOWNLOADFILE_CONFIRM
						 + " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//click cancel
			driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER+"/button[2]")).click();
			Thread.sleep(500);
			
		} else {
			failMsg = "0. cannot find add file confirm popup.";
		}
		
		//check list
		if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_DOWNLOAD_EMPTY))) {
			failMsg = failMsg + "\n2. file list error. cannot find empty message.";
		}
		
		List<WebElement> fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		for (int i = 0; i < CommonValues.TESTFILE_LIST.length; i++) {
			addedfile = filePath + CommonValues.TESTFILE_LIST[i];
			
			driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(1000);
			
			driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(1000);
			
			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if(fileList.size() == (i+1)) {
				if(fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(CommonValues.TESTFILE_LIST[i])) {
					
				}
				sharedFileList.add(CommonValues.TESTFILE_LIST[i]);
			} else {
				failMsg = failMsg + "\n3-" + i + ". download file list count error [Expected]" + (i+1) + " [Actual]" + fileList.size();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 23. 발표자 공유 invalid파일 등록_onair
	@Test(priority=23, dependsOnMethods = {"createSeminar"}, enabled = false)
	public void register_InvalidSharedFileOnAir() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		if(driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = "";
		for (int i = 0; i < CommonValues.TESTFILE_INVALID_LIST.length; i++) {
			addedfile = filePath + CommonValues.TESTFILE_INVALID_LIST[i];
			
			driver_presenter.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(500);
			
			if(isElementPresent(driver_presenter, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
				if(!driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(MSG_DOWNLOADFILE_INVALID)) {
					failMsg = failMsg + "\n0-" + i + ". invalid file message [Expected]" + MSG_DOWNLOADFILE_INVALID
							 + " [Actual]" + driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
				}
			} else {
				failMsg = failMsg + "\n0-" + i + ". cannot find toast.";
			}
			Thread.sleep(2000);
		}
		
		if(sharedFileList.size() != driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST)).size()) {
			failMsg = failMsg + "\n2. download file list count(after invalid action) [Expected]" + sharedFileList.size()
					 + " [Actual]" + driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST)).size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 24. 발표자 공유파일 삭제 onair
	@Test(priority=24, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void delete_SharedFileOnAir() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver_presenter);
		
		if(driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}	
		
		List<WebElement> fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		
		if(fileList.size() > 0) {
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);
			
			if(isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_CONFIRM_DIALOG))) {
				 if(!driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_DIALOG)).getText().contentEquals(MSG_DOWNLOADFILE_DELETE)) {
					 failMsg = failMsg + "\n1. delete popup message [Expected]" + MSG_DOWNLOADFILE_DELETE
							  + " [Actual]" + driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_DIALOG)).getText();
				 }
				 
				// cancel
				driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN+ "/button[2]")).click();
				Thread.sleep(500);
			} else {
				failMsg = failMsg + "0. cannot find delete popup.";
			}

			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if (sharedFileList.size() != fileList.size()) {
				failMsg = failMsg + "\n2. file count error(after cancel delete) [Expected]" + sharedFileList.size()
						+ " [Actual]" + fileList.size();
			}

			// delete
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);

			// cancel
			driver_presenter.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN + "/button[1]")).click();
			Thread.sleep(500);

			fileList = driver_presenter.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			if (sharedFileList.size()-1 != fileList.size()) {
				failMsg = failMsg + "\n3. file count error(after delete) [Expected]" + (sharedFileList.size()-1)
						+ " [Actual]" + fileList.size();
			} else {
				sharedFileList.remove(0);
			}
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 25. 운영자 공유파일 등록 삭제
	@Test(priority=25, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void organizer_SharedFileOnAir() throws Exception {
		String failMsg = "";
		
		//check file list
		if(driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_organizer.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
		
		if(sharedFileList.size() != fileList.size()) {
			failMsg = failMsg + "\n1. file count error(organizer) [Expected]" + (sharedFileList.size())
					+ " [Actual]" + fileList.size();
		} else {
			//add1
			String filePath = CommonValues.TESTFILE_PATH;
			if (System.getProperty("os.name").toLowerCase().contains("mac")) 
				filePath = CommonValues.TESTFILE_PATH_MAC;
			String addedfile = filePath + CommonValues.TESTFILE_LIST[0];
			
			driver_organizer.findElement(By.xpath(XPATH_ROOM_DOWNLOAD_ADDFILE)).sendKeys(addedfile);
			Thread.sleep(1000);
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER +"/button[1]")).click();
			Thread.sleep(500);
			
			fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			
			if(sharedFileList.size() + 1 != fileList.size()) {
				failMsg = failMsg + "\n2. file count error(organizer, add 1) [Expected]" + (sharedFileList.size()+1)
						+ " [Actual]" + fileList.size();
			} else {
				sharedFileList.add(CommonValues.TESTFILE_LIST[0]);
				
				if(!fileList.get(fileList.size()-1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(sharedFileList.size()-1))) {
					failMsg = failMsg + "\n3. added file name [Expected]" + sharedFileList.get(sharedFileList.size()-1)
							+ " [Actual]" + fileList.get(fileList.size()-1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
				}
			}
	
			//delete1
			fileList.get(1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DEL)).click();
			Thread.sleep(1000);
			
			driver_organizer.findElement(By.xpath(XPATH_ROOM_CONFIRM_BTN +"/button[1]")).click();
			Thread.sleep(500);
			sharedFileList.remove(1);
			fileList = driver_organizer.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));
			
			if(sharedFileList.size() != fileList.size()) {
				failMsg = failMsg + "\n4. file count error(organizer, delete1) [Expected]" + (sharedFileList.size())
						+ " [Actual]" + fileList.size();
			} else {
				//file list check
				for (int i = 0; i < sharedFileList.size(); i++) {
					if(!fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(i))){
						failMsg = failMsg + "\n5-" + i + ". file name [Expected]" + sharedFileList.get(i)
								+ " [Actual]" + fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
					}
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 26. guest 공유파일 확인 다운로드
	@Test(priority=26, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void guest_SharedFileOnAir() throws Exception {
		String failMsg = "";
		
		//check file list
		if (driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).getAttribute("class").contains("false")) {
			driver_guest.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_DOWNLOAD)).click();
			Thread.sleep(500);
		}

		List<WebElement> fileList = driver_guest.findElements(By.xpath(XPATH_ROOM_DOWNLOAD_LIST));

		if (sharedFileList.size() != fileList.size()) {
			failMsg = failMsg + "\n1. file count error(guest) [Expected]" + (sharedFileList.size()) + " [Actual]"
					+ fileList.size();
		} else {
			for (int i = 0; i < sharedFileList.size(); i++) {
				if(!fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText().contentEquals(sharedFileList.get(i))){
					failMsg = failMsg + "\n2-" + i + ". file name [Expected]" + sharedFileList.get(i)
							+ " [Actual]" + fileList.get(i).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_FILENAME)).getText();
				}
			}
			
			//download 1
			fileList.get(0).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DOWNLOAD)).click();
			Thread.sleep(1000);
			
			boolean downFile = fileCheck(sharedFileList.get(0));
			if(!downFile) {
				failMsg = failMsg + "\n3. cannot find download file";
			} 
			
			//download 2
			
			fileList.get(1).findElement(By.xpath(XPATH_ROOM_DOWNLOAD_LIST_BTN_DOWNLOAD)).click();
			Thread.sleep(500);
			
			downFile = fileCheck(sharedFileList.get(1));
			if(!downFile) {
				failMsg = failMsg + "\n4. cannot find download file";
			} 
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//31. 발표자, 운영자 onair룸 새로 입장 설정팝업 확인
	@Test(priority=31, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void settingpopup_onair_enter() throws Exception {
		String failMsg = "";
		
		//발표자 룸 나갔다가 재입장
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_BTN)).click();
		Thread.sleep(500);
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_POPUPBTN + "[2]")).click();
		Thread.sleep(500);
		
		if(!driver_presenter.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			failMsg = failMsg + "\n1. not detail view after exit room. current url : " + driver_presenter.getCurrentUrl();
			driver_presenter.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(1000);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver_presenter;
		js.executeScript("arguments[0].scrollIntoView();", driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(3000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		
		ArrayList<String> tabs2 = new ArrayList<String>(driver_presenter.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver_presenter.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = failMsg + "\n2. presenter cannot enter the room. current url : " + driver_presenter.getCurrentUrl();
			}
		} else {
			driver_presenter.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver_presenter.close();
			// switch room tab
			driver_presenter.switchTo().window(tabs2.get(1));
		}
		
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText().contentEquals(MSG_ROOM_SETTINGPOPUP_TITLE)) {
				failMsg = failMsg + "\n3. setting popup title [Expected]" + MSG_ROOM_SETTINGPOPUP_TITLE 
						+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText();
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_CAM))) {
				failMsg = failMsg + "\n4. cannot find camera menu.";
			}
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_MIC))) {
				failMsg = failMsg + "\n5. cannot find microphone menu.";
			}
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_SETTINGPOPUP_SPEAKER))) {
				failMsg = failMsg + "\n6. cannot find speaker menu.";
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN))) {
				failMsg = failMsg + "\n7. cannot find confirm button.";
			} else {
				if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).getText().contentEquals("Confirm")) {
					failMsg = failMsg + "\n8. setting popup button message [Expected]" + "Confirm" 
							+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).getText();
				}
				
				driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
				Thread.sleep(500);
			}
			
		} else {
			failMsg = "9. cannot find setting popup.";
		}
		
		//운영자 룸 나갔다가 재입장
		driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_BTN)).click();
		Thread.sleep(500);
		driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_POPUPBTN + "[2]")).click();
		Thread.sleep(500);
		
		if(!driver_organizer.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			failMsg = failMsg + "\n10. not detail view after exit room(organizer). current url : " + driver_presenter.getCurrentUrl();
			driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(1000);
		}
		
		js = (JavascriptExecutor) driver_organizer;
		js.executeScript("arguments[0].scrollIntoView();", driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(3000);
		
		tabs2 = new ArrayList<String>(driver_organizer.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver_organizer.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = failMsg + "\n11. organizer cannot enter the room. current url : " + driver_organizer.getCurrentUrl();
			}
		} else {
			driver_organizer.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver_organizer.close();
			// switch room tab
			driver_organizer.switchTo().window(tabs2.get(1));
		}
		
		if(isElementPresent(driver_organizer, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			failMsg = failMsg + "\n12. find setting popup.(organizer)";
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
			Thread.sleep(500);
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//32. 발표자, 운영자 onair룸 새로고침 팝업 확인
	@Test(priority=32, dependsOnMethods = {"enterRoom"}, enabled = true)
	public void settingpopup_refrash_onair() throws Exception {
		String failMsg = "";
		
		//발표자 refrash
		driver_presenter.navigate().refresh();
		Thread.sleep(2000);
		
		if(isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			if(!driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText().contentEquals(MSG_ROOM_SETTINGPOPUP_TITLE)) {
				failMsg = failMsg + "\n1. setting popup title [Expected]" + MSG_ROOM_SETTINGPOPUP_TITLE 
						+ " [Actual]" + driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE)).getText();
			}
			
			if(!isElementPresent(driver_presenter, By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN))) {
				failMsg = failMsg + "\n2. cannot find confirm button.";
			} else {
				driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
				Thread.sleep(500);
			}
			
		} else {
			failMsg = "0. cannot find setting popup.";
		}
		
		//운영자 refrash
		driver_organizer.navigate().refresh();
		Thread.sleep(2000);
		
		if(isElementPresent(driver_organizer, By.xpath(CommonValues.XPATH_ROOM_SETTING_TITLE))) {
			failMsg = failMsg + "\n3. find setting popup.(organizer)";
			
			driver_organizer.findElement(By.xpath(CommonValues.XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
			Thread.sleep(500);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}	
	
	public boolean fileCheck(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		boolean ret = false;
		int num = 10;
		
		String[] splitName = filename.split("\\.");
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {
			
			path = home + "\\Downloads\\" + splitName[0] + "." + splitName[1];
			File file = new File(path);

			if (file.exists()) {
				file.delete();
				return true;
			} else {
				while (num >= 0) {
					num--;
					path = home + "\\Downloads\\" + splitName[0] + " (" + num + ")." + splitName[1];
					file = new File(path);
					if (file.exists()) {
						file.delete();
						ret = true;
						break;
					}
				}
			}

		} else {
			path = home + "/Downloads/" + filename + ".xlsx";
			//todo...
		}
		return ret;
	}

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		driver_presenter.quit();
		driver_organizer.quit();
		driver_guest.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(WebDriver wd, By by) {
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
