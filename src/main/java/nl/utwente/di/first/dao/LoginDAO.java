package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.sql.*;

public enum LoginDAO {
    instance;
    LoginDAO() {

    }
    public String loginUser(User user) throws SQLException {
        String email = user.getEmail(); // user input
        String originalPassword = user.getPassword(); // user input
        try (Connection connection = DBConnection.createConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                    "FROM \"user\" " +
                    "WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                // TODO: Authentication
                String emailDB = resultSet.getString("email"); // email in db
                String passwordDB = resultSet.getString("password"); // hashed password in db
                String salt = resultSet.getString("salt");
                String hashedFinalPassword = Security.saltSHA256(originalPassword, Security.toByteArray(salt));
                if (email.equals(emailDB) && hashedFinalPassword.equals(passwordDB)) {
                    // TODO: Return the actual role of user (Authorization)
                    String query = "SELECT p.relname AS role " +
                            "FROM \"user\" u, pg_class p " +
                            "WHERE email = ? AND u.tableoid = p.oid";
                    PreparedStatement studentStatement = connection.prepareStatement(query);
                    studentStatement.setString(1, email);
                    ResultSet returnResultSet = studentStatement.executeQuery(); // cannot use executeUpdate
                    if (returnResultSet.next()) {
                        String role = returnResultSet.getString("role");
                        System.out.println(role);
                        switch (role) {
                            case "student":
                                return "STUDENT";
                            case "company":
                                return "COMPANY";
                            case "admin":
                                return "ADMIN";
                        }
                    }
                }
            }
        } catch(SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
        return "INVALID";
    }
}
