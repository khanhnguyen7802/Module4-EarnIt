package nl.utwente.di.first.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Flag {
    int eid;
    int week;
    int year;
    String status; // pending: not confirmed, but can't interact with it at the moment; accept: accepted; reject: company rejected; appeal: company rejected and student rejected
    int total_hours;
    String company_name;
    String job_title;
    byte[] logo;
    Integer suggested_hours;

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(int total_hours) {
        this.total_hours = total_hours;
    }

    public Integer getSuggested_hours() {
        return suggested_hours;
    }

    public void setSuggested_hours(Integer suggested_hours) {
        this.suggested_hours = suggested_hours;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
