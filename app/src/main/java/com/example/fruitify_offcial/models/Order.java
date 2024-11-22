package com.example.fruitify_offcial.models;

import com.example.fruitify_offcial.roomdb.CartProducts;

import java.util.List;

public class Order {
    private String orderId = null;
    private List<CartProducts> orderList = null;  // CartProducts represents the model for products in the cart
    private String userAddress = null;
    private String paymentMethod = null;
    private String orderingUserId = null;
    private int orderStatus = 0;
    private String orderDate = null;


    // Constructor
    public Order(String orderId, List<CartProducts> orderList, String userAddress, int orderStatus, String orderDate, String orderingUserId, String paymentMethod) {
        this.orderId = orderId;
        this.orderList = orderList;
        this.userAddress = userAddress;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderingUserId = orderingUserId;
        this.paymentMethod = paymentMethod;

    }

    // Default constructor
    public Order() {}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CartProducts> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<CartProducts> orderList) {
        this.orderList = orderList;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderingUserId() {
        return orderingUserId;
    }

    public void setOrderingUserId(String orderingUserId) {
        this.orderingUserId = orderingUserId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


}
