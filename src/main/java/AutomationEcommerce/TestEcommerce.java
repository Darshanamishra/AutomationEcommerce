package AutomationEcommerce;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class TestEcommerce {
	WebDriver d;
	String ExpectedResult, ActualResult;
	int End;
	SoftAssert SoftAssertion = new SoftAssert();

	@BeforeTest
	public void InitialiseBrowser() {
		WebDriverManager.chromedriver().setup();
		d = new ChromeDriver();
		d.manage().window().maximize();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void Login() throws Exception {

		JSONParser jp = new JSONParser();
		FileReader fr = new FileReader(".\\JsonFile\\TestData.json");
		Object ob = jp.parse(fr);
		JSONObject UserLogin = (JSONObject) ob;

		ExpectedResult = "Robin Smith";
		d.get("https://automationexercise.com/");
		d.findElement(By.xpath("//a[contains(.,'Signup / Login')]")).click();
		d.findElement(By.xpath("//div[@class='login-form']//input[@name='email']"))
				.sendKeys(UserLogin.get("username").toString());
		d.findElement(By.name("password")).sendKeys(UserLogin.get("password").toString());
		d.findElement(By.xpath("//button[.='Login']")).click();
		ActualResult = d.findElement(By.xpath("//a[contains(.,'Logged in as Robin Smith')]")).getText();
		// Validating valid login A/c
		Assert.assertTrue(ActualResult.contains(ExpectedResult));
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		d.findElement(By.xpath("//a[contains(.,'Logout')]")).click();
	}


	@SuppressWarnings("deprecation")
	@Test
	public void Registration() {
		JavascriptExecutor js = (JavascriptExecutor) d;
		ActualResult = "Automation Exercise - Signup / Login";
		d.get("https://automationexercise.com/login");
		ExpectedResult = d.getTitle();
		// Validating SignUp page has loaded
		Assert.assertEquals(ExpectedResult, ActualResult);
		d.manage().window().maximize();
		d.findElement(By.name("name")).sendKeys("Robin Smith");
		d.findElement(By.xpath("//div[@class='signup-form']//input[@name='email']"))
				.sendKeys("robinsmith2023@gmail.com");
		d.findElement(By.xpath("//button[.='Signup']")).click();
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// Validating A/c doesn't exist already
		if (d.findElement(By.xpath("//p[.='Email Address already exist!']")).isDisplayed()) {
			End = 2;
		} else {
			End = 1;
		}
		switch (End) {
		case 1:
			d.findElement(By.xpath("//input[@value='Mrs']")).click();
			js.executeScript("window.scroll(0,150)");
			d.findElement(By.id("password")).sendKeys("automation123");
			js.executeScript("window.scroll(0,150)");
			d.findElement(By.id("days")).sendKeys("15");
			d.findElement(By.id("months")).sendKeys("January");
			d.findElement(By.id("years")).sendKeys("2000");
			js.executeScript("window.scroll(0,150)");
			d.findElement(By.cssSelector("[for='newsletter']")).click();
			d.findElement(By.cssSelector("[for='optin']")).click();
			d.findElement(By.id("first_name")).sendKeys("Robin");
			d.findElement(By.id("last_name")).sendKeys("Smith");
			js.executeScript("window.scroll(0,150)");
			d.findElement(By.xpath("//p[4]/input[@class='form-control']")).sendKeys(" 1234 rd james st");
			d.findElement(By.id("country")).sendKeys("United States");
			d.findElement(By.id("state")).sendKeys("Newyork");
			d.findElement(By.id("city")).sendKeys("Newyork");
			js.executeScript("window.scroll(0,150)");
			d.findElement(By.id("zipcode")).sendKeys("a1b2c3");
			d.findElement(By.id("mobile_number")).sendKeys("1234567890");
			d.findElement(By.xpath("//button[.='Create Account']")).click();
			js.executeScript("window.scroll(0,150)");
			ActualResult = d.getTitle();
			ExpectedResult = "Automation Exercise - Account Created";
			// Validating A/c Successfully Created
			Assert.assertEquals(ExpectedResult, ActualResult);
			d.findElement(By.cssSelector(".btn-primary")).click();
			d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			d.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[5]/a")).click();
			// Validating A/c Successfully Deleted
			Assert.assertEquals(d.getCurrentUrl(), "https://automationexercise.com/delete_account");
			d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			d.findElement(By.xpath("//a[.='Continue']")).click();
		case 2:
			System.out.println("Account already exist");
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void ProductSearch() throws Exception {

		JavascriptExecutor js = (JavascriptExecutor) d;
		d.get("https://automationexercise.com/products");
		d.findElement(By.id("search_product")).sendKeys("Tshirt");
		ExpectedResult = "Tshirt";
		d.findElement(By.cssSelector(".fa-search")).click();
		js.executeScript("window.scroll(0,600)");
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		d.findElement(By.xpath("//a[@href='/product_details/2']")).click();
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		ActualResult = d.findElement(By.xpath("//h2[.='Men Tshirt']")).getText();
		// Result Accuracy check
		SoftAssertion.assertTrue(ActualResult.contains(ExpectedResult));
		System.out.println(ActualResult);
		ExpectedResult = "Tshirt";
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@AfterTest
	public void Teardown() {
		d.quit();
		;
	}

}
