package com.example.fruitify_offcial.models;

import java.util.List;

public class Product {
    private String productTitle;
    private String productQuantity;
    private String productUnit;
    private Long productPrice; // Change int to Long if needed
    private Long productStock; // Change int to Long if needed
    private String productCategory;
    private String productType;
    private int itemCount;
    private String adminUid;
    private String productImageUris; // Change to List<String> to support multiple images
    private String productRandomId;

    // Default no-argument constructor required for Firebase
    public Product() {}

    // Constructor with all fields (optional)
    public Product(String productTitle, String productQuantity, String productUnit, Long productPrice,
                   Long productStock, String productCategory, String productType, int itemCount,
                   String adminUid, List<String> productImageUris, String productRandomId) {
        this.productTitle = productTitle;
        this.productQuantity = productQuantity;
        this.productUnit = productUnit;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productCategory = productCategory;
        this.productType = productType;
        this.itemCount = itemCount;
        this.adminUid = adminUid;
        this.productImageUris = productImageUris.toString(); // Assign the list
        this.productRandomId = productRandomId;
    }

    // Getters and Setters
    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductStock() {
        return productStock;
    }

    public void setProductStock(Long productStock) {
        this.productStock = productStock;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getProductImageUris() {
        return productImageUris;
    }

    public void setProductImageUris(String productImageUris) {
        this.productImageUris = productImageUris;
    }

    public String getProductRandomId() {
        return productRandomId;
    }

    public void setProductRandomId(String productRandomId) {
        this.productRandomId = productRandomId;
    }
}
