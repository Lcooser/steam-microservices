package com.ms.invoice.dtos;

import java.util.UUID;

public class GameDTO {

    private String gameName;
    private UUID companyId;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
