package server.auth;

import java.sql.*;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:E:\\java\\ChatJavaFXMaven\\chat.db";
    private Connection connection;
    private Statement statement;

    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
    }


    public String getUsernameByLoginAndPassword(String login, String password) {
        String username = null;
        String request = String.format("SELECT username FROM users WHERE login = '%s' AND password = '%s'", login, password);
        try {
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Failed to database connection");
        }
        return username;
    }

    public void changeUsername(String newUsername, String login, String password) {
        String request = String.format("UPDATE users SET username = '%s' WHERE login = '%s' AND password = '%s'", newUsername, login, password);
        try {
            statement.executeUpdate(request);
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
