package com.example.farmin;


public class objectIncome {
    private String amount; // Assuming amount is stored as a String
    private String note;
    private String type;
    private String date;
    private String key;
    // Constructor, getters, and setters
    public objectIncome() {

    }

    public objectIncome(String amount, String note, String type, String date, String key) {
        this.amount = amount;
        this.note = note;
        this.type = type;
        this.date = date;
        this.key = key;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmountAsDouble() {
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getDate() {
        return date;
  }
}
