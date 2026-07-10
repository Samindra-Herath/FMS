package com.faculty.controller;

import com.faculty.view.AdminDashboardView;
import javax.swing.*;

public class AdminController {

    private AdminDashboardView view;

    public AdminController(AdminDashboardView view) {
        this.view = view;
        initControllerActions();
    }

    private void initControllerActions() {
        // This is where you will listen to btnAdd, btnEdit, and btnDelete clicks later
    }
}