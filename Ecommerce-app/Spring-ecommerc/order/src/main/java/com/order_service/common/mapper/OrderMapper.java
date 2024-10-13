package com.order_service.common.mapper;

import com.order_service.dto.cartdto.RequestGetCartById;
import com.order_service.dto.orderdto.OrderDTO;
import com.order_service.dto.orderdto.RequestOrderDTO;
import com.order_service.dto.paymentdto.PaymentDTO;
import com.order_service.dto.paymentdto.RequestChargeDTO;
import com.order_service.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderDTO convertToDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getId());
        orderDTO.setToken(order.getToken());
        orderDTO.setCartId(order.getCartId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setStutseOrderPayment(order.getStutseOrderPayment());
        return orderDTO;
    }

    public static Order convertToEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDTO.getOrderId());
        order.setToken(orderDTO.getToken());
        order.setCartId(orderDTO.getCartId());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setStutseOrderPayment(orderDTO.getStutseOrderPayment());
        return order;
    }

    public static RequestChargeDTO convertToRequestChargeDTO(RequestOrderDTO requestOrderDTO, Double totalPrice) {
        RequestChargeDTO requestChargeDTO = new RequestChargeDTO();
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setCurrency("USD");
        paymentDTO.setTotalPrice(totalPrice);
        paymentDTO.setOdrerId(requestOrderDTO.getOrderDTO().getOrderId());
        paymentDTO.setCartId(requestOrderDTO.getOrderDTO().getCartId());
        paymentDTO.setToken(requestOrderDTO.getToken());
        paymentDTO.setStutseOrderPayment(requestOrderDTO.getOrderDTO().getStutseOrderPayment());

        requestChargeDTO.setToken(requestOrderDTO.getToken());
        requestChargeDTO.setErrorCode(requestOrderDTO.getErrorCode());
        requestChargeDTO.setClientId(requestOrderDTO.getClientId());
        requestChargeDTO.setTracingId(requestOrderDTO.getTracingId());
        requestChargeDTO.setErrorDesc(requestOrderDTO.getErrorDesc());
        requestChargeDTO.setPaymentDTO(paymentDTO);
        return requestChargeDTO;
    }

    public static RequestGetCartById convertToRequestGetCartById(RequestOrderDTO requestOrderDTO) {
        OrderDTO orderDTO = requestOrderDTO.getOrderDTO();
        RequestGetCartById getCartById = new RequestGetCartById();
        getCartById.setCartId(orderDTO.getCartId());
        getCartById.setToken(requestOrderDTO.getToken());
        getCartById.setErrorCode(requestOrderDTO.getErrorCode());
        getCartById.setClientId(requestOrderDTO.getClientId());
        getCartById.setTracingId(requestOrderDTO.getTracingId());
        getCartById.setErrorDesc(requestOrderDTO.getErrorDesc());
        return getCartById;
    }
}
