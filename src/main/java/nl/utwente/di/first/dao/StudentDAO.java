package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum StudentDAO {
    instance;
    private List<Student> students = new ArrayList<>();
    private StudentDAO() {

    }
    public List<Student> getAllStudents() {
        try {
            Connection connection = DBConnection.createConnection();
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
        try {
            Connection connection = DBConnection.createConnection();
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
        try {
            Connection connection = DBConnection.createConnection();
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

    private List<Student> getQuery(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student();
            student.setBirth(resultSet.getString("birthdate"));
            student.setName(resultSet.getString("name"));
            student.setSkills(resultSet.getString("skills"));
            student.setStudy(resultSet.getString("study"));
            student.setUniversity(resultSet.getString("university"));
            student.setBtw_num(resultSet.getString("btw_number"));
            student.setEmail(resultSet.getString("email"));
        }
        return students;
    }
}
