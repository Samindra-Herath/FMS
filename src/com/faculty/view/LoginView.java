package com.faculty.view;

import com.faculty.dao.UserDAO;
import com.faculty.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JRadioButton rbAdmin, rbStudent, rbLecturer;
    private ButtonGroup roleGroup;
    private JButton btnSignIn, btnSignUp;

    public LoginView() {
        setTitle("Faculty Management System - Authentication");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblTitle = new JLabel("Sign In", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(50, 20, 300, 30);
        add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(40, 80, 100, 25);
        add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 80, 200, 25);
        add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(40, 120, 100, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 120, 200, 25);
        add(txtPassword);

        rbAdmin = new JRadioButton("Admin");
        rbAdmin.setBounds(80, 160, 70, 25);
        rbStudent = new JRadioButton("Student");
        rbStudent.setBounds(155, 160, 80, 25);
        rbStudent.setSelected(true);
        rbLecturer = new JRadioButton("Lecturer");
        rbLecturer.setBounds(240, 160, 90, 25);

        roleGroup = new ButtonGroup();
        roleGroup.add(rbAdmin);
        roleGroup.add(rbStudent);
        roleGroup.add(rbLecturer);

        add(rbAdmin);
        add(rbStudent);
        add(rbLecturer);

        btnSignIn = new JButton("Sign In");
        btnSignIn.setBounds(80, 210, 100, 35);
        add(btnSignIn);

        btnSignUp = new JButton("Sign Up");
        btnSignUp.setBounds(200, 210, 100, 35);
        add(btnSignUp);

        btnSignIn.addActionListener(e -> executeAuthentication());

        btnSignUp.addActionListener(e -> {
            new SignupGUI().setVisible(true);
            dispose();
        });
    }

    private void executeAuthentication() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String role = "Student";

        if (rbAdmin.isSelected()) role = "Admin";
        else if (rbLecturer.isSelected()) role = "Lecturer";

        UserDAO userDAO = new UserDAO();
        User sessionUser = userDAO.authenticate(username, password, role);

        if (sessionUser != null) {
            if (role.equals("Student")) {
                new StudentDashboardView(username).setVisible(true);
                dispose();
            } else if (role.equals("Lecturer")) {
                com.faculty.view.LecturerDashboardView lecturerView = new com.faculty.view.LecturerDashboardView(username);
                com.faculty.dao.LecturerDAO lecturerDao = new com.faculty.dao.LecturerDAO();
                new com.faculty.controller.LecturerController(lecturerView, lecturerDao, username);
                lecturerView.setVisible(true);
                dispose();
            } else if (role.equals("Admin")) {
                new AdminDashboardView(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Module operational for Students only in current iteration.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Access Denied: Invalid parameters matched.", "Auth Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}