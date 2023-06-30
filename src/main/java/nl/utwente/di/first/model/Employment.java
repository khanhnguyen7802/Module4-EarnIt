package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employment {
    int eid;    
    int sid;
    int cid;
    String student_name;
    String company_name;
    String job_description;
    String job_title;
    double salary_per_hour;
    byte[] logo;
    int appeal_amount;
    
    
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
    
    public double getSalary_per_hour() {
        return salary_per_hour;
    }

    public void setSalary_per_hour(double salary_per_hour) {
        this.salary_per_hour = salary_per_hour;
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
    
    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }
    
    public byte[] getLogo() {
        return logo;
    }
    
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    
    public int getAppeal_amount() {
        return appeal_amount;
    }
    
    public void setAppeal_amount(int appeal_amount) {
        this.appeal_amount = appeal_amount;
    }
}
