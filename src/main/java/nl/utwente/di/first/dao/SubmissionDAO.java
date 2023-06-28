package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Submission;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum SubmissionDAO {
    instance;
    private SubmissionDAO() {

    }

    private List<Submission> getQuery(ResultSet resultSet) throws SQLException {
        List<Submission> submissions = new ArrayList<>();
        while (resultSet.next()) {
            Submission submission = new Submission();
            submission.setEid((resultSet.getInt("eid")));
            submission.setComment(resultSet.getString("comment"));
            submission.setDate(resultSet.getDate("worked_date"));
            submission.setHours(resultSet.getInt("hours"));

            submissions.add(submission);
        }
        return submissions;
    }

    public boolean addSubmission(Submission submission) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO submission(eid, hours, worked_date, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, submission.getEid());
            statement.setInt(2, submission.getHours());
            statement.setDate(3, submission.getDate());
            statement.setString(4, submission.getComment());
            
            if (statement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Given an eid in a specific week, return the total hours worked
     * @param eid - the eid to know about the employment
     * @param week - the week being investigated
     * @return total hours worked in that week
     */
    public int getTotalHoursOfWeek(int eid, int week, int year) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT SUM(hours) AS total_hours " +
                            "FROM submission s, employment e " +
                            "WHERE e.eid = s.eid " +
                            "AND s.eid = ? " +
                            "AND DATE_PART('week', worked_date) = ? AND DATE_PART('year', worked_date) = ?"
            );
            preparedStatement.setInt(1, eid);
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

    /**
     * Given eid, week and year, return all submissions of that week
     * @param eid - employment id
     * @param week - the week being investigated
     * @param year - the year being investigated
     * @return a list of submissions have been made in that week
     */
    public List<Submission> getWeeklySubmission(int eid, int week, int year) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT s.comment, hours, worked_date " +
                            "FROM submission s, employment e " +
                            "WHERE s.eid = e.eid AND e.eid = ? " +
                            "AND DATE_PART('week', worked_date) = ? " +
                            "AND DATE_PART('year', worked_date) = ?"
            );

            preparedStatement.setInt(1, eid);
            preparedStatement.setInt(2, week);
            preparedStatement.setInt(3, year);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Submission> submissions = new ArrayList<>();
            while(resultSet.next()) {
                Submission submission = new Submission();
                submission.setHours(resultSet.getInt("hours"));
                submission.setComment(resultSet.getString("comment"));
                submission.setDate(resultSet.getDate("worked_date"));

                submissions.add(submission);
            }

            return submissions;

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

}
