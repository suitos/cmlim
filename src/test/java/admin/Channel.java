package admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Channel {
	
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
	
	@Test(priority = 0)
	public void EnterChannelList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_MENU_CHANNEL)).click();
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.ADMIN_URL + CommonValues.URL_CHANNELLIST)) {
			failMsg = "1. not channel info view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}

}
