package com.example.farmin;

public class addingUploads {
    public addingUploads() {

    }
    private String csType, key, userEmail, name, type,descrip,  notes,  mimageurl,  mqrcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public addingUploads(String csType, String key, String userEmail, String name, String type, String descrip, String notes, String imageurl, String qrcode) {
        this.csType = csType;
        this.key = key;
        this.userEmail = userEmail;
        this.name = name;
        this.descrip = descrip;
        this.notes = notes;
        this.mimageurl = imageurl;
        this.mqrcode = qrcode;
        this.type = type;
    }
}

