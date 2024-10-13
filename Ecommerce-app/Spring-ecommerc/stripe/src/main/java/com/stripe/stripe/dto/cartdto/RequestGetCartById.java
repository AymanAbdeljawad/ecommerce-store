package com.stripe.stripe.dto.cartdto;


import com.stripe.stripe.common.dto.InfoDTO;

public class RequestGetCartById extends InfoDTO {
    private Long cartId;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }
}
