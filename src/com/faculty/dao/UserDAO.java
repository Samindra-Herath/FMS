package com.faculty.dao;

import com.faculty.model.User;
import com.faculty.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public User authenticate(String username, String password, String role) {
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}