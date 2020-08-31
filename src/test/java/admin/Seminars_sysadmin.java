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

/* Seminars_sysadmin
 * 
 */

public class Seminars_sysadmin {
	
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
		comm.logintadmin(driver, CommonValues.USER_PARTNER_KR);
		
		System.out.println("End BeforeTest!!!");
	}

	// 10. search  세미나명, 채널명, 게시자명, 발표자닉네임
	@Test(priority = 10, enabled = true)
	public void keyworkdSearch() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		//세미나명
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("Practice");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() < 1) {
			failMsg = "1. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			String seminarName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
			if (!seminarName.contains("Practice")) {
				failMsg = failMsg + "\n2. searched partners name [Expected]" + "Practice" + " [Actual]" + seminarName;
			}
		}

		// 채널명
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("rsrsup1");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() < 1) {
			failMsg = "\n3. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			String channelName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if (!channelName.contentEquals("rsrsup1")) {
				failMsg = failMsg + "\n4. searched partners name [Expected]" + "rsrsup1" + " [Actual]" + channelName;
			}
		}
		
		// 게시자명
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("NickName");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() < 1) {
			failMsg = "\n5. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			String userName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[4]")).getText();
			if (!userName.contentEquals("NickName")) {
				failMsg = failMsg + "\n6. searched partners name [Expected]" + "NickName" + " [Actual]" + userName;
			}
		}
		
		// 발표자닉네임
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("rsrsup3");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() < 1) {
			failMsg = "\n7. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} 
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. keyword search - invalid
	@Test(priority = 11, enabled = true)
	public void keywordSearch_invalid() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		//blank
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("   ");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 0) {
			failMsg = "1. searched item error [Expected]0row [Actual]" + rows.size();
		} 
		
		//spacial
		for(int i = 0 ; i < CommonValues.SPECIAL_CHARACTER.length ; i++) {
			comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
			driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
			driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(CommonValues.SPECIAL_CHARACTER[i]);
			driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
			Thread.sleep(500);
			
			rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
			
			if(rows.size() != 0) {
				for(int j = 0 ; j < rows.size() ; j++) {
					String channel = rows.get(j).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
					String seminar = rows.get(j).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
					String user = rows.get(j).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[4]")).getText();
					
					if(!channel.contains(CommonValues.SPECIAL_CHARACTER[i]) && !seminar.contains(CommonValues.SPECIAL_CHARACTER[i])
							&& user.contains(CommonValues.SPECIAL_CHARACTER[i])) {
						failMsg = "\n2-" + i + ". searched item error. search keyword : " + CommonValues.SPECIAL_CHARACTER[i] 
								+ ", searched channel" + channel + ", searched seminarname" + seminar + ", searched username" + user;
					}
				}
			} 
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 12. listItem
	@Test(priority = 12, enabled = true)
	public void listItem() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		rows.get(0).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARINFO)) {
			failMsg = "1. not user info view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	

	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARLIST)) {
			wd.get(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARLIST);
			
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
