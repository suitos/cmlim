package seminartest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CommonValues {
	public static final String WEB_CHROME_DRIVER_PATH = System.getProperty("user.dir") + "/driver/chromedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_PATH = System.getProperty("user.dir") + "/driver/geckodriver.exe";
	public static final String WEB_EDGE_DRIVER_PATH = System.getProperty("user.dir") + "/driver/msedgedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_LINUX_PATH = "/tools/webdriver/geckodriver";
	
	public static boolean FOR_JENKINS = true;
	
	public static String SERVER_URL = "https://alpha.remoteseminar.com";

	public static int SETTING_ROOM_ATTENDEELIST = 4;
	public static boolean SETTING_ROOM_OPERATOR_MENU = false;
	
	/*user
	 * 	rsrsup1 |- rsrsup2
	 * 	member	|- rsrsup3
	 * 	total8	|- rsrsup4
	 * 			|- rsrsup6
	 * 			|- rsrsup7
	 * 			|- rsrsup8
	 * 			|- rsrsup11  
	 * 			|- rsrsup12
	 *  rsrsup9 
	 *  rsrsup10 : 회원가입 안한 계정
	 */
	public static String[] CHANNEL_MEMBERS = {"rsrsup2@gmail.com", "rsrsup3@gmail.com", "rsrsup4@gmail.com",
			"rsrsup6@gmail.com", "rsrsup7@gmail.com", "rsrsup8@gmail.com", "rsrsup11@gmail.com","rsrsup12@gmail.com",};
	public static String USEREMAIL= "rsrsup1@gmail.com";
	public static String USEREMAIL_CHID= "rsrsup1"; //USEREMAIL's channel ID
	public static String USERPW = "!Rsupport0";
	public static String PLACEHOLDER_LOGIN_EMAIL = "Enter your email address.";
	public static String PLACEHOLDER_LOGIN_PW = "Enter the password.";
	public static String SIGNUP_TXT = "Sign Up";
	public static String SIGNUP_LINK = "#form";
	
	public static String USEREMAIL_JOIN_TRY= "rsrsup10@gmail.com"; 
	public static String USEREMAIL_RSUP4= "rsrsup4@gmail.com"; //joind. for defalut values.
	public static String USEREMAIL_RSUP9= "rsrsup9@gmail.com"; //joind. for defalut values.
	public static String USERMAIL_RSUP12= "rsrsup12@gmail.com";
	public static String CHANGEPW_RSUP12= "!Rsupport00";
	//public static String USERPW_JOIN= "!111qqqq";
	public static String USEREMAIL_PRES= "rsrsup2@gmail.com"; //joined email & channel member
	public static String USEREMAIL_PRES2= "rsrsup3@gmail.com"; //joined email & channel member
	
	//for admin test
	public static String USEREMAIL_ADMIN = "adm.rsrsup@gmail.com"; //for channel
	public static String USERNICKNAME_ADMIN = "adminnickname";
	public static String USEREMAIL_JP = "rsrsupjp@gmail.com";
	public static String USERNICKNAME_JP = "rsrsupjpnickname";
	public static String CHANNELNAME_JP = "rsrsupjp";
	
	//login test
	public static String PASSWORD_FIND_URL = "/account/password-find";
	public static String PASSWORD_CHANGE_URL = "/account/password-change?key=";
	public static String INTRODUCE_URL = "https://www.remoteseminar.com/ja/";
	
	//join test
	public static String DELETE_ACCOUNT_URL = "/settings/delete-account";
	public static String REQUIRED_VALUE = "Required.";
	public static String INCORRECT_EMAIL_FORMAT = "Incorrect email format.";
	public static String SAME_EMAIL = "This email is already in use. Enter a different one."; //check lang
	public static String INCORRECT_AUTHCODE_FORMAT = "Invalid authentication code format"; 
	public static String INVALID_AUTHCODE_MSG = "Invalid verification number."; 
	public static String EMAIL_SEND_MSG = "Authentication code has been sent."; 
	public static String MSG_MARKETING_AGREEMENT = "I agree to receive product updates and event newsletters from Rsupport.(Select)";
	
	public static String AUTH_CODE = "1111111";

	public static String USERPW_WRONG_ERROR = "Password must include alphabets, numbers and special characters with a minimum of 8 characters.\nAlso, password cannot be the same as the email ID.";
	public static String USERPW_WRONG_ERROR2 = "Password cannot be the same as email ID.";
	public static String USERPW_WRONG_ERROR3 = "Consecutive characters/numbers cannot be used.";
	public static String USERPW_JOIN= "!Rsupport0";
	public static String USERNICKNAME_JOIN= "NickName";
	public static String USER_PHONE= "0100000000";
	public static String USER_COMPANY = "Company name";
	public static String USER_POSITION = "Position";
	public static String EMAIL_WRONG_CASE[]= {"test", "test@rsupport", "test.com" };
	public static String USERPW_WRONG_CASE[]= {"!111qqq", "11111111", "qqqqqqqq", "!@#$%^&*", "!111qqqqaaaaaaaaaaaaaaaaaa"} ;
	public static String USERPW_WRONG_CASE2[]= {"1234qwer", "1111qwer", "1357abcd", "4567qwer"} ;
	public static String NICKNAME_WRONG_CASE[]= {"NickName!@#$%", "$%^^&*NickName" };
	public static String PHONE_WRONG_CASE[]= {"0100000000abc", "0100000000!@#", "def0100000000", "^&*0100000000"};
	public static String COMPANY_WRONG_CASE[]= {"Company name!@!#", "$%&Company name"};
	public static String POSITION_WRONG_CASE[]= {"Position!@!#", "$%&Position"};
	
	public static String PLACEHOLDER_EMAIL = "Enter your email address.";
	public static String PLACEHOLDER_AUTHCODE = "Enter authentication code";
	public static String PLACEHOLDER_PASSWORD = "Enter the password.";
	public static String PLACEHOLDER_NICKNAME = "Enter a nickname.";
	public static String PLACEHOLDER_PHONE = "Enter the phone number.";
	public static String PLACEHOLDER_COMPANY = "Enter company name.";
	public static String PLACEHOLDER_POSITION = "Enter you job title.";
	public static String PLACEHOLDER_ENTRANCECODE = "Connection code";
	
	public static String PW_CHANGE_URL = "/account/password-change";
	public static String PLACEHOLDER_CURRENT_PW = "Enter current password";
	public static String PLACEHOLDER_NEW_PW = "Enter new password";
	public static String PLACEHOLDER_CONFIRM_PW = "Confirm password";
	public static String VALID_NEW_PW = "valid11!!";
	public static String CURRENT_PW_ERROR_MSG = "Please, confirm your current password.";
	public static String PW_MISSMATCH_ERROR_MSG = "Entered passwords do not match. Please, check and try again.";
	public static String PW_NEW_SAME_ERROR_MSG = "Entered passwords do not match. Please, check and try again.";
	public static String PW_CHANGE_SUCCESS_MSG = "Password has been changed.";
	
	//Long String
	public static String TWENTY_ONE = "11111111111111111111";
	public static String TWENTY_A = "aaaaaaaaaaaaaaaaaaaa";
	
	
	//List View
	public static String LIST_URI = "/seminar/list";
	public static String INTRO_URI = "/seminar/intro";
	//Common
	public static String CHANNEL_URI = "/channel/list";
	public static String SIGNUP_URI = "/signup/form";
	
	//CreateSeminar View
	public static String CREATE_URI = "/seminar/edit";
	public static String CREATE_SUCCESS_URI = "/seminar/publish-complete";
	public static String CREATE_PRESENTATION_URI = "/seminar/document";
	public static String CREATE_MEMBER_URI = "/seminar/members";
	public static String CREATE_WAITING_URI = "/seminar/waiting";
	
	public static String PLACEHOLDER_TITLE = "example) Building a Telework environment by Rsupport";
	public static String DEFAULT_ATTEND = "1000";
	public static String VALUE_GUIDE = "Enter the required information.";
	public static String LONG_TITEL_ERROR = "Seminar title cannot be longer than 40 characters.";
	public static String OVER_ATTEND_ERROR = "The maximum number of attendees is 1000.";
	public static String OVER_FILES_ERROR = "Maximum number of files has been exceeded.";
	public static String OVER_YOUTUBE_ERROR = "You have exceeded the max. number of YouTube registrations.";
	
	public static String VALID_SNAME = "auto test seminar";
	public static String BANNER_DEFAULT_MSG = "The presenter set in the member setting is entered.";
	public static String[] SEMINAR_TIME = {"30 minutes", "1 hours", "1 hours30 minutes", "2 hours", "2 hours30 minutes", "3 hours", "3 hours30 minutes", "4 hours", "4 hours30 minutes", "5 hours"};
	
	
	public static String YOUTUBE_URL[] = {"https://www.youtube.com/watch?v=miS5X2tGH2s", "https://www.youtube.com/watch?v=xzmaJ9FRW5w", "https://www.youtube.com/watch?v=8v2kVtkUF_s",
			"https://www.youtube.com/watch?v=gWEbrKceu8I&feature=youtu.be", "https://www.youtube.com/watch?v=7KviCqeAejg", "https://www.youtube.com/watch?v=5iSjqOPqC-Y&feature=youtu.be",
			"https://www.youtube.com/watch?v=xILILfIZlIw", "https://www.youtube.com/watch?v=ZTTlcaMEZhc", "https://www.youtube.com/watch?v=rSQHhwRstiY", "https://www.youtube.com/watch?v=WbWLTM206Po",
			"https://www.youtube.com/watch?v=NMh5prtXKYA"};
	public static String YOUTUBE_TITLE[] = {"RemoteMeeting", "RemoteMeeting Intro", "RemoteWorks", "RemoteWOL", "RemoteView6.0", "TAAS", "Innovation Tech", "Webviewer", "TECH DAY 2018", "MWC Barcelona",
			"Rsupport_Booth_Review"};
	public static String YOUTUBE_INVAILD_URL = "https://www.youtube.com/embed/dkZdxNhFHBMaaaaaaa";
	public static String TESTFILE_PATH = System.getProperty("user.dir") + "\\testdata\\";
	public static String TESTFILE_PATH_MAC = System.getProperty("user.dir") + "/testdata/";
	public static String TESTFILE_LIST[] = {"image.png", "text.txt", "ppt.pptx", "ppt2.ppt", "doc1.docx", "doc2.doc", "hwp.hwp", "image3.gif" ,"image2.jpg", "image4.jpeg", "image4.jpeg"}; 
	public static String TESTFILE_INVALID_LIST[] = {"excel.xlsx", "exe.exe", "zip.zip"}; 
	public static String ERROR_FILEFORMAT= "This file format is not supported.";
	public static String ERROR_YOUTUBELINK = "Please, enter a valid YouTube link.";
	public static String PLACEHOLDER_FILE_DES = "Enter a description to view with file.";

	public static String CREATE_MEMBER_ORGANIZER_B = "Set the organizer";
	public static String TOAST_CHANGE_DEFAULT_CHANNEL = "Default channel has been updated.";
	public static String MSG_DELETE_SEMINAR = "Do you want to delete the seminar?";
	public static String MSG_SAVE_REGISTERED_SEMINAR = "Seminar information has been changed.\n" + 
			"Do you want to send updated details to the attendees?";
	public static String MSG_POST_SEMINAR = "The seminar will be posted on the channel %s.\n" + 
			"Once posted, it cannot be changed.";
	public static String MSG_COPY_SEMINAR_LINK = "Seminar link copied.";
	public static String MSG_READYSEMINAR_EDIT = "Seminars in progress cannot be edited.";
	public static String MSG_EMPTY_MEMBER_POPUP = "Register a channel member.";
	public static String MSG_NORESULT_MEMBER_POPUP = "No results were found for your search.\n" + 
			"Search for member nickname or email.";

	
	//CreateSeminar Invite
	public static String INVITETEMP_VIEW ="/seminar/invite-temp";
	public static String INVITE_VIEW ="/seminar/invite";
	public static String INVITE_LIST ="/seminar/invite-list";
	public static String INVITE_ERROR_EMAIL ="Incorrect email format.";
	public static String INVITE_ERROR_EMAIL_DUPLICATE ="This email already exists. Duplicate emails will be deleted and registered.";
	public static String INVITE_ERROR_EMPTYLIST ="There is no email added.\nInvite attendees by adding emails.";
	public static String INVITE_MSG_SEND ="Do you want to send the email to the participants?";
	public static String MSG_SELCECT_EMAIL ="Select the email.";
	public static String MSG_SEND_EMAIL ="Email has been sent.";
	
	//Seminar Detail View
	public static String DETAIL_VIEW = "/seminar/view";
	public static String SEMINAR_LINK = "/seminar/pl";
	
	//Seminar statistics
	public static String URI_STATISTIC = "/seminar/statistics";
	
	//Seminar Room
	public static String SEMINAR_ROOM= "/room";
	public static String SEMINAR_CLOSE_MSG= "The seminar has ended.";
	public static String SEMINAR_STATUS_ONAIR = "ON AIR";
	public static String SEMINAR_ROOM_DELETE_TOAST = "It has been deleted.";
	public static String SEMINAR_ROOM_ADD_TOAST = "Saved";
	public static String SEMINAR_ROOM_URL_EMPTY_TOAST = "Enter the URL.";
	public static String SEMINAR_ROOM_URL_INVALID_TOAST = "Verify the URL.";
	public static String SEMINAR_ROOM_NO_VIDEO_TOAST = "There is no video selected.";
	
	//Attendees
	public static String SEMINAR_CLOSED_URI = "/seminar/close";
	public static String ATTENDEES_VIEW_TITLE = "Seminar attendance information";
	public static String ATTENDEES_NICKNAME_MSG= "Enter your name or nickname.";
	public static String ATTENDEES_EMAIL_MSG= "Incorrect email format.";
	public static String ATTENDEES_EMAIL = "attendees@rsupport.com";
	public static String ATTENDEES_NICKNAME = "attendees";
	public static String ATTENDEES_DUPLICATE_MSG = "There is already an attendee using the email %s. Please, use a different one.";
	public static String ATTENDEES_FULL_MSG = "This seminar is full.";
	public static String ATTENDEES_ENTRANCECODE_MSG = "Please, check the connection code.";
	
	//channel
	public static String CHANNEL_VIEW_URL = "/channel/view";
	public static String CHANNEL_EDIT_URL = "/channel/edit";
	public static String CHANNEL_LEVEL_MASTER = "My level : Master";
	public static String CHANNEL_DES_PLACEHOLDER = "Register the seminar on My Seminar.";

	public static String CHANNEL_INVITE_EMAIL_ERROR = "Incorrect email format.";
	public static String CHANNEL_INVITE_MASTER_EMAIL_ERROR = "Channel master account.";
	public static String CHANNEL_INVITE_DUPLICATE_EMAIL_ERROR = "This email is already in use.";
	public static String CHANNEL_INVITE_ALREADY_EMAIL_ERROR = "Invitation has already been sent to this email. Resend from member list.";
	public static String CHANNEL_INVITE_CLIPBOARD_ERROR = "There is an invalid email. Check and add again.";
	public static String CHANNEL_INVITE_CLIPBOARD_EMAIL = "test1@rsupport.com test2@rsupport.com test3@rsupport.com test4@rsupport.com test5@rsupport.com "
			+ "test6@rsupport.com test7@rsupport.com test8@rsupport.com test9@rsupport.com test0@rsupport.com";
	
	public static String[] CHANNEL_ID_INVALID = {"  ", "!abc", "abc!@", "a$bc", "ABCD"};
	public static String CHANNEL_PRIVATE_MSG = "Private Channel";
	public static String CHANNEL_PUBLIC_MSG = "Public Channel";
	
	//PreviewSeminar
	public static String PREVIEW_DETAILED_URI = "/seminar/preview-info";
	public static String PREVIEW_STANBY_URI = "/seminar/preview-display";
	public static String PREVIEW_SURVEY_URI = "/seminar/preview-survey";
	public static String POST_URI = "/seminar/publish-complete";
	public static String Preview_Tooltip = "seminar.onPreview.tooltip";
	public static String PREVIEW_DEFAULT = "Preview seminar in progress";
	public static String SURVEY_DEFAULT = "There is no seminar survey.";
	
	//ExitSeminar
	public static String PRESENTS_LIST = "rsrsup8 rsrsup12 rsrsup11";
	
	//Survey
	public static String SURVEY_TITLE = "SurveyTitle Test";
	public static String SURVEY_DESCRIPTION = "SurveyDescription_Test";
	public static String[] SURVEY_QUESTION = {"SurveyQuestion_Test","SurveyQuestion_Test1","SurveyQuestion_Test2"};

	//xpath
	public static String XPATH_CREATESEMINAR_SAVE_BTN = "//button[@class='btn btn-primary btn-l ']";
	public static String XPATH_CREATESEMINAR_TAB1 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[1]";
	public static String XPATH_CREATESEMINAR_TAB2 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[2]";
	public static String XPATH_CREATESEMINAR_TAB3 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[3]";
	public static String XPATH_CREATESEMINAR_TAB4 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[4]";
	public static String XPATH_CREATESEMINAR_TAB5 = "//div[@class='TabNavigation_tabWrap__3jzQi tab-wrap']/ul[1]/li[5]";
	public static String XPATH_CREATESEMINAR_TITLE = "//div[@class='seminar-title']/input[1]";
	public static String XPATH_CREATESEMINAR_ATTENDEES = "//div[@class='count-attendants']/input[1]";
	public static String XPATH_CREATESEMINAR_CHANNELLIST = "//div[@class='ChangeChannel_changeChannel__3-kW6']/ul/li";
	
	public static String XPATH_CREATESEMINAR_MEMBERPRESENTER_LIST = "//div[@role='presentation']";
	public static String XPATH_CREATESEMINAR_MEMBERPOPUP_LIST = "//li[@role='presentation']";
	public static String XPATH_CREATESEMINAR_MEMBERPOPUP_EMAIL = "./span[@class='member-email']";
	
	public static String XPATH_SEMINARVIEW_POST = "//button[@class='btn btn-secondary-light btn-auto actionButton']";
	public static String XPATH_SEMINARVIEW_ENTER = "//button[@class='btn btn-primary btn-auto actionButton']";
	public static String XPATH_SEMINARVIEW_EDIT = "//div[@class='ricon ricon-edit']";
	

	public static String XPATH_ROOM_STARTSEMINAR_BTN = "//button[@class='btn btn-primary btn-xl seminar-start']";
	public static String XPATH_ROOM_STARTSEMINAR_NOW_BTN = "//div[@class='countdown-footer']/button";
	public static String XPATH_ROOM_STARTSEMINAR_EXIT_BTN = "//button[@id='btn-exit']";
	public static String XPATH_ROOM_STARTSEMINAR_EXIT_POPUPBTN = "//div[@id='seminar-close-dialog']//button";
	
	public static String XPATH_ROOM_SETTING_TITLE = "//div[@id='device-setting-wrap']/div[@class='dialog-header']";
	public static String XPATH_ROOM_SETTING_CONFIRM_BTN = "//div[@class='buttons align-center']/button";
	
	public static String XPATH_SEMINARLIST_TAB = "//li[@class='seminarList__category__item']";
	public static String XPATH_MODAL_BODY = "//div[@class='modal-body']";
	public static String XPATH_MODAL_FOOTER = "//div[@class='modal-footer']";
	
	public void setDriverProperty(String browser) {
		String os = System.getProperty("os.name").toLowerCase();
		if (browser.contains("Chrome")) {
			if (os.contains("mac")) {
				 String path = System.getenv("DRIVER_HOME") + "/chromedriver";
				 System.setProperty("webdriver.chrome.driver", path);
			} else {
				System.setProperty("webdriver.chrome.driver", CommonValues.WEB_CHROME_DRIVER_PATH);
			}
		} else if(browser.contains("Edge")) {
			if (os.contains("mac")) {
				 String path = System.getenv("DRIVER_HOME") + "/msedgedriver";
				 System.setProperty("webdriver.edge.driver", path);
			} else if (os.contains("wndows")) {
				System.setProperty("webdriver.edge.driver", CommonValues.WEB_EDGE_DRIVER_PATH);
			}
		} else {
			System.setProperty("webdriver.gecko.driver", CommonValues.WEB_FIREFOX_DRIVER_PATH);
		}
	}
	
	public WebDriver setDriver(WebDriver driver, String browser, String lang, boolean presenter) {
		if (browser.contains("Chrome")) {
			ChromeOptions options = new ChromeOptions();
		    options.addArguments(lang);
		    //options.addArguments("headless");
		    //options.addArguments("disable-gpu");
		    
		    if(presenter) {
		    	options.addArguments("auto-select-desktop-capture-source=Entire screen");
			    
			    
			    Map<String, Object> prefs = new HashMap<>();

			    // with this chrome still asks for permission
				prefs.put("profile.managed_default_content_settings.media_stream", 1);
				prefs.put("profile.managed_default_content_settings.media_stream_camera", 1);
				prefs.put("profile.managed_default_content_settings.media_stream_mic", 1);

				// and this prevents chrome from starting
				prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.setting", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.last_used", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.setting", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.last_used", 1);
			    
				options.setExperimentalOption("prefs", prefs);
				
		    }
		    driver = new ChromeDriver(options);
		} else if (browser.contains("Edge")) {
			EdgeOptions options = new EdgeOptions();

			//options.addArguments(lang);
		    //options.addArguments("disable-gpu");
		    
		    options.setCapability("dom.webnotifications.enabled", 1);
		    options.setCapability("permissions.default.microphone", 1);
		    options.setCapability("permissions.default.camera", 1);

		    Map<String, Object> prefs = new HashMap<>();
		    // with this chrome still asks for permission
			prefs.put("profile.managed_default_content_settings.media_stream", 1);
			prefs.put("profile.managed_default_content_settings.media_stream_camera", 1);
			prefs.put("profile.managed_default_content_settings.media_stream_mic", 1);

			// and this prevents chrome from starting
			prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.setting", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.last_used", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.setting", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.last_used", 1);
		    
			//options.setExperimentalOption("prefs", prefs);
			
			driver = new EdgeDriver(options);
		} else {
			driver = new FirefoxDriver();
		}
		
		if(browser.contains("_test")){
			FOR_JENKINS = false;
		} else {
			
		}
		
		driver.manage().window().maximize();
		return driver;
	}
	
	public WebDriver setDriver(WebDriver driver, String browser, String lang) {
		driver = setDriver(driver, browser, lang, false);
		return driver;
	}
	
	public void loginseminar(WebDriver driver) throws Exception
	{
		loginseminar(driver, USEREMAIL);
	}
	
	public void loginseminar(WebDriver driver, String user) throws Exception
	{
		loginseminar(driver, user, USERPW);
	}
	
	public void loginseminar(WebDriver driver, String user, String pw) throws Exception {
		if(!driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL) 
				&& !driver.getCurrentUrl().contentEquals(CommonValues.SERVER_URL + "/")) {
			driver.get(CommonValues.SERVER_URL);
		}
	    driver.findElement(By.id("lang")).click();
	    //driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)=''])[1]/following::div[3]")).click();
	    driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    driver.findElement(By.xpath("//input[@name='email']")).clear();
	    driver.findElement(By.xpath("//input[@name='email']")).sendKeys(user);
	    driver.findElement(By.xpath("//input[@name='password']")).clear();
	    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(pw);
	    
	    
	    driver.findElement(By.xpath("//button[@type='submit']")).sendKeys(Keys.ENTER);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    Thread.sleep(1000); 

	    try {
			driver.findElement(By.xpath(XPATH_MODAL_FOOTER + "/button[1]")).click();
			Thread.sleep(1000); 
		} catch (NoSuchElementException e) {
	
		}
	    
	    String listurl = CommonValues.SERVER_URL + CommonValues.LIST_URI;

	    if(!driver.getCurrentUrl().contentEquals(listurl) && !driver.getCurrentUrl().contentEquals(SERVER_URL + INTRO_URI))
	    {
	    	Exception e =  new Exception("login fail!!! : " + driver.getCurrentUrl());
	    	throw e;
	    }
	}
	
	public void setCreateSeminar(WebDriver driver, String title, boolean isnow) throws Exception {
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).clear();
		driver.findElement(By.xpath("//div[@class='seminar-title']/input[1]")).sendKeys(title);

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar cal_today = Calendar.getInstance();
		cal_today.setTime(time);
		cal.setTime(time);
		if(isnow)
			cal.add(Calendar.MINUTE, 4);
		else
			cal.add(Calendar.DAY_OF_MONTH, 2);
		
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String seminarTime = format1.format(cal.getTime());
		//driver.findElement(By.name("startTime")).sendKeys(seminarTime);

		if (!isnow) {
			//click timepicker
			driver.findElement(By.xpath("//div[@class='react-datepicker__input-container']")).click();
			Thread.sleep(500);
			if (cal_today.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
				driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
				Thread.sleep(500);
			}
			
			//Choose Thursday, March 26th, 2020
			String seminaDayForm = "//div[@aria-label='Choose %s, %s %s, %s']";
			String seminarDayPath = String.format(seminaDayForm, getDayofWeek(cal.get(Calendar.DAY_OF_WEEK))
					, getMonth(cal.get(Calendar.MONTH)), getDay(cal.get(Calendar.DAY_OF_MONTH)), cal.get(Calendar.YEAR));
			
			System.out.println("******************seminarDayPath : " + seminarDayPath);
			if (isElementPresent(driver, By.xpath(seminarDayPath))) {
				if (driver.findElements(By.xpath(seminarDayPath)).size() == 2)
					driver.findElements(By.xpath(seminarDayPath)).get(1).click();
				else
					driver.findElement(By.xpath(seminarDayPath)).click();
			} else {
				System.out.println("@@@@@can not find day");
			}
		}
	}
	
	//channel : rsrsup1 으로 선택
	public void setCreateSeminar_setChannel(WebDriver driver) throws Exception {
		driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']")).click();
		Thread.sleep(1000);

		List<WebElement> channelList = driver.findElements(By.xpath(XPATH_CREATESEMINAR_CHANNELLIST));

		for (int i = 0; i < channelList.size(); i++) {
			if (channelList.get(i).findElement(By.xpath(".//label/span[1]")).getText().contentEquals("rsrsup1")) {
				// click second channel
				channelList.get(i).findElement(By.xpath(".//label/span[1]")).click();
			}
		}
		
		//click confirm
		driver.findElement(By.xpath("//div[@class='modal-footer']/button[1]")).click();
		Thread.sleep(500);
		
	}
	
	public void setCreateSeminar_setMember(WebDriver driver, List<String> presenters, List<String> organizers) throws Exception {
		/* rsrsup2 로그인 할 경우 채널 rsrsup1로 변경 및 권한 분리
		 * 마스터 - rsrsup1
		 * 게시자 - rsrsup2
		 * 발표자 - rsrsup8, rsrsup12, rsrsup11
		 * 운영자 - rsrsup3, rsrsup6
		*/
		
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB3)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='MemberSetting_member-info__speaker__iDgdD']/div/button")).click();
		Thread.sleep(1000);
		
		//select 3 (rsrsup + 3) //rsrsup8,rsrsup12,rsrsup11(알파 기준)
		List<WebElement> members = driver.findElements(By.xpath(XPATH_CREATESEMINAR_MEMBERPOPUP_LIST));
		if(members.size() > 0) {
			for (String presenter : presenters) {
				for (WebElement member : members) {
					if(member.findElement(By.xpath(XPATH_CREATESEMINAR_MEMBERPOPUP_EMAIL)).getText().contains(presenter)) {
						member.click();
					}
				}
			}
		} 
		
		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		
		String xpath = "//*[normalize-space(text()) and normalize-space(.)='" + CREATE_MEMBER_ORGANIZER_B+ "']";
		driver.findElement(By.xpath(xpath)).click();
		Thread.sleep(1000);

		//select 2 //rsrsup3,rsrsup6(알파 기준)
		members = driver.findElements(By.xpath(XPATH_CREATESEMINAR_MEMBERPOPUP_LIST));
		if(members.size() > 0) {
			
			for (WebElement member : members) {
				for (String organizer : organizers) {
					if(member.findElement(By.xpath(XPATH_CREATESEMINAR_MEMBERPOPUP_EMAIL)).getText().contains(organizer)) {
						member.click();
					}
				}
			}
		} 

		driver.findElement(By.xpath(CommonValues.XPATH_MODAL_FOOTER + "/button")).click();
		
		//디폴트 발표자 삭제
		List<WebElement> member_pres = driver.findElements(By.xpath(XPATH_CREATESEMINAR_MEMBERPRESENTER_LIST));
		Thread.sleep(500);
		member_pres.get(0).findElement(By.xpath(".//i[@class='ricon-close']")).click();
		Thread.sleep(500);
	}
	public void setCreateSeminar_setSurvey(WebDriver driver) throws Exception {
		
		driver.findElement(By.xpath(XPATH_CREATESEMINAR_TAB5)).click();
		Thread.sleep(500);
		driver.findElement(By.id("surveyTitle")).clear();
		driver.findElement(By.id("surveyTitle")).sendKeys(CommonValues.SURVEY_TITLE);
		driver.findElement(By.id("surveyDescription")).sendKeys(CommonValues.SURVEY_DESCRIPTION);
		
		WebElement addquestion = driver.findElement(By.xpath("//button[@class='btn btn-basic btn-s ']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", addquestion);
		addquestion.click();
		Thread.sleep(500);
		js.executeScript("arguments[0].scrollIntoView();", addquestion);
		addquestion.click();
		Thread.sleep(500);
		js.executeScript("arguments[0].scrollIntoView();", addquestion);
		addquestion.click();
		
		List<WebElement> surveyeditbox = driver.findElements(By.xpath("//div[@class='survey-editBox']"));
		
		//설문1 추가
		surveyeditbox.get(0).findElement(By.id("question1")).sendKeys(CommonValues.SURVEY_QUESTION[0]);
		Thread.sleep(500);
		//설문2 추가
		surveyeditbox.get(1).findElement(By.id("question1")).sendKeys(CommonValues.SURVEY_QUESTION[1]);
		Thread.sleep(500);
		//설문3 추가
		surveyeditbox.get(2).findElement(By.id("question1")).sendKeys(CommonValues.SURVEY_QUESTION[2]);
		Thread.sleep(500);
		
		//설문2 형식 교체 -중복  필수 객관식 
		surveyeditbox.get(1).findElement(By.id("question1")).click();
		driver.findElement(By.xpath("//div[@class='survey-editBox focused']/div/div[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[2]")).click();
		Thread.sleep(500);
		List<WebElement> addoption = driver.findElements(By.xpath("//div[@class='temp-add']"));
		addoption.get(1).click();
		driver.findElement(By.xpath("//span[@class='slider round']")).click();
		
		js.executeScript("arguments[0].scrollIntoView();", surveyeditbox.get(2));
	
		//설문3 형식 교체 - 주관식
		surveyeditbox.get(2).findElement(By.id("question1")).click();
		driver.findElement(By.xpath("//div[@class='survey-editBox focused']/div/div[1]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='box-option open']/div[3]")).click();
	}
	
	public String findSeminarIDInList(WebDriver driver, String title) throws InterruptedException {
		
		if(!driver.getCurrentUrl().contentEquals(SERVER_URL + LIST_URI)){
			driver.get(SERVER_URL + LIST_URI);
			Thread.sleep(1000);
		}
		
		String listitem = "//*/text()[normalize-space(.)='" + title + "']/parent::*";
		
		driver.findElement(By.xpath(listitem)).click();
		Thread.sleep(500);
		
		if(driver.getCurrentUrl().contains(SERVER_URL + DETAIL_VIEW)) {
			return driver.getCurrentUrl().replace(SERVER_URL + DETAIL_VIEW, "");
		} else {
			return "";
		}

	}
	
	public void postSeminar(WebDriver driver, String seminarID) throws InterruptedException {
		String seminarView = SERVER_URL + DETAIL_VIEW + seminarID;
		
		driver.get(seminarView);
		Thread.sleep(1000);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SEMINARVIEW_POST)));
		driver.findElement(By.xpath(XPATH_SEMINARVIEW_POST)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER + "/button[1]")).click();
		Thread.sleep(500);
	}
	
	public String checkSettingpopup(WebDriver wd) throws InterruptedException {
		// 설정팝업 확인
		Thread.sleep(5000);
		if(isElementPresent(wd, By.xpath(XPATH_ROOM_SETTING_TITLE))) {
			wd.findElement(By.xpath(XPATH_ROOM_SETTING_CONFIRM_BTN)).click();
			Thread.sleep(500);
			return "";
		} else {
			return "cannot find seminar setting popup.";
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
	
	public String getDayofWeek(int day) {
		String dayofweek = Integer.toString(day);
		
		switch(day) {
		case 1:
			dayofweek = "Sunday";
			break;	
		case 2:
			dayofweek = "Monday";
			break;
		case 3:
			dayofweek = "Tuesday";
			break;	
		case 4:
			dayofweek = "Wednesday";
			break;
		case 5:
			dayofweek = "Thursday";
			break;
		case 6:
			dayofweek = "Friday";
			break;
		case 7:
			dayofweek = "Saturday";
			break;	

		}
		return dayofweek;
	}
	
	public String getMonth(int date) {
		String month = "";
		
		switch(date) {
		case 0:
			month = "January";
			break;
		case 1:
			month = "Feburary";
			break;
		case 2:
			month = "March";
			break;	
		case 3:
			month = "April";
			break;
		case 4:
			month = "May";
			break;
		case 5:
			month = "June";
			break;
		case 6:
			month = "July";
			break;	
		case 7:
			month = "August";
			break;	
		case 8:
			month = "September";
			break;
		case 9:
			month = "October";
			break;
		case 10:
			month = "November";
			break;
		case 11:
			month = "December";
			break;		
		}
		
		return month;
	}
	
	String getDay(int day) {
		String dayofmonth;

		if(day == 1) {
			dayofmonth = day + "st";
		} else if(day == 2) {
			dayofmonth = day + "nd";
		} else if(day == 3) {
			dayofmonth = day + "rd";
		} else if(day == 21) {
			dayofmonth = day + "st";
		} else if(day == 22) {
			dayofmonth = day + "nd";
		} else if(day == 23) {
			dayofmonth = day + "rd";
		} else if(day == 31) {
			dayofmonth = day + "st";
		} else {
			dayofmonth = day + "th";
		}
		
		return dayofmonth;
	}
	
	public void selectAll(WebElement e) {

		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			e.click();
			JavascriptExecutor executor = (JavascriptExecutor) getDriver(e);
			executor.executeScript("arguments[0].select();", e);
		} else {
			e.sendKeys(Keys.CONTROL,"a");
		}

	}

	public static WebDriver getDriver(SearchContext context) {
	    if (context instanceof WebDriver) {
	        return (WebDriver) context;
	    } else if (context instanceof WrapsDriver) {
	        return ((WrapsDriver) context).getWrappedDriver();
	    } else if (context instanceof WrapsElement) {
	        return ((WrapsDriver) ((WrapsElement) context).getWrappedElement()).getWrappedDriver();
	    }
	    throw new IllegalArgumentException("Driver could not be extracted from the specified context");
	}
	
	public boolean checkShotURL(WebDriver wd, String shotURL, String originURL) throws InterruptedException {
		boolean pass = false;
		
		wd.get(shotURL);
		Thread.sleep(500);
		
		if(wd.getCurrentUrl().contentEquals(originURL)) {
			pass = false;
		}
		return pass;
	}
	
	public void checkBanner(WebDriver wd) {
		try {
			wd.findElement(By.xpath("//div[@class='Banner_banner__demo__2zAHq']//i[@class='ricon-close']")).click();
		} catch(Exception e) {
			//do not anything
		}
	}
}
