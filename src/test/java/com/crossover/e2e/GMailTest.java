package com.crossover.e2e;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GMailTest {
	private WebDriver driver;
	private Properties properties = new Properties();

	@BeforeTest
	public void setUp() throws Exception {

		properties.load(new FileReader(new File(
				"C:\\Users\\komal\\Downloads\\qa-automation-java\\qa-automation-selenium-java\\src\\test\\resources\\test.properties")));
		// Dont Change below line. Set this value in test.properties file incase you
		// need to change it..
		System.setProperty("webdriver.chrome.driver", properties.getProperty("webdriver.chrome.driver"));
		driver = new ChromeDriver();
	}

	@AfterTest
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void testSendEmail() throws Exception {
		driver.get(
				"https://accounts.google.com/signin/oauth/oauthchooseaccount?client_id=717762328687-iludtf96g1hinl76e4lc1b9a82g457nn.apps.googleusercontent.com&scope=profile%20email&redirect_uri=https%3A%2F%2Fstackauth.com%2Fauth%2Foauth2%2Fgoogle&state=%7B%22sid%22%3A1%2C%22st%22%3A%2259%3A3%3ABBC%2C16%3Ad6d43be47e814b98%2C10%3A1591608975%2C16%3Afbe7e8befa57ecde%2Cff07eae6cb92814ccc53330ba3ad167d91b66afc6cf797a2b05e4e949aa68bcd%22%2C%22cdl%22%3Anull%2C%22cid%22%3A%22717762328687-iludtf96g1hinl76e4lc1b9a82g457nn.apps.googleusercontent.com%22%2C%22k%22%3A%22Google%22%2C%22ses%22%3A%2296fe9c384acc45fc91bc60a78b8cda25%22%7D&response_type=code&o2v=1&as=sqXj6IoZ_vrDuD8djofpog&flowName=GeneralOAuthFlow");
		WebElement userElement = driver.findElement(By.xpath("//input[@id ='identifierId']"));
		userElement.sendKeys(properties.getProperty("username"));
		driver.findElement(By.xpath("//span[contains(text(),'Next')]")).click();
		Thread.sleep(5000);
		WebElement passwordElement = driver.findElement(By.name("password"));

		passwordElement.sendKeys(properties.getProperty("password"));
		driver.findElement(By.xpath("//span[contains(text(),'Next')]")).click();
		Thread.sleep(5000);
		driver.get("https://gmail.com");
		driver.manage().window().maximize();
		Thread.sleep(5000);
		WebElement composeElement = driver.findElement(By.xpath("//div[contains(text(),'Compose')]"));
		composeElement.click();
		driver.findElement(By.xpath("//textarea[@name='to']")).clear();
		driver.findElement(By.xpath("//textarea[@name='to']"))
				.sendKeys(properties.getProperty("username").toString() + "@gmail.com");
		WebElement subject = driver.findElement(By.xpath("//input[@name='subjectbox']"));
		subject.sendKeys(properties.getProperty("email.subject"));
		WebElement emailbody = driver.findElement(By.xpath("//div[@aria-label='Message Body']"));
		emailbody.sendKeys(properties.getProperty("email.body"));

		driver.findElement(By.xpath("//tbody//tr//td[5]//div//div//div")).click(); // 3 dots button
		driver.findElement(By.xpath("//div[@role='menu'][4]//div/following::div/following::div[1]")).click();
		driver.findElement(By.xpath("//div[@title='Social']//div//div")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Apply')]")).click();
		driver.findElement(By.xpath("//tr[@class='btC']//following::div[contains(text(),'Send')]")).click();
		Thread.sleep(6000);
		driver.findElement(By.xpath("//div[contains(text(),'Social')]")).click();
		List<WebElement> socialEmails = driver.findElements(By.xpath("//tr[@role='row'][1]"));
		for (WebElement email : socialEmails) {
			if (email.isDisplayed() && email.getText().contains(properties.getProperty("email.subject"))) {
				email.click();
				System.out.println("email clicked");
				String subjectOfEmail = driver.findElement(By.xpath("//h2[@class='hP']")).getText();
				System.out.println(subjectOfEmail);
				Assert.assertEquals(subjectOfEmail, properties.getProperty("email.subject"));
				String emailContent = driver.findElement(By.xpath("//div[@class='ii gt']//div//div")).getText();
				System.out.println(emailContent);
				Assert.assertEquals(emailContent, properties.getProperty("email.body"));

			}
		}
	}
}
