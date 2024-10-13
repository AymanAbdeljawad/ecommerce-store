package com.order_service.dto.cartdto;

import java.io.Serializable;
import java.util.List;

public class CartDTO implements Serializable {
    private static long SerialVersionID = 1L;
    private Long cartId;
    private List<CartItemDTO> items;
    private double totalPrice;

    public CartDTO() {
    }

    public CartDTO(Long id, List<CartItemDTO> items, double totalPrice) {
        this.cartId = id;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}