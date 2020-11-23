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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* QnATest
 * 0.세미나 생성
 * 1. 게시자겸 발표자 세미나 룸입장, 세미나 시작
 * 2. 참석자 A 입장
 * 3. 참석자 B : standby 입장
 * 11. 참석자A 질믄 입력, 발표자 질문 고정.
 * 12. 참석자B 질믄 입력 (참석자A,참석자B 질믄 1개씩 2개), 질문두개 고정,  참석자A가 본인 질문 삭제, 발표자가 B질문 삭제
 * 13. 참석자A2,B 1질믄 입력, 발표자가 질문 3개째 고정 시도(최대2개)
 * 14. 비공개 질문 고정 시도
 * 20. 공개 질문 좋아요 클릭 
 * 30. 비공개 질문에 답변, 공개 질문 좋아요 클릭 
 * 100. 세미나 종료
 * 101. 질문 통계확인
 */
public class QnATest {

	public String XPATH_QNA_PIN_BTN = ".//button[@class='qna-list-item__pin-btn']";
	public String XPATH_QNA_UNPIN_BTN = ".//button[@class='qna-list-item__pin-btn pinned']";
	public String XPATH_QNA_PINED_BODY = "./div[@class='qna-accordian__children-wrapper']";
	public String XPATH_QNA_HEADER_UNPIN_BTN = ".//div[@class='QnaTooltip_qna__tooltip__2kVq3']/button";
	public String XPATH_QNA_PIN_TOOLTIP = ".//span[@class='qna__tooltip__content left']";
	public String XPATH_QNA_UNPIN_TOOLTIP = "./div[@class='qna-accordian__children-wrapper']//span[@class='qna__tooltip__content left']";
	public String XPATH_QNA_FIXED_Q = "//div[@class='undefined scrollbar']/div";
	public String XPATH_QNA_FIXED_CLOSE_CLASSNAME = "QnaAccordian_qna-accordian__3d2Xw false";
	public String XPATH_QNA_FIXED_OPEN_CLASSNAME = "QnaAccordian_qna-accordian__3d2Xw active";
	public String XPATH_QNA_FIXED_OPEN_BTN = ".//div[@class='qna-accordian__header__right']";
	public String XPATH_QNA_QUESTION_DELETE_BTN = ".//button[@class='content-box__utills__close-btn']";
	public String XPATH_QNA_QUESTION_NICKNAME = ".//span[@class='content-box__nickname']";
	public String XPATH_QNA_QUESTION_LIKE_ICON = ".//button[@class='qna-list-item__like-btn']";
	public String XPATH_QNA_QUESTION_LIKEED_ICON = ".//button[@class='qna-list-item__like-btn liked']";
	public String XPATH_QNA_QUESTION_LIKE_COUNT = ".//span[@class='like-count']";
	
	public String MSG_QNA_PIN_TOOLTIP = "Pin";
	public String MSG_QNA_PIN_OVER = "The number of questions that can be pinned has been exceeded.";
	public String MSG_QNA_PIN_PRIVATE = "This is a private question.";
	
	public String MSG_QNA_PINOFF_TOOLTIP = "Unpin";
	public String QNA_QUESTION_PUBLIC = "QnA Test : public";
	public String QNA_QUESTION_PRIVATE = "QnA Test : private";
	public String QNA_ANSWER = "QnA Test : public answer";
	public String QNA_PRIVATE_ANSWER = "QnA Test : private answer";	
	
	public static String PresenterNickname = "NickName";
	
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
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
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
		
		Thread.sleep(500);

		// check seminar type
		driver.findElement(By.id("seminar-type")).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[1]")).click();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TITLE)).sendKeys(seminarName);

		driver.findElement(By.name("startTime")).sendKeys(seminarTime);
		Thread.sleep(2000);

		//presentation tab
		String presentationurl = CommonValues.SERVER_URL + CommonValues.CREATE_PRESENTATION_URI;
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB4)).click();

		Thread.sleep(1000);
		if (!driver.getCurrentUrl().contains(presentationurl)) {
			Exception e = new Exception("no presentation View : " + driver.getCurrentUrl());
			throw e;
		}
	
		// save seminar
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(2000);
		
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
		seminarID = createSeminar("QnA " + format2.format(cal.getTime()), time1);
	}
	
	//1. 게시자겸 발표자 세미나 룸입장, 세미나 시작
	@Test(priority = 1, enabled = true)
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
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		closeAlertAndGetItsText_webdriver(driver);
		
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
	
	// 2. 참석자 A 입장
	@Test(priority = 2, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
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
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys(OnAirRoom.ATTENDEES_NICKNAME + "1");
		
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).click();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).clear();
		attendADriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_EMAIL)).sendKeys(OnAirRoom.ATTENDEES_EMAIL);
		
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
		
		closeAlertAndGetItsText_webdriver(attendADriver);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendADriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("USER A fail to join Seminar : " + attendADriver.getCurrentUrl());
	    	throw e;
	    }
		
	}
	
	
	// 3. 참석자 B : standby 입장
	@Test(priority = 3, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
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
		attendBDriver.findElement(By.xpath(AttendeesTest.XPATH_ATTEND_NICKNAME)).sendKeys(OnAirRoom.ATTENDEES_NICKNAME + "2");
		
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
		
		closeAlertAndGetItsText_webdriver(attendBDriver);
		
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(attendBDriver.getCurrentUrl()))
	    {
	    	Exception e =  new Exception("USER B fail to join Seminar : " + attendBDriver.getCurrentUrl());
	    	throw e;
	    }
		
	}	

	// 11. 참석자A 질믄 입력, 발표자 질문 고정.
	@Test(priority = 11, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qnaPublicPin() throws Exception {
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
			String msg = checkQuestion(qnas.get(0), OnAirRoom.ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
			failMsg = failMsg + (msg.isEmpty()?msg:("\n2." + msg));
			
			//질문 고정 툴팁확인
			Actions actions = new Actions(driver);
			actions.moveToElement(qnas.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN))).perform();
			Thread.sleep(100);
			
			if(qnas.get(0).findElement(By.xpath(XPATH_QNA_PIN_TOOLTIP)).isDisplayed()){
				if(!qnas.get(0).findElement(By.xpath(XPATH_QNA_PIN_TOOLTIP)).getText().contentEquals(MSG_QNA_PIN_TOOLTIP)) {
					failMsg = failMsg + "\n3. pin icon tooltip [Expected]" + MSG_QNA_PIN_TOOLTIP 
							+ " [Actual]" + qnas.get(0).findElement(By.xpath(XPATH_QNA_PIN_TOOLTIP)).getText();
				}
				
			} else {
				failMsg = failMsg + "\n4. cannot find pin tooltip.";
			}
			
			//질문 고정
			qnas.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
			Thread.sleep(500);
			
			List<WebElement> fixedQ = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			if(fixedQ.size() != 1) {
				failMsg = failMsg + "\n5. fixed question size [Expected]1 [Actual]" + fixedQ.size();
			} else {
				//고정질문 확인
				if(fixedQ.get(0).getAttribute("class").contentEquals(XPATH_QNA_FIXED_CLOSE_CLASSNAME)) {
					fixedQ.get(0).findElement(By.xpath(XPATH_QNA_FIXED_OPEN_BTN)).click();
					Thread.sleep(100);
				}
				msg = checkQuestion(fixedQ.get(0), OnAirRoom.ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
				failMsg = failMsg + (msg.isEmpty()?msg:("\n6." + msg));
			}
			
			//참석자B 질문탭 뱃지 및 질문 확인
			try {
				if(!attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText().contentEquals("1")) {
					failMsg = failMsg + "7. QnA bedge count [Expected]1 [Actual]" 
							+ attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA_BEDGE)).getText();
				}
			} catch (NoSuchElementException e) {
				failMsg = failMsg + "\n 8. cannot find QnA bedge";
			}
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
			
			List<WebElement> qnasB = attendBDriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			if(qnasB.size() != 1) {
				failMsg = failMsg + "\n 9. QnA count [Expected]1 [Actual]" + qnasB.size();
			} else {
				if(qnasB.get(0).getAttribute("class").contentEquals(XPATH_QNA_FIXED_CLOSE_CLASSNAME)) {
					qnasB.get(0).findElement(By.xpath(XPATH_QNA_FIXED_OPEN_BTN)).click();
					Thread.sleep(100);
				}
				String msgB = checkQuestion(qnasB.get(0), OnAirRoom.ATTENDEES_NICKNAME + "1", QNA_QUESTION_PUBLIC, true);
				failMsg = failMsg + (msg.isEmpty()?msg:("\n10." + msgB));
			}	
			
			//고정 해제

			//tooltip
			fixedQ = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			actions.moveToElement(fixedQ.get(0).findElement(By.xpath(XPATH_QNA_UNPIN_BTN))).perform();
			Thread.sleep(200);
			if(fixedQ.get(0).findElement(By.xpath(XPATH_QNA_UNPIN_TOOLTIP)).isDisplayed()){
				if(!fixedQ.get(0).findElement(By.xpath(XPATH_QNA_UNPIN_TOOLTIP)).getText().contentEquals(MSG_QNA_PINOFF_TOOLTIP)) {
					failMsg = failMsg + "\n11. pin icon tooltip [Expected]" + MSG_QNA_PINOFF_TOOLTIP 
							+ " [Actual]" + fixedQ.get(0).findElement(By.xpath(XPATH_QNA_UNPIN_TOOLTIP)).getText();
				}
				
			} else {
				failMsg = failMsg + "\n12. cannot find pin off tooltip.";
			}
			
			fixedQ.get(0).findElement(By.xpath(XPATH_QNA_UNPIN_BTN)).click();
			Thread.sleep(500);
			//발표자쪽 질문리스트 현황
			fixedQ = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			qnas = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
			if(fixedQ.size() != 0 || qnas.size() !=1) {
				failMsg = failMsg + "\n13. presenter's qna status [Expected]pined Question size 0"
						+ " [Actual]pined Question size : " + fixedQ.size();
			}
			
			//참석자B 질문리스트 현황
			qnasB = attendBDriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			if(qnasB.size() != 0 ) {
				failMsg = failMsg + "\n14. attendeeB qna status [Expected]pined Question size 0"
						+ " [Actual]pined Question size" + qnasB.size();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 12. 참석자B 질믄 입력 (참석자A,참석자B 질믄 1개씩 2개), 질문두개 고정,  참석자A가 본인 질문 삭제, 발표자가 B질문 삭제
	@Test(priority = 12, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qnaPined_delete() throws Exception {
		String failMsg = "";
				
		//참석자B 질문작성
		addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		
		//발표자가 질문 두개 고정
		List<WebElement> qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas_pre.size() == 2) {
			qnas_pre.get(1).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
			qnas_pre.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
		}

		//참석자A 본인질문 삭제
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> fixedA = attendADriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		for (WebElement we : fixedA) {
			if(we.getAttribute("class").contentEquals(XPATH_QNA_FIXED_CLOSE_CLASSNAME)) {
				we.findElement(By.xpath(XPATH_QNA_FIXED_OPEN_BTN)).click();
				Thread.sleep(500);
			}
			if(we.findElement(By.xpath(XPATH_QNA_QUESTION_NICKNAME)).getText().contentEquals(OnAirRoom.ATTENDEES_NICKNAME + "1")) {
				we.findElement(By.xpath(XPATH_QNA_QUESTION_DELETE_BTN)).click();
				Thread.sleep(500);
				attendADriver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);
			}
		}
		fixedA = attendADriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedA.size() != 1) {
			failMsg = "1. Attendee1's fixed question size [Expected]1 [Actual]" + fixedA.size();
		}
		
		//발표자
		List<WebElement> fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixed_pre.size() != 1) {
			failMsg = failMsg + "\n2. presenter's fixed question size [Expected]1 [Actual]" + fixed_pre.size();
		}
		
		//참석자B
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> fixedB = attendBDriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedB.size() != 1) {
			failMsg = failMsg + "\n3. Attendee2's fixed question size [Expected]1 [Actual]" + fixedB.size();
		}
		
		//발표자가 고정된 참석자B질문 삭제
		if(fixed_pre.get(0).getAttribute("class").contentEquals(XPATH_QNA_FIXED_CLOSE_CLASSNAME)) {
			fixed_pre.get(0).findElement(By.xpath(XPATH_QNA_FIXED_OPEN_BTN)).click();
			Thread.sleep(500);
		}
		fixed_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_DELETE_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);
		
		//참석자A
		fixedA = attendADriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedA.size() != 0) {
			failMsg = failMsg + "\n4. Attendee1's fixed question size [Expected]0 [Actual]" + fixedA.size();
		}
		
		//발표자
		fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixed_pre.size() != 0) {
			failMsg = failMsg + "\n4. presenter's fixed question size [Expected]0 [Actual]" + fixed_pre.size();
		}
		
		//참석자B
		fixedB = attendBDriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedB.size() != 0) {
			failMsg = failMsg + "\n5. Attendee2's fixed question size [Expected]0 [Actual]" + fixedB.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 13. 참석자A2,B 1질믄 입력, 발표자가 질문 3개째 고정 시도(최대2개)
	@Test(priority = 13, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qnaPined_max() throws Exception {
		String failMsg = "";
		
		
		List<WebElement> qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas_pre.size() == 0) {
			addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
			addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
			addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		} else if(qnas_pre.size() == 1) {
			addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
			addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		} else if(qnas_pre.size() == 2) {
			addQuestion(attendBDriver, QNA_QUESTION_PUBLIC, true);
		}
		
		//발표자가 질문 두개 고정
		int qnasize = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST)).size();
		if(qnasize != 3) {
			failMsg = "1. Presenter's question size [Exepcted]3" + " [Actual]" + qnasize;
		} else {
			List<WebElement> fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			int fixcount = 2 - fixed_pre.size();
			
			for(int i = 0 ; i < fixcount ; i++) {
				qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
				qnas_pre.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
				Thread.sleep(500);
			}
		}

		qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		qnas_pre.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
		Thread.sleep(500);
		if(isElementPresent_wd(driver, By.xpath(CommonValues.XPATH_MODAL_BODY))) {
			if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_QNA_PIN_OVER)) {
				failMsg = failMsg + "\n2. pin to exceed popup message [Expected]" + MSG_QNA_PIN_OVER 
						+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
			Thread.sleep(500);
		}else {
			failMsg = failMsg + "\n2. cannot find popup.";
		}
		
		
		//고정 1개 해제
		List<WebElement> fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixed_pre.size() == 2) {
			if(fixed_pre.get(1).getAttribute("class").contentEquals(XPATH_QNA_FIXED_CLOSE_CLASSNAME)) {
				fixed_pre.get(1).findElement(By.xpath(XPATH_QNA_FIXED_OPEN_BTN)).click();
				Thread.sleep(500);
			}
			fixed_pre.get(1).findElement(By.xpath(XPATH_QNA_UNPIN_BTN)).click();
			Thread.sleep(500);
		}
		
		//고정 1개. 총2개
		qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnas_pre.size() >= 2) {
			qnas_pre.get(1).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
			Thread.sleep(500);
		}
		
		//참석자A
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> fixedA = attendADriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedA.size() != 2) {
			failMsg = failMsg + "\n3. Attendee1's fixed question size [Expected]2 [Actual]" + fixedA.size();
		}
		
		//발표자
		fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixed_pre.size() != 2) {
			failMsg = failMsg + "\n4. presenter's fixed question size [Expected]2 [Actual]" + fixed_pre.size();
		}
		
		//참석자B
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> fixedB = attendBDriver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
		if(fixedB.size() != 2) {
			failMsg = failMsg + "\n5. Attendee2's fixed question size [Expected]2 [Actual]" + fixedB.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 14. 비공개 질문 고정 시도
	@Test(priority = 14, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qna_pinPrivate() throws Exception {
		String failMsg = "";
		
		//기존 질문 삭제
		int fixedQ_count = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q)).size();
		for(int i = 0 ; i < fixedQ_count ; i++) {
			List<WebElement> fixed_pre = driver.findElements(By.xpath(XPATH_QNA_FIXED_Q));
			fixed_pre.get(0).findElement(By.xpath(XPATH_QNA_HEADER_UNPIN_BTN)).click();
			Thread.sleep(500);
		}
		
		int question_count = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST)).size();
		for(int i = 0 ; i < question_count ; i++) {
			List<WebElement> qnas_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
			qnas_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_DELETE_BTN)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		}
		
		//참석자A 비공개 질믄
		addQuestion(attendADriver, QNA_QUESTION_PRIVATE, false);
		
		//발표자가 비공개 질문 고정 시도
		List<WebElement> qnaPrivate_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qnaPrivate_pre.size() != 1) {
			failMsg = "1. cannot find private question. question size : " + qnaPrivate_pre.size();
		} else {
			qnaPrivate_pre.get(0).findElement(By.xpath(XPATH_QNA_PIN_BTN)).click();
			Thread.sleep(500);
			
			if(isElementPresent_wd(driver, By.xpath(CommonValues.XPATH_MODAL_BODY))) {
				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_QNA_PIN_PRIVATE)) {
					failMsg = failMsg + "\n2. pin error message [Exepcted]" + MSG_QNA_PIN_PRIVATE 
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
				Thread.sleep(500);
			} else {
				failMsg = failMsg + "\n3. cannot find error popup";
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 20. 공개 질문 좋아요 클릭 
	@Test(priority = 20, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qna_like() throws Exception {
		String failMsg = "";
		
		//참석자A 공개 질믄 
		addQuestion(attendADriver, QNA_QUESTION_PUBLIC, true);
		
		//참석자A 좋아요 클릭 (공개, 비공개 순 2개 중 공개 클릭)
		if(attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendADriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qna_A = attendADriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		qna_A.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_ICON)).click();
		Thread.sleep(500);
		
		String likecountA = qna_A.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		if(!likecountA.contentEquals("1")) {
			failMsg = "1. attendeeA's question like count [Expected]1" + " [Actual]" + likecountA;
		}
		
		//발표자 확인
		List<WebElement> qna_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		String likecountP = qna_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		if(!likecountP.contentEquals("1")) {
			failMsg = failMsg + "\n2. presenter's question like count [Expected]1" + " [Actual]" + likecountP;
		}
		qna_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_ICON)).click();
		Thread.sleep(500);
		likecountP = qna_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		if(!likecountP.contentEquals("2")) {
			failMsg = failMsg + "\n3. presenter's question like count [Expected]2" + " [Actual]" + likecountP;
		}
		
		//참석자B 좋아요 클릭
		if(attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).getAttribute("class").contentEquals("qna false")) {
			attendBDriver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TAB_QNA)).click();
			Thread.sleep(100);
		}
		List<WebElement> qna_B = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		qna_B.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_ICON)).click();
		Thread.sleep(500);
		
		String likecountB = qna_B.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		if(!likecountB.contentEquals("3")) {
			failMsg = failMsg + "\n4. attendeeA's question like count [Expected]3" + " [Actual]" + likecountB;
		}		
		
		//모두 해제
		qna_A.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKEED_ICON)).click();
		qna_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKEED_ICON)).click();
		qna_B.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKEED_ICON)).click();
		Thread.sleep(500);
		likecountA = qna_A.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		likecountP = qna_pre.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		likecountB = qna_B.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_COUNT)).getText();
		
		if(!(likecountA.contentEquals("0") && likecountP.contentEquals("0") && likecountB.contentEquals("0") )) {
			failMsg = failMsg + "\n5. like count [Expected]0" + " attendA : " + likecountA
					+ " presenter : " + likecountP + " attendB : " + likecountB;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 30. 비공개 질문에 답변, 공개 질문 좋아요 클릭 
	@Test(priority = 30, dependsOnMethods = { "SeminarRoom_Pres" }, enabled = true)
	public void qna_forStatics() throws Exception {

		List<WebElement> qna_pre = driver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		if(qna_pre.size() == 2) {
			addAnswer(qna_pre.get(1), QNA_PRIVATE_ANSWER);
		}
		
		List<WebElement> qna_B = attendBDriver.findElements(By.xpath(OnAirRoom.XPATH_ROOM_QNA_LIST));
		qna_B.get(0).findElement(By.xpath(XPATH_QNA_QUESTION_LIKE_ICON)).click();
		Thread.sleep(500);
		
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
			
			if(!wd.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(OnAirRoom.MSG_DELETE_ANSWER)) {
				failMsg = failMsg + "\n delete popup message [Expected]" + OnAirRoom.MSG_DELETE_ANSWER 
						+ " [Actual]" + wd.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
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
		
		if(!ee.findElement(By.xpath(XPATH_QNA_QUESTION_NICKNAME)).getText().contentEquals(nickname)) {
			failMsg = "\n qan writer [Expected]" + nickname 
					+ " [Actual]" + ee.findElement(By.xpath(XPATH_QNA_QUESTION_NICKNAME)).getText();
		}
	
		if(!ee.findElement(By.xpath(".//p[@class='content-box__content']")).getText().contentEquals(msg)) {
			failMsg = failMsg + "\n qan content [Expected]" + msg 
					+ " [Actual]" + ee.findElement(By.xpath(".//p[@class='content-box__content']")).getText();
		}
		
		if(ispublic) {
			try {
				ee.findElement(By.xpath(".//div[@class='content-box__utills']//i")).getAttribute("class");
				
				failMsg = failMsg + "\n find lock icon [Expected]" + "ricon-unlock" 
						+ " [Actual]" + ee.findElement(By.xpath(".//div[@class='content-box__utills']//i")).getAttribute("class");
			} catch(NoSuchElementException e) {
				//do not anything
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
		Thread.sleep(1000);
		
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
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}	
	
	// 101. 종료된 세미나 통계. QnA 확인
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
		
		// 응답 백분률 확인
		WebElement qnaPer = driver.findElement(By.xpath("//div[@class='statistic__attendee-item-box'][1]//div[@class='statistic__percent-box']"));
		if(!qnaPer.getText().contentEquals("50%")) {
			failMsg = failMsg + "\n2. statistics view, servery result. [Expected]50% [Actual]" + qnaPer.getText();
		}
		
		//qna tab
		driver.findElement(By.xpath("//div[@class='Statistic_statistic__tab-box__29-NQ']/button[3]")).click();
		Thread.sleep(500);		
		
		List<WebElement> qnalist = driver.findElements(By.xpath("//div[@class='Statistic_statistic__qna-box__2Qi3f']"));
		if(qnalist.size() != 2) {
			failMsg = "\n3. Statistics view. qna list size [Expected]2 [Actual]" + qnalist.size();
		} else {
			if(!qnalist.get(0).findElement(By.xpath(".//p[@class='qna__question']/span[1]")).getText().contentEquals(QNA_QUESTION_PUBLIC)) {
				failMsg = "\n4. Statistics view. qna question [Expected]" + QNA_QUESTION_PUBLIC 
						+ " [Actual]" + qnalist.get(0).findElement(By.xpath(".//p[@class='qna__question']/span[1]")).getText();
			}
			
			
			qnalist.get(1).click();
			Thread.sleep(1000);
			if(!qnalist.get(1).findElement(By.xpath(".//p[@class='qna__question']/span[1]")).getText().contentEquals(QNA_QUESTION_PRIVATE)) {
				failMsg = "\n5. Statistics view. qna question [Expected]" + QNA_QUESTION_PRIVATE 
						+ " [Actual]" + qnalist.get(1).findElement(By.xpath(".//p[@class='qna__question']/span[1]")).getText();
			}
			//응답 발표자 확인
			String answer_who = PresenterNickname + " <Presenter>";
			if(!qnalist.get(1).findElement(By.xpath(".//div[@class='qna__answer-content']/span[1]")).getText().contentEquals(answer_who)) {
				failMsg = "\n6. Statistics view. qna Answerer [Expected]" + answer_who 
						+ " [Actual]" + qnalist.get(1).findElement(By.xpath(".//div[@class='qna__answer-content']/span[1]")).getText();
			}
			//응답 답변
			if(!qnalist.get(1).findElement(By.xpath(".//div[@class='qna__answer-content']/textarea")).getText().contentEquals(QNA_PRIVATE_ANSWER)) {
				failMsg = "\n7. Statistics view. qna Answer [Expected]" + QNA_PRIVATE_ANSWER 
						+ " [Actual]" + qnalist.get(1).findElement(By.xpath(".//div[@class='qna__answer-content']/textarea")).getText();
			}
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
		} catch (NoAlertPresentException e) {
			System.out.println("error : " + e.getMessage());
			return "";
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
