package admin;

import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

/* Partners1
 * 10. keyword search
 * 11. keyword search - invalid
 * 12. listItem
 * 20. 파트너 등록
 * 21. 파트너 등록. 빈값
 * 22. 파트너 등록. invalid
 * 23. 파트너 등록. invalid2
 * 24. 파트너 등록. valid1
 * 25. 파트너 수정
 * 26. 파트너 수정. 필수값 삭제 시도
 * 27. 파트너 수정. invalid1
 * 28. 파트너 수정. invalid2
 * 29. 파트너  삭제. delete1
 * 30. 파트너 등록. valid2
 * 31. 파트너 관리자  등록. 구성
 * 32. 파트너 관리자  등록. email
 * 33. 파트너 관리자  등록. nickname
 * 34. 파트너 관리자  등록. department
 * 35. 파트너 관리자  등록. phone
 * 36. 파트너 관리자  등록. position
 * 37. 파트너 관리자  등록. save valid
 * 38. 파트너 관리자  등록. 수정취소
 * 39. 파트너 관리자  등록. 수정완료
 * 40. 파트너 관리자  등록. 수정-name
 * 41. 파트너 관리자  등록. 수정-department
 * 42. 파트너 관리자  등록. 수정-phone
 * 43. 파트너 관리자  등록. 수정-position
 * 44. 파트너 삭제 시도. 관리자가 있는 파트너
 * 45. 파트너 관리자 삭제
 * 46. 파트너 관리자 추가2
 * 47. 파트너 관리자 삭제2
 * 50. 파트너  삭제. delete2
 */

public class Partners {
	public static String XPATH_PARTNER_NAME = "//input[@id='signup-partner_name']";
	public static String XPATH_PARTNER_PARTNERS = "//input[@id='signup-partner_parentId']/../../span[@class='ant-select-selection-item']";
	public static String XPATH_PARTNER_PHONE = "//input[@id='signup-partner_phone']";
	public static String XPATH_PARTNER_CEO = "//input[@id='signup-partner_ceo']";
	public static String XPATH_PARTNER_TYPE = "//input[@id='signup-partner_businessType']";
	public static String XPATH_PARTNER_BUSINESSNO = "//input[@id='signup-partner_businessRegno']";
	public static String XPATH_PARTNER_ADDRESS = "//input[@id='signup-partner_address']";
	public static String XPATH_PARTNER_COUNTRY = "//input[@id='signup-partner_country']/../../span[@class='ant-select-selection-item']";
	public static String XPATH_PARTNER_HOMEPAGE = "//input[@id='signup-partner_homepage']";
	public static String XPATH_PARTNER_REMARK = "//input[@id='signup-partner_remark']";
	
	public static String XPATH_PARTNER_NAME_ERROR = "//input[@id='signup-partner_name']/../../../div[@class='ant-form-item-explain']/div";
	public static String XPATH_PARTNER_CEO_ERROR = "//input[@id='signup-partner_ceo']/../../../div[@class='ant-form-item-explain']/div";
	
	public static String XPATH_PARTNER_SUBMIT_BTN = "//button[@type='submit']";
	public static String XPATH_PARTNER_LIST_BTN = "//button[@class='ant-btn ant-btn-sub-3']";
	public static String XPATH_PARTNER_SAVE_TOAST = "//div[@class='ant-message-custom-content ant-message-success']/span[2]";
	public static String XPATH_PARTNER_ERROR_TOAST = "//div[@class='ant-message-custom-content ant-message-error']/span[2]";
	public static String XPATH_PARTNER_DELETE_BTN = "//button[@class='ant-btn ant-btn-primary ant-btn-dangerous']";
	public static String XPATH_PARTNER_ADD_USER_BTN = "//button[@class='ant-btn ant-btn-sub ant-btn-sm']";
	public static String XPATH_PARTNER_ADD_USER_EMAIL = "//div[@id='email']//input";
	public static String XPATH_PARTNER_ADDED_USER_EMAIL = "//input[@id='email']";
	public static String XPATH_PARTNER_ADD_USER_EMAIL_ERROR = "//div[@id='email']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_PARTNER_ADD_USER_NAME = "//input[@id='nickname']";
	public static String XPATH_PARTNER_ADD_USER_NAME_ERROR = "//input[@id='nickname']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_PARTNER_ADD_USER_DEP = "//input[@id='department']";
	public static String XPATH_PARTNER_ADD_USER_DEP_ERROR = "//input[@id='department']/../../../div[@class='ant-form-item-explain']";
	public static String XPATH_PARTNER_ADD_USER_PHONE = "//input[@id='phone']";
	public static String XPATH_PARTNER_ADD_USER_POSITION = "//input[@id='jobPosition']";
	public static String XPATH_PARTNER_POPUP_SAVE_BTN = "//div[@class='ant-modal-footer']/button[1]";
	public static String XPATH_PARTNER_POPUP_CANCEL_BTN = "//div[@class='ant-modal-footer']/button[2]";
	public static String XPATH_PARTNER_POPUP_DELETE_BTN = "//div[@class='ant-modal-footer']/button[@class='ant-btn ant-btn-primary ant-btn-dangerous']";
	public static String XPATH_PARTNER_USER_EMAIL_BTN = "//div[@id='email']//button";
	public static String XPATH_PARTNER_USER_LIST_ITEM = "//tr[@class='ant-table-row ant-table-row-level-0']";
	public static String XPATH_PARTNER_USER_LIST_ITEM_CELL = "./td[@class='ant-table-cell']";
	
	public static String MSG_PARTNER_NAME_EMPTY = "파트너사명을 기입해주세요.";
	public static String MSG_PARTNER_CEO_EMPTY = "대표자명을 기입해주세요.";
	public static String MSG_PARTNER_SAVE_SUCCESS = "저장되었습니다.";
	public static String MSG_PARTNER_DELETE_ALERT = "정말로 삭제하시겠습니까?";
	public static String MSG_PARTNER_DELETE_SUCCESS = "삭제되었습니다.";
	public static String MSG_PARTNER_USER_EMAIL_EMPTY = "이메일을 기입해주세요.";
	public static String MSG_PARTNER_USER_EMAIL_INVALID = "이메일 형식이 올바르지 않습니다.";
	public static String MSG_PARTNER_USER_EMAIL_DUPLICATE = "%s는 이미 존재하는 이메일주소 입니다.";
	public static String MSG_PARTNER_USER_NAME_EMPTY = "담당자명을 기입해주세요.";
	public static String MSG_PARTNER_USER_DEP_EMPTY = "부서를 기입해주세요.";
	public static String MSG_PARTNER_USER_EMAIL_VALID = "이메일 이용 가능합니다.";
	public static String MSG_PARTNER_DELETE_FAIL = "파트너 담당자, 하위 파트너사, 고객(서비스 사용자)이 없는 경우에만 삭제가 가능합니다. (42101)";
	
	public static String specialStr_no = "123456789!@#$%^&*()_+|";
	
	public static int partner_count = 2;
	public static String add_partner1 = "testPA1";
	public static String add_partner2 = "testPA2";
	public static String add_partner2_user1 = "pa2user1";
	
	public String partnerID = "";
	
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
		
		comm.logintadmin(driver, CommonValues.USER_PARTNER_1);

		System.out.println("End BeforeTest!!!");
	}

	// 10. keyword search
	@Test(priority = 10, enabled = true)
	public void keywordSearch() throws Exception {
		String failMsg = "";
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		//파트너사 명
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("rsA");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "1. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			String partnersName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!partnersName.contentEquals("PartnersA")) {
				failMsg = failMsg + "\n2. searched partners name [Expected]PartnersA [Actual]" + partnersName;
			}
		}
		
		//담당자명
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("ApartnersUser1");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 1) {
			failMsg = "=\n3. searched item error [Expected]1rows [Actual]" + rows.size();
		} else {
			String partnersName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!partnersName.contentEquals("PartnersA")) {
				failMsg = failMsg + "\n4. searched partners name [Expected]PartnersA [Actual]" + partnersName;
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
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);
		
		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("seleniumTest");
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 0) {
			failMsg = "1. searched item error [Expected]0row [Actual]" + rows.size();
		} 
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys("patest01");
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
		
		if(rows.size() != partner_count) {
			failMsg = "\n3. searched item error [Expected]" + partner_count + " [Actual]" + rows.size();
		} else {
			String partnersName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!partnersName.contentEquals("PartnersA")) {
				failMsg = failMsg + "\n4. searched partners name [Expected]PartnersA [Actual]" + partnersName;
			}
			
			partnersName = rows.get(1).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
			if(!partnersName.contentEquals("PartnersB")) {
				failMsg = failMsg + "\n5. searched partners name [Expected]PartnersB [Actual]" + partnersName;
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
				failMsg = "=\n3. searched item error [Expected]0rows [Actual]" + rows.size();
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
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			failMsg = "1. not partners edit view. current url : " + driver.getCurrentUrl();
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 20. 파트너 등록
	@Test(priority = 20, enabled = true)
	public void addpartner1() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_REGISTER_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERNEW)) {
			failMsg = "1. not new partners view. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERNEW);
			Thread.sleep(500);
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("placeholder").contentEquals("name")) {
			failMsg = failMsg + "\n2. name placeholder. [Expected]name [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_PARTNERS)).getText().contentEquals("seleniumTest")) {
			failMsg = failMsg + "\n3. super partner. [Expected]seleniumTest [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_PARTNERS)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("placeholder").contentEquals("phone")) {
			failMsg = failMsg + "\n4. phone placeholder. [Expected]phone [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("placeholder").contentEquals("ceo")) {
			failMsg = failMsg + "\n5. ceo placeholder. [Expected]ceo [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("placeholder").contentEquals("businessType")) {
			failMsg = failMsg + "\n6. businessType placeholder. [Expected]businessType [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("placeholder").contentEquals("businessRegno")) {
			failMsg = failMsg + "\n7. businessRegno placeholder. [Expected]businessRegno [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("placeholder").contentEquals("address")) {
			failMsg = failMsg + "\n8. address placeholder. [Expected]address [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_COUNTRY)).getText().contentEquals("한국")) {
			failMsg = failMsg + "\n9. country. [Expected]한국 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_COUNTRY)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("placeholder").contentEquals("homepage")) {
			failMsg = failMsg + "\n10. homepage placeholder. [Expected]homepage [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("placeholder").contentEquals("remark")) {
			failMsg = failMsg + "\n11. remark placeholder. [Expected]remark [Actual]" 
					+ driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("placeholder");
		}
		
		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 21. 파트너 등록. 빈값
	@Test(priority = 21, enabled = true)
	public void addpartner_empty() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT);
			Thread.sleep(500);
		}	
		
		// click submit
		driver.findElement(By.xpath(XPATH_PARTNER_SUBMIT_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME_ERROR)).getText().contentEquals(MSG_PARTNER_NAME_EMPTY)) {
			failMsg = "1. partners name error(empty) [Expected]" + MSG_PARTNER_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO_ERROR)).getText().contentEquals(MSG_PARTNER_CEO_EMPTY)) {
			failMsg = failMsg + "\n2. partners ceo error(empty) [Expected]" + MSG_PARTNER_CEO_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 파트너 등록. invalid
	@Test(priority = 22, enabled = true)
	public void addpartner_invalid1() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT);
			Thread.sleep(500);
		}	
		
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(specialStr_no + "name");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").contentEquals("name")) {
			failMsg = "1. name input error [Expected]name" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(specialStr_no + "phone");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n2. phone input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(specialStr_no + "ceo");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").contentEquals("ceo")) {
			failMsg = failMsg + "\n3. ceo input error [Expected]ceo" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(specialStr_no + "type");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").contentEquals("type")) {
			failMsg = failMsg + "\n4. businessType input error [Expected]type" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(specialStr_no + "number");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n5. businessNo input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(specialStr_no + "address");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").contentEquals("address")) {
			failMsg = failMsg + "\n6. address input error [Expected]address" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(specialStr_no + "homepage");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").contentEquals(specialStr_no + "homepage")) {
			failMsg = failMsg + "\n7. homepage input error [Expected]" + (specialStr_no + "homepage" ) 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(specialStr_no + "remark");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").contentEquals("remark")) {
			failMsg = failMsg + "\n8. remark input error [Expected]remark" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 23. 파트너 등록. invalid2
	@Test(priority = 23, enabled = true)
	public void addpartner_invalid2() throws Exception {
		String failMsg = "";	
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT);
			Thread.sleep(500);
		}		
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").length() > 20) {
			failMsg = "1. name input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(CommonValues.NUMBER_20 + "111");
		if(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. phone input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(CommonValues.CHARACTER_20 + "ceo");
		if(driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. ceo input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)));
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(CommonValues.CHARACTER_20 + "type");
		if(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n4. businessType input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(CommonValues.NUMBER_20 + CommonValues.NUMBER_20 + CommonValues.NUMBER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n5. businessNo input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n6. address input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n7. homepage input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").length();
		}
		
		char[] chars = new char[600];
		Arrays.fill(chars, 'a');
		String longchar = new String(chars);
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(longchar);
		if(driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").length() > 500) {
			failMsg = failMsg + "\n8. remark input length [Expected]500" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").length();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 24. 파트너 등록. valid1
	@Test(priority = 24, enabled = true)
	public void addpartner_valid1() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT);
			Thread.sleep(500);
		}

		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(add_partner1);
		
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(add_partner1 + "ceo");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)));
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_REMARK)));
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		
		// click submit
		driver.findElement(By.xpath(XPATH_PARTNER_SUBMIT_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner1)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			failMsg = failMsg + "\n4. cannot find saved partners in list. current url : " + driver.getCurrentUrl();
		} else {
			partnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").contentEquals(add_partner1)) {
				failMsg = failMsg + "\n5. saved partners name. [Expected]" + add_partner1
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").contentEquals(add_partner1 + "ceo")) {
				failMsg = failMsg + "\n6. saved partners name. [Expected]" + (add_partner1 + "ceo")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n7. saved partners phone is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n8. saved partners businessType is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n9. saved partners businessNo is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n9. saved partners address is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n9. saved partners homepage is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n9. saved partners remark is not empty. [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value");
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 25. 파트너 수정
	@Test(priority = 25, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_mod() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}

		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys("mod");
		
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys("111111");
	
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys("mod");
		
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys("type");

		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys("222222");

		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys("address");
		
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(CommonValues.ADMIN_URL);

		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys("remark");

		// click submit
		driver.findElement(By.xpath(XPATH_PARTNER_SUBMIT_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner1 + "mod")) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			failMsg = failMsg + "\n4. cannot find saved partners in list. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		} 
		partnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");

		if (!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").contentEquals(add_partner1 + "mod")) {
			failMsg = failMsg + "\n5. saved partners name. [Expected]" + add_partner1 + " [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value");
		}

		if (!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").contentEquals(add_partner1 + "ceo" + "mod")) {
			failMsg = failMsg + "\n6. saved partners name. [Expected]" + (add_partner1 + "ceo") + " [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value");
		}

		if (!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").contentEquals("111111")) {
			failMsg = failMsg + "\n7. saved partners phone. [Expected]111111  [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value");
		}
		if (!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").contentEquals("type")) {
			failMsg = failMsg + "\n8. saved partners businessType. [Expected]type  [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value");
		}
		if (!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").contentEquals("222222")) {
			failMsg = failMsg + "\n9. saved partners businessNo. [Expected]222222 [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value");
		}
		if (!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").contentEquals("address")) {
			failMsg = failMsg + "\n9. saved partners address. [Expected]address [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value");
		}
		if (!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").contentEquals(CommonValues.ADMIN_URL)) {
			failMsg = failMsg + "\n9. saved partners homepage. [Expected]" + CommonValues.ADMIN_URL +" [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value");
		}
		if (!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").contentEquals("remark")) {
			failMsg = failMsg + "\n9. saved partners remark. [Expected]remark [Actual]"
					+ driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 26. 파트너 수정. 필수값 삭제 시도
	@Test(priority = 26, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_mod2() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		
		// click submit
		driver.findElement(By.xpath(XPATH_PARTNER_SUBMIT_BTN)).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME_ERROR)).getText().contentEquals(MSG_PARTNER_NAME_EMPTY)) {
			failMsg = "1. partners name error(empty) [Expected]" + MSG_PARTNER_NAME_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO_ERROR)).getText().contentEquals(MSG_PARTNER_CEO_EMPTY)) {
			failMsg = failMsg + "\n2. partners ceo error(empty) [Expected]" + MSG_PARTNER_CEO_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 27. 파트너 수정. invalid1
	@Test(priority = 27, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_mod_invalid() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(specialStr_no + "name");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").contentEquals("name")) {
			failMsg = "1. name input error [Expected]name" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(specialStr_no + "phone");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n2. phone input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(specialStr_no + "ceo");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").contentEquals("ceo")) {
			failMsg = failMsg + "\n3. ceo input error [Expected]ceo" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)));
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(specialStr_no + "type");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").contentEquals("type")) {
			failMsg = failMsg + "\n4. businessType input error [Expected]type" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(specialStr_no + "number");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "\n5. businessNo input error [Expected]123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(specialStr_no + "address");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").contentEquals("address")) {
			failMsg = failMsg + "\n6. address input error [Expected]address" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(specialStr_no + "homepage");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").contentEquals(specialStr_no + "homepage")) {
			failMsg = failMsg + "\n7. homepage input error [Expected]" + (specialStr_no + "homepage" ) 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value");
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_REMARK)));
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(specialStr_no + "remark");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").contentEquals("remark")) {
			failMsg = failMsg + "\n8. remark input error [Expected]remark" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 28. 파트너 수정. invalid2
	@Test(priority = 28, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_mod_invalid2() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").length() > 20) {
			failMsg = "1. name input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(CommonValues.NUMBER_20 + "111");
		if(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. phone input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(CommonValues.CHARACTER_20 + "ceo");
		if(driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. ceo input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)));
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(CommonValues.CHARACTER_20 + "type");
		if(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n4. businessType input length [Expected]20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(CommonValues.NUMBER_20 + CommonValues.NUMBER_20 + CommonValues.NUMBER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n5. businessNo input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n6. address input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").length();
		}
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20 + CommonValues.CHARACTER_20);
		if(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").length() > 50) {
			failMsg = failMsg + "\n7. homepage input length [Expected]50" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").length();
		}
		
		char[] chars = new char[600];
		Arrays.fill(chars, 'a');
		String longchar = new String(chars);
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(longchar);
		if(driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").length() > 500) {
			failMsg = failMsg + "\n8. remark input length [Expected]500" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").length();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 29. 파트너  삭제. delete1
	@Test(priority = 29, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_delete() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)).click();
		Thread.sleep(500);
		
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		if(!alertText.contentEquals(MSG_PARTNER_DELETE_ALERT)) {
			failMsg = "1. delete alert message. [Expected]" + MSG_PARTNER_DELETE_ALERT + " [Actual]" + alertText;
		}
		alert.dismiss();
		
		driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)).click();
		Thread.sleep(500);
		alert.accept();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n2. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_DELETE_SUCCESS)) {
				failMsg = failMsg + "\n3. save toast message. [Expected]" + MSG_PARTNER_DELETE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
			partnerID = "";
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			failMsg = failMsg + "\n4. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner1)) {
				failMsg = failMsg + "\n5. fail to delete partner";
				Thread.sleep(500);
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 30. 파트너 등록. valid2
	@Test(priority = 30, enabled = true)
	public void addpartner_valid2() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_REGISTER_BTN)).click();
		Thread.sleep(500);
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT);
			Thread.sleep(500);
		}

		CommonValues comm = new CommonValues();
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_NAME)).sendKeys(add_partner2);
		
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).sendKeys("222222");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_CEO)));
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_CEO)).sendKeys(add_partner2 + "ceo");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_TYPE)));
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).sendKeys(add_partner2 + "type");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)));
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).sendKeys("222222");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).sendKeys(add_partner2 + "address");
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)));
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).sendKeys(CommonValues.ADMIN_URL);
		
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_REMARK)));
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).sendKeys(add_partner2 + "remark");
		
		// click submit
		driver.findElement(By.xpath(XPATH_PARTNER_SUBMIT_BTN)).click();
		Thread.sleep(1000);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n2. save toast message. [Expected]" + MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner2)) {
				rows.get(i).click();
				Thread.sleep(500);
				break;
			}
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT)) {
			failMsg = failMsg + "\n4. cannot find saved partners in list. current url : " + driver.getCurrentUrl();
		} else {
			partnerID = driver.getCurrentUrl().replace(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT, "");
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value").contentEquals(add_partner2)) {
				failMsg = failMsg + "\n5. saved partners name. [Expected]" + add_partner1
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_NAME)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value").contentEquals(add_partner2 + "ceo")) {
				failMsg = failMsg + "\n6. saved partners name. [Expected]" + (add_partner1 + "ceo")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_CEO)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value").contentEquals("222222")) {
				failMsg = failMsg + "\n7. saved partners phone. [Expected]222222 [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_PHONE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value").contentEquals(add_partner2 + "type")) {
				failMsg = failMsg + "\n8. saved partners businessType. [Expected]" + (add_partner2 + "type")+ " [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_TYPE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value").contentEquals("222222")) {
				failMsg = failMsg + "\n9. saved partners businessNo. [Expected]222222 [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_BUSINESSNO)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value").contentEquals(add_partner2 + "address")) {
				failMsg = failMsg + "\n9. saved partners address. [Expected]" + (add_partner2 + "address") +" [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_ADDRESS)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value").contentEquals(CommonValues.ADMIN_URL)) {
				failMsg = failMsg + "\n9. saved partners homepage. [Expected]" + CommonValues.ADMIN_URL + " [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_HOMEPAGE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value").contentEquals(add_partner2 + "remark")) {
				failMsg = failMsg + "\n9. saved partners remark. [Expected]" + (add_partner2 + "remark") + " [Actual]" 
						+ driver.findElement(By.xpath(XPATH_PARTNER_REMARK)).getAttribute("value");
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 31. 파트너 관리자  등록. 구성
	@Test(priority = 31, enabled = true)
	public void partner_addUser() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).getAttribute("placeholder").contentEquals("email")) {
			failMsg = "1. name placeholder. [Expected]" + ("email") 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("placeholder").contentEquals("nickname")) {
			failMsg = failMsg + "\n2. nickname placeholder. [Expected]" + ("nickname") 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("placeholder").contentEquals("department")) {
			failMsg = failMsg + "\n3. department placeholder. [Expected]" + ("department") 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("placeholder").contentEquals("phone")) {
			failMsg = failMsg + "\n4. phone placeholder. [Expected]" + ("phone") 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("placeholder");
		}
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("placeholder").contentEquals("jobPosition")) {
			failMsg = failMsg + "\n5. position placeholder. [Expected]" + ("jobPosition") 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("placeholder");
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 0) {
			failMsg = failMsg + "\n5. user count [Expected]" + ("0") 
					+ " [Actual]" + rows.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 32. 파트너 관리자  등록. email
	@Test(priority = 32, enabled = true)
	public void partner_addUser_email() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_PARTNER_USER_EMAIL_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText().contentEquals(MSG_PARTNER_USER_EMAIL_EMPTY)) {
			failMsg = "1. email error message(empty). [Expected]" + MSG_PARTNER_USER_EMAIL_EMPTY
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).sendKeys("invalid srt");
		driver.findElement(By.xpath(XPATH_PARTNER_USER_EMAIL_BTN)).click();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText().contentEquals(MSG_PARTNER_USER_EMAIL_INVALID)) {
			failMsg = failMsg + "\n2. email error message(invalid). [Expected]" + MSG_PARTNER_USER_EMAIL_INVALID
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText();
		}
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).sendKeys(CommonValues.USER_PARTNER_1);
		driver.findElement(By.xpath(XPATH_PARTNER_USER_EMAIL_BTN)).click();
		Thread.sleep(500);
		
		String msg = String.format(MSG_PARTNER_USER_EMAIL_DUPLICATE, CommonValues.USER_PARTNER_1);
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText().contentEquals(msg)) {
			failMsg = failMsg + "\n2. email error message(invalid). [Expected]" + msg
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 33. 파트너 관리자  등록. nickname
	@Test(priority = 33, enabled = true)
	public void partner_addUser_nickname() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME_ERROR)).getText().contentEquals(MSG_PARTNER_USER_NAME_EMPTY)) {
			failMsg = "1. name error message(empty). [Expected]" + MSG_PARTNER_USER_NAME_EMPTY
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(specialStr_no + "name");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").contentEquals("name")) {
			failMsg = failMsg + "\n2. name input error. [Expected]" + "name" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value");
		}
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. name length. [Expected]" + "20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").length();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 34. 파트너 관리자  등록. department
	@Test(priority = 34, enabled = true)
	public void partner_addUser_department() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP_ERROR)).getText().contentEquals(MSG_PARTNER_USER_DEP_EMPTY)) {
			failMsg = "1. department error message(empty). [Expected]" + MSG_PARTNER_USER_DEP_EMPTY
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(specialStr_no + "dep");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").contentEquals("dep")) {
			failMsg = failMsg + "\n2. department input error. [Expected]" + "dep" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value");
		}
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(CommonValues.CHARACTER_20 + "name");
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n3. department length. [Expected]" + "20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").length();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 35. 파트너 관리자  등록. phone
	@Test(priority = 35, enabled = true)
	public void partner_addUser_phone() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(specialStr_no);
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").contentEquals("123456789")) {
			failMsg = failMsg + "1. phone input error. [Expected]" + "123456789" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value");
		}
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(CommonValues.NUMBER_20 + "2222");
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. phone length. [Expected]" + "20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").length();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 36. 파트너 관리자  등록. position
	@Test(priority = 36, enabled = true)
	public void partner_addUser_position() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(specialStr_no + "position");
		if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").contentEquals("position")) {
			failMsg = failMsg + "1. position input error. [Expected]" + "position" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value");
		}
		
		CommonValues comm = new CommonValues();
		comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)));
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).clear();
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(CommonValues.CHARACTER_20 + "position");
		if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").length() > 20) {
			failMsg = failMsg + "\n2. position length. [Expected]" + "20" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").length();
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 37. 파트너 관리자  등록. save valid
	@Test(priority = 37, enabled = true)
	public void partner_addUser_valid1() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).sendKeys(add_partner2_user1 + "@rsupport.com");
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_PARTNER_USER_EMAIL_BTN)).click();
		Thread.sleep(500);
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_USER_EMAIL_VALID)) {
				failMsg = failMsg + "\n2. valid email toast message. [Expected]" + MSG_PARTNER_USER_EMAIL_VALID 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}

		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(add_partner2_user1);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(add_partner2_user1 +"dep");
		Thread.sleep(2000);
	
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
		Thread.sleep(1000);
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n3. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n4. save user toast message. [Expected]" + MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		Thread.sleep(2000);
		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			if(!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText().contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n6. added user name. [Expected]" + add_partner2_user1
						+ " [Actual]" + rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).getAttribute("value").contentEquals(add_partner2_user1 + "@rsupport.com")) {
				failMsg = failMsg + "\n7. added user email(popup). [Expected]" + (add_partner2_user1 + "@rsupport.com")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n8. added user name(popup). [Expected]" + add_partner2_user1
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").contentEquals(add_partner2_user1 + "dep")) {
				failMsg = failMsg + "\n9. added user department(popup). [Expected]" + (add_partner2_user1 + "dep")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n10. added user phone(popup). [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").isEmpty()) {
				failMsg = failMsg + "\n11. added user jobPosition(popup). [Expected]" + "empty"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value");
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 38. 파트너 관리자  등록. 수정취소
	@Test(priority = 38, enabled = true)
	public void partner_addUser_mod1() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			if(!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText().contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n2. added user name. [Expected]" + add_partner2_user1
						+ " [Actual]" + rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).isEnabled()) {
				failMsg = failMsg + "\n3. added user email(popup) is enabled.";
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys("mod");
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if (rows.size() != 1) {
			failMsg = failMsg + "\n4. user count [Expected]" + ("1") + " [Actual]" + rows.size();
		} else {
			if (!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText()
					.contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n5. user name. [Expected]" + add_partner2_user1 + " [Actual]"
						+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 39. 파트너 관리자  등록. 수정완료
	@Test(priority = 39, enabled = true)
	public void partner_addUser_mod2() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			if(!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText().contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n2. added user name. [Expected]" + add_partner2_user1
						+ " [Actual]" + rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).isEnabled()) {
				failMsg = failMsg + "\n3. added user email(popup) is enabled.";
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys("mod");
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys("mod");
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys("111111");
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys("position");
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
			Thread.sleep(500);
		}
		
		rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if (rows.size() != 1) {
			failMsg = failMsg + "\n4. user count [Expected]" + ("1") + " [Actual]" + rows.size();
		} else {
			if (!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText()
					.contentEquals(add_partner2_user1 + "mod")) {
				failMsg = failMsg + "\n5. user name. [Expected]" + add_partner2_user1 + "mod" + " [Actual]"
						+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			if (!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[4]")).getText()
					.contentEquals("dep")) {
				failMsg = failMsg + "\n6. user department. [Expected]" + "depmod" + " [Actual]"
						+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[4]")).getText();
			}
			if (!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[5]")).getText()
					.contentEquals("111111")) {
				failMsg = failMsg + "\n7. user phone. [Expected]" + "111111" + " [Actual]"
						+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[5]")).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 40. 파트너 관리자  등록. 수정-name
	@Test(priority = 40, enabled = true)
	public void partner_addUser_mod_name() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			if(!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText().contentEquals(add_partner2_user1 + "mod")) {
				failMsg = failMsg + "\n2. added user name. [Expected]" + add_partner2_user1 + "mod"
						+ " [Actual]" + rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			CommonValues comm = new CommonValues();
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME_ERROR)).getText().contentEquals(MSG_PARTNER_USER_NAME_EMPTY)) {
				failMsg = failMsg + "\n3. user name error msg(empty). [Expected]" + MSG_PARTNER_USER_NAME_EMPTY
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME_ERROR)).getText();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(specialStr_no + "name");
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").contentEquals("name")) {
				failMsg = failMsg + "\n2. name input error. [Expected]" + "name" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value");
			}
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(CommonValues.CHARACTER_20 + "name");
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").length() > 20) {
				failMsg = failMsg + "\n3. name length. [Expected]" + "20" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").length();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 41. 파트너 관리자  등록. 수정-department
	@Test(priority = 41, enabled = true)
	public void partner_addUser_mod_dep() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			CommonValues comm = new CommonValues();
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP_ERROR)).getText().contentEquals(MSG_PARTNER_USER_DEP_EMPTY)) {
				failMsg = failMsg + "\n2. user dep error msg(empty). [Expected]" + MSG_PARTNER_NAME_EMPTY
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP_ERROR)).getText();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(specialStr_no + "dep");
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").contentEquals("dep")) {
				failMsg = failMsg + "\n3. dep input error. [Expected]" + "dep" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value");
			}
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(CommonValues.CHARACTER_20 + "dep");
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").length() > 20) {
				failMsg = failMsg + "\n4. dep length. [Expected]" + "20" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").length();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 42. 파트너 관리자  등록. 수정-phone
	@Test(priority = 42, enabled = true)
	public void partner_addUser_mod_phone() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			CommonValues comm = new CommonValues();
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(Keys.BACK_SPACE);
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(specialStr_no);
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").contentEquals("123456789")) {
				failMsg = failMsg + "\n2. phone input error. [Expected]" + "123456789" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value");
			}
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys(CommonValues.NUMBER_20 + "2222");
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").length() > 20) {
				failMsg = failMsg + "\n3. phone length. [Expected]" + "20" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").length();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 43. 파트너 관리자  등록. 수정-position
	@Test(priority = 43, enabled = true)
	public void partner_addUser_mod_position() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			CommonValues comm = new CommonValues();
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(Keys.BACK_SPACE);
			
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(specialStr_no + "position");
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").contentEquals("position")) {
				failMsg = failMsg + "\n2. position input error. [Expected]" + "position" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value");
			}
			
			comm.selectAll(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)));
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(Keys.BACK_SPACE);
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).clear();
			driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys(CommonValues.CHARACTER_20 + "position");
			if(driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").length() > 20) {
				failMsg = failMsg + "\n3. position length. [Expected]" + "20" 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").length();
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 44. 파트너 삭제 시도. 관리자가 있는 파트너
	@Test(priority = 44, enabled = true)
	public void partner_delete_withuser() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)));

		driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)).click();
		Thread.sleep(500);
		
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		if(!alertText.contentEquals(MSG_PARTNER_DELETE_ALERT)) {
			failMsg = "1. delete alert message. [Expected]" + MSG_PARTNER_DELETE_ALERT + " [Actual]" + alertText;
		}
		alert.dismiss();
		
		driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)).click();
		Thread.sleep(500);
		alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_ERROR_TOAST))) {
			failMsg = failMsg + "\n2. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ERROR_TOAST)).getText().contentEquals(MSG_PARTNER_DELETE_FAIL)) {
				failMsg = failMsg + "\n3. fail toast message. [Expected]" + MSG_PARTNER_DELETE_FAIL 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ERROR_TOAST)).getText();
			}
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			failMsg = failMsg + "\n4. not editview. current url : " + driver.getCurrentUrl();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_LIST_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		boolean find = false;
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner2)) {
				find = true;
			}
		}
		
		if(!find) {
			failMsg = failMsg + "\n5. can not find partner in list. partnerID : " + partnerID;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 45. 파트너 관리자 삭제
	@Test(priority = 45, enabled = true)
	public void partner_deleteuser() throws Exception {
		String failMsg = "";

		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {

			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_DELETE_BTN)).click();
			Thread.sleep(500);
			
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(!alertText.contentEquals(MSG_PARTNER_DELETE_ALERT)) {
				failMsg = failMsg + "\n3. delete alert message. [Expected]" + MSG_PARTNER_DELETE_ALERT + " [Actual]" + alertText;
			}
			alert.dismiss();
			
			rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_DELETE_BTN)).click();
			Thread.sleep(500);
			alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
				failMsg = failMsg + "\n4. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_DELETE_SUCCESS)) {
					failMsg = failMsg + "\n5. delete toast message. [Expected]" + MSG_PARTNER_DELETE_SUCCESS 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
				}
			}
		}
		
		rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 0) {
			failMsg = failMsg + "\n6. cannot delete user. username : " 
					+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 46. 파트너 관리자 추가2
	@Test(priority = 46, enabled = true)
	public void partner_adduser_valid2() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)));
		
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_BTN)).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_EMAIL)).sendKeys(add_partner2_user1 + "@rsupport.com");
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_PARTNER_USER_EMAIL_BTN)).click();
		Thread.sleep(500);
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = "1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_USER_EMAIL_VALID)) {
				failMsg = failMsg + "\n2. valid email toast message. [Expected]" + MSG_PARTNER_USER_EMAIL_VALID 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}

		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).sendKeys(add_partner2_user1);
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).sendKeys(add_partner2_user1 +"dep");
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).sendKeys("111111");
		driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).sendKeys("position");
		
		Thread.sleep(2000);
	
		driver.findElement(By.xpath(XPATH_PARTNER_POPUP_SAVE_BTN)).click();
		Thread.sleep(1000);
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n3. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_SAVE_SUCCESS)) {
				failMsg = failMsg + "\n4. save user toast message. [Expected]" + MSG_PARTNER_SAVE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
		}
		Thread.sleep(2000);
		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "\n5. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {
			if(!rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText().contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n6. added user name. [Expected]" + add_partner2_user1
						+ " [Actual]" + rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
			}
			
			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).getAttribute("value").contentEquals(add_partner2_user1 + "@rsupport.com")) {
				failMsg = failMsg + "\n7. added user email(popup). [Expected]" + (add_partner2_user1 + "@rsupport.com")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADDED_USER_EMAIL)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value").contentEquals(add_partner2_user1)) {
				failMsg = failMsg + "\n8. added user name(popup). [Expected]" + add_partner2_user1
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_NAME)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value").contentEquals(add_partner2_user1 + "dep")) {
				failMsg = failMsg + "\n9. added user department(popup). [Expected]" + (add_partner2_user1 + "dep")
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_DEP)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value").contentEquals("111111")) {
				failMsg = failMsg + "\n10. added user phone(popup). [Expected]" + "111111"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_PHONE)).getAttribute("value");
			}
			
			if(!driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value").contentEquals("position")) {
				failMsg = failMsg + "\n11. added user jobPosition(popup). [Expected]" + "position"
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_ADD_USER_POSITION)).getAttribute("value");
			}
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_CANCEL_BTN)).click();
			
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 47. 파트너 관리자 삭제2
	@Test(priority = 47, enabled = true)
	public void partner_deleteuser2() throws Exception {
		String failMsg = "";
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM)));

		List<WebElement> rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 1) {
			failMsg = failMsg + "1. user count [Expected]" + ("1") 
					+ " [Actual]" + rows.size();
		} else {

			rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).click();
			Thread.sleep(500);
			
			driver.findElement(By.xpath(XPATH_PARTNER_POPUP_DELETE_BTN)).click();
			Thread.sleep(500);
			
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(500);
			
			if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
				failMsg = failMsg + "\n2. cannot find toast.";
			} else {
				if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_DELETE_SUCCESS)) {
					failMsg = failMsg + "\n3. delete toast message. [Expected]" + MSG_PARTNER_DELETE_SUCCESS 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
				}
			}
		}
		
		rows = driver.findElements(By.xpath(XPATH_PARTNER_USER_LIST_ITEM));
		if(rows.size() != 0) {
			failMsg = failMsg + "\n4. cannot delete user. username : " 
					+ rows.get(0).findElement(By.xpath(XPATH_PARTNER_USER_LIST_ITEM_CELL + "[3]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 50. 파트너  삭제. delete2
	@Test(priority = 50, dependsOnMethods = {"addpartner_valid1"}, enabled = true)
	public void addpartner_delete2() throws Exception {
		String failMsg = "";

		if (!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID)) {
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNEREDIT + partnerID);
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_PARTNER_DELETE_BTN)).click();
		Thread.sleep(500);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(500);
		
		if(!isElementPresent(By.xpath(XPATH_PARTNER_SAVE_TOAST))) {
			failMsg = failMsg + "\n1. cannot find toast.";
		} else {
			if(!driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText().contentEquals(MSG_PARTNER_DELETE_SUCCESS)) {
				failMsg = failMsg + "\n2. delete partner toast message. [Expected]" + MSG_PARTNER_DELETE_SUCCESS 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_PARTNER_SAVE_TOAST)).getText();
			}
			partnerID = "";
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST)) {
			failMsg = failMsg + "\n3. not listview. current url : " + driver.getCurrentUrl();
			driver.get(CommonValues.ADMIN_URL + CommonValues.URL_PARTNERLIST);
			Thread.sleep(500);
		}
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		for(int i = 0 ; i < rows.size() ; i++) {
			if(rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText().contentEquals(add_partner2)) {
				failMsg = failMsg + "\n5. fail to delete partner";
				Thread.sleep(500);
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
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
