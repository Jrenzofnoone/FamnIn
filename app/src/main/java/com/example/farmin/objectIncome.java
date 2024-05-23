package com.example.farmin;

public class objectIncome {
    public String amount;
    public String note;
    public String type;
    public String key;
    public String date;
    public String user;

    public objectIncome(){

    }

    public objectIncome(String amount, String note, String type, String key, String date, String user) {
        this.amount = amount;
        this.note = note;
        this.type = type;
        this.key = key;
        this.date = date;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
