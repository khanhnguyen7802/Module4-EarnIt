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
     * @param email of a specific student
     * @return a full list of all submissions that was made by that student
     */
    public List<Submission> getStudentDateSubmissions(String email, String date) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT st.name AS student_name, c.name AS company_name, s.* " +
                            "FROM Submission s, Student st, Company c, Employment e " +
                            "WHERE st.id = e.sid AND c.id = e.cid AND e.eid = s.eid " +
                            "AND st.email = ? AND s.worked_date = ? "
            );
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, convertToSqlDate(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Submission> getCompanyDateSubmissions(String email, String date) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT st.name AS student_name, c.name AS company_name, s.* " +
                            "FROM Submission s, Student st, Company c, Employment e " +
                            "WHERE st.id = e.sid AND c.id = e.cid AND e.eid = s.eid " +
                            "AND c.email = ? AND s.worked_date = ? "
            );
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, convertToSqlDate(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Submission> getStudentWeekSubmissions(String email, String date) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT st.name AS student_name, c.name AS company_name, s.* " +
                            "FROM Submission s, Student st, Company c, Employment e " +
                            "WHERE st.id = e.sid AND c.id = e.cid AND e.eid = s.eid " +
                            "AND st.email = ? " +
                            "AND worked_date >= DATEADD(wk, DATEDIFF(wk, 0, ?), 0) AND worked_date < DATEADD(wk, DATEDIFF(wk, 0, ?) + 1, 0)"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, convertToSqlDate(date));
            preparedStatement.setDate(3, convertToSqlDate(date));
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
                    "SELECT DISTINCT st.name AS student_name, c.name AS company_name, s.* " +
                            "FROM Submission s, Student st, Company c, Employment e " +
                            "WHERE st.id = e.sid AND c.id = e.cid AND e.eid = s.eid " +
                            "AND c.email = ? " +
                            "AND worked_date >= DATEADD(wk, DATEDIFF(wk, 0, ?), 0) AND worked_date < DATEADD(wk, DATEDIFF(wk, 0, ?) + 1, 0)"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setDate(2, convertToSqlDate(date));
            preparedStatement.setDate(3, convertToSqlDate(date));
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
            submission.setEid((resultSet.getInt("eid")));
            submission.setComment(resultSet.getString("comment"));
            submission.setDate(resultSet.getDate("worked_date").toString());
            submission.setHours(resultSet.getInt("hours"));

            submissions.add(submission);
        }
        return submissions;
    }

    public boolean addSubmission(Submission submission) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO submission(eid, hours, worked_date, status, comment) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, submission.getEid());
            statement.setInt(2, submission.getHours());
            statement.setDate(3, convertToSqlDate(submission.getDate()));
            statement.setString(4, "pending");
            statement.setString(5, submission.getComment());
            
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

    /**
     * Given a student's email in a specific week, return the total hours worked
     * @param email - the student's email
     * @param week - the week being investigated
     * @return total hours worked in that week
     */
    public int getTotalHoursOfWeek(String email, int week, int year) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT SUM(hours) AS total_hours " +
                            "FROM submission s, employment e, student st " +
                            "WHERE st.id = e.sid AND e.eid = s.eid " +
                            "AND st.email = ? " +
                            "AND DATE_PART('week', worked_date) = ? AND DATE_PART('year', worked_date) = ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, week);
            preparedStatement.setInt(3, year);
            ResultSet resultSet = preparedStatement.executeQuery();

            int result = 0;
            while(resultSet.next()) {
                result = resultSet.getInt("total_hours");
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


            //TODO: Day-submission is not flagged
//    public void flagSubmission(String subId, String flag) {
//        //Possible flags: <empty>, Confirmed, Accepted, Rejected, Appealed
//        try {
//            Connection connection = DBConnection.createConnection();
//            String query = "UPDATE submission SET status = ? WHERE submission_id = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, flag);
//            statement.setString(2, subId);
//        } catch (SQLException e) {
//            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
//            throw new RuntimeException(e);
//        }
//    }

    public Date convertToSqlDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(date);
        return new java.sql.Date(parsed.getTime());
    }
}
