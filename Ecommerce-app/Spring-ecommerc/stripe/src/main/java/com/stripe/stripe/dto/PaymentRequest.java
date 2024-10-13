package com.stripe.stripe.dto;

import com.stripe.stripe.common.dto.InfoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentRequest implements Serializable {
    private static long SerialVersionID = 1L;

    private Long productId;
    private Integer quantity;

    @Positive(message = "Amount must be positive")
    private int amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be 3 uppercase letters")
    private String currency;

    @Data
    public static class ProductDTO {

        private Long productId;

        @NotBlank(message = "Name is mandatory")
        private String name;

        @Size(max = 500, message = "Description must be less than or equal to 500 characters")
        private String description;

        @NotNull(message = "Price is mandatory")
        @PositiveOrZero(message = "Price must be zero or positive")
        private Double price;

        @NotNull(message = "Quantity is mandatory")
        @Min(value = 0, message = "Quantity must be zero or positive")
        private Integer quantity;

        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Data
    public static class ResponseProduct extends InfoDTO {
        private ProductDTO productDTO;
        public ResponseProduct() {}
        public ResponseProduct(Integer clientId, String tracingId, String errorCode, String errorDesc, ProductDTO productDTO) {
            super(clientId, tracingId, errorCode, errorDesc);
            this.productDTO = productDTO;
        }
        public ResponseProduct(Integer clientId, String tracingId, String errorCode, String errorDesc) {
            super(clientId, tracingId, errorCode, errorDesc);
            this.productDTO = productDTO;
        }

    }

    @Data
    public static class RequestProduct extends InfoDTO {
        private ProductDTO productDTO;
    }
}


