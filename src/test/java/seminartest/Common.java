package seminartest;

import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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

/* Common
 * 1. 헤더 UI,text 확인 
 * 2. 헤더 로고  url 이동 동작 확인
 * 3. 브라우저 언어 체크(한국어)
 * 4. 브라우저 언어 체크(일어)
 * 5. 브라우저 언어 체크(영어)
 * 6. 브라우저 하단 언어 변경
 */

public class Common {
	public static WebDriver driver;
	public static WebDriver enDriver;
	public static WebDriver jpDriver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR");
		enDriver = comm.setDriver(driver, browsertype, "lang=en_US");
		jpDriver = comm.setDriver(driver, browsertype, "lang=ja_JP");
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", enDriver);
		context.setAttribute("webDriver3", jpDriver);
		
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");
	}
	
	//1.헤더UI,text 확인
	@Test(priority=1)
	  public void checkHeaderUI() throws Exception {
		
		WebElement HeaderLogoPresent = driver.findElement(By.xpath("//img[@class='logo-image big']"));
		if(!HeaderLogoPresent.isDisplayed()) {
			Exception e = new Exception("Header Logo not Present");
			throw e;
		}
		
		WebElement LoginBtn = driver.findElement(By.xpath("//div[@class='l-right']/ul/li[1]"));
		String LoginBtn_innerText = LoginBtn.getText();
		
		if(!LoginBtn_innerText.contentEquals("로그인")) {
			Exception e = new Exception("Header Text fail :" + LoginBtn_innerText);
			throw e;
		}
		
		WebElement SignupBtn = driver.findElement(By.xpath("//div[@class='l-right']/ul/li[2]"));
		String SignupBtn_innerText = SignupBtn.getText();
		
		if(!SignupBtn_innerText.contentEquals("회원가입")) {
			Exception e = new Exception("Header Text fail :" + SignupBtn_innerText);
			throw e;
		}
		
	}
	//2.헤더 로고  url 이동 동작 확인 
	@Test(priority=2)
	  public void checkHeaderLogo() throws Exception {
		
		WebElement SignupBtn = driver.findElement(By.xpath("//div[@class='l-right']/ul/li[2]"));
		SignupBtn.click();
		String signupurl = CommonValues.SERVER_URL + CommonValues.SIGNUP_URI;
		Thread.sleep(500);
		
		if (!signupurl.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception(" signup Enter fail : " + driver.getCurrentUrl());
			throw e;
		}
		
		WebElement HeaderLogo = driver.findElement(By.xpath("//img[@class='logo-image big']"));
		HeaderLogo.click();
		String serverurl = CommonValues.SERVER_URL;
		Thread.sleep(500);
		if (!serverurl.equalsIgnoreCase(driver.getCurrentUrl()) && !(CommonValues.SERVER_URL + "/login").equalsIgnoreCase(driver.getCurrentUrl()) ) {
			Exception e = new Exception(" headerlogo click fail before login : " + driver.getCurrentUrl());
			throw e;
		}
		//login
		driver.findElement(By.xpath("//input[@name='email']")).clear();
	    driver.findElement(By.xpath("//input[@name='email']")).sendKeys(CommonValues.USEREMAIL);
	    driver.findElement(By.xpath("//input[@name='password']")).clear();
	    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(CommonValues.USERPW);
	    
	   
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		String listview = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		String introview = CommonValues.SERVER_URL + CommonValues.INTRO_URI;
		Thread.sleep(500);
		
		if (!driver.getCurrentUrl().equalsIgnoreCase(listview) && !driver.getCurrentUrl().equalsIgnoreCase(introview)) {
			Exception e = new Exception("login fail : " + driver.getCurrentUrl());
			throw e;
		}
		
        WebElement ChannelBtn = driver.findElement(By.linkText("채널"));
		//WebElement ChannelBtn = driver.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']"));
		ChannelBtn.click();
		String channelview = CommonValues.SERVER_URL + CommonValues.CHANNEL_URI;
		Thread.sleep(500);
		
		if (!channelview.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("channel Enter fail : " + driver.getCurrentUrl());
			throw e;
		}
		
		
		WebElement LoginHeaderLogo = driver.findElement(By.xpath("//img[@class='logo-image big']"));
		LoginHeaderLogo.click();
		Thread.sleep(500);
		
		if (!listview.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("Headerlogo click fail after login : " + driver.getCurrentUrl());
			throw e;
		}
		//logout
		driver.findElement(By.id("profile-drop-down")).click();
		driver.findElement(By.linkText("로그아웃")).click();
		
		Thread.sleep(500);
		if (!CommonValues.SERVER_URL.equalsIgnoreCase(driver.getCurrentUrl()) && !(CommonValues.SERVER_URL + "/").equalsIgnoreCase(driver.getCurrentUrl()) ) {
			Exception e = new Exception("logout fail : " + driver.getCurrentUrl());
			throw e;
		}
		
	}
	//3.브라우저 언어 체크(한국어)
	@Test(priority=3)
	  public void checkKRlanguage() throws Exception {
		
		driver.get(CommonValues.SERVER_URL);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']")));
		
		if(!driver.findElement(By.xpath("//p[@class='login__desc']")).getText().contentEquals("리모트 세미나에서 수준 높은 온라인 세미나를 개최하세요.")) {
			Exception e = new Exception("KR language fail :" + driver.findElement(By.xpath("//p[@class='login__desc']")).getText());
			throw e;
		}
		
		
	}
	
	//4.브라우저 언어 체크(일어)
	@Test(priority=4)
	  public void checkJPlanguage() throws Exception {
			
		jpDriver.get(CommonValues.SERVER_URL);
		WebDriverWait wait = new WebDriverWait(jpDriver, 50);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']")));
		
		if(!jpDriver.findElement(By.xpath("//p[@class='login__desc']")).getText().contentEquals("RemoteSeminarでレベルの高いオンラインセミナーを開催してみましょう。")) {
			Exception e = new Exception("JP language fail :" + jpDriver.findElement(By.xpath("//p[@class='login__desc']")).getText());
			throw e;
		}
	}
		
	//5.브라우저 언어 체크(영어)	
	@Test(priority=5)
	  public void checkENlanguage() throws Exception {
		
		enDriver.get(CommonValues.SERVER_URL);
		WebDriverWait wait = new WebDriverWait(enDriver, 50);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']")));
		
		if(!enDriver.findElement(By.xpath("//p[@class='login__desc']")).getText().contentEquals("Host high-quality online seminars with RemoteSeminars.")) {
			Exception e = new Exception("EN language fail :" + enDriver.findElement(By.xpath("//p[@class='login__desc']")).getText());
			throw e;
		
		}
	}
	
	//6.브라우저 하단 언어 변경
	@Test(priority=6)
		public void checkChangelanguage() throws Exception {
		
		driver.get(CommonValues.SERVER_URL);
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']")));
		
		if(!driver.findElement(By.xpath("//p[@class='login__desc']")).getText().contentEquals("리모트 세미나에서 수준 높은 온라인 세미나를 개최하세요.")) {
			Exception e = new Exception("KR language fail :" + driver.findElement(By.xpath("//p[@class='login__desc']")).getText());
			throw e;
		}
		
		driver.findElement(By.id("lang")).click();
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//a[@href='/login']")), "Sign in"));
		
		
		if(!driver.findElement(By.xpath("//p[@class='login__desc']")).getText().contentEquals("Host high-quality online seminars with RemoteSeminars.")) {
			Exception e = new Exception("EN language fail :" + driver.findElement(By.xpath("//p[@class='login__desc']")).getText());
			throw e;
		}
		
		
	}
	
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {

	    driver.quit();
	    enDriver.quit();
	    jpDriver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
}
