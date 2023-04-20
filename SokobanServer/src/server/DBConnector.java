package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnector {

    private static Connection conn;
    private static final String URL = "jdbc:derby://localhost:1527/sokoban";

    public static void initialise() {
        if (conn == null) {  // Only create a new connection if it doesn't exist (singleton-based approach)
            try {
                conn = DriverManager.getConnection(URL, "ridu", "sokoban123");
                System.out.println("Connected to the database.");
            } catch (SQLException e) {
                System.out.println("Could not connect to the database; an error occurred: " + e.getMessage());
                conn = null;
            }
        }
    }

    public static boolean addUser(String username, String email, String password) // For registration
    {
        if (conn != null) {
            try {
                String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.executeUpdate();
                return true;
            } catch (SQLException addUserE) {
                System.out.println("Could not add user: " + addUserE.getMessage());
            }
        }
        return false;
    }

    public static boolean fetchUser(String username, String password) // For login
    {
        if (conn != null) {
            try {
                String sql = "SELECT id FROM users WHERE USERNAME=? AND PASSWORD=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet user = statement.executeQuery();
//                System.out.println("HERE" + user.next());
                return (user.next());
            } catch (SQLException fetchUserE) {
                System.out.println("Could not fetch user: " + fetchUserE.getMessage());
            }
        }
        return false;
    }

    //  For registration validation
    public static boolean fetchUserByUsername(String username) {
        if (conn != null) {
            try {
                String sql = "SELECT id FROM users WHERE USERNAME=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, username);
                ResultSet user = statement.executeQuery();
                return (user.next());
            } catch (SQLException fetchUserByUsernE) {
                System.out.println("Could not fetch user by username: " + fetchUserByUsernE.getMessage());
            }
        }
        return false;
    }

    public static boolean fetchUserByEmail(String email) {
        if (conn != null) {
            try {
                String sql = "SELECT id FROM users WHERE EMAIL=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, email);
                ResultSet user = statement.executeQuery();
                return (user.next());
            } catch (SQLException fetchUserbyEmailE) {
                System.out.println("Could not fetch user by email: " + fetchUserbyEmailE.getMessage());
            }
        }
        return false;
    }
}