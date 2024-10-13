package com.order_service.dto.orderdto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAllOrderDTO extends InfoDTO {
    private List<OrderDTO> orderDTOList;
    public ResponseAllOrderDTO(){}
    public ResponseAllOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, List<OrderDTO> orderDTOList) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.orderDTOList = orderDTOList;
    }
}