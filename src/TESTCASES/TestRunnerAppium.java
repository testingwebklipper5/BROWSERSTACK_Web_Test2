package TESTCASES;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.framework.device.AppClient;
import com.framework.device.AppClientFactory;
import com.framework.device.TestContext;
import com.framework.events.IClient;
import com.framework.executor.Executor;
import com.framework.executor.TestCaseDTO;
import com.framework.pages.DummyPage;
import com.framework.reports.ReportAppium;
import com.framework.reports.ReportExcel;
import com.framework.utils.AppiumStartStopLinux;
import com.framework.utils.AppiumStartStopWindow;
import com.framework.utils.CommonUtilities;
import com.framework.utils.EmailUtilities;

public class TestRunnerAppium {

	private final static String appName = "browserstack";
	static final String CLIENT_PROPERTY = "client_property";
	static final String TEST_SUITE = "test_suite";
	static final String XPATH_PROPERTY = "xpath_property";
	static final String TOOL = "tool";

	private static OptionBuilder addOption(String opt, String desc, String argName) {
		return OptionBuilder.withLongOpt(opt).withDescription(desc).hasArg().withArgName(argName);
	}

	public static void main(String[] args) {

		String testCaseFileName = "/src/TESTSUITE/WEBTESTSUITE.properties";
		String xpathFileName = "/src/config/WebAppXPATH.properties";

		String clientFileName = "/src/config/ClientAndroidPropertyWebApp.properties";

		String tool = "appium";

		CommandLineParser cli = new DefaultParser();
		Options options = new Options();

		options.addOption(
				addOption(CLIENT_PROPERTY.toLowerCase(), "ClientAndroidPropertyFile", CLIENT_PROPERTY).create());
		options.addOption(addOption(TEST_SUITE.toLowerCase(), "TestSuiteFile", TEST_SUITE).create());
		options.addOption(addOption(XPATH_PROPERTY.toLowerCase(), "XpathPropertyFile", XPATH_PROPERTY).create());
		options.addOption(addOption(TOOL.toLowerCase(), "Tool Name", TOOL).create());

		try {
			CommandLine line = cli.parse(options, args);
			if (line.hasOption(CLIENT_PROPERTY.toLowerCase())) {
				clientFileName = line.getOptionValue(CLIENT_PROPERTY.toLowerCase());
			}
			if (line.hasOption(TEST_SUITE.toLowerCase())) {
				testCaseFileName = line.getOptionValue(TEST_SUITE.toLowerCase());
			}
			if (line.hasOption(XPATH_PROPERTY.toLowerCase())) {
				xpathFileName = line.getOptionValue(XPATH_PROPERTY.toLowerCase());
			}
			if (line.hasOption(TOOL.toLowerCase())) {
				tool = line.getOptionValue(TOOL.toLowerCase());
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		TestContext testContext = new TestContext(xpathFileName, clientFileName);
		System.out.println(testContext.getClientProperties().getProperty(IClient.DEVICE_ENVIRONMENT));

		System.out.println(testContext.getClientProperties().getProperty(IClient.DEVICE_ENVIRONMENT));
		if (testContext.getClientProperties().getProperty(IClient.DEVICE_ENVIRONMENT).equals("TESTOBJECT") == false) {
			if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("API")) {
				if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("WEB")) {
					if ((testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("LINUX"))
							|| (testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE)
									.equals("MAC")) == false) {
						AppiumStartStopWindow.startAppiumServer(testContext);
					} else {
						AppiumStartStopLinux.startAppiumServer(testContext);
					}
				}
			}
		}
		try {
			AppClient appClient = AppClientFactory.getAppClient(testContext, "appium");
			testContext.setAppClient(appClient);
			// testContext.getAppClient().turnGPSOn();
			// Properties props = LoadProperty.loadProperties(testCaseFileName);
			List<TestCaseDTO> testCaseList = CommonUtilities.getTestCaseList(testCaseFileName);
			if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("API")) {
				try {
					Thread.sleep(5000);// neccesary
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (testContext.getClientProperties().getProperty(IClient.UNINSTALL_OLD_APP).equals("YES")
						|| testContext.getClientProperties().getProperty(IClient.UNINSTALL_OLD_APP).equals("Yes")) {
					try {
						testContext.getAppClient().applicationClose(
								testContext.getClientProperties().getProperty(IClient.APP_PACKAGE_VALUE));

						testContext.getAppClient()
								.run("adb -s " + testContext.getClientProperties().getProperty(IClient.UDID_VALUE)
										+ " uninstall "
										+ testContext.getClientProperties().getProperty(IClient.APP_PACKAGE_VALUE));
						System.out.println("uninstall");
						testContext.getAppClient()
								.run("adb -s " + testContext.getClientProperties().getProperty(IClient.UDID_VALUE)
										+ " install "
										+ testContext.getClientProperties().getProperty(IClient.APP_SETUP_PATH));

						testContext.getAppClient().launch(
								testContext.getClientProperties().getProperty(IClient.APP_ACTIVITY_VALUE), false,
								false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			String folderName = ReportAppium.setReport(true,
					testContext.getClientProperties().getProperty(IClient.REPORT_FOLDER_PATH), "pdf");
			if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("API")
					|| !testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("WEB")) {
				// wait is needed for jars
				try {
					Thread.sleep(30000);// wait needed for jars is 25 seconds
				} catch (Exception e) {
				}
			}
			Map<String, Object> executorMap = Executor.execute(testContext, testCaseList);

			try {
				String pathName = testContext.getClientProperties().getProperty(IClient.REPORT_FOLDER_PATH);
				ReportExcel.generateReport(executorMap, appName, pathName);
				String path[] = ReportExcel.generateReport(executorMap, appName, pathName);
				///// CommonUtilities.copyCompleteReports(folderName,testContext.getClientProperties().getProperty(IClient.REPORT_FOLDER_PATH));
				if (testContext.getClientProperties().getProperty(IClient.EMAIL_SEND).equals("Yes")
						|| testContext.getClientProperties().getProperty(IClient.EMAIL_SEND).equals("YES")) {
					EmailUtilities.sendEmail(executorMap, path[0], path[1], folderName);
				}

			} catch (IOException e) {
				System.out.println("Exception while generating report : " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Exception while Sending Email : " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (testContext.getClientProperties().getProperty(IClient.DEVICE_ENVIRONMENT)
					.equals("TESTOBJECT") == false) {
				if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("API")) {
					if (!testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("WEB")) {
						if ((testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("LINUX"))
								|| (testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE)
										.equals("MAC")) == false) {
							AppiumStartStopWindow.stopAppiumServer(testContext);
						} else {
							AppiumStartStopLinux.stopAppiumServer(testContext);
						}
					}
				}
			} else {
				testContext.getAppClient().TestObjectKillAppium();
			}
		}

		if (testContext.getClientProperties().getProperty(IClient.PROJECT_TYPE).equals("WEB")) {
			testContext.getAppClient().killwebdriver();
		}

		System.exit(1);
	}
}
