package com.cts.utils;

import com.microsoft.playwright.Page;

public class JsUtils {

    public static String fetchLoginAccessKey(Page playwrightPage) {
        return playwrightPage.evaluate("() => localStorage.getItem('key)").toString();
    }
}
