package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum CompanyDAO {
    instance;
    private List<Company> companies = new ArrayList<>();
    private CompanyDAO() {

    }
    public List<Company> getAllCompany() {
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
                company.setEmail(resultSet.getString("email"));
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
    }

    public List<Company> getCompanyByStudent(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT c.* " +
                            "FROM company c, employment e, student s " +
                            "WHERE c.cid = e.cid AND e.sid = s.sid AND u.id = s.sid AND c.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Company> selectedCompanies = new ArrayList<>();
            while (resultSet.next()) {
                Company company = new Company();
                company.setName(resultSet.getString("name"));
                company.setLocation(resultSet.getString("location"));
                company.setField(resultSet.getString("field"));
                company.setContact(resultSet.getString("contact"));
                company.setKvk_num(resultSet.getString("kvk_number"));
                company.setEmail(resultSet.getString("email"));
                selectedCompanies.add(company);
            }
            return selectedCompanies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Company getCompany(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT * " +
                            "FROM company " +
                            "WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            Company company = new Company();
            while (resultSet.next()) {
                company.setName(resultSet.getString("name"));
                company.setLocation(resultSet.getString("location"));
                company.setField(resultSet.getString("field"));
                company.setContact(resultSet.getString("contact"));
                company.setKvk_num(resultSet.getString("kvk_number"));
                company.setEmail(resultSet.getString("email"));
            }
            return company;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
