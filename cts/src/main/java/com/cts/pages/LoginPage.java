package com.cts.pages;

import com.google.gson.JsonParser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.athena.BasePage;

public class LoginPage extends CtsBasePage {

    private Page page;

    private Locator userIdInput;
    private Locator passwordInput;
    private Locator loginButton;
    private ThreadLocal<String> accessToken = new ThreadLocal<>();

    public LoginPage(Page page) {
        super(page);
        this.page = page;
        this.userIdInput = page.locator("input[name='userId']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
    }

    @Step("Entering user ID: {userId}")
    private void enterUserId(String userId) {
        userIdInput.clear();
        userIdInput.fill(userId);
    }

    @Step("Entering password")
    private void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.fill(password);
    }

    @Step("Clicking login button")
    private void clickLogin() {
        loginButton.click();
    }

    @Step("Logging in to CTS with user ID: {userId}")
    public String loginToCTS(String userId, String password) {
        enterUserId(userId);
        enterPassword(password);
        page.waitForResponse(response -> {
            if (response.url().contains("auth/login") && response.status() == 200) {
                accessToken.set(JsonParser.parseString(response.text()).getAsJsonObject().get("details").getAsJsonObject().get("accessToken").getAsString());
                return true;
            }
            return false;
        }, loginButton::click);

        return accessToken.get();
    }
}
