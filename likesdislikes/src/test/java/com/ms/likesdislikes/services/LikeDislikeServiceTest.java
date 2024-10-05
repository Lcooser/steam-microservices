package com.ms.likesdislikes.services;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.models.LikeDislikeModel;
import com.ms.likesdislikes.repositories.LikeDislikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LikeDislikeServiceTest {

    @InjectMocks
    private LikeDislikeService likeDislikeService;

    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addLikeDislike_UserAlreadyVoted_ShouldThrowException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO(gameId, userId, LikeDislikeDTO.LikeDislikeType.LIKE);

        when(likeDislikeRepository.existsByGameIdAndUserId(gameId, userId)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> likeDislikeService.addLikeDislike(likeDislikeDTO));
    }

    @Test
    void addLikeDislike_UserNotVoted_ShouldSaveAndUpdate() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO(gameId, userId, LikeDislikeDTO.LikeDislikeType.LIKE);

        when(likeDislikeRepository.existsByGameIdAndUserId(gameId, userId)).thenReturn(false);

        // Act
        likeDislikeService.addLikeDislike(likeDislikeDTO);

        // Assert
        verify(likeDislikeRepository, times(1)).save(any(LikeDislikeModel.class));
    }

    @Test
    void countLikes_ShouldReturnCount() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        int mockLikesCount = 10;
        when(likeDislikeRepository.countByGameIdAndType(gameId, LikeDislikeDTO.LikeDislikeType.LIKE)).thenReturn(mockLikesCount);

        // Act
        int likesCount = likeDislikeService.countLikes(gameId);

        // Assert
        assertEquals(mockLikesCount, likesCount);
    }

    @Test
    void countDislikes_ShouldReturnCount() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        int mockDislikesCount = 5;
        when(likeDislikeRepository.countByGameIdAndType(gameId, LikeDislikeDTO.LikeDislikeType.DISLIKE)).thenReturn(mockDislikesCount);

        // Act
        int dislikesCount = likeDislikeService.countDislikes(gameId);

        // Assert
        assertEquals(mockDislikesCount, dislikesCount);
    }
}
