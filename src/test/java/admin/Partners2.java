package admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* Partners2
 * 10. 파트너사 변경 후 저장하지 않고 목록, 다시 확인
 * 11. 파트너사 변경 불가 케이스 1, 3depth초과, D를 F밑으로 시도
 * 12. 파트너사 변경 불가 케이스2, A를 D밑으로 시도
 * 14. 파트너사 변경  케이스 1, C를 B밑으로 시도
 * 15. 파트너사 변경  케이스 2, C를 한국지사 밑으로 시도
 * 20. 파트너 삭제 시도. 고객사가 있는 파트너(TestE)
 * 
 * 30. 파트너사 변경  케이스 3, 지사변경
 */

public class Partners2 {

	public static String XPATH_PARTNER_SELECTBOX = "//div[@class='ant-select-selector']";
	public static String XPATH_PARTNER_SELECTBOX_ITEM = "//div[@class='ant-select-tree-treenode ant-select-tree-treenode-switcher-open']";
	public static String XPATH_PARTNER_SELECTTREE = "//div[@class='ant-select-tree']";
	
	
	public static String MSG_PARTNER_INVALID_PARTNER = "상위 파트너사로 선택할 수 없는 파트너사입니다.";
	public static String MSG_PARTNER_CAHNGE_SUCCESS = "파트너사가 변경되었습니다.";

	/* partners2
	 * kr - TestA - TestD
	 * 	 |		 |- TestE : 고객사가 있는 파트너사
	 * 	 |- TestB - TestF
	 * 	 |- TestC 
	 */
	public static String PARTNER_TESTA = "TestA";
	public static String PARTNER_TESTB = "TestB";
	public static String PARTNER_TESTC = "TestC";
	public static String PARTNER_TESTD = "TestD";
	public static String PARTNER_TESTE = "TestE";
	public static String PARTNER_TESTF = "TestF";
	
	public static String PARTNER_TESTEUSER = "TestEUser";
	
	public static String PARTNER_KR_NAME = "한국지사";
	public static String PARTNER_JP_NAME = "일본지사";
	
	public static String findItem = ".//span[@title='%s']";
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private String PartnerID = "";

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

	// 10. 파트너사 변경 후 저장하지 않고 목록, 다시 확인
	@Test(priority = 10, enabled = true)
	public void partnerSelect() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_TESTD);
			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					js.executeScript("arguments[0].scrollIntoView(true);", tree.findElement(By.xpath(item)));
					Thread.sleep(200);
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
					Thread.sleep(200);
					
				}
			}
			tree.findElement(By.xpath(item)).click();
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTD)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_TESTD 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(100);
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n3. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_KR_NAME)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_KR_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. 파트너사 변경 불가 케이스 1, 3depth초과, D를 F밑으로 시도
	@Test(priority = 11, enabled = true)
	public void partnerSelect_invalid1() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTD);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_TESTF);
			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
					Thread.sleep(100);
				}
			}
			tree.findElement(By.xpath(item)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTF)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_TESTF 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_SUBMIT_BTN)).click();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(MSG_PARTNER_INVALID_PARTNER)) {
					failMsg = failMsg + "\n4. toast message. [Expected]" + MSG_PARTNER_INVALID_PARTNER 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText();
				}
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(100);
			
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTD);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTA)) {
				failMsg = failMsg + "\n6. selected partner. [Expected]" + PARTNER_TESTA 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 12. 파트너사 변경 불가 케이스2, A를 D밑으로 시도
	@Test(priority = 12, enabled = true)
	public void partnerSelect_invalid2() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_TESTD);
			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					js.executeScript("arguments[0].scrollIntoView(true);", tree.findElement(By.xpath(item)));
					Thread.sleep(100);
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
					Thread.sleep(100);
				}
			}
			tree.findElement(By.xpath(item)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTD)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_TESTD 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_SUBMIT_BTN)).click();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(MSG_PARTNER_INVALID_PARTNER)) {
					failMsg = failMsg + "\n4. toast message. [Expected]" + MSG_PARTNER_INVALID_PARTNER 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText();
				}
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(100);
			
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_KR_NAME)) {
				failMsg = failMsg + "\n6. selected partner. [Expected]" + PARTNER_KR_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 14. 파트너사 변경  케이스 1, C를 B밑으로 시도
	@Test(priority = 14, enabled = true)
	public void partnerSelect_valid1() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTC);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			Actions actions = new Actions(driver);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_TESTB);
			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					js.executeScript("arguments[0].scrollIntoView(true);",tree.findElement(By.xpath(item)));
					System.out.println("$$$find korea partner : " + PARTNER_TESTB);
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));
					actions.moveToElement(pas.get(pas.size()-1));
					actions.perform();
					Thread.sleep(100);
					
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
					Thread.sleep(100);
				}
			}
			Thread.sleep(500);
			tree.findElement(By.xpath(item)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTB)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_TESTB 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_SUBMIT_BTN)).click();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_SAVE_SUCCESS)) {
					failMsg = failMsg + "\n4. toast message. [Expected]" + Partners.MSG_PARTNER_SAVE_SUCCESS 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
				}
			}
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTC);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_TESTB)) {
				failMsg = failMsg + "\n6. selected partner. [Expected]" + PARTNER_TESTB 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 15. 파트너사 변경  케이스 2, C를 한국지사 밑으로 시도
	@Test(priority = 15, enabled = true)
	public void partnerSelect_valid2() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTC);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_KR_NAME);

			Actions actions = new Actions(driver);

			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					System.out.println("$$$find korea partner : " + PARTNER_KR_NAME);
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));

					actions.moveToElement(pas.get(0));
					actions.perform();
					Thread.sleep(100);
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(0));
					Thread.sleep(100);
					js.executeScript("window.scrollBy(0,-100)");
				}
			}
			tree.findElement(By.xpath(item)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_KR_NAME)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_KR_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_SUBMIT_BTN)).click();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_SAVE_SUCCESS)) {
					failMsg = failMsg + "\n4. toast message. [Expected]" + Partners.MSG_PARTNER_SAVE_SUCCESS 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
				}
			}
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTC);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_KR_NAME)) {
				failMsg = failMsg + "\n6. selected partner. [Expected]" + PARTNER_KR_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 20. 파트너 삭제 시도. 고객사가 있는 파트너(TestE)
	@Test(priority = 20, enabled = true)
	public void partner_delete_withCustomer() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Partners.XPATH_PARTNER_DELETE_BTN)));

			driver.findElement(By.xpath(Partners.XPATH_PARTNER_DELETE_BTN)).click();
			Thread.sleep(500);
			
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(!alertText.contentEquals(Partners.MSG_PARTNER_DELETE_ALERT)) {
				failMsg = failMsg + "\n2. delete alert message. [Expected]" + Partners.MSG_PARTNER_DELETE_ALERT + " [Actual]" + alertText;
			}
			
			alert.accept();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_DELETE_FAIL)) {
					failMsg = failMsg + "\n4. fail toast message. [Expected]" + Partners.MSG_PARTNER_DELETE_FAIL 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText();
				}
			}
			
			if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + PartnerID)) {
				failMsg = failMsg + "\n5. not editview. current url : " + driver.getCurrentUrl();
				Thread.sleep(500);
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(500);
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		boolean find = false;
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(PARTNER_TESTE)) {
				find = true;
			}
		}
		
		if(!find) {
			failMsg = failMsg + "\n5. can not find partner in list. partnerID : " + PartnerID;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. 파트너 삭제 시도. 파트너가 있는 파트너(TestA)
	@Test(priority = 21, enabled = true)
	public void partner_delete_withPartners() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Partners.XPATH_PARTNER_DELETE_BTN)));

			driver.findElement(By.xpath(Partners.XPATH_PARTNER_DELETE_BTN)).click();
			Thread.sleep(500);
			
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(!alertText.contentEquals(Partners.MSG_PARTNER_DELETE_ALERT)) {
				failMsg = failMsg + "\n2. delete alert message. [Expected]" + Partners.MSG_PARTNER_DELETE_ALERT + " [Actual]" + alertText;
			}
			
			alert.accept();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_DELETE_FAIL)) {
					failMsg = failMsg + "\n4. fail toast message. [Expected]" + Partners.MSG_PARTNER_DELETE_FAIL 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText();
				}
			}
			
			if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + PartnerID)) {
				failMsg = failMsg + "\n5. not editview. current url : " + driver.getCurrentUrl();
				Thread.sleep(500);
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(500);
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		boolean find = false;
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(PARTNER_TESTA)) {
				find = true;
			}
		}
		
		if(!find) {
			failMsg = failMsg + "\n5. can not find partner in list. partnerID : " + PartnerID;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 30. 파트너사 변경  케이스 3, 지사변경
	@Test(priority = 30, enabled = true)
	public void partnerSelect_invalid3() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.logoutaddmin(driver);
		
		comm.logintadmin(driver, CommonValues.USER_SYSADMIN);
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			PartnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			driver.findElement(By.xpath(XPATH_PARTNER_SELECTBOX)).click();
			Thread.sleep(500);
			WebElement tree = driver.findElement(By.xpath(XPATH_PARTNER_SELECTTREE));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String item = String.format(findItem, PARTNER_JP_NAME);
			for(int i = 0 ; i < 10 ; i++) {
				if (isElementPresent(tree, By.xpath(item))) {
					js.executeScript("arguments[0].scrollIntoView(true);", tree.findElement(By.xpath(item)));
					Thread.sleep(100);
					break;	
				} else {
					List<WebElement> pas = driver.findElements(By.xpath(XPATH_PARTNER_SELECTBOX_ITEM));
					js.executeScript("arguments[0].scrollIntoView(true);", pas.get(pas.size()-1));
					Thread.sleep(100);
				}
			}
			tree.findElement(By.xpath(item)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_JP_NAME)) {
				failMsg = failMsg + "\n2. selected partner. [Expected]" + PARTNER_JP_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_SUBMIT_BTN)).click();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST))) {
				failMsg = failMsg + "\n3. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(MSG_PARTNER_INVALID_PARTNER)) {
					failMsg = failMsg + "\n4. toast message. [Expected]" + MSG_PARTNER_INVALID_PARTNER 
							+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_ERROR_TOAST)).getText();
				}
			}
			
			driver.findElement(By.xpath(Partners.XPATH_PARTNER_LIST_BTN)).click();
			Thread.sleep(100);
			
		}
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(PARTNER_TESTA);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			rows.get(0).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText().contentEquals(PARTNER_KR_NAME)) {
				failMsg = failMsg + "\n6. selected partner. [Expected]" + PARTNER_KR_NAME 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_PARTNERS)).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			wd.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
			CommonValues comm = new CommonValues();
			comm.setCalender(driver);
			Thread.sleep(500);
		}
	}

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
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
