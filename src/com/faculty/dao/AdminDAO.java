package com.faculty.dao;

import com.faculty.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public List<Object[]> getAllStudents() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.student_id, s.full_name, s.student_reg_id, d.degree_name, s.email, s.mobile " +
                "FROM students s LEFT JOIN degrees d ON s.degree_id = d.degree_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("full_name"),
                        rs.getString("student_reg_id"),
                        rs.getString("degree_name") != null ? rs.getString("degree_name") : "Not Assigned",
                        rs.getString("email"),
                        rs.getString("mobile")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getAllLecturers() { return new ArrayList<>(); }
    public List<Object[]> getAllCourses() { return new ArrayList<>(); }
    public List<Object[]> getAllDepartments() { return new ArrayList<>(); }
    public List<Object[]> getAllDegrees() { return new ArrayList<>(); }

    public boolean addRecord(String table, Object[] data) {
        if ("Students".equals(table)) {
            String sql = "INSERT INTO students (full_name, student_reg_id, degree_id, email, mobile) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) data[0]);
                stmt.setString(2, (String) data[1]);
                stmt.setInt(3, (Integer) data[2]);
                stmt.setString(4, (String) data[3]);
                stmt.setString(5, (String) data[4]);
                return stmt.executeUpdate() > 0;
            } catch (Exception e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean updateRecord(String table, Object[] data) {
        if ("Students".equals(table)) {
            String sql = "UPDATE students SET full_name = ?, student_reg_id = ?, degree_id = ?, email = ?, mobile = ? WHERE student_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) data[0]);
                stmt.setString(2, (String) data[1]);
                stmt.setInt(3, (Integer) data[2]);
                stmt.setString(4, (String) data[3]);
                stmt.setString(5, (String) data[4]);
                stmt.setInt(6, (Integer) data[5]);
                return stmt.executeUpdate() > 0;
            } catch (Exception e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean deleteRecord(String table, String identifier) {
        if ("Students".equals(table)) {
            String sql = "DELETE FROM students WHERE student_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(identifier));
                return stmt.executeUpdate() > 0;
            } catch (Exception e) { e.printStackTrace(); }
        }
        return false;
    }
}