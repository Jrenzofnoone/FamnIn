package com.example.farmin;

public class addingUploads {
    public addingUploads() {

    }
    private String csType, key, userEmail, name, type, status,count,  notes,  mimageurl,  mqrcode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImageurl() {
        return mimageurl;
    }

    public void setImageurl(String imageurl) {
        this.mimageurl = imageurl;
    }

    public String getQrcode() {
        return mqrcode;
    }

    public void setQrcode(String qrcode) {
        this.mqrcode = qrcode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCsType() {
        return csType;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public addingUploads(String csType, String key, String userEmail, String name, String type,String status, String count, String notes, String imageurl, String qrcode) {
        this.csType = csType;
        this.key = key;
        this.userEmail = userEmail;
        this.name = name;
        this.count = count;
        this.notes = notes;
        this.mimageurl = imageurl;
        this.mqrcode = qrcode;
        this.type = type;
        this.status = status;
    }
}

