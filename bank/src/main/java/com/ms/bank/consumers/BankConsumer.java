package com.ms.bank.consumers;

import com.ms.bank.dtos.PaymentMessageDTO;
import com.ms.bank.services.BankService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankConsumer {

    private final BankService bankService;

    public BankConsumer(BankService bankService) {
        this.bankService = bankService;
    }

    @RabbitListener(queues = "${broker.queue.bank.name}")
    public void listenTransactionQueue(@Payload UUID userId) {
        bankService.createBankAccount(userId);
    }

    @RabbitListener(queues = "bank.transaction")
    public void listenTransactionQueue(@Payload PaymentMessageDTO paymentMessageDTO) {
        System.out.println("Received payment message: " + paymentMessageDTO);
        try {
            bankService.processPayment(paymentMessageDTO);
        } catch (RuntimeException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            throw e;
        }
    }

}
