package server.auth;

import java.sql.*;

public class DatabaseService {
    public static final String GET_USERNAME_REQUEST = "SELECT username FROM users WHERE login = ? AND password = ?";
    private static final String DB_URL = "jdbc:sqlite:E:\\java\\ChatJavaFXMaven\\chat.db";
    public static final String CHANGE_USERNAME_REQUEST = "UPDATE users SET username = ? WHERE login = ? AND password = ?";
    private Connection connection;
    private PreparedStatement getUsernameStatement;
    private PreparedStatement changeUsernameStatement;

    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            getUsernameStatement = connection.prepareStatement(GET_USERNAME_REQUEST);
            changeUsernameStatement = connection.prepareStatement(CHANGE_USERNAME_REQUEST);
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
    }

    public String getUsernameByLoginAndPassword(String login, String password) {
        String username = null;
        try {
            getUsernameStatement.setString(1, login);
            getUsernameStatement.setString(2, password);
            ResultSet resultSet = getUsernameStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
        return username;
    }

    public void changeUsername(String newUsername, String login, String password) {
        try {
            changeUsernameStatement.setString(1, newUsername);
            changeUsernameStatement.setString(2, login);
            changeUsernameStatement.setString(3, password);
            changeUsernameStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection with DB closed.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close database connection");
        }
    }
}
