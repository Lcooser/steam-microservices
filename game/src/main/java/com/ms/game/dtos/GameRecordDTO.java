package com.ms.game.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GameRecordDTO(@NotBlank String name,
                            @NotBlank String genre,
                            @NotBlank String description,
                            @NotNull UUID companyId) {
}
