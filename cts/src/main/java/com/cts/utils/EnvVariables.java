package com.cts.utils;

import org.athena.ReadConfigData;

import java.util.Properties;

public class EnvVariables {

    private static Properties getConfigMap() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
        ReadConfigData readConfigData = new ReadConfigData();
        return readConfigData.fetchConfigData(filePath);
    }

    public static String getBrowser() {
        return getConfigMap().getProperty("browser");
    }

    public static String getUrl() {
        return getConfigMap().getProperty("url");
    }

    public static String getHost() {
        return getConfigMap().getProperty("host");
    }

    public static String getRefer() {
        return getConfigMap().getProperty("refer");
    }
}
