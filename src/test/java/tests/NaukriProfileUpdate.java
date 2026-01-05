package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.DriverManager;
import utils.TestListener;

import java.time.Duration;

@Listeners(TestListener.class)
public class NaukriProfileUpdate extends BaseTest {

    @Test
    public void updateProfile() {

        ChromeOptions options = new ChromeOptions();

        // ✅ User Agent (important for CI)
        options.addArguments(
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Safari/537.36"
        );

        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-blink-features=AutomationControlled");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);
        DriverManager.setDriver(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // ================= LOGIN =================
            driver.get("https://www.naukri.com/nlogin/login");

            String user = System.getenv("NAUKRI_USER");
            String pass = System.getenv("NAUKRI_PASS");

            if (user == null || pass == null) {
                throw new RuntimeException("NAUKRI_USER / NAUKRI_PASS not set");
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(user);
            driver.findElement(By.id("passwordField")).sendKeys(pass);
            driver.findElement(By.xpath("//button[text()='Login']")).click();

            wait.until(ExpectedConditions.urlContains("homepage"));

            // ================= OPEN PROFILE =================
            WebElement completeProfileBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[self::button or self::a][contains(normalize-space(),'Complete profile')]")
                    )
            );
            js.executeScript("arguments[0].click();", completeProfileBtn);

            // ================= EDIT BASIC DETAILS =================
            WebElement editIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//em[contains(@class,'edit')]"))
            );
            js.executeScript("arguments[0].click();", editIcon);

            // Ensure edit popup is loaded
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@id='saveBasicDetailsBtn']")
            ));

            // ================= TRY HEADLINE UPDATE =================
            try {
                WebElement headline = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//input[contains(@placeholder,'headline') or contains(@name,'headline')]")
                        )
                );

                js.executeScript("arguments[0].scrollIntoView(true);", headline);
                Thread.sleep(1000);

                String oldText = headline.getAttribute("value");

                headline.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                headline.sendKeys(Keys.BACK_SPACE);

                if (oldText.endsWith(".")) {
                    headline.sendKeys(oldText.substring(0, oldText.length() - 1));
                } else {
                    headline.sendKeys(oldText + ".");
                }

                System.out.println("✔ Headline updated");

            } catch (TimeoutException te) {
                // Headline not editable for this account
                System.out.println("⚠ Headline not editable, proceeding to Save only");
            }

            // ================= SAVE =================
            WebElement saveBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("saveBasicDetailsBtn"))
            );
            js.executeScript("arguments[0].click();", saveBtn);

            System.out.println("✅ Naukri profile update completed");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw e; // ensure test fails if something breaks
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
