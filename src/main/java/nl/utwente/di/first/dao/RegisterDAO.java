package nl.utwente.di.first.dao;

import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.util.DBConnection;
import nl.utwente.di.first.util.Security;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum RegisterDAO {
    instance;
    
    RegisterDAO() {
        
    }
    
    public boolean registerStudent(Student student) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO student (email, password, salt, name, birthdate, university, study, skills, btw_number) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            byte[] byteSalt = Security.getSalt();
            String salt = Security.toHex(byteSalt);
            String securePassword = Security.saltSHA256(student.getPassword(), byteSalt);
            
            preparedStatement.setString(1, student.getEmail());
            preparedStatement.setString(2, securePassword);
            preparedStatement.setString(3, salt);
            preparedStatement.setString(4, student.getName());
            preparedStatement.setDate(5, Date.valueOf(student.getBirth()));
            preparedStatement.setString(6, student.getUniversity());
            preparedStatement.setString(7, student.getStudy());
            preparedStatement.setString(8, student.getSkills());
            preparedStatement.setString(9, student.getBtw_num());
            
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public boolean registerCompany(Company company) {
        try {
            Connection connection = DBConnection.createConnection();
            String query = "INSERT INTO company (email, password, salt, name, location, field, contact, kvk_number) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        
            byte[] byteSalt = Security.getSalt();
            String salt = Security.toHex(byteSalt);
            String securePassword = Security.saltSHA256(company.getPassword(), byteSalt);
        
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, securePassword);
            preparedStatement.setString(3, salt);
            preparedStatement.setString(4, company.getName());
            preparedStatement.setString(5, company.getLocation());
            preparedStatement.setString(6, company.getField());
            preparedStatement.setString(7, company.getContact());
            preparedStatement.setString(8, company.getKvk_num());
        
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
