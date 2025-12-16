package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String name) {

        if (driver == null) {
            System.out.println("Driver is NULL, screenshot skipped");
            return null;
        }

        try {
            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            String dir = "test-output/screenshots";
            new File(dir).mkdirs();

            String path = dir + "/" + name + ".png";
            Files.copy(src.toPath(), new File(path).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            return path;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
