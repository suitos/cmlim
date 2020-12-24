package seminartest;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* logintest
 * 
 * 1. 로그인 필드 확인(placeholder)
 * 2. 모두 빈값으로 로그인 시도
 * 3. 이메일 필드 비우고 로그인 시도
 * 4. 비밀번호 필드 비우고 로그인 시도
 * 5. 잘못된 ID, 정상 비밀번호 
 * 6. 정상 ID, 잘못된 비밀번호
 * 7. 정상 로그인 로그아웃
 * 8. 비밀번호 찾기 URL 및 UI
 * 9. 비밀번호 찾기 이메일 빈 값
 * 10.비밀번호 찾기 등록되지 않은 ID
 * 11.비밀번호 찾기 잘못된 ID 값
 * 12.비밀번호 변경 링크 진입
 * 13.변경 비밀번호 빈 값
 * 14.변경 비밀번호 상이한 값
 * 15.변경 비밀번호 보안 규정에 맞지 않은 값
 * 16.비밀번호 변경
 * 17.비밀번호 변경한 계정 원복
 * 
 * 20. 로그인 토큰확인 : 탭 연결 확인
 * 21. 로그인 토큰확인 : 중복로그인
 */

public class logintest {
	
	public static String XPATH_ID = "//input[@name='email']";
	public static String XPATH_PW = "//input[@name='password']";
	public static String XPATH_LOGIN_ERROR = "//p[@class='login__form__error-msg']";
	
	public static String XPATH_FIND_PW = "//div[@class='login__etc']/a[1]";
	
	public static String EMPTYEMAIL = "Enter the email ID.";
	public static String EMPTYPW = "Enter the password.";
	public static String WRONGID = "You have entered and invalid username or password.";
	public static String WRONGPW = "You have entered and invalid username or password.";
	public static String MSG_LOGIN_DUPLICATED = "This connection will be terminated as you have logged in elsewhere. [41612]";
	
	public static String FINDPASSWORD_DESC = "Please, enter the information to reset the password."+ "\n" +"Please enter the registered email address.";
	public static String FINDPASSWORD_ALERT_MSG = "Email information is incorrect. Please, check the email settings and try again.";
	public static String FINDPASSWORD_ERROR_MSG = "Email format is incorrect.";
	public static String FINDPASSWORD_SUBMIT_MSG = "A password reset email will be sent to your registered email.";
	public static String CHANGEPASSWORD_ERROR_MSG = "Password must include alphabets, numbers and special characters with a minimum of 8 characters.\n" + "Also, password cannot be the same as the email ID.";
	public static String CHANGEPASSWORD_ERROR_MSG2 = "Entered passwords do not match. Please, check and try again.";
	public static String CHANGEPASSWORD_COMPLETE_MSG = "Password has been changed.";
	public static String WRONGEMAIL = "1111@111.111";
	
	public static String changepasswordkey = "";
	
	
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
	}
	
	// 1. 로그인 필드 확인(placeholder)
	@Test(priority=1, enabled = true)
	  public void checkloginfield() throws Exception {
		String failMsg = "";
		
	    driver.get(CommonValues.SERVER_URL);
	    Thread.sleep(1000);
	    
	    if (!driver.findElement(By.xpath(XPATH_ID)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_LOGIN_EMAIL))
	    {
	    	failMsg = "1. wrong placeholder(Login Email field) : " + driver.findElement(By.xpath(XPATH_ID)).getAttribute("placehlder");
	    }
	    
	    if (!driver.findElement(By.xpath(XPATH_PW)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_LOGIN_PW))
	    {
	    	failMsg = failMsg + "\n 2. wrong placeholder(Login PW field) : " + driver.findElement(By.xpath(XPATH_PW)).getAttribute("placehlder");
	    }
	    
	    //회원가입링크
	    if (!driver.findElement(By.xpath("//div[@class='login__etc']/p[1]/a[1]")).getText().contentEquals(CommonValues.SIGNUP_TXT)
	    		&& !driver.findElement(By.xpath("//div[@class='login__etc']/p[1]/a[1]")).getAttribute("href").contentEquals(CommonValues.SIGNUP_LINK) )
	    {
	    	failMsg = failMsg + "\n 3. wrong sign up link : " + driver.findElement(By.xpath("//div[@class='login__etc']/p[1]/a[1]")).getText();

	    }
	    
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	
	// 2. 모두 빈값으로 로그인 시도
	@Test(priority=2, enabled = true)
	  public void loginEmpty() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    driver.findElement(By.id("lang")).click();

	    driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    
	    
	    Thread.sleep(1000); 
	    String worningmsg = driver.findElement(By.xpath(XPATH_LOGIN_ERROR)).getText();

	    if(worningmsg.equalsIgnoreCase(EMPTYEMAIL))
	    {
	    	System.out.println("same error msg");
	    }
	    else
	    {
	    	Exception e =  new Exception("error msg [Expected]" + EMPTYEMAIL + " [Actual]" + worningmsg);
	    	throw e;
	    }
	  }

	// 3. 이메일 필드 비우고 로그인 시도
	  @Test(priority=3, enabled = true)
	  public void loginfailEmptyID() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    
	    driver.findElement(By.xpath(XPATH_ID)).clear();
	    //driver.findElement(By.xpath(XPATH_ID)).sendKeys("abc@rsupport.com");
	    driver.findElement(By.xpath(XPATH_PW)).clear();
	    driver.findElement(By.xpath(XPATH_PW)).sendKeys("1111qqqq");
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    Thread.sleep(1000); 
	    
	    String worningmsg = driver.findElement(By.xpath(XPATH_LOGIN_ERROR)).getText();

	    if(worningmsg.equalsIgnoreCase(EMPTYEMAIL))
	    {
	    	System.out.println("same error msg");
	    }
	    else
	    {
	    	Exception e =  new Exception("error msg [Expected]" + EMPTYEMAIL + " [Actual]" + worningmsg);
	    	throw e;
	    }
	  }
	
	  //4. 비밀번호 필드 비우고 로그인 시도
	  @Test(priority=4, enabled = true)
	  public void loginfailEmptyPW() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    
	    driver.findElement(By.xpath(XPATH_ID)).clear();
	    driver.findElement(By.xpath(XPATH_ID)).sendKeys("abc@rsupport.com");
	    driver.findElement(By.xpath(XPATH_PW)).clear();
	    //driver.findElement(By.xpath(XPATH_PW)).sendKeys("1111qqqq");
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    Thread.sleep(1000); 
	    
	    String worningmsg = driver.findElement(By.xpath(XPATH_LOGIN_ERROR)).getText();

	    if(worningmsg.equalsIgnoreCase(EMPTYPW))
	    {
	    	System.out.println("same error msg");
	    }
	    else
	    {
	    	Exception e =  new Exception("error msg [Expected]" + EMPTYEMAIL + " [Actual]" + worningmsg);
	    	throw e;
	    }
	  }
	  
	  // 5. 잘못된 ID, 정상 비밀번호 
	  @Test(priority=5, enabled = true)
	  public void loginfailWrongID() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    
	    driver.findElement(By.xpath(XPATH_ID)).clear();
	    driver.findElement(By.xpath(XPATH_ID)).sendKeys("abctest");
	    driver.findElement(By.xpath(XPATH_PW)).clear();
	    driver.findElement(By.xpath(XPATH_PW)).sendKeys("1111qqqq");
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    Thread.sleep(1000); 
	    
	    String worningmsg = driver.findElement(By.xpath(XPATH_LOGIN_ERROR)).getText();

	    if(worningmsg.equalsIgnoreCase(WRONGID))
	    {
	    	System.out.println("same error msg");
	    }
	    else
	    {
	    	Exception e =  new Exception("error msg [Expected]" + WRONGID + " [Actual]" + worningmsg);
	    	throw e;
	    }
	  }
	  
	  // 6. 정상 ID, 잘못된 비밀번호
	  @Test(priority=6, enabled = true)
	  public void loginfailWrongPW() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    driver.findElement(By.id("lang")).click();
	    
	    driver.findElement(By.xpath(XPATH_ID)).clear();
	    driver.findElement(By.xpath(XPATH_ID)).sendKeys("sinhyekim@rsupport.com");
	    driver.findElement(By.xpath(XPATH_PW)).clear();
	    driver.findElement(By.xpath(XPATH_PW)).sendKeys("1111qqqq");
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    Thread.sleep(1000); 
	    
	    String worningmsg = driver.findElement(By.xpath(XPATH_LOGIN_ERROR)).getText();

	    if(worningmsg.equalsIgnoreCase(WRONGID))
	    {
	    	System.out.println("same error msg");
	    }
	    else
	    {
	    	Exception e =  new Exception("error msg [Expected]" + WRONGID + " [Actual]" + worningmsg);
	    	throw e;
	    }
	  }
	
	  // 7. 정상 로그인 로그아웃
	  @Test(priority=7, enabled = true)
	  public void loginlogout() throws Exception {
		driver.get(CommonValues.SERVER_URL);

		//login
		driver.findElement(By.xpath(XPATH_ID)).clear();
		driver.findElement(By.xpath(XPATH_ID)).sendKeys(CommonValues.USEREMAIL);
		driver.findElement(By.xpath(XPATH_PW)).clear();
		driver.findElement(By.xpath(XPATH_PW)).sendKeys(CommonValues.USERPW);

		driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
		
		Thread.sleep(1000);
		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI) 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INTRO_URI)) {
			Exception e = new Exception("login fail" + driver.getCurrentUrl());
			throw e;
		}
		
		//logout
		driver.findElement(By.id("profile-drop-down")).click();
		driver.findElement(By.linkText("Log out")).click();
		
		Thread.sleep(500);
		if (!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL) 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/") 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/login")) {
			Exception e = new Exception("logout fail :" + driver.getCurrentUrl());
			throw e;
		}
	  }
	  
	  // 8.비밀번호 찾기 URL 및 UI (RST-563)
	  @Test(priority=8, enabled = true) 
	  public void checkfindpassword() throws Exception {
		String failMsg = "";
		  
		driver.get(CommonValues.SERVER_URL);
		Thread.sleep(100);
		driver.findElement(By.xpath(XPATH_FIND_PW)).click();
		  
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.PASSWORD_FIND_URL)){
			failMsg = failMsg + "\n 1.wrong find password url [Expected]" + CommonValues.SERVER_URL + CommonValues.PASSWORD_FIND_URL
				+" [Actual]" + driver.getCurrentUrl();
		}
		  
		String desc1 = driver.findElement(By.xpath("//p[1]")).getText();
		String desc2 = driver.findElement(By.xpath("//p[2]")).getText();
	
		if(!(desc1 + "\n" + desc2).contentEquals(FINDPASSWORD_DESC)) {
			failMsg = failMsg + "\n 2.wrong find password UI Language resource [Expected]" + FINDPASSWORD_DESC
				+" [Actual]" + desc1 + "\n" + desc2;
		}
		Boolean textbox = driver.findElement(By.xpath("//input[@class='password-find__email-input']")).isDisplayed();
		if(textbox == false) {
			failMsg = failMsg + "\n 3.don't displayed textbox";
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		
		Boolean btn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isDisplayed();
		if(btn == false) {
			failMsg = failMsg + "\n 4.don't displayed confirmBtn";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  // 9.비밀번호 찾기 이메일 빈값 (RST-564)
	  @Test(priority=9, enabled = true)
	  public void checkfindpassword_empty() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(2000);
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		if(!alert_msg.contentEquals(FINDPASSWORD_ALERT_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG Language resource [Expected]" + FINDPASSWORD_ALERT_MSG
					+" [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText().contentEquals(FINDPASSWORD_ERROR_MSG)){
			failMsg = failMsg + "\n 2.wrong ErrorMSG Language resource [Expected]" + FINDPASSWORD_ERROR_MSG
					+" [Actual]" + driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText();			
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  // 10.비밀번호 찾기 등록되지 않은 ID (RST-565)
	  @Test(priority=10, enabled = true)
	  public void checkfindpassword_invalid() throws Exception {
		String failMsg = "";
		
		WebElement textbox = driver.findElement(By.xpath("//input[@class='password-find__email-input']"));
		textbox.click();
		textbox.sendKeys(WRONGEMAIL);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		//click confirm
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(2000);
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
			
		if(!alert_msg.contentEquals(FINDPASSWORD_ALERT_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG Language resource [Expected]" + FINDPASSWORD_ALERT_MSG
						+" [Actual]" + alert_msg;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  // 11.비밀번호 찾기 잘못된 ID 값 (RST-566)
	  @Test(priority=11, enabled = true)
	  public void checkfindpassword_invalid2() throws Exception {
		String failMsg = "";
		
		WebElement textbox = driver.findElement(By.xpath("//input[@class='password-find__email-input']"));
		
		textbox.click();
		textbox.clear();
		
		Thread.sleep(1000);
		textbox.sendKeys("!!!!!!!!");
		
		if(!driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText().contentEquals(FINDPASSWORD_ERROR_MSG)
				&& (!driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).isDisplayed())){
			failMsg = failMsg + "\n 2.wrong ErrorMSG don't displayed [Expected]" + FINDPASSWORD_ERROR_MSG
					+" [Actual]" + driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText();			
		}
		//click confirm
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(2000);
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(FINDPASSWORD_ALERT_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG  [Expected]" + FINDPASSWORD_ALERT_MSG
					+" [Actual]" + alert_msg;
		}
		Thread.sleep(1000);
		
		textbox.click();
		textbox.clear();
		
		Thread.sleep(1000);
		textbox.sendKeys("1234567890");
		
		if(!driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText().contentEquals(FINDPASSWORD_ERROR_MSG)
				&& (!driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).isDisplayed())){
			failMsg = failMsg + "\n 2.wrong ErrorMSG don't displayed [Expected]" + FINDPASSWORD_ERROR_MSG
					+" [Actual]" + driver.findElement(By.xpath("//span[@class='password-find__error-msg']")).getText();			
		}
		//click confirm
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(2000);		  
		Alert alert2 = driver.switchTo().alert();
		String alert_msg2 = alert.getText();
		alert2.accept();
		Thread.sleep(1000);
					
		if(!alert_msg2.contentEquals(FINDPASSWORD_ALERT_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG  [Expected]" + FINDPASSWORD_ALERT_MSG
					+" [Actual]" + alert_msg2;
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  @Test(priority=12, enabled = true)
	  public void checkchangepassword() throws Exception {
		String failMsg = "";
		
		WebElement textbox = driver.findElement(By.xpath("//input[@class='password-find__email-input']"));
		textbox.click();
		textbox.clear();
		textbox.sendKeys(CommonValues.USERMAIL_RSUP12);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		//click confirm
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(2000);
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(FINDPASSWORD_SUBMIT_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG  [Expected]" + FINDPASSWORD_SUBMIT_MSG
					+" [Actual]" + alert_msg;
		}
		Thread.sleep(1000);
		
		DBConnection connection = new DBConnection();
		changepasswordkey = connection.getPasswordResetKey(CommonValues.USERMAIL_RSUP12);
		Thread.sleep(2000);
		System.out.println(changepasswordkey);
		
		driver.get(CommonValues.SERVER_URL+CommonValues.PASSWORD_CHANGE_URL+changepasswordkey);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(!driver.findElement(By.xpath("//p[@class='password-change__title']")).getText().contentEquals("Enter the new password (8 ~ 24 letters, numbers and symbols).")){
			failMsg = failMsg + "\n 1.wrong password change title [Expected]" + "Enter the new password (8 ~ 24 letters, numbers and symbols)."
					+" [Actual]" + driver.findElement(By.xpath("//p[@class='password-change__title']")).getText();			
		}
		
		if (!driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).getAttribute("placeholder").contentEquals("Enter new password")) {
			failMsg = failMsg + "\n 2.wrong placeholder [Expected]" + "Enter new password "
					+" [Actual]"  + driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).getAttribute("placeholder");                     
		}
		if(!driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).getAttribute("placeholder").contentEquals("Confirm password")) {
			{
				failMsg = failMsg + "\n 3.wrong placeholder [Expected]" + "Confirm password"
						+" [Actual]"  + driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).getAttribute("placeholder");
			}
		}
		
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		
		if(!driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).isDisplayed()) {
			failMsg = failMsg + "\n 3.Change password Btn is not displayed";
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  @Test(priority=13, enabled = true)
	  public void checkchangepassword_empty() throws Exception {
		String failMsg = "";
		
		WebElement ChangepasswordBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", ChangepasswordBtn);
		
		ChangepasswordBtn.click();
		
		String changepassword_errorMsg = driver.findElement(By.xpath("//span[@class='password-change__error-msg']")).getText();

		if(!changepassword_errorMsg.contentEquals(CHANGEPASSWORD_ERROR_MSG)){
			failMsg = failMsg + "\n 1.wrong error msg [Expected]" + CHANGEPASSWORD_ERROR_MSG
					+" [Actual]" + changepassword_errorMsg;			
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  @Test(priority=14, enabled = true)
	  public void checkchangepassword_invalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).sendKeys(CommonValues.CHANGEPW_RSUP12);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).click();
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).sendKeys(CommonValues.USERPW);
		Thread.sleep(1000);
		
		WebElement ChangepasswordBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", ChangepasswordBtn);
		
		ChangepasswordBtn.click();
		Thread.sleep(1000);
		
		String changepassword_errorMsg2 = driver.findElement(By.xpath("//span[@class='password-change__error-msg']")).getText();
		
		if(!changepassword_errorMsg2.contentEquals(CHANGEPASSWORD_ERROR_MSG2)){
			failMsg = failMsg + "\n 1.wrong error msg [Expected]" + CHANGEPASSWORD_ERROR_MSG2
					+" [Actual]" + changepassword_errorMsg2;			
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  @Test(priority=15, enabled = true)
	  public void checkchangepassword_invalid2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).clear();
		
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).sendKeys(CommonValues.USERPW_WRONG_CASE[0]);
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).sendKeys(CommonValues.USERPW_WRONG_CASE[0]);
		
		WebElement ChangepasswordBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", ChangepasswordBtn);
		
		ChangepasswordBtn.click();
		
		List<WebElement> errorMsg = driver.findElements(By.xpath("//span[@class='password-change__error-msg']"));
		errorMsg.get(0).getText();

		if(!errorMsg.get(0).getText().contentEquals(CHANGEPASSWORD_ERROR_MSG)
		&& !errorMsg.get(1).getText().contentEquals(CHANGEPASSWORD_ERROR_MSG)) {
			failMsg = failMsg + "\n 1.wrong error msg [Expected]" + CHANGEPASSWORD_ERROR_MSG
					+" [Actual]" + "1" + errorMsg.get(0).getText() + "2" + errorMsg.get(1).getText();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	  }
	  
	  @Test(priority=16, enabled = true)
	  public void changepassword() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")));
		
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).clear();
		
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[1]")).sendKeys(CommonValues.CHANGEPW_RSUP12);
		driver.findElement(By.xpath("//div[@class='password-change__inner']/input[2]")).sendKeys(CommonValues.CHANGEPW_RSUP12);
		
		WebElement ChangepasswordBtn = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']"));
		
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView();", ChangepasswordBtn);
		
		ChangepasswordBtn.click();
		Thread.sleep(2000);
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(CHANGEPASSWORD_COMPLETE_MSG)) {
			failMsg = failMsg + "\n 1.wrong alertMSG  [Expected]" + CHANGEPASSWORD_COMPLETE_MSG
					+" [Actual]" + alert_msg;
		}
		if(!driver.getCurrentUrl().contentEquals(CommonValues.INTRODUCE_URL)){
			failMsg = failMsg + "\n 2.wrong URL [Expected]" + CommonValues.INTRODUCE_URL
					+" [Actual]" + driver.getCurrentUrl();
				}
		
		driver.get(CommonValues.SERVER_URL);
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys(CommonValues.USERMAIL_RSUP12);
	    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(CommonValues.CHANGEPW_RSUP12);
		
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(ListTest.XPATH_LIST_TAB_SAVED)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}

	    String listurl = CommonValues.SERVER_URL + CommonValues.LIST_URI;

	    if(!driver.getCurrentUrl().contentEquals(listurl) && !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INTRO_URI))
	    {
	    	Exception e =  new Exception("login fail!!! : " + driver.getCurrentUrl());
	    	throw e;
	    }
	  }
	  
	  @Test(priority=17, enabled = true)
	  public void changepassword_back() throws Exception {
		  
		driver.findElement(By.id("profile-drop-down")).click();
		driver.findElement(By.linkText("My information")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    if (driver.findElement(By.id("email")).isEnabled()) 
	    {
	    	Exception e =  new Exception("email field is enable");
	    	throw e;
	    }
	    driver.findElement(By.xpath("//button[@class='profile__basic__find-pwd-btn']")).click();
	    driver.findElement(By.id("oldPassword")).sendKeys(CommonValues.CHANGEPW_RSUP12);
	    driver.findElement(By.id("newPassword")).sendKeys(CommonValues.USERPW);
	    driver.findElement(By.id("confirmNewPassword")).sendKeys(CommonValues.USERPW);
	    
	    
	    JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		
	    driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
	    Thread.sleep(1000);
	    driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
	}

	//20. 로그인 토큰확인 : 탭 연결 확인
	@Test(priority = 20, enabled = true)
	public void logintoken_tab() throws Exception {
		String failMsg = "";
				
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, CommonValues.USEREMAIL);
		
		//빈 새탭
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,"t");
		driver.findElement(By.linkText("")).sendKeys(selectLinkOpeninNewTab);
		
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		if (tabs2.size() == 2) {
			driver.switchTo().window(tabs2.get(1));
			
			driver.get(CommonValues.SERVER_URL);
			Thread.sleep(500);
			
			if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
				failMsg = "1. second tab login error. current url : " + driver.getCurrentUrl();
			}
			// close 1 tab
			driver.close();
			// switch room tab
			driver.switchTo().window(tabs2.get(0));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//21. 로그인 토큰확인 : 중복로그인
	@Parameters({"browser"})
	@Test(priority = 21, enabled = true)
	public void logintoken_duplicate(String browsertype) throws Exception {
		String failMsg = "";
				
		driver.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		WebDriver secondDriver = null;
		secondDriver = comm.setDriver(secondDriver, browsertype, "lang=en_US");

		// 첫번째 브라우저 로그인
		comm.loginseminar(driver, CommonValues.USEREMAIL);
		Thread.sleep(1000);
		
		// 두번째 브라우저 로그인
		comm.loginseminar(secondDriver, CommonValues.USEREMAIL);
		Thread.sleep(1000);
		
		// 첫번째 브라우저 세미나 만들기 클릭
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot find element : " + e.getMessage());
		}
		
		
		if(isElementPresent(By.xpath(OnAirRoom.XPATH_ROOM_TOAST))){
			if(driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(MSG_LOGIN_DUPLICATED)) {
				failMsg = failMsg + "1. error message. [Expected]" + MSG_LOGIN_DUPLICATED 
						+ " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		}
		Thread.sleep(1000);
		/*
		if(isAlertPresent()) {
			Alert alert = driver.switchTo().alert();
			String alert_msg = alert.getText();
			alert.accept();
			
			if(!alert_msg.contentEquals(MSG_LOGIN_DUPLICATED)) {
				failMsg = failMsg + "1. alert message. [Expected]" + MSG_LOGIN_DUPLICATED 
						+ " [Actual]" + alert_msg;
			}
			
		} else {
			failMsg = "0. cannot find alert.";
		}
		*/
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL) 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/login")) {
			failMsg = failMsg + "\n2. token error. current url : " + driver.getCurrentUrl();
		}
		secondDriver.quit();
		
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
