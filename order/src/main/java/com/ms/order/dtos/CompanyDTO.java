package com.ms.order.dtos;

import java.util.UUID;

public class CompanyDTO {
    private UUID userId;
    private String companyName;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
