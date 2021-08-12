package server.auth;

import java.sql.*;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:E:\\java\\ChatJavaFXMaven\\chat.db";
    private Connection connection;

    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
    }


    public String getUsernameByLoginAndPassword(String login, String password) {
        String username = null;
        String request = "SELECT username FROM users WHERE login = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
        return username;
    }

    public void changeUsername(String newUsername, String login, String password) {
        String request = "UPDATE users SET username = ? WHERE login = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
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
