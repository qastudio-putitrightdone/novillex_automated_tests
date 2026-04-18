package com.cts.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlserver://172.16.0.38:1433;databaseName=master;encrypt=false";

        Connection conn = DriverManager.getConnection(url, "shubhamm", "$huBh@m21");

        System.out.println("Connected successfully!");
        conn.close();
    }
}
