
import javax.swing.*;
import java.awt.*;

public class StudentDashboardGUI extends JFrame {
    private String loggedInUsername;

    public StudentDashboardGUI(String username) {
        this.loggedInUsername = username;
        setTitle("FMS - Student Dashboard");
        setSize(750, 550); // Made it slightly larger to fit everything nicely
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use BorderLayout for the main window so we can separate the Header and the Tabs
        setLayout(new BorderLayout());

        // --- 1. CREATE THE HEADER PANEL (Top) ---
        JPanel headerPanel = new JPanel();
        // FlowLayout.RIGHT pushes all the buttons to the right side of the screen
        headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        // Profile Label (Using a simple Unicode icon for the profile)
        JLabel lblProfile = new JLabel("Logged in as:"+ this.loggedInUsername);
        lblProfile.setFont(new Font("Arial", Font.BOLD, 14));

        // Sign Out Button
        JButton btnSignOut = new JButton("Sign Out");
        btnSignOut.setFocusPainted(false); // Makes the button look a bit cleaner

        // Add Action to Sign Out Button
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Ask for confirmation
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to sign out?", "Sign Out", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    new AuthGUI().setVisible(true); // Open Login Window
                    dispose(); // Close Dashboard Window
                }
            }
        });

        // Add the label and button to the header panel
        headerPanel.add(lblProfile);
        headerPanel.add(btnSignOut);

        // Add the Header Panel to the TOP (NORTH) of the main window
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CREATE THE TABBED PANE (Center) ---
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Profile Tab
        JPanel profilePanel = createProfilePanel();
        tabbedPane.addTab("My Profile", profilePanel);

        // 2. Course Enrollment Tab
        JPanel enrollmentPanel = createEnrollmentPanel();
        tabbedPane.addTab("Course Enrollment", enrollmentPanel);

        // 3. Timetable Tab
        JPanel timetablePanel = createTimetablePanel();
        tabbedPane.addTab("My Timetable", timetablePanel);

        // Add the tabbed pane to the CENTER of the main window
        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- PANEL GENERATION METHODS ---

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblHeader = new JLabel("Student Profile Details");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBounds(50, 30, 300, 30);
        panel.add(lblHeader);

        // Labels to hold the data
        JLabel lblName = new JLabel("Full Name: Loading...");
        lblName.setBounds(50, 80, 400, 25);
        panel.add(lblName);

        JLabel lblBatch = new JLabel("Batch: Loading...");
        lblBatch.setBounds(50, 120, 400, 25);
        panel.add(lblBatch);

        // Database Fetching Logic
        try {
            java.sql.Connection conn = DatabaseConnection.getConnection();
            // We join users and students tables to get the profile of the logged-in user
            String sql = "SELECT s.full_name, s.batch FROM students s JOIN users u ON s.student_id = u.user_id WHERE u.username = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.loggedInUsername);

            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lblName.setText("Full Name: " + rs.getString("full_name"));
                lblBatch.setText("Batch: " + rs.getString("batch"));
            } else {
                lblName.setText("Full Name: Not set up yet. Please contact Admin.");
                lblBatch.setText("Batch: N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblName.setText("Error loading profile data.");
        }

        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblHeader = new JLabel("Available Courses");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBounds(50, 30, 300, 30);
        panel.add(lblHeader);

        // We will add checkboxes here dynamically later
        return panel;
    }

    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Better layout for tables

        JLabel lblHeader = new JLabel("Registered Courses & Timetable", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblHeader, BorderLayout.NORTH);

        // Dummy data for the JTable (Lecture 8 style)
        String[] columns = {"Course Code", "Course Name", "Credits"};
        String[][] data = {
                {"Waiting...", "Enroll in courses to see them here", "0"}
        };

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}