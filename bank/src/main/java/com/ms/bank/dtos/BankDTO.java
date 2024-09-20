package com.ms.bank.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class BankDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private double balance;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
