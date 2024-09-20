package com.ms.friendship.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

public record PlayerRecordDTO(@NotBlank UUID playerId,
                              @NotBlank UUID userId,
                              @NotBlank String name,
                              @Email String email,
                              @NotBlank String phone,
                              @NotBlank String gender,
                              @NotBlank LocalDate birthDate,
                              @NotBlank String cpf
                              ) {
}