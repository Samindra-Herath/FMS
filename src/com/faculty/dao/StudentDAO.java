package com.faculty.dao;

import com.faculty.model.Course;
import com.faculty.model.Student;
import com.faculty.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public Student getStudentProfile(String username) {
        // Changed to LEFT JOIN from users so a profile loads even if it's new/empty
        String sql = "SELECT u.user_id, s.full_name, s.student_reg_id, d.degree_name, s.email, s.mobile " +
                "FROM users u " +
                "LEFT JOIN students s ON u.user_id = s.student_id " +
                "LEFT JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE u.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("user_id"),
                            rs.getString("full_name") != null ? rs.getString("full_name") : "",
                            rs.getString("student_reg_id") != null ? rs.getString("student_reg_id") : "",
                            rs.getString("degree_name") != null ? rs.getString("degree_name") : "Not Assigned",
                            rs.getString("email") != null ? rs.getString("email") : "",
                            rs.getString("mobile") != null ? rs.getString("mobile") : ""
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Safely handles both UPDATE for existing and INSERT for newly registered accounts
    public boolean updateStudentProfile(String username, String fullName, String regId, String email, String mobile) {
        String checkSql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkUserStmt = conn.prepareStatement(checkSql)) {

            checkUserStmt.setString(1, username);
            ResultSet rsUser = checkUserStmt.executeQuery();

            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");

                String checkStudentSql = "SELECT student_id FROM students WHERE student_id = ?";
                try (PreparedStatement checkStudentStmt = conn.prepareStatement(checkStudentSql)) {
                    checkStudentStmt.setInt(1, userId);
                    ResultSet rsStudent = checkStudentStmt.executeQuery();

                    if (rsStudent.next()) {
                        // Profile exists -> Execute standard UPDATE
                        String updateSql = "UPDATE students SET full_name=?, student_reg_id=?, email=?, mobile=? WHERE student_id=?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, fullName);
                            updateStmt.setString(2, regId);
                            updateStmt.setString(3, email);
                            updateStmt.setString(4, mobile);
                            updateStmt.setInt(5, userId);
                            return updateStmt.executeUpdate() > 0;
                        }
                    } else {
                        // New signup Profile -> Execute INSERT
                        String insertSql = "INSERT INTO students (student_id, full_name, student_reg_id, email, mobile) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setInt(1, userId);
                            insertStmt.setString(2, fullName);
                            insertStmt.setString(3, regId);
                            insertStmt.setString(4, email);
                            insertStmt.setString(5, mobile);
                            return insertStmt.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Course> getEnrolledCourses(String username) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_code, c.course_name, c.credits, e.grade " +
                "FROM courses c " +
                "JOIN enrollments e ON c.course_code = e.course_code " +
                "JOIN users u ON e.student_id = u.user_id " +
                "WHERE u.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String grade = rs.getString("grade") != null ? rs.getString("grade") : "Pending";
                    list.add(new Course(rs.getString("course_code"), rs.getString("course_name"), rs.getInt("credits"), grade));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}