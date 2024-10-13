package com.stripe.stripe.common.mapper;

import com.stripe.stripe.dto.RequestChargeDTO;
import com.stripe.stripe.dto.cartdto.RequestGetCartById;
import com.stripe.stripe.dto.orderdto.OrderDTO;
import com.stripe.stripe.dto.orderdto.RequestOrderDTO;
import com.stripe.stripe.dto.payment.PaymentDTO;
import com.stripe.stripe.dto.productdto.RequestupdateQuantity;

public class DTOConverter {

    public static RequestChargeDTO convertToRequestChargeDTO(RequestOrderDTO requestOrderDTO) {
        if (requestOrderDTO == null) {
            return null;
        }
        PaymentDTO paymentDTO = new PaymentDTO();
        OrderDTO orderDTO = requestOrderDTO.getOrderDTO();

        if (orderDTO != null) {
            paymentDTO.setOdrerId(orderDTO.getOrderId());
            paymentDTO.setToken(orderDTO.getToken());
            paymentDTO.setCartId(orderDTO.getCartId());
            paymentDTO.setTotalPrice(orderDTO.getTotalPrice());
            paymentDTO.setCurrency("USD");
            paymentDTO.setStutseOrderPayment(orderDTO.getStutseOrderPayment());
        }
        RequestChargeDTO requestChargeDTO = new RequestChargeDTO();
        requestChargeDTO.setPaymentDTO(paymentDTO);
        return requestChargeDTO;
    }

    public static RequestOrderDTO convertToRequestOrderDTO(RequestChargeDTO requestChargeDTO) {
        if (requestChargeDTO == null) {
            return null;
        }
        RequestOrderDTO requestOrderDTO = new RequestOrderDTO();
        requestOrderDTO.setClientId(requestChargeDTO.getClientId());
        requestOrderDTO.setErrorCode(requestChargeDTO.getErrorCode());
        requestOrderDTO.setErrorDesc(requestChargeDTO.getErrorDesc());
        requestOrderDTO.setTracingId(requestChargeDTO.getTracingId());
        requestOrderDTO.setToken(requestChargeDTO.getToken());

        PaymentDTO paymentDTO = requestChargeDTO.getPaymentDTO();

        if (paymentDTO != null) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(paymentDTO.getOdrerId());
            orderDTO.setToken(paymentDTO.getToken());
            orderDTO.setCartId(paymentDTO.getCartId());
            orderDTO.setTotalPrice(paymentDTO.getTotalPrice());
            orderDTO.setStutseOrderPayment(paymentDTO.getStutseOrderPayment());
            requestOrderDTO.setOrderDTO(orderDTO);
        }
        return requestOrderDTO;
    }

    public static RequestGetCartById convertToRequestGetCartById(RequestChargeDTO requestChargeDTO) {
        RequestGetCartById requestGetCartById = new RequestGetCartById();
        requestGetCartById.setCartId(Long.valueOf(requestChargeDTO.getPaymentDTO().getCartId()));
        requestGetCartById.setClientId(requestChargeDTO.getClientId());
        requestGetCartById.setToken(requestChargeDTO.getToken());
        requestGetCartById.setTracingId(requestChargeDTO.getTracingId());
        requestGetCartById.setErrorDesc(requestChargeDTO.getErrorDesc());
        requestGetCartById.setErrorCode(requestChargeDTO.getErrorCode());
        return requestGetCartById;
    }

    public static RequestupdateQuantity convertToRequestupdateQuantity(RequestChargeDTO requestChargeDTO) {
        RequestupdateQuantity requestUpdateQuantity = new RequestupdateQuantity();
        requestUpdateQuantity.setToken(requestChargeDTO.getToken());
        requestUpdateQuantity.setClientId(requestChargeDTO.getClientId());
        requestUpdateQuantity.setTracingId(requestChargeDTO.getTracingId());
        requestUpdateQuantity.setErrorCode(requestChargeDTO.getErrorCode());
        requestUpdateQuantity.setErrorDesc(requestChargeDTO.getErrorDesc());
        return requestUpdateQuantity;
    }
}
