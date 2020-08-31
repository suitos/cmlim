package admin;

import static org.testng.Assert.fail;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* LoginTest

 * 
 */

public class LoginTest {
	
	public static String PLACEHOLDER_EMAIL = "이메일 (example@gmail.com)";
	public static String PLACEHOLDER_PW = "비밀번호 (8자이상)";
	public static String WARNING_EMAIL_EMPTY = "이메일을 입력하세요.";
	public static String WARNING_PW_EMPTY = "비밀번호를 입력하세요.";
	public static String WARNING_LOGOIN_INVALID_FORMAT = "이메일 형식이 올바르지 않습니다.";
	public static String WARNING_LOGOIN_FAIL = "로그인을 실패하였습니다.";
	public static String WARNING_LOGOIN_FAIL2 = "로그인이 불가능한 계정입니다.";

	public static String XPATH_EMAIL_ERROR = "//div[@class='ant-row ant-form-item ant-form-item-with-help ant-form-item-has-error'][1]//div[@class='ant-form-item-explain']/div";
	public static String XPATH_PW_ERROR = "//div[@class='ant-row ant-form-item ant-form-item-with-help ant-form-item-has-error'][2]//div[@class='ant-form-item-explain']/div";
	public static String XPATH_LOGIN_TOAST = "//div[@class='ant-message-custom-content ant-message-error']/span";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		//driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR");

		context.setAttribute("webDriver", driver);
		driver.get(CommonValues.ADMIN_URL);

		System.out.println("End BeforeTest!!!");
	}

	// 1. placeholder확인. 빈채로 로그인 시도
	@Test(priority = 1, enabled = true)
	public void login_empty() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).getAttribute("placeholder").contentEquals(PLACEHOLDER_EMAIL)) {
			failMsg = "1. Email placeholder [Expected]" + PLACEHOLDER_EMAIL 
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).getAttribute("placeholder").contentEquals(PLACEHOLDER_PW)) {
			failMsg = failMsg + "\n2. Email placeholder [Expected]" + PLACEHOLDER_PW 
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).getAttribute("placeholder");
		}
		
		//click login		
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_BUTTON)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText().contentEquals(WARNING_EMAIL_EMPTY)) {
			failMsg = failMsg + "\n3. Email error : empty [Expected]" + WARNING_EMAIL_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_EMAIL_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_PW_ERROR)).getText().contentEquals(WARNING_PW_EMPTY)) {
			failMsg = failMsg + "\n4. Password error : empty [Expected]" + WARNING_PW_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PW_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 2. 로그인 실패.
	@Test(priority = 2, enabled = true)
	public void login_invalid() throws Exception {
		String failMsg = "";

		CommonValues comm = new CommonValues();
		
		// invalid format
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys("test");
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys("test");

		// click login
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_BUTTON)).click();
		Thread.sleep(500);

		if (isElementPresent(By.xpath(XPATH_LOGIN_TOAST))) {
			if (!driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText()
					.contentEquals(WARNING_LOGOIN_INVALID_FORMAT)) {
				failMsg = "\n1. login fail message error (invalid format). [Expected]" + WARNING_LOGOIN_INVALID_FORMAT
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText();
			}
		} else {
			failMsg = "1-0. cannot find error toast";
		}
		Thread.sleep(3000);

		// wrong user email
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys("test@test.com");
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys("test");

		// click login
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_BUTTON)).click();
		Thread.sleep(500);

		if (isElementPresent(By.xpath(XPATH_LOGIN_TOAST))) {
			if (!driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText().contentEquals(WARNING_LOGOIN_FAIL)) {
				failMsg = "\n2. login fail message error (wrong email). [Expected]" + WARNING_LOGOIN_FAIL + " [Actual]"
						+ driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText();
			}
		} else {
			failMsg = "\n2-0. cannot find error toast";
		}
		Thread.sleep(3000);

		// valid email, wrong pw
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys(CommonValues.USER_PARTNER_KR);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys("test");

		// click login
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_BUTTON)).click();
		Thread.sleep(500);

		if (isElementPresent(By.xpath(XPATH_LOGIN_TOAST))) {
			if (!driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText().contentEquals(WARNING_LOGOIN_FAIL)) {
				failMsg = "\n3. login fail message error (wrong email). [Expected]" + WARNING_LOGOIN_FAIL + " [Actual]"
						+ driver.findElement(By.xpath(XPATH_LOGIN_TOAST)).getText();
			}
		} else {
			failMsg = "\n3-0. cannot find error toast";
		}

		Thread.sleep(3000);

		// seminar user
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_EMAIL)).sendKeys("rsrsup1@gmail.com");
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).clear();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)));
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_PW)).sendKeys("!Rsupport0");

		// click login
		driver.findElement(By.xpath(CommonValues.XPATH_LOGIN_BUTTON)).click();
		Thread.sleep(1000);

		String xpath = XPATH_LOGIN_TOAST + "[2]";
		if (isElementPresent(By.xpath(xpath))) {
			if (!driver.findElement(By.xpath(xpath)).getText().contentEquals(WARNING_LOGOIN_FAIL2)) {
				failMsg = "\n4. login fail message error (wrong email). [Expected]" + WARNING_LOGOIN_FAIL2 + " [Actual]"
						+ driver.findElement(By.xpath(xpath)).getText();
			}
		} else {
			failMsg = "\n4-0. cannot find error toast";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 3. 로그인  (sysadmin, 지사관리자, 일반관리자)
	@Test(priority = 3, enabled = true)
	public void login() throws Exception {
		String failMsg = "";	
		
		//sysadmin
		CommonValues comm = new CommonValues();
		comm.logintadmin(driver, CommonValues.USER_SYSADMIN);
		
		String ret = logout();
		failMsg = failMsg + (ret==""?"" : "\n1." + ret);
		
		//kr
		comm.logintadmin(driver, CommonValues.USER_PARTNER_KR);

		ret = logout();
		failMsg = failMsg + (ret == "" ? "" : "\n2." + ret);
		
		//normal
		comm.logintadmin(driver, CommonValues.USER_PARTNER_1);

		ret = logout();
		failMsg = failMsg + (ret == "" ? "" : "\n3." + ret);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	private String logout() throws InterruptedException {
		
		driver.findElement(By.xpath(CommonValues.XPATH_USER_PROFILE_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//ul[@class='ant-dropdown-menu ant-dropdown-menu-light ant-dropdown-menu-root ant-dropdown-menu-vertical']/li[4]")).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(CommonValues.XPATH_LOGIN_EMAIL))) {
			return "logout fail";
		}
		
		return "";
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
