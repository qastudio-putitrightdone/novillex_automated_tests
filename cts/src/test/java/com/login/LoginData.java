package com.login;

import org.testng.annotations.DataProvider;

public class LoginData {

    @DataProvider(name = "userData")
    public Object[][] getLoginData() {
        return new Object[][] {{"8585", "Bank@123"}};
    }
}
