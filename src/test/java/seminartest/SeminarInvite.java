package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SeminarInvite {
	
	public static String XPATH_INVITE_EMAIL_INPUT = "//div[@class='inviteAttendeeToEmail__add__input']/input";
	public static String XPATH_INVITE_EMAIL_ADD_BTN = "//div[@class='inviteAttendeeToEmail__add__input']/button";
	public static String XPATH_INVITE_EMAIL_SEND_BTN = "//div[@class='InviteAttendeeToEmail_inviteAttendeeToEmail__buttons__1SJa5']/button";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarID = "/deb91299-8f67-4c79-8a48-c95aace6e48e";
	
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
		comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
	  }	

	
	@Test (priority=1)
	public void seminarInvitationView() throws Exception {
		String failMsg = "";
		
	
		//create seminar after 10minute
		String seminarTitle = Thread.currentThread().getStackTrace()[1].getMethodName();
		seminarID = createSeminar(seminarTitle, "3");
		
		//click seminar invitation list
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='wrap-button']/a[1]")));
		driver.findElement(By.xpath("//div[@class='wrap-button']/a[1]")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			failMsg = "invitation view : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=2)
	public void seminarInvitation_title() throws Exception {
		String failMsg = "";
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		driver.findElement(By.xpath("//h2[@class='invite_invite-title__1V7Az']//span")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		String detail = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if (!detail.equalsIgnoreCase(driver.getCurrentUrl())) {
			failMsg = "detail view : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test (priority=3)
	public void seminarInvitation_valid1() throws Exception {
		String failMsg = "";
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//placeholder
		if(!driver.findElement(By.id("addEmailInput")).getAttribute("placeholder").contentEquals("E-mail")) {
			failMsg = "1. inputbox placeholder. [Actual]" + driver.findElement(By.id("addEmailInput")).getAttribute("placeholder");
		}
		
		//add 1
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		List<WebElement> persons = driver.findElements(By.id("test"));
		
		//added item
		if(!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 2. add 1 : selected persons [Actual]" + driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		
		if((persons.size() != 1) || !persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText().contentEquals("abc@rsupport.com")) {
			failMsg = failMsg + "\n 3. add 1 : added list [Actual]" + persons.size() + ", " + persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText();
		}
		
		//add clipboard data(vaild 10 email)
		String ctc = CommonValues.CHANNEL_INVITE_CLIPBOARD_EMAIL;
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
		driver.findElement(By.id("addEmailInput")).clear();
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			driver.findElement(By.id("addEmailInput")).sendKeys(ctc);
		else
			driver.findElement(By.id("addEmailInput")).sendKeys(Keys.CONTROL, "v");
		
		driver.findElement(By.id("addEmailInput")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		persons = driver.findElements(By.id("test"));
		
		//added item
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("11 people")) {
			failMsg = failMsg + "\n 4. add 10 : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}

		if (persons.size() != 11) {
			failMsg = failMsg + "\n 5. add 10 : added list [Actual]" + persons.size();
		}

		//delete 1
		persons.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(100);
		
		persons = driver.findElements(By.id("test"));
		
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("10 people")) {
			failMsg = failMsg + "\n 6. delete 1 : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (persons.size() != 10) {
			failMsg = failMsg + "\n 7. delete 1 : added list [Actual]" + persons.size();
		}
		
		//delete all - cancel
		driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
		Thread.sleep(500);
		persons = driver.findElements(By.id("test"));
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("10 people")) {
			failMsg = failMsg + "\n 8. delete cancel : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (persons.size() != 10) {
			failMsg = failMsg + "\n 9. delete cancel : added list [Actual]" + persons.size();
		}
		
		//delete all - confirm
		driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
	
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("0 people")) {
			failMsg = failMsg + "\n 10. delete all : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (isElementPresent(By.id("test"))) {
			failMsg = failMsg + "\n 11. delete all : added list [Actual]" + persons.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=4)
	public void seminarInvitation_invalid() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//add 1
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		List<WebElement> persons = driver.findElements(By.id("test"));

		// added item
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 2. add 1 : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}

		if ((persons.size() != 1) || !persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText()
				.contentEquals("abc@rsupport.com")) {
			failMsg = failMsg + "\n 3. add 1 : added list [Actual]" + persons.size() + ", "
					+ persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText();
		}
		
		//add empty
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 4. add empty : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (!driver.findElement(By.xpath("//span[@class='error-msg']")).getText().contentEquals(CommonValues.INVITE_ERROR_EMAIL)) {
			failMsg = failMsg + "\n 5. add empty : error msg [Expected]" +CommonValues.INVITE_ERROR_EMAIL + "[Actual]"
					+ driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		//add invalid email
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abcdefg");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 6. add invalid email : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (!driver.findElement(By.xpath("//span[@class='error-msg']")).getText()
				.contentEquals(CommonValues.INVITE_ERROR_EMAIL)) {
			failMsg = failMsg + "\n 7. add invalid email : error msg [Actual]"
					+ driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		//add duplicated email
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("1 people")) {
			failMsg = failMsg + "\n 8. add duplicated email : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}
		if (!driver.findElement(By.xpath("//span[@class='error-msg']")).getText()
				.contentEquals(CommonValues.INVITE_ERROR_EMAIL_DUPLICATE)) {
			failMsg = failMsg + "\n 9. add duplicated email : error msg [Actual]"
					+ driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		if(isElementPresent(By.xpath("//div[@class='modal-footer']/button[1]")))
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test (priority=5)
	public void seminarInvitation_invalid2() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=10)
	public void seminarInvitation_sendEmpty() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		if(driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).isEnabled()) {
			//delete all - confirm
			driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(500);
		}
		
		//click send
		driver.findElement(By.xpath("//div[@class='email-send-type']/button[1]")).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.INVITE_ERROR_EMPTYLIST))
			failMsg = "\n 1. error email box msg [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[1]")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=11)
	public void seminarInvitation_send() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		if (driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).isEnabled()) {
			// delete all - confirm
			driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		}
		
		
		//add 1
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		
		//add clipboard 2
		String ctc = "sinhyekim@rsupport.com rsrsup1@gmail.com";
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		driver.findElement(By.id("addEmailInput")).clear();
		
		if(System.getProperty("os.name").toLowerCase().contains("mac"))
			driver.findElement(By.id("addEmailInput")).sendKeys(ctc);
		else
			driver.findElement(By.id("addEmailInput")).sendKeys(Keys.CONTROL, "v");
		
		driver.findElement(By.id("addEmailInput")).sendKeys(Keys.ENTER);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(500);
		
		List<WebElement> persons = driver.findElements(By.id("test"));

		// added item
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("3 people")) {
			failMsg = failMsg + "\n 1. add 3 : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}

		if ((persons.size() != 3) ) {
			failMsg = failMsg + "\n 2. add 3 : added list [Actual]" + persons.size() + ", "
					+ persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText();
		}
		
		//click send..
		
		//driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		WebElement element = driver.findElement(By.xpath("//div[@class='email-send-type']/button"));
		//driver.findElement(By.xpath("//div[@class='email-send-type']/button")).sendKeys(Keys.ENTER);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();",element );
		Thread.sleep(500);
		
		//msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.INVITE_MSG_SEND))
			failMsg = "\n 1. send email box msg [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		
		//click cancel
		driver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[2]")).click();
		
		if(!driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText().contentEquals("0"))
			failMsg = failMsg + "\n 3. send cancel : total mail count [Actual]" + driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText();
		
		if(!driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText().contentEquals("Invite participants"))
			failMsg = failMsg + "\n 4. send list tab [Actual]" + driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	@Test (priority=12)
	public void seminarInvitation_send2() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		//clear list
		if (driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).isEnabled()) {
			// delete all - confirm
			driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		}
		
		
		//add 1
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys("abc@rsupport.com");
		driver.findElement(By.xpath("//div[@class='InviteAttendeeSearchEmail_inviteSearchEmail__1KfzB']/button")).click();
		Thread.sleep(100);
		
		//add clipboard 1
		String ctc = "sinhyekim@rsupport.com rsrsup1@gmail.com";
		StringSelection stringSelection = new StringSelection(ctc);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		driver.findElement(By.id("addEmailInput")).clear();
		driver.findElement(By.id("addEmailInput")).sendKeys(Keys.CONTROL, "v");
		driver.findElement(By.id("addEmailInput")).sendKeys(Keys.ENTER);

		Thread.sleep(500);
		
		List<WebElement> persons = driver.findElements(By.id("test"));

		// added item
		if (!driver.findElement(By.xpath("//span[@class='selected-persons']")).getText().contentEquals("3 people")) {
			failMsg = failMsg + "\n 1. add 3 : selected persons [Actual]"
					+ driver.findElement(By.xpath("//span[@class='selected-persons']")).getText();
		}

		if ((persons.size() != 3) ) {
			failMsg = failMsg + "\n 2. add 3 : added list [Actual]" + persons.size() + ", "
					+ persons.get(0).findElement(By.xpath(".//span[@class='email']")).getText();
		}
		
		//click send...
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-l ']")).click();
		Thread.sleep(500);
		
		//msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.INVITE_MSG_SEND))
			failMsg = "\n 1. send email box msg [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='invite-modal-footer']/button[1]")).click();
		
		if(!driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText().contentEquals("3"))
			failMsg = failMsg + "\n 3. send 3 : total mail count [Actual]" + driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText();
		
		if(!driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText().contentEquals("Send out list"))
			failMsg = failMsg + "\n 4. send list tab [Actual]" + driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=13)
	public void seminarInvitation_list() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		if(!driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText().contentEquals("Send out list")) {
			driver.findElement(By.xpath("//button[@class='invite-tab__button']")).click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
		
		List<WebElement> emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		
		//sent email : 3
		if(!driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText().contentEquals("3"))
			failMsg = failMsg + "\n 1. add 3 : total mail count [Actual]" + driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText();
		
		//emaillist : title 1 + email...
		if(emaillist.size() != 4)
			failMsg = failMsg + "\n 2. add 3 : list count [Actual]" + emaillist.size();
			
		//search empty
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//button[@class='search-btn']")).click();

		emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		if (emaillist.size() != 4)
			failMsg = failMsg + "\n 3. search empty : list count [Expected]4, [Actual]" + emaillist.size();
		
		//search invalid
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("invalid");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		if (emaillist.size() != 1)
			failMsg = failMsg + "\n 4. search invalid keyworkd : [Expected]0, list count [Actual]" + emaillist.size();
		
		//search valid
		driver.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys("sinhyekim");
		driver.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		if (emaillist.size() != 2) {
			failMsg = failMsg + "\n 5. search valid keyworkd : list count [Expected]2, [Actual]" + emaillist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=14)
	public void seminarInvitation_resend_Empty() throws Exception {
		String failMsg = "";
		
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		if(!driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText().contentEquals("Send out list")) {
			driver.findElement(By.xpath("//button[@class='invite-tab__button']")).click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
		
		//click resend
		driver.findElement(By.xpath("//div[@class='invite-list__button-box']/button[1]")).click();
		Thread.sleep(500);
		
		//dialog msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.MSG_SELCECT_EMAIL))
			failMsg = "\n 1. popup msg [Expected]" + CommonValues.MSG_SELCECT_EMAIL 
			+ "[Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=15)
	public void seminarInvitation_resend() throws Exception {
		String failMsg = "";
		
		driver.navigate().refresh();
		String invitation = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;
		if (!invitation.equalsIgnoreCase(driver.getCurrentUrl())) {
			driver.get(invitation);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		Thread.sleep(1000);
		if(!driver.findElement(By.xpath("//button[@class='invite-tab__button active']")).getText().contentEquals("Send out list")) {
			driver.findElement(By.xpath("//button[@class='invite-tab__button']")).click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
		
		List<WebElement> emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		
		for(int i = 0 ; i < emaillist.size() ; i ++) {
			if(emaillist.get(i).findElement(By.xpath(".//span[2]")).getText().contentEquals("sinhyekim@rsupport.com")) {
				emaillist.get(i).findElement(By.xpath("./div[@class='checkbox']")).click();
				
				break;
			}
		}
		
		//click resend (resend 1)
		driver.findElement(By.xpath("//div[@class='invite-list__button-box']/button[1]")).click();
		Thread.sleep(500);
		
		//dialog msg
		if(!driver.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(CommonValues.MSG_SEND_EMAIL))
			failMsg = "\n 1. popup msg [Expected]" + CommonValues.MSG_SEND_EMAIL 
			+ "[Actual]" + driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']")).click();
		Thread.sleep(500);
		
		//total 3+1
		if(!driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText().contentEquals("4"))
			failMsg = failMsg + "\n 2. add 3 : total mail count [Actual]" + driver.findElement(By.xpath("//p[@class='invite-status__item__value total']/span[1]")).getText();
		
		
		emaillist = driver.findElements(By.xpath("//li[@class='invite-list__item']"));
		if (emaillist.size() != 5) {
			failMsg = failMsg + "\n 3. search valid keyworkd : list count [Expected]5, [Actual]" + emaillist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test (priority=20, enabled=true)
	public void seminarInvitation_fin() throws Exception {
		String detail = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detail);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		// delete seminar
		driver.findElement(By.xpath("//div[@class='ricon ricon-trash']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
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

	public String createSeminar(String seminarName, String attendees) throws Exception {

		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		// check seminar type
		driver.findElement(By.id("seminar-type")).click();

		driver.findElement(By.xpath("//div[@class='box-option open']/div[1]")).click();

		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).sendKeys(seminarName);

		// set attendees
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).click();
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//div[@class='count-attendants']/input[1]")).sendKeys(attendees);
		Thread.sleep(500);
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver, seminarName, false);

		Thread.sleep(500);
		// save seminar
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[1]")).click();
		Thread.sleep(1000);
		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;

		String seminarUri = driver.getCurrentUrl().replace(createViewUri, "");
		System.out.println(" currentURL : " + driver.getCurrentUrl());
		System.out.println(" seminarUri : " + seminarUri);
		
		// post seminar
		driver.findElement(By.xpath("//div[@class='CommandButtons_commandButtons__1sy0n']/button[2]")).click();
		Thread.sleep(1000);

		// post popup : click post
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(3000);

		/*
		String testurl = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI;
		String seminarUri = driver.getCurrentUrl().replace(testurl, "");
		System.out.println(" currentURL : " + driver.getCurrentUrl());
		System.out.println(" seminarUri : " + seminarUri);
		*/
		String compleateUri = CommonValues.SERVER_URL + CommonValues.CREATE_SUCCESS_URI + seminarUri;
		if (!compleateUri.equalsIgnoreCase(driver.getCurrentUrl())) {
			Exception e = new Exception("fail to create seminar :[Actual]" + driver.getCurrentUrl() + " [Expected]" + compleateUri);
			throw e;
		}
		return seminarUri;
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
