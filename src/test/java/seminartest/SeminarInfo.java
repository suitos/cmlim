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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* ChannelSeminar 
 * 0. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1
 * 1. 게시자가 상세화면 구성 확인
 * 2. 게시자가 수정 클릭
 * 3. 게시자가 삭제 케이스는 CreateSeminar2#createview_draft_del
 * 3. 게시자가 저장완료 세미나 게시
 * 
 * 11. 게시자가 게시완료된 예정 세미나 상세화면 구성확인
 * 12. 게시자가 게시완료된 예정 세미나 링크 클릭
 * 13. 게시자가 게시완료된 예정 세미나 수정
 * 14. 게시자가 게시완료된 예정 세미나 초대하기
 * 15. 게시자가 게시완료된 예정 세미나 삭제하기
 * 
 * 20. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1 
 * 21. 채널 마스터가 저장완료 세미나 상세화면 구성 확인
 * 22. 채널 마스터가 수정 클릭
 * 23. 채널 마스터가 저장완료 세미나 삭제
 * 24. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1 
 * 25. 채널 마스터가 저장완료 세미나 게시
 * 
 * 31. 채널 마스터가 게시완료된 예정 세미나 구성확인
 * 32. 채널 마스터가 게시완료된 예정 세미나 링크 클릭
 * 33. 채널 마스터가 게시완료된 예정 세미나 수정
 * 34. 채널 마스터가 게시완료된 예정 세미나 초대하기
 * 35. 채널 마스터가 게시완료된 예정 세미나 삭제하기
 * 
 * 40. standby 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1
 * 41. 게시자가 standby 세미나 상세화면 구성확인
 * 42. 게시자가 standby 세미나 링크 클릭 
 * 43. 게시자가 standby 세미나  초대하기
 * 44. 게시자가 standby 세미나 입장하기(바로 입장)
 * 
 * 45. 채널 마스터가 standby 세미나 상세화면 구성확인
 * 46. 채널 마스터가 standby 세미나 링크 클릭 
 * 47. 채널 마스터가 standby 세미나  초대하기
 * 48. 채널 마스터가 standby 세미나 입장하기(바로 입장)
 * 
 * 50. 발표자가 standby세미나 시작 (onair로 변경)
 * 51. 게시자가 onair 세미나 상세화면 구성확인
 * 52. 게시자가 onair 세미나 링크 클릭 
 * 53. 게시자가 onair 세미나  초대하기
 * 54. 게시자가 onair 세미나 입장하기(바로 입장)
 * 
 * 55. 채널 마스터가 onair 세미나 상세화면 구성확인
 * 56. 채널 마스터가 onair 세미나 링크 클릭 
 * 57. 채널 마스터가 onair 세미나  초대하기
 * 58. 채널 마스터가 onair 세미나 입장하기(바로 입장)
 * 
 * 60. 발표자가 onair 세미나 종료하기
 * 61. 게시자가 지난 세미나 상세화면 구성확인
 * 62. 게시자가 지난 세미나 수정 클릭
 * 63. 게시자가 지난 세미나 통계보기 클릭
 * 
 * 65. 채널 마스터가 지난세미나 상세화면 구성확인
 * 66. 채널 마스터가 지난세미나 수정 클릭
 * 67. 게시자가 지난 세미나 통계보기 클릭
 * 
 * 70. 비공개세미나(일반) 저장완료 화면. 
 * 71. 비공개세미나(일반) 저장완료 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
 * 72. 비공개세미나(일반), 저장완료 세미나 게시(게시자), 예정세미나 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 73. 비공개세미나(일반), standby 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 74. 비공개세미나(일반), onair 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 75. 비공개세미나(일반), 지난 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 76. 비공개세미나(일반), 지난 세미나 생성. 채널마스터가 채널 리스트에서 확인(rsrsup1)
 * 77. 비공개세미나(일반), 지난 세미나 생성. 채널멤버가 채널 리스트에서 확인(rsrsup4)
 * 
 * 80. 비공개세미나(secret) 저장완료 화면. 
 * 81. 비공개세미나(secret) 저장완료 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
 * 82. 비공개세미나(secret), 저장완료 세미나 게시(게시자), 예정세미나 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 83. 비공개세미나(secret), standby 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인
 * 84. 비공개세미나(secret), onair 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
 * 85. 비공개세미나(secret), 지난 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인 	
 * 86. 비공개세미나(secret), 지난 세미나 생성. 채널마스터가 채널 리스트에서 확인(rsrsup1)
 * 87. 비공개세미나(secret), 지난 세미나 생성. 채널멤버가 채널 리스트에서 확인(rsrsup4)
 * 88. 비공개세미나(secret), 지난 세미나 생성. 다른 채널 멤버가 URL로 확인(rsrsup9)
 */

public class SeminarInfo {

	public static String USER_PUBLISHER = "rsrsup2";
	public static String USER_PRESENTER = "rsrsup3";
	public static String USER_ORGANIZER = "rsrsup6";
	public static String USER_MASTER = "rsrsup1";
	
	public static String MSG_POST = "Seminar will be published on %s.\nOnce published, channel cannot be changed.";
	public static String MSG_DELETE = "Do you want to delete the seminar?";
	public static String MSG_LINK = "Seminar link copied.";
	public static String MSG_PRIVATE_SEMINAR_VIEW = "This seminar is a private seminar.\nThee seminar details is visible to invited users only.";
	
	public static int SEMINAR_TYPE_PUBLIC = 0;
	public static int SEMINAR_TYPE_PRIVATE = 1;
	public static int SEMINAR_TYPE_PRIVATE_SECRET = 2;
	
	public static boolean part1 = true;
	
	public static WebDriver driver;
	public static WebDriver driver_publisher;
	public static WebDriver driver_master;
	public static WebDriver driver_organizer;
	public static WebDriver driver_guest;
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static String seminarTitle = "";
	public String seminarID = "";
	public String seminarLink = "";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		System.out.println("BeforeTest!!!");
		System.out.println(browsertype);

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US");
		driver_publisher = comm.setDriver(driver_publisher, browsertype, "lang=en_US");
		driver_master = comm.setDriver(driver_master, browsertype, "lang=en_US");
		driver_organizer = comm.setDriver(driver_organizer, browsertype, "lang=en_US");
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=en_US");
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", driver_publisher);
		context.setAttribute("webDriver3", driver_master);
		context.setAttribute("webDriver4", driver_organizer);
		context.setAttribute("webDriver5", driver_guest);
		
		driver.get(CommonValues.SERVER_URL);
		
        System.out.println("End BeforeTest!!!");
	}
	
	// 0. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1
	@Test(priority=0, enabled = true)
	public void createDraftSeminar() throws Exception {
		
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_publisher, USER_PUBLISHER + "@gmail.com");
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime());
		String seminarUri = createSeminar(seminarTitle, false, SEMINAR_TYPE_PUBLIC);
		seminarID = seminarUri;
	  }	
	
	// 1. 게시자가 상세화면 구성 확인
	@Test(priority = 1, dependsOnMethods = { "createDraftSeminar" }, enabled = true)
	public void draft_publisher() throws Exception {
		String failMsg = "";

		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver_publisher.get(detailView);
		Thread.sleep(500);

		// 링크 버튼 확인 (링크 없음)
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = "1. find link button (draft seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (draft seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 3. cannot find trash button (draft seminar)";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}

	}

	// 2. 게시자가 수정 클릭
	@Test(priority=2, dependsOnMethods = {"createDraftSeminar"}, enabled = true)
	public void draft_publisher_edit() throws Exception {
		String failMsg = "";
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver_publisher.getCurrentUrl().contentEquals(detailView)) {
			driver_publisher.get(detailView);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_publisher, "edit", true)) {
			Thread.sleep(500);
			if (!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. no edit view (draft seminar)";
			}
		} else {
			failMsg = failMsg + "\n 2. fail to click edit button (draft seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 3. 게시자가 저장완료 세미나 게시
	@Test(priority=3, dependsOnMethods = {"createDraftSeminar"}, enabled = true)
	public void draft_publisher_post() throws Exception {
		String failMsg = "";
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver_publisher.getCurrentUrl().contentEquals(detailView)) {
			driver_publisher.get(detailView);
			Thread.sleep(500);
		}
		
		// 게시 버튼 클릭
		buttonTest(driver_publisher, "post", true);
		Thread.sleep(500);
		
		String postmsg = String.format(MSG_POST, "rsrup1");
		try {
			if(driver_publisher.findElement(By.xpath("//div[@class='modal-body']")).getText().contentEquals(postmsg)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + postmsg 
						+ " [Actual]" + driver_publisher.findElement(By.xpath("//div[@class='modal-body']")).getText();
			}
			
			//cancel
			driver_publisher.findElement(By.xpath("//div[@class='modal-footer']/button[2]")).click();
			Thread.sleep(500);
			
			buttonTest(driver_publisher, "post", true);
			//confirm
			driver_publisher.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
			Thread.sleep(2000);
			
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 2. popup error(exception) : " + e.getMessage() ;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 11. 게시자가 게시완료된 예정 세미나 상세화면 구성확인
	@Test(priority=11, dependsOnMethods = {"createDraftSeminar", "draft_publisher_post"}, alwaysRun = true, enabled = true)
	public void post_publisher() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (!buttonTest(driver_publisher, "link", false)) {
			failMsg = "1. cannot find link button (posted seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (posted seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 3. cannot find trash button (posted seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 12. 게시자가 게시완료된 예정 세미나 링크 클릭
	@Test(priority=12, dependsOnMethods = {"createDraftSeminar", "post_publisher"}, alwaysRun = true, enabled = true)
	public void post_publisher_link() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_publisher, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced seminar url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 13. 게시자가 게시완료된 예정 세미나 수정
	@Test(priority=13, dependsOnMethods = {"createDraftSeminar", "post_publisher_link"}, alwaysRun = true, enabled = true)
	public void post_publisher_edit() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_publisher, "edit", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. not edit view : current url : " + driver_publisher.getCurrentUrl();
				Thread.sleep(500);
			}
		} else {
			failMsg = failMsg + "\n 2. cannot find edit button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	// 14. 게시자가 게시완료된 예정 세미나 초대하기
	@Test(priority=14, dependsOnMethods = {"createDraftSeminar", "post_publisher_edit"}, alwaysRun = true, enabled = true)
	public void post_publisher_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_publisher, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_publisher.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 15. 게시자가 게시완료된 예정 세미나 삭제하기
	@Test(priority=15, dependsOnMethods = {"createDraftSeminar", "post_publisher_invite"}, alwaysRun = true, enabled = true)
	public void post_publisher_delete() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		//  삭제 버튼 클릭
		if (buttonTest(driver_publisher, "trash", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_DELETE)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_DELETE 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//cancel
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(500);
			
			buttonTest(driver_publisher, "trash", true);
			Thread.sleep(500);
			//confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		} else {
			failMsg = failMsg + "\n 2. cannot find delete button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 20. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1 , 마스터 login
	@Test(priority = 20)
	public void createDraftSeminar2() throws Exception {
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");

		Date time = new Date();
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime());
		String seminarUri = createSeminar(seminarTitle, false, SEMINAR_TYPE_PUBLIC);
		seminarID = seminarUri;
		
	}	
	
	// 21. 채널 마스터가 저장완료 세미나 상세화면 구성 확인
	@Test(priority = 21, dependsOnMethods = { "createDraftSeminar2" })
	public void draft_master() throws Exception {
		String failMsg = "";

		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		driver_master.get(detailView);
		Thread.sleep(500);

		// 링크 버튼 확인 (링크 없음)
		if (buttonTest(driver_master, "link", false)) {
			failMsg = "1. find link button (draft seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (draft seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 3. cannot find trash button (draft seminar)";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 22. 채널 마스터가 수정 클릭
	@Test(priority=22, dependsOnMethods = {"draft_master"}, alwaysRun = true, enabled = true)
	public void draft_master_edit() throws Exception {
		String failMsg = "";
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver_master.getCurrentUrl().contentEquals(detailView)) {
			driver_master.get(detailView);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_master, "edit", true)) {
			Thread.sleep(500);
			if (!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. no edit view (draft seminar)";
			}
		} else {
			failMsg = failMsg + "\n 2. fail to click edit button (draft seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 23. 채널 마스터가 저장완료 세미나 삭제
	@Test(priority=23, dependsOnMethods = {"draft_master"}, alwaysRun = true, enabled = true)
	public void draft_master_delete() throws Exception {
		String failMsg = "";
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver_master.getCurrentUrl().contentEquals(detailView)) {
			driver_master.get(detailView);
			Thread.sleep(500);
		}
		
		// 삭제버튼 클릭
		if(buttonTest(driver_master, "trash", true)) {
			Thread.sleep(500);
			if(!driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_DELETE)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_DELETE 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//cancel
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(500);
			
			buttonTest(driver_master, "trash", true);
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
		} else {
			failMsg = failMsg + "\n 2. cannot find trash button  ";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 24. 저장완료 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1 , 마스터 login
	@Test(priority = 24)
	public void createDraftSeminar3() throws Exception {
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime());
		String seminarUri = createSeminar(seminarTitle, false, SEMINAR_TYPE_PUBLIC);
		seminarID = seminarUri;
		
	}	

	// 25. 채널 마스터가 저장완료 세미나 게시
	@Test(priority=25, dependsOnMethods = {"createDraftSeminar3"})
	public void draft_master_post() throws Exception {
		String failMsg = "";
		
		String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
		if(!driver_master.getCurrentUrl().contentEquals(detailView)) {
			driver_master.get(detailView);
			Thread.sleep(500);
		}
		
		// 게시 버튼 클릭
		buttonTest(driver_master, "post", true);
		Thread.sleep(500);
		
		String postmsg = String.format(MSG_POST, "rsrup1");
		try {
			if(driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(postmsg)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + postmsg 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//cancel
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(500);
			
			buttonTest(driver_master, "post", true);
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 2. popup error(exception) : " + e.getMessage() ;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 31. 채널 마스터가 게시완료된 예정 세미나 상세화면 구성확인
	@Test(priority=11, dependsOnMethods = {"createDraftSeminar3", "draft_master_post"}, alwaysRun = true, enabled = true)
	public void post_master() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인 (링크 없음)
		if (!buttonTest(driver_master, "link", false)) {
			failMsg = "1. cannot find link button (posted seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (posted seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 3. cannot find trash button (posted seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 32. 채널 마스터가 게시완료된 예정 세미나 링크 클릭
	@Test(priority=12, dependsOnMethods = {"createDraftSeminar3", "post_master"}, alwaysRun = true, enabled = true)
	public void post_master_link() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_master, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced channel url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 33. 채널 마스터가 게시완료된 예정 세미나 수정
	@Test(priority=33, dependsOnMethods = {"createDraftSeminar3", "post_master"}, alwaysRun = true, enabled = true)
	public void post_master_edit() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_master, "edit", true)) {
			Thread.holdsLock(500);
			
			if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. not edit view : current url : " + driver_master.getCurrentUrl();
				Thread.sleep(500);
			}
		} else {
			failMsg = failMsg + "\n 2. cannot find edit button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 34. 채널마스터가 게시완료된 예정 세미나 초대하기
	@Test(priority=34, dependsOnMethods = {"createDraftSeminar3", "post_master_edit"}, alwaysRun = true, enabled = true)
	public void post_master_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_master, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_master.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 35. 마스터가 게시완료된 예정 세미나 삭제하기
	@Test(priority=35, dependsOnMethods = {"createDraftSeminar3", "post_master_invite"}, alwaysRun = true, enabled = true)
	public void post_master_delete() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		//  삭제 버튼 클릭
		if (buttonTest(driver_master, "trash", true)) {
			Thread.sleep(500);
			
			if(!driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_DELETE)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_DELETE 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//cancel
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[2]")).click();
			Thread.sleep(500);
			
			buttonTest(driver_master, "trash", true);
			Thread.sleep(500);
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		} else {
			failMsg = failMsg + "\n 2. cannot find delete button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 40. standby 세미나 만들기. 게시자 rsrsup2, 발표자 rsrsup3, 운영자 rsrsup6, 마스터 rsrsup1 
	@Test(priority = 40)
	public void createReadySeminar() throws Exception {
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime());
		String seminarUri = createSeminar(seminarTitle, true, SEMINAR_TYPE_PUBLIC);
		seminarID = seminarUri;
		
		CommonValues comm = new CommonValues();
		comm.postSeminar(driver_publisher, seminarID);
		
	}

	// 41. 게시자가 standby세미나 상세화면 구성확인
	@Test(priority=41, dependsOnMethods = {"createReadySeminar"}, alwaysRun = true, enabled = true)
	public void standby_publisher() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (!buttonTest(driver_publisher, "link", false)) {
			failMsg = "1. cannot find link button (standby seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 2. find edit button (standby seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (standby seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		
		// standby tag 확인
		if (!driver_publisher.findElement(By.xpath("//div[@class='tag ready']/div")).getText().contentEquals("Stand By")) {
			failMsg = failMsg + "\n 6. standby tag error [Actual]"
					+ driver_publisher.findElement(By.xpath("//div[@class='tag ready']/div")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 42. 게시자가 standby 세미나 링크 클릭
	@Test(priority=42, dependsOnMethods = {"createReadySeminar", "standby_publisher"}, alwaysRun = true, enabled = true)
	public void standby_publisher_link() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_publisher, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced channel url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 43. 게시자가 standby 세미나  초대하기
	@Test(priority=43, dependsOnMethods = {"createReadySeminar", "standby_publisher_link"}, alwaysRun = true, enabled = true)
	public void standby_publisher_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_publisher, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_publisher.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 43. 게시자가 standby 세미나  입장하기
	@Test(priority=44, dependsOnMethods = {"createReadySeminar", "standby_publisher_link"}, alwaysRun = true, enabled = true)
	public void standby_publisher_enter() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver_publisher, "enter", true)) {
			Thread.sleep(1000);
			
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver_publisher.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver_publisher.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver_publisher.getCurrentUrl();
				} 
			} else {
				driver_publisher.switchTo().window(tabs2.get(0));
				//close 1 tab
				driver_publisher.close();
				//switch room tab
				driver_publisher.switchTo().window(tabs2.get(1));
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find enter button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 45. 채널마스터가 standby세미나 상세화면 구성확인
	@Test(priority=45, dependsOnMethods = {"createReadySeminar"}, alwaysRun = true, enabled = true)
	public void standby_master() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (!buttonTest(driver_master, "link", false)) {
			failMsg = "1. cannot find link button (standby seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2. find edit button (standby seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (standby seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		
		// standby tag 확인
		if (!driver_master.findElement(By.xpath("//div[@class='tag ready']/div")).getText().contentEquals("Stand By")) {
			failMsg = failMsg + "\n 6. standby tag error [Actual]"
					+ driver_master.findElement(By.xpath("//div[@class='tag ready']/div")).getText();
		}	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 46. 채널마스터가 standby 세미나 링크 클릭
	@Test(priority=46, dependsOnMethods = {"createReadySeminar", "standby_master"}, alwaysRun = true, enabled = true)
	public void standby_master_link() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_master, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced channel url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 47. 채널마스터가  standby 세미나  초대하기
	@Test(priority=47, dependsOnMethods = {"createReadySeminar", "standby_master_link"}, alwaysRun = true, enabled = true)
	public void standby_master_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_master, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_master.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 48. 채널마스터가  standby 세미나  입장하기
	@Test(priority=48, dependsOnMethods = {"createReadySeminar", "standby_master_invite"}, alwaysRun = true, enabled = true)
	public void standby_master_enter() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver_master, "enter", true)) {
			Thread.sleep(1000);
			
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver_master.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver_master.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver.getCurrentUrl();
				} 
			} else {
				driver_master.switchTo().window(tabs2.get(0));
				//close 1 tab
				driver_master.close();
				//switch room tab
				driver_master.switchTo().window(tabs2.get(1));
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find enter button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 50. 발표자가 standby세미나 시작 (onair로 변경)
	@Test(priority=50, dependsOnMethods = {"createReadySeminar", "standby_master_enter"}, alwaysRun = true, enabled = true)
	public void onair_presenter() throws Exception {
		String failMsg = "";
		
		// login presenter
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_PRESENTER + "@gmail.com");
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver, "enter", true)) {
			Thread.sleep(500);
			
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver_publisher.getCurrentUrl();
				} 
			} else {
				driver.switchTo().window(tabs2.get(0));
				//close 1 tab
				driver.close();
				//switch room tab
				driver.switchTo().window(tabs2.get(1));

				comm.checkSettingpopup(driver);
				
				// start seminar
				driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
				Thread.sleep(500);
				driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
				Thread.sleep(500);
				driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
				Thread.sleep(1000);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find enter button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 51. 게시자가 onair세미나 상세화면 구성확인
	@Test(priority=51, dependsOnMethods = {"onair_presenter"}, alwaysRun = true, enabled = true)
	public void onair_publisher() throws Exception {
		String failMsg = "";
		Thread.sleep(3000);
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		} else {
			driver_publisher.navigate().refresh();
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (!buttonTest(driver_publisher, "link", false)) {
			failMsg = "1. cannot find link button (standby seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 2. find edit button (standby seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (standby seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		
		// onair tag 확인
		if (!driver_publisher.findElement(By.xpath("//div[@class='tag onair']/div")).getText().contentEquals("ON AIR")) {
			failMsg = failMsg + "\n 6. OnAir tag error [Actual]"
					+ driver_publisher.findElement(By.xpath("//div[@class='tag onair']/div")).getText();
		}		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 52. 게시자가 onair 세미나 링크 클릭
	@Test(priority=52, dependsOnMethods = {"onair_presenter", "onair_publisher"}, alwaysRun = true, enabled = true)
	public void onair_publisher_link() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_publisher, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced channel url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 53. 게시자가 onair 세미나  초대하기
	@Test(priority=53, dependsOnMethods = {"onair_presenter", "onair_publisher_link"}, alwaysRun = true, enabled = true)
	public void onair_publisher_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_publisher, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_publisher.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 54. 게시자가 onair 세미나  입장하기
	@Test(priority=54, dependsOnMethods = {"onair_presenter", "onair_publisher_invite"}, alwaysRun = true, enabled = true)
	public void onair_publisher_enter() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver_publisher, "enter", true)) {
			Thread.sleep(1000);
		
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver_publisher.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver_publisher.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver.getCurrentUrl();
				} 
			} else {
				driver_publisher.switchTo().window(tabs2.get(1));
				Thread.sleep(100);
				
				if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID)) {
					failMsg = "2. cannot enter to room. current url : " + driver_publisher.getCurrentUrl();
				} 
				
				
				driver_publisher.close();
				//switch room tab
				driver_publisher.switchTo().window(tabs2.get(0));
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find enter button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 55. 채널마스터가 onair세미나 상세화면 구성확인
	@Test(priority=45, dependsOnMethods = {"onair_presenter", "onair_publisher_invite"}, alwaysRun = true, enabled = true)
	public void onair_master() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (!buttonTest(driver_master, "link", false)) {
			failMsg = "1. cannot find link button (standby seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2. find edit button (standby seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (standby seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 4. cannot find invite button (posted seminar)";
		}
		
		// 입장 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 5. cannot find enter button (posted seminar)";
		}
		// onair tag 확인
		if (!driver_master.findElement(By.xpath("//div[@class='tag onair']/div")).getText().contentEquals("ON AIR")) {
			failMsg = failMsg + "\n 6. OnAir tag onair [Actual]"
					+ driver_master.findElement(By.xpath("//div[@class='tag onair']/div")).getText();
		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 56. 채널마스터가 onair 세미나 링크 클릭
	@Test(priority=56, dependsOnMethods = {"onair_presenter", "onair_master"}, alwaysRun = true, enabled = true)
	public void onair_master_link() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if (buttonTest(driver_master, "link", true)) {
			Thread.sleep(500);
			
			if(!driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_LINK)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_LINK 
						+ " [Actual]" + driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			//confirm
			driver_master.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);
			
			//get seminar link(clipboard) 
			String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			String seminarLink = CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID;
			
			CommonValues comm = new CommonValues();
			if (comm.checkShotURL(driver_guest, clipboardtxt, seminarLink)) {
				failMsg =  "not mathced channel url. copy data : " + clipboardtxt;
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find link button (posted seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 57. 채널마스터가  onair 세미나  초대하기
	@Test(priority=57, dependsOnMethods = {"onair_presenter", "onair_master_link"}, alwaysRun = true, enabled = true)
	public void onair_master_invite() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 초대 버튼 클릭
		if (buttonTest(driver_master, "invite", true)) {
			Thread.sleep(500);
			
			if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.INVITE_VIEW + seminarID)) {
				failMsg = failMsg + "\n 1. not invite view : current url : " + driver_master.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find invite button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 58. 채널마스터가  onair 세미나  입장하기
	@Test(priority=58, dependsOnMethods = {"onair_presenter", "onair_master_link"}, alwaysRun = true, enabled = true)
	public void onair_master_enter() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver_master, "enter", true)) {
			Thread.sleep(500);
			
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver_master.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver_master.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver.getCurrentUrl();
				} 
			} else {
				driver_master.switchTo().window(tabs2.get(1));
				Thread.sleep(100);
				
				if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID)) {
					failMsg = failMsg + "\n 2. cannot enter to room. current url : " + driver_master.getCurrentUrl();
				} 
				driver_master.close();
				//switch room tab
				driver_master.switchTo().window(tabs2.get(0));
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find enter button (posted seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 60. 발표자가 onair 세미나 종료하기 
	@Test(priority=60, dependsOnMethods = {"onair_master_link"}, alwaysRun = true, enabled = true)
	public void finish_presenter() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 입장하기 버튼 클릭
		if (buttonTest(driver, "enter", true)) {
			Thread.sleep(500);
			
			String roomuri = CommonValues.SERVER_URL + CommonValues.SEMINAR_ROOM + seminarID;
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			if(tabs2.size() != 2) {
				if(!driver.getCurrentUrl().contentEquals(roomuri)) {
					failMsg = "1. cannot enter to room. current url : " + driver.getCurrentUrl();
				} 
			} else {
				driver.switchTo().window(tabs2.get(0));
				//close 1 tab
				driver.close();
				//switch room tab
				driver.switchTo().window(tabs2.get(1));
				
				//설정팝업 확인
				CommonValues comm = new CommonValues();
				comm.checkSettingpopup(driver);
				Thread.sleep(500);
				
				//종료
				driver.findElement(By.id("btn-exit")).click();
				Thread.sleep(500);
				driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
				Thread.sleep(500);
				driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
				Thread.sleep(1000);
	
				if(isElementPresent(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
					if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
						failMsg = failMsg + "\n2-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
								 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
					}
				} else {
					failMsg = failMsg + "\n2-2. cannot find toast (presenter)";
				}
			}

		} else {
			failMsg = failMsg + "\n 3. cannot find enter button (onair seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	// 61. 게시자가 종료 세미나 상세화면 구성확인
	@Test(priority=61, dependsOnMethods = {"finish_presenter"}, alwaysRun = true, enabled = true)
	public void past_publisher() throws Exception {
		String failMsg = "";
		
		driver_publisher.navigate().refresh();
		Thread.sleep(500);
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = "1. find link button (past seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (past seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (past seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 4. find invite button (past seminar)";
		}
		
		// 통계 버튼 확인
		if (!buttonTest(driver_publisher, "statistics", false)) {
			failMsg = failMsg + "\n 5. cannot find statistics button (past seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 62. 게시자가 지난 세미나 수정 클릭
	@Test(priority=62, dependsOnMethods = {"finish_presenter", "past_publisher"}, alwaysRun = true, enabled = true)
	public void past_publisher_edit() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_publisher, "edit", true)) {
			Thread.sleep(500);
			if (!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. no edit view (draft seminar)";
			}
		} else {
			failMsg = failMsg + "\n 2. fail to click edit button (past seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 63. 게시자가 지난 세미나  통계보기
	@Test(priority=63, dependsOnMethods = {"finish_presenter", "past_publisher_edit"}, alwaysRun = true, enabled = true)
	public void past_publisher_statistic() throws Exception {
		String failMsg = "";
		
		if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 통계보기 버튼 클릭
		if (buttonTest(driver_publisher, "statistics", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID)) {
				failMsg = failMsg + "\n 1. not statistics view : current url : " + driver_publisher.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find statistics button (past seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	// 65. 채널마스터 지난 세미나 상세화면 구성확인
	@Test(priority=65, dependsOnMethods = {"finish_presenter", "past_publisher_edit"}, alwaysRun = true, enabled = true)
	public void past_master() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		} else {
			driver_master.navigate().refresh();
			Thread.sleep(500);
		}
		
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = "1. find link button (standby seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2. cannot find edit button (past seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 3. find trash button (past seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 4. find invite button (past seminar)";
		}
		
		// 통계 버튼 확인
		if (!buttonTest(driver_master, "statistics", false)) {
			failMsg = failMsg + "\n 5. cannot find statistics button (past seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	// 66. 채널마스터  지난 세미나 수정 클릭
	@Test(priority=66, dependsOnMethods = {"finish_presenter", "past_master"}, alwaysRun = true, enabled = true)
	public void past_master_edit() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 수정 버튼 클릭
		if (buttonTest(driver_master, "edit", true)) {
			Thread.sleep(500);
			if (!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.CREATE_URI + seminarID)) {
				failMsg = failMsg + "\n 1. no edit view (draft seminar)";
			}
		} else {
			failMsg = failMsg + "\n 2. fail to click edit button (past seminar)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	// 67. 채널마스터  지난 세미나  통계보기
	@Test(priority=67, dependsOnMethods = {"finish_presenter", "past_master_edit"}, alwaysRun = true, enabled = true)
	public void past_master_statistic() throws Exception {
		String failMsg = "";
		
		if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID)) {
			driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
			Thread.sleep(500);
		}
		
		// 통계보기 버튼 클릭
		if (buttonTest(driver_master, "statistics", true)) {
			Thread.sleep(500);
			
			if(!driver_master.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + CommonValues.URI_STATISTIC + seminarID)) {
				failMsg = failMsg + "\n 1. not statistics view : current url : " + driver.getCurrentUrl();
				Thread.sleep(500);
			}

		} else {
			failMsg = failMsg + "\n 2. cannot find statistics button (past seminar)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	// 70. 비공개세미나(일반) 저장완료.
	@Test(priority=70, enabled = true)
	public void draft_private() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_private";
		String seminarUri = createSeminar(seminarTitle, false, SEMINAR_TYPE_PRIVATE);
		seminarID = seminarUri;
		
		// 각 유저 로그인
		driver.get(CommonValues.SERVER_URL + "/logout");
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_PRESENTER + "@gmail.com");
		
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");
		
		driver_organizer.get(CommonValues.SERVER_URL + "/logout");
		comm.loginseminar(driver_organizer, USER_ORGANIZER + "@gmail.com");
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//71. 비공개세미나(일반) 저장완료 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
	@Test(priority=71, dependsOnMethods = {"draft_private"}, enabled = true)
	public void draft_private_detailview() throws Exception {
		String failMsg = "";
		
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		//[게시자]
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = "1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] cannot find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (!buttonTest(driver_publisher, "post", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find post button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = "2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (!buttonTest(driver_master, "post", false)) {
			failMsg = failMsg + "\n 2-5.[master] find post button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = "3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (buttonTest(driver, "post", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] find post button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = "4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (buttonTest(driver_organizer, "post", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] find post button (private-normal seminar)";
		}	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//72. 비공개세미나(일반), 저장완료 세미나 게시(게시자), 예정세미나 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
	@Test(priority=72, dependsOnMethods = {"draft_private"}, enabled = true)
	public void post_private_detailview() throws Exception {
		String failMsg = "";	
		
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		// 게시 버튼 클릭
		buttonTest(driver_publisher, "post", true);
		Thread.sleep(500);
		String postmsg = String.format(MSG_POST, "rsrup1");
		try {
			if (driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(postmsg)) {
				failMsg = failMsg + "\n 0-1. post popup msg [Expected]" + postmsg + " [Actual]"
						+ driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			// confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 0-2. popup error(exception) : " + e.getMessage();
		}
		
		//[게시자]
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}		
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] cannot find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  연습하기 버튼 확인
		if (buttonTest(driver_publisher, "practice", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find practice button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (buttonTest(driver_master, "practice", false)) {
			failMsg = failMsg + "\n 2-5.[master] find practice button (private-normal seminar)";
		}
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (!buttonTest(driver, "practice", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find practice button (private-normal seminar)";
		}
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (!buttonTest(driver_organizer, "practice", false)) {
			failMsg = failMsg + "\n 4-5.[organizer] cannot find practice button (private-normal seminar)";
		}

		
		//delete seminar
		if (buttonTest(driver_publisher, "trash", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_DELETE)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_DELETE 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	//73. 비공개세미나(일반), standby 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=73, enabled = true)
	public void standby_private() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_private2";
		String seminarUri = createSeminar(seminarTitle, true, SEMINAR_TYPE_PRIVATE);
		seminarID = seminarUri;
		
		// post seminar
		CommonValues comm = new CommonValues();
		comm.postSeminar(driver_publisher, seminarID);
		

		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		//[게시자]
		// 링크 버튼 확인
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}		
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find enter button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 2-5.[master] find enter button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver, "enter", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find enter button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_organizer, "enter", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find enter button (private-normal seminar)";
		}			
		
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//74. 비공개세미나(일반), onair 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=74, enabled = true)
	public void onair_private() throws Exception {
		String failMsg = "";
		
		// goto seminar info(발표자)
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@class='btn btn-primary btn-auto actionButton']")));
		// click enter(new tab)
		buttonTest(driver, "enter", true);
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
		
		//설정팝업 확인
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//start seminar
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		//[게시자]
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find enter button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 2-5.[master] find enter button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver, "enter", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find enter button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_organizer, "enter", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find enter button (private-normal seminar)";
		}			
		
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	//75. 비공개세미나(일반), 지난 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=75, enabled = true)
	public void past_private() throws Exception {
		String failMsg = "";
		
		// goto seminar info(발표자)
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		
		buttonTest(driver, "enter", true);
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
		
		//설정팝업 확인
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//close seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(1000);
	
		if(isElementPresent(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n0-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n0-2. cannot find toast (presenter)";
		}
		
		//[게시자]
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_publisher, "statistics", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find statistics button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_master, "statistics", false)) {
			failMsg = failMsg + "\n 2-5.[master] find statistics button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver, "statistics", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find statistics button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_organizer, "statistics", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find statistics button (private-normal seminar)";
		}			
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//76. 비공개세미나(일반), 지난 세미나 생성. 채널마스터가 채널 리스트에서 확인(rsrsup1)
	@Test(priority=76, enabled = true)
	public void past_private_channellist_master() throws Exception {
		String failMsg = "";
		
		//channel list
		driver_master.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(100);
		driver_master.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		List<WebElement> channelList = driver_master.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		//click rsrsup1 channel 
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(USER_MASTER)) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}		
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver_master;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 5 ; i++) {
			if (isElementPresent(driver_master, By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(driver_master, By.xpath(listitem))) {
			failMsg = "1. can not find private seminar in channel seminar list";
			
		} else {
			driver_master.findElement(By.xpath(listitem)).click();
			Thread.sleep(500);
			
			String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
			if(!driver_master.getCurrentUrl().contentEquals(detailView)) {
				failMsg = failMsg + "\n 2. not seminar infomation view [Actual]" + driver_master.getCurrentUrl();
			} else {
				if(!buttonTest(driver_master, "lock", false)) {
					failMsg = failMsg + "\n 3. cannot find lock icon";
				}
			}
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//77. 비공개세미나(일반), 지난 세미나 생성. 채널멤버가 채널 리스트에서 확인(rsrsup4)
	@Test(priority=77, enabled = true)
	public void past_private_channellist_member() throws Exception {
		String failMsg = "";
		
		// master logout & member login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_master, "rsrsup4" + "@gmail.com");
		Thread.sleep(100);
		
		//channel list
		driver_master.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(100);
		driver_master.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		List<WebElement> channelList = driver_master.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		//click rsrsup1 channel 
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(USER_MASTER)) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}		
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver_master;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 5 ; i++) {
			if (isElementPresent(driver_master, By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (!isElementPresent(driver_master, By.xpath(listitem))) {
			failMsg = "1. can not find private seminar in channel seminar list";
			
		} else {
			driver_master.findElement(By.xpath(listitem)).click();
			Thread.sleep(100);
			
			String detailView = CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID;
			if(!driver_master.getCurrentUrl().contentEquals(detailView)) {
				failMsg = failMsg + "\n 2. not seminar infomation view [Actual]" + driver_master.getCurrentUrl();
				driver_master.get(detailView);
				Thread.sleep(100);
			}
			if (!driver_master.findElement(By.xpath("//p[@class='not-avaliable__content']")).getText().contentEquals(MSG_PRIVATE_SEMINAR_VIEW)) {
				failMsg = failMsg + "\n 3. private seminar view msg [Expected]" + MSG_PRIVATE_SEMINAR_VIEW + " [Actual]"
						+ driver_master.findElement(By.xpath("//p[@class='not-avaliable__content']")).getText();
			}
			//click goback
			driver_master.findElement(By.xpath("//div[@class='SeminarView_notAvaliable__19k2A']/button[1]")).click();
		}
		
		// member logout & master login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");
		Thread.sleep(100);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	// 80. 비공개세미나(secret) 저장완료.
	@Test(priority=80, enabled = true)
	public void draft_privateS() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_privateS";
		String seminarUri = createSeminar(seminarTitle, false, SEMINAR_TYPE_PRIVATE_SECRET);
		seminarID = seminarUri;
		
		// 각 유저 로그인
		driver.get(CommonValues.SERVER_URL + "/logout");
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver, USER_PRESENTER + "@gmail.com");
		
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");
		
		driver_organizer.get(CommonValues.SERVER_URL + "/logout");
		comm.loginseminar(driver_organizer, USER_ORGANIZER + "@gmail.com");
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//81. 비공개세미나(secret) 저장완료 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
	@Test(priority=71, dependsOnMethods = {"draft_privateS"}, enabled = true)
	public void draft_privateS_detailview() throws Exception {
		String failMsg = "";
		
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		//[게시자]
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = "1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] cannot find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (!buttonTest(driver_publisher, "post", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find post button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = "2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (!buttonTest(driver_master, "post", false)) {
			failMsg = failMsg + "\n 2-5.[master] find post button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = "3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (buttonTest(driver, "post", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] find post button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = "4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  게시하기 버튼 확인
		if (buttonTest(driver_organizer, "post", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] find post button (private-normal seminar)";
		}	
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//82. 비공개세미나(secret), 저장완료 세미나 게시(게시자), 예정세미나 상세화면 :  게시자, 채널마스터, 발표자, 운영자 확인
	@Test(priority=82, dependsOnMethods = {"draft_private"}, enabled = true)
	public void post_privateS_detailview() throws Exception {
		String failMsg = "";	
		
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		// 게시 버튼 클릭
		buttonTest(driver_publisher, "post", true);
		Thread.sleep(500);
		String postmsg = String.format(MSG_POST, "rsrup1");
		try {
			if (driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(postmsg)) {
				failMsg = failMsg + "\n 0-1. post popup msg [Expected]" + postmsg + " [Actual]"
						+ driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			// confirm
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n 0-2. popup error(exception) : " + e.getMessage();
		}
		
		//[게시자]
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}		
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] cannot find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  연습하기 버튼 확인
		if (buttonTest(driver_publisher, "practice", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find practice button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (!buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (buttonTest(driver_master, "practice", false)) {
			failMsg = failMsg + "\n 2-5.[master] find practice button (private-normal seminar)";
		}
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}	
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (!buttonTest(driver, "practice", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find practice button (private-normal seminar)";
		}
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		// 연습하기 버튼 확인
		if (!buttonTest(driver_organizer, "practice", false)) {
			failMsg = failMsg + "\n 4-5.[organizer] cannot find practice button (private-normal seminar)";
		}

		
		//delete seminar
		if (buttonTest(driver_publisher, "trash", true)) {
			Thread.sleep(500);
			
			if(!driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText().contentEquals(MSG_DELETE)) {
				failMsg = failMsg + "\n 1. post popup msg [Expected]" + MSG_DELETE 
						+ " [Actual]" + driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_BODY)).getText();
			}
			
			driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(500);

		}		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	//83. 비공개세미나(secret), standby 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=83, enabled = true)
	public void standby_privateS() throws Exception {
		String failMsg = "";
		
		Date time = new Date();
		Calendar startTime = Calendar.getInstance(); 
		startTime.setTime(time);
		SimpleDateFormat format2 = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		
		
		//create seminar after 2minute
		seminarTitle = format2.format(startTime.getTime()) + "_privateS2";
		String seminarUri = createSeminar(seminarTitle, true, SEMINAR_TYPE_PRIVATE_SECRET);
		seminarID = seminarUri;
		
		// post seminar
		CommonValues comm = new CommonValues();
		comm.postSeminar(driver_publisher, seminarID);
		

		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		//[게시자]
		// 링크 버튼 확인
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}		
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find enter button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 2-5.[master] find enter button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver, "enter", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find enter button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_organizer, "enter", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find enter button (private-normal seminar)";
		}			
		
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//84. 비공개세미나(secret), onair 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=84, enabled = true)
	public void onair_privateS() throws Exception {
		String failMsg = "";
		
		// goto seminar info(발표자)
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);

		// click enter(new tab)
		buttonTest(driver, "enter", true);
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
		
		//설정팝업 확인
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//start seminar
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_BTN)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_STARTSEMINAR_NOW_BTN)).click();
		Thread.sleep(1000);
		
		//[게시자]
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_publisher, "enter", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find enter button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (!buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] cannot find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_master, "enter", false)) {
			failMsg = failMsg + "\n 2-5.[master] find enter button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver, "enter", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find enter button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  enter 버튼 확인
		if (!buttonTest(driver_organizer, "enter", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find enter button (private-normal seminar)";
		}			
		
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		

	//85. 비공개세미나(일반), 지난 세미나 생성. 상세화면 : 게시자, 채널마스터, 발표자, 운영자 확인	
	@Test(priority=85, enabled = true)
	public void past_privateS() throws Exception {
		String failMsg = "";
		
		// goto seminar info(발표자)
		driver.get(CommonValues.SERVER_URL + CommonValues.SEMINAR_LINK + seminarID);
		buttonTest(driver, "enter", true);
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
		
		//설정팝업 확인
		CommonValues comm = new CommonValues();
		comm.checkSettingpopup(driver);
		
		//close seminar
		driver.findElement(By.id("btn-exit")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//section[@id='confirm-dialog']//div[@class='buttons align-center']/button[1]")).click();
		Thread.sleep(1000);
		if(isElementPresent(driver, By.xpath(OnAirRoom.XPATH_ROOM_TOAST))) {
			if(!driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText().contentEquals(CommonValues.SEMINAR_CLOSE_MSG)) {
				failMsg = failMsg + "\n0-1. toast message. (presenter) [Expected]" + CommonValues.SEMINAR_CLOSE_MSG
						 + " [Actual]" + driver.findElement(By.xpath(OnAirRoom.XPATH_ROOM_TOAST)).getText();
			}
		} else {
			failMsg = failMsg + "\n0-2. cannot find toast (presenter)";
		}
		
		//[게시자]
		driver_publisher.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_publisher, "lock", false)) {
			failMsg = failMsg + "\n1-0. [publisher] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_publisher, "link", false)) {
			failMsg = failMsg + "\n1-1. [publisher] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_publisher, "edit", false)) {
			failMsg = failMsg + "\n 1-2.[publisher] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_publisher, "trash", false)) {
			failMsg = failMsg + "\n 1-3.[publisher] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_publisher, "invite", false)) {
			failMsg = failMsg + "\n 1-4.[publisher] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_publisher, "statistics", false)) {
			failMsg = failMsg + "\n 1-5.[publisher] find statistics button (private-normal seminar)";
		}	
		
		//[master]
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_master, "lock", false)) {
			failMsg = failMsg + "\n2-0. [master] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_master, "link", false)) {
			failMsg = failMsg + "\n2-1. [master] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (!buttonTest(driver_master, "edit", false)) {
			failMsg = failMsg + "\n 2-2.[master] cannot find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_master, "trash", false)) {
			failMsg = failMsg + "\n 2-3.[master] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_master, "invite", false)) {
			failMsg = failMsg + "\n 2-4.[master] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_master, "statistics", false)) {
			failMsg = failMsg + "\n 2-5.[master] find statistics button (private-normal seminar)";
		}	
				
		//[발표자]
		driver.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver, "lock", false)) {
			failMsg = failMsg + "\n3-0. [presenter] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver, "link", false)) {
			failMsg = failMsg + "\n3-1. [presenter] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver, "edit", false)) {
			failMsg = failMsg + "\n 3-2.[presenter] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver, "trash", false)) {
			failMsg = failMsg + "\n 3-3.[presenter] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver, "invite", false)) {
			failMsg = failMsg + "\n 3-4.[presenter] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver, "statistics", false)) {
			failMsg = failMsg + "\n 3-5.[presenter] cannot find statistics button (private-normal seminar)";
		}	
		
		//[운영자]
		driver_organizer.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		// lock 버튼 확인
		if (!buttonTest(driver_organizer, "lock", false)) {
			failMsg = failMsg + "\n4-0. [organizer] cannot find lock icon (private-normal seminar)";
		}
		// 링크 버튼 확인
		if (buttonTest(driver_organizer, "link", false)) {
			failMsg = failMsg + "\n4-1. [organizer] find link button (private-normal seminar)";
		}
		// 수정 버튼 확인
		if (buttonTest(driver_organizer, "edit", false)) {
			failMsg = failMsg + "\n 4-2.[organizer] find edit button (private-normal seminar)";
		}
		// 삭제 버튼 확인
		if (buttonTest(driver_organizer, "trash", false)) {
			failMsg = failMsg + "\n 4-3.[organizer] find trash button (private-normal seminar)";
		}
		
		// 초대 버튼 확인
		if (buttonTest(driver_organizer, "invite", false)) {
			failMsg = failMsg + "\n 4-4.[organizer] find invite button (private-normal seminar)";
		}
		//  statistics 버튼 확인
		if (!buttonTest(driver_organizer, "statistics", false)) {
			failMsg = failMsg + "\n 5-5.[organizer] cannot find statistics button (private-normal seminar)";
		}			
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//86. 비공개세미나(secret), 지난 세미나 생성. 채널마스터가 채널 리스트에서 확인(rsrsup1)
	@Test(priority=86, enabled = true)
	public void past_privateS_channellist_master() throws Exception {
		String failMsg = "";
		
		//channel list
		driver_master.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(100);
		driver_master.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		List<WebElement> channelList = driver_master.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		//click rsrsup1 channel 
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(USER_MASTER)) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}		
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver_master;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 5 ; i++) {
			if (isElementPresent(driver_master, By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (isElementPresent(driver_master, By.xpath(listitem))) {
			failMsg = "1. find secret private seminar in channel seminar list";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//87. 비공개세미나(secret), 지난 세미나 생성. 채널멤버가 채널 리스트에서 확인(rsrsup4)
	@Test(priority=87, enabled = true)
	public void past_privateS_channellist_member() throws Exception {
		String failMsg = "";
		
		// master logout & member login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_master, "rsrsup4" + "@gmail.com");
		Thread.sleep(100);
		
		//channel list
		driver_master.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
		Thread.sleep(100);
		driver_master.findElement(By.xpath("//ul[@class='nav-seminar']//a[@href='/channel/list']")).click();
		Thread.sleep(500);
		
		List<WebElement> channelList = driver_master.findElements(By.xpath("//li[@class='ChannelListItem_ChannelListItem__3PdRK']"));
		
		//click rsrsup1 channel 
		for(int i = 0 ; i < channelList.size() ; i ++) {
			if(channelList.get(i).findElement(By.xpath(".//div[@class='content_title']")).getText().contentEquals(USER_MASTER)) {
				//click member channel
				channelList.get(i).findElement(By.xpath(".//div[@class='img-box']")).click();
				Thread.sleep(500);
			}		
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + seminarTitle + "']/parent::*";
		JavascriptExecutor js = (JavascriptExecutor) driver_master;
		// infinite scroll 20번 아래로 내려가면서 확인
		for (int i = 0 ; i < 5 ; i++) {
			if (isElementPresent(driver_master, By.xpath(listitem)))
				break;	
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000);
		}
		
		if (isElementPresent(driver_master, By.xpath(listitem))) {
			failMsg = "1. find secret private seminar in channel seminar list";
		}
		
		// member logout & master login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");
		Thread.sleep(100);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	

	//88. 비공개세미나(secret), 지난 세미나 생성. 다른 채널 멤버가 URL로 확인(rsrsup9)
	@Test(priority=88, enabled = true)
	public void past_privateS_channellist_guest() throws Exception {
		String failMsg = "";
		
		// master logout & rsrsup9 login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		CommonValues comm = new CommonValues();
		comm.loginseminar(driver_master, "rsrsup9" + "@gmail.com");
		Thread.sleep(100);
		
		driver_master.get(CommonValues.SERVER_URL + CommonValues.DETAIL_VIEW + seminarID);
		Thread.sleep(500);
		
		if (!driver_master.findElement(By.xpath("//p[@class='not-avaliable__content']")).getText().contentEquals(MSG_PRIVATE_SEMINAR_VIEW)) {
			failMsg = failMsg + "\n 1. private seminar view msg [Expected]" + MSG_PRIVATE_SEMINAR_VIEW + " [Actual]"
					+ driver_master.findElement(By.xpath("//p[@class='not-avaliable__content']")).getText();
		}
		//click goback
		driver_master.findElement(By.xpath("//div[@class='SeminarView_notAvaliable__19k2A']/button[1]")).click();
		
		// member logout & master login
		driver_master.get(CommonValues.SERVER_URL + "/logout");
		Thread.sleep(500);
		comm.loginseminar(driver_master, USER_MASTER + "@gmail.com");
		Thread.sleep(100);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}		
	
	public String createSeminar(String seminarName, boolean isnow, int seminaType) throws Exception {
		
		if(!driver_publisher.getCurrentUrl().contains(CommonValues.SERVER_URL + CommonValues.LIST_URI)) {
			driver_publisher.get(CommonValues.SERVER_URL + CommonValues.LIST_URI);
			Thread.sleep(500);
		}
		driver_publisher.findElement(By.xpath("//div[@class='l-right']/button[1]")).click();
	    Thread.sleep(2000);
	    
		//channel
		//click channel select
		driver_publisher.findElement(By.xpath("//div[@class='wrap-channel-option']//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(500);

		// channel popup : channel list
		List<WebElement> channelList = driver_publisher
				.findElements(By.xpath("//div[@class='radio-channelId']/div[@class='Radio_radioBox__2VtPF radio']"));

		for (int i = 0; i < channelList.size(); i++) {
			if (channelList.get(i).findElement(By.xpath(".//span[1]")).getText().contentEquals("rsrsup1")) {
				// click second channel
				channelList.get(i).findElement(By.xpath(".//span[1]")).click();
			}
		}
		Thread.sleep(500);
		
		// click confirm
		driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(2000);
		
		CommonValues comm = new CommonValues();
		comm.setCreateSeminar(driver_publisher, seminarName, isnow);
		Thread.sleep(500);
		
		if(seminaType > 0 ) {
			driver_publisher.findElement(By.xpath("//div[@id='seminar-type']")).click();
			Thread.sleep(100);
			driver_publisher.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
			Thread.sleep(100);
			if(seminaType > 1 ) {
				driver_publisher.findElement(By.xpath("//div[@class='checkbox']")).click();
			}
		}
		Thread.sleep(500);
		
		// 세미나 멤버 
		driver_publisher.findElement(By.xpath("//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]")).click();
		Thread.sleep(500);
		//presenter popup
		driver_publisher.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']//button[@class='btn btn-basic btn-m ']")).click();
		Thread.sleep(500);
		
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_PRESENTER);
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		//select rsrsup1
		driver_publisher.findElement(By.xpath("//li[@role='presentation']/span[@class='member-email']")).click();
		Thread.sleep(500);

		// click selected
		driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		Thread.sleep(500);
		
		//delete member 
		List<WebElement> member_pres = driver_publisher.findElements(By.xpath("//div[@role='presentation']"));
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(1000);
		
		// organizer popup
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CommonValues.CREATE_MEMBER_ORGANIZER_B+ "']";
		driver_publisher.findElement(By.xpath(xpath)).click();
		Thread.sleep(500);
		
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).clear();
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).sendKeys(USER_ORGANIZER);
		driver_publisher.findElement(By.xpath("//input[@class='search-input']")).sendKeys(Keys.ENTER);
		Thread.sleep(500);
		
		// select 1 (defalut 0 + 1)
		driver_publisher.findElement(By.xpath("//li[@role='presentation']//span[@class='member-name']")).click();
		
		// click selected
		driver_publisher.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		Thread.sleep(1000);
		
		// click [save] seminar
		driver_publisher.findElement(By.xpath(CommonValues.XPATH_CREATESEMINAR_SAVE_BTN)).click();
		Thread.sleep(1000);
		String seminarUri = comm.findSeminarIDInList(driver_publisher, seminarName);
		
		System.out.println(" currentURL : " + driver_publisher.getCurrentUrl());
		System.out.println(" seminarUri : " + seminarUri);
		
	    return seminarUri;
	}	
	
	public boolean buttonTest(WebDriver wd, String buttonType, boolean click) {
		String xpath = "";
		
		switch (buttonType) {
		case "lock":
			xpath = "//div[@class='ricon-lock']";
			break;
		case "link":
			xpath = "//div[@class='ricon ricon-link']";
			break;
		case "edit":
			xpath = "//div[@class='ricon ricon-edit']";
			break;	
		case "trash":
			xpath = "//div[@class='ricon ricon-trash']";
			break;	
		case "invite":
			xpath = "//div[@class='SeminarView_right__23lrS right']/button[1]";
			break;
		case "post":
			xpath = "//button[@class='btn btn-secondary-light btn-auto actionButton']";
			break;	
		case "enter":
			xpath = "//button[@class='btn btn-primary btn-auto actionButton']";
			break;	
		case "statistics":
			xpath = "//button[@class='btn btn-cancel btn-auto actionButton']";
			break;		
		case "practice":
			xpath = "//button[@class='btn btn-basic btn-auto SeminarView_actionButton__3tFHP']";
			break;		
		default:
			break;
		}
		if (click) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) wd;
				js.executeScript("arguments[0].scrollIntoView();", wd.findElement(By.xpath(xpath)));
				
				wd.findElement(By.xpath(xpath)).click();
				return true;
			} catch (NoSuchElementException e) {
				System.out.println("NoSuchElementException : " + xpath);
				return false;
			}

		} else {
			try {
				wd.findElement(By.xpath(xpath));
				return true;
			} catch (NoSuchElementException e) {
				System.out.println("NoSuchElementException : " + xpath);
				return false;
			}
		}
	}
	
	
	public void takescreenshot(WebDriver e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}	

	public void clearAttributeValue(WebElement el) {
		while (!el.getAttribute("value").isEmpty())
			el.sendKeys(Keys.BACK_SPACE);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		driver_publisher.quit();
		driver_master.quit();
		driver_organizer.quit();
		driver_guest.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(WebDriver wd, By by) {
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
	  
	private boolean findAlert(WebDriver wd, String msg) {
		System.out.println("findAlert method : " + wd);
		
		/*
		if (ExpectedConditions.alertIsPresent().apply(wd) == null) {
			System.out.println("cannot find alert : " + msg);
			return false;

		} else {
			// 알림창이 존재하면 알림창 확인을 누를것
			assertEquals(closeAlertAndGetItsText_webdriver(wd), msg);
			System.out.println("find alert : " + msg);
			return true;
		}
		*/
		
		try {
			assertEquals(closeAlertAndGetItsText_webdriver(wd), msg);
			return true;
		} catch (Exception e ) {
			System.out.println("Exception : " + e );
			return false;
		} 
		
	
	}

	private String closeAlertAndGetItsText_webdriver(WebDriver wd) {
		try {
			Alert alert = wd.switchTo().alert();
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
