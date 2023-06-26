package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Submission {
    private int eid;
    private int hours;
    private String comment;
    private String date;

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
    public String getDate(){
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
