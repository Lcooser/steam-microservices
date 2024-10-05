package com.ms.order.controllers;

import com.ms.order.dtos.OrderDTO;
import com.ms.order.models.OrderModel;
import com.ms.order.services.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        logger.info("Recebida solicitação para criar um novo pedido: {}", orderDTO);
        var orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDTO, orderModel);
        OrderModel order = orderService.createOrder(orderModel);
        logger.info("Pedido criado com sucesso. ID: {}", order.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<Void> markAsPaid(@PathVariable UUID id) {
        logger.info("Solicitação recebida para marcar o pedido com ID {} como pago", id);
        orderService.markAsPaid(id);
        logger.info("Pedido com ID {} marcado como pago com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
