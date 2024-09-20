package com.ms.bank.controllers;

import com.ms.bank.dtos.AddFundsDTO;
import com.ms.bank.dtos.BankDTO;
import com.ms.bank.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/{userId}")
    public ResponseEntity<BankDTO> getBalance(@PathVariable UUID userId) {
        BankDTO bankDTO = bankService.getBalance(userId);
        return ResponseEntity.ok(bankDTO);
    }
    @GetMapping("/userId/{userId}")
    public ResponseEntity<BankDTO> getAccountByUserId(@PathVariable UUID userId) {
        BankDTO bankDTO = bankService.getAccountByUserId(userId);
        return ResponseEntity.ok(bankDTO);
    }


    @PostMapping("/add-funds")
    public ResponseEntity<String> addFunds(@RequestBody AddFundsDTO addFundsDTO) {
        bankService.addFunds(addFundsDTO.getPlayerId(), addFundsDTO.getAmount());
        return ResponseEntity.ok("Funds added successfully.");
    }


}
