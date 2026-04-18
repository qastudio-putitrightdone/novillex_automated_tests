package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static com.cts.utils.JsUtils.fetchLoginAccessKey;

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
    public LoginResults loginToCTS(String userId, String password) {
        enterUserId(userId);
        enterPassword(password);
        Page page1 = page.waitForPopup(() -> clickLogin());
        accessToken.set(fetchLoginAccessKey(page1));

        return new LoginResults(page1, accessToken.get());
    }
}
