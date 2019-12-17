//package fr.uha.ensisa.gl.cmwfb.mantest_app.it;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//
//import org.junit.*;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//
//import fr.uha.ensisa.gl.cmwfb.mantest.dao.DaoFactory;
//import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestDao;
//import fr.uha.ensisa.gl.cmwfb.mantest.dao.TestSerieDao;
//import fr.uha.ensisa.gl.cmwfb.mantest_app.controller.TestSerieController;
//
//public class HelloIT {
//	public static WebDriver driver;
//	public static String port;
//
//	@BeforeClass
//	public static void setupWebDriver() {
//		try {
//			String mvnit = System.getProperty("phantomjs.binary.path");
//			if (mvnit != null && mvnit.length() > 0) {
//				driver = Class.forName("org.openqa.selenium.phantomjs.PhantomJSDriver").asSubclass(WebDriver.class)
//						.newInstance();
//			}
//		} catch (Exception x) {
//		}
//		if (driver == null) { // Looking for marionette in PATH …
//			String ext = System.getProperty("os.name", "").toLowerCase().startsWith("win") ? ".exe" : "";
//			String geckodrivername = "geckodriver" + ext;
//			Collection<String> pathes = new ArrayList<>();
//			for (String source : new String[] { System.getProperty("PATH") /* posix */,
//					System.getenv().get("Path") /* win < 10 */, System.getenv().get("PATH") /* win >= 10 */ }) {
//				if (source != null) {
//					pathes.addAll(Arrays.asList(source.trim().split(File.pathSeparator)));
//				}
//			}
//			File geckoDriver = null;
//			for (String path : pathes) {
//				File f = new File(path, geckodrivername);
//				if (f.exists() && f.canExecute()) {
//					System.setProperty("webdriver.gecko.driver", f.getAbsolutePath());
//					geckoDriver = f;
//					break;
//				}
//			}
//			/*if (geckoDriver == null) {
//				throw new IllegalStateException("Cannot find geckodriver on the PATH");
//			}*/
//			//driver = new FirefoxDriver();
//		}
//		port = System.getProperty("servlet.port", "3030");
//	}
//
//	@AfterClass
//	public static void shutdownWebDriver() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}
//
//	@Test
//	public void testName() {
//		String testName = "testname";
//		driver.get("http://localhost:" + port + "/hello?name=" + testName);
//		assertTrue("Sent name not found in page", driver.getPageSource().contains(testName));
//	}
//	
//
//	@Test
//	public void testConnection() throws Exception {
//		URL url = new URL("http://localhost:" + port + "/test?id=1");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.connect();
//        assertEquals(200, connection.getResponseCode());
//        url = new URL("http://localhost:" + port + "/test?id=1000");
//        connection = (HttpURLConnection) url.openConnection();
//        connection.connect();
//        assertEquals(200, connection.getResponseCode());
//	}
//	
//	@Test
//	public void testNotFound() {
//		driver.get("http://localhost:" + port + "/test?id=1000");
//		assertTrue(driver.getPageSource().contains("not found"));
//	}
//
//}