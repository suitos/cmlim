package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* OnAirRoom
 * 0.세미나 생성
 * 1. 게시자겸 발표자 세미나 룸입장, 세미나 시작
 * 2. 문서 파일 확인 (툴팁포함), 삭제 (사용안함. spec out)
 * 3. 발표자 onair 룸에서 발표문서 추가 (사용안함. spec out)
 * 4. 발표자 onair 룸에서 발표문서 추가 초과(10개 초과) (사용안함. spec out)
 * 5. 발표자 onair 룸에서 유튜브 확인 삭제
 * 6. 발표자 onair 룸에서 유튜브 추가 : invalid case
 * 7. 발표자 onair 룸에서 유튜브 추가
 * 8. 발표자  onair 룸에서 유튜브 추가 최대(10개)
 * 
 * 8. 참석자 A 입장
 * 9. 참석자 A : onair 화면 확인
 * 8. 참석자 B 입장
 * 
 * part2
 * 10. 발표자, 참석자A, 참석자 B : 채팅 기본(한줄씩)
 * 
 * part3
 * 21. 참석자A 질믄 입력, 발표자, 참석자B 질문 뱃지확인 
 * 22. 참석자B 질믄 입력 (참석자A,참석자B 질믄), 참석자A가 본인 질문 삭제
 * 23. 참석자A 질믄 입력 (참석자A 질믄,참석자B), 발표자가  참석자B 질문 삭제
 * 24. 참석자B 비공개 질믄 입력 (참석자A 공개1 ,참석자B 비공개1)
 * 25. 참석자A 비공개1 ,참석자B 비공개1. 참석자A가 비공개 질문 삭제
 * 26. 참석자A 비공개1 ,참석자B 비공개1. 발표자가 참석자B 비공개 질문 삭제
 * 
 * 30. 참석자A 비공개1, 참석자A 공개질믄 입력. 발표자가 공개질문에 답변 작성
 * 31. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 비공개질문에 답변 작성
 * 32. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 공개질문 답변 삭제 ( 공개, 비공개 순)
 * 33. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 비공개질문 답변 삭제 ( 공개, 비공개 순)
 * 
 * 100. 세미나 종료하기
 */
public class OnAirRoom {

	public static String XPATH_ROOM_SCREEN_BTN = "//div[@id='presentation-buttons']/section[1]";
	public static String XPATH_ROOM_CAM_BTN = "//div[@id='presentation-buttons']/section[2]";
	public static String XPATH_ROOM_DOC_BTN = "//div[@id='presentation-buttons']/section[4]";
	public static String XPATH_ROOM_DOC_UPLOAD = "//input[@id='doc-upload-input']";
	public static String XPATH_ROOM_YUTUBE_BTN = "//div[@id='presentation-buttons']/section[3]";
	public static String XPATH_ROOM_YUTUBEADD_BTN = "//button[@class='btn-rect plus']";
	public static String XPATH_ROOM_YUTUBEADD_URL_BOX = "//input[@class='url-input']";
	public static String XPATH_ROOM_DOC_ICON = "//li[@class='document-item']";
	public static String XPATH_ROOM_YUTUBE_ICON = "//li[@class='youtube-item']";
	public static String XPATH_ROOM_TOOLTIP = "//div[@class='wait-tooltip tooltip-inner']";
	public static String XPATH_ROOM_TOAST = "//div[@class='wrap-toast-outer']";
	
	public static String XPATH_ROOM_TAB_DOWNLOAD = "//div[@id='timeline-viewmode']/button[3]";
	public static String XPATH_ROOM_TAB_QNA = "//div[@id='timeline-viewmode']/button[2]";
	public static String XPATH_ROOM_TAB_CHAT = "//div[@id='timeline-viewmode']/button[1]";
	public static String XPATH_ROOM_TAB_QNA_BEDGE = "//div[@id='timeline-viewmode']/button[2]//div[@class='count-bedge']";
	public static String XPATH_ROOM_QNA_LIST = "//li[@class='Qna_qna-list-item__hId6u ']";
	
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	public static String MSG_DELETE_QUESTION = "Do you want to delete the question?";
	public static String MSG_DELETE_ANSWER = "Do you want to delete the answer?";

	public String DES_FILE = "tooltip test : File!";
	public String DES_YOUTUBE = "tooltip test2 : Youtube!";
	
	public String QNA_QUESTION_PUBLIC = "QnA Test : public";
	public String QNA_QUESTION_PRIVATE = "QnA Test : private";
	public String QNA_ANSWER = "QnA Test : public answer";	
	
	public static WebDriver driver;
	public static WebDriver attendADriver;
	public static WebDriver attendBDriver;
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarID = "";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US", true);
		attendADriver = comm.setDriver(attendADriver, browsertype, "lang=en_US");
		attendBDriver = comm.setDriver(attendBDriver, browsertype, "lang=en_US");

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", attendADriver);
		context.setAttribute("webDriver3", attendBDriver);

		//login rsrsup1
		comm.loginseminar(driver);
		System.out.println("End BeforeTest!!!");
	}
	
	public String createSeminar(String seminarName, String seminarTime) throws Exception {

		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(seminarName);

		driver.findElement(By.name("startTime")).sendKeys(seminarTime);
		Thread.sleep(2000);

		
		//presentation tab
		String presentationurl = CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI ;
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB4)).click();

		Thread.sleep(1000);
		if (!driver.getCurrentUrl().contains(presentationurl)) {
			Exception e = new Exception("no presentation View : " + driver.getCurrentUrl());
			throw e;
		}
		//add doc(png)
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String testDoc = filePath + CommonValues.TESTFILE_PDFS[0];
		driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testDoc);

		//add youtube
		driver.findElement(By.id("input-youtube-link")).clear();
		driver.findElement(By.id("input-youtube-link")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//input[@id='input-youtube-link']/following::button[1]")).click();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='DocItem_doc-item__2bSNb']//textarea[@class='input-content']")));
		} catch (Exception e) {
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		//add file, youtube description
		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));

		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).clear();
		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).sendKeys(DES_FILE);

		docs_before.get(1).findElement(By.xpath(".//textarea[@class='input-content']")).clear();
		docs_before.get(1).findElement(By.xpath(".//textarea[@class='input-content']")).sendKeys(DES_YOUTUBE);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='DocItem_doc-item__2bSNb']//textarea[@class='input-content']")));
		} catch (Exception e) {
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		Thread.sleep(7000);
		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).click();
		// save seminar
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();

		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ListTest.XPATH_LIST_TAB_SAVED)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		Thread.sleep(1000);
		
		String seminarUri = "";
		CommonValues comm = new CommonValues();
		seminarUri = comm.findSeminarIDInList(driver, seminarName);
		
		// post seminar
		comm.postSeminar(driver, seminarUri);
		
		if (seminarUri.isEmpty()) {
			Exception e = new Exception("fail to create seminar : " + driver.getCurrentUrl());
			throw e;
		}
		return seminarUri;
	}
	
	//0.세미나 생성
	@Test(priority = 0)
	public void CreateOnAirSeminar() throws Exception {
		Date time = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 5);
		SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat ( "HH:mm:ss");
		String time1 = format1.format(cal.getTime());
		
		//create seminar after 10minute
		seminarID = createSeminar("OnAir " + format2.format(cal.getTime()), time1);
	}
	
	//1. 게시자겸 발표자 세미나 룸입장, 세미나 시작
	@Test(priority = 1, dependsOnMethods = {"CreateOnAirSeminar"}, enabled = true)
	public void SeminarRoom_Pres() throws Exception {
		
		String failMsg = "";
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailview);
		Thread.sleep(2000);
		//click join button
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_EXIT_BTN)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		if(tabs2.size() == 2) {
			//close 1tab 
	    	driver.switchTo().window(tabs2.get(0));
			driver.close();
			driver.switchTo().window(tabs2.get(1));
			
			Thread.sleep(2000);
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			//check room uri
			if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
		    {
				failMsg = "fail to join Seminar : " + driver.getCurrentUrl();
		    } 

		}
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);

	    //start seminar
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//2. 문서 파일 확인 (툴팁포함), 삭제
	@Test(priority = 2, dependsOnMethods = { "SeminarRoom_Pres" }, alwaysRun = true,  enabled = true)
	public void doc_checkDelete() throws Exception {
		
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//click doc icon
		driver.findElement(By.xpath(XPATH_ROOM_DOC_BTN)).click();
		Thread.sleep(100);
		
		if(isElementPresent(By.xpath(XPATH_ROOM_DOC_ICON))){
			
			//cannot click
			if(driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON)).isSelected())
				failMsg = "1. doc item icon is enable.";
			
			//icon
			if(!driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//i[1]")).getAttribute("class").contentEquals("pdf icon")) {
				failMsg = failMsg + "\n 2. doc item icon [Expected]pdf, [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//i[1]")).getAttribute("class");
			}
			
			//title
			if(!driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText().contains(CommonValues.TESTFILE_PDFS[0])) {
				failMsg = failMsg + "\n 3. doc item title [Expected]" + CommonValues.TESTFILE_PDFS[0] + " [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText();
			}
			
			Actions actions = new Actions(driver);
			  
			// mouse hover
			WebElement web = driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(500);
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("cannot find element : " + e.getMessage());
			}
			
			//툴팁 확인
			if(isElementPresent(By.xpath(XPATH_ROOM_TOOLTIP))){
				if(!driver.findElement(By.xpath(XPATH_ROOM_TOOLTIP)).getText().contentEquals(DES_FILE)) {
					failMsg = failMsg + "\n 4. tooltip msg [Expected]" + DES_FILE 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_TOOLTIP)).getText();
				}
			} else {
				failMsg = failMsg + "\n 7. cannot find tooltip";
			}
			
			// 삭제 - cancel
			driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[2]")).click();
			Thread.sleep(500);
			
			//click doc btn again
			driver.findElement(By.xpath(XPATH_ROOM_DOC_BTN)).click();
			
					
			// mouse hover
			web = driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON  + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[1]")).click();
			Thread.sleep(500);
			
			if (!driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText()
					.contentEquals(CommonValues.SEMINAR_ROOM_DELETE_TOAST)) {
				failMsg = failMsg + "\n 5. toast message after delete item : [Actual] "
						+ driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText();
			} else {
				Thread.sleep(2000);
			}
			
		} else {
			failMsg = failMsg + "\n 8. cannot fild doc item";
		}
		
		//delete item check
		//click doc button again
		driver.findElement(By.xpath(XPATH_ROOM_DOC_BTN)).click();
		if(isElementPresent(By.xpath(XPATH_ROOM_DOC_ICON))){
			failMsg = failMsg + "\n 6. find doc item after delete. file name : "
					+ driver.findElement(By.xpath(XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 발표자 onair 룸에서 발표문서 추가
	@Test(priority = 3, dependsOnMethods = { "doc_checkDelete" }, alwaysRun = true, enabled = true)
	public void doc_addFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		if(!isElementPresent(By.xpath(XPATH_ROOM_DOC_UPLOAD))) {
			//click doc icon
			driver.findElement(By.xpath(XPATH_ROOM_DOC_BTN)).click();
		}
		
		List<WebElement> docitems = driver.findElements(By.xpath(XPATH_ROOM_DOC_ICON));
		int doccount = docitems.size();
		//upload file
		Thread.sleep(100);
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_PDFS[1];
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//check added file
		docitems = driver.findElements(By.xpath(XPATH_ROOM_DOC_ICON));
		if(doccount+1 == docitems.size()) {
			if(!docitems.get(doccount).findElement(By.xpath(".//div[@class='title']")).getText().contains(CommonValues.TESTFILE_PDFS[1])) {
				failMsg = "1. added file title error [Expected] " + CommonValues.TESTFILE_LIST[3]+ " [Actual]" + docitems.get(doccount).findElement(By.xpath("/div[@class='title']")).getText();
			}
			
		} else {
			failMsg = failMsg + "2. added file count error [Expected] " + doccount+1 + " [Actual]" + docitems.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 4. 발표자 onair 룸에서 발표문서 추가 초과(10개 초과)
	@Test(priority = 4, dependsOnMethods = { "doc_addFile" }, enabled = true)
	public void doc_addFileMax() throws Exception {
		//기획안됨
	}
	
	
	// 5. 발표자 onair 룸에서 유튜브 확인 삭제
	@Test(priority = 5, dependsOnMethods = { "doc_addFile" }, alwaysRun = true, enabled = true)
	public void youtube_checkDelete() throws Exception {
		
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//click youtube icon
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		Thread.sleep(100);
		
		if(isElementPresent(By.xpath(XPATH_ROOM_YUTUBE_ICON))){
			
			//cannot click
			if(driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON)).isSelected())
				failMsg = "1. youtube item icon is enable.";
			
			//title
			if(!driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON + "//div[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[0])) {
				failMsg = failMsg + "\n 2. youtube item title [Expected]" + CommonValues.YOUTUBE_TITLE[0] 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON + "//div[@class='title']")).getText();
			}
			Actions actions = new Actions(driver);
			
			//툴팁 확인
			WebElement web = driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			try {
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("cannot find element : " + e.getMessage());
			}
			
			//툴팁 확인
			if(isElementPresent(By.xpath(XPATH_ROOM_TOOLTIP))){
				if(!driver.findElement(By.xpath(XPATH_ROOM_TOOLTIP)).getText().contentEquals(DES_YOUTUBE)) {
					failMsg = failMsg + "\n3. tooltip msg [Expected]" + DES_YOUTUBE 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_TOOLTIP)).getText();
				}
			} else {
				failMsg = failMsg + "\n4. cannot find tooltip";
			}
			
			// mouse hover. delete - cancel
			web = driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[2]")).click();
			Thread.sleep(500);
			
			//click youtube icon again
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
			
			//delete - yes
			
			// mouse hover
			web = driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[1]")).click();
			Thread.sleep(500);
			
			
			if (!driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText()
					.contentEquals(CommonValues.SEMINAR_ROOM_DELETE_TOAST)) {
				failMsg = failMsg + "\n 3. toast message after delete item : [Actual] "
						+ driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText();
			}
			
			Thread.sleep(2000);
			
		} else {
			failMsg = "7. cannot fild doc item";
		}
		
		//delete item check
		//click youtube icon again
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		if(isElementPresent(By.xpath(XPATH_ROOM_YUTUBE_ICON))){
			failMsg = failMsg + "\n 4. find doc item after delete. file name : "
					+ driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_ICON + "//div[@class='title']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 6. 발표자 onair 룸에서 유튜브 추가 : invalid case
	@Test(priority = 6, dependsOnMethods = { "youtube_checkDelete" }, alwaysRun = true, enabled = true)
	public void youtube_add1() throws Exception {
		String failMsg = "";
		
		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		if(!isElementPresent_wd(driver, By.xpath(XPATH_ROOM_YUTUBEADD_BTN))) {
			//click youtube icon
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		}
		
		
		if(!isElementPresent(By.xpath(XPATH_ROOM_YUTUBEADD_BTN))) {
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
			Thread.sleep(500);
		}
		
		//기존 youtube icon 확인	
		List<WebElement> youtubeitems = driver.findElements(By.xpath(XPATH_ROOM_YUTUBE_ICON));
		int youtubecount = youtubeitems.size();
		
		//click +
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_BTN)).click();
		
		Thread.sleep(500);
		
		//empty url
		clearAttributeValue(driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)));
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).clear();
		Thread.sleep(500);
		//confirm 버튼 : disabled
		if (driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).isEnabled()) {
			failMsg = failMsg + "1. cinfirm button is enabled (youtube link empty)";
		} 
		Thread.sleep(2000);
		
		//invalid url
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).clear();
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).sendKeys("invalid URL test");
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='wrap-toast-outer']")));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		Thread.sleep(500);
		// toast
		if (!driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText()
				.contentEquals(CommonValues.SEMINAR_ROOM_URL_INVALID_TOAST)) {
			failMsg = failMsg + "\n 2. toast message after input invalid url : [Actual] "
					+ driver.findElement(By.xpath("//div[@class='wrap-toast-outer']")).getText();
		}
		Thread.sleep(2000);
		
		// input youtube url and click cancel
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).clear();
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).sendKeys(CommonValues.YOUTUBE_URL[3]);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[2]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		
		youtubeitems = driver.findElements(By.xpath(XPATH_ROOM_YUTUBE_ICON));

		if (youtubecount != youtubeitems.size()) {
			failMsg = failMsg + "\n 3. youtube count error adding cancellation [Expected] " + youtubecount + " [Actual]" + youtubeitems.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//7. 발표자 onair 룸에서 유튜브 추가
	@Test(priority = 7, dependsOnMethods = { "youtube_add1" }, alwaysRun = true, enabled = true)
	public void youtube_add2() throws Exception {
		String failMsg = "";
				
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//click youtube icon
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		
		if(!isElementPresent(By.xpath(XPATH_ROOM_YUTUBEADD_BTN))) {
			driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> youtubeitems = driver.findElements(By.xpath(XPATH_ROOM_YUTUBE_ICON));
		int youtubecount = youtubeitems.size();
		
		//click +
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_BTN)).click();
		Thread.sleep(500);
		//input youtube url
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).clear();
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBEADD_URL_BOX)).sendKeys(CommonValues.YOUTUBE_URL[3]);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_TOAST)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		Thread.sleep(500);
		
		if (!driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText()
				.contentEquals(CommonValues.SEMINAR_ROOM_ADD_TOAST)) {
			failMsg = failMsg + "\n 1. toast message after add youtube item : [Actual] "
					+ driver.findElement(By.xpath(XPATH_ROOM_TOAST)).getText();
		} 
		Thread.sleep(2000);
	
		//check added item
		driver.findElement(By.xpath(XPATH_ROOM_YUTUBE_BTN)).click();
		
		youtubeitems = driver.findElements(By.xpath(XPATH_ROOM_YUTUBE_ICON));

		if (youtubecount + 1 == youtubeitems.size()) {
			if (!youtubeitems.get(youtubecount).findElement(By.xpath(".//div[@class='title']")).getText()
					.contains(CommonValues.YOUTUBE_TITLE[3])) {
				failMsg = "1. added youtube title error [Expected] " + CommonValues.YOUTUBE_TITLE[3] + " [Actual]"
						+ youtubeitems.get(youtubecount).findElement(By.xpath("/div[@class='title']")).getText();
			}

		} else {
			failMsg = failMsg + "\n 2. added youtube count error [Expected] " + youtubecount + 1 + " [Actual]" + youtubeitems.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 8. 발표자  onair 룸에서 유튜브 추가 최대(10개)
	@Test(priority = 8, dependsOnMethods = { "youtube_add2" }, alwaysRun = true, enabled = true)
	public void youtube_max() throws Exception {
		String failMsg = "";
		
		//기획후에...
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 8. 참석자 A 입장
	@Test(priority = 8, dependsOnMethods = { "youtube_add2" }, alwaysRun = true,  enabled = true)
	public void enterSeminar_attendA() throws Exception {
		
		//seminar url
		String seminarurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
		
		attendADriver.get(seminarurl);
		Thread.sleep(2000);
		
		//click join
		attendADriver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		WebDriverWait wait = new WebDriverWait(attendADriver, 10);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)));
		} catch (Exception e) {
			System.out.println("cannot find element " + e.getMessage());
		}
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).click();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys(ATTENDEES_NICKNAME + "1");
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).click();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).sendKeys(ATTENDEES_EMAIL);
		
		attendADriver.findElement(By.xpath("//input[@name='companyName']")).clear();
		attendADriver.findElement(By.xpath("//input[@name='jobPosition']")).clear();
		attendADriver.findElement(By.xpath("//input[@name='phone']")).clear();
		
		if(!attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//div")).click();
		if(!attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//div")).click();
		
		//join seminar
		((JavascriptExecutor) attendADriver).executeScript("arguments[0].scrollIntoView(true);"
				, attendADriver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")));

		attendADriver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")).click();
		Thread.sleep(3000);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendADriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("USER A fail to join Seminar : " + attendADriver.getCurrentUrl());
	    	throw e;
	    }
		
	}
	
	// 9. 참석자 A : onair 화면 확인
	@Test(priority = 9, dependsOnMethods = { "youtube_add2" }, alwaysRun = true, enabled = true)
	public void onAirRoomBasic_attendA() throws Exception {
		String failMsg = "";
		
		//label..
		String xpath_onair = "//strong[@id='user-type']";
		if(!attendADriver.findElement(By.xpath(xpath_onair)).getAttribute("class").contains("onair")) {
			failMsg = "1. UserA room label : [Actual]" + attendADriver.findElement(By.xpath(xpath_onair)).getAttribute("class");
		}
		
		//tab..chat, qna, download
		if (!isElementPresent(By.xpath("//button[@class='chat active']"))) {
			failMsg = failMsg + "\n 4. UserA chat tab error.";
		}
		if (!isElementPresent(By.xpath("//button[@class='qna false']"))) {
			failMsg = failMsg + "\n 5. UserA chat tab error.";
		}
		if (!isElementPresent(By.xpath("//button[@class='download false']"))) {
			failMsg = failMsg + "\n 6. UserA chat tab error.";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 8. 참석자 B : standby 입장
	@Test(priority = 8, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void enterSeminar_attendB() throws Exception {
		
		//seminar url
		String seminarurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
		
		attendBDriver.get(seminarurl);
		Thread.sleep(1000);
		
		//click join
		attendBDriver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);
		
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).click();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).clear();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys(ATTENDEES_NICKNAME + "2");
		
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).click();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).clear();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).sendKeys("qa@test.com");
		
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_COMPANY)).clear();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_POSITION)).clear();
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_PHONE)).clear();
		
		if(!attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//div")).click();
		if(!attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//div")).click();
		
		//join seminar
		((JavascriptExecutor) attendBDriver).executeScript("arguments[0].scrollIntoView(true);"
				, attendBDriver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")));

		attendBDriver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']")).click();
		Thread.sleep(3000);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendBDriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("USER B fail to join Seminar : " + attendBDriver.getCurrentUrl());
	    	throw e;
	    }
		
	}	

	// 10. 발표자, 참석자A, 참석자 B : 채팅 기본(한줄씩)
	@Test(priority = 10, dependsOnMethods = { "youtube_add2" }, alwaysRun = true, enabled = true)
	public void chattingBasic() throws Exception {
		String failMsg = "";
		
		//send by presenter
		driver.findElement(By.xpath("//textarea[@class='chat-text']")).click();
		driver.findElement(By.xpath("//textarea[@class='chat-text']")).sendKeys("hello Iam King!");
		driver.findElement(By.xpath("//div[@id='chat-input-wrap']/button[1]")).click();
		Thread.sleep(500);
		// send by A
		attendADriver.findElement(By.xpath("//textarea[@class='chat-text']")).click();
		attendADriver.findElement(By.xpath("//textarea[@class='chat-text']")).sendKeys("hello Iam A1");
		attendADriver.findElement(By.xpath("//div[@id='chat-input-wrap']/button[1]")).click();
		Thread.sleep(500);
		// send by B
		attendBDriver.findElement(By.xpath("//textarea[@class='chat-text']")).click();
		attendBDriver.findElement(By.xpath("//textarea[@class='chat-text']")).sendKeys("hello Iam B2");
		attendBDriver.findElement(By.xpath("//textarea[@class='chat-text']")).sendKeys(Keys.ENTER);
		
		Thread.sleep(500);
		//check chat
		
		List<WebElement> chats_K =  driver.findElements(By.xpath("//ul[@id='chat-list']/li"));
		List<WebElement> chats_A =  attendADriver.findElements(By.xpath("//ul[@id='chat-list']/li"));
		List<WebElement> chats_B =  attendBDriver.findElements(By.xpath("//ul[@id='chat-list']/li"));
		
		if(chats_K.size() == 3 && chats_A.size() == 3 && chats_B.size() == 3) {
			String king = chats_K.get(0).findElement(By.xpath(".//p[@class='content']")).getText();
			String usera = chats_A.get(0).findElement(By.xpath(".//p[@class='content']")).getText();
			String userb = chats_B.get(0).findElement(By.xpath(".//p[@class='content']")).getText();
			
			if(!king.contentEquals("hello Iam King!"))
				failMsg = failMsg + "\n 2. Presenters chat list message first : " + king;
			if(!usera.contentEquals("hello Iam King!"))
				failMsg = failMsg + "\n 2. UserA chat list message first : " + usera;
			if(!userb.contentEquals("hello Iam King!"))
				failMsg = failMsg + "\n 2. UserB chat list message first : " + userb;
			
			king = chats_K.get(1).findElement(By.xpath(".//p[@class='content']")).getText();
			usera = chats_A.get(1).findElement(By.xpath(".//p[@class='content']")).getText();
			userb = chats_B.get(1).findElement(By.xpath(".//p[@class='content']")).getText();
			
			if(!king.contentEquals("hello Iam A1"))
				failMsg = failMsg + "\n 3. Presenters chat list message 2nd : " + king;
			if(!usera.contentEquals("hello Iam A1"))
				failMsg = failMsg + "\n 3. UserA chat list message 2nd : " + usera;
			if(!userb.contentEquals("hello Iam A1"))
				failMsg = failMsg + "\n 3. UserB chat list message 2nd : " + userb;
			
			king = chats_K.get(2).findElement(By.xpath(".//p[@class='content']")).getText();
			usera = chats_A.get(2).findElement(By.xpath(".//p[@class='content']")).getText();
			userb = chats_B.get(2).findElement(By.xpath(".//p[@class='content']")).getText();
			
			if(!king.contentEquals("hello Iam B2"))
				failMsg = failMsg + "\n 4. Presenters chat list message 3rd : " + king;
			if(!usera.contentEquals("hello Iam B2"))
				failMsg = failMsg + "\n 4. UserA chat list message 3rd : " + usera;
			if(!userb.contentEquals("hello Iam B2"))
				failMsg = failMsg + "\n 4. UserB chat list message 3rd : " + userb;
			
		} else {
			failMsg = "1. chat list size error. User A's chat list size : " + chats_B.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 21. 참석자A 질믄 입력, 발표자, 참석자B 질문 뱃지확인 
	@Test(priority = 21, dependsOnMethods = { "chattingBasic" }, alwaysRun = true, enabled = true)
	public void qnaPublic() throws Exception {
		String failMsg = "";
		
		QnATest qna = new QnATest();
		//참석자A 질문작성
		qna.addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		
		//발표자 질문탭 뱃지 및 질문 확인
		try {
			if(!driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = "1. QnA bedge count [Expected]1 [Actual]" 
						+ driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = "0. cannot find QnA bedge";
		}
		driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 1) {
			failMsg = failMsg + "\n 1. QnA count [Expected]1 [Actual]" + qnas.size();
		} else {
			String msg = qna.checkQuestion(qnas.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		try {
			if(!attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = failMsg + "3. QnA bedge count [Expected]1 [Actual]" 
						+ attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 00. cannot find QnA bedge";
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(500);
		
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 4. QnA count [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg = qna.checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n5." + msg));
		}		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 참석자B 질믄 입력 (참석자A,참석자B 질믄), 참석자A가 본인 질문 삭제
	@Test(priority = 22, dependsOnMethods = { "qnaPublic" }, alwaysRun = true,  enabled = true)
	public void qnaPublic_delete1() throws Exception {
		String failMsg = "";
			
		QnATest qna = new QnATest();
		
		//참석자B 질문작성
		qna.addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		
		//참석자A 본인질문 삭제
		if(attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(attendeeA) [Expected]2 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(attendADriver);  
			// mouse hover
			WebElement web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			
			WebDriverWait wait = new WebDriverWait(attendADriver, 10);
			try {
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_MODAL_BODY)));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("cannot find element : " + e.getMessage());
			}
			Thread.sleep(500);
			
			if(!attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_QUESTION)) {
				failMsg = failMsg + "\n 2. delete popup msg[Expected]" + MSG_DELETE_QUESTION +  "[Actual]" + attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			//cancel
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(100);
			
			web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			//confirm
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(100);	
		}
		
		//발표자 질문탭 뱃지 및 질문 확인
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg = qna.checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n 5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg = qna.checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n6." + msg));
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 23. 참석자A 질믄 입력 (참석자A,참석자B 질믄), 발표자가  참석자B 질문 삭제
	@Test(priority = 23, dependsOnMethods = { "qnaPublic_delete1" }, alwaysRun = true, enabled = true)
	public void qnaPublic_delete2() throws Exception {
		String failMsg = "";
		
		QnATest qna = new QnATest();
		
		//참석자A 질문작성
		qna.addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		
		//발표자가  참석자B 질문 삭제
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count [Expected]2 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(driver);  
			// mouse hover
			WebElement web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(100);
			
			if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_QUESTION)) {
				failMsg = failMsg + "\n 2. delete popup msg[Expected]" + MSG_DELETE_QUESTION +  "[Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			//cancel
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(100);
			
			web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			//confirm
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(100);	
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n 1. QnA count(attendee2) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg =  qna.checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}		
		
		//발표자 질문탭 질문 확인
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
	
			// 나머지도 삭제
			Actions actions = new Actions(driver);  
			// mouse hover
			WebElement web = qnas2.get(0);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			//confirm
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 24. 참석자A 비공개 질믄 입력 (참석자B 비공개1 ,참석자B 비공개0)
	@Test(priority = 24, dependsOnMethods = { "qnaPublic_delete2"}, alwaysRun = true, enabled = true)
	public void qnaPrivate() throws Exception {
		String failMsg = "";
		
		// 발표자, 참석자A 채팅탭으로 이동해놓음
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna active")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(100);
		}
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna active")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(100);
		}
		
		QnATest qna = new QnATest();
		
		//참석자A 비공개 질문작성
		qna.addQuestion(attendADriver, QNA_QUESTION_PRIVATE, false);
		
		//발표자 질문탭 뱃지 및 질문 확인
		try {
			if(!driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = "1. QnA bedge count(presenter) [Expected]1 [Actual]" 
						+ driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = "0. cannot find QnA bedge";
		}
		driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 1) {
			failMsg = failMsg + "\n 2. QnA count(presenter) [Expected]1 [Actual]" + qnas.size();
		} else {
			String msg =  qna.checkQuestion(qnas.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		try {
			if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = failMsg + "\n 4. QnA bedge is visible" ;
			}
		} catch (NoSuchElementException e) {
			
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 0) {
			failMsg = failMsg + "\n 5. QnA count [Expected]0 [Actual]" + qnas2.size();
		} 	
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 25. 참석자A 비공개1 ,참석자B 비공개1 만들기. 참석자A가 비공개 질문 삭제
	@Test(priority = 25, dependsOnMethods = { "qnaPrivate" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_delete1() throws Exception {
		String failMsg = "";
		
		QnATest qna = new QnATest();
		
		qna.addQuestion(attendBDriver, QNA_QUESTION_PRIVATE, false);
		
		// 참석자A 본인 비공개 질문 삭제
		if(attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 1) {
			failMsg = failMsg + "\n 1. QnA count(attendeeA) [Expected]1 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(attendADriver);  
			// mouse hover
			WebElement web = qnas.get(0);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			
			if(!attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_QUESTION)) {
				failMsg = failMsg + "\n 2. delete popup msg[Expected]" + MSG_DELETE_QUESTION +  "[Actual]" + attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			//cancel
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(100);
			
			web = qnas.get(0);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			//confirm
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(100);	
		}
		
		//발표자 질문탭 질문 확인
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(Presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg =  qna.checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n6." + msg));
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 26. 참석자A 비공개1 ,참석자B 비공개1. 발표자가 참석자B 비공개 질문 삭제
	@Test(priority = 26, dependsOnMethods = { "qnaPrivate_delete1" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_delete2() throws Exception {
		String failMsg = "";
		
		QnATest qna = new QnATest();
		
		qna.addQuestion(attendADriver, QNA_QUESTION_PRIVATE, false);
		
		// 발표자가 참석자B 비공개 질문 삭제
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(driver);  
			// mouse hover
			WebElement web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();

			WebDriverWait wait = new WebDriverWait(driver, 10);
			try {
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_MODAL_BODY)));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("cannot find element : " + e.getMessage());
			}
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_QUESTION)) {
				failMsg = failMsg + "\n 2. delete popup msg[Expected]" + MSG_DELETE_QUESTION +  "[Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			//cancel
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(100);
			
			web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			//confirm
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(100);	
		}
		
		//발표자 질문탭 질문 확인
		if(driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(Presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 0) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]0 [Actual]" + qnas3.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	//30. 참석자A 비공개1, 참석자A 공개질믄 입력. 발표자가 공개질문에 답변 작성
	@Test(priority = 30, dependsOnMethods = { "qnaPrivate_delete2" }, alwaysRun = true, enabled = true)
	public void qnaPublic_answer() throws Exception {
		String failMsg = "";
		List<String> ans = new ArrayList<String>();
		
		QnATest qna = new QnATest();
		
		//참석자A 공개질믄 입력
		qna.addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		Thread.sleep(500);
		
		// 발표자가 공개질문에 답변 작성
		if (driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			ans.add(QNA_ANSWER+"0");
			qna.addAnswer(qnas.get(0), QNA_ANSWER+"0");
		}
		
		//참석자A 답변확인
		if(attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas1 = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas1.size() != 2) {
			failMsg = failMsg + "\n2. QnA count(attendeeA) [Expected]2 [Actual]" + qnas1.size();
		} else {
			String msg =  qna.checkAnswer(qnas1.get(0), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}	
		
		//참석자B 답변확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n4. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n5." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//31. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 비공개질문에 답변 작성
	@Test(priority = 31, dependsOnMethods = { "qnaPublic_answer" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_answer() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		List<String> ans = new ArrayList<String>();
		
		// 발표자가  비공개질문에 답변 작성
		if (driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(1000);
		} else {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(1000);
		}
		
		QnATest qna = new QnATest();
		
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			ans.add(QNA_ANSWER+"1");
			qna.addAnswer(qnas.get(1), QNA_ANSWER+"1");
		}
		//참석자A 답변확인
		if(attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas1 = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas1.size() != 2) {
			failMsg = failMsg + "\n2. QnA count(attendeeA) [Expected]2 [Actual]" + qnas1.size();
		} else {
			String msg =  qna.checkAnswer(qnas1.get(1), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}	
		
		//참석자B 답변확인
		if(attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			List<String> tmp = new ArrayList<String>();
			tmp.add(QNA_ANSWER+"0");
			
			String msg =  qna.checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", tmp);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//32. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 공개질문 답변 삭제 ( 공개, 비공개 순)
	@Test(priority = 32, dependsOnMethods = { "qnaPrivate_answer" }, alwaysRun = true, enabled = true)
	public void qnaPublic_deleteAnswer() throws Exception {
		String failMsg = "";
		
		// 발표자가 공개질문 답변 삭제
		if (driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		} else {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(200);
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		}
		
		QnATest qna = new QnATest();
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			String msg =  qna.deleteAnswer(driver, qnas.get(0));
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		// 참석자A 공개질믄 답변 삭제 확인
		if (attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		}
		List<WebElement> qnas2 = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 2) {
			failMsg = failMsg + "\n3. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", null);
			failMsg = failMsg + (msg.contentEquals("empty")?"":("\n4." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//33. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 비공개질문 답변 삭제 ( 공개, 비공개 순)
	@Test(priority = 33, dependsOnMethods = { "qnaPublic_deleteAnswer" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_deleteAnswer() throws Exception {
		String failMsg = "";
		
		// 발표자가 비공개질문 답변 삭제
		if (driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		} else {
			driver.findElement(By.xpath(XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(200);
			driver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
		}
		
		QnATest qna = new QnATest();
		List<WebElement> qnas = driver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			String msg =  qna.deleteAnswer(driver, qnas.get(1));
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		// 참석자A 비공개질문  답변 삭제 확인
		if (attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		}
		List<WebElement> qnas2 = attendADriver.findElements(By.xpath(XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 2) {
			failMsg = failMsg + "\n3. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  qna.checkAnswer(qnas2.get(1), ATTENDEES_NICKNAME + "1", null);
			failMsg = failMsg + (msg.contentEquals("empty")?"":("\n4." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 100. 세미나 종료하기
	@Test(priority = 100, enabled = true)
	public void closeSeminar() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[2]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		Thread.sleep(700);
		/* 타이밍 문제로 토스트 확인은 하지 않는걸로
		if(isElementPresent_wd(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n1-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n1-2. cannot find toast (presenter)";
		}
		
		if(isElementPresent_wd(attendADriver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n2-1. toast message. (attendee A) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n2-2. cannot find toast (attendee A)";
		}
		
		if(isElementPresent_wd(attendBDriver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n3-1. toast message. (attendee B) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n3-2. cannot find toast (attendee B)";
		}
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}	
	
	// 101. 종료된 세미나 통계
	@Test(priority = 101, enabled = true)
	public void seminarStatistics() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/seminar/close" + seminarID)) {
			failMsg = "1. no ending view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.SERVER_URL + "/seminar/close" + seminarID);
			Thread.sleep(500);
		}
		//go to Statistics
		driver.findElement(By.xpath("//div[@class='buttons-wrap']/button[1]")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID)) {
			failMsg = "\n2. no Statistics view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID);
			Thread.sleep(500);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		attendADriver.quit();
		attendBDriver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	private boolean isElementPresent_wd(WebDriver wd, By by) {
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
