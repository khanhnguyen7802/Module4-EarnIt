package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
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

            String query = "SELECT * FROM employment";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Employment employment = new Employment();

                // TODO: Get the student name
                PreparedStatement studentPreparedStatement =
                        connection.prepareStatement("SELECT name FROM student WHERE id = ?");
                studentPreparedStatement.setInt(1, resultSet.getInt("sid"));
                ResultSet studentResultSet = studentPreparedStatement.executeQuery();

                if (studentResultSet.next())
                    employment.setStudentName(studentResultSet.getString("name"));

                // TODO: Get the company name
                PreparedStatement companyPreparedStatement =
                        connection.prepareStatement("SELECT name FROM company WHERE id = ?");
                companyPreparedStatement.setInt(1, resultSet.getInt("cid"));
                ResultSet companyResultSet = companyPreparedStatement.executeQuery();

                if (companyResultSet.next())
                    employment.setCompanyName(companyResultSet.getString("name"));


                // TODO: Get the job title and job description
                employment.setJob_title(resultSet.getString("job_title"));
                employment.setJob_description(resultSet.getString("job_description"));

                employments.add(employment);
            }

            return employments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
