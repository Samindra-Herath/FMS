import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database credentials based on XAMPP defaults from your lab guide
    private static final String URL = "jdbc:mysql://localhost:3306/fms_db?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // This method returns the active connection to the database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Attempting to connect to the database
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
        return connection;
    }
}