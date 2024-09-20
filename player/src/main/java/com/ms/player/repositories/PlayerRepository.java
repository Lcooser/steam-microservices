package com.ms.player.repositories;

import com.ms.player.models.PlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<PlayerModel, UUID> {
    Optional<PlayerModel> findByUserId(UUID userId);
}
