package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

/* RoomPresentation : rsrsup1
 * 0. 세미나 만들기 및 룸 입장. 시작
 * 1. 페이지가 10장이상인 ppt 문서공유, 썸네일 스크롤 젤 끝으로 이동후 선택. 페이지 정상인지 확인
 */

public class RoomPresentation {

	public static String PPT_FILE = "31p.pptx";
	
	public static String XPATH_CONTENT_DOC = "//div[@id='document-viewer-wrap']/div[@id='document-viewer-render']";
	public static String ATTRIBUTE_DOC_FILENAME = "data-filename";
	public static String XPATH_CONTENT_YOUTUBE = "//div[@id='youtube-viewer']/iframe[@id='youtube-player']";
	public static String XPATH_CONTENT_YOUTUBE_CONTROLLER = "//div[@class='viewer-wrap']/div[2]";
	public static String ATTRIBUTE_YOUTUBE_CONTROLLER_ATTENDEE = "controller attendee"; // class name
	public static String ATTRIBUTE_YOUTUBE_CONTROLLER_OPERATOR = "controller operator";
	public static String ATTRIBUTE_YOUTUBE_CONTROLLER_PRESENTER = "controller speaker";
	public static String XPATH_SCREENSHARE_OWNER = "//div[@id='screen-owner-explain']/strong";
	public static String XPATH_SCREENSHARE_VIEWER = "//div[@id='screen-viewer']/video";
	public static String XPATH_SCREENSHARE_STOP_BTN = "//div[@id='screen-owner-explain']//button";
	public static String XPATH_ROOM_INTRO_VIEW = "//div[@id='intro-viewer']";
	
	public static String MSG_SCREENSHARE_OWNER = "Screen sharing.";
	
	public static WebDriver driver_publisher;
	public static WebDriver driver_presenter;
	public static WebDriver driver_organizer;
	public static WebDriver driver_guest;
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String seminarTitle = "";
	public String seminarID = "";
	public String seminarLink = "";
	
	public List<String> Presenters;
	public List<String> Organizers;
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver_publisher = comm.setDriver(driver_publisher, browsertype, "lang=en_US");
		driver_presenter = comm.setDriver(driver_presenter, browsertype, "lang=en_US", true);
		driver_organizer = comm.setDriver(driver_organizer, browsertype, "lang=en_US");
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver_publisher);
		context.setAttribute("webDriver2", driver_presenter);
		context.setAttribute("webDriver3", driver_organizer);
		context.setAttribute("webDriver4", driver_guest);
		
		driver_publisher.get(CommonValues.SERVER_URL);
		
		Presenters = new ArrayList<>(Arrays.asList("rsrsup8@gmail.com", "rsrsup12@gmail.com", "rsrsup11@gmail.com"));
		Organizers = new ArrayList<>(Arrays.asList("rsrsup3@gmail.com", "rsrsup6@gmail.com"));
		
        System.out.println("End BeforeTest!!!");
	}
	
	// 0. 세미나 만들기 .
	@Test(priority=0)
	public void createSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_publisher, "rsrsup2@gmail.com");
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_RoomTest";
		
		// goto Create seminar
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver_publisher.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver_publisher.getCurrentUrl().contains(createViewUri)) {
			failMsg = "1. not create view" + driver_publisher.getCurrentUrl();
			driver_publisher.get(createViewUri);
		}
		comm.setCreateSeminar_setChannel(driver_publisher);
		comm.setCreateSeminar(driver_publisher, seminarTitle, true);
		comm.setCreateSeminar_setMember(driver_publisher, Presenters, Organizers);
		
		// save seminar
		driver_publisher.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver_publisher, seminarTitle);
		Thread.sleep(500);

		// post seminar
		comm.postSeminar(driver_publisher, seminarID);

	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 1. 발표자1, 운영자1, 참석자1 룸입장. 세미나 시작
	@Test(priority=1, enabled = true)
	public void enterRoom() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		
		//발표자 입장
		comm.loginseminar(driver_presenter, Presenters.get(0));
		driver_presenter.get(detailview);
		Thread.sleep(1000);
		JavascriptExecutor js = (JavascriptExecutor) driver_presenter;
		js.executeScript("arguments[0].scrollIntoView();", driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_presenter.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);
		
		closeAlertAndGetItsText_webdriver(driver_presenter);
		
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
		comm.loginseminar(driver_organizer, Organizers.get(0));
		driver_organizer.get(detailview);
		Thread.sleep(1000);
		js = (JavascriptExecutor) driver_organizer;
		js.executeScript("arguments[0].scrollIntoView();", driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)));
		// click enter(new tab)
		driver_organizer.findElement(By.xpath(AttendeesTest.XPATH_VIEW_JOIN)).click();
		Thread.sleep(2000);
		closeAlertAndGetItsText_webdriver(driver_organizer);
		
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
		
		closeAlertAndGetItsText_webdriver(driver_guest);
		
		//check room uri
		if(!roomuri.equalsIgnoreCase(driver_guest.getCurrentUrl())){
			failMsg = failMsg + "\n2. guest cannot enter the room. current url : " + driver_guest.getCurrentUrl();
	    }
		
		// 발표자 세미나 시작
		comm.checkSettingpopup(driver_presenter);
		
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(2000);
		driver_presenter.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 11. 페이지가 10장이상인 ppt 문서공유, 썸네일 스크롤 젤 끝으로 이동후 선택. 페이지 정상인지 확인
	@Test(priority=11, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void loadDoc() throws Exception {
		String failMsg = "";
		
		//click doc icon
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
		
		List<WebElement> docitems = driver_presenter.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
		int doccount = docitems.size();
		//upload file
		Thread.sleep(100);
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + PPT_FILE;
		driver_presenter.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(3000);
		driver_presenter.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//check added file
		docitems = driver_presenter.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
		if(doccount+1 != docitems.size()) {
			failMsg = failMsg + "1. added file count error [Expected] " + doccount+1 + " [Actual]" + docitems.size();

		} else {
			docitems.get(doccount).findElement(By.xpath("./div[@class='btn-rect']")).click();
			Thread.sleep(10000);
			
			List<WebElement> docPage = driver_presenter.findElements(By.xpath("//div[@id='document-viewer']//li"));
			
			JavascriptExecutor js = (JavascriptExecutor) driver_presenter;
			js.executeScript("arguments[0].scrollIntoView(true);", docPage.get(docPage.size()-1));
			Thread.sleep(1000);
			
			for (int i = 0; i < 10; i++) {
				docPage = driver_presenter.findElements(By.xpath("//div[@id='document-viewer']//li"));
				if(docPage.size() != 31) {
					Thread.sleep(1000);
				}
			}
			if(docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src").isEmpty()
					|| !docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src").contains("decrypt?filePath=")) {
				failMsg = failMsg + "2. doc file thumbnail error [Actual : path]"
					+ docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src");
			}
			Thread.sleep(1000);
			docPage.get(docPage.size()-2).click();
			Thread.sleep(500);
			
			if(driver_presenter.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").isEmpty()
					|| !driver_presenter.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("decrypt?filePath=")) {
				failMsg = failMsg + "\n3. doc file contents error [Actual : path]"
					+ docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src");
			}
			
			
			//발표자 문서공유 확인
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_CONTENT_DOC))) {
				failMsg = failMsg + "\n4. cannot find document content(presenter).";
			} else {
				if(!driver_presenter.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME).contentEquals(PPT_FILE)) {
					failMsg = failMsg + "\n5. content area filename [Expected]" + PPT_FILE
							 + " [Actual]" + driver_presenter.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME);
				}
			}
			
			//운영자 문서공유 확인
			if(!isElementPresent(driver_organizer, By.xpath(XPATH_CONTENT_DOC))) {
				failMsg = failMsg + "\n6. cannot find document content(organizer).";
			} else {
				if(!driver_organizer.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME).contentEquals(PPT_FILE)) {
					failMsg = failMsg + "\n7. content area filename(organizer) [Expected]" + PPT_FILE
							 + " [Actual]" + driver_organizer.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME);
				}
			}
			
			//참석자 문서공유 확인
			if(!isElementPresent(driver_guest, By.xpath(XPATH_CONTENT_DOC))) {
				failMsg = failMsg + "\n8. cannot find document content(guest).";
			} else {
				if(!driver_guest.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME).contentEquals(PPT_FILE)) {
					failMsg = failMsg + "\n9. content area filename(guest) [Expected]" + PPT_FILE
							 + " [Actual]" + driver_guest.findElement(By.xpath(XPATH_CONTENT_DOC)).getAttribute(ATTRIBUTE_DOC_FILENAME);
				}
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. 유튜브공유,
	@Test(priority=21, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void shareYoutube() throws Exception {
		String failMsg = "";
		
		//click doc icon
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();

		if(!isElementPresent(driver_presenter, By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_BTN))) {
			driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
			Thread.sleep(500);
		}
		
		//click +
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_BTN)).click();
		Thread.sleep(500);
		//input youtube url
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_URL_BOX)).clear();
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_URL_BOX)).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver_presenter.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(3000);
		
		//check added item
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		List<WebElement> youtubeitems = driver_presenter.findElements(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));

		if (youtubeitems.size() != 1) {
			failMsg = "0. fail to register youtube.";
		} else {
			youtubeitems.get(0).click();
			Thread.sleep(3000);
			
			//발표자 유튜브공유 확인
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_CONTENT_YOUTUBE))) {
				failMsg = failMsg + "\n1. fail to share youtube.";
			} else {
				if(!driver_presenter.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute("class").contentEquals(ATTRIBUTE_YOUTUBE_CONTROLLER_PRESENTER)) {
					failMsg = failMsg + "\n2. cannot find speakers's menu. : " + driver_presenter.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute("class");
				}
			}
			
			
			//운영자 유튜브공유 확인
			if(!isElementPresent(driver_organizer, By.xpath(XPATH_CONTENT_YOUTUBE))) {
				failMsg = failMsg + "\n3. fail to share youtube.(organizer)";
			} else {
				if(!driver_organizer.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute("class").contentEquals(ATTRIBUTE_YOUTUBE_CONTROLLER_OPERATOR)) {
					failMsg = failMsg + "\n4. cannot find attendee's menu.organizer : " + driver_organizer.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute("class");
				}
			}
			
			//참석자 유튜브공유 확인
			if(!isElementPresent(driver_guest, By.xpath(XPATH_CONTENT_YOUTUBE))) {
				failMsg = failMsg + "\n5. fail to share youtube.(guest)";
			} else {
				if(!driver_guest.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute("class").contentEquals(ATTRIBUTE_YOUTUBE_CONTROLLER_ATTENDEE)) {
					failMsg = driver_guest + "\n6. cannot find guest's menu.guest : driver_guest.findElement(By.xpath(XPATH_CONTENT_YOUTUBE_CONTROLLER)).getAttribute(\"class\")";
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 31. 화면공유
	@Test(priority=31, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void shareScreen() throws Exception {
		String failMsg = "";
		
		driver_presenter.findElement(By.xpath(OnAirRoom.XPATH_ROOM_SCREEN_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(driver_presenter, By.xpath(XPATH_SCREENSHARE_OWNER))) {
			failMsg = "1. fail to screen share(presenter)";
		} else {
			if(!driver_presenter.findElement(By.xpath(XPATH_SCREENSHARE_OWNER)).getText().contentEquals(MSG_SCREENSHARE_OWNER)) {
				failMsg = failMsg + "\n2. screen share messsage(presenter) [Expeced]" + MSG_SCREENSHARE_OWNER
						 + " [Actual] + driver_presenter.findElement(By.xpath(XPATH_SCREENSHARE_OWNER)).getText()";
			}
			
			//운영자 화면공유 화면 확인
			if(!isElementPresent(driver_organizer, By.xpath(XPATH_SCREENSHARE_VIEWER))) {
				failMsg = failMsg + "\n4. cannot find screen share view(organizer)";
			}
			
			//참석자 화면공유 화면 확인
			if(!isElementPresent(driver_guest, By.xpath(XPATH_SCREENSHARE_VIEWER))) {
				failMsg = failMsg + "\n5. cannot find screen share view(guest)";
			}
			
			//stop to screen share
			driver_presenter.findElement(By.xpath(XPATH_SCREENSHARE_STOP_BTN)).click();
			Thread.sleep(1000);
			
			//발표자 대기화면 확인
			if(!isElementPresent(driver_presenter, By.xpath(XPATH_ROOM_INTRO_VIEW))) {
				failMsg = failMsg + "\n6. cannot find intro view(organizer)";
			}
			
			//발표자 대기화면 확인
			if(!isElementPresent(driver_organizer, By.xpath(XPATH_ROOM_INTRO_VIEW))) {
				failMsg = failMsg + "\n7. cannot find intro view(organizer)";
			}
			
			//발표자 대기화면 확인
			if(!isElementPresent(driver_guest, By.xpath(XPATH_ROOM_INTRO_VIEW))) {
				failMsg = failMsg + "\n8. cannot find intro view(guest)";
			}

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}
	
	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}	

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver_publisher.quit();
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
			driver_publisher.switchTo().alert();
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
		} catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
			return e.getMessage();
		} finally {
			acceptNextAlert = true;
		}
	}

}
