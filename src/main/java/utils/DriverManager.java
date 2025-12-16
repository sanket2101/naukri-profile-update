package utils;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void setDriver(WebDriver drv) {
        driver.set(drv);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void removeDriver() {
        driver.remove();
    }
}
