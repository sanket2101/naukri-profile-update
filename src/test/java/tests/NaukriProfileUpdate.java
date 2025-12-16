package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import utils.DriverManager;
import utils.TestListener;

import java.time.Duration;

@Listeners(TestListener.class)
public class NaukriProfileUpdate extends BaseTest {

    @Test
    public void updateProfile() {

        ChromeOptions options = new ChromeOptions();

        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);
        DriverManager.setDriver(driver);   // ⭐ VERY IMPORTANT

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.get("https://www.naukri.com/nlogin/login");

            String user = System.getenv("NAUKRI_USER");
            String pass = System.getenv("NAUKRI_PASS");

            // Fallback for local run
            if (user == null || pass == null) {
                throw new RuntimeException(
                        "NAUKRI_USER / NAUKRI_PASS not set in environment");
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(user);

            driver.findElement(By.id("passwordField"))
                    .sendKeys(pass);

            driver.findElement(By.xpath("//button[text()='Login']")).click();

            wait.until(ExpectedConditions.urlContains("homepage"));

            WebElement completeProfileBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[self::button or self::a][contains(normalize-space(),'Complete profile')]")
                    )
            );
            js.executeScript("arguments[0].click();", completeProfileBtn);


            WebElement editIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//em[@class='icon edit ']"))
            );
            js.executeScript("arguments[0].click();", editIcon);

            WebElement saveBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("saveBasicDetailsBtn"))
            );
            js.executeScript("arguments[0].click();", saveBtn);

            System.out.println("Naukri profile edit & save completed successfully");

        } catch (Exception e) {

            // 🔴 VERY IMPORTANT: rethrow exception
            e.printStackTrace();
            throw e;   // <-- this ensures TestNG marks test as FAILED
        }
    }

}
