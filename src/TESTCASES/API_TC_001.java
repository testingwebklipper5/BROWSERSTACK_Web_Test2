package TESTCASES;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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

public class API_TC_001 extends TestCase {
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
		ArrayList browser_version_in_chrome_in_windows = new ArrayList<>();
		ArrayList browser_version_in_chrome_in_mac = new ArrayList<>();
		ArrayList browser_version_in_firefox_in_windows = new ArrayList<>();
		ArrayList browser_version_in_firefox_in_mac = new ArrayList<>();

		DummyPage dummyPage = new DummyPage(testContext);
		JAVAUTIL javautil = new JAVAUTIL();
		String api_response = automation.return_content_out_file();
		// System.out.println(api_response);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode idNode = null;
		String browser_name, browser_version, os;
		// read JSON like DOM Parser
		JsonNode rootNode;
		try {
			rootNode = objectMapper.readTree(api_response);
			// System.out.println(rootNode.toString());
			for (JsonNode nodea : rootNode) {
				browser_name = (nodea.path("browser").toString());
				// System.out.println(browser_name);
				browser_version = (nodea.path("browser_version").toString());
				os = (nodea.path("os").toString());
				// System.out.println(browser_version + "\n" + os);
				if (browser_name.contains("Firefox") && os.contains("OS X")) {
					browser_version_in_firefox_in_mac.add(browser_version);
				} else if (browser_name.contains("Firefox") && os.contains("Windows")) {
					browser_version_in_firefox_in_windows.add(browser_version);
				} else if (browser_name.contains("Chrome") && os.contains("OS X")) {
					browser_version_in_chrome_in_mac.add(browser_version);
				} else if (browser_name.contains("Chrome") && os.contains("Windows")) {
					browser_version_in_chrome_in_windows.add(browser_version);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//System.out.println(browser_version_in_chrome_in_windows);
//System.out.println(browser_version_in_chrome_in_mac);
//System.out.println(browser_version_in_firefox_in_mac);
//System.out.println(browser_version_in_firefox_in_windows);

		Collections.sort(browser_version_in_chrome_in_mac);
		Collections.sort(browser_version_in_chrome_in_windows);
		Collections.sort(browser_version_in_firefox_in_mac);
		Collections.sort(browser_version_in_firefox_in_windows);

		System.out.println("1. latest chrome version in Windows = " + browser_version_in_chrome_in_windows.get(browser_version_in_chrome_in_windows.size()-1));
		System.out.println("2. latest firefox version in Windows = " + browser_version_in_firefox_in_windows.get(browser_version_in_firefox_in_windows.size()-1));
		System.out.println("3. latest chrome version in OS X = " + browser_version_in_chrome_in_mac.get(browser_version_in_chrome_in_mac.size()-1));
		System.out.println("4. latest firefox version in OS X = " + browser_version_in_firefox_in_mac.get(browser_version_in_firefox_in_windows.size()-1));

		result.setStatus(true);
		result.setExceptionList(exceptionList);

		return result;

	}

	public void release() {

		DummyPage dummyPage = new DummyPage(testContext);
		JAVAUTIL javautil = new JAVAUTIL();

	}

}
