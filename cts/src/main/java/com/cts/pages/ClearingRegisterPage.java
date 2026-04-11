package com.cts.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.athena.BasePage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static com.cts.utils.TestData.isReallyBlank;

public class ClearingRegisterPage extends CtsBasePage {

    private Page page;

    private Locator dateInput;
    private Locator submitButton;
    private Locator reportTable;
    private Locator exportButton;
    private Locator formatDropdown;
    private Locator okButton;
    private Locator pickerButton;
    private Locator pickerFrontButton;
    private Locator monthPicker;
    private Locator dayPicker;

    public ClearingRegisterPage(Page page) {
        super(page);
        this.page = page;
        this.dateInput = page.locator("[placeholder='Select Date']");
        this.submitButton = page.locator("button[type='submit']");
        this.reportTable = page.frameLocator("iframe").locator("table[id='__bookmark_1'] tr");
        this.exportButton = page.frameLocator("iframe").locator("[name='exportReport']");
        this.formatDropdown = page.frameLocator("iframe").locator("#exportFormat");
        this.okButton = page.frameLocator("//iframe[contains(@src, '/birt/frameset')]").locator("[value='OK']").nth(3);
        this.pickerButton = page.locator(".picker-header button").nth(2);
        this.pickerFrontButton = page.locator(".picker-header button").nth(3);
        this.monthPicker = page.locator(".picker-header button").nth(0);
        this.dayPicker = page.locator(".picker-table td");
    }

    @Step("Setting date input to first day of month with offset: {monthBack} months")
    public ClearingRegisterPage setFromDate() {
        dateInput.nth(0).click();
        do {
            pickerButton.click(new Locator.ClickOptions().setDelay(2000));
        } while (!monthPicker.textContent().equals("Sep"));
        dayPicker.filter(new Locator.FilterOptions().setHasText("1")).first().click();

        return this;
    }

    public ClearingRegisterPage setToDate() {
        dateInput.nth(1).click();
        page.locator(".border-indigo-600").click();
        attachScreenshot(page, "Set to date to");

        return this;
    }

    public ClearingRegisterPage clickSubmitButton() {
        submitButton.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        return this;
    }

    public ArrayList<String> getFirstRowData() {
        ArrayList<String> firstRowData = new ArrayList<>();
        List<Locator> allColumns = reportTable.nth(2).locator("td").all();
        for (Locator eachColumn : allColumns) {
            if (!isReallyBlank(eachColumn.textContent().trim())) {
                firstRowData.add(eachColumn.textContent().trim());
            }
        }

        return firstRowData;
    }

    public ClearingRegisterPage clickExportButton() {
        exportButton.click();

        return this;
    }

    public ClearingRegisterPage selectExportFormat(String format) {
        formatDropdown.selectOption(format);
        page.waitForCondition(() -> okButton.isEnabled(), new Page.WaitForConditionOptions().setTimeout(80000));
        page.waitForTimeout(5000);

        return this;
    }

    public void clickOkToInitiateExport() {
        Download download = page.waitForDownload(new Page.WaitForDownloadOptions().setTimeout(60000), () ->
                okButton.click(new Locator.ClickOptions().setForce(true)));
        download.saveAs(Paths.get("src/test/resources/downloads/" + download.suggestedFilename()));
    }

}
