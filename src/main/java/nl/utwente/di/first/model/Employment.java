package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employment {
    int eid;    
    int sid;
    int cid;
    String studentName;
    String companyName;
    String job_description;
    String job_title;
    double salaryPerHour;
    
    
    public int getSid() {
        return sid;
    }
    
    public void setSid(int sid) {
        this.sid = sid;
    }
    
    public int getCid() {
        return cid;
    }
    
    public void setCid(int cid) {
        this.cid = cid;
    }
    
    public double getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(double salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }
    
    public int getEid() {
        return eid;
    }
    
    public void setEid(int eid) {
        this.eid = eid;
    }
    
    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
    
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }
}
