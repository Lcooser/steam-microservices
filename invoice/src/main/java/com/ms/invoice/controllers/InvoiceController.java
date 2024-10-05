package com.ms.invoice.controllers;

import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.services.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceModel>> getAllInvoices() {
        logger.info("Solicitação recebida para obter todas as faturas.");
        List<InvoiceModel> invoices = invoiceService.findAll();
        logger.info("Total de faturas encontradas: {}", invoices.size());
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceModel> getInvoiceById(@PathVariable UUID id) {
        logger.info("Solicitação recebida para obter fatura com ID: {}", id);
        Optional<InvoiceModel> invoice = invoiceService.findById(id);
        return invoice.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Fatura não encontrada com ID: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<InvoiceModel>> getInvoicesByOrderId(@PathVariable UUID orderId) {
        logger.info("Solicitação recebida para obter faturas com ID do pedido: {}", orderId);
        List<InvoiceModel> invoices = invoiceService.findByOrderId(orderId);
        if (invoices.isEmpty()) {
            logger.warn("Nenhuma fatura encontrada para o pedido com ID: {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Total de faturas encontradas para o pedido com ID: {}", orderId);
        return ResponseEntity.ok(invoices);
    }
}
