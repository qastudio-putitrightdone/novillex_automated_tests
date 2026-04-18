package com.cts.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.athena.ReadConfigData;

import java.util.HashMap;
import java.util.Properties;

import static com.cts.api.HeaderUtils.getHeaderMap;
import static com.cts.utils.EnvVariables.getUrl;

public class CtsApiClient {

    private Playwright playwright;
    private String accessToken;

    public CtsApiClient(Playwright playwright, String accessToken) {
        this.playwright = playwright;
        this.accessToken = accessToken;
    }

    private APIRequestContext createBaseCtsRequest() {

        return playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(getUrl())
                .setExtraHTTPHeaders(getHeaderMap(accessToken))
                .setIgnoreHTTPSErrors(true));
    }

    private void clearResources() {
        createBaseCtsRequest().post("/cts_module/auth/clear-resource");
    }

    private void logoutFromCts() {
        createBaseCtsRequest().post("/cts_module/auth/logout");
    }

    public void clearCtsContextAndLogout() {
        clearResources();
        logoutFromCts();
    }
}
