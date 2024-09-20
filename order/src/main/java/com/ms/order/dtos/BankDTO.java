package com.ms.order.dtos;

import java.util.UUID;

public class BankDTO {
    private UUID userId;
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
