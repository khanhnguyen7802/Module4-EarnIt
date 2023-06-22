package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Submission;
import nl.utwente.di.first.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum SubmissionDAO {
    instance;
    private SubmissionDAO() {

    }

    /**
     * Given an email, return a list of submissions of that student
     * @param email of a specific student
     * @return a full list of all submissions that was made by that student
     */
    public List<Submission> getStudentSubmissions(String email, String date, String flag) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM Submission s, Student st, Company c " +
                            "WHERE st.id = s.sid AND c.id = s.cid AND st.email = ? AND s.worked_date LIKE ? AND s.status LIKE ?"
            );
            return getQuery(email, date, flag, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Submission> getCompanySubmissions(String email, String date, String flag) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM Submission s, Student st, Company c " +
                            "WHERE c.email = ? AND st.id = s.sid AND c.id = s.cid"
            );
            return getQuery(email, date, flag, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Submission> getQuery(String email, String date, String flag, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, date);
        preparedStatement.setString(3, flag);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Submission> submissions = new ArrayList<>();
        while (resultSet.next()) {
            Submission submission = new Submission();
            submission.setEmploymentId(resultSet.getString("eid"));
            submission.setComment(resultSet.getString("comment"));
            submission.setDate(resultSet.getString("worked_date"));
            submission.setStatus(resultSet.getString("status"));
            submission.setHours(resultSet.getInt("hours"));
            submissions.add(submission);
        }
        return submissions;
    }

    public boolean addSubmission(Submission submission) {
        //Added submission has empty flag on default
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO submission(eid, hours, worked_date, comment, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, submission.getEmploymentId());
            statement.setInt(2, submission.getHours());
            statement.setString(3, submission.getDate().toString());
            statement.setString(4, submission.getComment());
            statement.setString(5, submission.getStatus());
            statement.execute();
            if (statement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
        return false;
    }

    public void flagSubmission(String subId, String flag) {
        //Possible flags: <empty>, Confirmed, Accepted, Rejected, Appealed
        try {
            Connection connection = DBConnection.createConnection();
            String query = "UPDATE submission SET status = ? WHERE submission_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, flag);
            statement.setString(2, subId);
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
    }
}
