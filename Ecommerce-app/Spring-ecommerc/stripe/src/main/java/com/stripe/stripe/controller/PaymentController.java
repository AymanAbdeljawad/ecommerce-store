package com.stripe.stripe.controller;

import com.stripe.model.checkout.Session;
import com.stripe.stripe.dto.RequestChargeDTO;
import com.stripe.stripe.service.StripeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripeService stripeService;
    private final RedisTemplate<String, RequestChargeDTO> redisTemplate;


    public PaymentController(StripeService stripeService, RedisTemplate<String, RequestChargeDTO> redisTemplate) {
        this.stripeService = stripeService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/charge")
    public ResponseEntity<Map<String, String>> charge(@RequestBody RequestChargeDTO requestChargeDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Session checkoutSession = stripeService.createCheckoutSession(requestChargeDTO, "requestChargeDTO:".concat(UUID.randomUUID().toString()));
            String checkoutUrl = checkoutSession.getUrl();
            response.put("status", "success");
            response.put("message", "Charge created successfully.");
            response.put("url", checkoutUrl);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", "Charge creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/success")
    public ModelAndView success(@RequestParam String uniqueId) {
        RequestChargeDTO requestChargeDTO = redisTemplate.opsForValue().get(uniqueId);
        ModelAndView modelAndView = new ModelAndView();

        if (requestChargeDTO != null) {
            String success = stripeService.updateOrder(requestChargeDTO);
            modelAndView.setViewName("success");
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/cancel");
            return modelAndView;
        }
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "cancel";
    }
}