package com.ms.likesdislikes.repositories;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.models.LikeDislikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeDislikeRepository extends JpaRepository<LikeDislikeModel, UUID> {
    long countByGameIdAndType(UUID gameId, LikeDislikeModel.LikeDislikeType type);
    boolean existsByGameIdAndUserId(UUID gameId, UUID userId);
    int countByGameIdAndType(UUID gameId, LikeDislikeDTO.LikeDislikeType type);

}
