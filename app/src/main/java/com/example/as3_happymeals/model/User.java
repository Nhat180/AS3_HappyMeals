package com.example.as3_happymeals.model;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String email;
    private String isAdmin = "0";
    private List<String> siteRegistered = new ArrayList<>();

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<String> getSiteRegistered() {
        return siteRegistered;
    }

    public void setSiteRegistered(List<String> siteRegistered) {
        this.siteRegistered = siteRegistered;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", siteRegistered=" + siteRegistered +
                '}';
    }
}

