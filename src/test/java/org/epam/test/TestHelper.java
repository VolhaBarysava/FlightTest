package org.epam.test;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class TestHelper {
	WebDriver driver;
	AppGenric objAppGeneric;
	
	@BeforeClass
	@Parameters({"url"})
	public void setUp(String url)
	{
		objAppGeneric = new AppGenric();
		System.setProperty("webdriver.chrome.driver", "Resources\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(url);
		driver.manage().window().maximize();
	}
	
	@AfterClass
	public void tearUp()
	{
		driver.quit();
	}

}
