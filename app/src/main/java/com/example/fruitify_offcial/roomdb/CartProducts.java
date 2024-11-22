package com.example.fruitify_offcial.roomdb;

import androidx.annotation.NonNull; // Import NonNull annotation
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CartProducts")
public class CartProducts {

    @PrimaryKey
    @NonNull // Marking productId as NonNull
    private String productId = "random";

    private String productTitle;
    private String productQuantity;
    private String productPrice;
    private int productCount;
    private int productStock;
    private String productImageUri; // Changed to single image URI
    private String productCategory;
    private String adminUid;

    // Constructor
    public CartProducts(@NonNull String productId, String productTitle, String productQuantity, String productPrice, Integer productCount, Integer productStock, String productImageUri, String productCategory, String adminUid) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.productStock = productStock;
        this.productImageUri = productImageUri;
        this.productCategory = productCategory;
        this.adminUid = adminUid;
    }


    // Default constructor
    public CartProducts() {
    }

    // Getters and Setters
    @NonNull // Ensure getter returns a non-null value
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public String getProductImageUri() { // Updated getter
        return productImageUri;
    }

    public void setProductImageUri(String productImageUri) { // Updated setter
        this.productImageUri = productImageUri;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }
}
