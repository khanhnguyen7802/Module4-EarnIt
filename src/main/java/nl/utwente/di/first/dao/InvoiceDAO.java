package nl.utwente.di.first.dao;


import nl.utwente.di.first.model.Invoice;
import nl.utwente.di.first.model.Submission;
import nl.utwente.di.first.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
            invoice.setDate_of_issue(resultSet.getString("date_of_issue"));

            invoices.add(invoice);
        }
        return invoices;
    }

    public boolean addInvoice(Invoice invoice) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO invoice(eid, week, total_salary, date_of_issue) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, invoice.getEid());
            statement.setInt(2, invoice.getWeek());
            statement.setDouble(3, invoice.getTotal_salary());
            statement.setString(4, invoice.getDate_of_issue());
            statement.execute();
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
     * @param companyEmail
     * @param week
     * @return the corresponding invoice
     */
    public List<Invoice> getStudentInvoice(String studentEmail, String companyEmail, int week) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT i.iid, week, total_salary, date_of_issue, c.name, s.name, e.job_title " +
                            "FROM invoice i, company c, employment e, student s " +
                            "WHERE i.eid = e.eid AND e.cid = c.id AND s.id = e.sid" +
                            "AND s.email = ? AND c.email = ? AND week = ? "
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.setString(2, companyEmail);
            preparedStatement.setInt(3, week);

            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
