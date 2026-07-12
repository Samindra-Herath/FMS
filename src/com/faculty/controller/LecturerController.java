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

        // Automatically sync and check if the profile exists when logging in
        Lecturer profileCheck = this.dao.getLecturerProfile(username);
        if (profileCheck == null) {
            this.dao.createNewLecturerProfile(username);
        }

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
    }

    private void handleProfileUpdate() {
        if (currentLecturer == null) {
            currentLecturer = dao.getLecturerProfile(username);
            if (currentLecturer == null) return;
        }

        currentLecturer.setFullName(view.getFullName());
        currentLecturer.setEmail(view.getEmail());
        currentLecturer.setMobile(view.getMobile());

        if (success) {
            JOptionPane.showMessageDialog(view, "Profile updated successfully!");
            loadLecturerData(); // Safely refresh fields on the UI screen
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update profile.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}