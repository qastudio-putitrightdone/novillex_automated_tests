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

import static org.testng.Assert.assertTrue;

public class LoginTests extends BaseTests {

    private ThreadLocal<String> loginAccess = new ThreadLocal<>();
    public static final String VERIFIED = "Successfully verified user.";
    public static final String ALREADY_LOGGED_IN = "User Already Logged in.";
    public static final String INVALID_USERID="Invalid User.";
    public static final String FORGET_PASS="User id is required";


    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User already logged in verification")
    @Description("To verify that if user is already logged in, it should display the message as user already logged")
    @Test
    public void loginuseridloggedinmsgs() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId("1001").
                verifyUserVerifiedMessage(ALREADY_LOGGED_IN);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User verification")
    @Description("To verify when user enters the correct user id and press tab then “Successfully Verified User“ message should display on the screen")
//    @Test
    public void loginuseridsuceessmsgs() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId("1003").
                verifyUserVerifiedMessage(VERIFIED);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("Toggle password visibility.")
    @Description("To verify that when user clicks on the eye button after entering the password , password should be displayed in readable format.")
    @Test
    void testPasswordVisibilityToggle() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.clickOnceEyeButton();
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

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To verify the if user is able to logout from CTS.")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void testForgetPassword(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        loginPage.clickOnForgotPassword();
        loginPage.clickOnNextButton();
        loginPage.verifyUserVerifiedMessage(FORGET_PASS);
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To check that when user enters the wrong user ID and click on next button, it will throw an error called User Not Found.")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void testForgetUserID(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
//        loginAccess.set(loginPage
//                .loginToCTS(userId, password));
        loginPage.clickOnForgotPassword();
        loginPage.enterUserId("865");
        loginPage.clickOnNextButton();
    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To verify that Back to login button is navigating to the main login page which contain user ID and password.")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void testBackToLoginNavigation(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
//        loginAccess.set(loginPage
//                .loginToCTS(userId, password));
        loginPage.clickOnForgotPassword();
        loginPage.clickOnBackButton();

    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To check that if UserID field is mandatory field, if user skips the field and proceed further, it will throw an error called, Please enter your user id")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void testErrorMessageOnUserID(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        loginPage.userIDErrorMessage();

    }

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("To check that if Password field is mandatory field, if user skips the field and proceed further, it will throw an error called, Please enter your password.")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void testErrorMessageOnPassword(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.enterUserId(userId).clickLogin();
        loginPage.passwordErrorMessage();

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        CtsApiClient ctsApiClient = new CtsApiClient(playwright, loginAccess.get());
        ctsApiClient.clearCtsContextAndLogout();
    }
}
