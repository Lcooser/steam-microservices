package com.ms.order.services;

import com.ms.order.dtos.*;
import com.ms.order.models.OrderModel;
import com.ms.order.repositories.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final String INVOICE_EXCHANGE = "order.exchange";
    private static final String INVOICE_ROUTING_KEY = "order.invoice";
    private static final String BANK_EXCHANGE = "bank.exchange";
    private static final String BANK_ROUTING_KEY = "bank.transaction";

    @Transactional
    public OrderModel createOrder(OrderModel orderModel) {
        return orderRepository.save(orderModel);
    }

    @Transactional
    public void markAsPaid(UUID orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        UUID userId = order.getCompanyUserId();
        PlayerDTO player = getPlayerInfo(order.getUserId());

        CompanyDTO company = getCompanyInfo(userId);

        double totalAmount = order.getQuantity() * order.getAmount();

        if (!hasSufficientBalance(order.getUserId(), totalAmount)) {
            throw new RuntimeException("Insufficient balance for userId: " + userId);
        }

        order.setPaid(true);
        order.setPaymentDate(new Date());
        orderRepository.save(order);

        OrderDTO orderDTO = createOrderDTO(order);

        rabbitTemplate.convertAndSend(INVOICE_EXCHANGE, INVOICE_ROUTING_KEY, orderDTO);

        sendPaymentToBank(player.getUserId(), company.getUserId(), totalAmount);
    }

    private PlayerDTO getPlayerInfo(UUID userId) {
        ResponseEntity<PlayerDTO> response = restTemplate.getForEntity("http://localhost:8083/players/userId/" + userId, PlayerDTO.class);
        return response.getBody();
    }

    private CompanyDTO getCompanyInfo(UUID userId) {
        ResponseEntity<CompanyDTO> response = restTemplate.getForEntity("http://localhost:8086/company/userId/" + userId, CompanyDTO.class);
        return response.getBody();
    }

    private boolean hasSufficientBalance(UUID playerId, double amount) {
        System.out.println(playerId);
        ResponseEntity<BankDTO> response = restTemplate.getForEntity("http://localhost:8080/bank/userId/" + playerId, BankDTO.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error fetching bank info for userId: " + playerId);
        }

        BankDTO bankInfo = response.getBody();

        if (bankInfo == null) {
            throw new RuntimeException("Bank information not found for userId: " + playerId);
        }

        return bankInfo.getBalance() >= amount;
    }


    private void sendPaymentToBank(UUID playerId, UUID companyId, double amount) {
        PaymentMessageDTO paymentMessageDTO = new PaymentMessageDTO();
        paymentMessageDTO.setPlayerId(playerId);
        paymentMessageDTO.setCompanyId(companyId);
        paymentMessageDTO.setAmount(amount);

        rabbitTemplate.convertAndSend(BANK_EXCHANGE, BANK_ROUTING_KEY, paymentMessageDTO);
    }

    private OrderDTO createOrderDTO(OrderModel order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setGameId(order.getGameId());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setApproved(order.getApproved());
        orderDTO.setPaid(order.getPaid());
        orderDTO.setPaymentDate(order.getPaymentDate());
        return orderDTO;
    }
}
