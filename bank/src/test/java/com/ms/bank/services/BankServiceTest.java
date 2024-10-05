package com.ms.bank.services;

import com.ms.bank.dtos.BankDTO;
import com.ms.bank.dtos.PaymentMessageDTO;
import com.ms.bank.models.BankModel;
import com.ms.bank.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankServiceTest {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankRepository bankRepository;

    private UUID playerId;
    private UUID companyId;
    private double initialBalance;
    private double paymentAmount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        initialBalance = 100.0;
        paymentAmount = 50.0;
    }

    @Test
    public void testProcessPayment_Success() {
        PaymentMessageDTO paymentMessage = new PaymentMessageDTO();
        paymentMessage.setPlayerId(playerId);
        paymentMessage.setCompanyId(companyId);
        paymentMessage.setAmount(paymentAmount);

        BankModel playerAccount = new BankModel();
        playerAccount.setUserId(playerId);
        playerAccount.setBalance(initialBalance);

        BankModel companyAccount = new BankModel();
        companyAccount.setUserId(companyId);
        companyAccount.setBalance(0.0);

        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.of(playerAccount));
        when(bankRepository.findByUserId(companyId)).thenReturn(Optional.of(companyAccount));

        bankService.processPayment(paymentMessage);

        assertEquals(initialBalance - paymentAmount, playerAccount.getBalance());
        assertEquals(paymentAmount, companyAccount.getBalance());
        verify(bankRepository, times(2)).save(any(BankModel.class));
    }

    @Test
    public void testProcessPayment_InsufficientBalance() {
        PaymentMessageDTO paymentMessage = new PaymentMessageDTO();
        paymentMessage.setPlayerId(playerId);
        paymentMessage.setCompanyId(companyId);
        paymentMessage.setAmount(paymentAmount);

        BankModel playerAccount = new BankModel();
        playerAccount.setUserId(playerId);
        playerAccount.setBalance(30.0); // Saldo menor que o valor do pagamento

        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.of(playerAccount));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankService.processPayment(paymentMessage);
        });

        assertEquals("Insufficient balance for playerId: " + playerId, thrown.getMessage());
    }

    @Test
    public void testCreateBankAccount_Success() {
        bankService.createBankAccount(playerId);

        ArgumentCaptor<BankModel> bankModelCaptor = ArgumentCaptor.forClass(BankModel.class);
        verify(bankRepository).save(bankModelCaptor.capture());

        BankModel savedModel = bankModelCaptor.getValue();
        assertEquals(playerId, savedModel.getUserId());
        assertEquals(0.0, savedModel.getBalance());
    }

    @Test
    public void testGetAccountByUserId_Success() {
        BankModel bankModel = new BankModel();
        bankModel.setUserId(playerId);
        bankModel.setBalance(initialBalance);

        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.of(bankModel));

        BankDTO bankDTO = bankService.getAccountByUserId(playerId);

        assertEquals(playerId, bankDTO.getUserId());
        assertEquals(initialBalance, bankDTO.getBalance());
    }

    @Test
    public void testGetAccountByUserId_AccountNotFound() {
        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankService.getAccountByUserId(playerId);
        });

        assertEquals("Account not found for userId: " + playerId, thrown.getMessage());
    }

    @Test
    public void testAddFunds_Success() {
        BankModel playerAccount = new BankModel();
        playerAccount.setUserId(playerId);
        playerAccount.setBalance(initialBalance);

        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.of(playerAccount));

        bankService.addFunds(playerId, paymentAmount);

        assertEquals(initialBalance + paymentAmount, playerAccount.getBalance());
        verify(bankRepository).save(playerAccount);
    }

    @Test
    public void testAddFunds_PlayerNotFound() {
        when(bankRepository.findByUserId(playerId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankService.addFunds(playerId, paymentAmount);
        });

        assertEquals("Player account not found", thrown.getMessage());
    }

    @Test
    public void testGetBalance_Success() {
        BankModel bankModel = new BankModel();
        bankModel.setUserId(playerId);
        bankModel.setBalance(initialBalance);

        when(bankRepository.findById(playerId)).thenReturn(Optional.of(bankModel));

        BankDTO bankDTO = bankService.getBalance(playerId);

        assertEquals(playerId, bankDTO.getUserId());
        assertEquals(initialBalance, bankDTO.getBalance());
    }

    @Test
    public void testGetBalance_AccountNotFound() {
        when(bankRepository.findById(playerId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankService.getBalance(playerId);
        });

        assertEquals("Account not found", thrown.getMessage());
    }
}
