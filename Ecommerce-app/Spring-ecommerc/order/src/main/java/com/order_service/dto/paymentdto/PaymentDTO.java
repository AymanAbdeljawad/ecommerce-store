package com.order_service.dto.paymentdto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long odrerId;
    private String token;
    private String cartId;
    private Double totalPrice;
    private String currency;
    private Integer stutseOrderPayment;
}
