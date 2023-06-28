package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateJobs {
    private int eid;
    private int cid;
    private int sid;
    private String title;
    private String description;
    private int salary;

    public CreateJobs() {

    }

    public int getEid() {return eid;}

    public void setEid(int eid) {this.eid = eid;}

    public int getCid() {return cid;}
    public void setCid(int cid) {this.cid = cid;}
    public int getSid() {return sid;}
    public void setSid(int sid) {this.sid = sid;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
    public int getSalary() {return salary;}
    public void setSalary(int salary) {
        this.salary = salary;
    }


}
