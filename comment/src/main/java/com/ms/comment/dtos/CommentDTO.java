package com.ms.comment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommentDTO(
        @NotNull UUID gameId,
        @NotNull UUID userId,
        @NotBlank String message
) { }
