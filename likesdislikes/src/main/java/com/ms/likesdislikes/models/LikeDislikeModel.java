package com.ms.likesdislikes.models;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "TB_LIKES_DISLIKES")
public class LikeDislikeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "user_id")
    private UUID userId;

    public LikeDislikeDTO.@NotNull LikeDislikeType getType() {
        return type;
    }

    public void setType(LikeDislikeDTO.@NotNull LikeDislikeType type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Enumerated(EnumType.STRING)
    private LikeDislikeDTO.@NotNull LikeDislikeType type;

    public enum LikeDislikeType {
        LIKE, DISLIKE
    }

}
