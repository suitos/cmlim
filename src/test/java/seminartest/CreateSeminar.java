package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
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
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* CreateSeminar
 * 
 * 3. 세미나 만들기 기본 시간(현재시간+3분)
 * 3. 배너의 시간 확인(설정 시간대로 표시됨)
 * 3. 세미나 제목 입력 확인 (placeholder, 40자 초과 입력 시도, 정상 케이스 입력 시도 후 배너 확인)
 * 3. 세미나 시간 선택 옵션 확인
 * 4. 세미나 참석인원 설정 확인 (최대 1000명, 0명 설정 안됨, 1000명이내(300명)설정 가능 확인)
 * 
 * 10. 상세화면 파일 첨부에 잘못된 형식의 파일 첨부 시도
 * 11. 세미나 상세화면 파일 첨부에 정상 파일 첨부, 10개 초과 파일 첨부 시도
 * 12. 상세화면 첨부된 파일 삭제 ㅣ
 * 
 * 20. 상세화면  속성 선택
 * 21. 저장완료 후 속성 변경
 */
public class CreateSeminar {
	public static String BANNER_MSG = "The presenter set in the member setting is entered.";
	public static String CHECKBOX_SECRETMODE = "Incognito mode (seminar will not be listed on the channel)";
	
	public static String XPATH_CREATESEMINAR_SEMINARYTPE = "//div[@id='seminar-type']/div[@class='text']";
	public static String XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE = "//input[@name='dropzone']";
	
	public static WebDriver driver;
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
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
		Thread.sleep(100);
		driver.get(CommonValues.SERVER_URL);

        System.out.println("End BeforeTest!!!");
	}
	
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver);

	  }	

	@Test(priority = 1)
	public void gotoCreateSeminar() throws Exception {

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			Exception e = new Exception("Seminar Edit view : " + driver.getCurrentUrl());
			throw e;
		}

	}

	// 3. 세미나 만들기 기본 시간(현재시간+3분)
	@Test(priority = 3)
	public void checkStartTime() throws Exception {
		String failMsg = "";

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = format2.format(cal.getTime());

		if (!driver.findElement(By.xpath("//div[@class='react-datepicker__input-container']")).getText().trim()
				.contentEquals(date1)) {
			failMsg = "Default Seminar date [actual] : "
					+ driver.findElement(By.xpath("//div[@class='react-datepicker__input-container']")).getText().trim()
					+ " [expected] : " + date1;

		}

		// check time
		cal.add(Calendar.MINUTE, 3);
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String time1 = format1.format(cal.getTime());

		if (!driver.findElement(By.name("startTime")).getAttribute("value").trim().contentEquals(time1)) {
			failMsg = failMsg + "\n" + "Default Seminar starts time [actual] : "
					+ driver.findElement(By.name("startTime")).getAttribute("value").trim() + " [expected] : " + time1;

		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 3. 배너의 시간 확인(설정 시간대로 표시됨)
	@Test(priority = 3)
	public void checkBannerTime() throws Exception {

		String failMsg = "";
		// Date : default today
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
		String date1 = format2.format(cal.getTime());

		SimpleDateFormat endtime = new SimpleDateFormat("HH:mm");
		String setTime = driver.findElement(By.name("startTime")).getAttribute("value");
		Date enddate = endtime.parse(setTime);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(enddate);
		cal2.add(Calendar.MINUTE, 30);

		String seminarDate = date1 + " " + setTime + " ~ " + endtime.format(cal2.getTime());

		// check banner date
		if (!driver.findElement(By.xpath("//div[@class='wrap-info']/div[@class='date']")).getText()
				.contentEquals(seminarDate)) {
			failMsg = "1. Banner Seminar time [expected] : " + seminarDate + " [actual] : "
					+ driver.findElement(By.xpath("//div[@class='wrap-info']/div[@class='date']")).getText();
		}
		
		if (!driver.findElement(By.xpath("//div[@class='wrap-info']/div[@class='author']")).getText().contentEquals(BANNER_MSG)) {
			failMsg = failMsg + "\n 2. Banner message [Expected]" + BANNER_MSG 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='wrap-info']/div[@class='author']")).getText() ;
		}

		if (driver.findElement(By.xpath("//div[@class='wrap-info']/div[@class='date']")).isSelected()) {
			failMsg = failMsg + "\n 3. Banner Date is selected";
		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}
	
	// 3. 세미나 제목 입력 확인 (placeholder, 40자 초과 입력 시도, 정상 케이스 입력 시도 후 배너 확인)
	@Test(priority = 3)
	public void checkTitle() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).click();

		// check title placeholder
		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_TITLE)) {
			failMsg = "1. Wrong title place holder : "
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("placeholder");
		}

		// send 41 strings in title
		String longtitle = CommonValues.TWENTY_ONE + CommonValues.TWENTY_ONE;
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(longtitle + "b");
		Thread.sleep(1000);
		assertEquals(closeAlertAndGetItsText(), CommonValues.LONG_TITEL_ERROR);

		if (!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value").contentEquals(longtitle)) {
			failMsg = failMsg + "\n2.wrong seminar title : "
					+ driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).getAttribute("value");

		}

		// 정상 케이스 제목 입력
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(CommonValues.VALID_SNAME);
		Thread.sleep(1000);
		// 배너에서 제목 확인
		if (!driver.findElement(By.xpath("//div[@class='title']")).getText().contentEquals(CommonValues.VALID_SNAME)) {
			failMsg = failMsg + "\n3.wrong banner title : "
					+ driver.findElement(By.xpath("//div[@class='title']")).getText();
		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	  
	// 3. 세미나 시간 선택 옵션 확인
	@Test(priority = 3)
	public void checkDate() throws Exception {
		String failMsg = "";

		// check select item
		for (int i = 0; i < CommonValues.SEMINAR_TIME.length; i++) {
			String xpath = "//div[@class='date']/select[@class='duration']/option[" + (i + 1) + "]";
			if (!driver.findElement(By.xpath(xpath)).getText().contentEquals(CommonValues.SEMINAR_TIME[i])) {
				failMsg = failMsg + "\n" + "Fail Seminar Date time  : " + driver.findElement(By.xpath(xpath)).getText();
			}
		}

		String lastitem = "//div[@class='date']/select[@class='duration']/option[" + (CommonValues.SEMINAR_TIME.length + 1) + "]";
		if (isElementPresent(By.xpath(lastitem))) {
			failMsg = failMsg + "\n" + "More time items  : " + driver.findElement(By.xpath("lastitem")).getText();

		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 4. 세미나 참석인원 설정 확인 (최대 1000명, 0명 설정 안됨, 1000명이내(300명)설정 가능 확인)
	@Test(priority = 4)
	public void checkAttendees() throws Exception {
		String failMsg = "";
  
		CommonValues comm = new CommonValues();
		
		//send 9000 in Attendees
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)));
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).sendKeys("9000");
		Thread.sleep(1000);
		assertEquals(closeAlertAndGetItsText(), CommonValues.OVER_ATTEND_ERROR);
		
		//send 0 (invalid value.)
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).sendKeys("0");
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).click();
		if(!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).getAttribute("value").contentEquals("1"))
		{
			failMsg = "wrong default Attendees(input 0) : " + driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).getAttribute("value");
		 
		}
		
		//send 300(valid)
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).sendKeys("300");
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).click();
		if(!driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).getAttribute("value").contentEquals("300"))
		{
			failMsg =  failMsg + "\n" + "wrong default Attendees(input 300) : " + driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_ATTENDEES)).getAttribute("value");
		    
		}
		
		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	

			
	// 10. 상세화면 파일 첨부에 잘못된 형식의 파일 첨부 시도
	@Test(priority = 10, enabled = false)
	public void checkSharefile_invalid() throws Exception {
		String failMsg = "";

		// add invaild file
		String testpng = "";
		
		String filePath = CommonValues.TESTFILE_PATH;
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = CommonValues.TESTFILE_PATH_MAC;

		for (int i = 0; i < CommonValues.TESTFILE_INVALID_LIST.length; i++) {
			testpng = filePath + CommonValues.TESTFILE_INVALID_LIST[i];
			driver.findElement(By.xpath(XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)).sendKeys(testpng);
			Thread.sleep(2000);

			WebElement web = driver.findElement(By.xpath("//div[@class='UploadDocument_upload-box__jvaP3']/p[@class='error']"));

			if (web == null || !web.getText().contentEquals(CommonValues.ERROR_FILEFORMAT)) {
				failMsg = failMsg + "\n" + "error add file, invalid fileformat : "
						+ CommonValues.TESTFILE_INVALID_LIST[i];

			}
		}
		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. 세미나 상세화면 파일 첨부에 정상 파일 첨부, 10개 초과 파일 첨부 시도
	@Test(priority = 11, enabled = true)
	public void checkSharefile_valid() throws Exception {
		String failMsg = "";

		String filePath = CommonValues.TESTFILE_PATH;
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = CommonValues.TESTFILE_PATH_MAC;
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)));
		
		String testpng = "";
		for (int i = 0; i < CommonValues.TESTFILE_LIST.length; i++) {
			testpng = filePath + CommonValues.TESTFILE_LIST[i];
			driver.findElement(By.xpath(XPATH_CREATESEMINAR_SHAREDFILE_DROPZONE)).sendKeys(testpng);
			Thread.sleep(500);
			
			if (i >= 10) {
				assertEquals(closeAlertAndGetItsText(), CommonValues.OVER_FILES_ERROR);
				System.out.println("check add file count 11");
				break;
			}

			Thread.sleep(2000);

			String finditem = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.TESTFILE_LIST[i]
					+ "']";
			WebElement web = driver.findElement(By.xpath(finditem));

			if (web == null || !web.getTagName().contentEquals("span")) {
				failMsg = failMsg + "\n" + "error add file, filename : " + CommonValues.TESTFILE_LIST[i];

			}

		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	  
	// 12. 상세화면 첨부된 파일 삭제 
	@Test(priority = 12)
	public void checkSharefile_delete() throws Exception {
		// check before count

		int items = driver
				.findElements(By.xpath("//div[@class='wrap-preview']/div[@class='DocumentItem_previewItem__156u8']"))
				.size();

		// delete 2 files
		driver.findElement(
				By.xpath("//div[@class='wrap-preview']/div[@class='DocumentItem_previewItem__156u8']/div[1]")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(
				By.xpath("//div[@class='wrap-preview']/div[@class='DocumentItem_previewItem__156u8']/div[1]")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		int afterdel = driver
				.findElements(By.xpath("//div[@class='wrap-preview']/div[@class='DocumentItem_previewItem__156u8']"))
				.size();

		if (items - 2 != afterdel) {
			Exception e = new Exception(
					"Fail Pre File Delete : before delete count " + (items) + ", after delete count " + (afterdel));
			throw e;
		}

	}

	// 20. 상세화면  속성 선택
	@Test(priority = 20)
	public void seminartype() throws Exception {
		String failMsg = "";
		
		//defalut
		if(!driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText().contentEquals("Public seminar")) {
			failMsg = "1. seminar type defalut value [Expected]Public seminar [Actual]" 
					+ driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
		}
		
		if(isElementPresent(By.xpath("//div[@class='checkbox']"))) {
			failMsg = failMsg + "\n2. find secret mode check box";
		}
		
		//click selectbox
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).click();
		Thread.sleep(100);
		
		//type check
		if(isElementPresent(By.xpath("//div[@class='box-option open']"))) {
			if(!driver.findElement(By.xpath("//div[@class='box-option open']/div[1]//p[@class='title']")).getText().contentEquals("Public seminar")) {
				failMsg = failMsg + "\n3. type selectbox item 1[Expected]Public seminar [Actual]" 
						+ driver.findElement(By.xpath("//div[@class='box-option open']/div[1]//p[@class='title']")).getText();;
			}
			if(!driver.findElement(By.xpath("//div[@class='box-option open']/div[2]//p[@class='title']")).getText().contentEquals("Private seminar")) {
				failMsg = failMsg + "\n4. type selectbox item 1[Expected]Private seminar [Actual]" 
						+ driver.findElement(By.xpath("//div[@class='box-option open']/div[2]//p[@class='title']")).getText();;
			}
			
			//click private
			driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(100);
			if(!driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText().contentEquals("Private seminar")) {
				failMsg = failMsg + "5. seminar type defalut value [Expected]Private seminar [Actual]" 
						+ driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
			}
			
			if(isElementPresent(By.xpath("//div[@class='checkbox']"))) {
				if(!driver.findElement(By.xpath("//div[@class='checkbox']//span")).getText().contentEquals(CHECKBOX_SECRETMODE)) {
					failMsg = failMsg + "6. secret check box [Expected]" + CHECKBOX_SECRETMODE + " [Actual]" 
							+ driver.findElement(By.xpath("//div[@class='checkbox']//span")).getText();
				}
			}else {
				failMsg = failMsg + "\n7. find secret mode check box";
			}
			
		}else {
			failMsg = failMsg + "\n8. cannot find type select menu";
		}
		
		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 21. 저장완료 후 속성 확인(수정가능)
	@Test(priority = 21)
	public void seminartype_saveas() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd hh:mm:ss");
		String title = "type test" + format2.format(cal.getTime());

		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, title, false);

		String type = driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
		
		// click save as..
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, title);

		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver.getCurrentUrl().contentEquals(detailView)) {
			driver.get(detailView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		driver.findElement(By.xpath(CreateSeminar2.XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);
		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
			failMsg = failMsg + "1. no edit view (draft seminar)";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			Thread.sleep(500);
		} else {
			if(!driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText().contentEquals(type)) {
				failMsg = failMsg + "\n 2. seminar type [Expected]" + type 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
			}
		}
		
		//click selectbox
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).click();
		Thread.sleep(100);

		//type check
		if (isElementPresent(By.xpath("//div[@class='box-option open']"))) {

			if (isElementPresent(By.xpath("//div[@class='checkbox']"))) {
				if (!driver.findElement(By.xpath("//div[@class='checkbox']//span")).getText()
						.contentEquals(CHECKBOX_SECRETMODE)) {
					failMsg = failMsg + "6. secret check box [Expected]" + CHECKBOX_SECRETMODE + " [Actual]"
							+ driver.findElement(By.xpath("//div[@class='checkbox']//span")).getText();
				}
			} else {
				failMsg = failMsg + "\n7. find secret mode check box";
			}

		} else {
			failMsg = failMsg + "\n8. cannot find type select menu";
		}	
		
		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 게시완료 후 속성 확인(수정불가)
	@Test(priority = 22)
	public void seminartype_post() throws Exception {
		String failMsg = "";	

		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			Thread.sleep(500);
		} 
		
		String type = driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
		
		// click save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath(CommonValues.XPATH_MODAL_BODY))) {
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(1000);
		}

		CommonValues comm = new CommonValues();
		comm.postSeminar(driver, seminarID);
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath(CreateSeminar2.XPATH_SEMINARVIEW_EDIT_BTN)).click();
		Thread.sleep(500);
		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
			failMsg = failMsg + "1. no edit view (draft seminar)";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
			Thread.sleep(500);
		} else {
			if(!driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText().contentEquals(type)) {
				failMsg = failMsg + "\n 2. seminar type [Expected]" + type 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_CREATESEMINAR_SEMINARYTPE)).getText();
			}
		}
		
		if(driver.findElement(By.xpath("//div[@id='seminar-type']")).isEnabled()) {
			//failMsg = failMsg + "\n 3. type select box is enabled.";
			
			driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
			Thread.sleep(100);

			//type check
			if (isElementPresent(By.xpath("//div[@class='box-option open']"))) {
				failMsg = failMsg + "\n 4. type select box is clickable.";
			}
		}
		
		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			
/*
	@Test(priority = 51)
	public void standby_video() throws Exception {
		String failMsg = "";
		
		//click video box
		driver.findElement(By.id("cover-mode")).click();
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Image'])[2]/following::div[1]")).click();
	    
	    Thread.sleep(500);
		// check input values (hello image)
		if (!driver.findElement(By.xpath("//div[@class='ql-container ql-snow']")).getText().contentEquals("hello image")) {
			failMsg = failMsg + "input message check : [Expected] hello image, [Actual] "
					+ driver.findElement(By.xpath("//div[@class='ql-container ql-snow']")).getText();
		}
		
		// input valid text (hello video)
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).click();
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).clear();
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).sendKeys("hello video");
		
		// save as click
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(1000);

		// check input values
		if (!driver.findElement(By.xpath("//div[@class='ql-container ql-snow']")).getText().contentEquals("hello video")) {
			failMsg = failMsg + "\n input message check [Expected] hello video, [Actual] "
					+ driver.findElement(By.xpath("//div[@class='ql-container ql-snow']")).getText();
		}
		
		// youtube inputbox placeholder
		if(!driver.findElement(By.xpath("//div[@class='linkBox']/input")).getAttribute("placeholder").contentEquals("Enter Youtube link")) {
			failMsg = failMsg + "\n youtube link input box placeholder error : [Actual] " + driver.findElement(By.xpath("//div[@class='linkBox']/input")).getAttribute("placeholder");
		}
		
		//input youtube invalid link 
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).click();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).clear();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).sendKeys("hello video");
		
		// save as click
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(2000);
		
		assertEquals(closeAlertAndGetItsText(), CommonValues.ERROR_YOUTUBELINK);
		
		// input youtube valid link
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).click();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).clear();
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).sendKeys(CommonValues.YOUTUBE_URL[0]);

		// save as click
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(2000);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 70)
	public void createNormalSeminar() throws Exception {

		driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);

		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, CommonValues.VALID_SNAME, false);
		
		//click save as..
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(1000);
		
		String seminarEdit = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
		seminarID = driver.getCurrentUrl().replace(seminarEdit, "");
		System.out.println(" currentURL : " + driver.getCurrentUrl());
		System.out.println(" seminarID : " + seminarID);
		
		
		//click post
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[2]")).click();
		Thread.sleep(500);
		
		//click popup post
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		
		Thread.sleep(2000);
		String compleateUri = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI + seminarID;
		if (!compleateUri.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("fail to create seminar, current url: " + driver.getCurrentUrl());
			throw e;
		}
	}
	  
	@Test(priority = 71)
	public void checkCompleateView() throws Exception {
		String failMsg = "";
		String compleateUri = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI + seminarID;
		String inviteview = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='wrap-button']/a[1]")));
		
		driver.findElement(By.xpath("//div[@class='wrap-button']/a[1]")).click();
		Thread.sleep(1000);
		if (!driver.getCurrentUrl().equalsIgnoreCase(inviteview)) {
			failMsg = "No Invite list view : [Actual]" + driver.getCurrentUrl() + " [Expected]" + inviteview;

		}

		driver.get(compleateUri);
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='wrap-button']/a[2]")));
		driver.findElement(By.xpath("//div[@class='wrap-button']/a[2]")).click();
		Thread.sleep(1000);
		if (!driver.getCurrentUrl().equalsIgnoreCase(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "\n" + "No seminar list view : [Actual]" + driver.getCurrentUrl() + " [Expected]"
					+ CommonValues.SERVER_URL + CommonValues.LIST_URI;
		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}
	  
	@Test(priority = 72)
	public void detailView() throws Exception {
		String failMsg = "";
		String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
		driver.get(detailview);

		driver.findElement(By.xpath("//div[@class='ricon ricon-link']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();

		Thread.sleep(1000);
		String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
				.getData(DataFlavor.stringFlavor);

		if (!clipboardtxt.equalsIgnoreCase(seminarLink)) {
			failMsg = "No match clipboard data: " + clipboardtxt;
		}

		// check edit view
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		Thread.sleep(1000);

		if (!driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).getAttribute("value")
				.contentEquals(CommonValues.VALID_SNAME)) {
			failMsg = failMsg + "\n" + "wrong seminar title : "
					+ driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).getAttribute("value");

		}	

	
		// check delete button
		driver.get(detailview);
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();

		Thread.sleep(500);
		if (!driver.getCurrentUrl().equalsIgnoreCase(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "\n" + "No seminar list view : " + driver.getCurrentUrl() + "&&"
					+ CommonValues.SERVER_URL + CommonValues.LIST_URI;

		}

		if (!failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
*/	
	
	@Test(priority = 100, enabled = true)
	public void deleteSeminar() throws Exception {
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	  public void clearAttributeValue(WebElement el) 
	  {
			while(!el.getAttribute("value").isEmpty())
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
