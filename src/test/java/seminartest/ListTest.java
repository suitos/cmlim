package seminartest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* ListTest
 * user : rsrsup2
 * 
 * part1
 * 1. 리스트에서 임시저장 세미나 게시하기
 * 2. 리스트에서 게시완료 세미나. 아이콘 배치 확인. 링크획득
 * 3. 리스트에서 게시완료 세미나 수정 들어가기
 * 4. 리스트에서 게시완료 세미나 초대하기 들어가기
 * 5. 리스트에서 게시완료 세미나 연습하기 (유저는 게시자이자 발표자)
 * 6. 리스트에서 게시완료 세미나 삭제
 * 
 * part2(standby)
 * 11. ready 세미나 생성 후 링크 획득 .아이콘 배치 확인.
 * 12. 리스트에서 ready 세미나 초대하기 들어가기
 * 
 * part3
 * 21. 종료 세미나 생성. 아이콘 배치 확인. 수정 클릭
 * 22. 리스트에서 종료 세미나 통게보기 들어가기
 * 
 * part3(미작성)
 * 31. 운영자인 게시자가 리스트의 게시완료 세미나 연습하기 입장시도
 * 32. 발표자, 운영자가 아닌 게시자가 리스트의 게시완료 연습하기 입장시도
 * 
 * part4 : 비공개세미나
 * 41. 비공개 세미나 임시저장 상태 확인(일반). 수정이동
 * 42. 비공개 세미나 임시저장(일반). 삭제
 * 43. 비공개 세미나 임시저장(일반). 게시하기
 * 44. 비공개 세미나 게시완료세미나(일반). 버튼 배치 확인. 수정이동
 * 45. 비공개 세미나 게시완료세미나(일반). 초대하기
 * 46. 비공개 세미나 게시완료세미나(일반). 연습하기
 * 47. 비공개 세미나 게시완료세미나(일반). 삭제
 * 
 * 50. 비공개 세미나 standby세미나. 상태 확인. 초대하기
 * 51. 비공개 세미나 onair세미나. 상태 확인. 초대하기
 * 55. 비공개 세미나 종료세미나. 상태 확인. 통계보기, 수정하기
 * 
 * 61. 비공개 세미나 임시저장 상태 확인(시크릿). 수정이동
 * 62. 비공개 세미나 임시저장(시크릿). 삭제
 * 63. 비공개 세미나 임시저장(시크릿). 게시하기
 * 64. 비공개 세미나 게시완료세미나(시크릿). 버튼 배치 확인. 수정이동
 * 65. 비공개 세미나 게시완료세미나(시크릿). 초대하기
 * 66. 비공개 세미나 게시완료세미나(시크릿). 연습하기
 * 67. 비공개 세미나 게시완료세미나(시크릿). 삭제
 * 
 * 70. 비공개 세미나 standby세미나(시크릿). 상태 확인. 초대하기
 * 71. 비공개 세미나 onair세미나(시크릿). 상태 확인. 초대하기 
 * 75. 비공개 세미나 종료세미나(시크릿). 상태 확인. 통계보기, 수정하기
 */

public class ListTest {
	public static String XPATH_LIST_TAB_SAVED = "//ul[@class='seminarList__category__list']/li[2]"; 
	public static String XPATH_LIST_TAB_REGISTERED = "//ul[@class='seminarList__category__list']/li[3]"; 
	public static String XPATH_LIST_TAB_PAST = "//ul[@class='seminarList__category__list']/li[4]"; 
	
	public static WebDriver driver;
	public static WebDriver driver_guest;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public String seminarID = "";
	public String seminarTitle = "";
	public String userName = "rsrsup2";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=en_US");

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", driver_guest);
		driver.get(CommonValues.SERVER_URL);

		System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority = 0)
	public void loginseminar() throws Exception {
		CommonValues comm = new CommonValues();
		//comm.loginseminar(driver, CommonValues.USEREMAIL_PRES);
		comm.loginseminar(driver, userName + "@gmail.com");
	}

	//1. 임시저장 세미나 게시하기
	@Test(priority = 1, enabled = true)
	public void draftseminar_post() throws Exception {
		String failMsg = "";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		System.out.println("seminar title : " + seminarTitle  + ", seminarID : " + seminarID);
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			//click post
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			//takescreenshot(we, "draft.png");
			if(buttonTest(we, "post", true)) {
				Thread.sleep(500);
				String popupmsg = String.format(CommonValues.MSG_POST_SEMINAR, userName);
				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(popupmsg)){
					failMsg = failMsg + "\n 2. post popup msg error : [Expected]" + popupmsg
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				Thread.sleep(500);
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				Thread.sleep(100);
				
				buttonTest(we, "post", true);
				Thread.sleep(500);
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(1000);
				
			} else {
				failMsg = failMsg + "\n 0. not find post button";
			}

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	//2. 리스트에서 게시완료 세미나 링크획득 
	@Test(priority = 2, dependsOnMethods = {"draftseminar_post"}, alwaysRun = true, enabled = true)
	public void registeredseminar_link() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if(!driver.getCurrentUrl().contentEquals(listView)) {
			driver.get(listView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
			closeBenner(driver);
		}
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(1000);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
	
			//버튼 확인
			if(!buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. can not find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. can not find edit icon";
			}
			if(!buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. can not find trash icon";
			}
			
			if(!buttonTest(we, "rehearsal", false)) {
				failMsg = failMsg + "\n1-4. can not find 2type button(rehearsal, invite button)";
			}
			
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-5. can not find 2type button(rehearsal, invite button)";
			}
			
			// click link
			if(buttonTest(we, "link", true)) {
				Thread.sleep(500);
				
				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_COPY_SEMINAR_LINK)){
					failMsg = failMsg + "\n 2. post popup msg error : [Expected]" + CommonValues.MSG_COPY_SEMINAR_LINK
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				//click confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				
				String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				String seminarlink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
				
				CommonValues comm = new CommonValues();
				if(comm.checkShotURL(driver, clipboardtxt, seminarlink)) {
					failMsg = failMsg + "\n 3. clipboard data error : [Expected]" + seminarlink+ " [Actual]" +clipboardtxt;
				}
			}
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//3. 리스트에서 게시완료 세미나 수정 들어가기
	@Test(priority = 3, dependsOnMethods = {"registeredseminar_link"}, alwaysRun = true, enabled = true)
	public void registeredseminar_edit() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			if(buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 2. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				}
			}else {
				failMsg = failMsg + "\n fail to click edit icon";
			}
			
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//4. 리스트에서 게시완료 세미나 초대하기 들어가기
	@Test(priority = 4, dependsOnMethods = {"registeredseminar_edit"}, alwaysRun = true,enabled = true)
	public void registeredseminar_invite() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			//click invite
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(1000);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	//5. 리스트에서 게시완료 세미나 연습하기
	@Test(priority = 5, dependsOnMethods = {"registeredseminar_invite"}, alwaysRun = true, enabled = true)
	public void registeredseminar_rehearsal() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {

			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click rehearsal
			if (buttonTest(we, "rehearsal", true)) {
				Thread.sleep(500);

				ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
				if(tabs2.size() == 2) {
					driver.switchTo().window(tabs2.get(1));
				    
					Thread.sleep(1000);
					String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
					//check room uri
					if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
				    {
						failMsg = failMsg + "\n2. fail to enter Seminar rehearsal: " + driver.getCurrentUrl();
				    } 
					// close 1tab
					driver.close();
					driver.switchTo().window(tabs2.get(0));

				} else {
					failMsg = failMsg + "\n3. not opened new tab : " + driver.getCurrentUrl();
				}

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//6. 리스트에서 게시완료 세미나 삭제
	@Test(priority = 6, enabled = true)
	public void registeredseminar_delete() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click trash icon 
			if (buttonTest(we, "trash", true)) {
				Thread.sleep(500);

				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
					failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				
				buttonTest(we, "trash", true);
				Thread.sleep(500);
				
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = "3. find seminar after delete";
		} 
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	//11. ready 세미나 생성. 아이콘 확인. 링크아이콘으로 클립보드 획득
	@Test(priority = 11, enabled = true)
	public void readyseminar_link() throws Exception {
		String failMsg = "";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		//setSeminar(seminarTitle, true);
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, true);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// click post
		comm.postSeminar(driver, seminarID);
		Thread.sleep(500);

		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if(!driver.getCurrentUrl().contentEquals(listView)) {
			driver.get(listView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
			closeBenner(driver);
		}

		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
			
			//버튼 확인
			if(!buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. can not find link icon";
			}
			if(buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-4. can not find invite button.";
			}
			
			// click link icon 
			if (buttonTest(we, "link", true)) {
				Thread.sleep(500);
				
				if (!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText()
						.contentEquals(CommonValues.MSG_COPY_SEMINAR_LINK)) {
					failMsg = failMsg + "\n 2. post popup msg error : [Expected]" + CommonValues.MSG_COPY_SEMINAR_LINK
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				// click confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();

				String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
						.getData(DataFlavor.stringFlavor);
				String seminarlink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
				
				if(comm.checkShotURL(driver, clipboardtxt, seminarlink)) {
					failMsg = failMsg + "\n 3. clipboard data error : [Expected]" + seminarlink+ " [Actual]" +clipboardtxt;
				}
				
			} else {
				failMsg = failMsg + "\n4. fail to click link icon.";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	
	
	//12. 리스트에서 ready 세미나 초대하기 들어가기
	@Test(priority = 12, enabled = true) 
	public void readyseminar_invite() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registered tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registered tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

	
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite ";
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	

	//14. ready 세미나 수정 시도  (세미나 상세화면에서)
	@Test(priority = 14, enabled = true, dependsOnMethods = {"readyseminar_invite"}, alwaysRun = true)
	public void readyseminar_tryedit() throws Exception {
		String failMsg = "";
		
		String editUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;
		driver.get(editUrl);
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_READYSEMINAR_EDIT)) {
			failMsg = "1. popup msg error : [Expected]" + CommonValues.MSG_READYSEMINAR_EDIT
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		Thread.sleep(500);
		String listUrl = CommonValues.SERVER_URL + CommonValues.LIST_URI;

		if (!driver.getCurrentUrl().contains(listUrl)) {
			failMsg = failMsg + "\n 2. no list view after try to edit ready seminar : [Expected]" + listUrl
					+ " [Actual]" + driver.getCurrentUrl();
		}

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//21. 종료 세미나 생성 후 아이콘 확인. 수정 클릭
	@Test(priority = 21, enabled = true)
	public void pastseminar_edit() throws Exception {
		String failMsg = "";
		
		//go to preview
	    String detailview = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver.get(detailview);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		//click enter
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(1));
	    
		Thread.sleep(1000);
		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		//check room uri
		if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
	    {
			failMsg = "1. fail to join Seminar : " + driver.getCurrentUrl();
	    } {
	    	//close 1tab 
	    	driver.switchTo().window(tabs2.get(0));
			driver.close();
			driver.switchTo().window(tabs2.get(1));
	    }
	    
		// 설정팝업 확인
	    CommonValues comm = new CommonValues();
	    comm.checkSettingpopup(driver);

	    //close seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(1000);
		if(isElementPresent_wd(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n0-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n0-2. cannot find toast (presenter)";
		}
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(500);
			
			//버튼 확인 : 수정만 노출
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. can not find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "statistics", false)) {
				failMsg = failMsg + "\n1-4. can not find statistics button";
			}
			
			// click edit icon 
			if (buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 3. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				} else {
					//check editable tab
					List<WebElement> tabs = driver.findElements(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li"));
					if(tabs.size() != 1) {
						failMsg = failMsg + "\n 4. editable tab size error : " + tabs.size();
					} else {
						if(!tabs.get(0).getText().contentEquals("Detailed information")) {
							failMsg = failMsg + "\n 5. editable tab title error : [Expected]Detailed information [Actual]" 
									+ tabs.get(0).getText();
						}
					}
				}
				
			} else {
				failMsg = failMsg + "\n6. fail to click edit icon.";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//22. 리스트에서 종료 세미나 통게보기 들어가기
	@Test(priority = 22, enabled = true)
	public void pastseminar_statistics() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			//click statistics
			if (buttonTest(we, "statistics", true)) {
				Thread.sleep(500);
				
				String statisticsUrl = CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID;

				if (!driver.getCurrentUrl().contains(statisticsUrl)) {
					failMsg = failMsg + "\n 3. no statistics view after click statistics button : [Expected]" + statisticsUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n4. fail to statistics button.";
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//41. 비공개 세미나 임시저장 상태 확인(일반). 수정이동
	@Test(priority = 41, enabled = true)
	public void privateN_draft() throws Exception {
		String failMsg = "";

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime()) + "_private";
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		//private
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if(!driver.getCurrentUrl().contentEquals(listView)) {
			driver.get(listView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
		}
		
		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(500);
	
			//버튼 확인
			if(!buttonTest(we, "lock", false)) {
				failMsg = failMsg + "\n1-1. can not find lock icon";
			}
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-2. find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-3. can not find edit icon";
			}
			if(!buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-4. can not find trash icon";
			}
			if(!buttonTest(we, "invite-temp", false)) {
				failMsg = failMsg + "\n1-5. can not find invite-temp button";
			}
			if(!buttonTest(we, "post", false)) {
				failMsg = failMsg + "\n1-6. can not find post button";
			}
			
			//click edit
			if(buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 3. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				} 
				
			} else {
				failMsg = failMsg + "\n 0. not click edit button";
			}

		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	//42. 비공개 세미나 임시저장(일반). 삭제
	@Test(priority = 42, enabled = true)
	public void privateN_draftDelete() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {

			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click statistics
			if (buttonTest(we, "trash", true)) {
				Thread.sleep(500);

				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
					failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				
				buttonTest(we, "trash", true);
				Thread.sleep(500);
				
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);
				
				
			} else {
				failMsg = failMsg + "\n4. fail to click trash button.";
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = "3. find seminar after delete";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//43. 비공개 세미나 임시저장(일반). 게시하기
	@Test(priority = 43, enabled = true)
	public void privateN_draftPost() throws Exception {
		String failMsg = "";

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		seminarTitle = format1.format(cal.getTime()) + "_private";
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		//private
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		if(!driver.getCurrentUrl().contentEquals(listView)) {
			driver.get(listView);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
		}

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			//click post
			if(buttonTest(we, "post", true)) {
				Thread.sleep(500);
				String popupmsg = String.format(CommonValues.MSG_POST_SEMINAR, userName);
				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(popupmsg)){
					failMsg = failMsg + "\n 2. post popup msg error : [Expected]" + popupmsg
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				Thread.sleep(500);
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				Thread.sleep(100);
				
				buttonTest(we, "post", true);
				Thread.sleep(500);
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(1000);
				
			} else {
				failMsg = failMsg + "\n 0. not click post button";
			}
		}				
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//44. 비공개 세미나 게시완료세미나(일반). 버튼 배치 확인. 수정이동
	@Test(priority = 44, enabled = true)
	public void privateN_registerdEdit() throws Exception {
		String failMsg = "";

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(500);
	
			//버튼 확인
			if(!buttonTest(we, "lock", false)) {
				failMsg = failMsg + "\n1-1. can not find lock icon";
			}
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-2. find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-3. can not find edit icon";
			}
			if(!buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-4. can not find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-5. can not find 2type button(invite)";
			}
			if(!buttonTest(we, "practice", false)) {
				failMsg = failMsg + "\n1-6. can not find 2type button(practice)";
			}
			
			//click edit
			if(buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 3. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				} 
				
			} else {
				failMsg = failMsg + "\n 0. not click edit button";
			}

		}			
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//45. 비공개 세미나 게시완료세미나(일반). 초대하기
	@Test(priority = 46, enabled = true)
	public void privateN_registerdInvite() throws Exception {
		String failMsg = "";

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			//click invite
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//46. 비공개 세미나 게시완료세미나(일반). 연습하기
	@Test(priority = 46, enabled = true)
	public void privateN_registerdRehearsal() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);

		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {

			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click rehearsal
			if (buttonTest(we, "rehearsal", true)) {
				Thread.sleep(500);

				ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
				if(tabs2.size() == 2) {
					driver.switchTo().window(tabs2.get(1));
				    
					Thread.sleep(1000);
					String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
					//check room uri
					if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
				    {
						failMsg = failMsg + "\n2. fail to enter Seminar rehearsal: " + driver.getCurrentUrl();
				    } 
					// close 1tab
					driver.close();
					driver.switchTo().window(tabs2.get(0));

				} else {
					failMsg = failMsg + "\n3. not opened new tab : " + driver.getCurrentUrl();
				}

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//47. 비공개 세미나 게시완료세미나(일반). 삭제
	@Test(priority = 47, enabled = true)
	public void privateN_registerdDelete() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click trash icon 
			if (buttonTest(we, "trash", true)) {
				Thread.sleep(500);

				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
					failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				
				buttonTest(we, "trash", true);
				Thread.sleep(500);
				
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = "3. find seminar after delete";
		} 
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			

	//50. 비공개 세미나 standby세미나. 상태 확인. 초대하기
	@Test(priority = 50, enabled = true)
	public void privateN_standbyInvite() throws Exception {
		String failMsg = "";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		closeBenner(driver);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		//setSeminar(seminarTitle, true);
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, true);
		
		// private
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		String createViewUri = CommonValues.SERVER_URL + CommonValues.CREATE_URI;
		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// click post
		comm.postSeminar(driver, seminarID);
		Thread.sleep(2000);

		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(1000);
			closeBenner(driver);
		}
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
			
			//버튼 확인, ready 태그
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. find link icon";
			}
			if(buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-4. can not find invite button.";
			}
			if(!buttonTest(we, "standby", false)) {
				failMsg = failMsg + "\n1-5. can not find standby tag.";
			}
			
			// click invite button 
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite ";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	

	//51. 비공개 세미나 onair세미나. 상태 확인. 초대하기
	@Test(priority = 51, enabled = true)
	public void privateN_onairInvite() throws Exception {
		String failMsg = "";
		
		// goto seminar info
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		// click enter(new tab)
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + driver.getCurrentUrl();
			}
		} else {
			driver.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver.close();
			// switch room tab
			driver.switchTo().window(tabs2.get(1));
		}
		
		// 설정팝업 확인
	    CommonValues comm = new CommonValues();
	    comm.checkSettingpopup(driver);
		
		//start seminar
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(1000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
			
			//버튼 확인, ready 태그
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. find link icon";
			}
			if(buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-4. can not find invite button.";
			}
			if(!buttonTest(we, "onair", false)) {
				failMsg = failMsg + "\n1-5. can not find onair tag.";
			}
			
			// click invite button 
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite ";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	
	
	//55. 비공개 세미나 종료세미나. 상태 확인. 통계보기, 수정하기
	@Test(priority = 55, enabled = true)
	public void privateN_past_statistics() throws Exception {
		String failMsg = "";
		
		// goto seminar info
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();",driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		// click enter(new tab)
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + driver.getCurrentUrl();
			}
		} else {
			driver.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver.close();
			// switch room tab
			driver.switchTo().window(tabs2.get(1));
		}

		// 설정팝업 확인
	    CommonValues comm = new CommonValues();
	    comm.checkSettingpopup(driver);
		
		//close seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(2000);
		
		if(isElementPresent_wd(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n0-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n0-2. cannot find toast (presenter)";
		}
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			//click statistics
			if (buttonTest(we, "statistics", true)) {
				Thread.sleep(500);
				
				String statisticsUrl = CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID;

				if (!driver.getCurrentUrl().contains(statisticsUrl)) {
					failMsg = failMsg + "\n 3. no statistics view after click statistics button : [Expected]" + statisticsUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n4. fail to statistics button.";
			}
		}
		
		// go to listview for edit
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 5. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			//click statistics
			if (buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String editview = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(editview)) {
					failMsg = failMsg + "\n 6. no statistics view after click statistics button : [Expected]" + editview
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n7. fail to edit button.";
			}
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//61. 비공개 세미나 임시저장 상태 확인(시크릿). 수정이동
	@Test(priority = 61, enabled = true)
	public void privateS_draft() throws Exception {
		String failMsg = "";

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
		Thread.sleep(1000);
		closeBenner(driver);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime()) + "_private";
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		//private & secret
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='checkbox']")).click();
		if(!driver.findElement(By.xpath("//div[@class='checkbox']//input")).isSelected()) {
			failMsg = "0. not checked secret mode";
		}
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
	
			//버튼 확인
			if(!buttonTest(we, "lock", false)) {
				failMsg = failMsg + "\n1-1. can not find lock icon";
			}
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-2. find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-3. can not find edit icon";
			}
			if(!buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-4. can not find trash icon";
			}
			if(!buttonTest(we, "invite-temp", false)) {
				failMsg = failMsg + "\n1-5. can not find post button";
			}
			if(!buttonTest(we, "post", false)) {
				failMsg = failMsg + "\n1-6. can not find post button";
			}
			
			//click edit
			if(buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 3. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				} 
				
			} else {
				failMsg = failMsg + "\n 0. not click edit button";
			}

		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	//62. 비공개 세미나 임시저장(시크릿). 삭제
	@Test(priority = 62, enabled = true)
	public void privateS_draftDelete() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {

			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click statistics
			if (buttonTest(we, "trash", true)) {
				Thread.sleep(500);

				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
					failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				
				buttonTest(we, "trash", true);
				Thread.sleep(500);
				
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);
				
				
			} else {
				failMsg = failMsg + "\n4. fail to click trash button.";
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = "3. find seminar after delete";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//63. 비공개 세미나 임시저장(secret). 게시하기
	@Test(priority = 63, enabled = true)
	public void privateS_draftPost() throws Exception {
		String failMsg = "";

		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		seminarTitle = format1.format(cal.getTime()) + "_privateS";
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, false);
		
		//private
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='checkbox']")).click();
		if(!driver.findElement(By.xpath("//div[@class='checkbox']//input")).isSelected()) {
			failMsg = "0. not checked secret mode";
		}
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click draft tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_SAVED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			//click post
			if(buttonTest(we, "post", true)) {
				Thread.sleep(500);
				String popupmsg = String.format(CommonValues.MSG_POST_SEMINAR, userName);
				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(popupmsg)){
					failMsg = failMsg + "\n 2. post popup msg error : [Expected]" + popupmsg
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				Thread.sleep(500);
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				Thread.sleep(100);
				
				buttonTest(we, "post", true);
				Thread.sleep(500);
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(1000);
				
			} else {
				failMsg = failMsg + "\n 0. not click post button";
			}
		}				
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//64. 비공개 세미나 게시완료세미나(시크릿). 버튼 배치 확인. 수정이동
	@Test(priority = 64, enabled = true)
	public void privateS_registerdEdit() throws Exception {
		String failMsg = "";

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);

		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab : seminar title : " + seminarTitle;
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(500);
	
			//버튼 확인
			if(!buttonTest(we, "lock", false)) {
				failMsg = failMsg + "\n1-1. can not find lock icon";
			}
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-2. find link icon";
			}
			if(!buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-3. can not find edit icon";
			}
			if(!buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-4. can not find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-5. can not find 2type button(invite)";
			}
			if(!buttonTest(we, "practice", false)) {
				failMsg = failMsg + "\n1-6. can not find 2type button(practice)";
			}
			//click edit
			if(buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String createViewUrl = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(createViewUrl)) {
					failMsg = failMsg + "\n 3. no create view after click edit button : " + driver.getCurrentUrl();
					driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
				} 
				
			} else {
				failMsg = failMsg + "\n 0. not click edit button";
			}

		}			
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//65. 비공개 세미나 게시완료세미나(시크릿). 초대하기
	@Test(priority = 65, enabled = true)
	public void privateS_registerdInvite() throws Exception {
		String failMsg = "";

		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));
			
			//click invite
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//66. 비공개 세미나 게시완료세미나(일반). 연습하기
	@Test(priority = 66, enabled = true)
	public void privateS_registerdRehearsal() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);

		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0; i < 20; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}

		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {

			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click rehearsal
			if (buttonTest(we, "rehearsal", true)) {
				Thread.sleep(500);

				ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
				if(tabs2.size() == 2) {
					driver.switchTo().window(tabs2.get(1));
				    
					Thread.sleep(1000);
					String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
					//check room uri
					if(!roomuri.equalsIgnoreCase(driver.getCurrentUrl()))
				    {
						failMsg = failMsg + "\n2. fail to enter Seminar rehearsal: " + driver.getCurrentUrl();
				    } 
					// close 1tab
					driver.close();
					driver.switchTo().window(tabs2.get(0));

				} else {
					failMsg = failMsg + "\n3. not opened new tab : " + driver.getCurrentUrl();
				}

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}

		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//67. 비공개 세미나 게시완료세미나(시크릿). 삭제
	@Test(priority = 67, enabled = true)
	public void privateS_registerdDelete() throws Exception {
		String failMsg = "";
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in registerd tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			// click trash icon 
			if (buttonTest(we, "trash", true)) {
				Thread.sleep(500);

				if(!driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(CommonValues.MSG_DELETE_SEMINAR)) {
					failMsg = failMsg + "\n 2. msg error(delete seminar) [Expected]" + CommonValues.MSG_DELETE_SEMINAR
							+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
				}
				
				//cancel
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
				
				buttonTest(we, "trash", true);
				Thread.sleep(500);
				
				//confirm
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);

			} else {
				failMsg = failMsg + "\n 3. fail to click invite button";
			}
		}
		
		driver.navigate().refresh();
		Thread.sleep(1000);
		
		if (isElementPresent(By.xpath(listitem))) {
			failMsg = "3. find seminar after delete";
		} 
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}			

	//70. 비공개 세미나 standby세미나(시크릿). 상태 확인. 초대하기
	@Test(priority = 70, enabled = true)
	public void privateS_standbyInvite() throws Exception {
		String failMsg = "";
		
		// goto Create seminar
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		driver.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();

		Thread.sleep(1000);
		
		//set seminar data
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		seminarTitle = format1.format(cal.getTime());
		//setSeminar(seminarTitle, true);
		CommonValues comm  = new CommonValues();
		comm.setCreateSeminar(driver, seminarTitle, true);
		
		// private
		driver.findElement(By.xpath("//div[@id='seminar-type']")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//div[@class='checkbox']")).click();
		if(!driver.findElement(By.xpath("//div[@class='checkbox']//input")).isSelected()) {
			failMsg = "0. not checked secret mode";
		}
		Thread.sleep(100);
		
		//save as..
		driver.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);

		// 임시저장 세미나 ID
		seminarID = comm.findSeminarIDInList(driver, seminarTitle);

		// click post
		comm.postSeminar(driver, seminarID);

		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(1000);
			closeBenner(driver);
		}
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
			
			//버튼 확인, ready 태그
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. find link icon";
			}
			if(buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-4. can not find invite button.";
			}
			if(!buttonTest(we, "standby", false)) {
				failMsg = failMsg + "\n1-5. can not find standby tag.";
			}
			
			// click invite button 
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite ";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}	

	//71. 비공개 세미나 onair세미나. 상태 확인. 초대하기
	@Test(priority = 71, enabled = true)
	public void privateS_onairInvite() throws Exception {
		String failMsg = "";
		
		// goto seminar info
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		// click enter(new tab)
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + driver.getCurrentUrl();
			}
		} else {
			driver.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver.close();
			// switch room tab
			driver.switchTo().window(tabs2.get(1));
		}
		
		// 설정팝업 확인
	    CommonValues comm = new CommonValues();
	    comm.checkSettingpopup(driver);
		
		//start seminar
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		driver.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(1000);
		closeBenner(driver);
		
		// click registerd tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_REGISTERED)).click();
		Thread.sleep(500);

		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = "1. can not find seminar in draft tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			Thread.sleep(100);
			
			//버튼 확인, ready 태그
			if(buttonTest(we, "link", false)) {
				failMsg = failMsg + "\n1-1. find link icon";
			}
			if(buttonTest(we, "edit", false)) {
				failMsg = failMsg + "\n1-2. find edit icon";
			}
			if(buttonTest(we, "trash", false)) {
				failMsg = failMsg + "\n1-3. find trash icon";
			}
			if(!buttonTest(we, "invite", false)) {
				failMsg = failMsg + "\n1-4. can not find invite button.";
			}
			if(!buttonTest(we, "onair", false)) {
				failMsg = failMsg + "\n1-5. can not find onair tag.";
			}
			
			// click invite button 
			if(buttonTest(we, "invite", true)) {
				Thread.sleep(500);
				
				String inviteUrl = CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID;

				if (!driver.getCurrentUrl().contains(inviteUrl)) {
					failMsg = failMsg + "\n 2. no invite view after click invite button : [Expected]" + inviteUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n 3. fail to click invite ";
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}		

	//75. 비공개 세미나 종료세미나(secret). 상태 확인. 통계보기, 수정하기
	@Test(priority = 75, enabled = true)
	public void privateS_past_statistics() throws Exception {
		String failMsg = "";
		
		// goto seminar info
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();",driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)));
		// click enter(new tab)
		driver.findElement(By.xpath(CommonValues.XPATH_SEMINARVIEW_ENTER)).click();
		Thread.sleep(2000);

		String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		if (tabs2.size() != 2) {
			if (!driver.getCurrentUrl().contentEquals(roomuri)) {
				failMsg = "0. cannot enter to room. current url : " + driver.getCurrentUrl();
			}
		} else {
			driver.switchTo().window(tabs2.get(0));
			// close 1 tab
			driver.close();
			// switch room tab
			driver.switchTo().window(tabs2.get(1));
		}

		// 설정팝업 확인
	    CommonValues comm = new CommonValues();
	    comm.checkSettingpopup(driver);
		
		//close seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(2000);
		
		if(isElementPresent_wd(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n0-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n0-2. cannot find toast (presenter)";
		}
		
		// go to listview
		String listView = CommonValues.SERVER_URL + CommonValues.LIST_URI;
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 2. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			//click statistics
			if (buttonTest(we, "statistics", true)) {
				Thread.sleep(500);
				
				String statisticsUrl = CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID;

				if (!driver.getCurrentUrl().contains(statisticsUrl)) {
					failMsg = failMsg + "\n 3. no statistics view after click statistics button : [Expected]" + statisticsUrl
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n4. fail to statistics button.";
			}
		}
		
		// go to listview for edit
		driver.get(listView);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);

		// click past tab
		driver.findElement(By.xpath(XPATH_LIST_TAB_PAST)).click();
		Thread.sleep(500);
		
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 20 ; i++) {
			if (isElementPresent(By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(By.xpath(listitem))) {
			failMsg = failMsg + "\n 5. can not find seminar in past tab";
			driver.get(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID);
		} else {
			
			WebElement seminarTitle = driver.findElement(By.xpath(listitem));
			WebElement we = seminarTitle.findElement(By.xpath("../../../."));

			//click statistics
			if (buttonTest(we, "edit", true)) {
				Thread.sleep(500);
				
				String editview = CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID;

				if (!driver.getCurrentUrl().contains(editview)) {
					failMsg = failMsg + "\n 6. no statistics view after click statistics button : [Expected]" + editview
							+ " [Actual]" + driver.getCurrentUrl();
				}
			} else {
				failMsg = failMsg + "\n7. fail to edit button.";
			}
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	public boolean buttonTest(WebElement we, String buttonType, boolean click) {
		String xpath = "";
		
		switch (buttonType) {
		case "link":
			xpath = ".//i[@class='ricon-editor-link']";
			break;
		case "edit":
			xpath = ".//i[@class='ricon-edit']";
			break;	
		case "trash":
			xpath = ".//i[@class='ricon-trash']";
			break;	
		case "rehearsal":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[2]";
			break;
		case "statistics":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[1]";
			break;
		case "practice":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[2]";
			break;
		case "invite":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[1]";
			break;
		case "invite-temp":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[1]";
			break;		
		case "post":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[2]";
			break;		
		case "button2":
			xpath = ".//div[@class='SeminarListItem_seminarListItem__buttons__19f5i']/button[2]";
			break;		
		case "lock":
			xpath = ".//i[@class='ricon-lock']";
			break;
		case "standby":
			xpath = ".//div[@class='tag ready']/div";
			break;	
		case "onair":
			xpath = ".//div[@class='tag onair']/div";
			break;		
		default:
			break;
		}
		if (click) {
			try {
				we.findElement(By.xpath(xpath)).click();
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}

		} else {
			try {
				if(buttonType.contentEquals("stnadby") && buttonType.contentEquals("onair")) {
					if(buttonType.contentEquals("stnadby") && we.findElement(By.xpath(xpath)).getText().contentEquals("Stand By"))
						return true;
					else if (buttonType.contentEquals("onair") && we.findElement(By.xpath(xpath)).getText().contentEquals("ON AIR"))
						return true;
					else 
						return false;
				} else if(we.findElement(By.xpath(xpath)).isDisplayed()) {
					return true;
				} else {
					return false;
				}
			} catch (NoSuchElementException e) {
				return false;
			}
		}
	}
	
	private void closeBenner(WebDriver wd) {
		if(isElementPresent_Driver(wd, By.xpath("//div[@class='Banner_banner__demo__2zAHq']"))) {
			wd.findElement(By.xpath("//div[@class='Banner_banner__demo__2zAHq']//i[@class='ricon-close']")).click();
		}
	}
	
	@AfterClass(alwaysRun = true)
	  public void tearDown() throws Exception {
		  
	    driver.quit();
	    driver_guest.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}	
	
	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	private boolean isElementPresent_wd(WebDriver wd, By by) {
		try {
			wd.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}	
	  
	private boolean isElementPresent_Driver(WebDriver wd, By by) {
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
