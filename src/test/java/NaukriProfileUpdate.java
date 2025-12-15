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
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://www.naukri.com/nlogin/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameField")))
                    .sendKeys(System.getenv("NAUKRI_USER"));

            driver.findElement(By.id("passwordField"))
                    .sendKeys(System.getenv("NAUKRI_PASS"));

            driver.findElement(By.xpath("//button[text()='Login']")).click();

            wait.until(ExpectedConditions.urlContains("homepage"));

            driver.get("https://www.naukri.com/mnjuser/profile");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Update resume')]")
            )).click();

            System.out.println("Naukri profile updated successfully");

        } finally {
            driver.quit();
        }
    }
}
