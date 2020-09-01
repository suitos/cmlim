package seminartest;

import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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


/* 0.로그인
 * 1.세미나룸생성
 * 2.임시저장,미리보기 버튼 활성화 조건 확인(RST-383)
 * 3.미리보기 상세정보란 제목 및 발표자 정보 확인 (RST-615)
 * 4.대기화면 이미지모드 케이스 미리보기 페이지 대기제목 및 발표자 정보 확인 (RST-608)
 * 5.대기화면 동영상모드 케이스 미리보기 페이지 대기제목 및 발표자 정보 확인(RST-613) > TO-DO 개발 미완
 * 6.설문없는 경우 미리보기 페이지 UI 확인 (RST-614)
 * 7.설문있는 경우 미리보기 페이지 UI, 새로고침 버튼 동작 확인(RST-385, RST-388)
 * 8.게시완료할 경우 미리보기 버튼 유무 확인(RST-386)
 */

public class PreviewSeminar {
	
	public static WebDriver driver;
	public static String MSG_NO_YOUTUBE_URL = "Please, enter a valid YouTube link.";
	
	public String seminarTitle = "Preview test";
	public String seminarID = "";
	public String createViewURL = "";
	public String BannerseminarTitle = "";
	public String BannerseminarDate = "";
	public String PresentList = "";
	public String seminarInfo = "";
	
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	 
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
			
		context.setAttribute("webDriver", driver);
		
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");

	}
	//로그인
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
		
		Thread.sleep(1000);
	}
	
	@Test(priority=1)
	  public void createRoom() throws Exception {
		
		createViewURL = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000); 
		if(!driver.getCurrentUrl().contentEquals(createViewURL)) {
			Exception e =  new Exception("Create Seminar view : " +  driver.getCurrentUrl());
			throw e;
		}
	}
	//임시저장,미리보기 버튼 활성화 조건 확인(RST-383)
	@Test(priority=2)
	  public void checkActivePreviewBtn() throws Exception {

		WebElement PreviewBtn_BeforeTitle = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview disabled']"));
				
		//미리보기 비활성화 확인
		Boolean checkPreviewBtn_BeforeDraft = PreviewBtn_BeforeTitle.isEnabled();
		if(checkPreviewBtn_BeforeDraft == true) {
			Exception e = new Exception("Preview_btn is enabled!! Before click Draft");
			throw e;
		}
		
		//툴팁 확인
		Actions action = new Actions(driver);
		WebElement PreviewBtn_BeforeDraft2 = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview disabled']"));
		Thread.sleep(1000);
		action.moveToElement(PreviewBtn_BeforeDraft2).build().perform();
		Thread.sleep(3000);
				
		Boolean isToolTipDisplayed = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]")).isDisplayed();
		System.out.println("Is Tooltip displayed ? : " + isToolTipDisplayed);
		if (isToolTipDisplayed == true) {
			String tooltipText = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]")).getText();
			System.out.println("Tooltip Text:- " + tooltipText);
			if(!tooltipText.contentEquals(CommonValues.Preview_Tooltip)) {
				Exception e = new Exception("Preview_tooltip text is wrong :" + tooltipText);
				throw e;
			}
		}
		else {
			Exception e = new Exception("Preview_tooltip is not displayed");
			throw e;		
			}
		
		//세미나 제목 입력
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		BannerseminarTitle = driver.findElement(By.xpath("//div[@class='wrap-info']/div[1]")).getText();
		BannerseminarDate = driver.findElement(By.xpath("//div[@class='wrap-info']/div[2]")).getText();
			
		WebElement PreviewBtn_AfterDraft = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview ']"));
		Boolean checkPreviewBtn_AfterDraft = PreviewBtn_AfterDraft.isEnabled();
		if(checkPreviewBtn_AfterDraft == false) {
			Exception e = new Exception("Preview_btn is not enabled!! After click Draft");
			throw e;
		}
	
		String parentHandle = driver.getWindowHandle();
		
		PreviewBtn_AfterDraft.click();
		
		for (String winHandle : driver.getWindowHandles()) {
		    driver.switchTo().window(winHandle); }
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.PREVIEW_DETAILED_URI))
		{
			  Exception e =  new Exception("no Preview Detailed View : " + driver.getCurrentUrl());
			  throw e;
		}
		String Preview_Title = driver.findElement(By.xpath("//div[@class='l-left']")).getText();
		if(!(Preview_Title.contentEquals(CommonValues.PREVIEW_DEFAULT)))
		{
			Exception e =  new Exception("Different Preview Default Title :" + Preview_Title);
			  throw e;
		}
		WebElement RefreshBtn = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto l-right ']"));
		if(!RefreshBtn.isDisplayed() && !RefreshBtn.isEnabled()) {
			Exception e =  new Exception("RefreshBtn error!!");
			  throw e;
		}
		
		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(parentHandle);
	}

	@Test(priority=3) //RST-615
	  public void checkDetailedInfo() throws Exception{
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar_setMember(driver);
		
		List<WebElement> allPresent = driver.findElements(By.xpath("//span[@class='member-item__user-info__nickname']"));
		String Present = allPresent.get(0).getText();
		String Present1 = allPresent.get(1).getText();
		String Present2 = allPresent.get(2).getText();
		
		PresentList = Present + "," + Present1 + "," + Present2;
		seminarInfo = BannerseminarDate + "\n" + PresentList;
		
		WebElement DraftBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']"));
		DraftBtn.click();
		Thread.sleep(1000);
		WebElement PreviewBtn_AfterDraft = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview ']"));
		
		String parentHandle = driver.getWindowHandle();
		
		PreviewBtn_AfterDraft.click();
		
		for (String winHandle : driver.getWindowHandles()) {
		    driver.switchTo().window(winHandle); }
		
		String Preview_seminarTitle = driver.findElement(By.xpath("//p[@class='text title']")).getText();
		
		if(!(Preview_seminarTitle.contentEquals(seminarTitle)) && !(Preview_seminarTitle.contentEquals(BannerseminarTitle)))
		{
			failMsg = failMsg + "\n 1.Different Seminar Title in Detailedinfo [Expected]" + seminarTitle + "and" + BannerseminarTitle 
					+" [Actual]" + Preview_seminarTitle;
		}
		String Preview_seminarInfo = driver.findElement(By.xpath("//p[@class='info-date-author date']")).getText();
		
		if(!(Preview_seminarInfo.contentEquals(seminarInfo)))
		{
			failMsg = failMsg + "\n 2.Different Seminar Date and Present in Detailedinfo [Expected]" + seminarInfo
					+" [Actual]" + Preview_seminarInfo;
		}

		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(parentHandle);
	}
	@Test(priority=4) //RST-608
	  public void checkStanbyScreen_ImageMode() throws Exception{
		String failMsg = "";
		String stanbyText_imagemode = "Check_Standbyview_Imagemode";
		
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).sendKeys(Keys.BACK_SPACE);
		
		while(!driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).getText().isEmpty())
				driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).sendKeys(Keys.BACK_SPACE);
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='ql-container ql-snow']/div")).sendKeys(stanbyText_imagemode);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Thread.sleep(1000);
			
		String parentHandle = driver.getWindowHandle();
		driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview ']")).click();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); }
			
		WebElement Preview_StanbyBtn = driver.findElement(By.xpath("//ul[@class='tab ']/li[2]"));
		Preview_StanbyBtn.click();
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.PREVIEW_STANBY_URI))
		{
			failMsg = failMsg + "\n 1.Wrong Preview StanbyURL : " + driver.getCurrentUrl();
		}
		String Preview_StanbyTitle = driver.findElement(By.xpath("//div[@class='title ql-editor']/p")).getText();
		if(!Preview_StanbyTitle.contentEquals(stanbyText_imagemode)) {
			failMsg = failMsg + "\n 2. Preview StanbyTitle_VALIDVALUE  [Expected]" + stanbyText_imagemode
					+" [Actual]" + Preview_StanbyTitle;
		}
		String Preview_StanbyInfo = driver.findElement(By.xpath("//p[@class='info-date-author date']")).getText();
		if(!(Preview_StanbyInfo.contentEquals(seminarInfo)))
		{
			failMsg = failMsg + "\n 3. Different Seminar Date and Present in StanbyScreen [Expected]" + seminarInfo
					+" [Actual]" + Preview_StanbyInfo;
		}
		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(parentHandle);	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=5) //RST-613(개발 미완성)
	  public void checkStanbyScreen_VideoMode() throws Exception{
		String failMsg = "";
		String stanbyText_videomode = "Check_Standbyview_Videomode";
		
		driver.findElement(By.id("cover-mode")).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		
		WebElement DraftBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']"));
		WebElement PreviewBtn_AfterDraft = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto preview ']"));
		PreviewBtn_AfterDraft.click();
		
		WebDriverWait popup = new WebDriverWait(driver, 20);
		popup.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='modal-footer']/button")));
		
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(MSG_NO_YOUTUBE_URL)) {
			failMsg = failMsg + "\n 1. empty youtube link msg [Expected]" + MSG_NO_YOUTUBE_URL 
						+" [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		}
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='linkBox']/input")).sendKeys(CommonValues.YOUTUBE_URL[1]);
		/*
		WebDriverWait youtubetitle = new WebDriverWait(driver, 20);
		youtubetitle.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='ytp-title-text']/a")), CommonValues.YOUTUBE_TITLE[1]));
		System.out.println(driver.findElement(By.xpath("///*[@id=\"player_uid_340996391_1\"]/div[3]/div[2]/div/a")).getText());
		if(!driver.findElement(By.xpath("//div[@class='ytp-title-text']/a")).getText().contentEquals(CommonValues.YOUTUBE_TITLE[1])) {
			failMsg = failMsg + "\n 2. youtube link can't insert [Expected]" + CommonValues.YOUTUBE_TITLE[1] 
					+" [Actual]" + driver.findElement(By.xpath("//div[@class='ytp-title-text']/a")).getText();
		}
		*/
		WebElement editor = driver.findElement(By.xpath("//div[@class='ql-editor']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", editor);
		
		editor.click();
		Thread.sleep(1000);
		editor.sendKeys(Keys.BACK_SPACE);
		
		while(!editor.getText().isEmpty())
			   editor.sendKeys(Keys.BACK_SPACE);
		Thread.sleep(1000);
		editor.sendKeys(stanbyText_videomode);
		
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView();", DraftBtn);
		
		DraftBtn.click();
		Thread.sleep(1000);
		
		String parentHandle = driver.getWindowHandle();
		PreviewBtn_AfterDraft.click();
		
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); }
		
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		
		/*to do 
		 * 동영상모드 대기모드 미리보기 검증 (개발 미완성)
		 */
		
		driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=6) //RST-614
	  public void checkEmptySurvey() throws Exception{
		String failMsg = "";
		String parentHandle = driver.getWindowHandle();
		
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); }
		
		driver.findElement(By.xpath("//ul[@class='tab ']/li[3]")).click();
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.PREVIEW_SURVEY_URI))
		{
			failMsg = failMsg + "\n 1. Preview SurveyURL_validvalue [Expected]" + CommonValues.SERVER_URL + CommonValues.PREVIEW_SURVEY_URI 
					+" [Actual]" + driver.getCurrentUrl();
			  
		}
		String Preview_emptySurvey = driver.findElement(By.xpath("//div[@class='wrap-contents']/h1")).getText();
		if(!(Preview_emptySurvey.contentEquals(CommonValues.SURVEY_DEFAULT)))
		{
			failMsg = failMsg + "\n 2. Different Default Survey msg [Expected]" + CommonValues.SURVEY_DEFAULT 
					+" [Actual]" + driver.findElement(By.xpath("//div[@class='wrap-contents']/h1")).getText();
		}
		
		driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	@Test(priority=7) //RST-385, RST-388
	  public void checkValueableSurvey() throws Exception{
		String failMsg = "";
		String parentHandle = driver.getWindowHandle();
		
		WebElement DraftBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']"));
		
		driver.findElement(By.xpath("//ul[@class='tab ']/li[5]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("surveyTitle")).clear();
		driver.findElement(By.id("surveyTitle")).sendKeys(CommonValues.SURVEY_TITLE);
		driver.findElement(By.id("surveyDescription")).sendKeys(CommonValues.SURVEY_DESCRIPTION);
		
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		driver.findElement(By.id("question1")).sendKeys(CommonValues.SURVEY_QUESTION[0]);
		
		DraftBtn.click();
		Thread.sleep(3000);
		
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); }
		
		WebElement RefreshBtn = driver.findElement(By.xpath("//div[@class='btn btn-transparent btn-auto l-right ']"));
		RefreshBtn.click();
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[3]")).click();
		
		String Preview_surveyTitle = driver.findElement(By.id("surveyTitle")).getText();
		String Preview_surveyDescription = driver.findElement(By.name("surveyDescription")).getText();
		String Preview_surveyQuestion = driver.findElement(By.id("question1")).getText();
		
		if(!Preview_surveyTitle.contentEquals(CommonValues.SURVEY_TITLE) && (!Preview_surveyDescription.contentEquals(CommonValues.SURVEY_DESCRIPTION)
			&&(!Preview_surveyQuestion.contentEquals(CommonValues.SURVEY_QUESTION[0]))))
		{
			failMsg = failMsg + "\n 1.wrong Preview Survey value [Expected]" + CommonValues.SURVEY_TITLE + CommonValues.SURVEY_DESCRIPTION + CommonValues.SURVEY_QUESTION[0]
					+" [Actual]" + Preview_surveyTitle + Preview_surveyDescription + Preview_surveyQuestion;  
		}
		
		String Preview_surveyseminarTitle = driver.findElement(By.xpath("//p[@class='title']")).getText();
		
		if(!(Preview_surveyseminarTitle.contentEquals(seminarTitle)) && !(Preview_surveyseminarTitle.contentEquals(BannerseminarTitle)))
		{
			failMsg = failMsg + "\n 2.Different Preview Seminar Title in Survey [Expected]" + seminarTitle + "and" + BannerseminarTitle
					+" [Actual]" + Preview_surveyseminarTitle;
		}
		
		String Preview_surveyseminarInfo = driver.findElement(By.xpath("//div[@class='author']")).getText() + "\n" 
		+ driver.findElement(By.xpath("//div[@class='date']")).getText().replace(" ",",");
		System.out.println(Preview_surveyseminarInfo);
		if(!(Preview_surveyseminarInfo.contentEquals(seminarInfo)))
		{
			failMsg = failMsg + "\n 3.Different Seminar Date and Present in Survey [Expected]" + seminarInfo
					+" [Actual]" + Preview_surveyseminarInfo;
		}
		
		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(parentHandle);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority=8)
	public void checkPreviewBtnAfterPost() throws Exception{
		String failMsg = "";
		driver.findElement(By.xpath("//button[@class='btn btn-secondary-light btn-l ']")).click();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-primary btn-m ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-m ']")).click();
		WebDriverWait wait2 = new WebDriverWait(driver, 20);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-secondary-light btn-xl']")));
		
		String PostURL = CommonValues.SERVER_URL + CommonValues.POST_URI;
		seminarID = driver.getCurrentUrl().replace(PostURL, "");
		
		driver.findElement(By.xpath("//a[@class='btn btn-secondary-light btn-xl']")).click();
		driver.findElement(By.xpath("//ul[@class='user-seminar-list']/li[1]/div[2]/button[2]/i")).click();

		String editURL = driver.getCurrentUrl();
		
		if(!editURL.contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID))
		{
			failMsg = failMsg + "\n 1.Can't Enter EDIT page [Expected]" + editURL
					+" [Actual]" + driver.getCurrentUrl();
		}
		if(!driver.findElements(By.xpath("//div[@class='btn btn-transparent btn-auto preview disabled']")).isEmpty() 
			&& !driver.findElements(By.xpath("//div[@class='btn btn-transparent btn-auto preview ']")).isEmpty())
		{
			failMsg = failMsg + "\n 2.Preview Btn is exist";  
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
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
