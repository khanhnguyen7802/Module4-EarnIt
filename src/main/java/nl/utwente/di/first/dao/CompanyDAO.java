package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum CompanyDAO {
    instance;
    CompanyDAO() {

    }

    /**
     * Get a list of all companies
     *
     * @return a list of all companies
     */
    public List<Company> getAllCompany() {
        try (Connection connection = DBConnection.createConnection();) {
            
            String query = "SELECT * " +
                           "FROM company";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return getQuery(resultSet);
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
        
        //TODO replace the calls to this function to a function that gets all employments instead of all companies.
        try (Connection connection = DBConnection.createConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT c.* " +
                            "FROM company c, employment e, student s " +
                            "WHERE c.id = e.cid AND e.sid = s.id AND s.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Company> getQuery(ResultSet resultSet) throws SQLException {
        List<Company> companies = new ArrayList<>();
        while (resultSet.next()) {
            Company company = new Company();
            company.setCid(resultSet.getInt("id"));
            company.setName(resultSet.getString("name"));
            company.setLocation(resultSet.getString("location"));
            company.setField(resultSet.getString("field"));
            company.setContact(resultSet.getString("contact"));
            company.setKvk_num(resultSet.getString("kvk_number"));
            company.setEmail(resultSet.getString("email"));
            company.setLogo(resultSet.getBytes("logo"));
            companies.add(company);
        }
        return companies;
    }

    /**
     * Given a company's email, return the information of that company
     *
     * @param email of a company
     * @return information of that company
     */
    public Company getCompany(String email) {
        try (Connection connection = DBConnection.createConnection();) {
            
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM company " +
                            "WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Company> results = getQuery(resultSet);
            return (results.isEmpty()) ? null : results.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateCompany(String email, Company company) {

        try (Connection connection = DBConnection.createConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE company " +
                            "SET email = ?, password = ?, salt = ?, name = ?, location = ?, field = ?, contact = ?, kvk_number = ? " +
                            "WHERE email = ?"
            );
            byte[] byteSalt = Security.getSalt();
            String salt = Security.toHex(byteSalt);
            String securePassword = Security.saltSHA256(company.getPassword(), byteSalt);

            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, securePassword);
            preparedStatement.setString(3, salt);
            preparedStatement.setString(4, company.getName());
            preparedStatement.setString(5, company.getLocation());
            preparedStatement.setString(6, company.getField());
            preparedStatement.setString(7, company.getContact());
            preparedStatement.setString(8, company.getKvk_num());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
