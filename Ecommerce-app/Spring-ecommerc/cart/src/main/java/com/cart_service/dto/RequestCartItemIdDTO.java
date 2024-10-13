package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;

public class RequestCartItemIdDTO extends InfoDTO {

    private Long cartId;
    private Long itemId;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
