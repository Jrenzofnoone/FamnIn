package com.example.farmin;

public class anotherUserUploads {
        String key,userEmail,userNickname, userImage, userAge, userNumber;
        public anotherUserUploads() {

        }

        public anotherUserUploads(String key,String userEmail,String userNickname, String userAge, String userNumber) {
            this.key = key;
            this.userEmail = userEmail;
            this.userNickname = userNickname;
            this.userAge = userAge;
            this.userNumber = userNumber;
        }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserAge() {
            return userAge;
        }

        public void setUserAge(String userAge) {
            this.userAge = userAge;
        }

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
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

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}


