package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.CreateJobs;
import nl.utwente.di.first.util.DBConnection;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public enum CreateJobsDAO {
    instance;
    private CreateJobsDAO() {

    }
    private List<CreateJobs> getQuery(ResultSet resultSet) throws SQLException {
        List<CreateJobs> createJobs = new ArrayList<>();
        while (resultSet.next()) {
            CreateJobs createJob = new CreateJobs();
            createJob.setCid(resultSet.getInt("cid"));
            createJob.setSid(resultSet.getInt("sid"));
            createJob.setEid(resultSet.getInt("eid"));
            createJob.setTitle(resultSet.getString("job_title"));
            createJob.setDescription(resultSet.getString("job_description"));
            createJob.setSalary(resultSet.getInt("salary_per_hour"));

            createJobs.add(createJob);
        }
        return createJobs;
    }

    public boolean addCreateJob(CreateJobs createJobs) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO createJob(cid, sid, eid, job_title, job_description, salary_per_hour) VALUE(?, ?, ?, ?, ?, ? )";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, createJobs.getCid());
            statement.setInt(2, createJobs.getSid());
            statement.setInt(3, createJobs.getEid());
            statement.setString(4, createJobs.getTitle());
            statement.setString(5, createJobs.getDescription());
            statement.setInt(6, createJobs.getSalary());

            if (statement.executeUpdate() !=0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
