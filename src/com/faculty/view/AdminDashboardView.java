package com.faculty.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    private JTable tblData;
    private DefaultTableModel tableModel;
    private JLabel lblCurrentTab;
    private String currentView = "Students";

    private JButton btnStudents, btnLecturers, btnCourses, btnDepartments, btnDegrees, btnLogout;
    private JButton btnAdd, btnEdit, btnDelete, btnSaveChanges;

    public AdminDashboardView(String username) {
        setTitle("Faculty Management System - Admin Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        btnStudents = createSidebarButton("Students", 140);
        btnLecturers = createSidebarButton("Lecturers", 190);
        btnCourses = createSidebarButton("Courses", 240);
        btnDepartments = createSidebarButton("Departments", 290);
        btnDegrees = createSidebarButton("Degrees", 340);

        btnLogout = new JButton("Sign Out");
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

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout());
        lblCurrentTab = new JLabel("Students");
        lblCurrentTab.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(lblCurrentTab, BorderLayout.WEST);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Add new");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        crudPanel.add(btnAdd);
        crudPanel.add(btnEdit);
        crudPanel.add(btnDelete);
        topPanel.add(crudPanel, BorderLayout.EAST);
        mainContentPanel.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tblData = new JTable(tableModel);
        tblData.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblData);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        btnSaveChanges = new JButton("Save changes");
        btnSaveChanges.setBackground(new Color(110, 44, 194));
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setFont(new Font("Arial", Font.BOLD, 14));
        mainContentPanel.add(btnSaveChanges, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);

        btnLogout.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        updateTabHeaders("Students");
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

    // UPDATED FUNCTION: Aligned exactly to the parameters inside your database schema definitions
    public void updateTabHeaders(String targetTab) {
        this.currentView = targetTab;
        lblCurrentTab.setText(targetTab);
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (currentView.equals("Students")) {
            tableModel.setColumnIdentifiers(new String[]{"ID", "Full Name", "Reg ID", "Degree", "Email", "Mobile"});
        } else if (currentView.equals("Lecturers")) {
            // Displays your core database metadata alongside the joined department context
            tableModel.setColumnIdentifiers(new String[]{"Lecturer ID", "Full Name", "Department", "Email", "Mobile Number"});
        } else if (currentView.equals("Courses")) {
            tableModel.setColumnIdentifiers(new String[]{"Course Code", "Course Name", "Credits", "Assigned Lecturer ID"});
        } else if (currentView.equals("Departments")) {
            tableModel.setColumnIdentifiers(new String[]{"Department ID", "Department Name"});
        } else if (currentView.equals("Degrees")) {
            tableModel.setColumnIdentifiers(new String[]{"Degree ID", "Degree Name", "Linked Dept ID"});
        }
    }

    public DefaultTableModel getTableModel() { return this.tableModel; }
    public JTable getTblData() { return this.tblData; }
    public JButton getBtnStudents() { return btnStudents; }
    public JButton getBtnLecturers() { return btnLecturers; }
    public JButton getBtnCourses() { return btnCourses; }
    public JButton getBtnDepartments() { return btnDepartments; }
    public JButton getBtnDegrees() { return btnDegrees; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnSaveChanges() { return btnSaveChanges; }
}