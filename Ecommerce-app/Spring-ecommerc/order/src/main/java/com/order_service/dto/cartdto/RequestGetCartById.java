package com.order_service.dto.cartdto;


import com.order_service.common.dto.InfoDTO;

public class RequestGetCartById extends InfoDTO {
    private String cartId;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}
