package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.athena.BasePage;

import static com.cts.constants.UserConstants.ROLE_NAME_ERROR_MESSAGE;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RolePage extends CtsBasePage {

    private Page page;

    private Locator addRoleButton;
    private Locator roleNameInput;
    private Locator addButton;
    private Locator roleList;
    private Locator cancelButton;

    public RolePage(Page page) {
        super(page);
        this.page = page;
        this.addRoleButton = page.locator("header button");
        this.roleNameInput = page.locator("input[name='roleName']");
        this.addButton = page.locator("button[type='submit']");
        this.roleList = page.locator("tbody td");
        this.cancelButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel"));
    }

    @Step("Click on Add Role button in header")
    public void clickAddRoleButtonInHeader() {
        addRoleButton.click();
    }

    @Step("Entering role name: {roleName}")
    private void enterRoleName(String roleName) {
        roleNameInput.clear();
        roleNameInput.fill(roleName);
    }

    @Step("Click on Add button to add new role")
    public void clickAddRoleButton() {
        addButton.click();
    }

    @Step("Adding new role with name: {roleName}")
    public RolePage addNewRole(String roleName) {
        clickAddRoleButtonInHeader();
        enterRoleName(roleName);
        clickAddRoleButton();
        page.waitForCondition(() -> !addButton.isVisible());

        return this;
    }

    @Step("Cancel new role with name: {roleName}")
    public RolePage cancelNewRole(String roleName) {
        clickAddRoleButtonInHeader();
        enterRoleName(roleName);
        clickCancelButton();
        page.waitForCondition(() -> !addButton.isVisible());

        return this;
    }

    @Step("Verifying that role with name: {roleName} is added to the list")
    public RolePage verifyRoleAdded(String roleName) {
        assertThat(roleList.filter(new Locator.FilterOptions().setHasText(roleName))).isVisible();
        attachScreenshot(page, "Role Added: " + roleName);

        return this;
    }

    @Step("Verifying that role with name: {roleName} is added to the list")
    public RolePage verifyRoleNotAdded(String roleName) {
        assertThat(roleList.filter(new Locator.FilterOptions().setHasText(roleName))).not().isVisible();
        attachScreenshot(page, "Role Added: " + roleName);

        return this;
    }


    @Step("Verifying that mandatory field error message is displayed when trying to add a role without a name")
    public void verifyMandatoryFieldErrorMessage() {
        assertThat(page.getByText(ROLE_NAME_ERROR_MESSAGE)).isVisible();
        attachScreenshot(page, "Mandatory Field Error Message Displayed");
    }

    @Step("Click on Cancel button to cancel adding new role")
    public void clickCancelButton() {
        cancelButton.click();
    }
}
