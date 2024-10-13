package com.cart_service.service;

import com.cart_service.common.constants.Constants;
import com.cart_service.common.mapper.CartMapper;
import com.cart_service.config.ResourceNotFoundException;
import com.cart_service.dto.*;
import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;
import com.cart_service.repository.CartItemRepository;
import com.cart_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
    }

    public ResponseCartDTO addItemsToCart(RequestCartItemDTO requestCartItemDTO) {
        String token = requestCartItemDTO.getToken();
        List<CartItemDTO> cartItemsDTO = requestCartItemDTO.getCartItemsDTO();
        Cart cart;
        try {
            cart = cartRepository.findByToken(token).orElseGet(() -> cartRepository.save(new Cart(token)));
            for (CartItemDTO dto : cartItemsDTO) {
                CartItem cartItem = CartMapper.toCartItem(dto);
                Optional<CartItem> existingCartItemOpt = cartItemRepository.findByProductNameAndToken(cartItem.getProductName(), token);

                if (existingCartItemOpt.isPresent()) {
                    CartItem existingCartItem = existingCartItemOpt.get();
                    existingCartItem.setQuantity(dto.getQuantity());
                    existingCartItem.setCart(cart);
                    cart.getItems().remove(existingCartItem);
                    cart.getItems().add(existingCartItem);
                } else {
                    cartItem.setCart(cart);
                    cartItem.setToken(token);
                    cart.getItems().add(cartItem);
                }
            }
            Cart savedCart = cartRepository.save(cart);
            CartDTO cartDTO = CartMapper.toCartDTO(savedCart);
            return new ResponseCartDTO(Constants.SUCCESS_CODE, "0", Constants.SUCCESS_MESSAGE, Constants.SUCCESS_MESSAGE, savedCart.getToken(), cartDTO);
        } catch (DataIntegrityViolationException e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", Constants.ERROR_MESSAGE, Constants.DATA_INTEGRITY_VIOLATION_PREFIX + e.getMessage(), token);
        } catch (Exception e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", Constants.ERROR_MESSAGE, Constants.UNEXPECTED_ERROR_PREFIX + e.getMessage(), token);
        }
    }

    public ResponseCartDTO getItemsFromCartById(RequestGetCartById requestGetCartById) {
        String token = requestGetCartById.getToken();
        Long cartId = requestGetCartById.getCartId();
        try {
            CartDTO cartDTO = cartRepository.findByTokenAndId(token, cartId)
                    .map(CartMapper::toCartDTO)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Cart not found with id: %d and token: %s", cartId, token)));
            return new ResponseCartDTO(Constants.SUCCESS_CODE, "0", Constants.SUCCESS_MESSAGE, "Cart retrieved successfully", token, cartDTO);
        } catch (ResourceNotFoundException e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", e.getMessage(), token);
        } catch (Exception e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "An unexpected error occurred: " + e.getMessage(), token
            );
        }
    }

    public ResponseCartDTO deleteCartByID(RequestGetCartById requestGetCartById) {
        Long cartId = requestGetCartById.getCartId();
        String token = requestGetCartById.getToken();
        try {
            if (!cartRepository.existsById(cartId)) {
                return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Cart not found with id: " + cartId, token);
            }
            int deletedItemsCount = cartItemRepository.deleteByCartId(cartId);
            cartRepository.deleteById(cartId);
            String message = deletedItemsCount > 0 ? "Cart and its items deleted successfully" : "Cart deleted successfully but no items were found";
            return new ResponseCartDTO(Constants.SUCCESS_CODE, "0", "Success", message, token);
        } catch (DataAccessException dae) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure",
                    "Database access error: " + dae.getMessage(), token);
        } catch (IllegalArgumentException iae) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Invalid argument: " + iae.getMessage(), token);
        } catch (Exception e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "An unexpected error occurred: " + e.getMessage(), token);
        }
    }

    public ResponseCartDTO deleteItemFromCart(RequestCartItemIdDTO requestCartItemIdDTO) {
        Long cartId = requestCartItemIdDTO.getCartId();
        Long itemId = requestCartItemIdDTO.getItemId();
        String token = requestCartItemIdDTO.getToken();
        try {
            Optional<Cart> cartOptional = cartRepository.findByTokenAndId(token, cartId);
            if (!cartOptional.isPresent()) {
                return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Cart not found with id: " + cartId + " or token error", token);
            }
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
            if (!cartItemOptional.isPresent()) {
                return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Item not found with id: " + itemId, token);
            }
            cartItemRepository.deleteById(itemId);
            boolean hasItemsLeft = cartItemRepository.existsByCartId(cartId);
            if (!hasItemsLeft) {
                cartRepository.deleteById(cartId);
                return new ResponseCartDTO(Constants.SUCCESS_CODE, "0", "Success", "Item removed and cart deleted successfully", token);
            }
            return new ResponseCartDTO(Constants.SUCCESS_CODE, "0", "Success", "Item removed from cart successfully", token);
        } catch (DataAccessException dae) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Database access error: " + dae.getMessage(), token);
        } catch (IllegalArgumentException iae) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "Invalid argument: " + iae.getMessage(), token);
        } catch (Exception e) {
            return new ResponseCartDTO(Constants.ERROR_CODE, "1", "Failure", "An unexpected error occurred: " + e.getMessage(), token);
        }
    }

}