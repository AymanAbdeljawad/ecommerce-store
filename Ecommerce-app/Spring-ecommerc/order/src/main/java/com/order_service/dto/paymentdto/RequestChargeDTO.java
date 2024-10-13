package com.order_service.dto.paymentdto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequestChargeDTO extends InfoDTO {
    private PaymentDTO paymentDTO;
}
