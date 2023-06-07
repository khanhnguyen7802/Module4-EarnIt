package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Submission;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubmissionDAO {

    //TODO does student need an attribute with the companies they work for?

    //TODO employer: approve or reject submission -> propose new one
    //TODO student: agree or disagree -> submission goes to staff

    public boolean addSubmission(Submission submission){ //TODO: Not final
        try {
            Connection connection = DBConnection.createConnection();

            String query = "INSERT INTO progress(hours, description, date) " +
                    "VALUES (?, ?, ?)"; //Temporary - no table in db yet

            PreparedStatement insertSubmissionStatement = connection.prepareStatement(query);
            insertSubmissionStatement.setString(1, Integer.toString(submission.getNumberOfHours())); //TODO: String
            insertSubmissionStatement.setString(2, submission.getDescription());
            insertSubmissionStatement.setString(3, submission.getDateString()); //TODO: String

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


    //TODO
    public boolean rejectSubmission(){
        return true;
    }

    public boolean suggestDifferentSubmission(){
        return true;
    }

    public boolean flagSubmission(){
        return true;
    }
}
