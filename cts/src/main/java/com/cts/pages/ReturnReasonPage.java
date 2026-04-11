package com.cts.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.athena.BasePage;

import java.util.List;

public class ReturnReasonPage extends CtsBasePage {
    private Page page;

    private Locator reasonTableList;
    private Locator chargeAmountInput;
    private Locator updateButton;
    private Locator specificReasonRow;

    public ReturnReasonPage(Page page) {
        super(page);
        this.page = page;
        this.reasonTableList = page.locator("tbody tr");
        this.chargeAmountInput = page.locator("[name='chargeAmount']");
        this.updateButton = page.locator("[type='submit']");
    }

    @Step("Checking if return reason with name: {reasonName} is displayed")
    public boolean checkReturnReasonDisplayed(String reasonName) {
        boolean flag = false;
        page.waitForCondition(() -> reasonTableList.all().size() > 0);
        List<Locator> allReasonList = reasonTableList.all();
        for (Locator eachReason : allReasonList) {
            if (eachReason.locator("td").nth(2).locator(".absolute")
                    .textContent().trim().equals(reasonName)) {
                attachScreenshot(page, "Selected reason: " + reasonName);
                flag = true;
                break;
            }
        }

        return flag;
    }

    @Step("Selecting reason with name: {reasonName}")
    public Locator selectSpecificReason(String reasonName) {
        page.waitForCondition(() -> reasonTableList.all().size() > 0);
        List<Locator> allReasonList = reasonTableList.all();
        for (Locator eachReason : allReasonList) {
            if (eachReason.locator("td").nth(2).locator(".absolute")
                    .textContent().trim().equals(reasonName)) {
                eachReason.click();
                specificReasonRow = eachReason;
                attachScreenshot(page, "Selected reason: " + reasonName);
                break;
            }
        }

        return specificReasonRow;
    }

    @Step("Updating charge amount to: {chargeAmount}")
    public ReturnReasonPage updateChargeAmount(String chargeAmount) {
        chargeAmountInput.clear();
        chargeAmountInput.fill(chargeAmount);
        attachScreenshot(page, "Updated charge amount to: " + chargeAmount);

        return this;
    }

    @Step("Clicking on update button")
    public ReturnReasonPage clickUpdateButton() {
        updateButton.click();

        return this;
    }

    @Step("Verifying charge amount updated to: {expectedChargeAmount}")
    public void verifyChargeAmountUpdated(String expectedChargeAmount) {
        page.waitForCondition(() -> specificReasonRow.locator("td").nth(4)
                .textContent().equals(expectedChargeAmount));
        attachScreenshot(page, "Verified charge amount updated to: " + expectedChargeAmount);
    }
}
