package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;

import java.util.List;

public class RequestCartItemDTO extends InfoDTO {
    private Long cartId;
    private Long token;
    private List<CartItemDTO> cartItemsDTO;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemDTO> getCartItemsDTO() {
        return cartItemsDTO;
    }

    public void setCartItemsDTO(List<CartItemDTO> cartItemsDTO) {
        this.cartItemsDTO = cartItemsDTO;
    }
}
