package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* JoinSeminar
 * 1. 회원가입 페이지 이동
 * 2. 국가선택 박스 확인
 * 2-2. 이메일란 placeholder, 빈값일때 에러 확인
 * 3. 이메일란 잘못된값 입력 후 에러 확인
 * 4. 이메일란 이미 가입되어 있는 이메일 입력 후 에러 확인
 * 5. 이메일란 가입되지 않은 정상 이메일 입력 확인
 * 10. 인증번호 란 placeholder, 빈값일때 에러
 * 11. 인증번호란 잘못된 값 입력 후 에러
 * 12. 인증번호 - 만료 : 인증번호 2번 부여. 첫번째 인증번호로 가입시도 
 * 13. 인증번호 - invalid : 인증번호 받기. 이메일을 다른 이메일로 변경 후 가입시도
 * 
 * 20. 비밀번호란 placeholder, 빈값일때 에러
 * 21. 비밀번호란 잘못된 값(조건 미충족)
 * 22. 비밀번호란 잘못된 값2(조건 미충족) : 연속된 문자숫자, 중복문자숫자
 * 23. 비밀번호란 비밀번호 조건 충족
 * 30. 닉네임란 placeholder, 빈값 에러
 * 31. 닉네임 잘못된 포멧
 * 32. 닉네임 길이 초과
 * 33. 닉네임 정상 포멧
 * 40. 전화번호 필드 placeholder, 빈값(정상)
 * 41. 전화번호 필드 잘못된 포멧
 * 42. 전화번호 필드 길이 초과
 * 43. 전화번호 필드 정상포멧
 * 50. 회사명 필드 placeholder, 빈값(정상)
 * 51. 회사명 필드 잘못된 포멧
 * 52. 회사명 필드 길이 초과
 * 53. 회사명 필드 정상 포멧
 * 60. 직책 필드 placeholder, 빈값(정상)
 * 61. 직책 필드 잘못된 포멧
 * 62. 직책필드 길이 초과
 * 63. 직책필드 정상 포멧
 * 
 * 70. 마케팅동의 체크박스
 * 71. 회원가입 정상 - 마케팅 동의 체크안함 - 내정보 확인 후 탈퇴
 * 75. 회원가입 정상 - 마케팅 동의 체크 
 * 76. 가입한 회원 채널 URL변경 - 정상케이스 - 회원탈퇴
 */

public class JoinSeminar {
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String XPATH_EMAIL = "//input[@name='email']";
	public static String XPATH_NICKNAME = "//input[@name='nickname']";
	public static String XPATH_AUTHCODE = "//input[@name='authCode']";
	public static String XPATH_PASSWORD = "//input[@name='password']";
	public static String XPATH_PHONE = "//input[@name='phone']";
	public static String XPATH_COMPANY = "//input[@name='companyName']";
	public static String XPATH_POSITION = "//input[@name='jobPosition']";
	
	public static String XPATH_EMAIL_ERROR = "//input[@name='email']/../../../span[@class='form__content__error-msg']";
	public static String XPATH_EMAIL_VALID = "//input[@name='email']/../../../span[@class='form__content__auth-email']";
	public static String XPATH_AUTHCODE_ERROR = "//input[@name='authCode']/../../../span[@class='form__content__error-msg']";
	public static String XPATH_PASSWORD_ERROR = "//input[@name='password']/../../../span[@class='form__content__error-msg']";
	public static String XPATH_NICKNAME_ERROR = "//input[@name='nickname']/../../../span[@class='form__content__error-msg']";
	public static String XPATH_COMPANY_ERROR = "//input[@name='companyName']/../../../span[@class='form__content__error-msg']";
	public static String XPATH_POSITION_ERROR = "//input[@name='jobPosition']/../../../span[@class='form__content__error-msg']";
	
	public static String CHANNEL_URI = "hello";
	
	public String seminarID = "";
	
	public String JOIN_USER = "rsrsup1@rsupport.com";
	
	public static String MSG_CONFIRM_WITHDRAWAL = "Do you really want to unsubscribe?";
	
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
	
	//1. 회원가입 페이지 이동
	@Test(priority=1)
	public void gotojoinpage() throws Exception {
	    driver.get(CommonValues.SERVER_URL);
	    driver.findElement(By.id("lang")).click();
	    //driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='��'])[1]/following::div[3]")).click();
	    driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
	    
	    driver.findElement(By.xpath("//div[@class='l-right']/ul/li[2]")).click();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    String joinURL = CommonValues.SERVER_URL + "/signup/form";
	    if(!driver.getCurrentUrl().contentEquals(joinURL)) {
	    	String currenturl = driver.getCurrentUrl();
	    	driver.get(joinURL);
	    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    	Exception e =  new Exception("join pange : " + currenturl);
	    	throw e;
	    }
	    
	  }	

	//2. 국가선택 박스 확인
	@Test(priority = 2, enabled = true)
	public void checkCountryBox() throws Exception {
		boolean isPass = true;
		String failMsg = "";
		Thread.sleep(500);
		//select box : Korea
		if(!driver.findElement(By.xpath("//select[@name='country']/option[@value='kr']")).getText().trim().contentEquals("Korea"))
		{
			isPass = false;
	    	failMsg = "Select Box Vaule error (Korea) : " +  driver.findElement(By.xpath("//select[@name='country']/option[@value='kr']")).getText();
		}

		// select box : United State
		if (!driver.findElement(By.xpath("//select[@name='country']/option[@value='us']")).getText().trim().contentEquals("United States of America")) {
			isPass = false;
			failMsg = failMsg + "\n" + "Select Box Vaule error (United States of America) : " + driver.findElement(By.xpath("//select[@name='country']/option[@value='us']")).getText();
		}

		// select box : Japan
		if (!driver.findElement(By.xpath("//select[@name='country']/option[@value='jp']")).getText().trim().contentEquals("Japan")) {
			isPass = false;
			failMsg = failMsg + "\n" + "Select Box Vaule error (Japan) : " + driver.findElement(By.xpath("//select[@name='country']/option[@value='jp']")).getText();
		}
		
		Thread.sleep(500);
		
		if(!isPass)
	    {
	    	Exception e =  new Exception(failMsg);
	    	throw e;
	    }
	}
	
	//2-2. 이메일란 placeholder, 빈값일때 에러 확인
	@Test(priority = 2, enabled = true)
	public void checkEmailfield_empty() throws Exception {
		
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_EMAIL)).click();
	    driver.findElement(By.xpath(XPATH_EMAIL)).clear();
	    
	    //placeholder
	    if (!driver.findElement(By.xpath(XPATH_EMAIL)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_EMAIL))
	    {
	    	failMsg = "no placeholder (email) : " + driver.findElement(By.xpath(XPATH_EMAIL)).getAttribute("placeholder");
	    }
	    
	    //check email error
	    driver.findElement(By.xpath(XPATH_AUTHCODE)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText().contentEquals(CommonValues.REQUIRED_VALUE))
	    {
	    	failMsg = failMsg + "\n" + "wrong error msg [1] (empty email) : " + driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText();
	    	
	    }
	    
	    //click auth email
	    driver.findElement(By.xpath(" //button[@class='form__auth-code-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText().contentEquals(CommonValues.REQUIRED_VALUE))
	    {
	    	failMsg = failMsg + "\n" + "wrong error msg2 [2] [(empty email) : " + driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText();
	    }
	    
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	    
	}
	
	//3. 이메일란 잘못된값 입력 후 에러 확인
	@Test(priority = 3, enabled = true)
	public void checkEmailfield_invalid() throws Exception {
		String failMsg = "";
		
		 //check wrong format
	    driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.TWENTY_ONE);
	    driver.findElement(By.xpath(XPATH_AUTHCODE)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText().contentEquals(CommonValues.INCORRECT_EMAIL_FORMAT))
	    {
	    	failMsg = "wrong error msg(worng email) : " + driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText();
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//4. 이메일란 이미 가입되어 있는 이메일 입력 후 에러 확인
	@Test(priority = 4, enabled = true)
	public void checkEmailfield_duplicate() throws Exception {
		String failMsg = "";
		
		//same email
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
	    driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL);
	    driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText().contentEquals(CommonValues.SAME_EMAIL))
	    {
	    	failMsg = "wrong error msg(same email) : " + driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText();
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//5. 이메일란 가입되지 않은 정상 이메일 입력 확인
	@Test(priority = 5, enabled = true)
	public void checkEmailfield_valid() throws Exception {
		String failMsg = "";
		
		//valid email
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
	    driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
	    driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_EMAIL_VALID)).getText().contentEquals(CommonValues.EMAIL_SEND_MSG))
	    {
	    	failMsg = failMsg + "\n" + "wrong error msg(vaild email) : " + driver.findElement(By.xpath(XPATH_EMAIL_VALID)).getText();
	    }
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//10. 인증번호 란 placeholder, 빈값일때 에러
	@Test(priority = 10, enabled = true)
	public void checkEmailauthfield_empty() throws Exception {
		String failMsg = "";
		
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));
		
		if (!driver.findElement(By.xpath(XPATH_AUTHCODE)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_AUTHCODE))
	    {
		
	    	failMsg = "no placeholder (authCode) : " + driver.findElement(By.xpath(XPATH_AUTHCODE)).getAttribute("placeholder");
	    }
		
		//input normal email
		driver.findElement(By.xpath(XPATH_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_EMAIL)).click();
	    driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
	    
	    //check auth code error
	    driver.findElement(By.xpath(XPATH_AUTHCODE)).clear();
	    driver.findElement(By.xpath(XPATH_AUTHCODE)).click();
	    
	    driver.findElement(By.xpath(XPATH_PASSWORD)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText().contentEquals(CommonValues.REQUIRED_VALUE))
	    {
	    	failMsg = failMsg + "\n" + "wrong error msg [1] (empty authCode) : " + driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText();
	    	
	    }
	    
	    //click signup 
	    driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText().contentEquals(CommonValues.REQUIRED_VALUE))
	    {
	    	failMsg = failMsg + "\n" + "wrong error msg2 [2] [(empty authCode) : " + driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText();
	    	
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//11. 인증번호란 잘못된 값 입력 후 에러
	@Test(priority = 11, enabled = true)
	public void checkEmailauthfield_invalid2() throws Exception {
		String failMsg = "";
		
		//pw, nickname input valid
		driver.findElement(By.xpath(XPATH_PASSWORD)).clear();
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_NICKNAME)).clear();
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys("test");
		
		//check wrong format
	    driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.TWENTY_ONE);
	    driver.findElement(By.xpath(XPATH_PASSWORD)).click();
	    driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText().contentEquals(CommonValues.INVALID_AUTHCODE_MSG))
	    {
	    	failMsg = "wrong error msg(worng authCode) : " + driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText();
	 
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//12. 인증번호 - 만료 : 인증번호 2번 부여. 첫번째 인증번호로 가입시도 
	@Test(priority = 12, enabled = true)
	public void checkEmailauthfield_oldcode() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(JOIN_USER);
		
		driver.findElement(By.xpath(XPATH_PASSWORD)).clear();
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
		
		driver.findElement(By.xpath(XPATH_NICKNAME)).clear();
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys("joinTC");
		
		DBConnection connection = new DBConnection();
		ArrayList<String> beforeCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
		Thread.sleep(500);
		ArrayList<String> afterCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		for (String str : afterCode) { 
			if (!beforeCode.contains(str)) {
				driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(str);
			}
		}
		
		driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
		Thread.sleep(500);

		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		
	    if (!driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText().contentEquals(CommonValues.INVALID_AUTHCODE_MSG))
	    {
	    	failMsg = "wrong error msg(expired authCode) : " + driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText();
	 
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//13. 인증번호 - invalid : 인증번호 받기. 이메일을 다른 이메일로 변경 후 가입시도
	@Test(priority = 13, enabled = true)
	public void checkEmailauthfield_missmatch() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(JOIN_USER);
		
		driver.findElement(By.xpath(XPATH_PASSWORD)).clear();
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
		
		driver.findElement(By.xpath(XPATH_NICKNAME)).clear();
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys("joinTC");
		
		DBConnection connection = new DBConnection();
		ArrayList<String> beforeCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
		Thread.sleep(500);
		ArrayList<String> afterCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		for (String str : afterCode) { 
			if (!beforeCode.contains(str)) {
				driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(str);
			}
		}
		
		driver.findElement(By.xpath(XPATH_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys("missmatch@rsupport.com");
		Thread.sleep(100);
		
		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		
	    if (!driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText().contentEquals(CommonValues.INVALID_AUTHCODE_MSG))
	    {
	    	failMsg = "wrong error msg(change email) : " + driver.findElement(By.xpath(XPATH_AUTHCODE_ERROR)).getText();
	 
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	//20. 비밀번호란 placeholder, 빈값일때 에러
	@Test(priority = 20, enabled = true)
	public void checkPwfield_empty() throws Exception {
		String failMsg = "";	
		
		// init email, auth code
		clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));

		if (!driver.findElement(By.xpath(XPATH_PASSWORD)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_PASSWORD)) {
			failMsg = "no placeholder (password) : "
					+ driver.findElement(By.xpath(XPATH_PASSWORD)).getAttribute("placeholder");
		}

		// input normal email & auth code
		driver.findElement(By.xpath(XPATH_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(JOIN_USER);
		driver.findElement(By.xpath(XPATH_AUTHCODE)).clear();
		driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.AUTH_CODE);

		// check pw error
		driver.findElement(By.xpath(XPATH_PASSWORD)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
		driver.findElement(By.xpath(XPATH_PASSWORD)).click();
		
		driver.findElement(By.xpath(XPATH_EMAIL)).click();
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText()
				.contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = failMsg + "\n" + "wrong error msg [1] (empty password) : " + driver
					.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();

		}

		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText()
				.contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = failMsg + "\n" + "wrong error msg2 [2] [(empty password) : " + driver
					.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();

		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//21. 비밀번호란 잘못된 값(조건 미충족)
	@Test(priority = 21, enabled = true)
	public void checkPwfield_invalid() throws Exception {
		String failMsg = "";
		
		//check wrong format
	    for(int i=0 ; i < CommonValues.USERPW_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
	    	driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_WRONG_CASE[i]);
	    	driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		    Thread.sleep(500);
		    if (!driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText().contentEquals(CommonValues.USERPW_WRONG_ERROR))
		    {
				failMsg = failMsg + "\n" + i + ". wrong error msg(worng password : " + CommonValues.USERPW_WRONG_CASE[i] + ") : " + driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();
		    
		    }  
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//22. 비밀번호란 잘못된 값2(조건 미충족) : 연속된 문자숫자, 중복문자숫자, 이메일과 동일
	@Test(priority = 22, enabled = true)
	public void checkPwfield_invalid2() throws Exception {
		String failMsg = "";
		
		//check wrong format
	    for(int i=0 ; i < CommonValues.USERPW_WRONG_CASE2.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
	    	driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_WRONG_CASE2[i]);
	    	driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		    Thread.sleep(500);
		    if (!driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText().contentEquals(CommonValues.USERPW_WRONG_ERROR3))
		    {
				failMsg = failMsg + "\n2-" + i + ". wrong error msg(worng password : " + CommonValues.USERPW_WRONG_CASE2[i] + ") : " + driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();
		    
		    }  
	    }
		
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
    	driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(JOIN_USER);
    	driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText().contentEquals(CommonValues.USERPW_WRONG_ERROR2))
	    {
			failMsg = failMsg + "\n3" + ". wrong error msg(same email : " + JOIN_USER + ") : " + driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();
	    
	    }  
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//23. 비밀번호란 비밀번호 조건 충족
	@Test(priority = 23, enabled = true)
	public void checkPwfield_valid() throws Exception {
		String failMsg = "";
		
		 // check valid case
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
    	driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
    	driver.findElement(By.xpath(XPATH_NICKNAME)).click();
	    Thread.sleep(500);
	    try {
	    	if (driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).isDisplayed())
		    {
				failMsg = "wrong error msg[(vaild password) : " + driver.findElement(By.xpath(XPATH_PASSWORD_ERROR)).getText();
	  
		    }
	    } catch(NoSuchElementException e) {
	    	//success
	    }
	    
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	 
	//30. 닉네임란 placeholder, 빈값 에러
	@Test(priority = 30, enabled = true)
	public void checkNicknamefield_empty() throws Exception {
		String failMsg = "";
		
		// init email, auth code, password
		clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));

		if (!driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_NICKNAME)) {
			failMsg = "no placeholder (nickname) : "
					+ driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("placeholder");

		}

		// input normal email, auth code, password
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
		driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.AUTH_CODE);
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_JOIN);

		// check nickname error
		driver.findElement(By.xpath(XPATH_NICKNAME)).clear();
		driver.findElement(By.xpath(XPATH_NICKNAME)).click();
		
		driver.findElement(By.xpath(XPATH_PHONE)).click();
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText()
				.contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = failMsg + "\n" + "wrong error msg [2] (empty nickname) : " + driver
					.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText();

		}

		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		if (!driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText()
				.contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = failMsg + "\n" + "wrong error msg [2] (empty nickname) : " + driver
					.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText();

		}
	    
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	  
	//31. 닉네임 잘못된 포멧
	@Test(priority = 31, enabled = true)
	public void checkNicknamefield_invalid() throws Exception {
		String failMsg = "";
		
		//check wrong format
	    for(int i=0 ; i < CommonValues.NICKNAME_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));
	    	driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.NICKNAME_WRONG_CASE[i]);
	    	driver.findElement(By.xpath(XPATH_PHONE)).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("value").contentEquals(CommonValues.USERNICKNAME_JOIN))
		    {
				failMsg = failMsg + "\n" + "fail worng nickname format : " + driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("value");
		    	
		    }
	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//32. 닉네임 길이 초과
	@Test(priority = 32, enabled = true)
	public void checkNicknamefield_invalid2() throws Exception {
		String failMsg = "";
		
		 //long case (max 20)
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));
	    driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.TWENTY_A + "111");
	    driver.findElement(By.xpath(XPATH_PHONE)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
			failMsg = "wrong error msg(long nickname(23bytes) : " + driver.findElement(By.xpath(XPATH_NICKNAME)).getAttribute("value");
	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//33. 닉네임 정상 포멧
	@Test(priority = 33, enabled = true)
	public void checkNicknamefield_valid() throws Exception {
		String failMsg = "";
		
		// check valid case
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));
    	driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.USERNICKNAME_JOIN);
    	driver.findElement(By.xpath(XPATH_PHONE)).click();
	    Thread.sleep(500);
	    if(isElementPresent(By.xpath(XPATH_NICKNAME_ERROR)))
	    {
	    	if (driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild nickname) : " + driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText();
		    
		    }
	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//40. 전화번호 필드 placeholder, 빈값(정상)
	@Test(priority = 40, enabled = true)
	public void checkPhonefield_empty() throws Exception {
		String failMsg = "";
		
		// init email, auth code, password
		clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));

		if (!driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_PHONE)) {
			failMsg = "no placeholder (phone) : " + driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("placeholder");

		}

		// input normal email, auth code, password
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
		driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.AUTH_CODE);
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_JOIN);
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.USERNICKNAME_JOIN);
		
		
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PHONE)));
    	driver.findElement(By.xpath(XPATH_PHONE)).clear();
    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
		
		if(isElementPresent(By.xpath(XPATH_NICKNAME_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild nickname) : " + driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText();
		
		    }
	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//41. 전화번호 필드 잘못된 포멧
	@Test(priority = 41, enabled = true)
	public void checkPhonefield_invalid() throws Exception {
		String failMsg = "";
		
		//check wrong format
	    for(int i=0 ; i < CommonValues.PHONE_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_PHONE)));
	    	driver.findElement(By.xpath(XPATH_PHONE)).sendKeys(CommonValues.PHONE_WRONG_CASE[i]);
	    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("value").contentEquals(CommonValues.USER_PHONE))
		    {
				failMsg = failMsg + "\n" + "fail worng phone format : [Actual]" + driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("value");

		    }
		    
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//42. 전화번호 필드 길이 초과
	@Test(priority = 42, enabled = true)
	public void checkPhonefield_invalid2() throws Exception {
		String failMsg = "";
		
		//long case (max 20)
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PHONE)));
	    driver.findElement(By.xpath(XPATH_PHONE)).sendKeys(CommonValues.TWENTY_ONE + "111");
	    driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("value").contentEquals(CommonValues.TWENTY_ONE))
	    {
			failMsg = failMsg + "\n" + "wrong error msg [2] (long phone(23bytes) : " + driver.findElement(By.xpath(XPATH_PHONE)).getAttribute("value");
	
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//43. 전화번호 필드 정상포멧
	@Test(priority = 43, enabled = true)
	public void checkPhonefield_valid() throws Exception {
		String failMsg = "";
		
		// check valid case
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_PHONE)));
    	driver.findElement(By.xpath(XPATH_PHONE)).sendKeys(CommonValues.USERNICKNAME_JOIN);
    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
	    
	    if(isElementPresent(By.xpath(XPATH_NICKNAME_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild nickname) : " + driver.findElement(By.xpath(XPATH_NICKNAME_ERROR)).getText();
		
		    }
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//50. 회사명 필드 placeholder, 빈값(정상)
	@Test(priority = 50, enabled = true)
	public void checkCompanyfield_empty() throws Exception {
		String failMsg = "";
		
		// init email, auth code, password
		clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));

		if (!driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_COMPANY)) {
			failMsg = "no placeholder (companyName) : "
					+ driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("placeholder");

		}

		// input normal email, auth code, password
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
		driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.AUTH_CODE);
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_JOIN);
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.USERNICKNAME_JOIN);
		
		clearAttributeValue(driver.findElement(By.xpath(XPATH_COMPANY)));
    	driver.findElement(By.xpath(XPATH_COMPANY)).clear();
    	driver.findElement(By.xpath(XPATH_POSITION)).click();
	    Thread.sleep(500);
	    
	    if(isElementPresent(By.xpath(XPATH_COMPANY_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_COMPANY_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild companyName) : " + driver.findElement(By.xpath(XPATH_COMPANY_ERROR)).getText();
		    }
	    }
	    

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//51. 회사명 필드 잘못된 포멧
	@Test(priority = 51, enabled = true)
	public void checkCompanyfield_invalid() throws Exception {
		String failMsg = "";
		
		//check wrong format
	    for(int i=0 ; i < CommonValues.COMPANY_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_COMPANY)));
	    	driver.findElement(By.xpath(XPATH_COMPANY)).sendKeys(CommonValues.COMPANY_WRONG_CASE[i]);
	    	driver.findElement(By.xpath(XPATH_POSITION)).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("value").contentEquals(CommonValues.USER_COMPANY))
		    {
				failMsg = failMsg + "\n" + "fail worng phone format : " + driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("value");
		    }
		    
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//52. 회사명 필드 길이 초과
	@Test(priority = 52, enabled = true)
	public void checkCompanyfield_invalid2() throws Exception {
		String failMsg = "";
		
		//long case (max 20)
		clearAttributeValue(driver.findElement(By.xpath(XPATH_COMPANY)));
	    driver.findElement(By.xpath(XPATH_COMPANY)).sendKeys(CommonValues.TWENTY_A + "111");
	    driver.findElement(By.xpath(XPATH_POSITION)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
			failMsg = failMsg + "\n" + "wrong error msg [2] (long company name(23bytes) : " + driver.findElement(By.xpath(XPATH_COMPANY)).getAttribute("value");
	    	
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//53. 회사명 필드 정상 포멧
	@Test(priority = 53, enabled = true)
	public void checkCompanyfield_valid() throws Exception {
		String failMsg = "";
		
		// check valid case
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_COMPANY)));
    	driver.findElement(By.xpath(XPATH_COMPANY)).sendKeys(CommonValues.USER_COMPANY);
    	driver.findElement(By.xpath(XPATH_POSITION)).click();
	    Thread.sleep(500);
	    
	    if(isElementPresent(By.xpath(XPATH_COMPANY_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_COMPANY_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild companyName) : " + driver.findElement(By.xpath(XPATH_COMPANY_ERROR)).getText();
		    }
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//60. 직책 필드 placeholder, 빈값(정상)
	@Test(priority = 60, enabled = true)
	public void checkPositionfield_empty() throws Exception {
		String failMsg = "";
		
		// init email, auth code, password
		clearAttributeValue(driver.findElement(By.xpath(XPATH_EMAIL)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_AUTHCODE)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_PASSWORD)));
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NICKNAME)));

		if (!driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("placeholder")
				.contentEquals(CommonValues.PLACEHOLDER_POSITION)) {
			failMsg = "no placeholder (jobPosition) : "
					+ driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("placeholder");
		}

		// input normal email, auth code, password
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(CommonValues.USEREMAIL_JOIN_TRY);
		driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(CommonValues.AUTH_CODE);
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW_JOIN);
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys(CommonValues.USERNICKNAME_JOIN);
		
		clearAttributeValue(driver.findElement(By.xpath(XPATH_POSITION)));
    	driver.findElement(By.xpath(XPATH_POSITION)).clear();
    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
	    
	    if(isElementPresent(By.xpath(XPATH_POSITION_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_POSITION_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild jobPosition) : " + driver.findElement(By.xpath(XPATH_POSITION_ERROR)).getText();
		    }
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//61. 직책 필드 잘못된 포멧
	@Test(priority = 61, enabled = true)
	public void checkPositionfield_invalid() throws Exception {
		String failMsg = "";
		
		 //check wrong format
	    for(int i=0 ; i < CommonValues.POSITION_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.xpath(XPATH_POSITION)));
	    	driver.findElement(By.xpath(XPATH_POSITION)).sendKeys(CommonValues.POSITION_WRONG_CASE[i]);
	    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("value").contentEquals(CommonValues.USER_POSITION))
		    {
				failMsg = failMsg + "\n" + "fail worng jobPosition format : " + driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("value");
		    }
		    
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//62. 직책필드 길이 초과
	@Test(priority = 62, enabled = true)
	public void checkPositionfield_invalid2() throws Exception {
		String failMsg = "";
		
		//long case (max 20)
		clearAttributeValue(driver.findElement(By.xpath(XPATH_POSITION)));
	    driver.findElement(By.xpath(XPATH_POSITION)).sendKeys(CommonValues.TWENTY_A + "111");
	    driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
			failMsg = failMsg + "\n" + "wrong error msg [2] (long jobPosition(23bytes) : " + driver.findElement(By.xpath(XPATH_POSITION)).getAttribute("value");

	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//63. 직책필드 정상 포멧
	@Test(priority = 63, enabled = true)
	public void checkPositionfield_valid() throws Exception {
		String failMsg = "";
		
		// check valid case
	    clearAttributeValue(driver.findElement(By.xpath(XPATH_POSITION)));
    	driver.findElement(By.xpath(XPATH_POSITION)).sendKeys(CommonValues.USER_POSITION);
    	driver.findElement(By.xpath(XPATH_COMPANY)).click();
	    Thread.sleep(500);
	    
	    if(isElementPresent(By.xpath(XPATH_POSITION_ERROR))) {
	    	if (driver.findElement(By.xpath(XPATH_POSITION_ERROR)).isDisplayed())
		    {
				failMsg = failMsg + "\n" + "wrong error msg[(vaild jobPosition) : " + driver.findElement(By.xpath(XPATH_POSITION_ERROR)).getText();
		    }
	    }
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//70. 마케팅동의 체크박스
	@Test(priority = 70, enabled = true)
	public void marketingCheckbox() throws Exception {
		String failMsg = "";
		
		if(isElementPresent(By.xpath("//label[@for='marketingAgreement']"))) {
			
			if(!driver.findElement(By.xpath("//label[@for='marketingAgreement']/span")).getText().contentEquals(CommonValues.MSG_MARKETING_AGREEMENT)) {
				failMsg = "1. marketing agreement checkbox msg [Expected]" + CommonValues.MSG_MARKETING_AGREEMENT
						+ " [Actual]" + driver.findElement(By.xpath("//label[@for='marketingAgreement']/span")).getText();
			}
			
			if(driver.findElement(By.xpath("//label[@for='marketingAgreement']/input")).isSelected()) {
				failMsg = failMsg + "\n2. marketing agreement checkbox defalut value [Expected] Uncheck + [Actual] Check";
			}
			
			driver.findElement(By.xpath("//label[@for='marketingAgreement']/div")).click();
			Thread.sleep(100);
			
			if(!driver.findElement(By.xpath("//label[@for='marketingAgreement']/input")).isSelected()) {
				failMsg = failMsg + "\n3. marketing agreement checkbox defalut value after click check [Expected] Check [Actual] Unheck";
			}
			
			driver.findElement(By.xpath("//label[@for='marketingAgreement']/div")).click();
			Thread.sleep(100);
			
			if(driver.findElement(By.xpath("//label[@for='marketingAgreement']/input")).isSelected()) {
				failMsg = failMsg + "\n4. marketing agreement checkbox defalut value after click uncheck [Expected] Uncheck [Actual] Check";
			}
			
		}else {
			failMsg = "not find marketing agreement checkbox";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//71. 회원가입 정상 - 마케팅 동의 체크안함 (JOIN_USER) - 내정보 확인 후 탈퇴
	@Test(priority = 71, enabled = true)
	public void joinUser_min() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/signup/form")) {
			driver.get(CommonValues.SERVER_URL + "/signup/form");
			Thread.sleep(100);
		}
		
		driver.navigate().refresh();
		Thread.sleep(500);
		
		
		driver.findElement(By.xpath(XPATH_EMAIL)).click();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(JOIN_USER);
		
		driver.findElement(By.xpath(XPATH_PASSWORD)).click();
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
		
		driver.findElement(By.xpath(XPATH_NICKNAME)).click();
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys("joinTC");
		
		DBConnection connection = new DBConnection();
		
		ArrayList<String> beforeCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		
		driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
		Thread.sleep(500);
		
		ArrayList<String> afterCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		for (String str : afterCode) { 
			if (!beforeCode.contains(str)) {
				driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(str);
			}
		}
		
		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/signup/finished")){
			failMsg = "1. not success join view [Actual]" + driver.getCurrentUrl();
			
			driver.get(CommonValues.SERVER_URL);
		} else {
			driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
			Thread.sleep(500);
		}
		
		//login
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, JOIN_USER);
		
	    Thread.sleep(1000); 

	    if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INTRO_URI)
	    		&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
	    	failMsg = failMsg + "\n2. joined user is login fail : " + driver.getCurrentUrl();
	    }
		
	    failMsg = checkMarketingAgreement(false);
	    joinUser_withdrawal();
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	

	//75. 회원가입 정상 - 마케팅 동의 체크
	@Test(priority = 75, enabled = true)
	public void joinUser_max() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/signup/form")) {
			driver.get(CommonValues.SERVER_URL + "/signup/form");
			Thread.sleep(100);
		}
		
		driver.navigate().refresh();
		Thread.sleep(500);
		
		
		driver.findElement(By.xpath(XPATH_EMAIL)).click();
		driver.findElement(By.xpath(XPATH_EMAIL)).sendKeys(JOIN_USER);
		
		driver.findElement(By.xpath(XPATH_PASSWORD)).click();
		driver.findElement(By.xpath(XPATH_PASSWORD)).sendKeys(CommonValues.USERPW);
		
		driver.findElement(By.xpath(XPATH_NICKNAME)).click();
		driver.findElement(By.xpath(XPATH_NICKNAME)).sendKeys("joinTC");
		
		driver.findElement(By.xpath(XPATH_COMPANY)).click();
		driver.findElement(By.xpath(XPATH_COMPANY)).sendKeys("joinCompany");
		driver.findElement(By.xpath(XPATH_POSITION)).click();
		driver.findElement(By.xpath(XPATH_POSITION)).sendKeys("joinJobPosition");
		driver.findElement(By.xpath(XPATH_PHONE)).click();
		driver.findElement(By.xpath(XPATH_PHONE)).sendKeys("0100000000");
		
		driver.findElement(By.xpath("//label[@for='marketingAgreement']/div")).click();
		Thread.sleep(100);
		
		DBConnection connection = new DBConnection();
		
		ArrayList<String> beforeCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		
		driver.findElement(By.xpath("//button[@class='form__auth-code-btn']")).click();
		Thread.sleep(500);
		
		ArrayList<String> afterCode = connection.getAuthCodeForJoin(JOIN_USER);
		
		for (String str : afterCode) { 
			if (!beforeCode.contains(str)) {
				driver.findElement(By.xpath(XPATH_AUTHCODE)).sendKeys(str);
			}
		}
		
		driver.findElement(By.xpath("//button[@class='form__submit-btn']")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/signup/finished")){
			failMsg = "1. not success join view [Actual]" + driver.getCurrentUrl();
			
			driver.get(CommonValues.SERVER_URL);
		} else {
			driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
			Thread.sleep(500);
		}
		
		//login
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, JOIN_USER);
	    Thread.sleep(1000); 

	    if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INTRO_URI)
	    		&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
	    	failMsg = failMsg + "\n2. joined user is login fail : " + driver.getCurrentUrl();
	    }
	    
	    failMsg = checkMarketingAgreement(true);
	    
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//76. 가입한 회원 채널 URL변경 - 정상케이스 - 회원탈퇴
	@Test(priority = 76, enabled = true)
	public void joinUser_channelURL() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='img-box']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//ul[@class='tab ']/li[2]")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL)) {
			failMsg = "1. not channel management view. current url : " + driver.getCurrentUrl();
	    	driver.get(CommonValues.SERVER_URL + CommonValues.CHANNEL_EDIT_URL);
	    	Thread.sleep(500);
		}
		
		//click Change URL button
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).click();
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).clear();
		
		driver.findElement(By.xpath("//input[@class='Input_basic__3hycH Input_input__mcpaw  input']")).sendKeys("hello");
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='desc valid']")).getText().contentEquals("This URL can be used.")) {
			failMsg = "1.valid url Message : [Actual] " + driver.findElement(By.xpath("//div[@class='desc valid']")).getText();
		}
		
		// button check
		if (!driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).isEnabled()) {
			failMsg = failMsg + "\n2.Selected button is disabled!(valid ID)";
		}

		//click confirm button
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		Thread.sleep(2000);
		
		String channelURL = CommonValues.SERVER_URL + CommonValues.CHANNEL_VIEW_URL + "/" + CHANNEL_URI;
		
		//channel url
		if(!driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText().contentEquals(channelURL)) {
			failMsg = failMsg + "\n3.channel url : [Expected]" + channelURL + " [Actual] " + driver.findElement(By.xpath("//span[@class='ChannelTitle_channel-url__1I1Qh']")).getText();
		}	
		
		// url button
		if (isElementPresent(By.xpath("//button[@class='btn btn-basic btn-s btn-edit-ChannelId']"))) {
			failMsg = failMsg + "\n4.change URL button is enabled";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	//80. 가입한 회원 채널 URL변경 - 정상케이스 - 회원탈퇴
	@Test(priority = 80, enabled = true)
	public void clearUser() throws Exception {
		joinUser_withdrawal();
	}
		
	public String checkMarketingAgreement(boolean marketing) throws InterruptedException {
		String failMsg = "";
		
		driver.get(CommonValues.SERVER_URL + "/settings/profile");
		Thread.sleep(100);
		
		if (marketing) {
			if(!driver.findElement(By.xpath("//input[@id='marketingAgreement']")).isSelected()) {
				failMsg = failMsg + "\n[Marketing]. marketing check box is unchecked.";
			}
		} else {
			if(driver.findElement(By.xpath("//input[@id='marketingAgreement']")).isSelected()) {
				failMsg = failMsg + "\n[Marketing]. marketing check box is checked.";
			}
		}
		return failMsg;
	}
	
	//회원탈퇴
	public void joinUser_withdrawal() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/settings/profile")) {
			driver.get(CommonValues.SERVER_URL + "/settings/profile");
			Thread.sleep(100);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@class='Profile_profile__etc__2QArS']//a")));
		driver.findElement(By.xpath("//div[@class='Profile_profile__etc__2QArS']//a")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL))
		{
			failMsg = failMsg + "\n3-2. no Delete Account view : " + driver.getCurrentUrl();
			driver.get(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL);
		}
		
		//confirm click check agree
		driver.findElement(By.xpath("//div[@class='checkbox']")).click();
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")));
		driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='confirm-body']")).getText().contentEquals(MSG_CONFIRM_WITHDRAWAL)) {
			failMsg = failMsg + "\n3-3. Confirm popup msg [Expected]" + MSG_CONFIRM_WITHDRAWAL 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='confirm-body']")).getText();
		}
		
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/login")) {
	    	failMsg = failMsg + "\n3-4. fail to withdrawal : " + driver.getCurrentUrl();
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			
	
	private void clearAttributeValue(WebElement el) {
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
