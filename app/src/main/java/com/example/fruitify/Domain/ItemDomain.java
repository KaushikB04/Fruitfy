package com.example.fruitify.Domain;

public class ItemDomain {

    private String title;
    private String description;
    private String picUrl;
    private int price;

    private double star;
    private int id;
    private int  categoryId;

    public ItemDomain(){

    }

    public ItemDomain(String title, String description, String picUrl, int price, double star, int id, int categoryId) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.star = star;
        this.id = id;
        this.categoryId = categoryId;
    }

    public int getPrice() {
        return price;
    }




    public void setPrice(int price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


}
