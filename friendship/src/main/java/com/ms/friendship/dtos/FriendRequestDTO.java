package com.ms.friendship.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FriendRequestDTO(
        UUID requestId,
         UUID senderId,
         UUID receiverId
) {
}
