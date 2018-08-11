package TESTCASES;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.framework.events.IClient;
import com.framework.executor.TestCase;
import com.framework.executor.TestResult;
import com.framework.pages.DummyPage;
import com.framework.reports.ReportAppium;
import com.google.gson.JsonObject;
import com.sun.xml.internal.fastinfoset.sax.Properties;

import events.IXPNEvents;
import util.JAVAUTIL;

public class WEB_TC_001 extends TestCase {
	DummyPage dummyPage = new DummyPage(testContext);
	ArrayList exceptionList = new ArrayList<>();
	String title = dummyPage.returnddmmyyyy() + " " + dummyPage.webReturnTime();

	public void setUp() {
		testId = "WEB_TC_001";
		testDesc = "";
		DummyPage dummyPage = new DummyPage(testContext);

	}

	@Override
	public TestResult test() {
		TestResult result = new TestResult(testId, testDesc);

		DummyPage dummyPage = new DummyPage(testContext);
		JAVAUTIL javautil = new JAVAUTIL();
		dummyPage.webclick1("//a[contains(@href,\"users/sign_in\")]");// clicking on signin button
		dummyPage.webdefaultWaitTillElementAppear(IXPNEvents.SIGN_IN_EMAIL_FIELD, 20000); // explicit wait for element
																							// to appear;
		dummyPage.webclick(IXPNEvents.SIGN_IN_EMAIL_FIELD);
		dummyPage.webelementsendtext(IXPNEvents.SIGN_IN_EMAIL_FIELD, "steb6015@gmail.com");

		dummyPage.webclick(IXPNEvents.SIGN_IN_PASSWORD_FIELD);
		dummyPage.webelementsendtext(IXPNEvents.SIGN_IN_PASSWORD_FIELD, "testtest");
		dummyPage.webclick1("//*[@id=\"user_submit\"]");
		dummyPage.defaultWait(5000);
		if (dummyPage.webisElementFound1("//a[contains(@class,\"close\")]")) {
			dummyPage.javascriptclick1("//a[contains(@class,\"close\")]");
		}
		dummyPage.webclick1("//*[@id=\"product-menu-toggle\"]");
		dummyPage.webclick1(
				"//div[contains(text(),\"Selenium browser testing\")]/parent::a[contains(@href,\"\")]/div[contains(text(),\"Automate\")]");
		dummyPage.webdefaultWaitTillElementAppear1("//*[@id=\"projectMenu_chosen\"]/a", 10000);
		dummyPage.webclick1("//*[@id=\"projectMenu_chosen\"]/a");
		dummyPage.defaultWait(500);
		dummyPage.webclick1("//*[@id=\"projectMenu_chosen\"]/div/ul/li[text()=\"All Projects\"]");
		if (dummyPage.webisElementFound1("//*[@id=\"done\" and contains(@class,\"bs-checkbox checked\")]")) {
		} else {
			dummyPage.webclick1("//*[@id=\"done\" and contains(@class,\"bs-checkbox\")]");
		}
		if (dummyPage.webisElementFound1("//*[@id=\"timeout\" and contains(@class,\"bs-checkbox checked\")]")) {
		} else {
			dummyPage.webclick1("//*[@id=\"timeout\" and contains(@class,\"bs-checkbox\")]");
		}
		if (dummyPage.webisElementFound1("//*[@id=\"failed\" and contains(@class,\"bs-checkbox checked\")]")) {
		} else {
			dummyPage.webclick1("//*[@id=\"failed\" and contains(@class,\"bs-checkbox\")]");
		}
		dummyPage.defaultWait(1000);
		dummyPage.webisElementFound1("//*[contains(@data-query-name,\"Project\")]");
		dummyPage.javascriptclick1("//*[contains(@data-query-name,\"Project\")]");
		
		//System.out.println(dummyPage.webgetAllValues("", "//*[contains(@data-query-name,\"Project\")]", "@id"));
		String project_name = dummyPage.webelementGetText2("//*[@class=\"full search-box results\"]//span[1]").replace("Build:", "");
		String verify_projectname = "//h3[contains(text(),\"" + project_name + "\")]";
		String session_id = dummyPage.webelementGetText2("//td[1]/a");
		String build_id = dummyPage.webelementGetText2("//span[contains(text(),\"Build ID\")]/following-sibling::span");

		dummyPage.webclick1("//*[@id=\"search-field\"]");
		dummyPage.webelementsendtext1("//*[@id=\"search-field\"]", project_name);
		testContext.getAppClient().webclickenterbutton("//*[@id=\"search-field\"]");
		dummyPage.defaultWait();
//		dummyPage.javascriptclick1("//*[contains(@class,\"svg-icon search-icon\")]");//clicking on search icon
		String project_name_on_search = dummyPage
				.webelementGetText2("//*[@class=\"full search-box results\"]//span[1]");
		if (project_name.equals(project_name_on_search) == false) {
			exceptionList.add("search using project name not working");
		}
		dummyPage.webclick1("//*[@id=\"search-field\"]");
		dummyPage.webelementsendtext1("//*[@id=\"search-field\"]", build_id);
		testContext.getAppClient().webclickenterbutton("//*[@id=\"search-field\"]");
		dummyPage.defaultWait();
		String build_id_on_search = dummyPage
				.webelementGetText2("//*[@class=\"full search-box results\"]//span[1]");
		if (build_id.equals(build_id_on_search) == false) {
			exceptionList.add("search using build id not working");
		}

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

}
