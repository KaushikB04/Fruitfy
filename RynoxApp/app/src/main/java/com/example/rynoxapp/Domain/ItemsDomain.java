package com.example.rynoxapp.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable {

    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private int review;
    private double rating;
    private int NumberinCart;

    // No-argument constructor
    public ItemsDomain() {
        // Initialize fields with default values if needed
        this.title = "";
        this.description = "";
        this.picUrl = new ArrayList<>();
        this.price = 0.0;
        this.review = 0;
        this.rating = 0.0;
        this.NumberinCart = 0;
    }

    // Parameterized constructor
    public ItemsDomain(String title, String description, ArrayList<String> picUrl, double price, int review, double rating) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.review = review;
        this.rating = rating;
        this.NumberinCart = 0; // Initialize numberInCart with a default value
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberinCart() {
        return NumberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        this.NumberinCart = numberinCart;
    }
}
