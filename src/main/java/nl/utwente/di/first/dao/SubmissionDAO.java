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
    public List<Submission> getSubmissions(String email) {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT *" +
                            "FROM Submission s, User u " +
                            "WHERE u.email = ? AND (u.id = s.sid OR u.id = s.cid)"
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

    public List<Submission> getWeekOfSubmissions(String email, String weekNumber){ //TODO not tested
        List<Submission> weekOfSubmissions = new ArrayList<>();
        List<Submission> allSubmissions = getSubmissions(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("ww");

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
