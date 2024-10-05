package com.ms.invoice.services;

import com.ms.invoice.models.InvoiceModel;
import com.ms.invoice.repositories.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldInvokeRepositorySave() {
        InvoiceModel invoice = new InvoiceModel();

        invoiceService.save(invoice);

        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void findAll_ShouldReturnListOfInvoices() {
        List<InvoiceModel> mockInvoices = Collections.singletonList(new InvoiceModel());
        when(invoiceRepository.findAll()).thenReturn(mockInvoices);

        List<InvoiceModel> result = invoiceService.findAll();

        assertEquals(mockInvoices, result);
    }

    @Test
    void findById_Exists_ShouldReturnInvoice() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        InvoiceModel mockInvoice = new InvoiceModel();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(mockInvoice));

        Optional<InvoiceModel> result = invoiceService.findById(invoiceId);

        assertEquals(Optional.of(mockInvoice), result);
    }

    @Test
    void findById_NotExists_ShouldReturnEmpty() {
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        Optional<InvoiceModel> result = invoiceService.findById(invoiceId);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void findByOrderId_ShouldReturnListOfInvoices() {
        UUID orderId = UUID.randomUUID();
        List<InvoiceModel> mockInvoices = Collections.singletonList(new InvoiceModel());
        when(invoiceRepository.findByOrderId(orderId)).thenReturn(mockInvoices);

        List<InvoiceModel> result = invoiceService.findByOrderId(orderId);

        assertEquals(mockInvoices, result);
    }
}
