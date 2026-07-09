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
        String sql = "SELECT s.student_id, s.full_name, s.student_reg_id, d.degree_name, s.email, s.mobile " +
                "FROM students s " +
                "JOIN users u ON s.student_id = u.user_id " +
                "LEFT JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE u.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("student_id"),
                            rs.getString("full_name"),
                            rs.getString("student_reg_id"),
                            rs.getString("degree_name") != null ? rs.getString("degree_name") : "Not Assigned",
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

    public boolean updateStudentProfile(Student student) {
        String sql = "UPDATE students SET full_name=?, student_reg_id=?, email=?, mobile=? WHERE student_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getStudentRegId());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getMobileNumber());
            stmt.setInt(5, student.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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