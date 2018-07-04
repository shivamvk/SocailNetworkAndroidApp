package com.example.shivamvk.facebookandroid;

public class User {

    String userId,userName,userEmail,userPassword,userImage;

    public User(String userId, String userName, String userEmail, String userPassword, String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserImage() {
        return userImage;
    }
}

