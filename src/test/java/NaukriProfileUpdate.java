import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class NaukriProfileUpdate {

    @Test
    public void updateProfile() {

        ChromeOptions options = new ChromeOptions();

        // Headless ONLY in GitHub Actions
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            /* 1️⃣ Open Login Page */
            driver.get("https://www.naukri.com/nlogin/login");

            /* 2️⃣ Login */
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(System.getenv("NAUKRI_USER"));

            driver.findElement(By.id("passwordField"))
                    .sendKeys(System.getenv("NAUKRI_PASS"));

            driver.findElement(By.xpath("//button[text()='Login']")).click();

            /* 3️⃣ Wait for login success */
            wait.until(ExpectedConditions.urlContains("homepage"));

            /* Click Complete Profile button */
            WebElement completeProfileBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[self::button or self::a][contains(normalize-space(),'Complete profile')]")
                    )
            );

// Use JS click for React UI
            js.executeScript("arguments[0].click();", completeProfileBtn);


            /* 4️⃣ Click Edit Profile (pencil icon) */
            WebElement editProfileIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//em[@class='icon edit ']")
                    )
            );
            js.executeScript("arguments[0].click();", editProfileIcon);

            /* 5️⃣ Click Save button */
            WebElement saveButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//button[@id='saveBasicDetailsBtn']")
                    )
            );
            js.executeScript("arguments[0].click();", saveButton);

            /* 6️⃣ Small wait to allow backend update */
            Thread.sleep(2000);

            System.out.println("Naukri profile edit & save completed successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
