package com.ms.likesdislikes.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LikeDislikeDTO(
        @NotNull UUID gameId,
        @NotNull UUID userId,
        @NotNull LikeDislikeType type
) {
    public enum LikeDislikeType {
        LIKE, DISLIKE
    }
}
