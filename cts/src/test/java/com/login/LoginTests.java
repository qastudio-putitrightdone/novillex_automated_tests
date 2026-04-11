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

    @Epic("Login")
    @Feature("CTS Login Screen")
    @Story("User Login verification")
    @Description("User Login verification")
    @Test(dataProviderClass = LoginData.class, dataProvider = "userData")
    public void loginToCts(String userId, String password) {
        LoginPage loginPage = new LoginPage(page);
        loginAccess.set(loginPage
                .loginToCTS(userId, password));
        CtsDashboardPage ctsDashboardPage = new CtsDashboardPage(page);
        ctsDashboardPage
                .verifyDashboardPageNavigation();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        CtsApiClient ctsApiClient = new CtsApiClient(playwright, loginAccess.get());
        ctsApiClient.clearCtsContextAndLogout();
    }
}
