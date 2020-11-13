package admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * 8. 세미나 리스트, 날짜 선택 
 * 9. 세미나 아이템 리스트 & 리스트 페이징
 * 10. search  세미나명, 채널명, 게시자명, 발표자닉네임
 * 11. keyword search - invalid
 * 12. search - 속성
 * 13. search - 상태
 * 14. search - 타임존
 * 20. listItem
 * 21. 세미나 정보 화면
 * 
 * 31. 세미나 리스트  엑셀 파일 확인
 */

public class Seminars_sysadmin {
	
	public static String XPATH_SEMINAR_SELECTOR_PROPERTY = "//form[@id='seminar-searchbar']//div[@class='ant-row']/div[1]";
	public static String XPATH_SEMINAR_SELECTOR_STATE = "//form[@id='seminar-searchbar']//div[@class='ant-row']/div[2]";
	public static String XPATH_SEMINAR_SELECTOR_TIMEZONE = "//form[@id='seminar-searchbar']//div[@class='ant-row']/div[3]";
	
	public static String XPATH_SEMINAR_SELECTOR_PROPERTY_ALL = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[1]";
	public static String XPATH_SEMINAR_SELECTOR_PROPERTY_PUBLIC = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[2]";
	public static String XPATH_SEMINAR_SELECTOR_PROPERTY_PRIVATE = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[3]";
	
	public static String XPATH_SEMINAR_SELECTOR_STATE_TEMP = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[2]";
	public static String XPATH_SEMINAR_SELECTOR_STATE_NOTREADY = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[3]";
	public static String XPATH_SEMINAR_SELECTOR_STATE_STANDBY = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[4]";
	public static String XPATH_SEMINAR_SELECTOR_STATE_ONAIR = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[5]";
	public static String XPATH_SEMINAR_SELECTOR_STATE_END = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[6]";
	
	public static String XPATH_SEMINAR_SELECTOR_TIMEZONE_KR = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[2]";
	public static String XPATH_SEMINAR_SELECTOR_TIMEZONE_JP = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[3]";
	public static String XPATH_SEMINAR_SELECTOR_TIMEZONE_US = "//div[@class='ant-select-dropdown ant-select-dropdown-placement-bottomLeft ']//div[@class='rc-virtual-list-holder-inner']/div[4]";
	
	public static String XPATH_SEMINAR_VIEW_TITLE = "//div[@class='__profile__desc']//span";
	public static String XPATH_SEMINAR_VIEW_INFO_DATA = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][%d]/td";
	public static String XPATH_SEMINAR_VIEW_INFO_CHANNEL = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][1]";
	public static String XPATH_SEMINAR_VIEW_INFO_TITLE = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][2]";
	public static String XPATH_SEMINAR_VIEW_INFO_STATUS = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][3]";
	public static String XPATH_SEMINAR_VIEW_INFO_PROPERTY = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][4]";
	public static String XPATH_SEMINAR_VIEW_INFO_TIMEZONE = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][5]";
	public static String XPATH_SEMINAR_VIEW_INFO_EXCPECTEDTIME = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][6]";
	public static String XPATH_SEMINAR_VIEW_INFO_REALTIME = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][7]";
	public static String XPATH_SEMINAR_VIEW_INFO_PRESENTER = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][8]";
	public static String XPATH_SEMINAR_VIEW_INFO_EXCUTETIME = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][9]";
	public static String XPATH_SEMINAR_VIEW_INFO_AUTHOR = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][10]";
	public static String XPATH_SEMINAR_VIEW_INFO_POSTDATE = "//div[@class='ant-descriptions-view']//tr[@class='ant-descriptions-row'][11]";
	
	
	
	public static String PUBLIC = "PUBLIC";
	public static String PRIVATE = "PRIVATE";
	public static String TEMP = "TEMP";
	public static String NOTREADY = "NOT_READY";
	public static String STANDBY = "READY";
	public static String ONAIR = "ONAIR";
	public static String END = "END";
	public static String TIMEZONE_KR = "Asia/Seoul";
	public static String TIMEZONE_JP = "Asia/Tokyo";
	public static String TIMEZONE_US = "US/Eastern";
	
	
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
		comm.logintadmin(driver, CommonValues.USER_SYSADMIN);
		
		System.out.println("End BeforeTest!!!");
	}
	
	// 8. 세미나 리스트, 날짜 선택 
	@Test(priority = 8, enabled = true)
	public void search_date() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARLIST);
		Thread.sleep(500);
		
		Date time = new Date();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);

		//defalut checked button
		if(!driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[4]//input")).isSelected()) {
			failMsg = "1. defalut checked button is not 'this month'.";
		}
		
		//3months
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		Calendar cal_startday = Calendar.getInstance();
		cal_startday.setTime(time);
		cal_startday.add(Calendar.MONTH, -3);
		CommonValues comm = new CommonValues();
		String ret = comm.checkSearchedItemInRow(2, driver, cal_startday, cal_today);
		if(!ret.isEmpty())
			failMsg = failMsg + ret;
		
		//all
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[1]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		cal_startday = Calendar.getInstance();
		cal_startday.setTime(time);
		cal_startday.set(Calendar.YEAR, 1990);
		
		ret = comm.checkSearchedItemInRow(3, driver, cal_startday, cal_today);
		if(!ret.isEmpty())
			failMsg = failMsg + ret;
		
		//last month
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[3]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		cal_startday = Calendar.getInstance();
		cal_startday.setTime(time);
		cal_startday.add(Calendar.MONTH, -1);
		cal_startday.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar cal_temp = (Calendar)cal_startday.clone();
		cal_temp.set(Calendar.DAY_OF_MONTH, cal_temp.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		ret = comm.checkSearchedItemInRow(4, driver, cal_startday, cal_temp);
		if(!ret.isEmpty())
			failMsg = failMsg + ret;
		
		
		//this month
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[4]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		cal_startday = Calendar.getInstance();
		cal_startday.setTime(time);
		cal_startday.set(Calendar.DAY_OF_MONTH, 1);
		
		ret = comm.checkSearchedItemInRow(5, driver, cal_startday, cal_today);
		if(!ret.isEmpty())
			failMsg = failMsg + ret;
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 9. 세미나 아이템 리스트 & 리스트 페이징
	@Test(priority = 9, enabled = true)
	public void seminarList() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		CommonValues comm = new CommonValues();
		comm.setCalender(driver);
		Thread.sleep(1000);

		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));

		//기본 30rows
		if (rows.size() != 30) {
			failMsg = "1. default seminar list rows [Expected]30 [Actual]" + rows.size();
		} 
		//전체 세미나 갯수 확인
		int totalCount = Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""));
		
		List<WebElement> paging = driver.findElements(By.xpath(CommonValues.XPATH_LIST_PAGING));

		// 세미나갯수/30(기본 row) = 마지막페이지 번호 -1과 동일
		String lastP = paging.get(paging.size()-2).findElement(By.xpath("./a")).getText();
		if(Integer.parseInt(lastP) -1 != totalCount/30) {
			failMsg = failMsg + "\n2. list paging error. paging count [Expected]" + totalCount/30 + " [Actual]" + (Integer.parseInt(lastP)-1);
		}
	
		//다음페이지 클릭
		paging.get(paging.size()-1).click();
		Thread.sleep(500);
		
		//2번 활성화 되어 있는지 확인
		if(!paging.get(2).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n3. 2nd page is not actived";
		}
		
		//이전페이지 클릭
		paging.get(0).click();
		Thread.sleep(500);
		
		//1번 활성화 되어 있는지 확인
		if(!paging.get(1).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n4. 1st page is not actived";
		}
		
		//3번 클릭
		paging.get(3).click();
		Thread.sleep(500);
		
		//3번 활성화 되어 있는지 확인
		if(!paging.get(3).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n5. 3rd page is not actived";
		}
		
		//마지막 페이지 클릭
		paging.get(paging.size() - 2).click();
		Thread.sleep(500);
		
		int totalCount2 = Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""));
		
		if(totalCount != totalCount2) {
			failMsg = failMsg + "\n6. last page check. total count error [Expected]" + totalCount + " [Actual]" + totalCount2;
		}
		
		//click 50rows
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_ROWS +"[2]")).click();
		Thread.sleep(500);
		
		paging = driver.findElements(By.xpath(CommonValues.XPATH_LIST_PAGING));
		if(paging.size() != 0) {
			// 세미나갯수/50(기본 row) = 마지막페이지 번호 -1과 동일
			lastP = paging.get(paging.size()-2).findElement(By.xpath("./a")).getText();
			if(Integer.parseInt(lastP) -1 != totalCount/50) {
				failMsg = failMsg + "\n7-1. shown 50 rows.  paging count [Expected]" + totalCount/50 + " [Actual]" + (Integer.parseInt(lastP)-1);
			}			
		} else {
			failMsg = failMsg + "\n7-0. cannot find paging element.";
		}
		
		if(totalCount != Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""))) {
			failMsg = failMsg + "\n7-2. shown 50 rows. total count error  [Expected]" + totalCount 
					+ " [Actual]" + Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""));
		}
		
		//click 100rows
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_ROWS +"[3]")).click();
		Thread.sleep(500);
		
		paging = driver.findElements(By.xpath(CommonValues.XPATH_LIST_PAGING));
		if(paging.size() != 0) {
			// 세미나갯수/50(기본 row) = 마지막페이지 번호 -1과 동일
			lastP = paging.get(paging.size()-2).findElement(By.xpath("./a")).getText();
			if(Integer.parseInt(lastP) -1 != totalCount/50) {
				failMsg = failMsg + "\n8-1. shown 100 rows. paging count [Expected]" + totalCount/100 + " [Actual]" + (Integer.parseInt(lastP)-1);
			}	
		} else {
			failMsg = failMsg + "\n8-0. cannot find paging element.";
		}
		
		if(totalCount != Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""))) {
			failMsg = failMsg + "\n8-2. shown 100 rows. total count error  [Expected]" + totalCount 
					+ " [Actual]" + Integer.parseInt(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCHED_ITEM_COUNT)).getText().replaceAll("[^0-9]", ""));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 10. search  세미나명, 채널명, 게시자명, 발표자닉네임
	@Test(priority = 10, enabled = true)
	public void keyworkdSearch() throws Exception {
		String failMsg = "";
		
		//reset
		driver.navigate().refresh();
		Thread.sleep(500);
		
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

		comm.selectAll(driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)));
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_INPUTBOX)).clear();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 12. search - 속성
	@Test(priority = 12, enabled = true)
	public void Search_property() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY_PUBLIC)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = "1. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText().contentEquals(PUBLIC)) {
					failMsg = failMsg + "\n2-" + i + ". searched seminar prpperty. [Expected]" + PUBLIC 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY_PRIVATE)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = failMsg + "\n3. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText().contentEquals(PRIVATE)) {
					failMsg = failMsg + "\n4-" + i + ". searched seminar prpperty. [Expected]" + PRIVATE 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
	
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY_ALL)).click();
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 13. search - 상태
	@Test(priority = 13, enabled = true)
	public void Search_state() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE_TEMP)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = "1. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText().contentEquals(TEMP)) {
					failMsg = failMsg + "\n2-" + i + ". searched seminar state. [Expected]" + TEMP 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE_NOTREADY)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = failMsg + "\n3. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText().contentEquals(NOTREADY)) {
					failMsg = failMsg + "\n4-" + i + ". searched seminar state. [Expected]" + NOTREADY 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE_STANDBY)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 0) {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText().contentEquals(STANDBY)) {
					failMsg = failMsg + "\n5-" + i + ". searched seminar state. [Expected]" + STANDBY 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE_ONAIR)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() != 0) {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText().contentEquals(ONAIR)) {
					failMsg = failMsg + "\n6-" + i + ". searched seminar state. [Expected]" + ONAIR 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE_END)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = failMsg + "\n7. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText().contentEquals(END)) {
					failMsg = failMsg + "\n8-" + i + ". searched seminar state. [Expected]" + END 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_STATE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY_ALL)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 14. search - 타임존
	@Test(priority = 14, enabled = true)
	public void Search_timezone() throws Exception {
		String failMsg = "";
		
		checkListView(driver);
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE_KR)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = "1. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText().contentEquals(TIMEZONE_KR)) {
					failMsg = failMsg + "\n2-" + i + ". searched seminar state. [Expected]" + TIMEZONE_KR 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE_JP)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = failMsg + "\n3. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText().contentEquals(TIMEZONE_JP)) {
					failMsg = failMsg + "\n4-" + i + ". searched seminar state. [Expected]" + TIMEZONE_JP 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE_US)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		if(rows.size() == 0) {
			failMsg = failMsg + "\n5. searched item error [Expected]more than 1 [Actual]" + rows.size();
		} else {
			for(int i = 0 ; i < rows.size() ; i++) {
				if(!rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText().contentEquals(TIMEZONE_US)) {
					failMsg = failMsg + "\n6-" + i + ". searched seminar state. [Expected]" + TIMEZONE_US 
							+ " [Actual]" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText()
							+ ", seminar title:" + rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
				}
			}
		}
		
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_TIMEZONE)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_SEMINAR_SELECTOR_PROPERTY_ALL)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 20. listItem
	@Test(priority = 20, enabled = true)
	public void listItem() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);

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

	// 21. 세미나 정보 화면
	@Test(priority = 21, enabled = true)
	public void seminarInfoView() throws Exception {
		String failMsg = "";	
		
		checkListView(driver);

		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		
		//1번째 줄 세미나 정보 데이터 
		String postDate = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[1]")).getText();
		String channelName = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[2]")).getText();
		String title = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[3]")).getText();
		String author = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[4]")).getText();
		String property = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[5]")).getText();
		String timezone = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[6]")).getText();
		String expectedTime = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[7]")).getText();
		String realTime = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[8]")).getText();
		String excuteTime = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[9]")).getText();
		String status = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[10]")).getText();
		String attendee = rows.get(0).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[11]")).getText();
		
		rows.get(0).click();
		Thread.sleep(500);
		
		if(!driver.getCurrentUrl().contains(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARINFO)) {
			failMsg = "0. not user info view. current url : " + driver.getCurrentUrl();
		} else {
			
			//title
			if(!driver.findElement(By.xpath(XPATH_SEMINAR_VIEW_TITLE)).getText().contentEquals(title)) {
				failMsg = failMsg + "\n1. seminar info :  title [Expected]" + title 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_SEMINAR_VIEW_TITLE)).getText();
			}
			
			//channel
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 1))).getText().contentEquals(channelName)) {
				failMsg = failMsg + "\n2. seminar info :  channel [Expected]" + channelName 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 1))).getText();
			}
			
			//status
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 3))).getText().contains(status)) {
				failMsg = failMsg + "\n3. seminar info :  status [Expected]" + status 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 3))).getText();
			}
			
			//property
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 4))).getText().contentEquals(property)) {
				failMsg = failMsg + "\n4. seminar info :  property [Expected]" + property 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 4))).getText();
			}
			
			//timezone
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 5))).getText().contentEquals(timezone)) {
				failMsg = failMsg + "\n5. seminar info :  timezone [Expected]" + timezone 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 5))).getText();
			}
			
			//expectedTime
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 6))).getText().contentEquals(expectedTime)) {
				failMsg = failMsg + "\n6. seminar info :  expectedTime [Expected]" + expectedTime 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 6))).getText();
			}
			
			//realTime
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 7))).getText().contentEquals(realTime)) {
				failMsg = failMsg + "\n7. seminar info :  realTime [Expected]" + realTime 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 7))).getText();
			}
			
			//excuteTime
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 9))).getText().contentEquals(excuteTime)) {
				failMsg = failMsg + "\n8. seminar info :  excuteTime [Expected]" + excuteTime 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 9))).getText();
			}
			
			//author
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 10))).getText().contentEquals(author)) {
				failMsg = failMsg + "\n9. seminar info :  author [Expected]" + author 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 10))).getText();
			}
			
			//postDate
			if(!driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 11))).getText().contentEquals(postDate)) {
				failMsg = failMsg + "\n10. seminar info :  postDate [Expected]" + postDate 
						+ " [Actual]" + driver.findElement(By.xpath(String.format(XPATH_SEMINAR_VIEW_INFO_DATA, 11))).getText();
			}
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 31. 세미나 리스트  엑셀 파일 확인
	@Test(priority = 31, enabled = true)
	public void seminarListExcel() throws Exception {
		String failMsg = "";	
		
		driver.get(CommonValues.ADMIN_URL + CommonValues.URL_SEMINARLIST);
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_DATERADIO + "[3]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath(CommonValues.XPATH_LIST_SEARCH_BTN)).click();
		Thread.sleep(500);
		
		List<WebElement> rows = driver.findElements(By.xpath(CommonValues.XPATH_LIST_ROW_ITEM));
		int rowSize = rows.size();
		int columnSize = driver.findElements(By.xpath("//th[@class='ant-table-cell']")).size();
		
		driver.findElement(By.xpath("//button[@class='ant-btn ant-btn-sub ant-btn-sm']")).click();
		Thread.sleep(3000);
		
		CommonValues comm = new CommonValues();
		String[][] excelData = readExcelFile(comm.Excelpath("seminar"));

		if(rowSize <= excelData.length && columnSize <= excelData[0].length) {
			for(int i = 0 ; i < rowSize ; i++ ) {
				for(int j = 0 ; j < columnSize ; j++ ) {
					String excelCol = "";
					
					String rowData = rows.get(i).findElement(By.xpath(CommonValues.XPATH_LIST_ROW_CELL + "[" + (j+1) + "]")).getText();
					if(j == 0) {
						excelCol = excelData[i][j].replace(".", "/");
					} else if (j == 6 || j == 7) {
						excelCol = excelData[i][j+2].replace(".", "/");
						
						String[] date = rowData.split(" ");
						rowData = String.format("%s %s %s %s", date[0], date[1], date[2], date[4]);
						
					} else if (j == 8) {
						excelCol = excelData[i][j+2];
						if (excelCol.isEmpty()) {
							excelCol = "00:00:00";
						}	
					} else if(j >= 2 && j < 4) {
						excelCol = excelData[i][j + 1];
					} else if (j >= 4) {
						excelCol = excelData[i][j + 2];
					} else {
						excelCol = excelData[i][j];
					}
					excelCol = checkExcelData(excelCol);
					
					if(!rowData.contentEquals(excelCol)) {
						failMsg = failMsg + "\n" + i + "-" + j +". excelData. [Expected]" 
								+ rowData
								+ " [Actual]" + excelCol;
					}
				}
			}
		} else {
			failMsg = "0. excelData size error. [Expeced]" + rowSize + "," + columnSize 
					+ " [Actual]" + excelData.length + "," + excelData[0].length;
		}
		
		deleteExcelFile(comm.Excelpath("seminar"));
		
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
	
	private String checkExcelData(String value) {

		value = value.replace("비공개", "PRIVATE");
		value = value.replace("공개", "PUBLIC");
		
		value = value.replace("종료", "END");
		value = value.replace("스탠바이", "READY");
		value = value.replace("예정", "NOT_READY");
		value = value.replace("온에어", "ONAIR");

		return value;
	}
	
	private String[][] readExcelFile(String filepath) throws Exception {

		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook testDataWorkBook = new XSSFWorkbook(inputStream);
		Sheet testDataSheet = testDataWorkBook.getSheetAt(0);

		int rowCount = testDataSheet.getLastRowNum();
		int cells = testDataSheet.getRow(0).getPhysicalNumberOfCells();

		DataFormatter formatter = new DataFormatter();

		String[][] data = new String[rowCount][cells];

		for (int i = 1; i <  rowCount; i++) {
			Row row = testDataSheet.getRow(i);
			for (int j = 1; j < cells; j++) {
				
				Cell cell = row.getCell(j);
				String a = formatter.formatCellValue(cell);

				data[i-1][j-1] = a;
			}
		}
		testDataWorkBook.close();
		return data;
	}

	private static void deleteExcelFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("File is delete");
	        } else {
	            
	            System.out.println("File is not exist");  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
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
