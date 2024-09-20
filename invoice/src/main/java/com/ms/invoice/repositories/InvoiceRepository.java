package com.ms.invoice.repositories;

import com.ms.invoice.models.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceModel, UUID> {
    List<InvoiceModel> findByOrderId(UUID orderId);
}
