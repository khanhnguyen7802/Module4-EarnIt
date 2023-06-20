package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Student;
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
    public List<Submission> getStudentSubmissions(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT * " +
                            "FROM submission su, student st " +
                            "WHERE st.email = ? AND st.id = su.sid"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Submission> submissions = new ArrayList<>();
            while (resultSet.next()) {
                Submission submission = new Submission();
                submission.setComment(resultSet.getString("comment"));
                submission.setDate(resultSet.getString("worked_date"));
                submission.setStatus(resultSet.getString("status"));
                submission.setHours(resultSet.getInt("hours"));
                submissions.add(submission);
            }
            return submissions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Filters a students submissions by an input given week number from that year
     * @param email
     * @param weekNumber
     * @return a list with a student's submissions from a certain week (by given week number)
     */
    public List<Submission> getWeekOfSubmissions(String email, String weekNumber){ //TODO not tested
        List<Submission> weekOfSubmissions = new ArrayList<>();
        List<Submission> allSubmissions = getStudentSubmissions(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("w"); // result week in year

        for(Submission s: allSubmissions){
            if(dateFormat.format(s.getDate()).equals(dateFormat.format(weekNumber)))
                weekOfSubmissions.add(s);
        }
        return weekOfSubmissions;
    }

    public boolean addSubmission(Submission submission) {
        //Added submission has empty flag on default
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO submission(hours, worked_date, comment, status) VALUES (?, ?, ?, ?)";
            PreparedStatement insertSubmissionStatement = connection.prepareStatement(query);
            insertSubmissionStatement.setInt(1, submission.getHours());
            insertSubmissionStatement.setString(2, submission.getDate().toString());
            insertSubmissionStatement.setString(3, submission.getComment());
            insertSubmissionStatement.setString(4, submission.getStatus());
            insertSubmissionStatement.execute();
            if (insertSubmissionStatement.executeUpdate() != 0) {
                return true;
            }
        } catch (SQLException e) {
            // FIXME Runtime exceptions should be thrown as little as possible, error messages are much preferred.
            throw new RuntimeException(e);
        }
        return false;
    }

    //TODO not finished
    public boolean confirmWeekSubmissions(String email, List<Submission> weekOfSubmissions){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        try{
            Connection connection = DBConnection.createConnection();
            for(Submission s: weekOfSubmissions) {
                PreparedStatement confirmSubmissionStatement = connection.prepareStatement(
                        "UPDATE submission " +
                                "SET status = 'Confirmed' WHERE student.email = ? AND submission.worked_date = ? AND student.id = submission.sid"
                );
                confirmSubmissionStatement.setString(1, email);
                confirmSubmissionStatement.setString(2, dateFormat.format(s.getDate()));//TODO check if it matches
                confirmSubmissionStatement.execute();
                if (confirmSubmissionStatement.executeUpdate() != 0) {
                    return true;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    public void flagSubmission(String sid, String cid, LocalDate date, String flag) {
        /*
            Possible flags:
            empty (submitted but not confirmed)
            Confirmed
            Accepted
            Rejected
            Appealed
         */

    }
}
