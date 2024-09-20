package com.ms.invoice.services;

import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public void save(InvoiceModel invoice) {
        invoiceRepository.save(invoice);
    }

    public List<InvoiceModel> findAll() {
        return invoiceRepository.findAll();
    }

    public Optional<InvoiceModel> findById(UUID id) {
        return invoiceRepository.findById(id);
    }

    public List<InvoiceModel> findByOrderId(UUID orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }
}
