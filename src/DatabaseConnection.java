/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rizqi mubarrok
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/DatabaseDonasiKita"; // Ganti 'login_db' sesuai database Anda
    private static final String USER = "postgres"; // Ganti dengan username PostgreSQL Anda
    private static final String PASSWORD = "barok"; // Ganti dengan password PostgreSQL Anda

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // Tes koneksi
        Connection connection = connect();
        if (connection != null) {
            System.out.println("Connection test successful.");
        } else {
            System.out.println("Connection test failed.");
        }
    }
}