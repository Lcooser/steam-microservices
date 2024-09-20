package com.ms.order.controllers;

import com.ms.order.dtos.OrderDTO;
import com.ms.order.models.OrderModel;
import com.ms.order.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        var orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDTO, orderModel);
        OrderModel order = orderService.createOrder(orderModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<Void> markAsPaid(@PathVariable UUID id) {
        orderService.markAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}
