package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

public class CtsDashboardPage extends CtsBasePage {

    private Page page;

    private Locator logoutButton;
    private Locator collapsibleMenuItems;
    private Locator collapsibleMenuItemHeaders;
    private Locator menuItemList;

    public CtsDashboardPage(Page page) {
        super(page);
        this.page = page;
        this.logoutButton = page.locator(".header svg").nth(2);
        this.collapsibleMenuItems = page.locator(".menu-collapse");
        this.collapsibleMenuItemHeaders = page.locator(".menu-collapse-item .items-center");
        this.menuItemList = page.locator("ul .menu-item");
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

    public List<String> getCollapsibleMenuItems(String menuItemHeader) {
        for (Locator eachMenu: collapsibleMenuItems.all()) {
            if (eachMenu.locator(collapsibleMenuItemHeaders).textContent().equals(menuItemHeader)) {
                return eachMenu.locator(menuItemList).allTextContents();
            }
        }
        return null;
    }
}
