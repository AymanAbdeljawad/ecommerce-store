package com.order_service.service;

import com.order_service.common.constants.ConstantsURL;
import com.order_service.common.mapper.OrderMapper;
import com.order_service.dto.cartdto.CartItemDTO;
import com.order_service.dto.cartdto.RequestGetCartById;
import com.order_service.dto.cartdto.ResponseCartDTO;
import com.order_service.dto.orderdto.*;
import com.order_service.dto.paymentdto.PaymentDTO;
import com.order_service.dto.paymentdto.RequestChargeDTO;
import com.order_service.entity.Order;
import com.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, RequestChargeDTO> kafkaTemplate;

    public void sendMessage(RequestChargeDTO requestChargeDTO) {
        kafkaTemplate.send("my-topic", requestChargeDTO);
    }

    private static final String MY_TOPIC = "my-topic";

    /***************************************************************************************************
     ***************************  Start methods related create Order  **********************************
     ***************************************************************************************************/
    public ResponseOrderDTO createOrder(RequestOrderDTO requestOrderDTO) {
        if (requestOrderDTO == null || requestOrderDTO.getOrderDTO().getCartId() == null || requestOrderDTO.getToken() == null) {
            return new ResponseOrderDTO(1, "Error", "Invalid input parameters.", "Invalid input parameters.");
        }
        try {
            OrderDTO orderDTO = requestOrderDTO.getOrderDTO();
            ResponseCartDTO responseCartDTO = getCartItems(requestOrderDTO);
            if (responseCartDTO == null || responseCartDTO.getCartDTO() == null) {
                return new ResponseOrderDTO(1, "Error", "Failed to retrieve cart items.", "Failed to retrieve cart items.");
            }

            Double totalPrice = calculateTotalPrice(responseCartDTO);
            orderDTO.setTotalPrice(totalPrice);
            orderDTO.setStutseOrderPayment(1);

            Order savedOrder = saveOrder(orderDTO);
            if (savedOrder == null) {
                return new ResponseOrderDTO(1, "Error", "Failed to save order.", "Failed to save order.");
            }

            requestOrderDTO.getOrderDTO().setOrderId(savedOrder.getId());

            RequestChargeDTO requestChargeDTO = OrderMapper.convertToRequestChargeDTO(requestOrderDTO, totalPrice);;

//            ListenableFuture<SendResult<String, RequestChargeDTO>> send = kafkaTemplate.send(MY_TOPIC, requestChargeDTO);

            OrderDTO savedOrderDTO = OrderMapper.convertToDTO(savedOrder);
            return new ResponseOrderDTO(0, "Success", "Order created successfully.", "Order created successfully.", savedOrderDTO, null);

        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "An unexpected error occurred. Please try again later.", e.getMessage());
        }
    }


    private ResponseCartDTO getCartItems(RequestOrderDTO requestOrderDTO) {
        RequestGetCartById getCartById = OrderMapper.convertToRequestGetCartById(requestOrderDTO);
        ResponseEntity<ResponseCartDTO> response = restTemplate.postForEntity(ConstantsURL.GET_ITEMS_FROM_CART_BYID, getCartById, ResponseCartDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to retrieve cart items: " + response.getStatusCode());
        }
        return response.getBody();
    }

    private Double  calculateTotalPrice(ResponseCartDTO responseCartDTO) {
        Double totalPrice = 0.0;
        List<CartItemDTO> items = responseCartDTO.getCartDTO().getItems();
        for (CartItemDTO cartItemDTO : items) {
            totalPrice += cartItemDTO.getPrice();
        }
        return totalPrice;
    }

    private Order saveOrder(OrderDTO orderDTO) {
        Order order = OrderMapper.convertToEntity(orderDTO);
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setToken(orderDTO.getToken());
        return orderRepository.save(order);
    }

    /***************************************************************************************************
     ***************************  End methods related create Order  ************************************
     ***************************************************************************************************/

    public ResponseAllOrderDTO getAllOrders(RequestAllOrderDTO requestAllOrderDTO) {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDTO> orderDTOS = orders.stream()
                    .map(OrderMapper::convertToDTO)
                    .collect(Collectors.toList());
            if (orderDTOS.isEmpty()) {
                return new ResponseAllOrderDTO(1, "No Orders Found", "No orders available", "No orders available", Collections.emptyList());
            }
            return new ResponseAllOrderDTO(0, "Success", "Orders retrieved successfully", "Orders retrieved successfully", orderDTOS);
        } catch (Exception e) {
            return new ResponseAllOrderDTO(1, "Error", "Failed to retrieve orders. Please try again later.", "Error occurred", Collections.emptyList());
        }
    }

    public ResponseOrderDTO getOrderById(RequestGetOrderByIdDTO requestByIdOrderDTO) {
        Long orderId = requestByIdOrderDTO.getOrderId();
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                OrderDTO orderDTO = OrderMapper.convertToDTO(orderOptional.get());
                return new ResponseOrderDTO(0, "Success", "Order found successfully", "Order found successfully", orderDTO);
            } else {
                return new ResponseOrderDTO(1, "Not Found", "Order not found with ID " + orderId, "Order not found");
            }
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to retrieve order. Please try again later.", "Error occurred");
        }
    }

    public ResponseOrderDTO updateOrder(RequestOrderDTO requestOrderDTO) {
        Long orderId = requestOrderDTO.getOrderDTO().getOrderId();
        try {
            if (!orderRepository.existsById(orderId)) {
                return new ResponseOrderDTO(1, "Order Not Found", "Order with ID " + orderId + " does not exist.", "Order not found");
            }
            Order order = OrderMapper.convertToEntity(requestOrderDTO.getOrderDTO());
            order.setTotalPrice(requestOrderDTO.getOrderDTO().getTotalPrice());

            Order updatedOrder = orderRepository.save(order);
            OrderDTO updatedOrderDTO = OrderMapper.convertToDTO(updatedOrder);

            return new ResponseOrderDTO(0, "Update Successful", "Order updated successfully.", "Order updated successfully.", updatedOrderDTO);
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to update order. Please try again later.", "Error occurred");
        }
    }

    public ResponseOrderDTO deleteOrder(RequestGetOrderByIdDTO requeestByIdOrderDTO) {
        Long orderId = requeestByIdOrderDTO.getOrderId();
        try {
            if (orderRepository.existsById(orderId)) {
                orderRepository.deleteById(orderId);
                return new ResponseOrderDTO(0, "Success", "Order deleted successfully", "Order deleted successfully");
            } else {
                return new ResponseOrderDTO(1, "Not Found", "Order with ID " + orderId + " not found", "Order not found");
            }
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to delete order. Please try again later.", "Error occurred");
        }
    }

    public ResponseOrderDTO updatePaymentStatus(RequestOrderDTO requestOrderDTO) {
        Long orderId = requestOrderDTO.getOrderDTO().getOrderId();
        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (!optionalOrder.isPresent()) {
                return new ResponseOrderDTO(1, "Not Found", "Order with ID " + orderId + " does not exist.", "Order not found");
            }

            Order existingOrder = optionalOrder.get();
            existingOrder.setStutseOrderPayment(2);
            Order updatedOrder = orderRepository.save(existingOrder);
            OrderDTO updatedOrderDTO = OrderMapper.convertToDTO(updatedOrder);
            return new ResponseOrderDTO(0, "Success", "Order updated successfully.", "Order updated successfully", updatedOrderDTO);
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to update order payment status. Please try again later.", "Error occurred");
        }
    }
}



