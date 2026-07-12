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

    // FIXED: Now reads email and mobile variables to fill the dashboard table layout grid spaces properly
    public List<Object[]> getAllLecturers() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT l.lecturer_id, l.full_name, d.dept_name, l.email, l.mobile " +
                "FROM lecturers l LEFT JOIN departments d ON l.dept_id = d.dept_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("lecturer_id"),
                        rs.getString("full_name"),
                        rs.getString("dept_name") != null ? rs.getString("dept_name") : "Not Assigned",
                        rs.getString("email") != null ? rs.getString("email") : "",
                        rs.getString("mobile") != null ? rs.getString("mobile") : ""
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

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

        if ("Lecturers".equals(table)) {
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Lecturer')";
            String lecSql = "INSERT INTO lecturers (lecturer_id, full_name, dept_id, email, mobile) VALUES (?, ?, ?, ?, ?)";
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false);

                try (PreparedStatement stmt1 = conn.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt1.setString(1, (String) data[0]);
                    stmt1.setString(2, (String) data[1]);
                    stmt1.executeUpdate();

                    ResultSet rs = stmt1.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedUserId = rs.getInt(1);

                        try (PreparedStatement stmt2 = conn.prepareStatement(lecSql)) {
                            stmt2.setInt(1, generatedUserId);
                            stmt2.setString(2, (String) data[2]);
                            stmt2.setInt(3, (Integer) data[3]);
                            stmt2.setString(4, (String) data[4]);
                            stmt2.setString(5, (String) data[5]);
                            stmt2.executeUpdate();
                        }
                    }
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                if (conn != null) { try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); } }
                e.printStackTrace();
            } finally {
                if (conn != null) { try { conn.close(); } catch (Exception ex) { ex.printStackTrace(); } }
            }
        }
        return false;
    }

    // FIXED: SQL string and statement parameters now modify full_name, dept_id, email, and mobile updates together
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

        if ("Lecturers".equals(table)) {
            String sql = "UPDATE lecturers SET full_name = ?, dept_id = ?, email = ?, mobile = ? WHERE lecturer_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) data[0]); // Full name
                stmt.setInt(2, (Integer) data[1]);   // Dept ID
                stmt.setString(3, (String) data[2]);  // Email
                stmt.setString(4, (String) data[3]);  // Mobile
                stmt.setInt(5, (Integer) data[4]);   // Lecturer ID (target)
                return stmt.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        if ("Lecturers".equals(table)) {
            int targetId = Integer.parseInt(identifier);
            String deleteLec = "DELETE FROM lecturers WHERE lecturer_id = ?";
            String deleteUser = "DELETE FROM users WHERE user_id = ?";
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false);

                try (PreparedStatement stmt1 = conn.prepareStatement(deleteLec)) {
                    stmt1.setInt(1, targetId);
                    stmt1.executeUpdate();
                }
                try (PreparedStatement stmt2 = conn.prepareStatement(deleteUser)) {
                    stmt2.setInt(1, targetId);
                    stmt2.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                if (conn != null) { try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); } }
                e.printStackTrace();
            } finally {
                if (conn != null) { try { conn.close(); } catch (Exception ex) { ex.printStackTrace(); } }
            }
        }
        return false;
    }
}