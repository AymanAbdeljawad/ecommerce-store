package com.stripe.stripe.dto.productdto;

import com.stripe.stripe.common.dto.InfoDTO;
import com.stripe.stripe.dto.QuantityDTO;
import lombok.Data;

import java.util.List;

@Data
public class RequestupdateQuantity extends InfoDTO {
    private List<QuantityDTO> quantityDTOList;
}
