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
        try {
            Connection connection = DBConnection.createConnection();

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

    public List<Flag> getAllWeekInstances() {
        try {
            Connection connection = DBConnection.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM flag " //TODO: we might not need to get all
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            return getQuery(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Flag> getQuery(ResultSet resultSet) throws SQLException {
        List<Flag> flags = new ArrayList<>();
        while (resultSet.next()) {
            Flag flag = new Flag();
            flag.setEid(resultSet.getInt("eid"));
            flag.setWeek(resultSet.getInt("week"));
            flag.setYear(resultSet.getInt("year"));
            flag.setStatus(resultSet.getString("status"));

            flags.add(flag);
        }
        return flags;
    }

}
