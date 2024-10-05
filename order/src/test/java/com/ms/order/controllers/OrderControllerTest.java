package com.ms.order.controllers;

import com.ms.order.dtos.OrderDTO;
import com.ms.order.models.OrderModel;
import com.ms.order.services.OrderService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnCreated() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID());
        orderDTO.setUserId(UUID.randomUUID());
        orderDTO.setGameId(UUID.randomUUID());
        orderDTO.setCompanyUserId(UUID.randomUUID());
        orderDTO.setAmount(100.0);
        orderDTO.setQuantity(1);
        orderDTO.setPaymentMethod("Credit Card");
        orderDTO.setApproved(true);
        orderDTO.setPaid(false);

        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(orderDTO.getOrderId());

        when(orderService.createOrder(any(OrderModel.class))).thenReturn(orderModel);

        ResponseEntity<OrderModel> response = orderController.createOrder(orderDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderModel, response.getBody());
        verify(orderService, times(1)).createOrder(any(OrderModel.class));
    }

    @Test
    void markAsPaid_ShouldReturnNoContent() {
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderService).markAsPaid(orderId);

        ResponseEntity<Void> response = orderController.markAsPaid(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).markAsPaid(orderId);
    }
}
