package com.cts.pages;

import com.google.gson.JsonParser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

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
    private Locator nextButton;
    private Locator backButton;
    private Locator notification;

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
        this.nextButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        this.backButton =page.locator(".text-center").nth(2);
        this.notification =page.locator("div.toast-wrapper");
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
        assertThat(useridBoxMsg.getByText(message)).isVisible();
        attachScreenshot(page,"Verify message: {message}");
    }


    @Step("Entering password")
    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.fill(password);
    }

    @Step("click on the eye button and check if the password is visible or not")
    public LoginPage clickOnceEyeButton() {
        passwordInput.fill("MySecret123");
        assert passwordInput.getAttribute("type").equals("password");
        attachScreenshot(page,"Before click on eye button");
        eyeButton.click();
        assert passwordInput.getAttribute("type").equals("text");
        attachScreenshot(page,"After click on eye button");
        return this;
    }

    @Step("Clicking login button")
//    public void clickLogin() {
//        Page newPage = page.waitForPopup(() -> {loginButton.click();
//        });
//    }
    public Page clickLogin() {
        return page.waitForPopup(() -> {
            loginButton.click();
        });
    }

    @Step("Clicks on the Forgot Password")
    public void clickOnForgotPassword() {
        forgetpasswordLink.click();
    }

    @Step("Clicks on Next button")
    public void clickOnNextButton(){
        nextButton.click();
        notification.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        attachScreenshot(page,"Error called User Not Found.");
    }

    @Step("Clicks on Back button")
    public void clickOnBackButton(){
        backButton.click();
        page.waitForURL("**/sign-in");
        attachScreenshot(page,"URL should contain /login");
    }

    @Step("Check Error message for UseID")
    public Locator userIDErrorMessage() {
        attachScreenshot(page,"Error message for UserId");
        return page.getByText("Please enter your user id",
                new Page.GetByTextOptions().setExact(true));
    }

    @Step("Check Error message for Password")
    public Locator passwordErrorMessage() {
        attachScreenshot(page,"Error message for Password");
        return page.getByText("Please enter your password",
                new Page.GetByTextOptions().setExact(true));
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
