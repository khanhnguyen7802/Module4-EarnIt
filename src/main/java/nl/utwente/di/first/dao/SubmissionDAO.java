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

public enum SubmissionDAO { //Might be a good idea to keep the week number of each submission in the database for simplicity. Otherwise, we need the query statements in a loop?
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
     * @param weekNumber - given by the user
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

    /**
     * Changes the status of the given week of submissions to "Confirmed".
     * @param weekOfSubmissions - list of submissions that should be produced using the getWeekOfSubmissions() method
     */
    //TODO not finished - check query and whether the loop is fine or not -- otherwise forget the list and match dates with week number in sql
    public boolean confirmWeekSubmissions(String sid, String cid, List<Submission> weekOfSubmissions){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        boolean statementsExecution = true;
        try{
            Connection connection = DBConnection.createConnection();
            PreparedStatement confirmSubmissionStatement = connection.prepareStatement(
                    "UPDATE submission " +
                            "SET status = 'Confirmed' WHERE submission.sid = ? AND submission.cid = ? AND submission.worked_date = ?");
            for(Submission s: weekOfSubmissions) {
                confirmSubmissionStatement.setString(1, sid);
                confirmSubmissionStatement.setString(2, cid);
                confirmSubmissionStatement.setString(3, dateFormat.format(s.getDate()));
                confirmSubmissionStatement.execute();
                if (confirmSubmissionStatement.executeUpdate() == 0) {
                    statementsExecution = false;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return statementsExecution;
    }

    /**
     * Sets the status of the submissions from a given week to "Rejected", and changes their number of hours to what the company suggested.
     * @param weekOfSubmissions - list of submissions that should be produced using the getWeekOfSubmissions() method
     * @param suggestedHours - given by the user
     */ //TODO not finished - check query and whether the loop is fine or not -- otherwise forget the list and match dates with week number in sql
    public boolean rejectWeekSubmission(String sid, String cid, List<Submission> weekOfSubmissions, int suggestedHours){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        boolean statementsExecution = true;
        try{
            Connection connection = DBConnection.createConnection();
            PreparedStatement rejectSubmissionStatement = connection.prepareStatement(
                    "UPDATE submission " +
                            "SET status = 'Rejected', hours = ? WHERE submission.sid = ? AND submission.cid = ? AND submission.worked_date = ?");
            for(Submission s: weekOfSubmissions) {
                rejectSubmissionStatement.setString(1, String.valueOf(suggestedHours));
                rejectSubmissionStatement.setString(2, sid);
                rejectSubmissionStatement.setString(3, cid);
                rejectSubmissionStatement.setString(4, dateFormat.format(s.getDate()));
                rejectSubmissionStatement.execute();
                if (rejectSubmissionStatement.executeUpdate() == 0) {
                    statementsExecution = false;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return statementsExecution;
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
