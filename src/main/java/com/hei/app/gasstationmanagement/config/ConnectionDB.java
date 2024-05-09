package com.hei.app.gasstationmanagement.config;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionDB {
    static String databaseUrl = System.getenv("DB_URL");
    static String databaseUser = System.getenv("DB_USER");
    static String databasePassword = System.getenv("DB_PASSWORD");
    static Connection connection;

    public static Connection createConnection() {
        try {
            if (connection == null) {
                return DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
