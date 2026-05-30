package com.srms;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection - Provides a PostgreSQL JDBC connection.
 *
 * HOW TO CONFIGURE:
 *   Change DB_URL, DB_USER, and DB_PASSWORD below to match
 *   your local PostgreSQL setup before running the application.
 */
public class DBConnection {

    // ── Change these three values to match your PostgreSQL setup ──────────
    private static final String DB_URL      = "jdbc:postgresql://localhost:5433/studentdb";
    private static final String DB_USER     = "postgres";
    private static final String DB_PASSWORD = "BIG-Black-Bird!";   // ← change this
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns a live Connection to the database, or null on failure.
     */
    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
            return conn;
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
