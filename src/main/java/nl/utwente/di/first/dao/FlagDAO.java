package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Flag;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum FlagDAO {
    instance;

    private FlagDAO() {

    }

    public boolean addFlag(Flag flag) {
        //Possible flags: <empty>, Confirmed, Accepted, Rejected, Appealed
        try (Connection connection = DBConnection.createConnection()) {
            

            // check if a row (eid, week, year) has already existed or not
            // if not, then add in
            PreparedStatement statement = connection.prepareStatement("SELECT EXISTS( " +
                    "SELECT 1 FROM flag WHERE week = ? AND year = ? AND eid = ? " + ")"
            );

            statement.setInt(1, flag.getWeek());
            statement.setInt(2, flag.getYear());
            statement.setInt(3, flag.getEid());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                if(resultSet.getString("exists").equals("f")) {
                    String query = "INSERT INTO flag(eid, week, year, status) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, flag.getEid());
                    preparedStatement.setInt(2, flag.getWeek());
                    preparedStatement.setInt(3, flag.getYear());
                    preparedStatement.setString(4, flag.getStatus());

                    if (preparedStatement.executeUpdate() != 0) {
                        return true;
                    }

                    return false;
                }
                System.out.println("already existed");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
    }
        return false;
}

    /**
     * Return all the flag (aka weekly submission) of a specific student
     * @param week - the week being investigated
     * @param year - the year being investigated
     * @return a list of Flag (i.e. weekly submission) of a student
     */
    public List<Flag> getAllWeeklyFlags(String email, int week, int year) {
        try (Connection connection = DBConnection.createConnection()) {
            
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT e.eid, SUM(hours) AS total_hours, c.name AS company_name, job_title, c.logo AS logo, f.status, f.suggested_hours " +
                            "FROM submission s, employment e, student st, company c, flag f " +
                            "WHERE s.eid = e.eid AND e.sid = st.id AND c.id = e.cid AND f.eid = e.eid " +
                            "AND f.week = ?" +
                            "AND DATE_PART('week', worked_date) = f.week " +
                            "AND DATE_PART('year', worked_date) = ? " +
                            "AND st.email = ? " +
                            "GROUP BY e.eid, c.name, job_title, c.logo, f.status, f.week"
            );

            preparedStatement.setInt(1, week);
            preparedStatement.setInt(2, year);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            List<Flag> flags = new ArrayList<>();
            while (resultSet.next()) {
                Flag flag = new Flag();
                flag.setEid(resultSet.getInt("eid"));
                flag.setWeek(week);
                flag.setYear(year);
                flag.setStatus(resultSet.getString("status"));
                flag.setTotal_hours(resultSet.getInt("total_hours"));
                flag.setCompany_name(resultSet.getString("company_name"));
                flag.setJob_title(resultSet.getString("job_title"));
                flag.setLogo(resultSet.getBytes("logo"));
                flag.setSuggested_hours(resultSet.getInt("suggested_hours"));

                flags.add(flag);
                
            }
            return flags;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean updateStatus(Flag flag) {
        try (Connection connection = DBConnection.createConnection();) {
            
            String query = "UPDATE flag " +
                    "SET status = ?, suggested_hours = ? " +
                    "WHERE eid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, flag.getStatus());
            preparedStatement.setInt(2, flag.getSuggested_hours());
            preparedStatement.setInt(3, flag.getEid());
            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the number of rejected weeks
     * @param email
     * @return the number of weeks that are rejected
     */
    public int getTotalRejectedWeeks(String email) {
        try (Connection connection = DBConnection.createConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT COUNT(*) AS total_rejected " +
                            "FROM flag f, employment e, student s " +
                            "WHERE s.id = e.sid AND f.eid = e.eid " +
                            "AND f.status = 'reject' " +
                            "AND s.email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            int result = 0;
            while (resultSet.next()) {
                result = resultSet.getInt("total_rejected");
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Return the (week+year) of all reflected weeks
     * @param email
     * @return (week+year) for all reflected weeks
     */
    public List<Flag> getAllRejectedWeeks(String email) {
        try (Connection connection = DBConnection.createConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT week, year " +
                            "FROM flag f, employment e, student st " +
                            "WHERE f.eid = e.eid AND e.sid = st.id " +
                            "AND status='rejected' " +
                            "AND st.email = ?"
                            );

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Flag> flags = new ArrayList<>();
            while (resultSet.next()) {
                Flag flag = new Flag();
                flag.setWeek(resultSet.getInt("week"));
                flag.setYear(resultSet.getInt("year"));

                flags.add(flag);
            }
            return flags;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
