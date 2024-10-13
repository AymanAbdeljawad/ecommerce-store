package com.stripe.stripe.service;

import com.google.gson.Gson;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.stripe.common.constants.ConstantsURL;
import com.stripe.stripe.common.mapper.DTOConverter;
import com.stripe.stripe.dto.PaymentRequest;
import com.stripe.stripe.dto.QuantityDTO;
import com.stripe.stripe.dto.RequestChargeDTO;
import com.stripe.stripe.dto.cartdto.CartDTO;
import com.stripe.stripe.dto.cartdto.CartItemDTO;
import com.stripe.stripe.dto.cartdto.RequestGetCartById;
import com.stripe.stripe.dto.cartdto.ResponseCartDTO;
import com.stripe.stripe.dto.orderdto.RequestOrderDTO;
import com.stripe.stripe.dto.payment.PaymentDTO;
import com.stripe.stripe.dto.productdto.RequestupdateQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StripeService {
    private final String stripeSecretKey;
    private final Gson gson = new Gson();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, RequestChargeDTO> redisTemplate;

    public StripeService(@Value("${stripe.secret.key}") String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
        com.stripe.Stripe.apiKey = stripeSecretKey;
    }


//    @KafkaListener(topics = "my-topic", groupId = "payment-group")
//    public void
//    consume(String message) {
//        String json = message;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            RequestChargeDTO requestChargeDTO = objectMapper.readValue(json, RequestChargeDTO.class);
//            String uniqueId = "requestChargeDTO:".concat(UUID.randomUUID().toString());
//            Session session = createCheckoutSession(requestChargeDTO, uniqueId);
//            redisTemplate.opsForValue().set(uniqueId, requestChargeDTO);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public Session createCheckoutSession(RequestChargeDTO requestChargeDTO, String uniqueId) throws StripeException {
        RequestOrderDTO requestOrderDTO = DTOConverter.convertToRequestOrderDTO(requestChargeDTO);
            ResponseEntity<RequestOrderDTO> requestOrderDTOResponseEntity = restTemplate.postForEntity(ConstantsURL.ORDER_CREATE_ORDER, requestOrderDTO, RequestOrderDTO.class);
            redisTemplate.opsForValue().set(uniqueId, requestChargeDTO);
        PaymentDTO paymentDTO = requestChargeDTO.getPaymentDTO();
        String successUrl = "http://localhost:8086/stripe/api/payment/success?uniqueId=" + uniqueId;
        SessionCreateParams sessionParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(paymentDTO.getCurrency())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Product")
                                        .build())
                                .setUnitAmount((long) (paymentDTO.getTotalPrice() * 100))
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl("http://localhost:8086/stripe/api/payment/cancel")
                .build();
        return Session.create(sessionParams);
    }

    public String updateOrder(RequestChargeDTO requestChargeDTO) {
        try {
            CartDTO cartDTO = fetchCartItems(requestChargeDTO);
            List<CartItemDTO> items = cartDTO.getItems();
            RequestupdateQuantity requestUpdateQuantity = createUpdateQuantityRequest(requestChargeDTO, items);
            updateProductQuantities(requestUpdateQuantity);
            String success = updatePaymentStatus(requestChargeDTO);
            return "success".equals(success) ? "success" : "failed";
        } catch (Exception e) {
            throw new RuntimeException("Order update failed", e);
        }
    }

    private CartDTO fetchCartItems(RequestChargeDTO requestChargeDTO) {
        RequestGetCartById requestGetCartById = DTOConverter.convertToRequestGetCartById(requestChargeDTO);
        ResponseEntity<ResponseCartDTO> response = restTemplate.postForEntity(ConstantsURL.CART_GET_ITEMS_FROM_CART_BYID, requestGetCartById, ResponseCartDTO.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to retrieve cart items");
        }
        return response.getBody().getCartDTO();
    }

    private RequestupdateQuantity createUpdateQuantityRequest(RequestChargeDTO requestChargeDTO, List<CartItemDTO> items) {
        RequestupdateQuantity requestUpdateQuantity = DTOConverter.convertToRequestupdateQuantity(requestChargeDTO);
        List<QuantityDTO> quantityDTOList = items.stream()
                .map(item -> {
                    QuantityDTO quantityDTO = new QuantityDTO();
                    quantityDTO.setQuantity(item.getQuantity());
                    quantityDTO.setProductName(item.getProductName());
                    return quantityDTO;
                }).collect(Collectors.toList());
        requestUpdateQuantity.setQuantityDTOList(quantityDTOList);
        return requestUpdateQuantity;
    }

    private void updateProductQuantities(RequestupdateQuantity requestUpdateQuantity) {
        ResponseEntity<PaymentRequest.RequestProduct> response = restTemplate.postForEntity(ConstantsURL.PRODUCTS_UPDATE_QUANTITY_LIST, requestUpdateQuantity, PaymentRequest.RequestProduct.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update product quantities");
        }
    }

    private String updatePaymentStatus(RequestChargeDTO requestChargeDTO) {
        RequestOrderDTO requestOrderDTO = DTOConverter.convertToRequestOrderDTO(requestChargeDTO);
        ResponseEntity<RequestChargeDTO> response = restTemplate.postForEntity(ConstantsURL.ORDER_UPDATE_PAYMENT_STATUS, requestOrderDTO, RequestChargeDTO.class);
        return "Success to update payment status";
    }
}