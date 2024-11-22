package com.example.fruitify_offcial.models;

public class OrderedItems {
    private String orderId;
    private String itemDate;
    private Integer itemStatus;
    private String itemTitle;
    private Double itemPrice;

    public OrderedItems(String orderId, String orderDate, int orderStatus, StringBuilder finalTitle, double totalPrice) {
        this.orderId = orderId;
        this.itemDate = orderDate;
        this.itemStatus = orderStatus;
        this.itemTitle = finalTitle.toString();
        this.itemPrice = totalPrice; // Use double for precision
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemDate() {
        return itemDate;
    }

    public Integer getItemStatus() {
        return itemStatus;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public Double getItemPrice() {
        return itemPrice;
    }
}

