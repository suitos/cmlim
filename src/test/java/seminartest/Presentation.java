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
import org.openqa.selenium.ElementClickInterceptedException;
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
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* Presentation
 * user : rsrsup2
 * 0. 세미나 만들기 상세 설정
 * 
 * Part1
 * 1. 발표자료 탭 이동 - 유튜브 빈값 확인
 * 2. 발표자료 - 유튜브 잘못된 url
 * 3. 발표자료 - 정상 유튜브 url 추가
 * 4. 발표자료 - 유튜브 추가 10개 초과
 * 
 * part2
 * 11. 발표자료 - 파일 추가 메세지 확인
 * 12. 발표자료 - 잘못된파일추가
 * 13. 발표자료 - 정상 파일 추가1
 * 14. 발표자료 - 발표자료 10개 초과
 * 
 * part3
 * 21. 발표자료 - 정렬 (14번에서 저장한 대로 재진입후 확인)
 * 22. 아이콘 클릭 - 유튜브
 * 23. 아이콘 클릭 - 파일
 * 24. 발표자료 삭제  - 취소
 * 25. 발표자료 삭제 - 확인
 * 
 * part4
 * 31. 유튜브 /파일  설명 기본
 * 32. 유튜브 - 설명 max
 * 33. 유튜브 - 파일 설명 정상 입력
 * 
 */

public class Presentation {

	public static String PLACEHOLDER_YOUTUBE = "Enter Youtube link";
	public static String PLACEHOLDER_PRESENTION_DES = "Enter a description to view with file.";
	public static String MSG_YOUTUBE_ERROR = "Please, enter a valid YouTube link.";
	public static String MSG_YOUTUBE_EXCEED_ERROR = "You have exceeded the max. number of YouTube registrations.";
	public static String MSG_FILE_EXCEED_ERROR = "Maximum number of files has been exceeded.";
	public static String MSG_FILE_UPLOAD = "Uploaded files can be used and shared by the presenter and the organizer at the seminar (up to 10 files, 500MB total). * File type : doc, docx, pdf, ppt, pptx, hwp, txt, jpg, jpeg, png, gif";
	public static String MSG_DEETE_ITEM = "Do you want to delete the selected file?";
	
	public String des_youtube1 = "1.youtube description test!!";
	public String des_file1 = "2. file description test:)";
	
	public String filePath = "";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	public String seminarTitle = "";
	
	private ArrayList<String> addedItem = new ArrayList();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");

		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = CommonValues.TESTFILE_PATH_MAC;
		else
			filePath = CommonValues.TESTFILE_PATH;
		
		context.setAttribute("webDriver", driver);
		driver.get(CommonValues.SERVER_URL);

		System.out.println("End BeforeTest!!!");
	}

	// 0. 세미나 만들기 상세 설정
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		String failMsg = "";
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			failMsg = "1. not create view" + driver.getCurrentUrl();
			driver.get(createViewUri);
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 1. 발표자료 탭 이동 - 유튜브 빈값 확인
	@Test(priority = 1, enabled = true)
	public void presentation_youtube_empty() throws Exception {
		String failMsg = "";
		
		//click member tab
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
		Thread.sleep(500);
		
		//placeholder
		if(!driver.findElement(By.xpath("//input[@id='input-youtube-link']")).getAttribute("placeholder").contentEquals(PLACEHOLDER_YOUTUBE)) {
			failMsg = "1. youtube input placeholder. [Expected]" + PLACEHOLDER_YOUTUBE 
					+ " [Actual]" + driver.findElement(By.xpath("//input[@id='input-youtube-link']")).getAttribute("placeholder");
		}
		
		// click confirm
		driver.findElement(By.xpath("//label[@for='input-youtube-link']/button")).click();
		Thread.sleep(500);
		assertEquals(closeAlertAndGetItsText(), MSG_YOUTUBE_ERROR);
		
		// enter
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).click();
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		assertEquals(closeAlertAndGetItsText(), MSG_YOUTUBE_ERROR);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
			
	}

	// 2. 발표자료 탭 - 유튜브 잘못된 url
	@Test(priority = 2, enabled = true)
	public void presentation_youtube_invalid() throws Exception {
		String failMsg = "";
		
		//click member tab
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//invalid url
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[0] + "**invalid");
		driver.findElement(By.xpath("//label[@for='input-youtube-link']/button")).click();
		Thread.sleep(1000);
		assertEquals(closeAlertAndGetItsText(), MSG_YOUTUBE_ERROR);
		
		//invalid url
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).clear();
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[0] + "**invalid");
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		assertEquals(closeAlertAndGetItsText(), MSG_YOUTUBE_ERROR);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
			
	}	
	
	// 3. 발표자료 - 정상 유튜브 url 추가
	@Test(priority = 3, enabled = true)
	public void presentation_youtube_valid() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// valid url
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//label[@for='input-youtube-link']/button")).click();
		Thread.sleep(500);
		
		List<WebElement> docs = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if(docs.size() != 1) {
			failMsg = "1. added youtube count error [Expected]1 [Actual]" + docs.size();
		} 
		
		if(!docs.get(0).findElement(By.xpath(".//h4[@class='title']")).getText().contains(CommonValues.YOUTUBE_TITLE[0])) {
			failMsg = failMsg + "\n 1. added youtube titie error [Expected]" + CommonValues.YOUTUBE_TITLE[0] 
					+ " [Actual]" + docs.get(0).findElement(By.xpath(".//h4[@class='title']")).getText();
		} else {
			addedItem.add(CommonValues.YOUTUBE_TITLE[0]);
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 4. 발표자료 - 유튜브 추가 10개 초과
	@Test(priority = 4, enabled = true, dependsOnMethods = {"presentation_youtube_valid"})
	public void presentation_youtube_max() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		//add 9 ( +1 =10 valid)
		for(int i = 1 ; i < 10 ;  i++) {
			driver.findElement(By.xpath("//input[@id='input-youtube-link']")).clear();;
			driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[i]);
			driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(Keys.ENTER);
			addedItem.add(CommonValues.YOUTUBE_TITLE[i]);
			Thread.sleep(500);
		}
		
		// try add 11
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).clear();;
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(CommonValues.YOUTUBE_URL[0]);
		driver.findElement(By.xpath("//input[@id='input-youtube-link']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		assertEquals(closeAlertAndGetItsText(), MSG_YOUTUBE_EXCEED_ERROR);
		
		List<WebElement> docs = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if(docs.size() != 10) {
			failMsg = "1. added youtube count error [Expected]10 [Actual]" + docs.size();
		} 
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	
	
	// 11. 발표자료 - 파일 추가 메세지 확인
	@Test(priority = 11, enabled = true)
	public void presentation_file() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}		
		
		if(!driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU seminar-page-title']/div")).getText().contentEquals(MSG_FILE_UPLOAD)) {
			failMsg = "1. Presentation tab message error [Actual]" 
					+ driver.findElement(By.xpath("//div[@class='PageTitleWrapper_wrap__1lvJU seminar-page-title']/div")).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	
	
	// 12. 발표자료 - 잘못된파일추가
	@Test(priority = 12, enabled = true)
	public void presentation_file_invalid() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		String testpng = "";
		for (int i = 0; i < CommonValues.TESTFILE_INVALID_LIST.length; i++) {
			testpng = filePath + CommonValues.TESTFILE_INVALID_LIST[i];
			driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testpng);
			Thread.sleep(1000);

			WebElement web = driver.findElement(By.xpath("//div[@class='box-upload']/following::p[@class='error']"));

			if (web == null || !web.getText().contentEquals(CommonValues.ERROR_FILEFORMAT)) {
				
				failMsg = failMsg + "\n" +i+ ". error add file, invalid fileformat : " + CommonValues.TESTFILE_INVALID_LIST[i];
			}

		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	// 13. 발표자료 - 정상 파일 추가1
	@Test(priority = 13, enabled = true)
	public void presentation_file_valid() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//presentation list size
		int docs_beforeS = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']")).size();
		
		//add vaild file
		String testpng = filePath + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testpng);
		Thread.sleep(1000);
		
		List<WebElement> docs_after = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if((docs_beforeS+1) != docs_after.size()) {
			failMsg = "1. added file count error [Expected]" + (docs_beforeS+1) + " [Actual]" + docs_after.size();
		}  else {
			addedItem.add(CommonValues.TESTFILE_LIST[0]);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	
	
	// 14. 발표자료 - 발표자료 10개 초과
	@Test(priority = 14, enabled = true, dependsOnMethods = {"presentation_file_valid"})
	public void presentation_file_max() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//presentation list size
		int docs_beforeS = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']")).size();
		
		//add vaild file 9
		String testpng = "";
		for(int i = 1 ; i < 10 ; i++) {
			testpng = filePath + CommonValues.TESTFILE_LIST[i];
			driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testpng);
			addedItem.add(CommonValues.TESTFILE_LIST[i]);
			Thread.sleep(1000);
		}
		
		//add 1
		testpng = filePath + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath("//div[@class='box-upload']/input[@class='file']")).sendKeys(testpng);
		Thread.sleep(1000);
		assertEquals(closeAlertAndGetItsText(), MSG_FILE_EXCEED_ERROR);
		
		Thread.sleep(1000);
		List<WebElement> docs_after = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if((docs_beforeS+9) != docs_after.size()) {
			failMsg = "1. added file count error [Expected]" + (docs_beforeS+1) + " [Actual]" + docs_after.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 21. 발표자료 - 정렬 (14번에서 저장한 대로 재진입후 확인)
	@Test(priority = 21, enabled = true, dependsOnMethods = {"presentation_youtube_max", "presentation_file_max"})
	public void presentation_sort() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));

		for(int i = 0 ; i < docs_before.size() ; i ++) {
				
			if(!docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText().contains(addedItem.get(i))) {
				failMsg = failMsg + "\n " + i + ". file sort error [Expected]" + addedItem.get(i) 
						+ " [Actual]" + docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText();
			}
		}
		
		// save..
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']//button[1]")).click();
		Thread.sleep(1000);
		
		CommonValues comm = new CommonValues();
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
	
		// go to detail view
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();
		Thread.sleep(500);
		
		createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		if(!driver.getCurrentUrl().contentEquals(createViewUri)) {
			driver.get(createViewUri);
		}
		
		// presentation tab
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
		Thread.sleep(500);
		
		docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		for(int i = 0 ; i < docs_before.size() ; i ++) {
				
			if(!docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText().contains(addedItem.get(i))) {
				failMsg = failMsg + "\n " + (i+20) + ". file sort error(after save) [Expected]" + addedItem.get(i) 
						+ " [Actual]" + docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 22. 아이콘 클릭 - 유튜브
	@Test(priority = 22, enabled = true)
	public void presentation_youtube_icon() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		//click youtube icon
		docs_before.get(0).findElement(By.xpath(".//i[@class='icon youtube']")).click();
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(1));
	    
		Thread.sleep(1000);
		String youtubelink = "https://www.youtube.com/";
		//check room uri
		if(!driver.getCurrentUrl().contains(youtubelink))
	    {
			failMsg = "1. youtube icon do not work: " + driver.getCurrentUrl();
	    } {
	    	//close 1tab 
			driver.close();
			driver.switchTo().window(tabs2.get(0));
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 23. 아이콘 클릭 - 파일
	@Test(priority = 23, enabled = true)
	public void presentation_file_icon() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		//presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		//click file icon
		
		try {
			docs_before.get(11).findElement(By.xpath(".//div[@class='file-type-box']")).click();
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    if(tabs2.size() != 1) {
		    	driver.switchTo().window(tabs2.get(1));
		    	failMsg = "1. clickable file icon"; 
		    }
		} catch (ElementClickInterceptedException e) {
			
		}
		//check room uri
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI))
	    {
			failMsg = failMsg + "\n 2. file icon click error " + driver.getCurrentUrl();
	    } 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 24. 발표자료 삭제  - 취소
	@Test(priority = 24, enabled = true)
	public void presentation_delete1() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		int beforeCount = docs_before.size();
		
		// mouse hover
		Actions actions = new Actions(driver);
		WebElement web = docs_before.get(11).findElement(By.xpath(".//div[@class='file-type-box']"));
		actions.moveToElement(web).perform();
		Thread.sleep(100);
		
		//click delete button
		docs_before.get(11).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(100);
		
		//popup msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(MSG_DEETE_ITEM)) {
			failMsg = "1. delete popup msg error : [Expected]" + MSG_DEETE_ITEM 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(100);
		
		docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));

		if(beforeCount != docs_before.size()) {
			failMsg = failMsg + "\n 2. Presentations count error : [Expected]" + beforeCount 
					+ " [Actual]" +docs_before.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 25. 발표자료 삭제  - 확인
	@Test(priority = 25, enabled = true)
	public void presentation_delete2() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		int beforeCount = docs_before.size();
		
		// mouse hover
		Actions actions = new Actions(driver);
		WebElement web = docs_before.get(11).findElement(By.xpath(".//div[@class='file-type-box']"));
		actions.moveToElement(web).perform();
		Thread.sleep(100);
		
		//click delete button
		docs_before.get(11).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(100);
		
		//popup msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(MSG_DEETE_ITEM)) {
			failMsg = "1. delete popup msg error : [Expected]" + MSG_DEETE_ITEM 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(100);
		addedItem.remove(11);
		
		docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));

		if(beforeCount-1 != docs_before.size()) {
			failMsg = failMsg + "\n 2. Presentations count error : [Expected]" + beforeCount 
					+ " [Actual]" +docs_before.size();
		}
		
		for(int i = 0 ; i < docs_before.size() ; i ++) {
			
			if(!docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText().contains(addedItem.get(i))) {
				failMsg = failMsg + "\n " + (i+2) + ". file sort error(after delete) [Expected]" + addedItem.get(i) 
						+ " [Actual]" + docs_before.get(i).findElement(By.xpath(".//h4[@class='title']")).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 31. 유튜부-파일 설명란 기본
	@Test(priority = 31, enabled = true)
	public void presentation_des1() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		// des placeholder
		if(!docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']"))
				.getAttribute("placeholder").contentEquals(PLACEHOLDER_PRESENTION_DES)) {
			failMsg = "1. placeholder error : [Expected]" + PLACEHOLDER_PRESENTION_DES 
					+ " [Actual]" + docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']"))
					.getAttribute("placeholder");
		}
		
		if(!docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']"))
				.getAttribute("placeholder").contentEquals(PLACEHOLDER_PRESENTION_DES)) {
			failMsg = failMsg + "\n 2. placeholder error : [Expected]" + PLACEHOLDER_PRESENTION_DES 
					+ " [Actual]" + docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']"))
					.getAttribute("placeholder");
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 32. 유튜브 - 설명 max
	@Test(priority = 32, enabled = true)
	public void presentation_des2() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		// des max 200
		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).clear();
		for(int i = 0 ; i < 6 ; i ++) {
			docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).sendKeys(CommonValues.TWENTY_A + CommonValues.TWENTY_ONE);
		}
		Thread.sleep(500);
		docs_before.get(1).findElement(By.xpath(".//textarea[@class='input-content']")).click();
		
		//click tab3
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
		Thread.sleep(500);
		
		docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		if(docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).getText().length() != 100) {
			failMsg = "1. description length error [Expected]200 [Actual]" 
					+ docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).getText().length();
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 33. 유튜브 - 파일 설명 정상 입력
	@Test(priority = 33, enabled = true)
	public void presentation_des_valid() throws Exception {
		String failMsg = "";

		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}

		// presentation list size
		List<WebElement> docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
	
		// des valid
		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).clear();
		docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).sendKeys(des_youtube1);
		
		docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']")).clear();
		docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']")).sendKeys(des_file1);
		
		docs_before.get(1).findElement(By.xpath(".//textarea[@class='input-content']")).click();
		Thread.sleep(1000);
		
		//save..
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']//button[1]")).click();
		Thread.sleep(1000);
		
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
		if(driver.getCurrentUrl().contains(createViewUri)) {
			driver.get(createViewUri + seminarID);
			Thread.sleep(1000);
		}
		
		// click member tab
		if (!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI)) {
			driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
			Thread.sleep(500);
		}
		
		// click tab3
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]")).click();
		Thread.sleep(500);
		
		//check des
		docs_before = driver.findElements(By.xpath("//li[@class='DocItem_doc-item__2bSNb']"));
		
		if(!docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).getText().contentEquals(des_youtube1)) {
			failMsg = "1. youtube item description error [Exepcted]" + des_youtube1 
					+ " [Actual]" + docs_before.get(0).findElement(By.xpath(".//textarea[@class='input-content']")).getText();
		}
		if(!docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']")).getText().contentEquals(des_file1)) {
			failMsg = failMsg + "\n 2. file item description error [Exepcted]" + des_file1 
					+ " [Actual]" + docs_before.get(11).findElement(By.xpath(".//textarea[@class='input-content']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	// 100. 세미나 삭제
	@Test(priority = 100, enabled = true)
	public void seminarmember_deleteseminar() throws Exception {
		
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
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
