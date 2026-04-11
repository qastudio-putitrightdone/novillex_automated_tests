package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.athena.BasePage;

import static com.cts.constants.UserConstants.ADMIN_ROLE;
import static com.cts.constants.UserConstants.VALID_PASSWORD;
import static com.cts.utils.TestData.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UsersPage extends CtsBasePage {

    private Page page;

    private Locator addUserButton;
    private Locator roleIdDropDown;
    private Locator userIdInput;
    private Locator nameInput;
    private Locator usernameInput;
    private Locator passwordInput;
    private Locator mailIdInput;
    private Locator mobileNumberInput;
    private Locator addButton;
    private Locator userList;
    private Locator cancelButton;

    public UsersPage(Page page) {
        super(page);
        this.page = page;
        this.addUserButton = page.locator("header button");
        this.roleIdDropDown = page.locator(".select").first();
        this.userIdInput = page.locator("[name='userId']");
        this.nameInput = page.locator("[name='name']");
        this.usernameInput = page.locator("[name='userName']");
        this.passwordInput = page.locator("[name='password']");
        this.mailIdInput = page.locator("[name='mailId']");
        this.mobileNumberInput = page.locator("[name='contactNo']");
        this.addButton = page.locator("button[type='submit']");
        this.userList = page.locator("tbody td");
        this.cancelButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel"));
    }

    @Step("Click on Add User button in header")
    public UsersPage clickAddUserButtonInHeader() {
        addUserButton.click();

        return this;
    }

    @Step("Selecting role ID")
    private void selectRoleId() {
        roleIdDropDown.click();
        page.waitForCondition(() -> roleIdDropDown.locator(".select__menu").isVisible());
        roleIdDropDown.locator(".select__menu").getByText(ADMIN_ROLE).click();
    }

    @Step("Entering user ID: {userId}")
    private void enterUserId(String userId) {
        userIdInput.clear();
        userIdInput.fill(userId);
    }

    @Step("Entering name: {name}")
    private void enterName(String name) {
        nameInput.clear();
        nameInput.fill(name);
    }

    @Step("Entering username: {username}")
    private void enterUsername(String username) {
        usernameInput.clear();
        usernameInput.fill(username);
    }

    @Step("Entering password")
    private void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.fill(password);
    }

    @Step("Entering mail ID: {mailId}")
    private void enterMailId(String mailId) {
        mailIdInput.clear();
        mailIdInput.fill(mailId);
    }

    @Step("Entering mobile number: {mobileNumber}")
    private void enterMobileNumber(String mobileNumber) {
        mobileNumberInput.clear();
        mobileNumberInput.fill(mobileNumber);
    }

    @Step("Click on Add button to add new user")
    public void clickAddUserButton() {
        addButton.click();
    }

    @Step("Creating a random admin user")
    public String createRandomAdminUser() {
        clickAddUserButtonInHeader();
        selectRoleId();
        String userId = generateRandomValue(5);
        enterUserId(userId);
        enterName(generateRandomValue(8));
        enterUsername(generateRandomValue(8));
        enterPassword(VALID_PASSWORD);
        enterMailId(generateRandomEmail());
        enterMobileNumber(IndiaMobileNumberGenerator());
        clickAddUserButton();

        return userId;
    }

    @Step("Verifying that user with identifier: {userIdentifier} is added to the list")
    public void verifyUserAdded(String userIdentifier) {
        assertThat(userList.filter(new Locator.FilterOptions().setHasText(userIdentifier))).isVisible();
        attachScreenshot(page, "Role Added: " + userIdentifier);
    }

    @Step("Verifying that user with identifier: {userIdentifier} is added to the list")
    public void verifyUserNotAdded(String userIdentifier) {
        assertThat(userList.filter(new Locator.FilterOptions().setHasText(userIdentifier))).not().isVisible();
        attachScreenshot(page, "Role Added: " + userIdentifier);
    }

    @Step("Click on Cancel button to cancel adding new user")
    public void clickCancelButton() {
        cancelButton.click();
    }

    @Step("Canceling a random admin user creation")
    public String cancelUserCreation() {
        clickAddUserButtonInHeader();
        selectRoleId();
        String userId = generateRandomValue(5);
        enterUserId(userId);
        enterName(generateRandomValue(8));
        enterUsername(generateRandomValue(8));
        enterPassword(VALID_PASSWORD);
        enterMailId(generateRandomEmail());
        enterMobileNumber(IndiaMobileNumberGenerator());
        clickCancelButton();

        return userId;
    }

    @Step("Verifying that mandatory field error message is displayed when trying to add a user without filling required fields")
    public void verifyMandatoryFieldErrorMessage(String message) {
        assertThat(page.getByText(message).first()).isVisible();
        attachScreenshot(page, "Mandatory Field Error Message Displayed");
    }
}
