package com.order_service.controller;

import com.order_service.dto.orderdto.*;
import com.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<ResponseOrderDTO> createOrder(@Valid @RequestBody RequestOrderDTO requestOrderDTO) {
        ResponseOrderDTO order = orderService.createOrder(requestOrderDTO);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @PostMapping("/getall")
    public ResponseEntity<ResponseAllOrderDTO> getAllOrders(@Valid @RequestBody RequestAllOrderDTO requestOrderDTO) {
        ResponseAllOrderDTO allOrders = orderService.getAllOrders(requestOrderDTO);
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @PostMapping("/getbyid")
    public ResponseEntity<ResponseOrderDTO> getOrderById(@Valid @RequestBody RequestGetOrderByIdDTO requeestByIdOrderDTO) {
        ResponseOrderDTO orderById = orderService.getOrderById(requeestByIdOrderDTO);
        return new ResponseEntity<>(orderById, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseOrderDTO> updateOrder(@Valid @RequestBody RequestOrderDTO requestOrderDTO) {
        ResponseOrderDTO order = orderService.updateOrder(requestOrderDTO);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<ResponseOrderDTO> updatePaymentStatus(@Valid @RequestBody RequestOrderDTO requestOrderDTO) {
        ResponseOrderDTO order = orderService.updatePaymentStatus(requestOrderDTO);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @PostMapping("/delete")
    public ResponseEntity<ResponseOrderDTO> deleteOrder(@Valid @RequestBody RequestGetOrderByIdDTO requeestByIdOrderDTO) {
        ResponseOrderDTO responseOrderDTO = orderService.deleteOrder(requeestByIdOrderDTO);
        return new ResponseEntity<>(responseOrderDTO, HttpStatus.OK);
    }
}
