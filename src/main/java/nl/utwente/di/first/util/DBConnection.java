package nl.utwente.di.first.util;

import java.sql.*;

public class DBConnection {
    public static String DB_HOST = "localhost";
    public static String DB_NAME = "earnit";
    public static String DB_URL = "jdbc:postgresql://" + DB_HOST + ":5432/" + DB_NAME;

    public static Connection createConnection() {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, "postgres", "root");
        } catch (SQLException e) {
            System.err.println("Error connecting: " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);
        }

        return connection;
    }

    // for testing only
    public static void main(String[] args) {
        try (Connection connection = DBConnection.createConnection();) {

            String query = "SELECT * " +
                    "FROM company";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("ok");
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
    }



}
