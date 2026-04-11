package com.utils.constats;

public enum UrlPathsEnum {

    LOGIN("/sign-in");

    private final String urlPath;

    UrlPathsEnum(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlPath() {
        return urlPath;
    }
}
