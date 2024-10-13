package com.stripe.stripe.dto.cartdto;

import java.io.Serializable;
import java.util.List;
public class CartDTO implements Serializable {
    private static long SerialVersionID = 1L;
    private Long id;
    private String token;
    private List<CartItemDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}
