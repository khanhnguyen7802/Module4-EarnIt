package nl.utwente.di.first.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission {

    private int numberOfHours;
    private String description;
    private boolean flagged;
    private LocalDateTime date;

    //TODO: status enum for rejections

    public Submission(int numberOfHours, String description){
        this.numberOfHours = numberOfHours;
        this.description = description;
        this.flagged = false;
        this.date = LocalDateTime.now(); //TODO is this suitable?
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

    public String getDateString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = date.format(dateTimeFormatter);
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
