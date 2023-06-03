package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDAO {
    public UserDAO() {

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
                String emailDB = resultSet.getString("email"); // email in db
                String passwordDB = resultSet.getString("password"); // hashed password in db
                String salt = resultSet.getString("salt");

                String hashedFinalPassword = Security.saltSHA256(originalPassword, Security.toByteArray(salt));

                if (email.equals(emailDB) && hashedFinalPassword.equals(passwordDB)) {
                    // TODO: Query from table "student"
                    String queryStudent = "SELECT email " +
                            "FROM student " +
                            "WHERE email = ?";
                    PreparedStatement studentStatement = connection.prepareStatement(queryStudent);
                    studentStatement.setString(1, email);
                    ResultSet returnResultSet = studentStatement.executeQuery(); // cannot use executeUpdate

                    String emailStudentQuery = "";

                    if (returnResultSet.next()) {
                        emailStudentQuery = returnResultSet.getString("email");
                    }

                    // TODO: Query from table "company"
                    String queryCompany = "SELECT email " +
                            "FROM company " +
                            "WHERE email = ?";
                    PreparedStatement companyStatement = connection.prepareStatement(queryCompany);
                    companyStatement.setString(1, email);
                    returnResultSet = companyStatement.executeQuery(); // cannot use executeUpdate

                    String emailCompanyQuery = "";

                    if (returnResultSet.next()) {
                        emailCompanyQuery = returnResultSet.getString("email");
                    }

                    if (emailStudentQuery.equals(emailDB)) {
                        return "STUDENT";
                    } else if (emailCompanyQuery.equals(emailDB)) {
                        return "COMPANY";
                    }
                }
            }

        } catch(SQLException e){
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

            String insertUserQuery = "INSERT INTO \"user\"(email, password, salt) VALUES (?, ?, ?)";

            PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery);
            insertUserStatement.setString(1, student.getEmail());
            insertUserStatement.setString(2, finalHashedPassword);
            insertUserStatement.setString(3, stringSalt);
            insertUserStatement.execute();


            String insertStudentQuery = "INSERT INTO student(name, university, birthdate, study, skills, btw_num) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStudentStatement = connection.prepareStatement(insertStudentQuery);
            insertStudentStatement.setString(1, student.getName());
            insertStudentStatement.setString(2, student.getUniversity());
            insertStudentStatement.setString(3, student.getBirth());
            insertStudentStatement.setString(4, student.getStudy());
            insertStudentStatement.setString(5, student.getSkills());
            insertStudentStatement.setString(6, student.getBtw_num());

            if (insertStudentStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
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

            String insertUserQuery = "INSERT INTO \"user\"(email, password, salt) VALUES (?, ?, ?)";

            PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery);
            insertUserStatement.setString(1, company.getEmail());
            insertUserStatement.setString(2, finalHashedPassword);
            insertUserStatement.setString(3, stringSalt);
            insertUserStatement.execute();

            String insertCompanyQuery = "INSERT INTO company(name, location, field, contact, kvk_num) values (?, ?, ?, ?, ?)";
            PreparedStatement insertCompanyStatement = connection.prepareStatement(insertCompanyQuery);
            insertCompanyStatement.setString(1, company.getName());
            insertCompanyStatement.setString(2, company.getLocation());
            insertCompanyStatement.setString(3, company.getField());
            insertCompanyStatement.setString(4, company.getContact());
            insertCompanyStatement.setString(5, company.getKvk_num());
            if (insertCompanyStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
