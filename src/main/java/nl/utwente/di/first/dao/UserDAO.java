package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public enum UserDAO {
    instance;
    private UserDAO() {

    }

    public String loginUser(User user) throws SQLException {
        String email = user.getEmail(); // user input
        String originalPassword = user.getPassword(); // user input

        try {
            Connection connection = DBConnection.createConnection();
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

                        if (role.equals("student")) {
                            return "STUDENT";
                        } else if (role.equals("company")) {
                            return "COMPANY";
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

    public boolean registerStudent(Student student) {
        try {
            Connection connection = DBConnection.createConnection();
            String finalHashedPassword = "";
            byte[] salt = Security.getSalt();
            String stringSalt = Security.toHex(salt); // this one to save into database
            String passwordToHash = student.getPassword();

            finalHashedPassword = Security.saltSHA256(passwordToHash, salt);

            String query = "INSERT INTO student(email, password, salt, name, birthdate, university, study, skills) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStudentStatement = connection.prepareStatement(query);
            insertStudentStatement.setString(1, student.getEmail());
            insertStudentStatement.setString(2, finalHashedPassword);
            insertStudentStatement.setString(3, stringSalt);
            insertStudentStatement.setString(4, student.getName());
            insertStudentStatement.setString(5, student.getBirth());
            insertStudentStatement.setString(6, student.getUniversity());
            insertStudentStatement.setString(7, student.getStudy());
            insertStudentStatement.setString(8, student.getSkills());

            insertStudentStatement.execute();


            if (insertStudentStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean registerCompany(Company company) {
        try {
            Connection connection = DBConnection.createConnection();
            String finalHashedPassword = "";
            byte[] salt = Security.getSalt();
            String stringSalt = Security.toHex(salt); // this one to save into database
            String passwordToHash = company.getPassword();

            finalHashedPassword = Security.saltSHA256(passwordToHash, salt);

            String query = "INSERT INTO company(email, password, salt, name, location, field, contact) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertCompanyStatement = connection.prepareStatement(query);
            insertCompanyStatement.setString(1, company.getEmail());
            insertCompanyStatement.setString(2, finalHashedPassword);
            insertCompanyStatement.setString(3, stringSalt);
            insertCompanyStatement.setString(4, company.getName());
            insertCompanyStatement.setString(5, company.getLocation());
            insertCompanyStatement.setString(6, company.getField());
            insertCompanyStatement.setString(7, company.getContact());
            insertCompanyStatement.execute();

            if (insertCompanyStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
        return false;
    }
}
