package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequestProductIdDTO extends InfoDTO {
    private Long productId;
}
