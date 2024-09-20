package com.ms.invoice.controllers;

import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.services.InvoiceService;
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

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceModel>> getAllInvoices() {
        List<InvoiceModel> invoices = invoiceService.findAll();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceModel> getInvoiceById(@PathVariable UUID id) {
        Optional<InvoiceModel> invoice = invoiceService.findById(id);
        return invoice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<InvoiceModel>> getInvoicesByOrderId(@PathVariable UUID orderId) {
        List<InvoiceModel> invoices = invoiceService.findByOrderId(orderId);
        return invoices.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(invoices);
    }
}
