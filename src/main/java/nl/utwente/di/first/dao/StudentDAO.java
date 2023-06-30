package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum StudentDAO {
    instance;
    StudentDAO() {

    }
    public List<Student> getAllStudents() {
        try (Connection connection = DBConnection.createConnection();) {
            
            String query = "SELECT * FROM student";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return getQuery(resultSet);
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }

    }

    /**
     * Return a list of students employed by a given company
     *
     * @param email of a company
     * @return list of students employed by that company
     */
    public List<Student> getStudentByCompany(String email) {
        try (Connection connection = DBConnection.createConnection()) {
            
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM company c, employment e, student s " +
                            "WHERE c.id = e.cid AND e.sid = s.id AND c.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return information of a give student
     *
     * @param email of a student
     * @return information of that student
     */
    public Student getStudent(String email) {
        try (Connection connection = DBConnection.createConnection()) {
            
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM student " +
                            "WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Student> results = getQuery(resultSet);
            return (results.isEmpty()) ? null : results.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStudent(String email, Student student) {

        try (Connection connection = DBConnection.createConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE student " +
                            "SET email = ?, password = ?, salt = ?, name = ?, university = ?, study = ?, skills = ?, btw_number = ? " +
                            "WHERE email = ?"
            );
            byte[] byteSalt = Security.getSalt();
            String salt = Security.toHex(byteSalt);
            String securePassword = Security.saltSHA256(student.getPassword(), byteSalt);

            preparedStatement.setString(1, student.getEmail());
            preparedStatement.setString(2, securePassword);
            preparedStatement.setString(3, salt);
            preparedStatement.setString(4, student.getName());
//            preparedStatement.setDate(5, Date.valueOf(student.getBirth()));
            preparedStatement.setString(5, student.getUniversity());
            preparedStatement.setString(6, student.getStudy());
            preparedStatement.setString(7, student.getSkills());
            preparedStatement.setString(8, student.getBtw_num());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Student> getQuery(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setBirth(resultSet.getString("birthdate"));
            student.setName(resultSet.getString("name"));
            student.setSkills(resultSet.getString("skills"));
            student.setStudy(resultSet.getString("study"));
            student.setUniversity(resultSet.getString("university"));
            student.setBtw_num(resultSet.getString("btw_number"));
            student.setEmail(resultSet.getString("email"));
            students.add(student);
        }
        return students;
    }
}
