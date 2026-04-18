package com.cts.pages;

import com.microsoft.playwright.Page;

public class LoginResults {

    private final Page page;
    private final String accessToken;

    public LoginResults(Page page, String accessToken) {
        this.page = page;
        this.accessToken = accessToken;
    }

    public Page getPage() {
        return page;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
