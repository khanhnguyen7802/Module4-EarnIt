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
     *
     * @param eid  - the eid to know about the employment
     * @param week - the week being investigated
     * @return total hours worked in that week
     */
    public int getTotalHoursOfWeek(int eid, int week, int year) {
        try (Connection connection = DBConnection.createConnection()) {

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
            while (resultSet.next()) {
                result = resultSet.getInt("total_hours");
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given eid, week and year, return all submissions of that week
     *
     * @param week - the week being investigated
     * @param year - the year being investigated
     * @return a list of submissions have been made in that week
     */
    public List<Submission> getWeeklySubmission(String email, int week, int year) {
        List<Submission> submissions = new ArrayList<>();
        try (Connection connection = DBConnection.createConnection()) {


            // this query will return all employment's of that student in the given (week, year)
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT e.eid, comment, hours, worked_date, c.name AS company_name, job_title, c.logo AS logo " +
                            "FROM submission s, employment e, student st, company c " +
                            "WHERE s.eid = e.eid AND e.sid = st.id AND c.id = e.cid " +
                            "AND DATE_PART('week', worked_date) = ? " +
                            "AND DATE_PART('year', worked_date) = ? " +
                            "AND st.email = ?"
            );

            preparedStatement.setInt(1, week);
            preparedStatement.setInt(2, year);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Submission submission = new Submission();
                submission.setEid(resultSet.getInt("eid"));
                submission.setHours(resultSet.getInt("hours"));
                submission.setComment(resultSet.getString("comment"));
                submission.setDate(resultSet.getDate("worked_date"));
                submission.setCompany_name(resultSet.getString("company_name"));
                submission.setJob_title(resultSet.getString("job_title"));
                submission.setLogo(resultSet.getBytes("logo"));

                submissions.add(submission);
            }

            return submissions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Submission> getWeeklySubmissionForCompany(int eid, String email, int week, int year) {
        List<Submission> submissions = new ArrayList<>();
        try (Connection connection = DBConnection.createConnection()) {

            // this query will return all students together with employment for a company in the given (week, year)
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT e.eid, comment, hours, worked_date, st.name AS student_name, job_title " +
                            "FROM submission s, employment e, student st, company c " +
                            "WHERE s.eid = e.eid AND e.sid = st.id AND c.id = e.cid " +
                            "AND DATE_PART('week', worked_date) = ? " +
                            "AND DATE_PART('year', worked_date) = ? " +
                            "AND e.eid = ?" +
                            "AND c.email = ?"
            );

            preparedStatement.setInt(1, week);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, eid);
            preparedStatement.setString(4, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Submission submission = new Submission();

                submission.setEid(resultSet.getInt("eid"));
                submission.setHours(resultSet.getInt("hours"));
                submission.setComment(resultSet.getString("comment"));
                submission.setDate(resultSet.getDate("worked_date"));
                submission.setJob_title(resultSet.getString("job_title"));

                submissions.add(submission);
            }

            return submissions;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
