import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignInTest {
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
	public void shouldThrowAnErrorIfSignInDetailsAreMissing() {
		setUp();
		driver.get("https://www.cleartrip.com/");
		driver.findElement(By.linkText("Your trips")).click();
		driver.findElement(By.id("SignIn")).click();
		driver.switchTo().frame(driver.findElement(By.cssSelector("#modal_window")));
		waitFor(3000);
		driver.findElement(By.id("signInButton")).submit();
		String errors1 = driver.findElement(By.id("errors1")).getText();
		Assert.assertTrue(errors1.contains("There were errors in your submission"));
		driver.quit();
	}

	private void waitFor(int durationInMilliSeconds) {
		try {
			Thread.sleep(durationInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
	}

	@AfterMethod
	public void tearDown() {
		// close the browser
		driver.quit();
	}
}