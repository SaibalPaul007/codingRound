import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightBookingTest {

	public WebDriver driver;
	public Platform platform;
	public ChromeOptions chromeOptions;

	@BeforeMethod
	public void setUp() {
		chromeOptions = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 1);
		chromeOptions.setExperimentalOption("prefs", prefs);
		if (platform == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				platform = Platform.WIN10;
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
				driver = new ChromeDriver(chromeOptions);
				driver.manage().window().maximize();
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				platform = Platform.LINUX;
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver_linux");
				driver = new ChromeDriver(chromeOptions);
				driver.manage().window().maximize();
			} else if (operSys.contains("mac")) {
				platform = Platform.MAC;
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver");
				driver = new ChromeDriver(chromeOptions);
				driver.manage().window().maximize();
			}
		}
	}

	@Test
	public void testThatResultsAppearForAOneWayJourney() {
		setUp();
		driver.get("https://www.cleartrip.com/");
		waitFor(2000);
		driver.findElement(By.id("OneWay")).click();
		driver.findElement(By.id("FromTag")).clear();
		driver.findElement(By.id("FromTag")).sendKeys("Bangalore");

		// wait for the auto complete options to appear for the origin
		waitFor(5000);
		List<WebElement> originOptions = driver.findElements(By.xpath("//*[@id='ui-id-1']//li"));
		originOptions.get(0).click();

		driver.findElement(By.id("ToTag")).clear();
		driver.findElement(By.id("ToTag")).sendKeys("Delhi");

		// wait for the auto complete options to appear for the destination
		waitFor(5000);
		// select the first item from the destination auto complete list
		List<WebElement> destinationOptions = driver.findElements(By.xpath("//*[@id='ui-id-2']//li"));
		destinationOptions.get(0).click();

		driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/table/tbody/tr[3]/td[7]/a")).click();

		// all fields filled in. Now click on search
		driver.findElement(By.id("SearchBtn")).click();

		waitFor(5000);
		// verify that result appears for the provided journey search
		Assert.assertTrue(isElementPresent(By.className("searchSummary")));
	}

	@AfterMethod
	public void tearDown() {
		// close the browser
		driver.quit();
	}

	private void waitFor(int durationInMilliSeconds) {
		try {
			Thread.sleep(durationInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}