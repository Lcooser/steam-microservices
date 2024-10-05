package com.ms.order.services;

import com.ms.order.dtos.BankDTO;
import com.ms.order.dtos.CompanyDTO;
import com.ms.order.dtos.OrderDTO;
import com.ms.order.dtos.PlayerDTO;
import com.ms.order.models.OrderModel;
import com.ms.order.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldSaveOrder() {
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(UUID.randomUUID());

        when(orderRepository.save(any(OrderModel.class))).thenReturn(orderModel);

        OrderModel savedOrder = orderService.createOrder(orderModel);

        assertEquals(orderModel, savedOrder);
        verify(orderRepository, times(1)).save(orderModel);
    }

    @Test
    void markAsPaid_ShouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.markAsPaid(orderId));
    }

}
