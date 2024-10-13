package com.stripe.stripe.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class QuantityDTO implements Serializable {
    private static long SerialVersionID = 1L;
    private String productName;
    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;
}
