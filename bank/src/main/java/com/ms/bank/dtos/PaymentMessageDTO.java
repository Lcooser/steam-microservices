package com.ms.bank.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class PaymentMessageDTO {

    @NotNull
    private UUID playerId;

    @NotNull
    private UUID companyId;

    @NotNull
    private double amount;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
