package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@XmlRootElement
public class Submission {
    private String eid;
    private int hours;
    private String comment;
    private String status;
    private Date date;

    public Submission() {
        status = "";
    }
    public String getEmploymentId() {
        return eid;
    }
    public void setEmploymentId(String eid) {
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
    public Date getDate(){
        return date;
    }
    public void setDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = formatter.parse(date);
        this.date = new java.sql.Date(parsed.getTime());
    }
}
