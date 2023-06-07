package nl.utwente.di.first.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission {

    private int numberOfHours;
    private String description;
    private boolean flagged;
    private LocalDateTime submissionDate; //The date in which the submission is created
    private LocalDate workedDate; //The declared date in which the student worked

    //TODO: status enum for rejections

    public Submission(int numberOfHours, String description, LocalDate workedDate){
        this.numberOfHours = numberOfHours;
        this.description = description;
        this.flagged = false;
        this.workedDate = workedDate;
        this.submissionDate = LocalDateTime.now(); //TODO is this suitable?
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public String getSubmissionDateString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = submissionDate.format(dateTimeFormatter);
        return formattedDateTime;
    }

    public String getWorkedDateString(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateTime = workedDate.format(dateTimeFormatter);
        return formattedDateTime;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void flag() {
        this.flagged = true;
    }
}
