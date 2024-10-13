package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;

public class RequestCartDTO extends InfoDTO {
    private CartDTO cartDTO;

    public CartDTO getCartDTO() {
        return cartDTO;
    }

    public void setCartDTO(CartDTO cartDTO) {
        this.cartDTO = cartDTO;
    }
}
