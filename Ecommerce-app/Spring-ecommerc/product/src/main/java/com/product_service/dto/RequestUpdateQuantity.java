package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class RequestUpdateQuantity extends InfoDTO {
    private List<QuantityDTO> quantityDTOList;
}
