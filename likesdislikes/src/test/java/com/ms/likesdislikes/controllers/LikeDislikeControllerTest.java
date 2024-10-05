package com.ms.likesdislikes.controllers;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.services.LikeDislikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LikeDislikeControllerTest {

    @InjectMocks
    private LikeDislikeController likeDislikeController;

    @Mock
    private LikeDislikeService likeDislikeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addLikeDislike_ShouldReturnCreated() {
        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO(UUID.randomUUID(), UUID.randomUUID(), LikeDislikeDTO.LikeDislikeType.LIKE);

        ResponseEntity<Void> response = likeDislikeController.addLikeDislike(likeDislikeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(likeDislikeService, times(1)).addLikeDislike(likeDislikeDTO);
    }

    @Test
    void getLikes_ShouldReturnLikesCount() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Long mockLikesCount = 10L;
        when(likeDislikeService.countLikes(gameId)).thenReturn(Math.toIntExact(mockLikesCount));

        ResponseEntity<Long> response = likeDislikeController.getLikes(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLikesCount, response.getBody());
    }

    @Test
    void getDislikes_ShouldReturnDislikesCount() {
        UUID gameId = UUID.randomUUID();
        Long mockDislikesCount = 5L;
        when(likeDislikeService.countDislikes(gameId)).thenReturn(Math.toIntExact(mockDislikesCount));

        ResponseEntity<Long> response = likeDislikeController.getDislikes(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDislikesCount, response.getBody());
    }

}
