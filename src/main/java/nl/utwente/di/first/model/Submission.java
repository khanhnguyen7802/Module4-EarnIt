package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@XmlRootElement
public class Submission {
    private int eid;
    private int hours;
    private String comment;
    private String status;
    private String date;
    private String companyName;
    private String jobTitle;
    private String studentName;

    public Submission() {
        status = "";
    }
    public int getEmploymentId() {
        return eid;
    }
    public void setEmploymentId(int eid) {
        this.eid = eid;
    }
    public int getHours() {
        return hours;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
