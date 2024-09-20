package com.ms.comment.repositories;

import com.ms.comment.models.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {
    List<CommentModel> findByGameId(UUID gameId);
}
