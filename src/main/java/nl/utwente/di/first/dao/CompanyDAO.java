package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum CompanyDAO {
    instance;
    private CompanyDAO() {

    }

    /**
     * Get a list of all companies
     *
     * @return a list of all companies
     */
    public List<Company> getAllCompany() {
        try {
            List<Company> companies = new ArrayList<>();
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
                company.setJob_title(resultSet.getString("job_title"));
                company.setJob_description(resultSet.getString("job_description"));

                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a student's email, return all companies that student work for
     *
     * @param email of a student
     * @return a list of all companies that student is currently working for
     */
    public List<Company> getCompanyByStudent(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT c.* " +
                            "FROM company c, employment e, student s " +
                            "WHERE c.id = e.cid AND e.sid = s.id AND s.email = ?"
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
                company.setJob_title(resultSet.getString("job_title"));
                company.setJob_description(resultSet.getString("job_description"));
                selectedCompanies.add(company);
            }
            return selectedCompanies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a company's email, return the information of that company
     *
     * @param email of a company
     * @return information of that company
     */
    public Company getCompany(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
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
                company.setJob_title(resultSet.getString("job_title"));
                company.setJob_description(resultSet.getString("job_description"));
            }
            return company;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
