package nl.utwente.di.first.dao;

import jakarta.ws.rs.Path;
import nl.utwente.di.first.model.Employment;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum EmploymentDAO {
    instance;

    EmploymentDAO() {

    }

    public List<Employment> getAllEmployments() {
        List<Employment> employments = new ArrayList<>();

        try {
            Connection connection = DBConnection.createConnection();

            String query = "SELECT e.eid, e.sid, e.cid, s.name AS student_name, c.name AS company_name, job_title, job_description, salary_per_hour, c.logo AS logo " +
                            "FROM student s, company c, employment e " +
                            "WHERE s.id = e.sid AND c.id = e.cid";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Employment employment = new Employment();
                employment.setEid(resultSet.getInt("eid"));
                employment.setCid(resultSet.getInt("cid"));
                employment.setSid(resultSet.getInt("sid"));
                employment.setStudent_name(resultSet.getString("student_name"));
                employment.setCompany_name(resultSet.getString("company_name"));
                employment.setJob_title(resultSet.getString("job_title"));
                employment.setJob_description(resultSet.getString("job_description"));
                employment.setSalary_per_hour(resultSet.getDouble("salary_per_hour"));
                employment.setLogo(resultSet.getBytes("logo"));

                employments.add(employment);
            }

            return employments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Employment> getAllVacancies() {
        List<Employment> result = new ArrayList<>();
        try {
            Connection connection = DBConnection.createConnection();
            String query = "SELECT e.eid, e.sid, e.cid, c.name AS company_name, job_title, job_description, salary_per_hour, c.logo AS logo " +
                           "FROM company c, employment e " +
                           "WHERE e.sid IS NULL AND c.id = e.cid";
    
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                Employment employment = new Employment();
                employment.setEid(resultSet.getInt("eid"));
                employment.setCid(resultSet.getInt("cid"));
                employment.setSid(resultSet.getInt("sid"));
                employment.setStudent_name(null);
                employment.setCompany_name(resultSet.getString("company_name"));
                employment.setJob_title(resultSet.getString("job_title"));
                employment.setJob_description(resultSet.getString("job_description"));
                employment.setSalary_per_hour(resultSet.getDouble("salary_per_hour"));
                employment.setLogo(resultSet.getBytes("logo"));
                
                result.add(employment);
                
            }
            
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean linkStudent(Employment employment) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "UPDATE employment " +
                           "SET sid = ? " +
                           "WHERE eid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employment.getSid());
            preparedStatement.setInt(2, employment.getEid());
            return preparedStatement.executeUpdate() == 1;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
