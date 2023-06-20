package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Employment;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum EmploymentDAO {
    instance;

    private EmploymentDAO() {

    }

    public List<Employment> getAllEmployments() {
        List<Employment> employments = new ArrayList<>();

        try {
            Connection connection = DBConnection.createConnection();

            String query = "SELECT s.name AS student_name, c.name AS company_name, job_title, job_description, salaryPerHour " +
                            "FROM student s, company c, employment e " +
                            "WHERE s.id = e.sid AND c.id = e.cid";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Employment employment = new Employment();
                employment.setStudentName(resultSet.getString("student_name"));
                employment.setCompanyName(resultSet.getString("company_name"));
                employment.setJob_title(resultSet.getString("job_title"));
                employment.setJob_description(resultSet.getString("job_description"));
                employment.setSalaryPerHour(resultSet.getDouble("salaryPerHour"));

                employments.add(employment);
            }

            return employments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
