package com.order_service.dto.cartdto;

import java.io.Serializable;

public class CartItemDTO implements Serializable {

    private Long id;
    private String productName;
    private String description;
    private int quantity;
    private double price;

    public CartItemDTO() {
    }

    public CartItemDTO(Long id, String productName, String description, int quantity, double price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}