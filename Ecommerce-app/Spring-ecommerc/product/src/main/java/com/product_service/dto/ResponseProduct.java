package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class ResponseProduct extends InfoDTO {
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
