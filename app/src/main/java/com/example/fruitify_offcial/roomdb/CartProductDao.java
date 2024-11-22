package com.example.fruitify_offcial.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fruitify_offcial.models.Product;

import java.util.List;

@Dao

public interface CartProductDao {

    // Insert a product into the cart, replacing it if it already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartProduct(CartProducts products);

    // Update an existing product in the cart
    @Update
    void updateCartProduct(CartProducts products);

    // Get all products in the cart as LiveData
    @Query("SELECT * FROM CartProducts")
    LiveData<List<CartProducts>> getAllProducts();

    // Get the total count of products in the cart
    @Query("SELECT COUNT(*) FROM CartProducts")
    int getAllProductsCount();

    // Delete a product from the cart by product ID
    @Query("DELETE FROM CartProducts WHERE productId = :productId")
    void deleteCartProduct(String productId);

    @Query("DELETE FROM CartProducts")
    void deleteCartProducts();

}

