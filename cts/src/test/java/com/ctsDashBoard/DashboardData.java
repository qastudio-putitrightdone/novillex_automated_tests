package com.ctsDashBoard;

import org.testng.annotations.DataProvider;

public class DashboardData {

    @DataProvider(name = "userData")
    public Object[][] getLoginData() {
        return new Object[][] {{"8585","Bank@123"}};
    }

    @DataProvider(name = "userData1")
    public Object[][] multiuserLoginData() {
        return new Object[][] {{"1001", "Bank@123"},
                {"1004","Bank@123"},{"1008","Bank@123"},{"8585","Bank@123"},{"1005","Bank@123"}};
    }
}
