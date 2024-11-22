package com.example.fruitify_offcial.models;

public class Category {
    private String title;
    private int image;

    public Category(){

    }

    // Corrected constructor to accept title and image
    public Category(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
