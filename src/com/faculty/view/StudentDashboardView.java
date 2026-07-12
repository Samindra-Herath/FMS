package com.faculty.view;

import com.faculty.dao.StudentDAO;
import com.faculty.model.Course;
import com.faculty.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboardView extends JFrame {
    private String loggedInUser;
    private StudentDAO studentDAO;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Form Reference Components
    private JTextField txtName, txtRegId, txtDegree, txtEmail, txtMobile;

    public StudentDashboardView(String username) {
        this.loggedInUser = username;
        this.studentDAO = new StudentDAO();

        setTitle("Faculty Management System - Student Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. STYLED PURPLE SIDEBAR
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(new Color(122, 67, 225));
        sidebar.setPreferredSize(new Dimension(230, 600));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Arial", Font.PLAIN, 45));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setBounds(25, 20, 180, 50);
        sidebar.add(lblAvatar);

        JLabel lblWelcome = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        lblWelcome.setBounds(25, 80, 180, 25);
        sidebar.add(lblWelcome);

        JButton btnProfile = new JButton("Profile Details");
        btnProfile.setBounds(15, 140, 200, 35);
        sidebar.add(btnProfile);

        JButton btnTimetable = new JButton("Time table");
        btnTimetable.setBounds(15, 190, 200, 35);
        sidebar.add(btnTimetable);

        JButton btnCourses = new JButton("Course Enrolled");
        btnCourses.setBounds(15, 240, 200, 35);
        sidebar.add(btnCourses);

        JButton btnLogout = new JButton("↩ Sign Out");
        btnLogout.setBounds(15, 500, 200, 35);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // 2. CENTRAL INTERFACE CARDS CONTAINER
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        mainContentPanel.add(buildProfilePanel(), "Profile");
        mainContentPanel.add(buildTimetablePanel(), "Timetable");
        mainContentPanel.add(buildCoursesPanel(), "Courses");

        add(mainContentPanel, BorderLayout.CENTER);

        // 3. EVENT LAYOUT NAVIGATION ROUTING
        btnProfile.addActionListener(e -> {
            refreshProfileData();
            cardLayout.show(mainContentPanel, "Profile");
        });
        btnTimetable.addActionListener(e -> cardLayout.show(mainContentPanel, "Timetable"));
        btnCourses.addActionListener(e -> {
            refreshEnrolledCourses();
            cardLayout.show(mainContentPanel, "Courses");
        });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Confirm systemic sign-out?", "Sign Out", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true);
                dispose();
            }
        });

        // Initialize display by pulling profile records right away
        refreshProfileData();
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("Profile Details");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(40, 30, 200, 30);
        panel.add(title);

        int startY = 90;
        int spacingY = 45;

        JLabel lblName = new JLabel("Full Name:"); lblName.setBounds(40, startY, 100, 25); panel.add(lblName);
        txtName = new JTextField(); txtName.setBounds(160, startY, 300, 25); panel.add(txtName);

        JLabel lblReg = new JLabel("Student ID:"); lblReg.setBounds(40, startY + spacingY, 100, 25); panel.add(lblReg);
        txtRegId = new JTextField(); txtRegId.setBounds(160, startY + spacingY, 300, 25); panel.add(txtRegId);

        JLabel lblDeg = new JLabel("Degree:"); lblDeg.setBounds(40, startY + (spacingY * 2), 100, 25); panel.add(lblDeg);
        txtDegree = new JTextField(); txtDegree.setBounds(160, startY + (spacingY * 2), 300, 25); txtDegree.setEditable(false); panel.add(txtDegree);

        JLabel lblEmail = new JLabel("Email:"); lblEmail.setBounds(40, startY + (spacingY * 3), 100, 25); panel.add(lblEmail);
        txtEmail = new JTextField(); txtEmail.setBounds(160, startY + (spacingY * 3), 300, 25); panel.add(txtEmail);

        JLabel lblMobile = new JLabel("Mobile Number:"); lblMobile.setBounds(40, startY + (spacingY * 4), 100, 25); panel.add(lblMobile);
        txtMobile = new JTextField(); txtMobile.setBounds(160, startY + (spacingY * 4), 300, 25); panel.add(txtMobile);

        JButton btnSaveChanges = new JButton("Save changes");
        btnSaveChanges.setBackground(new Color(122, 67, 225));
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaveChanges.setBounds(160, startY + (spacingY * 5) + 10, 150, 35);
        panel.add(btnSaveChanges);

        btnSaveChanges.addActionListener(e -> saveProfileModifications());
        return panel;
    }

    private void refreshProfileData() {
        Student activeStudentProfile = studentDAO.getStudentProfile(loggedInUser);
        if (activeStudentProfile != null) {
            txtName.setText(activeStudentProfile.getFullName() != null ? activeStudentProfile.getFullName() : "");
            txtRegId.setText(activeStudentProfile.getStudentRegId() != null ? activeStudentProfile.getStudentRegId() : "");
            txtDegree.setText(activeStudentProfile.getDegreeName() != null ? activeStudentProfile.getDegreeName() : "Not Assigned");
            txtEmail.setText(activeStudentProfile.getEmail() != null ? activeStudentProfile.getEmail() : "");
            txtMobile.setText(activeStudentProfile.getMobileNumber() != null ? activeStudentProfile.getMobileNumber() : "");
        }
    }

    private void saveProfileModifications() {
        // Read directly from text fields to bypass null model check
        String newName = txtName.getText();
        String newRegId = txtRegId.getText();
        String newEmail = txtEmail.getText();
        String newMobile = txtMobile.getText();

        if (studentDAO.updateStudentProfile(loggedInUser, newName, newRegId, newEmail, newMobile)) {
            JOptionPane.showMessageDialog(this, "Profile state updated successfully.");
            refreshProfileData();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Data writing failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Time table", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.add(title, BorderLayout.NORTH);

        // Explicit structural scheduling mapping from page 6 of guidelines
        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[][] data = {
                {"08.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
                {"10.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
                {"Interval", "Interval", "Interval", "Interval", "Interval", "Interval"},
                {"01.00", "SE", "OOP", "SE", "SE", "SE"},
                {"03.00", "SE", "OOP", "SE", "SE", "SE"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private DefaultTableModel coursesTableModel;

    private JPanel buildCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Courses Enrolled", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Course code", "Course name", "Credits", "Grade"};
        coursesTableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(coursesTableModel);
        table.setRowHeight(35);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshEnrolledCourses() {
        coursesTableModel.setRowCount(0); // Wipe stale data views
        List<Course> userCourses = studentDAO.getEnrolledCourses(loggedInUser);
        for (Course c : userCourses) {
            coursesTableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredits(), c.getGrade()});
        }
    }
}