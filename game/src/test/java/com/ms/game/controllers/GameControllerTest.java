package com.ms.game.controllers;

import com.ms.game.dtos.GameRecordDTO;
import com.ms.game.dtos.LikeDislikeDTO;
import com.ms.game.models.GameModel;
import com.ms.game.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    private GameRecordDTO gameRecordDTO;
    private GameModel gameModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameRecordDTO = new GameRecordDTO("Test Game", "Action", "Test Description", UUID.randomUUID());
        gameModel = new GameModel();
        gameModel.setName(gameRecordDTO.name());
        gameModel.setGenre(gameRecordDTO.genre());
        gameModel.setDescription(gameRecordDTO.description());
        gameModel.setCompanyId(gameRecordDTO.companyId());
    }

    @Test
    public void testCreateGame() {
        when(gameService.createGame(any(GameRecordDTO.class))).thenReturn(gameModel);

        ResponseEntity<GameModel> response = gameController.createGame(gameRecordDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(gameModel, response.getBody());
        verify(gameService, times(1)).createGame(gameRecordDTO);
    }

    @Test
    public void testGetAllGames() {
        List<GameModel> games = new ArrayList<>();
        games.add(gameModel);
        when(gameService.findAllGames()).thenReturn(games);

        ResponseEntity<List<GameModel>> response = gameController.getAllGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, response.getBody());
    }

    @Test
    public void testGetGamesByCompanyId() {
        List<GameModel> games = new ArrayList<>();
        games.add(gameModel);
        when(gameService.findGamesByCompanyId(any(UUID.class))).thenReturn(games);

        ResponseEntity<List<GameModel>> response = gameController.getGamesByCompanyId(UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, response.getBody());
    }

    @Test
    public void testGetGameById_Found() {
        when(gameService.findGameById(any(UUID.class))).thenReturn(Optional.of(gameModel));

        ResponseEntity<GameModel> response = gameController.getGameById(UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameModel, response.getBody());
    }

    @Test
    public void testGetGameById_NotFound() {
        when(gameService.findGameById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<GameModel> response = gameController.getGameById(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateGame_Found() {
        when(gameService.updateGame(any(UUID.class), any(GameRecordDTO.class))).thenReturn(Optional.of(gameModel));

        ResponseEntity<GameModel> response = gameController.updateGame(UUID.randomUUID(), gameRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameModel, response.getBody());
    }

    @Test
    public void testUpdateGame_NotFound() {
        when(gameService.updateGame(any(UUID.class), any(GameRecordDTO.class))).thenReturn(Optional.empty());

        ResponseEntity<GameModel> response = gameController.updateGame(UUID.randomUUID(), gameRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteGame_Success() {
        when(gameService.deleteGame(any(UUID.class))).thenReturn(true);

        ResponseEntity<Void> response = gameController.deleteGame(UUID.randomUUID());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteGame_NotFound() {
        when(gameService.deleteGame(any(UUID.class))).thenReturn(false);

        ResponseEntity<Void> response = gameController.deleteGame(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateLikesDislikes() {
        ResponseEntity<Void> response = gameController.updateLikesDislikes(UUID.randomUUID(), new LikeDislikeDTO(UUID.randomUUID(), UUID.randomUUID(), LikeDislikeDTO.LikeDislikeType.LIKE));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameService, times(1)).updateGameLikesDislikes(any(UUID.class), any(LikeDislikeDTO.class));
    }
}
