package com.cart_service.common.mapper;

import com.cart_service.dto.CartDTO;
import com.cart_service.dto.CartItemDTO;
import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class CartMapper {

    public static Cart toCart(CartDTO cartDTO) {
        if (cartDTO == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(cartDTO.getId());
        cart.setToken(cartDTO.getToken());
        cart.setItems(mapCartItems(cartDTO.getItems()));
        return cart;
    }

    public static CartDTO toCartDTO(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setToken(cart.getToken());
        cartDTO.setItems(mapCartItemsDTO(cart.getItems()));
        return cartDTO;
    }

    public static CartItem toCartItem(CartItemDTO cartItemDTO) {
        if (cartItemDTO == null) {
            return null;
        }
        com.cart_service.entity.CartItem cartItem = new com.cart_service.entity.CartItem();
        cartItem.setId(cartItemDTO.getId());
        cartItem.setProductName(cartItemDTO.getProductName());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(cartItemDTO.getPrice());
        cartItem.setDescription(cartItemDTO.getDescription());
        cartItem.setToken(cartItemDTO.getToken());
        return cartItem;
    }

    public static CartItemDTO toCartItemDTO(com.cart_service.entity.CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setProductName(cartItem.getProductName());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setPrice(cartItem.getPrice());
        cartItemDTO.setDescription(cartItem.getDescription());
        cartItemDTO.setToken(cartItem.getToken());
        return cartItemDTO;
    }

    private static List<com.cart_service.entity.CartItem> mapCartItems(List<CartItemDTO> cartItemDTOs) {
        return Optional.ofNullable(cartItemDTOs)
                .orElseGet(List::of)
                .stream()
                .map(CartMapper::toCartItem)
                .collect(Collectors.toList());
    }

    private static List<CartItemDTO> mapCartItemsDTO(List<com.cart_service.entity.CartItem> cartItems) {
        return Optional.ofNullable(cartItems)
                .orElseGet(List::of)
                .stream()
                .map(CartMapper::toCartItemDTO)
                .collect(Collectors.toList());
    }
}