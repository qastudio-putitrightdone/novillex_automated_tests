package com.cts.api;

import java.util.HashMap;

import static com.cts.utils.EnvVariables.*;

public class HeaderUtils {

    public static HashMap<String, String> getHeaderMap(String accessToken) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/json, text/plain, */*");
        headerMap.put("host", getHost());
        headerMap.put("origin", getUrl());
        headerMap.put("referer", getRefer());
        headerMap.put("access-token", accessToken);
        return headerMap;
    }
}
