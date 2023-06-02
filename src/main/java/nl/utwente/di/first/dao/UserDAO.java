package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;

public class UserDAO {
    public UserDAO() {

    }

    public String loginUser(User user) throws SQLException {
        String email = user.getEmail(); // login data
        String password = user.getPassword(); // login data

        try {
            Connection connection = DBConnection.createConnection();
            String query = "SELECT email, password " +
                    "FROM \"user\" " +
                    "WHERE email = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String emailDB = resultSet.getString("email"); // data retrieved in database
                String passwordDB = resultSet.getString("password"); // data retrieved in database

                if (email.equals(emailDB) && password.equals(passwordDB)) {
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
            String query = "INSERT INTO \"user\"(email, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getEmail());
            preparedStatement.setString(2, student.getPassword());

            query = "INSERT INTO student(email, first, last, birth, university, study, lvStudy) values (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getEmail());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getName());
            preparedStatement.setString(4, student.getBirth());
            preparedStatement.setString(5, student.getUniversity());
            preparedStatement.setString(6, student.getStudy());
            preparedStatement.setString(7, student.getLvStudy());
            if (preparedStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean registerCompany(Company company) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO accounts(email, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, company.getPassword());

            query = "INSERT INTO company(email, name, location, field, contact) values (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, company.getName());
            preparedStatement.setString(3, company.getLocation());
            preparedStatement.setString(4, company.getField());
            preparedStatement.setString(5, company.getContact());
            if (preparedStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
