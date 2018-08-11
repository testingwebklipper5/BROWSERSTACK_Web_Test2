package TESTCASES;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.framework.events.IClient;
import com.framework.executor.TestCase;
import com.framework.executor.TestResult;
import com.framework.pages.DummyPage;
import com.framework.reports.ReportAppium;
import com.google.gson.JsonObject;

import com.sun.xml.internal.fastinfoset.sax.Properties;

import events.IXPNEvents;
import util.JAVAUTIL;

public class WEB_TC_002 extends TestCase {
	DummyPage dummyPage = new DummyPage(testContext);
	ArrayList exceptionList = new ArrayList<>();
	String title = dummyPage.returnddmmyyyy() + " " + dummyPage.webReturnTime();

	public void setUp() {
		testId = "WEB_TC_002";
		testDesc = "To verify user is able to upload the app and the tool starts giving logs as soon as the app is launched.";
		DummyPage dummyPage = new DummyPage(testContext);

	}

	@Override
	public TestResult test() {
		TestResult result = new TestResult(testId, testDesc);

		DummyPage dummyPage = new DummyPage(testContext);
		JAVAUTIL javautil = new JAVAUTIL();
		dummyPage.webhiturl1("https://browserstack.com");// navigating to browserstack.com
		dummyPage.webclick1("//a[contains(@href,\"sign_in\")]"); // clicking on sign in button
		// loggin in to the platform
		dummyPage.webclick1("//*[@id=\"user_email_login\"]");
		dummyPage.webelementsendtext1("//*[@id=\"user_email_login\"]", "kush+test12@browserstack.com");

		dummyPage.webclick1("//*[@id=\"user_password\"]");
		dummyPage.webelementsendtext1("//*[@id=\"user_password\"]", "test1234");
		dummyPage.webclick1("//*[@id=\"user_submit\"]");// signing in with valid username and password

		dummyPage.defaultWait(5000);
		close();
		dialog_close();
		// navigating to app live
		dummyPage.webclick1("//*[@id=\"product-menu-toggle\"]");
		dummyPage.webclick1(
				"//div[contains(text(),\"Interactive native & hybrid app testing\")]/parent::a/div[contains(text(),\"App Live\")]");

		dummyPage.defaultWait();
		close();
		dialog_close();
		dummyPage.webclick1("//*[@id=\"originaluploadapk\"]");
		// uploading apk through robot class
		dummyPage.web_upload_file(testContext, "MAC", "",
				testContext.xPathProperties.getProperty(IXPNEvents.PATH_FOR_APK_TO_B_UPLOADED));// uploading app from
																								// the local machine to
																								// the browser.
		dummyPage.defaultWait(30000);// waiting for app to get uploaded
		if (dummyPage.webisElementFound1(
				"//*[@id=\"app-list-wrapper\"]/div[contains(@data-package,\"com.example.android.basicnetworking\")]") == false) {
			dummyPage.web_upload_file(testContext, "MAC", "",
					testContext.xPathProperties.getProperty(IXPNEvents.PATH_FOR_APK_TO_B_UPLOADED));// uploading app
																									// from the local
																									// machine to the
																									// browser.
			dummyPage.defaultWait(30000);

		}
		dummyPage.webdefaultWaitTillElementAppear1(
				"//*[@id=\"app-list-wrapper\"]/div[contains(@data-package,\"com.example.android.basicnetworking\")]", // waiting
																														// for
																														// the
																														// apk
																														// to
																														// be
																														// availble
																														// in
																														// the
																														// list
				10000);
		if (dummyPage.webisElementFound1("//*[@id=\"rf-browsers\"]/div/div/a[contains(text(),\"Got it\")]")) {
			dummyPage.webclick1("//*[@id=\"rf-browsers\"]/div/div/a[contains(text(),\"Got it\")]");

		}
		// selecting device and performing the desired operation
		dummyPage.webclick1("//a[contains(text(),\"Nexus 6P\")]");// selecting device nexus 6p from the available
																	// devices
		if (dummyPage.webiselementclickable1("//*[@id=\"getScreenSize\"]/div/div/a[contains(text(),\"Set Size\")]")) {
			dummyPage.webclick1("//*[@id=\"getScreenSize\"]/div/div/a[contains(text(),\"Set Size\")]");
		}

		// now device is getting initialized.
		dummyPage.webdefaultWaitTillElementAppear1(
				"//*[@id=\"logs-text\"]//span[contains(text(),\"Starting device Nexus 6P\")]", 50000);
		dummyPage.webdefaultWaitTillElementAppear1(
				"//*[@id=\"logs-text\"]//span[contains(text(),\"Downloading app on Nexus 6P\")]", 50000);
		dummyPage.webdefaultWaitTillElementAppear1(
				"//*[@id=\"logs-text\"]//span[contains(text(),\"Installing app on Nexus 6P\")]", 50000);

		dummyPage.webverifyElementFound1(
				"//*[@id=\"logs-text\"]//span[contains(text(),\"Installing app on Nexus 6P\")]");

		dummyPage.webdefaultWaitTillElementAppear1("//*[@id=\"flashlight-overlay-native\"]", 150000);// as soon as the
																									// app opens

		// dummyPage.webdefaultWaitTillElementAppear1("//*[@id=\"logs-text\"]//span[contains(text(),\"Starting
		// app on Nexus 6P\")]", 50000);

		dummyPage.webverifyElementFound1("//*[@id=\"logs-text\"]//span[contains(text(),\"Starting app on Nexus 6P\")]");// verifying
																														// logs
																														// on
																														// as
																														// soon
																														// as
																														// the
																														// app
																														// launches.
		dummyPage.webverifyElementFound1(
				"//*[@id=\"logs-text\"]//span[contains(text(),\"com.example.android.basicnetworking\")]");

		if (exceptionList.size() == 0) {
			result.setStatus(true);
		}
		result.setExceptionList(exceptionList);

		return result;

	}

	public void release() {

		DummyPage dummyPage = new DummyPage(testContext);
		JAVAUTIL javautil = new JAVAUTIL();

	}

	public void close() {
		if (dummyPage.webisElementFound1("//a[contains(@class,\"close\")]")) {
			dummyPage.javascriptclick1("//a[contains(@class,\"close\")]");
		}
	}

	public void dialog_close() {
		if (dummyPage.webiselementclickable1("//*[@id=\"skip-local-installation\"]/*")) {
			dummyPage.webclick1("//*[@id=\"skip-local-installation\"]/*");
		}
	}
}
