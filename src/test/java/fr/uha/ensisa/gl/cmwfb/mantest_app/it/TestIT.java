package fr.uha.ensisa.gl.cmwfb.mantest_app.it;

import static org.junit.Assert.*;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestIT {
	
	public static WebDriver driver;
	public static String port;
	public static String baseURL;
	
	@BeforeClass
	public static void setupWebDriver() {
		try {
			String mvnit = System.getProperty("phantomjs.binary.path");
			if (mvnit != null && mvnit.length() > 0) {
				driver = Class.forName("org.openqa.selenium.phantomjs.PhantomJSDriver").asSubclass(WebDriver.class)
						.newInstance();
			}
		} catch (Exception x) {
		}
		if (driver == null) { // Looking for marionette in PATH …
			String ext = System.getProperty("os.name", "").toLowerCase().startsWith("win") ? ".exe" : "";
			String geckodrivername = "geckodriver" + ext;
			Collection<String> pathes = new ArrayList<>();
			for (String source : new String[] { System.getProperty("PATH") /* posix */,
					System.getenv().get("Path") /* win < 10 */, System.getenv().get("PATH") /* win >= 10 */ }) {
				if (source != null) {
					pathes.addAll(Arrays.asList(source.trim().split(File.pathSeparator)));
				}
			}
			File geckoDriver = null;
			for (String path : pathes) {
				File f = new File(path, geckodrivername);
				if (f.exists() && f.canExecute()) {
					System.setProperty("webdriver.gecko.driver", f.getAbsolutePath());
					geckoDriver = f;
					break;
				}
			}
			if (geckoDriver == null) {
				throw new IllegalStateException("Cannot find geckodriver on the PATH");
			}
			driver = new FirefoxDriver();
		}
		port = System.getProperty("servlet.port", "8080");
		baseURL = "http://localhost:" + port + "/test";
		driver.get("http://localhost:" + port + "/create?testName=newTest");
	}

	@AfterClass
	public static void shutdownWebDriver() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testConnection() throws Exception {
		URL url = new URL(baseURL + "?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
        url = new URL(baseURL + "?id=1000");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
	}
	
	@Test
	public void testNotFound() {
		driver.get(baseURL + "?id=1000");
		assertTrue(driver.getPageSource().contains("not found"));
	}

}
