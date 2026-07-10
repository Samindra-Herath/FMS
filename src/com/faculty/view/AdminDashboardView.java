package com.faculty.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    private JTable tblData;
    private DefaultTableModel tableModel;
    private JLabel lblCurrentTab;
    private String currentView = "Students";

    public AdminDashboardView(String username) {
        setTitle("Faculty Management System - Admin Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR PANEL ---
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(new Color(110, 44, 194));
        sidebar.setPreferredSize(new Dimension(230, 600));

        JLabel lblAvatar = new JLabel("W", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Arial", Font.PLAIN, 45));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setBounds(25, 20, 180, 50);
        sidebar.add(lblAvatar);

        JLabel lblWelcome = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        lblWelcome.setBounds(25, 80, 180, 25);
        sidebar.add(lblWelcome);

        JButton btnStudents = createSidebarButton("Students", 140);
        JButton btnLecturers = createSidebarButton("Lecturers", 190);
        JButton btnCourses = createSidebarButton("Courses", 240);
        JButton btnDepartments = createSidebarButton("Departments", 290);
        JButton btnDegrees = createSidebarButton("Degrees", 340);

        JButton btnLogout = new JButton("Sign Out");
        btnLogout.setBounds(15, 500, 200, 35);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));

        sidebar.add(btnStudents);
        sidebar.add(btnLecturers);
        sidebar.add(btnCourses);
        sidebar.add(btnDepartments);
        sidebar.add(btnDegrees);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // --- 2. MAIN WORKSPACE PANEL ---
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Heading & CRUD Action Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        lblCurrentTab = new JLabel("Students");
        lblCurrentTab.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(lblCurrentTab, BorderLayout.WEST);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add new");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        crudPanel.add(btnAdd);
        crudPanel.add(btnEdit);
        crudPanel.add(btnDelete);
        topPanel.add(crudPanel, BorderLayout.EAST);

        mainContentPanel.add(topPanel, BorderLayout.NORTH);

        // Central Grid Table View
        tableModel = new DefaultTableModel();
        tblData = new JTable(tableModel);
        tblData.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblData);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Decorative Bar / Save changes button area
        JButton btnSaveChanges = new JButton("Save changes");
        btnSaveChanges.setBackground(new Color(110, 44, 194));
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setFont(new Font("Arial", Font.BOLD, 14));
        mainContentPanel.add(btnSaveChanges, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);

        // --- 3. TAB EVENT ROUTING ---
        btnStudents.addActionListener(e -> switchTab("Students"));
        btnLecturers.addActionListener(e -> switchTab("Lecturers"));
        btnCourses.addActionListener(e -> switchTab("Courses"));
        btnDepartments.addActionListener(e -> switchTab("Departments"));
        btnDegrees.addActionListener(e -> switchTab("Degrees"));

        btnLogout.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        // Initialize display headers
        switchTab("Students");
        new com.faculty.controller.AdminController(this);
    }

    private JButton createSidebarButton(String text, int yPosition) {
        JButton button = new JButton(text);
        button.setBounds(15, yPosition, 200, 35);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(110, 44, 194));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;
    }

    private void switchTab(String targetTab) {
        currentView = targetTab;
        lblCurrentTab.setText(targetTab);

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Setup individual layout metadata configurations matching your schema properties
        if (currentView.equals("Students")) {
            tableModel.setColumnIdentifiers(new String[]{"Student ID", "Full Name", "Batch", "Degree"});
        } else if (currentView.equals("Lecturers")) {
            tableModel.setColumnIdentifiers(new String[]{"Lecturer ID", "Full Name", "Department"});
        } else if (currentView.equals("Courses")) {
            tableModel.setColumnIdentifiers(new String[]{"Course Code", "Course Name", "Credits"});
        } else if (currentView.equals("Departments")) {
            tableModel.setColumnIdentifiers(new String[]{"Department ID", "Department Name"});
        } else if (currentView.equals("Degrees")) {
            tableModel.setColumnIdentifiers(new String[]{"Degree ID", "Degree Name"});
        }

        // Add dummy rows to let you step through and visualize layouts natively
        tableModel.addRow(new Object[]{"Sample ID 1", "Placeholder Row Data A", "Val 1", "Val 2"});
        tableModel.addRow(new Object[]{"Sample ID 2", "Placeholder Row Data B", "Val 3", "Val 4"});
    }
}