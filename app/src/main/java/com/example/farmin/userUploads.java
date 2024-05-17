package com.example.farmin;

public class userUploads {

    String userEmail, userImage;

    public userUploads(String userEmail, String userImage) {
        this.userEmail = userEmail;
        this.userImage = userImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
