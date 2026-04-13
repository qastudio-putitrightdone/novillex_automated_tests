package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.athena.BasePage;

public class CtsDashboardPage extends CtsBasePage {

    private Page page;

    private Locator logoutButton;

    private Locator bankheader;
    private Locator notifications ;


    public CtsDashboardPage(Page page) {
        super(page);
        this.page = page;
        this.bankheader = page.locator(".text-center.justify-start.text-neutral-700.text-xs.font-medium.font-lato");
        this.notifications = page.locator(".dropdown.text-gray-400");
        this.logoutButton = page.locator(".header svg").nth(2);
    }

    @Step("Verify CTS Dashboard is displayed")
    public CtsDashboardPage verifyDashboardPageNavigation() {
        page.waitForURL("**/dashboard");
        attachScreenshot(page, "CTS Dashboard");

        return this;
    }

    @Step("Logging out from CTS")
    public LoginPage logoutFromCts() {
        logoutButton.click();
        page.waitForURL("**/sign-in");
        attachScreenshot(page, "Logout from CTS");

        return new LoginPage(page);
    }
}
