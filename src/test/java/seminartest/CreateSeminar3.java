package seminartest;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* 0.로그인
 * 1.필수 설문 있는 세미나 생성
 * 2.발표자 세미나 룸 입장
 * 3.로그인 참석자 세미나 입장
 * 4.세미나 종료
 * 5.필수 설문 참여x 후 제출
 * 6.필수 설문 참여 후 제출
 * 7.세미나 생성
 * 8.설문 제목 입력
 * 9.설문 제목 max값 확인
 * 10.질문 추가 - 주관식
 * 11.질문 삭제
 * 12.질문 추가 - 객관식
 * 13.질문 입력
 * 14.질문 max값 확인
 * 15.질문 복사
 * 16.발표자 세미나 입장
 * 17.로그인 참석자 세미나 입장
 * 18.로그인x 참석자 세미나 입장
 * 19.세미나 종료
 * 20.제출 후 url 확인
 */

public class CreateSeminar3 {
	
	public static WebDriver driver;
	public static WebDriver LoginMember_driver;
	public static WebDriver NotLoginMember_driver;
	
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	
	public String seminarTitle = "";
	
	public String seminarID = "";
	public String createViewURL = "";
	public String seminarRoomURL = "";
	public String seminarLinkURL = "";
	public String seminarViewURL = "";
	public String closedurl = "";
	public String seminarUri = "";
	
	public String TestData = "abc123!@#$";
	public String AlertMsg = "Enter the answer to the required question.";
	public String AlertMsg2 = "Thank you for participating to the survey.";
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
		
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US", true);
		LoginMember_driver = comm.setDriver(LoginMember_driver, browsertype, "lang=en_US");
		NotLoginMember_driver = comm.setDriver(LoginMember_driver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", LoginMember_driver);
		context.setAttribute("webDriver3", NotLoginMember_driver);
		
        System.out.println("End BeforeTest!!!");

	}
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	}

	@Test(priority = 1)
	public void createSeminar() throws Exception {

		seminarTitle = "SurveyTest";
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		driver.get(createViewURL);
		Thread.sleep(500);
		if (!driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e = new Exception("Create Seminar view : " + driver.getCurrentUrl());
			throw e;
		}
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, true);
		comm.setCreateSeminar_setChannel(driver);
		comm.setCreateSeminar_setSurvey(driver);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();",
				driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)));
		// save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);
		comm.postSeminar(driver, seminarUri);

		if (seminarUri == null || seminarUri.isEmpty()) {
			Exception e = new Exception("fail to create seminar : " + driver.getCurrentUrl());
			throw e;
		}

		seminarID = driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW, "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;

	}

	@Test(priority = 2)
	public void EnterSeminar() throws Exception {
		String failMsg = "";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();",
				driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_publisher = new WebDriverWait(driver, 40);
			room_publisher.until(ExpectedConditions.textToBePresentInElement(
					driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 3)
	public void EnterSeminar_LoginMember() throws Exception {
		String failMsg = "";

		LoginMember_driver.get(CommonValues.SERVER_URL);

		CommonValues comm = new CommonValues();
		comm.loginseminar(LoginMember_driver, CommonValues.USEREMAIL_RSUP9);
		try {
			WebDriverWait login_present = new WebDriverWait(LoginMember_driver, 20);
			login_present.until(ExpectedConditions.textToBePresentInElement(
					LoginMember_driver.findElement(By.xpath("//div[@id='profile-drop-down']")),
					CommonValues.USEREMAIL_RSUP9.replace("@gmail.com", "")));

		} catch (Exception e) {
			failMsg = failMsg + "not login";
		}

		LoginMember_driver.get(seminarViewURL);
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		LoginMember_driver.findElement(By.xpath("//label[@for='serviceAgreement']/div")).click();
		LoginMember_driver.findElement(By.xpath("//label[@for='infoAgreement']/div")).click();
		Thread.sleep(1000);
		LoginMember_driver
				.findElement(
						By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']"))
				.click();
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		try {
			WebDriverWait room_loginmember = new WebDriverWait(LoginMember_driver, 40);
			room_loginmember.until(ExpectedConditions.textToBePresentInElement(
					LoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 4)
	public void EndSeminar() throws Exception {
		String failMsg = "";

		closedurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;

		WebElement StartSeminarSettingBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m']"));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", StartSeminarSettingBtn);

		StartSeminarSettingBtn.click();

		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@id='btn-exit']/i")));

		driver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		WebDriverWait exit_popup = new WebDriverWait(driver, 20);
		exit_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='buttons']/div/button[1]")).click();
		TimeUnit.SECONDS.sleep(20);

		if (!driver.getCurrentUrl().contentEquals(closedurl)
				&& !LoginMember_driver.getCurrentUrl().contentEquals(closedurl)) {

			failMsg = failMsg + "Not close url";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 5)
	public void CannotSubmitSurvey() throws Exception {
		String failMsg = "";

		WebElement submitbtn = LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", submitbtn);

		submitbtn.click();

		Alert alert = LoginMember_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "1.different alertMSG  [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void SubmitSurvey() throws Exception {
		String failMsg = "";
		
		List<WebElement> checkoption = LoginMember_driver.findElements(By.xpath("//div[@class='fake-checkbox']")); 
		
		checkoption.get(0).click();
		
		WebElement submitbtn = LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		submitbtn.click();
		
		Alert alert = LoginMember_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "1.different alertMSG  [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if (!LoginMember_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "\n 2.Wrong URL : " + LoginMember_driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	@Test(priority = 7)
	public void createSeminar2() throws Exception {

		seminarTitle = "SurveyTest2";
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
		driver.get(createViewURL);
		Thread.sleep(500);
		if (!driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e = new Exception("Create Seminar view : " + driver.getCurrentUrl());
			throw e;
		}
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setChannel(driver);
		comm.setCreateSeminar(driver, seminarTitle, true);
	}

	@Test(priority = 8)
	public void createSurvey() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB5)).click();
		Thread.sleep(500);

		WebElement SurveyTitle = driver.findElement(By.id("surveyTitle"));
		WebElement SurveyDescription = driver.findElement(By.id("surveyDescription"));

		insertData(SurveyTitle, 1, TestData);
		insertData(SurveyDescription, 1, TestData);

		// save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);

		// edit
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB5)).click();
		Thread.sleep(500);

		WebElement SurveyTitle2 = driver.findElement(By.id("surveyTitle"));
		WebElement SurveyDescription2 = driver.findElement(By.id("surveyDescription"));

		if (!SurveyTitle2.getAttribute("value").contentEquals(TestData)) {
			failMsg = "1.Survey Title is wrong [Expected]" + TestData + " [Actual]"
					+ SurveyTitle2.getAttribute("value");
		}

		if (!SurveyDescription2.getAttribute("value").contentEquals(TestData)) {
			failMsg = failMsg + "\n 2.Survey Description is wrong [Expected]" + TestData + " [Actual]"
					+ SurveyDescription2.getAttribute("value");
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 9)
	public void createSurveyMAX() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();

		WebElement SurveyTitle = driver.findElement(By.id("surveyTitle"));
		WebElement SurveyDescription = driver.findElement(By.id("surveyDescription"));

		insertData(SurveyTitle, 2, TestData);
		insertData(SurveyDescription, 4, TestData);

		String SurveyTitleMax = SurveyTitle.getAttribute("value");
		String SurveyDescriptionMax = SurveyDescription.getAttribute("value");

		if (SurveyTitleMax.length() != 40) {
			failMsg = "1.Survey Title is more than 40 char + [Expected]40 [Actual]" + SurveyTitleMax.length();
		}

		if (SurveyDescriptionMax.length() != 1000) {
			failMsg = failMsg + "\n 2.Survey Description is more than 1000 char + [Expected]1000 [Actual]"
					+ SurveyDescriptionMax.length();
		}

		// save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(4);

		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);

		// edit
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB5)).click();

		WebElement SurveyTitle2 = driver.findElement(By.id("surveyTitle"));
		WebElement SurveyDescription2 = driver.findElement(By.id("surveyDescription"));

		if (!SurveyTitle2.getAttribute("value").contentEquals(SurveyTitleMax)) {
			failMsg = failMsg + "\n 3.Survey Title is wrong [Expected]" + TestData + " [Actual]"
					+ SurveyTitle2.getAttribute("value");
		}

		if (!SurveyDescription2.getAttribute("value").contentEquals(SurveyDescriptionMax)) {
			failMsg = failMsg + "\n 4.Survey Description is wrong [Expected]" + TestData + " [Actual]"
					+ SurveyDescription2.getAttribute("value");
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 10)
	public void addQuestion_shortanswer() throws Exception {
		String failMsg = "";

		WebElement addquestion = driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']"));
		addquestion.click();

		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));

		if (surveylist.findElements(By.xpath("//div[@class='answer-radio-group']")).isEmpty()) {
			failMsg = "1.default is not multiple";
		}

		surveylist.findElement(By.id("question1")).click();
		Thread.sleep(500);

		surveylist.findElement(By.xpath("//div[@class='icon']")).click();
		List<WebElement> option = surveylist
				.findElements(By.xpath("//div[@class='btn btn-transparent btn-auto option-item  ']"));
		option.get(2).click();

		WebElement surveylist2 = driver.findElement(By.xpath("//div[@class='survey-list']"));

		if (surveylist2.findElements(By.xpath("//div[@class='answer-short']")).isEmpty()) {
			failMsg = failMsg + "\n 2.not change to open-ended";
		}

		surveylist2.findElement(By.id("question1")).click();
		Thread.sleep(500);
		surveylist2.findElement(By.xpath("//div[@class='icon']")).click();
		Thread.sleep(500);
		List<WebElement> option2 = surveylist2
				.findElements(By.xpath("//div[@class='btn btn-transparent btn-auto option-item  ']"));
		option2.get(0).click();

		if (surveylist2.findElements(By.xpath("//div[@class='answer-radio-group']")).isEmpty()) {
			failMsg = failMsg + "\n 3.not change to multiple";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 11)
	public void deleteQuestion() throws Exception {
		String failMsg = "";
		// question delete
		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> option = surveylist.findElements(By.xpath("//div[@class='group']"));
		option.get(1).click();

		if (!surveylist.findElements(By.xpath("//div[@class='answer-short']")).isEmpty()) {
			failMsg = "1. Question is not delete";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 12)
	public void addQuestion_multiple() throws Exception {
		String failMsg = "";

		WebElement addquestion = driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']"));
		addquestion.click();

		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox = surveylist.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));

		radiobox.get(1).findElement(By.xpath("//div[@class='temp-add']")).click();

		WebElement surveylist2 = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox2 = surveylist2.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));

		if (radiobox2.size() != 3) {
			failMsg = "1.Do not option added + [Expected]3 [Actual]" + radiobox2.size();
		}

		radiobox2.get(1).findElement(By.xpath("//i[@class='ricon-close']")).click();

		WebElement surveylist3 = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox3 = surveylist3.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));

		if (radiobox3.size() != 2) {
			failMsg = "1.Do not option deleted + [Expected]2 [Actual]" + radiobox3.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 13)
	public void addQuestion() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();

		WebElement question = driver.findElement(By.id("question1"));
		insertData(question, 1, TestData);

		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox = surveylist.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));
		insertData(radiobox.get(0).findElement(By.xpath("//span/input")), 1, TestData);

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(4);

		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);

		// edit
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB5)).click();

		WebElement question2 = driver.findElement(By.id("question1"));
		WebElement surveylist2 = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox2 = surveylist2.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));

		if (!question2.getText().contentEquals(TestData)) {
			failMsg = "1.Question Title is wrong [Expected]" + TestData + " [Actual]" + question2.getText();
		}

		if (!radiobox2.get(0).findElement(By.xpath("//span/input")).getAttribute("value").contentEquals(TestData)) {
			failMsg = failMsg + "\n 2.Question Option is wrong [Expected]" + TestData + " [Actual]"
					+ radiobox2.get(0).findElement(By.xpath("//span/input")).getAttribute("value");
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 14)
	public void addQuestionMAX() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();

		WebElement question = driver.findElement(By.id("question1"));
		insertData(question, 2, TestData);

		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox = surveylist.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));
		insertData(radiobox.get(0).findElement(By.xpath("//span/input")), 2, TestData);

		String QuestionMax = question.getText();
		String OptionMax = radiobox.get(0).findElement(By.xpath("//span/input")).getAttribute("value");

		if (QuestionMax.length() != 40) {
			failMsg = "1.Question Title is more than 40 [Expected]40 [Actual]" + QuestionMax.length();
		}

		if (OptionMax.length() != 40) {
			failMsg = failMsg + "\n 2.Question Option is more than 40 [Expected]40 [Actual]" + OptionMax.length();
		}

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(4);

		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);

		// edit
		driver.findElement(By.xpath("//div[@class='ricon ricon-edit']")).click();

		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_TAB5)).click();

		WebElement question2 = driver.findElement(By.id("question1"));
		WebElement surveylist2 = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> radiobox2 = surveylist2.findElements(By.xpath("//div[@class='Radio_radioBox__2VtPF radio']"));

		if (!question2.getText().contentEquals(QuestionMax)) {
			failMsg = failMsg + "\n 3.Question Title is wrong [Expected]" + QuestionMax + " [Actual]"
					+ question2.getText();
		}

		if (!radiobox2.get(0).findElement(By.xpath("//span/input")).getAttribute("value").contentEquals(OptionMax)) {
			failMsg = failMsg + "\n 4.Question Option is wrong [Expected]" + OptionMax + " [Actual]"
					+ radiobox2.get(0).findElement(By.xpath("//span/input")).getAttribute("value");
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 15)
	public void addQuestionCopy() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();

		WebElement question = driver.findElement(By.id("question1"));
		question.click();

		WebElement surveylist = driver.findElement(By.xpath("//div[@class='survey-list']"));
		List<WebElement> option = surveylist.findElements(By.xpath("//div[@class='group']"));
		option.get(2).click();

		List<WebElement> surveylist2 = driver.findElements(By.xpath("//div[@class='box-question']"));
		String question1 = surveylist2.get(0).findElement(By.xpath("//textarea")).getText();
		String question1_option = surveylist2.get(0).findElement(By.xpath("//span/input")).getAttribute("value");
		String question2 = surveylist2.get(1).findElement(By.xpath("//textarea")).getText();
		String question2_option = surveylist2.get(1).findElement(By.xpath("//span/input")).getAttribute("value");

		if (!question1.contentEquals(question2)) {
			failMsg = "1.do not coppied [Expected]" + question1 + " [Actual]" + question2;

		}

		if (!question1.contentEquals(question2)) {
			failMsg = failMsg + "\n 2.do not coppied [Expected]" + question1_option + " [Actual]" + question2_option;
		}
		
		// save
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(1000);

		String seminarUri = "";
		seminarUri = comm.findSeminarIDInList(driver, seminarTitle);
		comm.postSeminar(driver, seminarUri);

		if (seminarUri == null || seminarUri.isEmpty()) {
			Exception e = new Exception("fail to create seminar : " + driver.getCurrentUrl());
			throw e;
		}

		seminarID = driver.getCurrentUrl().replace(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW, "");
		seminarViewURL = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		seminarRoomURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		seminarLinkURL = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 16)
	public void EnterSeminar2() throws Exception {
		String failMsg = "";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();",
				driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(1));

		try {
			WebDriverWait room_publisher = new WebDriverWait(driver, 40);
			room_publisher.until(ExpectedConditions.textToBePresentInElement(
					driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 17)
	public void EnterSeminar_LoginMember2() throws Exception {
		String failMsg = "";

		LoginMember_driver.get(seminarViewURL);
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		LoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		LoginMember_driver.findElement(By.xpath("//label[@for='serviceAgreement']/div")).click();
		LoginMember_driver.findElement(By.xpath("//label[@for='infoAgreement']/div")).click();
		Thread.sleep(1000);
		LoginMember_driver
				.findElement(
						By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']"))
				.click();
		LoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		try {
			WebDriverWait room_loginmember = new WebDriverWait(LoginMember_driver, 40);
			room_loginmember.until(ExpectedConditions.textToBePresentInElement(
					LoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 18)
	public void EnterSeminar_NotLoginMember() throws Exception {
		String failMsg = "";

		NotLoginMember_driver.get(CommonValues.SERVER_URL);

		NotLoginMember_driver.get(seminarLinkURL);
		NotLoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		JavascriptExecutor js = (JavascriptExecutor) NotLoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();",
				NotLoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		NotLoginMember_driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();

		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='nickname']")).sendKeys(ATTENDEES_NICKNAME);

		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).click();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).clear();
		NotLoginMember_driver.findElement(By.xpath("//input[@name='email']")).sendKeys(ATTENDEES_EMAIL);

		NotLoginMember_driver.findElement(By.xpath("//label[@for='serviceAgreement']/div")).click();
		NotLoginMember_driver.findElement(By.xpath("//label[@for='infoAgreement']/div")).click();
		Thread.sleep(1000);
		NotLoginMember_driver
				.findElement(
						By.xpath("//button[@class='btn btn-primary btn-xl public-apply__attendeeInfo__enter-btn']"))
				.click();
		NotLoginMember_driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(3000);

		try {
			WebDriverWait room_notloginmember = new WebDriverWait(NotLoginMember_driver, 40);
			room_notloginmember.until(ExpectedConditions.textToBePresentInElement(
					NotLoginMember_driver.findElement(By.xpath("//section[@id='gnb-left']/h1")), seminarTitle));
		} catch (Exception e) {
			failMsg = failMsg + "Drivers can't access Room_URL";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 19)
	  public void EndSeminar2() throws Exception{
		String failMsg = "";

		closedurl = CommonValues.SERVER_URL + CommonValues.SEMINAR_CLOSED_URI + seminarID;
		
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);

		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView();",
				driver.findElement(By.xpath("//button[@id='btn-exit']/i")));

		driver.findElement(By.xpath("//button[@id='btn-exit']")).click();
		WebDriverWait exit_popup = new WebDriverWait(driver, 20);
		exit_popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='buttons']/div/button[1]")).click();
		TimeUnit.SECONDS.sleep(20);

		if (!driver.getCurrentUrl().contentEquals(closedurl)
				&& !LoginMember_driver.getCurrentUrl().contentEquals(closedurl)
				&& !NotLoginMember_driver.getCurrentUrl().contentEquals(closedurl)) {

			failMsg = failMsg + "Not close url";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20)
	  public void SubmitSurvey2() throws Exception{
		String failMsg = "";
		
		WebElement submitbtn = LoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		JavascriptExecutor js = (JavascriptExecutor) LoginMember_driver;
		js.executeScript("arguments[0].scrollIntoView();", submitbtn);

		submitbtn.click();

		Alert alert = LoginMember_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "1.different alertMSG  [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}

		if (!LoginMember_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			failMsg = failMsg + "\n 2.Wrong URL : " + LoginMember_driver.getCurrentUrl();
		}
		
		WebElement submitbtn2 = NotLoginMember_driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		JavascriptExecutor js2 = (JavascriptExecutor) NotLoginMember_driver;
		js2.executeScript("arguments[0].scrollIntoView();", submitbtn2);
		
		submitbtn2.click();
		
		Alert alert2 = NotLoginMember_driver.switchTo().alert();
		String alert_msg2 = alert2.getText();
		alert2.accept();
		Thread.sleep(1000);
		
		if (!alert_msg2.contentEquals(AlertMsg2)) {
			failMsg = failMsg + "\n 3.different alertMSG  [Expected]" + AlertMsg2 + " [Actual]" + alert_msg2;
		}

		if (!NotLoginMember_driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/")) {
			failMsg = failMsg + "\n 4.Wrong URL : " + NotLoginMember_driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {
	  
		driver.quit();
		LoginMember_driver.quit();
		NotLoginMember_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	private void insertData(WebElement e, int N, String data) throws Exception {
		switch(N) {
		case 1:
			e.click();
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			e.sendKeys(data);
		break;
		case 2:
			e.click();
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			for (int i=0; i<5; i++){
			e.sendKeys(data);}
		break;
		case 3:
			e.click();
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			for (int i=0; i<10; i++){
			e.sendKeys(data);}
		break;
		case 4:
			e.click();
			while(!e.getAttribute("value").isEmpty() || !e.getText().isEmpty())
				e.sendKeys(Keys.BACK_SPACE);
			for (int i=0; i<101; i++){
			e.sendKeys(data);}
		break;
		}
	}

}

