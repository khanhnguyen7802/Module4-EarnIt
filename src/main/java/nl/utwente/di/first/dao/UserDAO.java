package nl.utwente.di.first.dao;

import jakarta.xml.bind.annotation.XmlRootElement;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

@XmlRootElement
public enum UserDAO {
    instance;
    private ArrayList studentList = new ArrayList();
    private ArrayList companyList = new ArrayList();
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

    public ArrayList getStudentList() {
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

    public ArrayList getCompanyList() {
        try {
            Connection connection = DBConnection.createConnection();

            String query = "SELECT * FROM company";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
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
}
