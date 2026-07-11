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
    private Lecturer currentLecturer;

    public LecturerController(LecturerDashboardView view, LecturerDAO dao, String username) {
        this.view = view;
        this.dao = dao;
        this.username = username;

        loadLecturerData();
        this.view.getBtnSaveProfile().addActionListener(e -> handleProfileUpdate());
    }

    private void loadLecturerData() {
        currentLecturer = dao.getLecturerProfile(username);

        if (currentLecturer != null) {
            view.setFullName(currentLecturer.getFullName());
            view.setDepartment(currentLecturer.getDepartmentName());
            view.setEmail(currentLecturer.getEmail());
            view.setMobile(currentLecturer.getMobile()); // Note: Make sure your Lecturer model has this getter or match it to your model fields

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
        if (currentLecturer == null) return;

        currentLecturer.setFullName(view.getFullName());
        currentLecturer.setEmail(view.getEmail());
        currentLecturer.setMobile(view.getMobile()); // Note: Match to your model setter name

        boolean success = dao.updateLecturerProfile(currentLecturer);
        if (success) {
            JOptionPane.showMessageDialog(view, "Profile updated successfully!");
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
