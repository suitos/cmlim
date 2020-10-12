package admin;

import static org.testng.Assert.fail;

import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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

/* Users
 * 10. search  닉네임, 계정
 * 11. keyword search - invalid
 * 12. listItem
 * 20. user 등록 - 구성
 * 21. user 등록. 빈값
 * 22. user 등록. email check
 * 23. user 등록. invalid
 * 24. 파트너 등록. invalid2
 * 25. user 등록. valid1
 * 26. user 수정
 * 27. user 수정. 필수값 삭제 시도
 * 28. 파트너 수정. invalid1
 * 29. user 수정. invalid2
 * 30. user 정보. 마케팅수신동의
 * 31. user 정보. 중요고객사
 * 50. delete user1
 */

public class Users {
	public static String XPATH_USER_INFO_NAME = "//input[@id='nickname']";
	public static String XPATH_USER_INFO_NAME_ERROR = "//input[@id='nickname']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_USER_INFO_EMAIL = "//input[@id='email']";
	public static String XPATH_USER_INFO_EMAIL_ERROR = "//input[@id='email']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_USER_INFO_PHONE = "//input[@id='phone']";
	public static String XPATH_USER_INFO_COMPANY = "//input[@id='companyName']";
	public static String XPATH_USER_INFO_POSITION = "//input[@id='jobPosition']";
	public static String XPATH_USER_INFO_TYPE = "//input[@id='businessType']";
	public static String XPATH_USER_INFO_BUSINESSNO = "//input[@id='businessRegno']";
	public static String XPATH_USER_INFO_ADDRESS = "//input[@id='address']";
	
	public static String XPATH_USER_INFO_SAVE_BTN = "//button[@type='submit']";
	public static String XPATH_USER_DUPLICATION_BTN = "//button[@id='duplication']";
	public static String XPATH_USER_INFO_DELETEUSER_BTN = "//button[@class='ant-btn ant-btn-primary ant-btn-dangerous']";
	
	public static String XPATH_USER_INFO_PROFILE_NEME = "//div[@class='__profile']//h5";
	public static String XPATH_USER_INFO_PROFILE_EMAIL = "//div[@class='ant-descriptions']//tr[@class='ant-descriptions-row'][1]/td//span[@class='ant-descriptions-item-content']";
	public static String XPATH_USER_INFO_PROFILE_PARTNER = "//div[@class='ant-descriptions']//tr[@class='ant-descriptions-row'][2]/td//span[@class='ant-descriptions-item-content']";
	public static String XPATH_USER_INFO_PROFILE_PARTNER_BTN = "//div[@class='ant-descriptions']//tr[@class='ant-descriptions-row'][2]//button";
	public static String XPATH_USER_INFO_PROFILE_MARKETING  = "//div[@class='ant-descriptions']//tr[@class='ant-descriptions-row'][3]//input";
	public static String XPATH_USER_INFO_PROFILE_IMPORTANTUSER  = "//div[@class='ant-descriptions']//tr[@class='ant-descriptions-row'][4]//input";
	public static String XPATH_USER_INFO_PROFILE_PHONE = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][1]/td";
	public static String XPATH_USER_INFO_PROFILE_COMPANY = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][2]/td";
	public static String XPATH_USER_INFO_PROFILE_POSITION = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][3]/td";
	public static String XPATH_USER_INFO_PROFILE_TYPE = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][4]/td";
	public static String XPATH_USER_INFO_PROFILE_BUSINESSNO = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][5]/td";
	public static String XPATH_USER_INFO_PROFILE_ADDRESS = "//div[@class='ant-descriptions ant-descriptions-bordered']//tr[@class='ant-descriptions-row'][6]/td";
	
	public static String XPATH_USER_INFO_PROFILE_PARTNER_SELECTBOX = "//div[@class='ant-select-selector']";
	
	public static String XPATH_USER_INFO_EDIT_BTN = "//div[@class='buttons']/button[1]";
	public static String XPATH_USER_EDIT_SAVE_BTN = "//button[@type='submit']";
	
	public static String MSG_USER_NAME_EMPTY = "닉네임을 기입해주세요.";
	public static String MSG_USER_EMAIL_EMPTY = "이메일을 기입해주세요.";
	public static String MSG_USER_EMAIL_INVALID = "이메일 형식이 올바르지 않습니다.";
	public static String MSG_USER_CHANGE_MARKETING = "마케팅 수신 동의가 변경되었습니다.";
	public static String MSG_USER_CHANGE_IMPORTANTUSER = "중요 고객이 변경되었습니다.";
	public static String MSG_USER_DELETE_USER = "정말로 탈퇴하시겠습니까?";
	
	public static String specialStr_no = "123456789!@#$%^&*()_+|";
	public static int user_count = 2;
	public static String user_pauser01 = "pauser01";
	public static String user_pauser01_name = "pauser01name";
	public static String user_pauser02 = "pauser02";
	public static String user_pauser02_name = "pauser01name";
	public static String user_temp = "pausertemp"; //등록,삭제용
	
	public static WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
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
		comm.logintadmin(driver, CommonValues.USER_PARTNER_1);
		
		System.out.println("End BeforeTest!!!");
	}

	// 10. search  닉네임, 계정
	@Test(priority = 10, enabled = true)
	public void keyworkdSearch() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		//닉네임
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("01name");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			String partnersName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if (!partnersName.contentEquals(user_pauser01_name)) {
				failMsg = failMsg + "\n2. searched partners name [Expected]" + user_pauser01_name + " [Actual]" + partnersName;
			}
		}

		// 계정
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("pauser01@");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);

		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		if (rows.size() != 1) {
			failMsg = "=\n3. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			String partnersName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if (!partnersName.contentEquals(user_pauser01_name)) {
				failMsg = failMsg + "\n4. searched partners name [Expected]" + user_pauser01_name + " [Actual]" + partnersName;
			}
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
		
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("rsrsup02");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 0) {
			failMsg = "1. searched item error [Expected]0row [Actual]" + rows.size();
		} 
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("rsrsup03");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		if(rows.size() != 0) {
			failMsg = "\n2. searched item error [Expected]0row [Actual]" + rows.size();
		} 
		
		//blank
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("  ");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != user_count) {
			failMsg = "\n3. searched item error [Expected]" + user_count + " [Actual]" + rows.size();
		} else {
			String userName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!userName.contains("pauser")) {
				failMsg = failMsg + "\n4. searched partners name [Expected]PartnersA [Actual]" + userName;
			}
			
			userName = rows.get(1).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!userName.contains("pauser")) {
				failMsg = failMsg + "\n5. searched partners name [Expected]PartnersB [Actual]" + userName;
			}
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
				if(CommonValues.SPECIAL_CHARACTER[i] != "@")
					failMsg = "\n6-" + i + ". searched item error. search keyword : " + CommonValues.SPECIAL_CHARACTER[i] 
						+ " [Expected]0rows [Actual]" + rows.size();
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
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			failMsg = "1. not user info view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 20. user 등록
	@Test(priority = 20, enabled = true)
	public void adduser1() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_REGISTER_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			failMsg = "1. not new user view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("placeholder").contentEquals("Nickname")) {
			failMsg = failMsg + "\n2. name placeholder. [Expected]Nickname [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).getAttribute("placeholder").contentEquals("Email")) {
			failMsg = failMsg + "\n3. Email placeholder. [Expected]Email [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("placeholder").contentEquals("phone")) {
			failMsg = failMsg + "\n4. phone placeholder. [Expected]phone [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("placeholder").contentEquals("companyName")) {
			failMsg = failMsg + "\n5. companyName placeholder. [Expected]companyName [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("placeholder").contentEquals("jobPosition")) {
			failMsg = failMsg + "\n6. jobPosition placeholder. [Expected]jobPosition [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("placeholder").contentEquals("businessType")) {
			failMsg = failMsg + "\n7. businessType placeholder. [Expected]businessType [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("placeholder").contentEquals("businessRegno")) {
			failMsg = failMsg + "\n8. businessRegno placeholder. [Expected]businessRegno [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("placeholder").contentEquals("address")) {
			failMsg = failMsg + "\n9. address placeholder. [Expected]address [Actual]" 
					+ driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("placeholder");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. user 등록. 빈값
	@Test(priority = 21, enabled = true)
	public void adduser_empty() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}	
		
		// click submit
		driver.findElement(By.xpath(XPATH_USER_INFO_SAVE_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME_ERROR)).getText().contentEquals(MSG_USER_NAME_EMPTY)) {
			failMsg = "1. user name error(empty) [Expected]" + MSG_USER_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText().contentEquals(MSG_USER_EMAIL_EMPTY)) {
			failMsg = failMsg + "\n2. user email error(empty) [Expected]" + MSG_USER_EMAIL_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	// 22. user 등록. email check
	@Test(priority = 22, enabled = true)
	public void adduser_email() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}
		
		//empty
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText().contentEquals(MSG_USER_EMAIL_EMPTY)) {
			failMsg = "1. email input error(empty) [Expected]" + MSG_USER_EMAIL_EMPTY  
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText();
		}		
		
		//invalid format
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys("invalidStr");
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText().contentEquals(MSG_USER_EMAIL_INVALID)) {
			failMsg = failMsg + "\n2. email input error(invalid format) [Expected]" + MSG_USER_EMAIL_INVALID  
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText();
		}
		
		//duplicated email
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)));
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(user_pauser01 + "@rsupport.com");
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		Thread.sleep(100);
		
		String msg = String.format(Partners.MSG_PARTNER_USER_EMAIL_DUPLICATE, user_pauser01 + "@rsupport.com");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText().contentEquals(msg)) {
			failMsg = failMsg + "\n3. email input error(invalid format) [Expected]" + msg  
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL_ERROR)).getText();
		}
		
		//valid email
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)));
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(user_temp + "@rsupport.com");
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		Thread.sleep(500);
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n4. cannot find toast";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_USER_EMAIL_VALID)){
				failMsg = failMsg + "\n5. toast message. [Expected]" + Partners.MSG_PARTNER_USER_EMAIL_VALID
						 + " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 23. user 등록. invalid
	@Test(priority = 23, enabled = true)
	public void adduser_invalid1() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}	
		
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(specialStr_no + "name");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").contentEquals("name")) {
			failMsg = "1. name input error [Expected]name" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(specialStr_no + "phone");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n2. phone input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(specialStr_no + "company");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").contentEquals("company")) {
			failMsg = failMsg + "\n3. company input error [Expected]company" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(specialStr_no + "position");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").contentEquals("position")) {
			failMsg = failMsg + "\n4. position input error [Expected]position" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(specialStr_no + "type");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").contentEquals("type")) {
			failMsg = failMsg + "\n5. type input error [Expected]type" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(specialStr_no);
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n6. businessRegNo input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(specialStr_no + "address");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").contentEquals(specialStr_no + "address")) {
			failMsg = failMsg + "\n7. address input error [Expected]" + (specialStr_no + "address" ) 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 24. 파트너 등록. invalid2
	@Test(priority = 24, enabled = true)
	public void adduser_invalid2() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}		
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)));
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").length() > 20) {
			failMsg = "1. name input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(CommonValues.NUMBER_20 + "111");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. phone input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)));
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(CommonValues.CHARACTER_20 + "company");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. company input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)));
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(CommonValues.CHARACTER_20 + "position");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n4. position input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(CommonValues.NUMBER_20 + "type");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n5. type input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(CommonValues.NUMBER_20 + CommonValues.NUMBER_20 + CommonValues.NUMBER_20);
		if(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n6. businessRegNo input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)));
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.NUMBER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n7. address input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").length();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 25. user 등록. valid1
	@Test(priority = 25, enabled = true)
	public void adduser_valid1() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}

		CommonValues comm = new CommonValues();
		
		//valid email
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)));
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(user_temp + "@rsupport.com");
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		Thread.sleep(500);
		if (!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\1. cannot find toast";
		}	
		Thread.sleep(2000);
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)));
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(user_temp);
		
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)));
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)));
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)));
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		
		// click submit
		driver.findElement(By.xpath(XPATH_USER_INFO_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + Partners.MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(user_temp)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			failMsg = failMsg + "\n4. cannot find saved user in list. current url : " + driver.getCurrentUrl();
		} else {
			userID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO, "");
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText().contentEquals(user_temp)) {
				failMsg = failMsg + "\n5. saved user name. [Expected]" + user_temp
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText().contentEquals(user_temp + "@rsupport.com")) {
				failMsg = failMsg + "\n6. saved user email. [Expected]" + (user_temp + "@rsupport.com")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText();
			}
			
			String tmpPa = CommonValues.USER_PARTNER_1_NAME + "  파트너사 변경";
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText().contentEquals(tmpPa)) {
				failMsg = failMsg + "\n7. saved user partner. [Expected]" + (tmpPa)
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText().isEmpty()) {
				failMsg = failMsg + "\n8. saved user phone. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText().isEmpty()) {
				failMsg = failMsg + "\n9. saved user company. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText().isEmpty()) {
				failMsg = failMsg + "\n10. saved user position. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText().isEmpty()) {
				failMsg = failMsg + "\n11. saved user bussiness type. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText().isEmpty()) {
				failMsg = failMsg + "\n12. saved user bussiness number. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText().isEmpty()) {
				failMsg = failMsg + "\n13. saved user address. [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText();
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 26. user 수정
	@Test(priority = 26, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_mod() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_EDIT_BTN)).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID)) {
			failMsg = "1. not user edit view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID);
			Thread.sleep(500);
		}

		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").contentEquals(user_temp)) {
			failMsg = failMsg + "\n2. saved user name(edit view). [Expected]" + user_temp
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value");
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).getAttribute("value").contentEquals(user_temp + "@rsupport.com")) {
			failMsg = failMsg + "\n3. saved user email(edit view). [Expected]" + (user_temp + "@rsupport.com")
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).getAttribute("value");
		}
		
		if(driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).isEnabled()) {
			failMsg = failMsg + "\n4. email box is enabled.";
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys("mod");
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys("111111");
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys("company");
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys("position");
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys("businessType");
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys("111111");
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys("address");
		
		
		// click submit
		driver.findElement(By.xpath(XPATH_USER_EDIT_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + Partners.MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(user_temp + "mod")) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			failMsg = failMsg + "\n4. cannot find saved user in list. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		} 
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText().contentEquals(user_temp + "mod")) {
			failMsg = failMsg + "\n5. saved user name. [Expected]" + user_temp + "mod"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText().contentEquals(user_temp + "@rsupport.com")) {
			failMsg = failMsg + "\n6. saved user email. [Expected]" + (user_temp + "@rsupport.com")
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText();
		}
		
		String partnerStr = CommonValues.USER_PARTNER_1_NAME + "  파트너사 변경";
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText().contentEquals(partnerStr)) {
			failMsg = failMsg + "\n7. saved user partner. [Expected]" + (partnerStr)
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText().contentEquals("111111")) {
			failMsg = failMsg + "\n8. saved user phone. [Expected]" + "111111"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText().contentEquals("company")) {
			failMsg = failMsg + "\n9. saved user company. [Expected]" + "company"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText().contentEquals("position")) {
			failMsg = failMsg + "\n10. saved user position. [Expected]" + "position"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText().contentEquals("businessType")) {
			failMsg = failMsg + "\n11. saved user bussiness type. [Expected]" + "businessType"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText().contentEquals("111111")) {
			failMsg = failMsg + "\n12. saved user bussiness number. [Expected]" + "111111"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText().contentEquals("address")) {
			failMsg = failMsg + "\n13. saved user address. [Expected]" + "address"
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 27. user 수정. 필수값 삭제 시도
	@Test(priority = 27, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_mod2() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_EDIT_BTN)).click();
		Thread.sleep(500);

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID)) {
			failMsg = "1. not user edit view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)));
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		
		
		// click submit
		driver.findElement(By.xpath(XPATH_USER_EDIT_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME_ERROR)).getText().contentEquals(MSG_USER_NAME_EMPTY)) {
			failMsg = failMsg + "\n2. user name error(empty) [Expected]" + MSG_USER_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 28. 파트너 수정. invalid1
	@Test(priority = 28, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_mod_invalid() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)));
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(specialStr_no + "name");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").contentEquals("name")) {
			failMsg = "1. name input error [Expected]name" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(specialStr_no + "phone");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n2. phone input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)));
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(specialStr_no + "company");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").contentEquals("company")) {
			failMsg = failMsg + "\n3. company input error [Expected]company" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)));
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(specialStr_no + "position");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").contentEquals("position")) {
			failMsg = failMsg + "\n4. position input error [Expected]position" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(specialStr_no + "type");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").contentEquals("type")) {
			failMsg = failMsg + "\n5. type input error [Expected]type" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(specialStr_no);
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n6. businessRegNo input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)));
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(specialStr_no + "address");
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").contentEquals(specialStr_no + "address")) {
			failMsg = failMsg + "\n7. address input error [Expected]" + (specialStr_no + "address" ) 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 29. user 수정. invalid2
	@Test(priority = 29, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_mod_invalid2() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USEREDIT + userID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)));
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").length() > 20) {
			failMsg = "1. name input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys(CommonValues.NUMBER_20 + "111");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. phone input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)));
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys(CommonValues.CHARACTER_20 + "company");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. company input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)));
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys(CommonValues.CHARACTER_20 + "position");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n4. position input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)));
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys(CommonValues.NUMBER_20 + "type");
		if(driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n5. type input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys(CommonValues.NUMBER_20 + CommonValues.NUMBER_20 + CommonValues.NUMBER_20);
		if(driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n6. businessRegNo input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)));
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.NUMBER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n7. address input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).getAttribute("value").length();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 30. user 정보. 마케팅수신동의
	@Test(priority = 30, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_marketing() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		}
		
		if(driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_MARKETING)).isSelected()) {
			failMsg = "1. marketing checkbox is checked.";
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_MARKETING)).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n3. cannot find toast";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_USER_CHANGE_MARKETING)) {
				failMsg = failMsg + "\n4. toast message [Expected]" + MSG_USER_CHANGE_MARKETING
						 + " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_MARKETING)).isSelected()) {
			failMsg = failMsg + "\n5. marketing checkbox is unchecked.(after click)";
		}
		
		driver.navigate().refresh();
		Thread.sleep(2000);
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_MARKETING)).isSelected()) {
			failMsg = failMsg + "\n6. marketing checkbox is unchecked.(after refresh)";
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_MARKETING)).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n7. cannot find toast";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_USER_CHANGE_MARKETING)) {
				failMsg = failMsg + "\n8. toast message [Expected]" + MSG_USER_CHANGE_MARKETING
						 + " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		Thread.sleep(2000);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 31. user 정보. 중요고객사
	@Test(priority = 31, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void adduser_ImportantUser() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		}
		Thread.sleep(1000);
		if(driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_IMPORTANTUSER)).isSelected()) {
			failMsg = "1. important user checkbox is checked.";
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_IMPORTANTUSER)).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n3. cannot find toast";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_USER_CHANGE_IMPORTANTUSER)) {
				failMsg = failMsg + "\n4. toast message [Expected]" + MSG_USER_CHANGE_IMPORTANTUSER
						 + " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_IMPORTANTUSER)).isSelected()) {
			failMsg = failMsg + "\n5. important user checkbox is unchecked.(after click)";
		}
		
		driver.navigate().refresh();
		Thread.sleep(2000);
		
		if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_IMPORTANTUSER)).isSelected()) {
			failMsg = failMsg + "\n6. important user checkbox is unchecked.(after refresh)";
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_IMPORTANTUSER)).click();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n7. cannot find toast";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_USER_CHANGE_IMPORTANTUSER)) {
				failMsg = failMsg + "\n8. toast message [Expected]" + MSG_USER_CHANGE_IMPORTANTUSER
						 + " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		Thread.sleep(2000);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 40. delete user1
	@Test(priority = 40, dependsOnMethods = {"adduser_valid1"}, enabled = true)
	public void deleteUser() throws Exception {
		String failMsg = "";	
	
		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_USER_INFO_DELETEUSER_BTN)).click();
		Thread.sleep(500);
		
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		if(!alertText.contentEquals(MSG_USER_DELETE_USER)) {
			failMsg = "1. delete alert message. [Expected]" + MSG_USER_DELETE_USER + " [Actual]" + alertText;
		}
		alert.dismiss();
		
		driver.findElement(By.xpath(XPATH_USER_INFO_DELETEUSER_BTN)).click();
		Thread.sleep(500);
		alert.accept();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n2. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_DELETE_SUCCESS)) {
				failMsg = failMsg + "\n3. delete toast message. [Expected]" + Partners.MSG_PARTNER_DELETE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
			userID = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 41. add user valid2
	@Test(priority = 41, enabled = true)
	public void addUser_valid2() throws Exception {
		String failMsg = "";	
	
		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERNEW);
			Thread.sleep(500);
		}

		//valid email
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_EMAIL)).sendKeys(user_temp + "@rsupport.com");
		driver.findElement(By.xpath(XPATH_USER_DUPLICATION_BTN)).click();
		Thread.sleep(500);
		if (!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\1. cannot find toast";
		}	
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_NAME)).sendKeys(user_temp);
		
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_PHONE)).sendKeys("111111");
		
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_COMPANY)).sendKeys("company");
		
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_POSITION)).sendKeys("position");
		
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_TYPE)).sendKeys("type");
		
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_BUSINESSNO)).sendKeys("111111");
		
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_USER_INFO_ADDRESS)).sendKeys("address");
		
		// click submit
		driver.findElement(By.xpath(XPATH_USER_INFO_SAVE_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(Partners.MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + Partners.MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(user_temp)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			failMsg = failMsg + "\n4. cannot find saved user in list. current url : " + driver.getCurrentUrl();
		} else {
			userID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO, "");
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText().contentEquals(user_temp)) {
				failMsg = failMsg + "\n5. saved user name. [Expected]" + user_temp
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_NEME)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText().contentEquals(user_temp + "@rsupport.com")) {
				failMsg = failMsg + "\n6. saved user email. [Expected]" + (user_temp + "@rsupport.com")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_EMAIL)).getText();
			}
			
			String tmpPa = CommonValues.USER_PARTNER_1_NAME + "  파트너사 변경";
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText().contentEquals(tmpPa)) {
				failMsg = failMsg + "\n7. saved user partner. [Expected]" + (tmpPa)
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PARTNER)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText().contentEquals("111111")) {
				failMsg = failMsg + "\n8. saved user phone. [Expected]" + "111111"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_PHONE)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText().contentEquals("company")) {
				failMsg = failMsg + "\n9. saved user company. [Expected]" + "company"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_COMPANY)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText().contentEquals("position")) {
				failMsg = failMsg + "\n10. saved user position. [Expected]" + "position"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_POSITION)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText().contentEquals("type")) {
				failMsg = failMsg + "\n11. saved user bussiness type. [Expected]" + "type"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_TYPE)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText().contentEquals("111111")) {
				failMsg = failMsg + "\n12. saved user bussiness number. [Expected]" + "111111"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_BUSINESSNO)).getText();
			}
			
			if(!driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText().contentEquals("address")) {
				failMsg = failMsg + "\n13. saved user address. [Expected]" + "address"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USER_INFO_PROFILE_ADDRESS)).getText();
			}
			
		}
	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 42. add user - login seminar
	@Test(priority = 42, dependsOnMethods = {"addUser_valid2"}, enabled = true)
	public void addUser_loginseminar() throws Exception {
		String failMsg = "";	

		seminartest.DBConnection db = new seminartest.DBConnection();
		db.setUserPW(user_temp + "@rsupport.com");
		
		driver.get(seminartest.CommonValues.SERVER_URL);
		seminartest.CommonValues seminarComm = new seminartest.CommonValues();
		seminarComm.loginseminar(driver, user_temp + "@rsupport.com");
		
		Thread.sleep(500);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 43. add user - login seminar
	@Test(priority = 43, dependsOnMethods = { "addUser_valid2" }, enabled = true)
	public void addUser2_deleteUser() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.ADMIN_URL);
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_USERINFO + userID);
			Thread.sleep(500);
		} 
		
		// delete user
		Thread.sleep(1000);
		driver.findElement(By.xpath(XPATH_USER_INFO_DELETEUSER_BTN)).click();
		Thread.sleep(500);

		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		if (!alertText.contentEquals(MSG_USER_DELETE_USER)) {
			failMsg = "1. delete alert message. [Expected]" + MSG_USER_DELETE_USER + " [Actual]" + alertText;
		}
		alert.accept();
		Thread.sleep(500);

		if (!isElementPresent(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n14. cannot find toast.";
		} else {
			if (!driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText()
					.contentEquals(Partners.MSG_PARTNER_DELETE_SUCCESS)) {
				failMsg = failMsg + "\n15. delete toast message. [Expected]" + Partners.MSG_PARTNER_DELETE_SUCCESS
						+ " [Actual]" + driver.findElement(By.xpath(Partners.XPATH_PARTNER_SAVE_TOAST)).getText();
			}
			userID = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	private void checkListView(WebDriver wd) throws InterruptedException {
		if(!wd.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST)) {
			wd.get(CommonValues.ADMIN_URL + CommonValues.URL_USERLIST);
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
