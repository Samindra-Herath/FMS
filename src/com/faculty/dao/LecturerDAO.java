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

    // 1. Fetch Lecturer Profile Details
    public Lecturer getLecturerProfile(String username) {
        String sql = "SELECT l.lecturer_id, l.full_name, d.dept_name, l.email, l.mobile " +
                "FROM lecturers l " +
                "JOIN users u ON l.lecturer_id = u.user_id " +
                "LEFT JOIN departments d ON l.dept_id = d.dept_id " +
                "WHERE u.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lecturer(
                            rs.getInt("lecturer_id"),
                            rs.getString("full_name"),
                            rs.getString("dept_name") != null ? rs.getString("dept_name") : "Not Assigned",
                            rs.getString("email"),
                            rs.getString("mobile")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 💡 NEW METHOD: Safely links to the master user record and generates the missing profile dynamically
    public boolean createNewLecturerProfile(String username) {
        String findUserIdSql = "SELECT user_id FROM users WHERE username = ? AND role = 'Lecturer'";
        String insertLecturerSql = "INSERT INTO lecturers (lecturer_id, username, password, full_name, dept_id, email, mobile) VALUES (?, ?, '123', ?, 1, '', '')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtFind = conn.prepareStatement(findUserIdSql)) {

            stmtFind.setString(1, username);
            try (ResultSet rs = stmtFind.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");

                    try (PreparedStatement stmtInsert = conn.prepareStatement(insertLecturerSql)) {
                        stmtInsert.setInt(1, userId);
                        stmtInsert.setString(2, username);
                        stmtInsert.setString(3, username); // Default fullname set as the username initial identifier
                        return stmtInsert.executeUpdate() > 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Update Profile Details
    public boolean updateLecturerProfile(Lecturer lecturer) {
        String sql = "UPDATE lecturers SET full_name=?, email=?, mobile=? WHERE lecturer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturer.getFullName());
            stmt.setString(2, lecturer.getEmail());
            stmt.setString(3, lecturer.getMobile());
            stmt.setInt(4, lecturer.getLecturerId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. View Teaching Courses
    public List<Course> getTeachingCourses(String username) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_code, c.course_name, c.credits " +
                "FROM courses c " +
                "JOIN users u ON c.lecturer_id = u.user_id " +
                "WHERE u.username = ?";
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