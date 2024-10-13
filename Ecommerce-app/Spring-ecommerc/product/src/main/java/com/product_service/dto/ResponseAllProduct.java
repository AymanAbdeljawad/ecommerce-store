package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAllProduct extends InfoDTO {
    private List<ProductDTO> productDTO;
    public ResponseAllProduct() {}
    public ResponseAllProduct(Integer clientId, String tracingId, String errorCode, String errorDesc, List<ProductDTO> productDTO) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.productDTO = productDTO;
    }

}