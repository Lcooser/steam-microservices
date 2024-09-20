package com.ms.bank.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddFundsDTO {

    @NotNull
    private UUID playerId;

    @NotNull
    private double amount;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
