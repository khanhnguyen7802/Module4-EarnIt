package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Date;

@XmlRootElement
public class Submission {
    int eid;
    int hours;
    String comment;
    Date date;

    public Submission() {

    }
    public int getEid() {
        return eid;
    }
    public void setEid(int eid) {
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
    public Date getDate(){
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

}
