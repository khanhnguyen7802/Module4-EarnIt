package nl.utwente.di.first.dao;


import nl.utwente.di.first.model.Invoice;
import nl.utwente.di.first.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum InvoiceDAO {
    instance;

    private InvoiceDAO() {

    }

    private List<Invoice> getQuery(ResultSet resultSet) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();

        while (resultSet.next()) {
            Invoice invoice = new Invoice();
            invoice.setEid(resultSet.getInt("eid"));
            invoice.setWeek(resultSet.getInt("week"));
            invoice.setTotal_salary(resultSet.getInt("total_salary"));
            invoice.setDate_of_issue(resultSet.getDate("date_of_issue"));
            invoice.setCompany_address(resultSet.getString("company_address"));
            invoice.setCompany_name(resultSet.getString("company_name"));
            invoice.setIid(resultSet.getInt("invoice_number"));
            invoice.setKvk_number(resultSet.getString("kvk_number"));
            invoice.setStudent_name(resultSet.getString("student_name"));
            invoice.setBtw_number(resultSet.getString("btw_number"));
            invoice.setWeek_number(resultSet.getInt("week_number"));
            invoice.setJob_title(resultSet.getString("job_title"));
            invoice.setLogo(resultSet.getBytes("logo"));

            invoices.add(invoice);
        }
        return invoices;
    }

    public boolean addInvoice(Invoice invoice) {
        try (Connection connection = DBConnection.createConnection();) {
            
            String query = "INSERT INTO invoice(eid, week, total_salary, date_of_issue) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, invoice.getEid());
            statement.setInt(2, invoice.getWeek());
            statement.setDouble(3, invoice.getTotal_salary());
            statement.setDate(4, invoice.getDate_of_issue());

            if (statement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Given student, company and week, return the corresponding invoice
     * @param studentEmail
     * @param week
     * @return the corresponding invoice
     */
    public List<Invoice> getWeeklyInvoices(String studentEmail, int week, int year) {
        try (Connection connection = DBConnection.createConnection();) {
            
            // this query will return all invoices for a student within the time range
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT i.iid AS invoice_number, i.week AS week_ number, " +
                            "c.name AS company_name, s.name AS student_name, c.logo AS logo " +
                            "e.job_title, total_salary, date_of_issue, c.kvk_number, s.btw_number " +
                            "FROM invoice i, company c, employment e, student s " +
                            "WHERE i.eid = e.eid AND e.cid = c.id AND s.id = e.sid " +
                            "AND s.email = ? AND week = ? AND year = ? "
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.setInt(2, week);
            preparedStatement.setInt(3, year);

            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
