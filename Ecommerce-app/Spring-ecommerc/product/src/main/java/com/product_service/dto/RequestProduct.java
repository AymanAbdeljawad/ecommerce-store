package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequestProduct extends InfoDTO {
    private ProductDTO productDTO;
}
