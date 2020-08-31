package seminartest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class TestListener implements ITestListener {
	WebDriver driver=null;

	String sharedPath_pre ="http://10.1.101.181:8080/job/RS_test/lastSuccessfulBuild/artifact/target/surefire-reports/fail_";
	String filePath_win = System.getProperty("user.dir") + "\\target\\surefire-reports\\fail_";
	String filePath_mac = System.getProperty("user.dir") + "/target/surefire-reports/fail_";
	String sharedPath ="";
	String filePath = "";
	String fileuri = "/target/surefire-reports";

    @Override
    public void onTestFailure(ITestResult result) {
    	System.out.println("***** Error "+result.getName()+" test has failed *****");
    	String methodName=result.getName().toString().trim();
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver)context.getAttribute("webDriver");
        
        
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");


		if(System.getProperty("os.name").toLowerCase().contains("win"))
			filePath = filePath_win + format2.format(cal.getTime()) + "\\";
		else 
			filePath = filePath_mac + format2.format(cal.getTime()) + "/";
		
		//sharedPath = sharedPath_pre + format2.format(cal.getTime()) + "/";
		sharedPath = filePath;
        File d = new File(filePath);
        if (!d.isDirectory()) d.mkdir();
        
        if(CommonValues.FOR_JENKINS) return;
        
        if (driver != null) takeScreenShot(methodName, driver);
    	
    	if ((WebDriver)context.getAttribute("webDriver2") != null) takeScreenShot(methodName + "WD2", (WebDriver)context.getAttribute("webDriver2"));
    	
    	if ((WebDriver)context.getAttribute("webDriver3") != null) takeScreenShot(methodName + "WD3", (WebDriver)context.getAttribute("webDriver3"));
    	if ((WebDriver)context.getAttribute("webDriver4") != null) takeScreenShot(methodName + "WD4", (WebDriver)context.getAttribute("webDriver4"));
    	if ((WebDriver)context.getAttribute("webDriver5") != null) takeScreenShot(methodName + "WD5", (WebDriver)context.getAttribute("webDriver5"));
    	if ((WebDriver)context.getAttribute("webDriver6") != null) takeScreenShot(methodName + "WD6", (WebDriver)context.getAttribute("webDriver6"));
    }
    
    public void takeScreenShot(String methodName, WebDriver driver) {
    	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with test method name 
    	
    	 String filename = String.format("%s-%s", methodName, Calendar.getInstance().getTimeInMillis());
    	 System.out.println("***methodName "+methodName+" ***");
            try {
            	File destFile = new File(filePath+filename+".png");
            	String jkpath = sharedPath + filename + ".png";
				FileUtils.copyFile(scrFile, destFile);
				System.out.println("***Placed screen shot in "+filePath+" ***");
				System.out.println("***Placed screen shot file name : "+destFile.getName()+" ***");
				//Reporter.log("<a href='"+ destFile.getName()+ "'> <img src='"+ destFile.getName() + "' height='100' width='100'/> </a>");
				Reporter.log("<a href='"+ jkpath + "'> <img src='"+ jkpath + "' height='100' width='100'/> </a>");
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
	public void onFinish(ITestContext context) {}
  
    public void onTestStart(ITestResult result) {   }
  
    public void onTestSuccess(ITestResult result) {   }

    public void onTestSkipped(ITestResult result) {   }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {   }

    public void onStart(ITestContext context) {   }
}  