package com.novillex.utils;

public class GetEnv {

    public static String fetchEnv(String envParam, boolean isMandatory, String defaultValue) {
        try {
            if (isMandatory && defaultValue == null) {
                return System.getProperty(envParam);
            } else if (isMandatory && defaultValue != null) {
                return defaultValue;
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Environment parameter " + envParam + " is needed for the execution !!!", e);
        }
    }
}
