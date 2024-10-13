package com.stripe.stripe.dto;

import com.stripe.stripe.common.dto.InfoDTO;
import com.stripe.stripe.dto.payment.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestChargeDTO extends InfoDTO {
    private PaymentDTO paymentDTO;
}
