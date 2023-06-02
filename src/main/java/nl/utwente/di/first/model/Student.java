package nl.utwente.di.first.model;

public class Student extends User {
    String birth;
    String university;
    String study;
    String lvStudy;

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBirth() {
        return birth;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getUniversity() {
        return university;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getStudy() {
        return study;
    }

    public String getLvStudy() {
        return lvStudy;
    }

    public void setLvStudy(String lvStudy) {
        this.lvStudy = lvStudy;
    }
}
