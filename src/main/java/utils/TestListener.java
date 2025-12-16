package utils;

import com.aventstack.extentreports.*;
import org.openqa.selenium.*;
import org.testng.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static List<File> screenshots = new ArrayList<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest =
                extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        capture("PASS", result.getName());
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        capture("FAIL", result.getName());
        test.get().fail(result.getThrowable());
    }

    private void capture(String status, String testName) {

        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            System.out.println("Driver is NULL, screenshot skipped");
            return;
        }

        try {
            File dir = new File("screenshots");
            if (!dir.exists()) dir.mkdirs();

            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            File dest = new File("screenshots/" + testName + "_" + status + ".png");

            java.nio.file.Files.copy(
                    src.toPath(),
                    dest.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            screenshots.add(dest);

            // ⭐ attach screenshot to Extent
            test.get().addScreenCaptureFromPath(dest.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();

        EmailUtil.sendEmailWithAttachments(
                screenshots,
                new File("test-output/ExtentReport.html")
        );
    }
}
