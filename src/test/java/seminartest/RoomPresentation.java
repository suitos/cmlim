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

/* RoomPresentation : rsrsup1
 * 0. 세미나 만들기 및 룸 입장. 시작
 * 1. 페이지가 10장이상인 ppt 문서공유, 썸네일 스크롤 젤 끝으로 이동후 선택. 페이지 정상인지 확인
 */

public class RoomPresentation {

	public static String PPT_FILE = "31p.pptx";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String seminarTitle = "";
	public String seminarID = "";
	public String seminarLink = "";
	
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
	
	// 0. 세미나 만들기 및 룸 입장. 시작
	@Test(priority=0)
	public void createSeminar() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL);
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime());
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		if (!driver.getCurrentUrl().contains(createViewUri)) {
			failMsg = "1. not create view" + driver.getCurrentUrl();
			driver.get(createViewUri);
		}
		comm.setCreateSeminar(driver, seminarTitle, true);
		
		// save seminar
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);
		Thread.sleep(500);

		// post seminar
		comm.postSeminar(driver, seminarID);
		
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
	    if(!driver.getCurrentUrl().contentEquals(detailview)) {
	    	driver.get(detailview);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
	    }
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
	    
		// 설정팝업 확인
		if (isElementPresent(By.xpath("//div[@id='device-setting-wrap']"))) {
			driver.findElement(By.xpath("//div[@class='buttons align-center']/button")).click();
			Thread.sleep(500);
		}
		
	    //start seminar
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)));
	    driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")));
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }	
	
	// 1. 페이지가 10장이상인 ppt 문서공유, 썸네일 스크롤 젤 끝으로 이동후 선택. 페이지 정상인지 확인
	@Test(priority=1, dependsOnMethods = {"createSeminar"}, enabled = true)
	public void loadDoc() throws Exception {
		String failMsg = "";
		
		//click doc icon
		driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_DOC_BTN)).click();
		
		List<WebElement> docitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
		int doccount = docitems.size();
		//upload file
		Thread.sleep(100);
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + PPT_FILE;
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(3000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//check added file
		docitems = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_DOC_ICON));
		if(doccount+1 == docitems.size()) {
			
			docitems.get(doccount).findElement(By.xpath("./div[@class='btn-rect']")).click();
			Thread.sleep(500);
			
			List<WebElement> docPage = driver.findElements(By.xpath("//div[@id='document-viewer']//li"));
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", docPage.get(docPage.size()-1));
			Thread.sleep(10000);
			
			if(docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src").isEmpty()
					|| !docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src").contains("decrypt?filePath=")) {
				failMsg = failMsg + "1. doc file thumbnail error [Actual : path]"
					+ docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src");
			}
			
			docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).click();
			Thread.sleep(500);
			
			if(driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").isEmpty()
					|| !driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("decrypt?filePath=")) {
				failMsg = failMsg + "\n2. doc file contents error [Actual : path]"
					+ docPage.get(docPage.size()-1).findElement(By.xpath(".//img")).getAttribute("src");
			}
			
		} else {
			failMsg = failMsg + "2. added file count error [Expected] " + doccount+1 + " [Actual]" + docitems.size();
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
