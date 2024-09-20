package com.ms.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

public record UserRecordDto(@NotBlank String name,
                            @NotBlank @Email String email,
                            @NotBlank String password,
                            String cpf,
                            String cnpj,
                            @NotBlank String phone,
                            String gender,
                            String socialReason,
                            Date birthDate ) {
}
