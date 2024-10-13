package com.order_service.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "cart_id", nullable = false)
    private String cartId;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "stutse_order_payment", nullable = false)
    private Integer stutseOrderPayment;
}

