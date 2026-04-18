package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.athena.BasePage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CtsDashboardPage extends CtsBasePage {

    private Page page;

    private Locator logoutButton;

    private Locator headerSection;
    private Locator notifications ;
    private Locator sideMenu;
    private Locator sideMenuItems;
    private Locator dashboardmsg;


    public CtsDashboardPage(Page page) {
        super(page);
        this.page = page;
        this.headerSection = page.locator(".text-center.justify-start.text-neutral-700.text-xs.font-medium.font-lato");
        this.notifications = page.locator(".dropdown.text-gray-400");
        this.logoutButton = page.locator(".header svg").nth(2);
//        this.sideMenu=page.locator(".menu.menu-transparent.px-2.pb-2");
//        this.sideMenuItems=page.locator("nav.menu > div.menu-collapse > div.menu-collapse-item");
//        Locator sideNav = page.locator("div.app-layout-modern").locator("div.side-nav");
        this.dashboardmsg=page.locator(".notification-content");
        page.locator(".menu-collapse-item.menu-collapse-item-transparent.mb-2").first().click();
    }

    @Step("Verify CTS Dashboard is displayed")
    public CtsDashboardPage verifyDashboardPageNavigation() {
        page.waitForURL("**/dashboard");
        attachScreenshot(page, "CTS Dashboard");

        return this;
    }

    @Step("Verify header contains: {text}")
    public CtsDashboardPage verifyHeaderField(String text) {
        Locator filtered = headerSection
                .filter(new Locator.FilterOptions().setHasText(text));
        assertThat(filtered).isVisible();
        return this;
    }

    @Step("Verify header contains Date")
    public CtsDashboardPage verifyDateInHeader() {
        Locator date = headerSection.last();
        String uiDate = date.innerText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM, yyyy", Locale.ENGLISH);
        String systemDate = LocalDate.now().format(formatter);
        return this;
    }

    @Step("Verify notification button is visible")
    public CtsDashboardPage verifynotificationbutton() {
        assertThat(notifications).isVisible();
        return this;
    }

    @Step("Verify logout button is visible")
    public CtsDashboardPage verifylogoutbutton() {
        assertThat(logoutButton).isVisible();
        return this;
    }

    @Step("Verify side menu is visible")
    public CtsDashboardPage verifySideMenu() {
        assertThat(sideMenu).isVisible();
        return this;
    }

    @Step("Verify side menu contains expected menus")
    public void verifyExactSideMenus(String... expectedMenus) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        int actualCount = sideMenuItems.count();
        if (actualCount != expectedMenus.length) {
            throw new AssertionError(
                    "Menu count mismatch → Expected: " + expectedMenus.length +
                            " but got: " + actualCount
            );
        }
        for (String menu : expectedMenus) {
            Locator item = sideMenuItems
                    .filter(new Locator.FilterOptions().setHasText(menu));
            assertThat(item).isVisible();
        }
    }

    @Step("Verify {text} submenus")//need change
    public void verifyInwardSubMenus(String... expectedSubMenus) {

        for (String subMenu : expectedSubMenus) {
            Locator item = page.locator(".menu-item.menu-item-light.menu-item-hoverable")
                    .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^" + subMenu + "$")));
            assertThat(item).isVisible();
        }
    }

    @Step("Logging out from CTS")
    public LoginPage logoutFromCts() {
        logoutButton.click();
        page.waitForURL("**/sign-in");
        attachScreenshot(page, "Logout from CTS");

        return new LoginPage(page);
    }
}
