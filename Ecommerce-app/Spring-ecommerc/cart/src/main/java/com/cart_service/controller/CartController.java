package com.cart_service.controller;

import com.cart_service.dto.RequestCartItemDTO;
import com.cart_service.dto.RequestCartItemIdDTO;
import com.cart_service.dto.RequestGetCartById;
import com.cart_service.dto.ResponseCartDTO;
import com.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addItemsToCart")
    public ResponseEntity<ResponseCartDTO> addItemsToCart(@Valid @RequestBody RequestCartItemDTO requestCartItemDTO) {
        ResponseCartDTO responseCartDTO = cartService.addItemsToCart(requestCartItemDTO);
        return new ResponseEntity<>(responseCartDTO, HttpStatus.CREATED);
    }

    @PostMapping("/getItemsFromCartById")
    public ResponseEntity<ResponseCartDTO> getItemsFromCartById(@Valid @RequestBody RequestGetCartById getCartById) {
        ResponseCartDTO cartById = cartService.getItemsFromCartById(getCartById);
        return new ResponseEntity<>(cartById, HttpStatus.OK);
    }

    @PostMapping("/deleteCartByID")
    public ResponseEntity<ResponseCartDTO> deleteCartByID(@Valid @RequestBody RequestGetCartById requestGetCartById) {
        ResponseCartDTO responseCartDTO = cartService.deleteCartByID(requestGetCartById);
        return new ResponseEntity<>(responseCartDTO, HttpStatus.OK);
    }

    @PostMapping("/deleteItemFromCart")
    public ResponseEntity<ResponseCartDTO> deleteItemFromCart(@Valid @RequestBody RequestCartItemIdDTO requestCartItemIdDTO) {
        ResponseCartDTO responseCartDTO = cartService.deleteItemFromCart(requestCartItemIdDTO);
        return new ResponseEntity<>(responseCartDTO, HttpStatus.OK);
    }

}