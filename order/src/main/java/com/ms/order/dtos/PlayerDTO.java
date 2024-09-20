package com.ms.order.dtos;

import java.util.UUID;

public class PlayerDTO {
    private UUID userId;
    private String name;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
