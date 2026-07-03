import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthGUI extends JFrame {

    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRole;
    private JButton btnLogin, btnSignup;

    public AuthGUI() {
        // Set up the main window (JFrame)
        setTitle("FMS - Sign In");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Using absolute positioning as per Lecture 8
        setLocationRelativeTo(null); // Centers the window on your screen

        // Title Label
        JLabel lblTitle = new JLabel("Faculty Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(70, 20, 300, 30);
        add(lblTitle);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 70, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 70, 180, 25);
        add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 110, 180, 25);
        add(txtPassword);

        // Role Dropdown
        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(50, 150, 100, 25);
        add(lblRole);

        String[] roles = {"Student", "Lecturer", "Admin"};
        comboRole = new JComboBox<>(roles);
        comboRole.setBounds(150, 150, 180, 25);
        add(comboRole);

        // Login Button
        btnLogin = new JButton("Sign In");
        btnLogin.setBounds(150, 200, 85, 30);
        add(btnLogin);

        // Sign Up Button
        btnSignup = new JButton("Sign Up");
        btnSignup.setBounds(245, 200, 85, 30);
        add(btnSignup);

        // --- EVENT LISTENERS (The Logic) ---

        // What happens when you click "Sign In"
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        // What happens when you click "Sign Up"
        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupGUI().setVisible(true); // Opens the Sign Up window
                dispose(); // Closes the current Login window
            }
        });
    }

    // Method to check credentials against the database
    private void loginUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String role = comboRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            // SQL Query to check if the user exists
            String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If rs.next() is true, a match was found in the database!
                JOptionPane.showMessageDialog(this, "Login Successful!");
                if (role.equals("Student")) {
                    new StudentDashboardGUI(username).setVisible(true); // Pass the variable here!
                } else if (role.equals("Admin")) {
                    // new AdminDashboardGUI().setVisible(true); // We will build this later
                    JOptionPane.showMessageDialog(this, "Admin Dashboard coming soon!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lecturer Dashboard coming soon!");
                }
                dispose();
                // Here we will later open the Student or Admin Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Role", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }
}