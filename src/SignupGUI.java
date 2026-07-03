import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupGUI extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JRadioButton rbStudent, rbLecturer, rbAdmin;
    private ButtonGroup roleGroup;
    private JButton btnRegister, btnBack;

    public SignupGUI() {
        setTitle("FMS - Sign Up");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Title
        JLabel lblTitle = new JLabel("Register New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(100, 20, 200, 30);
        add(lblTitle);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 70, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(170, 70, 160, 25);
        add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(170, 110, 160, 25);
        add(txtPassword);

        // Confirm Password
        JLabel lblConfirm = new JLabel("Confirm Pass:");
        lblConfirm.setBounds(50, 150, 100, 25);
        add(lblConfirm);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(170, 150, 160, 25);
        add(txtConfirmPassword);

        // Role Selection
        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(50, 190, 100, 25);
        add(lblRole);

        rbStudent = new JRadioButton("Student");
        rbStudent.setBounds(120, 190, 75, 25);
        rbStudent.setSelected(true);

        rbLecturer = new JRadioButton("Lecturer");
        rbLecturer.setBounds(195, 190, 80, 25);

        rbAdmin = new JRadioButton("Admin");
        rbAdmin.setBounds(275, 190, 70, 25);

        roleGroup = new ButtonGroup();
        roleGroup.add(rbStudent);
        roleGroup.add(rbLecturer);
        roleGroup.add(rbAdmin);

        add(rbStudent);
        add(rbLecturer);
        add(rbAdmin);

        // Register Button
        btnRegister = new JButton("Register");
        btnRegister.setBounds(150, 250, 100, 30);
        add(btnRegister);

        // Back to Login Button
        btnBack = new JButton("Back to Login");
        btnBack.setBounds(135, 290, 130, 30);
        add(btnBack);

        // --- EVENT LISTENERS ---

        // Register Logic
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Go back to Login Screen
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AuthGUI().setVisible(true); // Open Login window
                dispose(); // Close this window
            }
        });
    }

    private void registerUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        String role = "";
        if (rbStudent.isSelected()) role = "Student";
        else if (rbLecturer.isSelected()) role = "Lecturer";
        else if (rbAdmin.isSelected()) role = "Admin";

        // 1. Validation Checks
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Database Insertion
        try {
            Connection conn = DatabaseConnection.getConnection();

            // SQL query to insert the new user
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            // executeUpdate() is used for INSERT, UPDATE, DELETE
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.");
                new AuthGUI().setVisible(true); // Take them back to login
                dispose(); // Close signup window
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error! Username might already exist.");
        }
    }
}