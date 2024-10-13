package com.stripe.stripe.dto.cartdto;

import com.stripe.stripe.common.dto.InfoDTO;
import lombok.Data;

@Data
public class ResponseCartDTO extends InfoDTO {
    private CartDTO cartDTO;

    public ResponseCartDTO() {
    }

    public ResponseCartDTO(CartDTO cartDTO) {
        this.cartDTO = cartDTO;
    }
    public ResponseCartDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, String token) {
        super(clientId, tracingId, errorCode, errorDesc, token);
        this.cartDTO = cartDTO;
    }

    public ResponseCartDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, CartDTO cartDTO) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.cartDTO = cartDTO;
    }

    public ResponseCartDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, String token, CartDTO cartDTO) {
        super(clientId, tracingId, errorCode, errorDesc, token);
        this.cartDTO = cartDTO;
    }

    public CartDTO getCartDTO() {
        return cartDTO;
    }

    public void setCartDTO(CartDTO cartDTO) {
        this.cartDTO = cartDTO;
    }
}
