package nl.utwente.di.first.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static String DB_HOST = "bronto.ewi.utwente.nl";
    public static String DB_NAME = "dab_di22232b_116";
    public static String DB_URL = "jdbc:postgresql://" + DB_HOST + ":5432/" + DB_NAME + "?currentSchema=earnit";

    public static Connection createConnection() {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, "dab_di22232b_116", "FsrnTKGS3P+pC/a+");
        } catch (SQLException e) {
            System.err.println("Error connecting: " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);
        }

        return connection;
    }



}
