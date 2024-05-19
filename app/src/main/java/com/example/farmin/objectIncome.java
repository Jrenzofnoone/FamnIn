package com.example.farmin;

public class objectIncome {
    public String amount;
    public String note;

    public objectIncome(){

    }
    public objectIncome(String amount, String note) {
        this.amount = amount;
        this.note = note;
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
}
