package com.example.crudapp.dao;

import com.example.crudapp.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHandler {

    private String url = "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;";
    private String user = "sa";
    private String password = "YourPassword123";
    private String databaseName = "testdb";

    public UserDatabaseHandler() {
        setupDatabase();
    }

    private void setupDatabase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            statement.execute("IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = '" + databaseName + "') CREATE DATABASE " + databaseName);
            statement.execute("USE " + databaseName);

            String createTableSQL = "IF OBJECT_ID('users', 'U') IS NULL " +
                    "CREATE TABLE users (" +
                    "id BIGINT IDENTITY(1,1) PRIMARY KEY," +
                    "email VARCHAR(255) NOT NULL UNIQUE," +
                    "first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(50)," +
                    "employee_type VARCHAR(50)," +
                    "company_name VARCHAR(255)," +
                    "active BIT DEFAULT 1)";
            
            statement.execute(createTableSQL);
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Error during setup: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement s = conn.createStatement();
        s.execute("USE " + databaseName);
        s.close();
        return conn;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setPhone(rs.getString("phone"));
                u.setEmployeeType(rs.getString("employee_type"));
                u.setCompanyName(rs.getString("company_name"));
                u.setActive(rs.getBoolean("active"));
                userList.add(u);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User getOneUser(Long id) {
        User user = null;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhone(rs.getString("phone"));
                user.setEmployeeType(rs.getString("employee_type"));
                user.setCompanyName(rs.getString("company_name"));
                user.setActive(rs.getBoolean("active"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void addUser(User u) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO users (email, first_name, last_name, phone, employee_type, company_name, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getEmployeeType());
            ps.setString(6, u.getCompanyName());
            ps.setBoolean(7, u.isActive());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                u.setId(keys.getLong(1));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: Could not add user. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(User u) {
        try {
            Connection conn = getConnection();
            String sql = "UPDATE users SET email=?, first_name=?, last_name=?, phone=?, employee_type=?, company_name=?, active=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getEmployeeType());
            ps.setString(6, u.getCompanyName());
            ps.setBoolean(7, u.isActive());
            ps.setLong(8, u.getId());
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: Could not update user. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser(Long id) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
