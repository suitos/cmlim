package admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.List;
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
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* Users2
 * 10. 유저 파트너 변경(변경후 원복) : TestEUser
 * 11. 유저 채널 : rsrsup2
 * 12. 유저 채널 search : rsrsup2
 * 13. 유저 채널  등록 시도 : rsrsup2
 * 20. 유저 세미나 탭  : rsrsup2
 * 21. 유저 세미나 탭 search  : rsrsup2
 */

public class Users2 {

	public static String XPATH_POPUP_SUBMIT = "//div[@class='ant-modal-footer']/button[1]";
	public static String XPATH_POPUP_CANCEL = "//div[@class='ant-modal-footer']/button[2]";
	
	public static String XPATH_USER_TAB_CHANNEL = "//div[@class='ant-tabs-nav ant-tabs-nav-animated']//div[@role='tab'][4]";
	public static String XPATH_USER_TAB_SEMINAR = "//div[@class='ant-tabs-nav ant-tabs-nav-animated']//div[@role='tab'][5]";
	
	public static String XPATH_USER_INFO_LIST = "//tbody[@class='ant-table-tbody']/tr";
	public static String XPATH_USER_INFO_INPUTBOX = "//div[@class='ant-form-item-control-input']//input";
	public static String XPATH_USER_INFO_CAHNNELREG = "//div[@class='ant-col ant-col-8']/button[2]";
	public static String XPATH_USER_INFO_SEARCHEMPTY = "//section[@id='search-result-wrap']//p[@class='ant-empty-description']";
	public static String XPATH_USER_SEARCH_COUNT = "//span[@class='total']";
	public static String XPATH_CHANNEL_NEW_MASTER = "//input[@id='channelMaster']";
	
	public static String MSG_PLACEHOLDER_SEARCH_CHANNEL = "채널명을 입력하세요";
	public static String MSG_PLACEHOLDER_SEARCH_SEMINAR = "세미나 명을 입력하세요";
	public static String MSG_SEARCHEMPTY = "데이터가 없습니다.";
	public static String MSG_SEARCHCOUNT = "%d건";
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private String testUser = "rsrsup2";
	private String userID = "";

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
		comm.logintadmin(driver, CommonValues.USER_PARTNER_KR);
		
		System.out.println("End BeforeTest!!!");
	}

	// 10. 유저 파트너 변경(변경후 원복) : TestEUser
	@Test(priority = 10, enabled = false)
	public void user_changePartner() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Partners2.PARTNER_TESTEUSER);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(Partners2.PARTNER_TESTEUSER)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			failMsg = failMsg + "1. cannot find saved user in list. current url : " + driver.getCurrentUrl();
		} else {
			userID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO, "");
		}
		
		String originPa = driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER)).getText();
		
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_SELECTBOX)).click();
		Thread.sleep(500);
		
		WebElement tree = driver.findElement(By.xpath(Partners2.XPATH_PARTNER_SELECTTREE));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String item = String.format(Partners2.findItem, Partners2.PARTNER_TESTD);
		for(int i = 0 ; i < 10 ; i++) {
			if (isElementPresent(tree, By.xpath(item))) {
				break;	
			} else {
				List<WebElement> pas = driver.findElements(By.xpath(Partners2.XPATH_PARTNER_SELECTBOX_ITEM));
				js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
				Thread.sleep(100);
			}
		}
		tree.findElement(By.xpath(item)).click();
		
		Thread.sleep(100);
	
		driver.findElement(By.xpath(XPATH_POPUP_CANCEL)).click();
		Thread.sleep(100);
		
		if(!driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER)).getText().contentEquals(originPa)) {
			failMsg = failMsg + "\n2. partner. [Expected]" + originPa 
					+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
		}
		
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_SELECTBOX)).click();
		Thread.sleep(500);
		
		tree = driver.findElement(By.xpath(Partners2.XPATH_PARTNER_SELECTTREE));

		item = String.format(Partners2.findItem, Partners2.PARTNER_TESTD);
		for(int i = 0 ; i < 10 ; i++) {
			if (isElementPresent(tree, By.xpath(item))) {
				break;	
			} else {
				List<WebElement> pas = driver.findElements(By.xpath(Partners2.XPATH_PARTNER_SELECTBOX_ITEM));
				js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
				Thread.sleep(100);
			}
		}
		tree.findElement(By.xpath(item)).click();
		Thread.sleep(100);
	
		driver.findElement(By.xpath(XPATH_POPUP_SUBMIT)).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n3. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners2.MSG_PARTNER_CAHNGE_SUCCESS)) {
				failMsg = failMsg + "\n4. toast message. [Expected]" + Partners2.MSG_PARTNER_CAHNGE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		String selectedPa = originPa.replace(Partners2.PARTNER_TESTE, Partners2.PARTNER_TESTD);
		if(!driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER)).getText().contentEquals(selectedPa)) {
			failMsg = failMsg + "\n5. changed partner. [Expected]" + selectedPa 
					+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
		}
		
		//파트너사 원복
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(Users.XPATH_USER_INFO_PROFILE_PARTNER_SELECTBOX)).click();
		Thread.sleep(500);
		
		tree = driver.findElement(By.xpath(Partners2.XPATH_PARTNER_SELECTTREE));

		item = String.format(Partners2.findItem, Partners2.PARTNER_TESTE);
		for(int i = 0 ; i < 10 ; i++) {
			if (isElementPresent(tree, By.xpath(item))) {
				break;	
			} else {
				List<WebElement> pas = driver.findElements(By.xpath(Partners2.XPATH_PARTNER_SELECTBOX_ITEM));
				js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
				Thread.sleep(100);
			}
		}
		tree.findElement(By.xpath(item)).click();
		Thread.sleep(100);
	
		driver.findElement(By.xpath(XPATH_POPUP_SUBMIT)).click();
		Thread.sleep(500);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. 유저 채널 : rsrsup2
	@Test(priority = 11, enabled = true)
	public void user_channel() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(testUser);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(testUser)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			failMsg = failMsg + "1. cannot find user in list. current url : " + driver.getCurrentUrl();
		} else {
			userID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO, "");
			
			driver.findElement(By.xpath(XPATH_USER_TAB_CHANNEL)).click();
			Thread.sleep(500);
			
			List<WebElement> channels = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
			String channelname = "rsrsup1rsrsup2";
			for(int i = 0 ; i < channels.size() ; i++) {
				channelname = channelname.replace(channels.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText(), "");
			}
			
			if(!channelname.isEmpty()) {
				failMsg = failMsg + "\n2. cannot find user'channel. channel name : " + channelname;
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	// 12. 유저 채널 search : rsrsup2
	@Test(priority = 12,dependsOnMethods = {"user_channel"},  enabled = true)
	public void user_channel_search() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
		}
		
		if(driver.findElement(By.xpath(XPATH_USER_TAB_CHANNEL)).getAttribute("aria-selected").contentEquals("false")) {
			driver.findElement(By.xpath(XPATH_USER_TAB_CHANNEL)).click();
			Thread.sleep(500);
		}
		
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).getAttribute("placeholder").contentEquals(MSG_PLACEHOLDER_SEARCH_CHANNEL)) {
			failMsg = failMsg + "1. channel search inputbox placeholder. [Expected]" + MSG_PLACEHOLDER_SEARCH_CHANNEL
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).getAttribute("placeholder");
		}
		
		//channel name
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys("rsup2");
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		List<WebElement> channels = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
		
		if(channels.size() != 1) {
			failMsg = failMsg + "\n2. searched channel count. [Expected]" + "1"
					+ " [Actual]" + channels.size();
		} else {
			if(!channels.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(testUser)) {
				failMsg = failMsg + "\n3. searched channel name. [Expected]" + testUser
						+ " [Actual]" + channels.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			}
		}

		//invalid keyword
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)));
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys("rsup3");
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		channels = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
		
		if(channels.size() != 0) {
			failMsg = failMsg + "\n4. searched channel row count. [Expected]" + "0"
					+ " [Actual]" + channels.size();
		} else {
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_SEARCHEMPTY)).getText().contentEquals(MSG_SEARCHEMPTY)) {
				failMsg = failMsg + "\n5. search empty message. [Expected]" +MSG_SEARCHEMPTY 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_SEARCHEMPTY)).getText();
			}
			
			String searchCount = String.format(MSG_SEARCHCOUNT, 0);
			if(!driver.findElement(By.xpath(XPATH_USER_SEARCH_COUNT)).getText().contentEquals(searchCount)) {
				failMsg = failMsg + "\n6. search count. [Expected]" + searchCount 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_SEARCH_COUNT)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 13. 유저 채널  등록 시도 : rsrsup2
	@Test(priority = 13, dependsOnMethods = {"user_channel"},  enabled = true)
	public void user_channel_createChannel() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
		}
		
		if(driver.findElement(By.xpath(XPATH_USER_TAB_CHANNEL)).getAttribute("aria-selected").contentEquals("false")) {
			driver.findElement(By.xpath(XPATH_USER_TAB_CHANNEL)).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_CAHNNELREG)).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELNEW)) {
			failMsg = "1. not channel new url. current url : " + driver.getCurrentUrl();
		} else {
			if(!driver.findElement(By.xpath(XPATH_CHANNEL_NEW_MASTER)).getAttribute("value").contentEquals(testUser + "@gmail.com")) {
				failMsg = failMsg + "\n2. channel master in new channelview. [Expected]" + (testUser + "@gmail.com")
						 + " [Actual]" + driver.findElement(By.xpath(XPATH_CHANNEL_NEW_MASTER)).getAttribute("value");
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 20. 유저 세미나 탭  : rsrsup2
	@Test(priority = 20, enabled = true)
	public void user_seminar() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
		}
		
		driver.findElement(By.xpath(XPATH_USER_TAB_SEMINAR)).click();
		Thread.sleep(500);
		
		List<WebElement> seminars = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
		
		//list defaout rows : 30rows
		if (seminars.size() != 30) {
			failMsg = failMsg + "1. seminar count error. [Expected]" + "30" + " [Actual]" + seminars.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 21. 유저 세미나 탭 search  : rsrsup2
	@Test(priority = 21, enabled = true)
	public void user_seminar_search() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
		}
		
		if(driver.findElement(By.xpath(XPATH_USER_TAB_SEMINAR)).getAttribute("aria-selected").contentEquals("false")) {
			driver.findElement(By.xpath(XPATH_USER_TAB_SEMINAR)).click();
			Thread.sleep(500);
		}
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).getAttribute("placeholder").contentEquals(MSG_PLACEHOLDER_SEARCH_SEMINAR)) {
			failMsg = failMsg + "1. seminar search inputbox placeholder. [Expected]" + MSG_PLACEHOLDER_SEARCH_SEMINAR
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).getAttribute("placeholder");
		}
		
		// seminar name
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)));
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(testUser);
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		List<WebElement> seminars = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
		
		if (seminars.size() == 0) {
			failMsg = failMsg + "\n2. seminar search count error. [Expected]" + "more than 1" + " [Actual]"
					+ seminars.size();
		} else {
			if(!seminars.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText().contains(testUser)) {
				failMsg = failMsg + "\n3. searched seminar name. [Expected]contains:" + testUser
						+ " [Actual]" + seminars.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
			}
		}
		
		//invalid keyworkd
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)));
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys("invalid keyword");
		driver.findElement(By.xpath(XPATH_USER_INFO_INPUTBOX)).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		seminars = driver.findElements(By.xpath(XPATH_USER_INFO_LIST));
		
		if (seminars.size() != 0) {
			failMsg = failMsg + "\n4. seminar search count error. [Expected]" + "0" + " [Actual]"
					+ seminars.size();
		} else {
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_SEARCHEMPTY)).getText().contentEquals(MSG_SEARCHEMPTY)) {
				failMsg = failMsg + "\n5. search empty message. [Expected]" +MSG_SEARCHEMPTY 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_SEARCHEMPTY)).getText();
			}
			
			String searchCount = String.format(MSG_SEARCHCOUNT, 0);
			if(!driver.findElement(By.xpath(XPATH_USER_SEARCH_COUNT)).getText().contentEquals(searchCount)) {
				failMsg = failMsg + "\n6. search count. [Expected]" + searchCount 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_SEARCH_COUNT)).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST)) {
			wd.get(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST);
			
			CommonValues comm = new CommonValues();
			comm.setCalender(wd);
			
			Thread.sleep(500);
		}
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
	
	private boolean isElementPresent(WebElement wd, By by) {
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
