package com.faculty.dao;

import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    // 1. Fetch Lecturer Profile Details (Fixed JOIN logic to use the username directly)
    public Lecturer getLecturerProfile(String username) {
        String sql = "SELECT l.lecturer_id, l.full_name, d.dept_name, l.email, l.mobile " +
                "FROM lecturers l " +
                "LEFT JOIN departments d ON l.dept_id = d.dept_id " +
                "WHERE l.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lecturer(
                            rs.getInt("lecturer_id"),
                            rs.getString("full_name") != null ? rs.getString("full_name") : "",
                            rs.getString("dept_name") != null ? rs.getString("dept_name") : "Not Assigned",
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

    // 2. Safely Update or Insert Profile Details
    public boolean updateLecturerProfile(String username, String fullName, String email, String mobile) {
        String checkSql = "SELECT lecturer_id FROM lecturers WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Profile exists in lecturers table -> Run standard UPDATE
                String updateSql = "UPDATE lecturers SET full_name=?, email=?, mobile=? WHERE username=?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, fullName);
                    updateStmt.setString(2, email);
                    updateStmt.setString(3, mobile);
                    updateStmt.setString(4, username);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Profile doesn't exist yet (Created via Sign Up) -> Run INSERT
                String insertSql = "INSERT INTO lecturers (username, password, full_name, email, mobile) VALUES (?, 'linked_account', ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, fullName);
                    insertStmt.setString(3, email);
                    insertStmt.setString(4, mobile);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. View Teaching Courses (Fixed relational join)
    public List<Course> getTeachingCourses(String username) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_code, c.course_name, c.credits " +
                "FROM courses c " +
                "JOIN lecturers l ON c.lecturer_id = l.lecturer_id " +
                "WHERE l.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                            rs.getString("course_code"),
                            rs.getString("course_name"),
                            rs.getInt("credits"),
                            ""
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}