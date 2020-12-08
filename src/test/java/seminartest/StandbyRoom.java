package seminartest;

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

/* StandbyRoom
 * part1 발표자 rsrsup1
 * 0. standby 세미나 만들기
 * 1. 발표자 standby 룸에 입장
 * 2. 발표자 standby 룸에서 발표문서 확인, 툴팁 확인 후 삭제
 * 3. 발표자 standby 룸에서 발표문서 추가
 * 4. 발표자 standby 룸에서 발표문서 추가 초과(10개 초과)
 * 5. 발표자 standby 룸에서 유튜브 확인 , 툴팁 확인 후 삭제
 * 6. 발표자 standby 룸에서 유튜브 추가 : invalid case
 * 7. 발표자 standby 룸에서 유튜브 추가
 * 8. 발표자 standby 룸에서 유튜브 추가 최대(10개)
 * 
 * 8. 참석자 A 입장
 * 9. 참석자 A : standby 화면 확인
 * 8. 참석자 B 입장
 * 
 * part2
 * 10. 발표자, 참석자A, 참석자 B : 채팅 기본(한줄씩)
 * 
 * part3
 * 21. 참석자A 질믄 입력, 발표자, 참석자B 질문 뱃지확인 
 * 22. 참석자B 질믄 입력 (참석자A,참석자B 질믄), 참석자A가 본인 질문 삭제
 * 23. 참석자A 질믄 입력 (참석자A,참석자B 질믄), 발표자가  참석자B 질문 삭제
 * 24. 참석자B 비공개 질믄 입력 (참석자A 공개1 ,참석자B 비공개1)
 * 25. 참석자A 비공개1 ,참석자B 비공개1. 참석자A가 비공개 질문 삭제
 * 26. 참석자A 비공개1 ,참석자B 비공개1. 발표자가 참석자B 비공개 질문 삭제
 * 
 * 30. 참석자A 비공개1, 참석자A 공개질믄 입력. 발표자가 공개질문에 답변 작성
 * 31. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 비공개질문에 답변 작성
 * 32. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 공개질문 답변 삭제 ( 공개, 비공개 순)
 * 33. 참석자A 비공개1, 참석자A 공개질믄1, 발표자가 비공개질문 답변 삭제 ( 공개, 비공개 순)
 */
public class StandbyRoom {
	
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
		
		driver.get(CommonValues.SERVER_URL);

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
		
		Thread.sleep(5000);
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
	
	// 0. standby 세미나 만들기
	@Test(priority = 0)
	public void CreateSandbySeminar() throws Exception {
		Date time = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 5);
		SimpleDateFormat format1 = new SimpleDateFormat ("HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat ("HH:mm:ss");
		String time1 = format1.format(cal.getTime());
		
		//create seminar after 10minute
		seminarID = createSeminar("standby_" + format2.format(cal.getTime()), time1);
	}
	
	// 1. 발표자 standby 룸에 입장
	@Test(priority = 1)
	public void SeminarRoom_Pres() throws Exception {
		
		String failMsg = "";
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailview);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		//click join button
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);
		
		Thread.sleep(2000);
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(1));
	    
		Thread.sleep(1000);
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
	    {
			failMsg = "fail to join Seminar : " + driver.getCurrentUrl();
	    } {
	    	//close 1tab 
	    	driver.switchTo().window(tabs2.get(0));
			driver.close();
			driver.switchTo().window(tabs2.get(1));
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 2. 발표자 standby 룸에서 발표문서 확인 (툴팁 확인) 후 삭제 : 스펙아웃 사용안함
	@Test(priority = 2, dependsOnMethods = { "SeminarRoom_Pres" },  enabled = true)
	public void doc_checkDelete() throws Exception {
		
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//click doc button
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
		Thread.sleep(100);
		
		if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON))){
			
			//cannot click
			if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON)).isSelected())
				failMsg = "1. doc item icon is enable.";
			
			//icon
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//i[1]")).getAttribute("class").contentEquals("pdf icon")) {
				failMsg = failMsg + "\n 2. doc item icon [Expected]pdf, [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//i[1]")).getAttribute("class");
			}
			
			//title
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText().contains(CommonValues.TESTFILE_PDFS[0])) {
				failMsg = failMsg + "\n 3. doc item title [Expected]" + CommonValues.TESTFILE_PDFS[0] + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText();
			}
			
			Actions actions = new Actions(driver);
			  
			// mouse hover
			WebElement web = driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
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
			if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP))){
				if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)).getText().contentEquals(DES_FILE)) {
					failMsg = failMsg + "\n 4. tooltip msg [Expected]" + DES_FILE 
							+ " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)).getText();
				}
			} else {
				failMsg = failMsg + "\n 7. cannot find tooltip";
			}
			
			// 삭제 - cancel
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[2]")).click();
			Thread.sleep(500);
			
			//click doc button again
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
			
			//delete - yes
			
			// mouse hover
			web = driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[1]")).click();
			Thread.sleep(500);
			
			if (!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText()
					.contentEquals(CommonValues.SEMINAR_ROOM_DELETE_TOAST)) {
				failMsg = failMsg + "\n 5. toast message after delete item : [Actual] "
						+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			} else {
				Thread.sleep(2000);
			}
			
			
		} else {
			failMsg = failMsg + "\n 8. cannot fild doc item";
		}
		
		//delete item check
		//click doc icon again
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
		if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON))){
			failMsg = failMsg + "\n 6. find doc item after delete. file name : "
					+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON + "//div[@class='title']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 발표자 standby 룸에서 발표문서 추가 
	@Test(priority = 3, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void doc_addFile() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		if(!isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_DOC_UPLOAD))) {
			//click doc icon
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
		}
		
		List<WebElement> docitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
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
		docitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
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
	
	// 4. 발표자 standby 룸에서 발표문서 추가 초과(10개 초과) :  : 스펙아웃 사용안함
	@Test(priority = 4, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void doc_addFileMax() throws Exception {
		//기획안됨
	}
	
	// 5. 발표자 standby 룸에서 유튜브 확인 삭제
	@Test(priority = 5, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void youtube_checkDelete() throws Exception {
		
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		//click youtube icon
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		Thread.sleep(100);
		
		if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON))){
			
			//cannot click
			if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON)).isSelected())
				failMsg = "1. youtube item icon is enable.";
			
			//title
			if(!driver.findElement(By.xpath("//li[@class='youtube-item']//div[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[0])) {
				failMsg = failMsg + "\n 2. youtube item title [Expected]" + CommonValues.YOUTUBE_TITLE[0] 
						+ " [Actual]" + driver.findElement(By.xpath("//li[@class='youtube-item'']//div[@class='title']")).getText();
			}
			Actions actions = new Actions(driver);
			
			//툴팁 확인
			WebElement web = driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(500);
			
			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("cannot find element : " + e.getMessage());
			}
			//툴팁 확인
			if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP))){
				if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)).getText().contentEquals(DES_YOUTUBE)) {
					failMsg = failMsg + "\n 3. tooltip msg [Expected]" + DES_YOUTUBE 
							+ " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOOLTIP)).getText();
				}
			} else {
				failMsg = failMsg + "\n4. cannot find tooltip";
			}
			
			// mouse hover. delete - cancel
			web = driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[2]")).click();
			Thread.sleep(500);
			
			//click youtube icon again
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
			
			//delete - yes
			
			// mouse hover
			web = driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON + "//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//section[@id='confirm-dialog']//button[1]")).click();
			Thread.sleep(500);
			
			
			if (!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText()
					.contentEquals(CommonValues.SEMINAR_ROOM_DELETE_TOAST)) {
				failMsg = failMsg + "\n 5. toast message after delete item : [Actual] "
						+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
			
			Thread.sleep(2000);
			
		} else {
			failMsg = "6. cannot fild doc item";
		}
		
		//delete item check
		//click youtube icon again
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON))){
			failMsg = failMsg + "\n 4. find doc item after delete. file name : "
					+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON + "//div[@class='title']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 6. 발표자 standby 룸에서 유튜브 추가 : invalid case
	@Test(priority = 6, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void youtube_add1() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		if(!isElementPresent_wd(driver, By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_BTN))) {
			//click youtube icon
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		}
		
		//기존 youtube icon 확인
		List<WebElement> youtubeitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));
		int youtubecount = youtubeitems.size();
		
		//click +
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_BTN)).click();
		
		Thread.sleep(500);
		
		//empty url
		clearAttributeValue(driver.findElement(By.xpath("//input[@class='url-input']")));
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		Thread.sleep(500);
		//confirm 버튼 : disabled
		if (driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).isEnabled()) {
			failMsg = failMsg + "1. cinfirm button is enabled (youtube link empty)";
		} 
		Thread.sleep(2000);
		
		//invalid url
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys("invalid URL test");
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		// toast
		if (!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText()
				.contentEquals(CommonValues.SEMINAR_ROOM_URL_INVALID_TOAST)) {
			failMsg = failMsg + "\n 2. toast message after input invalid url : [Actual] "
					+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
		}
		Thread.sleep(2000);
		
		// input youtube url and click cancel
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[3]);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[2]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		
		youtubeitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));

		if (youtubecount != youtubeitems.size()) {
			failMsg = failMsg + "\n 3. youtube count error adding cancellation [Expected] " + youtubecount + " [Actual]" + youtubeitems.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 7. 발표자 standby 룸에서 유튜브 추가
	@Test(priority = 7, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void youtube_add2() throws Exception {
		String failMsg = "";
		
		//refresh window
		driver.navigate().refresh();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//click youtube icon
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		
		List<WebElement> youtubeitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));
		int youtubecount = youtubeitems.size();
		
		//click +
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBEADD_BTN)).click();
		Thread.sleep(500);
		//input youtube url
		driver.findElement(By.xpath("//input[@class='url-input']")).clear();
		driver.findElement(By.xpath("//input[@class='url-input']")).sendKeys(CommonValues.YOUTUBE_URL[3]);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)));
		} catch (Exception e) {
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		Thread.sleep(100);
		
		if (!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText()
				.contentEquals(CommonValues.SEMINAR_ROOM_ADD_TOAST)) {
			failMsg = failMsg + "\n 1. toast message after add youtube item : [Actual] "
					+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
		} 
		Thread.sleep(2000);
	
		//check added item
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_BTN)).click();
		
		youtubeitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_YUTUBE_ICON));

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
	
	// 8. 발표자 standby 룸에서 유튜브 추가 최대(10개)
	@Test(priority = 8, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void youtube_max() throws Exception {
		String failMsg = "";
		
		//기획후에...
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 8. 참석자 A 입장
	@Test(priority = 8, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void enterSeminar_attendA() throws Exception {
		
		//seminar url
		String seminarurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
		
		attendADriver.get(seminarurl);
		Thread.sleep(2000);
		
		//click join
		attendADriver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(1000);
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).click();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys(ATTENDEES_NICKNAME + "1");
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).click();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).sendKeys(ATTENDEES_EMAIL);
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_COMPANY)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_POSITION)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_PHONE)).clear();
		
		if(!attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1+ "//input")).isSelected())
			attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX1 + "//div")).click();
		if(!attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2+ "//input")).isSelected())
			attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_AGREE_CHECKBOX2 + "//div")).click();
		
		//join seminar
		((JavascriptExecutor) attendADriver).executeScript("arguments[0].scrollIntoView(true);"
				, attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_ENTER)));

		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_ENTER)).click();
		Thread.sleep(3000);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendADriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("USER A fail to join Seminar : " + attendADriver.getCurrentUrl());
	    	throw e;
	    }
		
	}
	
	// 9. 참석자 A : standby 화면 확인
	@Test(priority = 9, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void standbyRoomBasic_attendA() throws Exception {
		String failMsg = "";
		
		//label..
		if(!attendADriver.findElement(By.xpath("//header[@id='gnb']//strong[@id='user-type']")).getText().contentEquals("Stand By")) {
			failMsg = "1. UserA room label : [Actual]" + attendADriver.findElement(By.xpath("//header[@id='gnb']//strong[@id='user-type']")).getText();
		}

		//time
		if(!attendADriver.findElement(By.xpath("//header[@id='gnb']//strong[@id='time']")).getText().contentEquals("00:00:00")) {
			failMsg = failMsg + "\n 2. UserA room tile : [Actual]" + attendADriver.findElement(By.xpath("//header[@id='gnb']//strong[@id='time']")).getText();
		}
		
		
		//tab..chat, qna, download
		if (!isElementPresent(By.xpath("//button[@class='chat active']"))) {
			failMsg = failMsg + "\n 3. UserA chat tab error.";
		}
		if (!isElementPresent(By.xpath("//button[@class='qna false']"))) {
			failMsg = failMsg + "\n 4. UserA chat tab error.";
		}
		if (!isElementPresent(By.xpath("//button[@class='download false']"))) {
			failMsg = failMsg + "\n 5. UserA chat tab error.";
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
		Thread.sleep(2000);
		
		//click join
		attendBDriver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")).click();
		Thread.sleep(1000);
		
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
				, attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_ENTER)));

		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_ENTER)).click();
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
	@Test(priority = 10, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
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
	@Test(priority = 21, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qnaPublic() throws Exception {
		String failMsg = "";
				
		//참석자A 질문작성
		addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		
		//발표자 질문탭 뱃지 및 질문 확인
		try {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = "1. QnA bedge count [Expected]1 [Actual]" 
						+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = "0. cannot find QnA bedge";
		}
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 1) {
			failMsg = failMsg + "\n 1. QnA count [Expected]1 [Actual]" + qnas.size();
		} else {
			String msg = checkQuestion(qnas.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		try {
			if(!attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = failMsg + "3. QnA bedge count [Expected]1 [Actual]" 
						+ attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 00. cannot find QnA bedge";
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 4. QnA count [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg = checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n5." + msg));
		}		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 참석자B 질믄 입력 (참석자A,참석자B 질믄), 참석자A가 본인 질문 삭제
	@Test(priority = 22, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qnaPublic_delete1() throws Exception {
		String failMsg = "";
				
		//참석자B 질문작성
		addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		
		WebDriverWait waitA = new WebDriverWait(attendADriver, 10);
		
		//참석자A 본인질문 삭제
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(attendeeA) [Expected]2 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(attendADriver);  
			// mouse hover
			WebElement web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
			Thread.sleep(500);
			
			if(!attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_QUESTION)) {
				failMsg = failMsg + "\n2. delete popup msg[Expected]" + MSG_DELETE_QUESTION +  "[Actual]" + attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			//cancel
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(100);
			
			web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();

			try {
				waitA.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")));
			} catch (Exception e) {
				System.out.println("cannot find element : " + e.getMessage());
			}
			//confirm
			attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(100);	
		}
		
		//발표자 질문탭 뱃지 및 질문 확인
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n3.QnA count(presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg = checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n5.QnA count(attendeeB) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg = checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n6." + msg));
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 23. 참석자A 질믄 입력 (참석자A,참석자B 질믄), 발표자가  참석자B 질문 삭제
	@Test(priority = 23, dependsOnMethods = { "SeminarRoom_Pres", "qnaPublic_delete1" }, alwaysRun = true, enabled = true)
	public void qnaPublic_delete2() throws Exception {
		String failMsg = "";
				
		//참석자A 질문작성
		addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		
		//발표자가  참석자B 질문 삭제
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
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
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n 1. QnA count(attendee2) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg =  failMsg + checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}		
		
		//발표자 질문탭 질문 확인
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
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
	@Test(priority = 24, dependsOnMethods = { "SeminarRoom_Pres", "qnaPublic_delete2"}, enabled = true)
	public void qnaPrivate() throws Exception {
		String failMsg = "";
		
		// 발표자, 참석자A 채팅탭으로 이동해놓음
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna active")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(100);
		}
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna active")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(100);
		}
		
		//참석자A 비공개 질문작성
		addQuestion(attendADriver, QNA_QUESTION_PRIVATE, false);
		
		//발표자 질문탭 뱃지 및 질문 확인
		try {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = "1. QnA bedge count(presenter) [Expected]1 [Actual]" 
						+ driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText();
			}
		} catch (NoSuchElementException e) {
			failMsg = "0. cannot find QnA bedge";
		}
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 1) {
			failMsg = failMsg + "\n 2. QnA count(presenter) [Expected]1 [Actual]" + qnas.size();
		} else {
			String msg =  checkQuestion(qnas.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		try {
			if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
				failMsg = failMsg + "\n 4. QnA bedge is visible" ;
			}
		} catch (NoSuchElementException e) {
			
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 0) {
			failMsg = failMsg + "\n 5. QnA count [Expected]0 [Actual]" + qnas2.size();
		} 	
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 25. 참석자A 비공개1 ,참석자B 비공개1 만들기. 참석자A가 비공개 질문 삭제
	@Test(priority = 25, dependsOnMethods = { "SeminarRoom_Pres", "qnaPrivate" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_delete1() throws Exception {
		String failMsg = "";
		
		addQuestion(attendBDriver, QNA_QUESTION_PRIVATE, false);
		
		// 참석자A 본인 비공개 질문 삭제
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
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
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(Presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 뱃지 및 질문 확인
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 1) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas3.size();
		} else {
			String msg =  checkQuestion(qnas3.get(0), ATTENDEES_NICKNAME + "2", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n6." + msg));
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 26. 참석자A 비공개1 ,참석자B 비공개1. 발표자가 참석자B 비공개 질문 삭제
	@Test(priority = 26, dependsOnMethods = { "SeminarRoom_Pres", "qnaPrivate_delete1" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_delete2() throws Exception {
		String failMsg = "";
		
		addQuestion(attendADriver, QNA_QUESTION_PRIVATE, false);
		
		// 발표자가 참석자B 비공개 질문 삭제
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {

			Actions actions = new Actions(driver);  
			// mouse hover
			WebElement web = qnas.get(1);
			actions.moveToElement(web).perform();
			Thread.sleep(100);
			
			web.findElement(By.xpath(".//i[@class='ricon-close']")).click();
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
		if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n 3. QnA count(Presenter) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkQuestion(qnas2.get(0), ATTENDEES_NICKNAME + "1", QNA_QUESTION_PRIVATE, false);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		//참석자B 질문탭 확인
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		Thread.sleep(100);
		
		List<WebElement> qnas3 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas3.size() != 0) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]0 [Actual]" + qnas3.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	//30. 참석자A 비공개1, 참석자A 공개질믄 입력. 발표자가 공개질문에 답변 작성
	@Test(priority = 30, dependsOnMethods = { "SeminarRoom_Pres", "qnaPrivate_delete2" }, alwaysRun = true, enabled = true)
	public void qnaPublic_answer() throws Exception {
		String failMsg = "";
		List<String> ans = new ArrayList<String>();
		//참석자A 공개질믄 입력
		addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		Thread.sleep(500);
		
		// 발표자가 공개질문에 답변 작성
		if (driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			ans.add(QNA_ANSWER+"0");
			addAnswer(qnas.get(0), QNA_ANSWER+"0");
		}
		
		//참석자A 답변확인
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas1 = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas1.size() != 2) {
			failMsg = failMsg + "\n2. QnA count(attendeeA) [Expected]2 [Actual]" + qnas1.size();
		} else {
			String msg =  checkAnswer(qnas1.get(0), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}	
		
		//참석자B 답변확인
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n4. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n5." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//31. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 비공개질문에 답변 작성
	@Test(priority = 31, dependsOnMethods = { "SeminarRoom_Pres", "qnaPublic_answer" }, alwaysRun = true, enabled = true)
	public void qnaPrivate_answer() throws Exception {
		String failMsg = "";
		List<String> ans = new ArrayList<String>();
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		// 발표자가  비공개질문에 답변 작성
		if (driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		} else {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		}
		
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			ans.add(QNA_ANSWER+"1");
			addAnswer(qnas.get(1), QNA_ANSWER+"1");
		}
		
		//참석자A 답변확인
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas1 = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas1.size() != 2) {
			failMsg = failMsg + "\n2. QnA count(attendeeA) [Expected]2 [Actual]" + qnas1.size();
		} else {
			String msg =  checkAnswer(qnas1.get(1), ATTENDEES_NICKNAME + "1", ans);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n3." + msg));
		}	
		
		//참석자B 답변확인
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qnas2 = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 1) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			List<String> tmp = new ArrayList<String>();
			tmp.add(QNA_ANSWER+"0");
			
			String msg =  checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", tmp);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n4." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//32. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 공개질문 답변 삭제 ( 공개, 비공개 순)
	@Test(priority = 32, dependsOnMethods = { "SeminarRoom_Pres", "qnaPrivate_answer" }, alwaysRun = true, enabled = true)
	public void qnaPublic_deleteAnswer1() throws Exception {
		String failMsg = "";
		
		// 발표자가 공개질문 답변 삭제
		if (driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		} else {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(200);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		}
		
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			String msg =  deleteAnswer(driver, qnas.get(0));
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		// 참석자A 공개질믄 답변 삭제 확인
		if (attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		}
		List<WebElement> qnas2 = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 2) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkAnswer(qnas2.get(0), ATTENDEES_NICKNAME + "1", null);
			failMsg = failMsg + (msg.contentEquals("empty")?"":("\n4." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//33. 참석자A 비공개1, 참석자A 공개질믄1,  발표자가 비공개질문 답변 삭제 ( 공개, 비공개 순)
	@Test(priority = 33, dependsOnMethods = { "SeminarRoom_Pres", "qnaPublic_deleteAnswer1" }, alwaysRun = true, enabled = true)
	public void qnaPublic_deleteAnswer2() throws Exception {
		String failMsg = "";
		
		// 발표자가 비공개질문 답변 삭제
		if (driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		} else {
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
			Thread.sleep(200);
			driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
		}
		
		List<WebElement> qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if (qnas.size() != 2) {
			failMsg = failMsg + "\n 1. QnA count(presenter) [Expected]2 [Actual]" + qnas.size();
		} else {
			String msg =  deleteAnswer(driver, qnas.get(1));
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
		}
		
		// 참석자A 비공개질문  답변 삭제 확인
		if (attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(500);
		}
		List<WebElement> qnas2 = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas2.size() != 2) {
			failMsg = failMsg + "\n5. QnA count(attendeeB) [Expected]1 [Actual]" + qnas2.size();
		} else {
			String msg =  checkAnswer(qnas2.get(1), ATTENDEES_NICKNAME + "1", null);
			failMsg = failMsg + (msg.contentEquals("empty")?"":("\n4." + msg));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	public void addQuestion(WebDriver wd, String msg, boolean ispublic) throws InterruptedException {
		
		if(wd.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			wd.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		
		wd.findElement(By.xpath("//i[@class='ricon-write-black']")).click();
		Thread.sleep(100);
		
		if(!ispublic)
			wd.findElement(By.xpath("//div[@class='qna-question__btn-box']//div[@class='checkbox']")).click();
		Thread.sleep(100);
		
		wd.findElement(By.xpath("//textarea[@class='Qna_qna-textarea__2bvZA scrollbar']")).clear();
		wd.findElement(By.xpath("//textarea[@class='Qna_qna-textarea__2bvZA scrollbar']")).sendKeys(msg);
		wd.findElement(By.xpath("//div[@class='qna-question__btn-box']/button[@class='btn btn-primary btn-m ']")).click();
		
		Thread.sleep(500);
		wd.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_CHAT)).click();
	}
	
	public void addAnswer(WebElement ee, String msg) throws InterruptedException {
		
		ee.findElement(By.xpath(".//div[@class='qna-list-item__button-box']/button[1]")).click();
		Thread.sleep(500);
		ee.findElement(By.xpath(".//textarea[@class='Qna_qna-textarea__2bvZA scrollbar']")).clear();
		ee.findElement(By.xpath(".//textarea[@class='Qna_qna-textarea__2bvZA scrollbar']")).sendKeys(msg);
		ee.findElement(By.xpath(".//div[@class='qna-list-item__inner-box answer']/button[1]")).click();
		Thread.sleep(500);
	}	
	
	public String deleteAnswer(WebDriver wd, WebElement ee) throws InterruptedException {
		String failMsg = "";
		List<WebElement> replys = ee.findElements(By.xpath(".//li[@class='Qna_qna-reply__1zRc7']"));
		
		for (int i = 0; i < replys.size(); i++) {
			replys.get(i).findElement(By.xpath(".//button[@class='qna-reply__close-btn']")).click();
			Thread.sleep(500);
			
			if(!wd.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_DELETE_ANSWER)) {
				failMsg = failMsg + "\n delete popup message [Expected]" + MSG_DELETE_ANSWER 
						+ " [Actual]" + wd.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getAttribute("innerText");
			}
			
			//cancel
			wd.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(500);
			replys.get(i).findElement(By.xpath(".//button[@class='qna-reply__close-btn']")).click();
			Thread.sleep(500);
			//confirm
			wd.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		return failMsg;
	}
	
	public String checkQuestion(WebElement ee, String nickname, String msg, boolean ispublic) {
		String failMsg = "";
		
		if(!ee.findElement(By.xpath(".//span[@class='content-box__nickname']")).getText().contentEquals(nickname)) {
			failMsg = "\n qan writer [Expected]" + nickname 
					+ " [Actual]" + ee.findElement(By.xpath(".//span[@class='content-box__nickname']")).getText();
		}
	
		if(!ee.findElement(By.xpath(".//p[@class='content-box__content']")).getText().contentEquals(msg)) {
			failMsg = failMsg + "\n qan content [Expected]" + msg 
					+ " [Actual]" + ee.findElement(By.xpath(".//p[@class='content-box__content']")).getText();
		}
		
		if(ispublic) {
			try {
				ee.findElement(By.xpath(".//div[@class='content-box__utills']//i")).getAttribute("class").contentEquals("ricon-lock");
				failMsg = failMsg + "\n qna lock icon [Expected]" + "cannot find icon" 
						+ " [Actual]find lock icon";
			} catch (NoSuchElementException e) {
				//success
			}
		} else {
			if(!ee.findElement(By.xpath(".//div[@class='content-box__utills']//i")).getAttribute("class").contentEquals("ricon-lock")) {
				failMsg = failMsg + "\n qna lock icon [Expected]" + "ricon-lock" 
						+ " [Actual]" + ee.findElement(By.xpath(".//div[@class='content-box__utills']//i")).getAttribute("class");
			}
		}
		
		return failMsg;
	}
	
	public String checkAnswer(WebElement ee, String nickname, List<String> msg) {
		String failMsg = "";
		List<WebElement> replys = ee.findElements(By.xpath(".//li[@class='Qna_qna-reply__1zRc7']"));
		
		if (msg == null || replys.size() == 0) {
			failMsg = "empty";
		} else if(replys.size() == msg.size()) {
			for (int i = 0; i < replys.size() ; i++) {
				if(!replys.get(i).findElement(By.xpath(".//div[@class='qna-reply__contents']/p")).getText().contentEquals(msg.get(i))) {
					failMsg = failMsg + "\n reply [Expected]" + msg.get(i) 
							+ " [Actual]" + replys.get(i).findElement(By.xpath(".//div[@class='qna-reply__contents']/p")).getText();
				}
				
			}
		}
		
		return failMsg;
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
		/* 토스트 타이밍 문제로 주석처리
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
