package com.ms.bank.services;

import com.ms.bank.dtos.BankDTO;
import com.ms.bank.dtos.PaymentMessageDTO;
import com.ms.bank.models.BankModel;
import com.ms.bank.repositories.BankRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Transactional
    public void processPayment(PaymentMessageDTO paymentMessage) {
        System.out.println("Processing payment: " + paymentMessage);
        System.out.println(paymentMessage.getPlayerId());
        System.out.println(paymentMessage.getCompanyId());
        BankModel playerAccount = bankRepository.findByUserId(paymentMessage.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player account not found"));

        System.out.println("Player account found: " + playerAccount);

        if (playerAccount.getBalance() < paymentMessage.getAmount()) {
            throw new RuntimeException("Insufficient balance for playerId: " + paymentMessage.getPlayerId());
        }

        playerAccount.setBalance(playerAccount.getBalance() - paymentMessage.getAmount());
        bankRepository.save(playerAccount);

        BankModel companyAccount = bankRepository.findByUserId(paymentMessage.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company account not found"));

        companyAccount.setBalance(companyAccount.getBalance() + paymentMessage.getAmount());
        bankRepository.save(companyAccount);
    }


    @Transactional
    public void createBankAccount(UUID userId) {
        BankModel bankModel = new BankModel();
        bankModel.setUserId(userId);
        bankModel.setBalance(0.0);
        bankRepository.save(bankModel);
    }

    public BankDTO getAccountByUserId(UUID userId) {
        System.out.println("Fetching account for userId: " + userId);

        BankModel bankModel = bankRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for userId: " + userId));

        BankDTO bankDTO = new BankDTO();
        bankDTO.setUserId(bankModel.getUserId());
        bankDTO.setBalance(bankModel.getBalance());
        return bankDTO;
    }



    @Transactional
    public void addFunds(UUID playerId, double amount) {
        BankModel playerAccount = bankRepository.findByUserId(playerId)
                .orElseThrow(() -> new RuntimeException("Player account not found"));

        playerAccount.setBalance(playerAccount.getBalance() + amount);
        bankRepository.save(playerAccount);
    }



    @Transactional
    public BankDTO getBalance(UUID userId) {
        System.out.println(userId);
        BankModel bankModel = bankRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        BankDTO bankDTO = new BankDTO();
        bankDTO.setUserId(bankModel.getUserId());
        bankDTO.setBalance(bankModel.getBalance());
        return bankDTO;
    }
}
