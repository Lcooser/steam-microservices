package com.ms.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class CompanyDTO {

    private UUID userId;
    private String name;
    private String email;
    private String cnpj;
    private String phone;
    private String socialReason;

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public void setName(String name) {
        this.name = name;
    }
}
