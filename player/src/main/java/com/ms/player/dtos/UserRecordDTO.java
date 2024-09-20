package com.ms.player.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.UUID;

public record UserRecordDTO(@NotBlank UUID userId,
                            @NotBlank String name,
                            @NotBlank String email,
                            @NotBlank String cpf,
                            @NotBlank String phone,
                            @NotBlank String gender,
                            @NotBlank Date birthDate
                            ) {
}
