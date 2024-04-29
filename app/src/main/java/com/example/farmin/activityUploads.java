package com.example.farmin;

public class activityUploads {
    public activityUploads() {

    }
    String name;
    String type;
    String descrip;
    String year;
    String month;
    String day;
    String yield;
    String notes;
    String mimage;
    String key;

    public activityUploads(String key,String name, String type, String descrip, String year, String month, String day, String yield, String notes, String image) {
        this.name = name;
        this.type = type;
        this.descrip = descrip;
        this.year = year;
        this.month = month;
        this.day = day;
        this.yield = yield;
        this.notes = notes;
        this.mimage = image;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
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
