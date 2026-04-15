package com.ctsDashBoard;

import com.base.BaseTests;
import com.cts.api.CtsApiClient;
import com.cts.pages.CtsDashboardPage;
import com.cts.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class DashboardTests extends BaseTests {
    private ThreadLocal<String> loginAccess = new ThreadLocal<>();
    public static final String VERIFIED = "Successfully verified user.";

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User verification")
    @Description("To verify when user enters the correct user id and press tab then “Successfully Verified User“ message should display on the screen")
    public void loginUseridSucessMsgs() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId("1003").
                verifyUserVerifiedMessage(VERIFIED);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("Login to CTS – Cheque Truncation system.")
    @Test(dataProviderClass = DashboardData.class, dataProvider = "userData")
    public void loginToCts(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage
                .verifyDashboardPageNavigation();
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To verify the if user is able to logout from CTS.")
    @Test(dataProviderClass = DashboardData.class, dataProvider = "userData")
    public void logoutFromCts(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage
                .verifyDashboardPageNavigation()
                .logoutFromCts();
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("Header information verification")
    @Description("To verify that all the required information like Branch code , bank code, day and date, notifications and logout button.")
    @Test(dataProviderClass = DashboardData.class, dataProvider = "userData")
    public void testHeaderInfo(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage
                .verifyDashboardPageNavigation()
                .verifyHeaderField("Branch Code")
                .verifyHeaderField("Bank Code")
                .verifyDateInHeader()
                .verifynotificationbutton()
                .verifylogoutbutton();
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("Side menu bar verification")
    @Description("To verify that when user login, all the required side menu bar should be visible on the left side.")
    @Test(dataProviderClass = DashboardData.class, dataProvider = "userData")
    public void testSideMenuVisibility(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage.verifyExactSideMenus(
                "Inward",
                "Outward",
                "Access Management",
                "Reports",
                "Master Management"
        );
    }

    @Epic("Login")
    @Feature("Inward")
    @Story("Inwards sub menus display verification")
    @Description("To verify that when user clicks on the Inward, the submenus should display like File Upload, Mark pending, Mark Pending Auth, Financial Validation, Mark Refer, Extension Request, Auth and RRF File.")
    @Test(dataProviderClass = DashboardData.class, dataProvider = "userData")
    public void testInwardSubMenus(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage.verifyInwardSubMenus(
                "File Upload",
                "Mark Pending",
                "Mark Pending Auth",
                "Financial Validation",
                "Mark Refer",
                "Extension Request",
                "Auth and RRF Download"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        CtsApiClient ctsApiClient = new CtsApiClient(playwright, loginAccess.get());
        ctsApiClient.clearCtsContextAndLogout();
    }
}
