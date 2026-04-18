package com.cts.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.athena.ReadConfigData;

import java.util.HashMap;
import java.util.Properties;

public class CtsApiClient {

    private Playwright playwright;
    private String accessToken;

    public CtsApiClient(Playwright playwright, String accessToken) {
        this.playwright = playwright;
        this.accessToken = accessToken;
    }

    private Properties getConfigMap() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
        ReadConfigData readConfigData = new ReadConfigData();
        return readConfigData.fetchConfigData(filePath);
    }

    private HashMap<String, String> getHeaderMap() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/json, text/plain, */*");
        headerMap.put("host", getConfigMap().getProperty("host"));
        headerMap.put("origin", getConfigMap().getProperty("url"));
        headerMap.put("referer", getConfigMap().getProperty("refer"));
        headerMap.put("access-token", accessToken);
        return headerMap;
    }

    private APIRequestContext createBaseCtsRequest() {
        String baseUrl = getConfigMap().getProperty("url");

        return playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl)
                .setExtraHTTPHeaders(getHeaderMap())
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
