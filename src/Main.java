import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Attempting to connect to the database.");

        // Call our utility method
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("Success! Connected to fms_db.");
        } else {
            System.out.println("Failed to connect. Check XAMPP and your code.");
        }


        // Create an instance of our AuthGUI and make it visible
        AuthGUI loginScreen = new AuthGUI();
        loginScreen.setVisible(true);
    }
}