package com.faculty.controller;

import com.faculty.dao.LecturerDAO;
import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.view.LecturerDashboardView;
import javax.swing.*;
import java.util.List;

public class LecturerController {
    private LecturerDashboardView view;
    private LecturerDAO dao;
    private String username;

    public LecturerController(LecturerDashboardView view, LecturerDAO dao, String username) {
        this.view = view;
        this.dao = dao;
        this.username = username;

        loadLecturerData();
        this.view.getBtnSaveProfile().addActionListener(e -> handleProfileUpdate());
    }

    private void loadLecturerData() {
        Lecturer currentLecturer = dao.getLecturerProfile(username);

        if (currentLecturer != null) {
            view.setFullName(currentLecturer.getFullName());
            view.setDepartment(currentLecturer.getDepartmentName());
            view.setEmail(currentLecturer.getEmail());
            view.setMobile(currentLecturer.getMobile());
        }

        // Load courses independently so they display even if the profile details are incomplete
        List<Course> courses = dao.getTeachingCourses(username);
        view.getTableModel().setRowCount(0);
        for (Course c : courses) {
            view.getTableModel().addRow(new Object[]{
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getCredits()
            });
        }
    }

    private void handleProfileUpdate() {
        // Read directly from the view text fields instead of relying on the model variable
        String newName = view.getFullName();
        String newEmail = view.getEmail();
        String newMobile = view.getMobile();

        // Pass the raw data to the DAO so it can handle Inserts or Updates automatically
        boolean success = dao.updateLecturerProfile(username, newName, newEmail, newMobile);

        if (success) {
            JOptionPane.showMessageDialog(view, "Profile details saved successfully!");
            loadLecturerData(); // Refresh the screen
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update profile.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}