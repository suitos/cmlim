package seminartest;

import static org.testng.Assert.fail;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
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
import org.testng.annotations.Ignore;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* MyProfile
 * part1
 * 1. 이메일 필드 확인 - 비활성화
 * 2. 모두 빈값으로 저장 시도
 * 3. 닉네임필드 확인
 * 4. 회사 필드 확인
 * 5. 직책필드 확인
 * 6. 전화번호 필드 확인
 * 
 * 11. 저장 시도 - 취소
 * 12. 저장 시도 - 확인 (정상저장)
 * 
 * part2
 * 20. 회원 탈퇴 화면  : 동의 체크 없이 탈퇴 시도
 * 
 * part3
 * 30. 비밀번호 변경 : placeholder확인
 * 31. 빈값으로 저장 시도
 * 32. 현재 비밀번호란 확인
 * 33. 새 비밀번호란 확인
 * 34. 확인 비밀번호란 확인
 * 35. 새 비밀번호-확인비밀번호 다르게 저장 시도
 * 36. 이메일 주소와 동일한 비번 설정 시도
 * 37. 현재 비밀번호와 동일한 비밀번호로 저장 시도
 * 38. 비밀번호 변경화면 cancel
 * 39. 정상 비밀번호 변경 후 - 로그인 시도
 */

public class MyProfile {
	
	public static String XPATH_OLD_PW = "//input[@id='oldPassword']";
	public static String XPATH_NEW_PW = "//input[@id='newPassword']";
	public static String XPATH_CONFIRM_PW = "//input[@id='confirmNewPassword']";
	
	public static String MSG_DO_NOT_AGREE_DEL = "Review the terms and check the agreement box.";
	public static String MSG_CONFIRM_DEL = "Do you really want to unsubscribe?";
	public static String MSG_ERROR_NICKNAME = "This is required. Enter max. 20 alphanumeric characters.";
	
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
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority=0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver);
	  }	
	
	// 1. 이메일 필드 확인 - 비활성화
	@Test(priority=1)
	public void checkEmailfield() throws Exception 
	{
		moveMyProfile();
		
		String failMsg = "";
		
		if(driver.findElement(By.id("email")).isEnabled())
	    {
	    	failMsg = "Email Field is Enabled!";
	    }
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 2. 모두 빈값으로 저장 시도
	@Test(priority=2)
	public void saveEmpty() throws Exception 
	{
		moveMyProfile();
		
		clearAttributeValue(driver.findElement(By.id("companyName")));
		clearAttributeValue(driver.findElement(By.id("jobPosition")));
		clearAttributeValue(driver.findElement(By.id("phone")));
		
		driver.findElement(By.id("companyName")).click();
		driver.findElement(By.id("jobPosition")).click();
		
	    JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("phone")));
		driver.findElement(By.id("phone")).click();
		

		driver.findElement(By.xpath("//button[@class='profile__save-btn']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@id=''])[3]")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		System.out.println("Co Name : " + driver.findElement(By.id("companyName")).getText());
		if(!driver.findElement(By.id("companyName")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("companyName is not blank : " + driver.findElement(By.id("companyName")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("jobPosition")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("jobPosition is not blank : " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("phone")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("phone is not blank : " + driver.findElement(By.id("phone")).getAttribute("value"));
	    	throw e;
	    }
		
	}

	// 3. 닉네임필드 확인
	@Test(priority=3, dependsOnMethods = {"saveEmpty"}, alwaysRun = true, enabled = true)
	public void checkNickNamefield() throws Exception 
	{
		moveMyProfile();
		
		String failMsg = "";
		
	    driver.findElement(By.id("nickname")).click();

	    //check empty values (bug)
	    if(System.getProperty("os.name").toLowerCase().contains("mac")) {
	    	while(!driver.findElement(By.id("nickname")).getAttribute("value").isEmpty())
	    		driver.findElement(By.id("nickname")).sendKeys(Keys.BACK_SPACE);
	    } else {
	    	driver.findElement(By.id("nickname")).sendKeys(Keys.CONTROL , "a");
	    }
	    driver.findElement(By.id("nickname")).sendKeys(Keys.BACK_SPACE);
	    Thread.sleep(500);

	    if(!driver.findElement(By.xpath("//label[@for='nickname']/p")).getText().contentEquals(MSG_ERROR_NICKNAME)) {
	    	failMsg = "1. empty nickname error msg : [Expceted]" + MSG_ERROR_NICKNAME 
	    			+ " [Actual]" + driver.findElement(By.xpath("//label[@for='nickname']/p")).getText();
	    }
	    //click save
	    JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='profile__save-btn']")));
		driver.findElement(By.xpath("//button[@class='profile__save-btn']")).click();
		
		Thread.sleep(500);
		if(!driver.findElement(By.xpath("//label[@for='nickname']/p")).getText().contentEquals(MSG_ERROR_NICKNAME)) {
	    	failMsg = failMsg + "\n 2. empty nickname error msg (click save) : [Expceted]" + MSG_ERROR_NICKNAME 
	    			+ " [Actual]" + driver.findElement(By.xpath("//label[@for='nickname']/p")).getText();
	    }
	    
	    //long case (max 20)
	    driver.findElement(By.id("nickname")).sendKeys(CommonValues.TWENTY_A + "111");
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.id("companyName")));
	    driver.findElement(By.id("companyName")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.id("nickname")).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	failMsg = failMsg + "\n 3. wrong error msg(long nickname(input 23bytes) :" 
	    			+ " [Actual]" + driver.findElement(By.id("nickname")).getAttribute("value");
	    }
	    
	    //check wrong format
	    for(int i=0 ; i < CommonValues.NICKNAME_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.id("nickname")));
	    	driver.findElement(By.id("nickname")).sendKeys(CommonValues.NICKNAME_WRONG_CASE[i]);
			js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("companyName")));
	    	driver.findElement(By.id("companyName")).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.id("nickname")).getAttribute("value").contentEquals(CommonValues.USERNICKNAME_JOIN))
		    {
		    	failMsg = failMsg + "\n " + (3+i) + ". fail worng nickname format : [Expected]" + CommonValues.USERNICKNAME_JOIN
		    			+ " [Actual]" + driver.findElement(By.id("nickname")).getAttribute("value");
		    }
		    
	    }
	    
	    if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	    
	}

	// 4. 회사 필드 확인
	@Test(priority=4, dependsOnMethods = {"checkNickNamefield"}, alwaysRun = true, enabled = true)
	public void checkCompanyfield() throws Exception 
	{
	    //check empty values 
	    driver.findElement(By.id("companyName")).clear();
	    JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("companyName")));
	    driver.findElement(By.id("companyName")).click();
	    driver.findElement(By.id("companyName")).clear();
	    Thread.sleep(500);
	    
	    if (!driver.findElement(By.id("companyName")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("fail Company field (empty value): " + driver.findElement(By.id("companyName")).getAttribute("value"));
	    	throw e;
	    }

	    //long case (max 20)
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("phone")));
		
	    driver.findElement(By.id("companyName")).sendKeys(CommonValues.TWENTY_A + "111");
	    driver.findElement(By.id("phone")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.id("companyName")).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	Exception e =  new Exception("wrong error msg(long companyName(23bytes) : " + driver.findElement(By.id("companyName")).getAttribute("value"));
	    	throw e;
	    }
	    
	    //check wrong format
	    for(int i=0 ; i < CommonValues.COMPANY_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.id("companyName")));
	    	driver.findElement(By.id("companyName")).sendKeys(CommonValues.COMPANY_WRONG_CASE[i]);
	    	driver.findElement(By.id("phone")).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.id("companyName")).getAttribute("value").contentEquals(CommonValues.USER_COMPANY))
		    {
		    	Exception e =  new Exception("fail worng companyName format : " + driver.findElement(By.id("companyName")).getAttribute("value"));
		    	throw e;
		    }
		    
	    }
	    
	   
	}

	// 5. 직책필드 확인
	@Test(priority=5, dependsOnMethods = {"checkCompanyfield"}, alwaysRun = true, enabled = true)
	public void checkPositionfield() throws Exception 
	{
	    //check empty values 
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("jobPosition")));
		
	    driver.findElement(By.id("jobPosition")).clear();
	    driver.findElement(By.id("jobPosition")).click();
	    driver.findElement(By.id("jobPosition")).clear();
	    driver.findElement(By.id("phone")).click();
	    
	    
	    if (!driver.findElement(By.id("jobPosition")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("fail jobPosition field (empty value): " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
	    	throw e;
	    }

	    //long case (max 20)
	    driver.findElement(By.id("jobPosition")).sendKeys(CommonValues.TWENTY_A + "111");
	    driver.findElement(By.id("phone")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.id("jobPosition")).getAttribute("value").contentEquals(CommonValues.TWENTY_A))
	    {
	    	Exception e =  new Exception("wrong error msg(long jobPosition(23bytes) : " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
	    	throw e;
	    }
	    
	    //check wrong format
	    for(int i=0 ; i < CommonValues.POSITION_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.id("jobPosition")));
	    	driver.findElement(By.id("jobPosition")).sendKeys(CommonValues.POSITION_WRONG_CASE[i]);
	    	driver.findElement(By.id("phone")).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.id("jobPosition")).getAttribute("value").contentEquals(CommonValues.USER_POSITION))
		    {
		    	Exception e =  new Exception("fail worng jobPosition format : " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
		    	throw e;
		    }
		    
	    }
	}
	
	// 6. 전화번호 필드 확인
	@Test(priority=6, dependsOnMethods = {"checkPositionfield"}, alwaysRun = true, enabled = true)
	public void checkPhonefield() throws Exception 
	{
	    
	    //check empty values 
	    clearAttributeValue(driver.findElement(By.id("phone")));
	    driver.findElement(By.id("phone")).clear();
	    driver.findElement(By.id("jobPosition")).click();
	    
	    
	    if (!driver.findElement(By.id("phone")).getAttribute("value").isEmpty())
	    {
	    	Exception e =  new Exception("fail phone field (empty value): " + driver.findElement(By.id("phone")).getAttribute("value"));
	    	throw e;
	    }

	    //long case (max 20)
	    driver.findElement(By.id("phone")).sendKeys(CommonValues.TWENTY_ONE + "222");
	    driver.findElement(By.id("jobPosition")).click();
	    Thread.sleep(500);
	    if (!driver.findElement(By.id("phone")).getAttribute("value").contentEquals(CommonValues.TWENTY_ONE))
	    {
	    	Exception e =  new Exception("wrong error msg(long jobPosition(23bytes) : " + driver.findElement(By.id("phone")).getAttribute("value"));
	    	throw e;
	    }
	    
	    //check wrong format
	    for(int i=0 ; i < CommonValues.PHONE_WRONG_CASE.length ; i++)
	    {
	    	clearAttributeValue(driver.findElement(By.id("phone")));
	    	driver.findElement(By.id("phone")).sendKeys(CommonValues.PHONE_WRONG_CASE[i]);
	    	driver.findElement(By.id("jobPosition")).click();
		    Thread.sleep(500);
		    if(!driver.findElement(By.id("phone")).getAttribute("value").contentEquals(CommonValues.USER_PHONE))
		    {
		    	Exception e =  new Exception("fail worng phone format : " + driver.findElement(By.id("phone")).getAttribute("value"));
		    	throw e;
		    }
		    
	    }
	}
	
	// 11. 저장 시도 - 취소
	@Test(priority=11)
	public void cancelSave() throws Exception 
	{
		moveMyProfile();
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		String savedCo = driver.findElement(By.id("companyName")).getAttribute("value");
		String savedDe = driver.findElement(By.id("jobPosition")).getAttribute("value");
		String savedNo = driver.findElement(By.id("phone")).getAttribute("value");
		
		
		Thread.sleep(1000);
		
		clearAttributeValue(driver.findElement(By.id("companyName")));
		clearAttributeValue(driver.findElement(By.id("jobPosition")));
		clearAttributeValue(driver.findElement(By.id("phone")));

		driver.findElement(By.id("companyName")).click();
		driver.findElement(By.id("companyName")).sendKeys(CommonValues.USER_COMPANY);
		driver.findElement(By.id("jobPosition")).click();
		driver.findElement(By.id("jobPosition")).sendKeys(CommonValues.USER_POSITION);
		driver.findElement(By.id("phone")).sendKeys(CommonValues.USER_PHONE);
	
		
		Thread.sleep(1000);
		//click save - cancel
	    JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='profile__save-btn']")));
		driver.findElement(By.xpath("//button[@class='profile__save-btn']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Thread.sleep(500);
	
		Thread.sleep(1000);
		driver.navigate().refresh();
		Thread.sleep(1000);
		if(!driver.findElement(By.id("companyName")).getAttribute("value").contentEquals(savedCo))
	    {
	    	Exception e =  new Exception("companyName is worng : " + driver.findElement(By.id("companyName")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("jobPosition")).getAttribute("value").contentEquals(savedDe))
	    {
	    	Exception e =  new Exception("jobPosition is worng : " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("phone")).getAttribute("value").contentEquals(savedNo))
	    {
	    	Exception e =  new Exception("phone is worng : " + driver.findElement(By.id("phone")).getAttribute("value"));
	    	throw e;
	    }
	
	}
	
	// 12. 저장 시도 - 확인
	@Test(priority=12, dependsOnMethods = {"cancelSave"}, alwaysRun = true, enabled = true)
	public void validinformation() throws Exception 
	{
		
		moveMyProfile();

		Thread.sleep(1000);
		clearAttributeValue(driver.findElement(By.id("nickname")));
		clearAttributeValue(driver.findElement(By.id("companyName")));
		clearAttributeValue(driver.findElement(By.id("jobPosition")));
		clearAttributeValue(driver.findElement(By.id("phone")));
		
		driver.findElement(By.id("nickname")).click();
		driver.findElement(By.id("nickname")).sendKeys(CommonValues.USERNICKNAME_JOIN);;
		driver.findElement(By.id("companyName")).click();
		driver.findElement(By.id("companyName")).sendKeys(CommonValues.USER_COMPANY);
		driver.findElement(By.id("jobPosition")).click();
		driver.findElement(By.id("jobPosition")).sendKeys(CommonValues.USER_POSITION);
		driver.findElement(By.id("phone")).click();
		driver.findElement(By.id("phone")).sendKeys(CommonValues.USER_PHONE);

		//click save - save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='profile__save-btn']")));
		driver.findElement(By.xpath("//button[@class='profile__save-btn']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Thread.sleep(2000);

		
		if(!driver.findElement(By.id("nickname")).getAttribute("value").contentEquals(CommonValues.USERNICKNAME_JOIN))
	    {
	    	Exception e =  new Exception("nickname is worng : " + driver.findElement(By.id("nickname")).getAttribute("value"));
	    	throw e;
	    }
		
		if(!driver.findElement(By.id("companyName")).getAttribute("value").contentEquals(CommonValues.USER_COMPANY))
	    {
	    	Exception e =  new Exception("companyName is worng : " + driver.findElement(By.id("companyName")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("jobPosition")).getAttribute("value").contentEquals(CommonValues.USER_POSITION))
	    {
	    	Exception e =  new Exception("jobPosition is worng : " + driver.findElement(By.id("jobPosition")).getAttribute("value"));
	    	throw e;
	    }
		if(!driver.findElement(By.id("phone")).getAttribute("value").contentEquals(CommonValues.USER_PHONE))
	    {
	    	Exception e =  new Exception("phone is worng : " + driver.findElement(By.id("phone")).getAttribute("value"));
	    	throw e;
	    }
	
	}
	
	// 20. 회원 탈퇴 화면  : 동의 체크 없이 탈퇴 시도
	@Test(priority=20, dependsOnMethods = {"validinformation"}, alwaysRun = true, enabled = true)
	public void deleteAccount() throws Exception 
	{
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@class='Profile_profile__etc__2QArS']//a")));
		driver.findElement(By.xpath("//div[@class='Profile_profile__etc__2QArS']//a")).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL))
		{
			failMsg = "1. no Delete Account view : " + driver.getCurrentUrl();
			driver.get(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL);
		}
		
		//confirm click uncheck agree
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")));
		driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath("//div[@class='alert-body']")).getText().contentEquals(MSG_DO_NOT_AGREE_DEL)) {
			failMsg = failMsg + "\n 2. popup msg error(not agree) : [Expected]" + MSG_DO_NOT_AGREE_DEL
					 + " [Actual]" + driver.findElement(By.xpath("//div[@class='alert-body']")).getText();
		}
		
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. 회원 탈퇴 화면  : 동의 체크 후 확인 취소
	@Test(priority=21)
	public void deleteAccount_cancel() throws Exception 
	{
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL))
		{
			driver.get(CommonValues.SERVER_URL + CommonValues.DELETE_ACCOUNT_URL);
			Thread.sleep(500);
		}
		
		//check agree
		driver.findElement(By.xpath("//div[@class='fake-checkbox']")).click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")));
		driver.findElement(By.xpath("//div[@class='btn-box']/button[1]")).click();
		Thread.sleep(500);
		
		//popup msg
		if(!driver.findElement(By.xpath("//div[@class='confirm-body']")).getText().contentEquals(MSG_CONFIRM_DEL)) {
			failMsg = failMsg + "\n 2. popup msg error(confirm delete) : [Expected]" + MSG_CONFIRM_DEL
					 + " [Actual]" + driver.findElement(By.xpath("//div[@class='confirm-body']")).getText();
		}
		
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(500);
		
		//cancel
		driver.findElement(By.xpath("//div[@class='btn-box']/button[2]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 30. 비밀번호 변경 : placeholder확인
	@Test(priority=30)
	public void changePWView() throws Exception 
	{
		String failMsg = "";
		moveMyProfile();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//button[@class='profile__basic__find-pwd-btn']")).click();
		Thread.sleep(500);
		
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_OLD_PW)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_CURRENT_PW)) {
			failMsg = "placeholder error(current pw) [expected]" + CommonValues.PLACEHOLDER_CURRENT_PW + " [actual]" + driver.findElement(By.xpath(XPATH_OLD_PW)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_NEW_PW)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_NEW_PW)) {
			failMsg = failMsg + "\n" +  "placeholder error(current pw) [expected]" + CommonValues.PLACEHOLDER_NEW_PW + " [actual]" + driver.findElement(By.xpath(XPATH_NEW_PW)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_CONFIRM_PW)).getAttribute("placeholder").contentEquals(CommonValues.PLACEHOLDER_CONFIRM_PW)) {
			failMsg = failMsg + "\n" + "placeholder error(current pw) [expected]" + CommonValues.PLACEHOLDER_CONFIRM_PW + " [actual]" + driver.findElement(By.xpath(XPATH_CONFIRM_PW)).getAttribute("placeholder");
		}
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 31. 빈값으로 저장 시도
	@Test(priority=31)
	public void changePWEmpty() throws Exception 
	{
		String failMsg = "";
		//empty all
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(100);
		if(!driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.REQUIRED_VALUE)
				|| !driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = "error msg(empty values) [expected]" + CommonValues.REQUIRED_VALUE + " [actual : new pw]" + driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText()
					+ " [actual : confirm pw]" + driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText();
		}
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 32. 현재 비밀번호란 확인
	@Test(priority=32)
	public void changePW_CurrentPW() throws Exception 
	{
		String failMsg = "";
		
		//empty current pw, valid new/confirm
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(100);
		
		//empty
		if(!driver.findElement(By.xpath("//label[@for='oldPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg = failMsg + "\n 1. error msg(empty values) [expected]" + CommonValues.REQUIRED_VALUE + " [actual]" + driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText();
		}
		
		//invalid current pw, valid new/confirm
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys("test111!!!");
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(500);
		
		if(isElementPresent(By.xpath("//div[@class='modal-body']"))) {
			if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.CURRENT_PW_ERROR_MSG)) {
				failMsg = failMsg +  "\n 2. current pw error msg [expected]" + CommonValues.CURRENT_PW_ERROR_MSG + " [actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
			}
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		} else {
			failMsg = failMsg + "\n cannot find error popup";
		}
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 33. 새 비밀번호란 확인
	@Test(priority=33)
	public void changePW_newPW() throws Exception 
	{
		String failMsg = "";
		
		//valid current pw
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);
		
		//new pw : wrong format
		for(int i = 0 ; i < CommonValues.USERPW_WRONG_CASE.length ; i++) {
			driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
			driver.findElement(By.xpath(XPATH_NEW_PW)).click();
			driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
			clearAttributeValue(driver.findElement(By.xpath(XPATH_NEW_PW)));
			driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.USERPW_WRONG_CASE[i]);
			
			driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
			Thread.sleep(100);
			
			if(!driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.USERPW_WRONG_ERROR)) {
				failMsg = failMsg + "\n error msg(empty values) [expected]" + CommonValues.USERPW_WRONG_ERROR + " [actual]" + driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText();
			}
			
		}
		
		//new pw : email(id)
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.USEREMAIL);
		
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		Thread.sleep(100);
		
		if(!driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.USERPW_WRONG_ERROR)) {
			failMsg = failMsg + "\n error msg(empty values) [expected]" + CommonValues.USERPW_WRONG_ERROR + " [actual]" + driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText();
		}
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 34. 확인 비밀번호란 확인
	@Test(priority=34)
	public void changePW_confirmPW() throws Exception 
	{
		String failMsg = "";
		
		// reset
		driver.navigate().refresh();
		Thread.sleep(500);
		
		//valid current pw
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);
		
		//vaild new pw
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		
		//empty confirm
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		Thread.sleep(100);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(100);
		
		if(!driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText().contentEquals(CommonValues.REQUIRED_VALUE)) {
			failMsg =  "error msg(empty values) [expected]" + CommonValues.REQUIRED_VALUE + " [actual]" + driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText();
		}
	
		// old pw : wrong format
		for (int i = 0; i < CommonValues.USERPW_WRONG_CASE.length; i++) {
			driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
			driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
			driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
			clearAttributeValue(driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
			driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.USERPW_WRONG_CASE[i]);

			driver.findElement(By.xpath(XPATH_NEW_PW)).click();
			Thread.sleep(100);

			if (!driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText()
					.contentEquals(CommonValues.USERPW_WRONG_ERROR)) {
				failMsg = failMsg + "\n error msg(wrong values) [expected]" + CommonValues.USERPW_WRONG_ERROR
						+ " [actual]"
						+ driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']"))
								.getText();
			}

		}
	
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 35. 새 비밀번호-확인비밀번호 다르게 저장 시도
	@Test(priority=35)
	public void changePW_missMatch() throws Exception 
	{
		String failMsg = "";
		
		//valid current pw
		clearAttributeValue(driver.findElement(By.xpath(XPATH_OLD_PW)));
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);
		
		//vaild new pw
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NEW_PW)));
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		
		//miss match
		clearAttributeValue(driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.VALID_NEW_PW + "test");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(100);

		if (!driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText()
				.contentEquals(CommonValues.PW_MISSMATCH_ERROR_MSG)) {
			failMsg = failMsg + "\n error msg(miss match) [expected]" + CommonValues.PW_MISSMATCH_ERROR_MSG
					+ " [actual]"
					+ driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']"))
							.getText();
		}
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 36. 이메일 주소와 동일한 비번 설정 시도
	@Test(priority=36, enabled=true)
	public void changePW_sameEmail() throws Exception 
	{
		String failMsg = "";

		// valid current pw
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_OLD_PW)));
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);

		// vaild new pw
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NEW_PW)));
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.USEREMAIL);
		
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.USEREMAIL);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);

		if (!driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']")).getText()
				.contentEquals(CommonValues.USERPW_WRONG_ERROR2)) {
			failMsg = failMsg + "\n new PW : error msg(same value with Email) [expected]" + CommonValues.USERPW_WRONG_ERROR2
					+ " [actual]"
					+ driver.findElement(By.xpath("//label[@for='newPassword']//span[@class='error-msg']"))
							.getText();
		}
		
		if (!driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']")).getText()
				.contentEquals(CommonValues.USERPW_WRONG_ERROR2)) {
			failMsg = failMsg + "\n confirm PW : error msg(same value with Email) [expected]" + CommonValues.USERPW_WRONG_ERROR2
					+ " [actual]"
					+ driver.findElement(By.xpath("//label[@for='confirmNewPassword']//span[@class='error-msg']"))
							.getText();
		}
		
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 37. 현재 비밀번호와 동일한 비밀번호로 저장 시도
	@Test(priority=37)
	public void changePW_samePW() throws Exception 
	{
		String failMsg = "";

		// valid current pw
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_OLD_PW)));
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);

		// vaild new pw
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NEW_PW)));
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.USERPW);
		
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.USERPW);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);

		// 에러 메세지 변경 필요
		if(isElementPresent(By.xpath("//div[@class='modal-body']"))) {
			if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.PW_NEW_SAME_ERROR_MSG)) {
				failMsg = "same old pw [expected]" + CommonValues.PW_NEW_SAME_ERROR_MSG + " [actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
			}
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		} else {
			failMsg = failMsg + "cannot find error popup";
		}
		
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	// 38. 비밀번호 변경화면 cancel
	@Test(priority=38)
	public void changePW_cancel() throws Exception 
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//a[@class='btn btn-cancel btn-xl  ']")));
		driver.findElement(By.xpath("//a[@class='btn btn-cancel btn-xl  ']")).click();
		
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/settings/profile")){
			Exception e =  new Exception("cancel pw change : " + driver.getCurrentUrl());
	    	throw e;
		}
	}
	
	// 39. 정상 비밀번호 변경 후 - 로그인 시도
	@Test(priority=39)
	public void changePW_validPW() throws Exception 
	{
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contains("/account/password-change")) {
			driver.findElement(By.xpath("//button[@class='profile__basic__find-pwd-btn']")).click();
			Thread.sleep(500);
		}

		// valid current pw
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		//clearAttributeValue(driver.findElement(By.id("currentPassword")));
		Thread.sleep(100);
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.USERPW);
		//System.out.println("*********get PW : " + driver.findElement(By.id("currentPassword")).getAttribute("value"));

		// vaild new pw
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();

		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.VALID_NEW_PW);
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")));
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);

		if(isElementPresent(By.xpath("//div[@class='modal-body']"))) {
			if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.PW_CHANGE_SUCCESS_MSG)) {
				failMsg = "current pw error msg [expected]" + CommonValues.PW_CHANGE_SUCCESS_MSG + " [actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
			}
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		} else {
			failMsg = failMsg + "cannot find error popup";
		}
		
		Thread.sleep(1000);
		//change pw again(roll back)
		driver.findElement(By.xpath(XPATH_OLD_PW)).click();
		driver.findElement(By.xpath(XPATH_OLD_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_OLD_PW)));
		driver.findElement(By.xpath(XPATH_OLD_PW)).sendKeys(CommonValues.VALID_NEW_PW);

		// vaild new pw
		driver.findElement(By.xpath(XPATH_NEW_PW)).click();
		driver.findElement(By.xpath(XPATH_NEW_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_NEW_PW)));
		driver.findElement(By.xpath(XPATH_NEW_PW)).sendKeys(CommonValues.USERPW);

		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).click();
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).clear();
		clearAttributeValue(driver.findElement(By.xpath(XPATH_CONFIRM_PW)));
		driver.findElement(By.xpath(XPATH_CONFIRM_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-xl ']")).click();
		Thread.sleep(1000);

		if (isElementPresent(By.xpath("//div[@class='modal-body']"))) {
			if (!driver.findElement(By.xpath("//div[@class='modal-body']")).getText()
					.contentEquals(CommonValues.PW_CHANGE_SUCCESS_MSG)) {
				failMsg = "current pw error msg [expected]" + CommonValues.PW_CHANGE_SUCCESS_MSG + " [actual]"
						+ driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
			}
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		} else {
			failMsg = failMsg + "cannot find error popup";
		}
				
		if(failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
		
	}


	public void moveMyProfile() throws Exception
	{
		String myprofile = CommonValues.SERVER_URL + "/settings/profile";
		
		if(driver.getCurrentUrl().contentEquals(myprofile)) return;
		
		driver.findElement(By.id("profile-drop-down")).click();
		
		//must be fix!!
		driver.findElement(By.linkText("My information")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    if( !myprofile.equalsIgnoreCase(driver.getCurrentUrl()) || driver.findElement(By.id("email")).isEnabled())
	    {
	    	driver.get(myprofile);
	    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    	
	    	Exception e =  new Exception("no profile view");
	    	throw e;
	    }
	    
	    if (driver.findElement(By.id("email")).isEnabled()) 
	    {
	    	Exception e =  new Exception("email field is enable");
	    	throw e;
	    }
	    
	    
	}
	
	public void clearAttributeValue(WebElement el) 
	{
		while(!el.getAttribute("value").isEmpty())
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
