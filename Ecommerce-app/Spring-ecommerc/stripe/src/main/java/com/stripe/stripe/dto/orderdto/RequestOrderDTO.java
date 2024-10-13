package com.stripe.stripe.dto.orderdto;

import com.stripe.stripe.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequestOrderDTO extends InfoDTO {
    private OrderDTO orderDTO;
}