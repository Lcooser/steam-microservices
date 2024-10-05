package com.ms.bank.controllers;

import com.ms.bank.dtos.AddFundsDTO;
import com.ms.bank.dtos.BankDTO;
import com.ms.bank.services.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankControllerTest {

    @InjectMocks
    private BankController bankController;

    @Mock
    private BankService bankService;

    private UUID userId;
    private double initialBalance;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        initialBalance = 100.0;
    }

    @Test
    public void testGetBalance_Success() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setUserId(userId);
        bankDTO.setBalance(initialBalance);

        when(bankService.getBalance(userId)).thenReturn(bankDTO);

        ResponseEntity<BankDTO> response = bankController.getBalance(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankDTO, response.getBody());
        verify(bankService).getBalance(userId);
    }

    @Test
    public void testGetAccountByUserId_Success() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setUserId(userId);
        bankDTO.setBalance(initialBalance);

        when(bankService.getAccountByUserId(userId)).thenReturn(bankDTO);

        ResponseEntity<BankDTO> response = bankController.getAccountByUserId(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankDTO, response.getBody());
        verify(bankService).getAccountByUserId(userId);
    }

    @Test
    public void testAddFunds_Success() {
        AddFundsDTO addFundsDTO = new AddFundsDTO();
        addFundsDTO.setPlayerId(userId);
        addFundsDTO.setAmount(50.0);

        ResponseEntity<String> response = bankController.addFunds(addFundsDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Funds added successfully.", response.getBody());
        verify(bankService).addFunds(userId, 50.0);
    }

    @Test
    public void testGetBalance_AccountNotFound() {
        when(bankService.getBalance(userId)).thenThrow(new RuntimeException("Account not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankController.getBalance(userId);
        });

        assertEquals("Account not found", thrown.getMessage());
    }

    @Test
    public void testGetAccountByUserId_AccountNotFound() {
        when(bankService.getAccountByUserId(userId)).thenThrow(new RuntimeException("Account not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankController.getAccountByUserId(userId);
        });

        assertEquals("Account not found", thrown.getMessage());
    }

    @Test
    public void testAddFunds_PlayerNotFound() {
        AddFundsDTO addFundsDTO = new AddFundsDTO();
        addFundsDTO.setPlayerId(userId);
        addFundsDTO.setAmount(50.0);

        doThrow(new RuntimeException("Player account not found")).when(bankService).addFunds(userId, 50.0);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankController.addFunds(addFundsDTO);
        });

        assertEquals("Player account not found", thrown.getMessage());
    }
}
