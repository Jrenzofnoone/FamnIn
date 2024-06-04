package com.example.farmin;

public class activityUploads {
    public activityUploads() {

    }
    String name;
    String type;
    String descrip;
    String startYear;
    String startMonth;
    String startDay;
    String finishYear;
    String finishMonth;
    String finishDay;
    String notes;
    String mimage;
    String key;
    String userEmail;

    public activityUploads(String userEmail,String key,String name, String type, String descrip, String startYear, String startMonth, String startDay,String finishYear, String finishMonth, String finishDay, String notes, String image) {
        this.userEmail = userEmail;
        this.name = name;
        this.type = type;
        this.descrip = descrip;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.finishYear = finishYear;
        this.finishMonth = finishMonth;
        this.finishDay = finishDay;
        this.notes = notes;
        this.mimage = image;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getFinishYear() {
        return finishYear;
    }

    public void setFinishYear(String finishYear) {
        this.finishYear = finishYear;
    }

    public String getFinishMonth() {
        return finishMonth;
    }

    public void setFinishMonth(String finishMonth) {
        this.finishMonth = finishMonth;
    }

    public String getFinishDay() {
        return finishDay;
    }

    public void setFinishDay(String finishDay) {
        this.finishDay = finishDay;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getimage() {
        return mimage;
    }

    public void setimage(String image) {
        this.mimage = image;
    }
}