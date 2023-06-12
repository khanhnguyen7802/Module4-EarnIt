package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum UserDAO {
    instance;
    private List<Student> studentList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();
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

    public List<Student> getStudentList() {
        try {
            Connection connection = DBConnection.createConnection();

            String query = "SELECT * FROM student";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Student student = new Student();
                student.setBirth(resultSet.getString("birthdate"));
                student.setName(resultSet.getString("name"));
                student.setSkills(resultSet.getString("skills"));
                student.setStudy(resultSet.getString("study"));
                student.setUniversity(resultSet.getString("university"));
                student.setBtw_num(resultSet.getString("btw_number"));
                student.setBirth(resultSet.getString("email"));

                studentList.add(student);

            }

            return studentList;

        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }

    }

    public List<Company> getCompanyList() {
        try {
            Connection connection = DBConnection.createConnection();

            String query = "SELECT * FROM company";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Company company = new Company();
                company.setName(resultSet.getString("name"));
                company.setLocation(resultSet.getString("location"));
                company.setField(resultSet.getString("field"));
                company.setContact(resultSet.getString("contact"));
                company.setKvk_num(resultSet.getString("kvk_number"));
                companyList.add(company);
            }

            return companyList;

        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
    }

    public List<Company> getCompanyListByEmail(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT c.*" +
                    "FROM Company c, Employment e, Student s" +
                    "WHERE c.cid = e.cid AND e.sid = s.sid AND u.id = s.sid AND c.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Company> companies = new ArrayList<>();

            while (resultSet.next()) {
                Company company = new Company();
                company.setName(resultSet.getString("name"));
                company.setLocation(resultSet.getString("location"));
                company.setField(resultSet.getString("field"));
                company.setContact(resultSet.getString("contact"));
                company.setKvk_num(resultSet.getString("kvk_number"));
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getStudentListByEmail(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.*" +
                            "FROM Company c, Employment e, Student s" +
                            "WHERE c.cid = e.cid AND e.sid = s.sid AND u.id = s.sid AND s.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> students = new ArrayList<>();

            while(resultSet.next()) {
                Student student = new Student();
                student.setBirth(resultSet.getString("birthdate"));
                student.setName(resultSet.getString("name"));
                student.setSkills(resultSet.getString("skills"));
                student.setStudy(resultSet.getString("study"));
                student.setUniversity(resultSet.getString("university"));
                student.setBtw_num(resultSet.getString("btw_number"));
                student.setBirth(resultSet.getString("email"));

                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
