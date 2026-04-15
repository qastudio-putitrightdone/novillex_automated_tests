package com.ctsDashBoard;

import org.testng.annotations.DataProvider;

public class DashboardData {

    @DataProvider(name = "userData")
    public Object[][] getLoginData() {
        return new Object[][] {{"1003", "Qwerty@123"}};
    }
}
