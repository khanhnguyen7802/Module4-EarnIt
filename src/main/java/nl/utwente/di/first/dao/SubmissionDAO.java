package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Submission;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public enum SubmissionDAO {
    instance;
    private SubmissionDAO() {

    }

    /**
     * Given an email, return a list of submissions of that student
     * @param email - the email of a specific student
     * @param date - the date of submission
     * @param flag - status of the submission
     * @return a full list of all submissions that was made by that student on a specific date
     */
    public List<Submission> getStudentDateSubmissions(String email, String date, String flag) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM Submission s, Student st, Employment e " +
                            "WHERE st.id = e.sid AND e.eid = s.eid " +
                            "AND st.email = ? AND s.worked_date = ? AND s.status LIKE ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, convertToSqlDate(date));
            preparedStatement.setString(3, flag);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    public List<Submission> getCompanyDateSubmissions(String email, String date, String flag) {
//        try {
//            Connection connection = DBConnection.createConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT DISTINCT s.* " +
//                            "FROM Submission s, Company c, Employment e " +
//                            "WHERE c.id = e.cid AND e.eid = s.eid " +
//                            "AND c.email = ? AND s.worked_date = ? AND s.status LIKE ?"
//            );
//            preparedStatement.setString(1, email);
//            preparedStatement.setDate(2, convertToSqlDate(date));
//            preparedStatement.setString(3, flag);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            return getQuery(resultSet);
//        } catch (SQLException | ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * Given an email, return a 1-week range list of submissions of that student
     * @param email - the email of a specific student
     * @param week - the specific week
     * @param flag - status of the submission
     * @return a full list of all submissions that was made by that student on a specific date
     */
    public List<Submission> getStudentWeekSubmissions(String email, int week, String flag) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM Submission s, Student st, Employment e " +
                            "WHERE st.id = e.sid AND e.eid = s.eid " +
                            "AND st.email = ? AND s.status LIKE ? " +
                            "AND DATE_PART('week', worked_date) = ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, flag);
            preparedStatement.setInt(3, week);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Submission> getCompanyWeekSubmissions(String email, String date, String flag) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT s.* " +
                            "FROM Submission s, Company c, Employment e " +
                            "WHERE c.id = e.cid AND e.eid = s.eid " +
                            "AND c.email = ? AND s.status LIKE ? " +
                            "AND worked_date >= DATEADD(wk, DATEDIFF(wk, 0, ?), 0) AND worked_date < DATEADD(wk, DATEDIFF(wk, 0, ?) + 1, 0)"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, flag);
            preparedStatement.setDate(3, convertToSqlDate(date));
            preparedStatement.setDate(4, convertToSqlDate(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Submission> getQuery(ResultSet resultSet) throws SQLException, ParseException {
        List<Submission> submissions = new ArrayList<>();
        while (resultSet.next()) {
            Submission submission = new Submission();
            submission.setEmploymentId(resultSet.getInt("eid"));
            submission.setComment(resultSet.getString("comment"));
            submission.setDate(resultSet.getDate("worked_date").toString());
            submission.setStatus(resultSet.getString("status"));
            submission.setHours(resultSet.getInt("hours"));
            submissions.add(submission);
        }
        return submissions;
    }

    /**
     * In the frontend, when the submission button is clicked,
     * then a new submission is added into database
     *
     * @param submission
     * @return true if successfully added; otherwise false
     */
    public boolean addSubmission(Submission submission) {
        //Added submission has empty flag on default
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO submission(eid, hours, worked_date, comment, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, submission.getEmploymentId());
            statement.setInt(2, submission.getHours());
            statement.setDate(3, convertToSqlDate(submission.getDate()));
            statement.setString(4, submission.getComment());
            statement.setString(5, ""); // empty flag
            statement.execute();
            if (statement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
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

    public Date convertToSqlDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(date);
        return new java.sql.Date(parsed.getTime());
    }
}
