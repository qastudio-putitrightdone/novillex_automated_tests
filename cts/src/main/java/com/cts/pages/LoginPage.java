package com.cts.pages;

import com.google.gson.JsonParser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.athena.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends CtsBasePage {

    private Page page;

    private Locator userIdInput;
    private Locator passwordInput;
    private Locator loginButton;
    private Locator useridBoxMsg;
    private Locator eyeButton;
    private Locator forgetpasswordLink;
    private Locator Errormsg;

    private ThreadLocal<String> accessToken = new ThreadLocal<>();

    public LoginPage(Page page) {
        super(page);
        this.page = page;
        this.userIdInput = page.locator("input[name='userId']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.useridBoxMsg = page.locator(".text-sm.font-lato.text-red-500");
        this.eyeButton =page.locator(".input-suffix-end");
        this.forgetpasswordLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                .setName("Forgot Password?"));
        this.Errormsg=page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions()
                        .setName("Cheque Truncation System")
                        .setLevel(4));
    }

    @Step("Entering user ID: {userId}")
    public LoginPage enterUserId(String userId) {
        userIdInput.clear();
        userIdInput.fill(userId);
        page.keyboard().press("Tab");
        return this;
    }

    @Step("Verify message: {message}")
    public void verifyUserVerifiedMessage(String message) {
        assertThat(
                page.getByText(message,
                        new Page.GetByTextOptions().setExact(true))
        ).isVisible();
        attachScreenshot(page,"Verify message: {message}");
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
