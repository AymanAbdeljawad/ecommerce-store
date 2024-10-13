package com.order_service.dto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequeestByIDProductDTO extends InfoDTO {
    private Long productId;
}
