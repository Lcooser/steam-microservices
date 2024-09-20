package com.ms.game.repositories;

import com.ms.game.models.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<GameModel, UUID> {

    List<GameModel> findByCompanyId(UUID companyId);
}
