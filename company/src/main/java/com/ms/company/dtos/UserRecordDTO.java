package com.ms.company.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRecordDTO(@NotBlank UUID userId,
                            @NotBlank String name,
                            @NotBlank @Email String email,
                            @NotBlank String cnpj,
                            @NotBlank String phone,
                            @NotBlank String socialReason       ) {
}
