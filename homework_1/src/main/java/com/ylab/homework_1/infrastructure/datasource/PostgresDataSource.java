package com.ylab.homework_1.infrastructure.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresDataSource {
    private static String url;
    private static String username;
    private static String password;

    public static void initDB(Properties properties) {
        url = properties.getProperty("db.url");
        username = properties.getProperty("db.user");
        password = properties.getProperty("db.password");

        if (url == null || username == null || password == null) {
            throw new IllegalArgumentException("Error receiving data");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}