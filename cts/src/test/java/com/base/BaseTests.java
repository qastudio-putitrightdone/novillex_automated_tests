package com.base;

import com.cts.utils.PagenameEnums;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.athena.LaunchBrowser;
import org.athena.ReadConfigData;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static com.cts.utils.NavigateUtils.*;
import static com.utils.constats.UrlPathsEnum.LOGIN;

public class BaseTests {

    LaunchBrowser launchBrowser = new LaunchBrowser();

    protected Page page;
    protected BrowserContext browserContext;
    protected Playwright playwright;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        List<Object> playwrightObjects = launchBrowser
                .initiateBrowserAndApplication(fetchBaseConfigData().getProperty("browser"),
                        fetchBaseConfigData().getProperty("url") + LOGIN.getUrlPath());
        page = (Page) playwrightObjects.get(2);
        browserContext = (BrowserContext) playwrightObjects.get(1);
        playwright = (Playwright) playwrightObjects.get(0);
        page.setDefaultTimeout(30000);
        PlaywrightAssertions.setDefaultAssertionTimeout(15000);
    }

    private void clearDownloads() {
        try {
            Path downloadsPath = Paths.get("src/test/resources/downloads");
            if (Files.exists(downloadsPath)) {
                Allure.step("Clearing downloaded files");
                Files.walk(downloadsPath)
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties fetchBaseConfigData() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
        ReadConfigData readConfigData = new ReadConfigData();
        return readConfigData.fetchConfigData(filePath);
    }

    public void navigateTo(String pageName) {
        if (pageName.equals(String.valueOf(PagenameEnums.user_role))) {
            page.navigate(user_role_page_url);
        } else if (pageName.equals(String.valueOf(PagenameEnums.users))) {
            page.navigate(users_page_url);
        } else if (pageName.equals(String.valueOf(PagenameEnums.return_reason))) {
            page.navigate(return_reason_page_url);
        } else if (pageName.equals(String.valueOf(PagenameEnums.inward_clering_register))) {
            page.navigate(inward_clearing_register_page_url);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closeContext(ITestResult iTestResult) {
         int testStatus = iTestResult.getStatus();
        if (testStatus == ITestResult.SUCCESS) {
            Allure.step("Test Passed !!!", Status.PASSED);
        } else if (testStatus == ITestResult.FAILURE) {
            Allure.step("Test Failed !!!", Status.FAILED);

            //Shubham---
            Throwable error = iTestResult.getThrowable();
                // 🔥 Differentiate failure types
            if (error instanceof AssertionError) {
                Allure.step("Assertion Failure", Status.FAILED);
            } else if (error instanceof com.microsoft.playwright.TimeoutError) {
                Allure.step("Timeout Issue", Status.BROKEN);
            } else {
                Allure.step("Other Exception", Status.BROKEN);
            }
            byte[] capturedScreen = page.screenshot();
            Allure.addAttachment("Failed Test" + System.currentTimeMillis(),
                    "image/png", new ByteArrayInputStream(capturedScreen), "png");
        } else if (testStatus == ITestResult.SKIP) {
            Allure.step("Test Skipped", Status.SKIPPED);
        }

        if (page != null) {
            page.close();
            clearDownloads();
        }
    }
}
