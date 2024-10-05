package com.ms.invoice.controllers;

import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.services.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @InjectMocks
    private InvoiceController invoiceController;

    @Mock
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllInvoices_ShouldReturnInvoices() {
        List<InvoiceModel> mockInvoices = Collections.singletonList(new InvoiceModel());
        when(invoiceService.findAll()).thenReturn(mockInvoices);

        ResponseEntity<List<InvoiceModel>> response = invoiceController.getAllInvoices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInvoices, response.getBody());
    }

    @Test
    void getInvoiceById_Exists_ShouldReturnInvoice() {
        UUID invoiceId = UUID.randomUUID();
        InvoiceModel mockInvoice = new InvoiceModel();
        when(invoiceService.findById(invoiceId)).thenReturn(Optional.of(mockInvoice));

        ResponseEntity<InvoiceModel> response = invoiceController.getInvoiceById(invoiceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInvoice, response.getBody());
    }

    @Test
    void getInvoiceById_NotExists_ShouldReturnNotFound() {
        UUID invoiceId = UUID.randomUUID();
        when(invoiceService.findById(invoiceId)).thenReturn(Optional.empty());

        ResponseEntity<InvoiceModel> response = invoiceController.getInvoiceById(invoiceId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getInvoicesByOrderId_Exists_ShouldReturnInvoices() {
        UUID orderId = UUID.randomUUID();
        List<InvoiceModel> mockInvoices = Collections.singletonList(new InvoiceModel());
        when(invoiceService.findByOrderId(orderId)).thenReturn(mockInvoices);

        ResponseEntity<List<InvoiceModel>> response = invoiceController.getInvoicesByOrderId(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInvoices, response.getBody());
    }

    @Test
    void getInvoicesByOrderId_NotExists_ShouldReturnNotFound() {
        UUID orderId = UUID.randomUUID();
        when(invoiceService.findByOrderId(orderId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<InvoiceModel>> response = invoiceController.getInvoicesByOrderId(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
