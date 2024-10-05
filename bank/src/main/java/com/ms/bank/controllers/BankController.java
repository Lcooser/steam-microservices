package com.ms.bank.controllers;

import com.ms.bank.dtos.AddFundsDTO;
import com.ms.bank.dtos.BankDTO;
import com.ms.bank.services.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bank")
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankService bankService;

    @GetMapping("/{userId}")
    public ResponseEntity<BankDTO> getBalance(@PathVariable UUID userId) {
        logger.info("Solicitação recebida para obter o saldo do usuário com ID: {}", userId);
        BankDTO bankDTO = bankService.getBalance(userId);
        logger.info("Saldo obtido com sucesso para o usuário com ID: {}", userId);
        return ResponseEntity.ok(bankDTO);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<BankDTO> getAccountByUserId(@PathVariable UUID userId) {
        logger.info("Solicitação recebida para obter a conta do usuário com ID: {}", userId);
        BankDTO bankDTO = bankService.getAccountByUserId(userId);
        logger.info("Conta obtida com sucesso para o usuário com ID: {}", userId);
        return ResponseEntity.ok(bankDTO);
    }

    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(@RequestBody AddFundsDTO addFundsDTO) {
        logger.info("Recebida solicitação para adicionar fundos. ID do jogador: {}, Quantidade: {}", addFundsDTO.getPlayerId(), addFundsDTO.getAmount());
        bankService.addFunds(addFundsDTO.getPlayerId(), addFundsDTO.getAmount());
        logger.info("Fundos adicionados com sucesso. ID do jogador: {}", addFundsDTO.getPlayerId());
        return ResponseEntity.ok("Funds added successfully.");
    }
}
