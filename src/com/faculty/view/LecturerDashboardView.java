package com.faculty.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LecturerDashboardView extends JFrame {
    private String currentUsername;
    private JLabel lblWelcome;
    private JTextField txtName, txtDept, txtEmail, txtMobile;
    private JButton btnSaveProfile, btnLogout;
    private JTable tblCourses;
    private DefaultTableModel tableModel;
    private JPanel mainWorkspace;
    private CardLayout cardLayout;

    // The three navigation menu buttons
    private JButton btnProfileMenu;
    private JButton btnTimetableMenu;
    private JButton btnCoursesMenu;

    public LecturerDashboardView(String username) {
        this.currentUsername = username;

        setTitle("Faculty Management System - Lecturer Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. PURPLE SIDEBAR PANEL
        JPanel sidebarPanel = new JPanel(null);
        sidebarPanel.setBackground(new Color(122, 67, 225));
        sidebarPanel.setPreferredSize(new Dimension(230, 600));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Arial", Font.PLAIN, 45));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setBounds(25, 20, 180, 50);
        sidebarPanel.add(lblAvatar);

        lblWelcome = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        lblWelcome.setBounds(25, 80, 180, 25);
        sidebarPanel.add(lblWelcome);

        // Sidebar Navigation Buttons arranged perfectly down the layout
        btnProfileMenu = new JButton("My Profile");
        btnProfileMenu.setBounds(15, 140, 200, 35);
        sidebarPanel.add(btnProfileMenu);

        btnTimetableMenu = new JButton("Time Table");
        btnTimetableMenu.setBounds(15, 190, 200, 35);
        sidebarPanel.add(btnTimetableMenu);

        btnCoursesMenu = new JButton("Teaching Courses");
        btnCoursesMenu.setBounds(15, 240, 200, 35);
        sidebarPanel.add(btnCoursesMenu);

        btnLogout = new JButton("↩ Log Out");
        btnLogout.setBounds(15, 500, 200, 35);
        sidebarPanel.add(btnLogout);

        add(sidebarPanel, BorderLayout.WEST);

        // 2. MAIN WORKSPACE PANELS CONTAINER (CardLayout switches views)
        cardLayout = new CardLayout();
        mainWorkspace = new JPanel(cardLayout);

        mainWorkspace.add(buildProfilePanel(), "ProfileCard");
        mainWorkspace.add(buildTimetablePanel(), "TimetableCard");
        mainWorkspace.add(buildCoursesPanel(), "CoursesCard");
        add(mainWorkspace, BorderLayout.CENTER);

        // Backup internal click router rules
        btnProfileMenu.addActionListener(e -> cardLayout.show(mainWorkspace, "ProfileCard"));
        btnTimetableMenu.addActionListener(e -> cardLayout.show(mainWorkspace, "TimetableCard"));
        btnCoursesMenu.addActionListener(e -> cardLayout.show(mainWorkspace, "CoursesCard"));

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Confirm sign-out?", "Sign Out", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginView().setVisible(true);
            }
        });
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("Profile Details");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(40, 30, 200, 30);
        panel.add(title);

        int startY = 100;
        int spacingY = 50;

        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(40, startY, 120, 25); panel.add(lblName);
        txtName = new JTextField(); txtName.setBounds(200, startY, 320, 25); panel.add(txtName);

        JLabel lblDeptLabel = new JLabel("Department:");
        lblDeptLabel.setBounds(40, startY + spacingY, 120, 25); panel.add(lblDeptLabel);
        txtDept = new JTextField(); txtDept.setBounds(200, startY + spacingY, 320, 25); txtDept.setEditable(false); panel.add(txtDept);

        JLabel lblEmail = new JLabel("Email Address:");
        lblEmail.setBounds(40, startY + (spacingY * 2), 120, 25); panel.add(lblEmail);
        txtEmail = new JTextField(); txtEmail.setBounds(200, startY + (spacingY * 2), 320, 25); panel.add(txtEmail);

        JLabel lblMobile = new JLabel("Mobile Number:");
        lblMobile.setBounds(40, startY + (spacingY * 3), 120, 25); panel.add(lblMobile);
        txtMobile = new JTextField(); txtMobile.setBounds(200, startY + (spacingY * 3), 320, 25);
        panel.add(txtMobile);

        btnSaveProfile = new JButton("Save Changes");
        btnSaveProfile.setBackground(new Color(122, 67, 225));
        btnSaveProfile.setForeground(Color.WHITE);
        btnSaveProfile.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaveProfile.setBounds(200, startY + (spacingY * 4) + 10, 150, 35);
        panel.add(btnSaveProfile);

        return panel;
    }

    private JPanel buildTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Time table", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(122, 67, 225));
        title.setBorder(new EmptyBorder(15, 10, 15, 10));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[][] data = {
                {"08.00", "OOP", "DBMS", "OOP", "Network", "OOP"},
                {"10.00", "OOP", "DBMS", "OOP", "Network", "OOP"},
                {"Interval", "Interval", "Interval", "Interval", "Interval", "Interval"},
                {"01.00", "SE", "OOP", "SE", "DSA", "SE"},
                {"03.00", "SE", "OOP", "SE", "DSA", "SE"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(40);
        table.setGridColor(new Color(122, 67, 225));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Assigned Course Modules", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 10, 15, 10));
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Course Code", "Course Name", "Credits"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblCourses = new JTable(tableModel);
        tblCourses.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(tblCourses);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Encapsulation Getters for Controller interaction
    public JButton getBtnProfileMenu() {
        return btnProfileMenu;
    }
    public JButton getBtnTimetableMenu() {
        return btnTimetableMenu;
    }
    public JButton getBtnCoursesMenu() {
        return btnCoursesMenu;
    }
    public CardLayout getCardLayout() {
        return cardLayout;
    }
    public JPanel getMainWorkspace() {
        return mainWorkspace;
    }

    public String getFullName() {
        return txtName.getText();
    }
    public void setFullName(String val) {
        txtName.setText(val);
    }
    public String getEmail() {
        return txtEmail.getText();
    }
    public void setEmail(String val) {
        txtEmail.setText(val);
    }
    public String getMobile() {
        return txtMobile.getText();
    }
    public void setMobile(String val) {
        txtMobile.setText(val);
    }
    public void setDepartment(String val) {
        txtDept.setText(val);
    }
    public JButton getBtnSaveProfile() {
        return btnSaveProfile;
    }
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}