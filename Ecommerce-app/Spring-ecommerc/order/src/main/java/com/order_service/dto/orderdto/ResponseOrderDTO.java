package com.order_service.dto.orderdto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class ResponseOrderDTO extends InfoDTO {
    private OrderDTO orderDTO;
    private String url;
    public ResponseOrderDTO(){}
    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, OrderDTO orderDTO) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.orderDTO = orderDTO;
    }

    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc) {
        super(clientId, tracingId, errorCode, errorDesc);
    }

    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, String token, OrderDTO orderDTO, String url) {
        super(clientId, tracingId, errorCode, errorDesc, token);
        this.orderDTO = orderDTO;
        this.url = url;
    }

    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, OrderDTO orderDTO, String url) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.orderDTO = orderDTO;
        this.url = url;
    }
}
