package tests;

import org.testng.annotations.AfterMethod;
import utils.DriverManager;

public class BaseTest {

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (DriverManager.getDriver() != null) {
            DriverManager.getDriver().quit();
            DriverManager.removeDriver();
        }
    }
}
