package nl.utwente.di.first.dao;

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
    public List<Student> getAllStudent() {
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
                student.setEmail(resultSet.getString("email"));
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }

    }

    public List<Student> getStudentByCompany(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM company c, employment e, student s " +
                            "WHERE c.cid = e.cid AND e.sid = s.sid AND u.id = s.sid AND s.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> sFilter = new ArrayList<>();

            while(resultSet.next()) {
                Student student = new Student();
                student.setBirth(resultSet.getString("birthdate"));
                student.setName(resultSet.getString("name"));
                student.setSkills(resultSet.getString("skills"));
                student.setStudy(resultSet.getString("study"));
                student.setUniversity(resultSet.getString("university"));
                student.setBtw_num(resultSet.getString("btw_number"));
                student.setEmail(resultSet.getString("email"));
                sFilter.add(student);
            }
            return sFilter;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Student getStudent(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT * " +
                            "FROM student " +
                            "WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            Student student = new Student();
            student.setBirth(resultSet.getString("birthdate"));
            student.setName(resultSet.getString("name"));
            student.setSkills(resultSet.getString("skills"));
            student.setStudy(resultSet.getString("study"));
            student.setUniversity(resultSet.getString("university"));
            student.setBtw_num(resultSet.getString("btw_number"));
            student.setEmail(resultSet.getString("email"));
            return student;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
