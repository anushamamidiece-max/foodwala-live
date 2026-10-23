package com.foodwala.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Central JDBC helper for Foodwala project.
 * Automatically checks for Render Environment Variables first.
 * If not found, falls back to your Aiven MySQL Database.
 */
public class DBUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // 1. Default Aiven Connection Details
    private static final String DEFAULT_URL = "jdbc:mysql://foodwala-db-foodwala-project.i.aivencloud.com:22514/foodwala?ssl-mode=REQUIRED&serverTimezone=UTC";
    private static final String DEFAULT_USER = "avnadmin";
    
    // ⚠️ REPLACE 'YOUR_ACTUAL_AIVEN_PASSWORD_HERE' WITH YOUR REAL AIVEN PASSWORD ⚠️
    private static final String DEFAULT_PASSWORD = "AVNS_NEZG-XCw-SilLJS6ski"; 

    // 2. Read from Environment Variables (Render) or use Defaults
    private static final String URL = System.getenv().getOrDefault("FOODWALA_DB_URL", DEFAULT_URL);
    private static final String DB_USER = System.getenv().getOrDefault("FOODWALA_DB_USER", DEFAULT_USER);
    private static final String DB_PASSWORD = System.getenv().getOrDefault("FOODWALA_DB_PASSWORD", DEFAULT_PASSWORD);

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Connector/J not found on classpath!");
            e.printStackTrace();
        }
    }

    /** Opens a new connection to the database. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }

    /** Null-safe close of Connection / Statement / ResultSet. */
    public static void close(Connection con, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }
}