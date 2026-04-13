package com.login;

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

public class LoginTests extends BaseTests {

    private ThreadLocal<String> loginAccess = new ThreadLocal<>();
    public static final String VERIFIED = "Successfully verified user.";
    public static final String ALREADY_LOGGED_IN = "User Already Logged in.";
    public static final String INVALID_USERID="Invalid User.";

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User already logged in verification")
    @Description("To verify that if user is already logged in, it should display the message as user already logged")
    public void loginuseridloggedinmsgs() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId("1001").
                verifyUserVerifiedMessage(ALREADY_LOGGED_IN);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User verification")
    @Description("To verify when user enters the correct user id and press tab then “Successfully Verified User“ message should display on the screen")
    public void loginuseridsuceessmsgs() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId("1003").
                verifyUserVerifiedMessage(VERIFIED);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("Login to CTS – Cheque Truncation system.")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
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
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void logoutFromCts(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage
                .verifyDashboardPageNavigation()
                .logoutFromCts();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        CtsApiClient ctsApiClient = new CtsApiClient(playwright, loginAccess.get());
        ctsApiClient.clearCtsContextAndLogout();
    }
}
