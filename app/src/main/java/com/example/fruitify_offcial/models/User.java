package com.example.fruitify_offcial.models;

public class User {
    private String uid;
    private String userPhoneNumber;
    private String userAddress;

    // Constructor
    public User() {}

    // Getters
    public String getUid() {
        return uid;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    // Setters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
