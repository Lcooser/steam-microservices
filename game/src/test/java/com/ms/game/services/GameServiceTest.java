package com.ms.game.services;

import com.ms.game.dtos.GameRecordDTO;
import com.ms.game.dtos.LikeDislikeDTO;
import com.ms.game.models.GameModel;
import com.ms.game.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RestTemplate restTemplate;

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
    public void testCreateGame_Success() {
        when(restTemplate.getForEntity(any(String.class), eq(Void.class))).thenReturn(ResponseEntity.ok().build());
        when(gameRepository.save(any(GameModel.class))).thenReturn(gameModel);

        GameModel createdGame = gameService.createGame(gameRecordDTO);

        assertEquals(gameModel, createdGame);
        verify(gameRepository, times(1)).save(any(GameModel.class));
    }

    @Test
    public void testCreateGame_CompanyNotFound() {
        when(restTemplate.getForEntity(any(String.class), eq(Void.class))).thenReturn(ResponseEntity.notFound().build());

        assertThrows(RuntimeException.class, () -> gameService.createGame(gameRecordDTO));
    }

    @Test
    public void testFindGamesByCompanyId() {
        List<GameModel> games = new ArrayList<>();
        games.add(gameModel);
        when(gameRepository.findByCompanyId(any(UUID.class))).thenReturn(games);

        List<GameModel> result = gameService.findGamesByCompanyId(UUID.randomUUID());

        assertEquals(games, result);
    }

    @Test
    public void testFindAllGames() {
        List<GameModel> games = new ArrayList<>();
        games.add(gameModel);
        when(gameRepository.findAll()).thenReturn(games);

        List<GameModel> result = gameService.findAllGames();

        assertEquals(games, result);
    }

    @Test
    public void testFindGameById_Found() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(gameModel));

        Optional<GameModel> result = gameService.findGameById(UUID.randomUUID());

        assertEquals(Optional.of(gameModel), result);
    }

    @Test
    public void testFindGameById_NotFound() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<GameModel> result = gameService.findGameById(UUID.randomUUID());

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateGame_Found() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(gameModel));
        when(gameRepository.save(any(GameModel.class))).thenReturn(gameModel);

        Optional<GameModel> result = gameService.updateGame(UUID.randomUUID(), gameRecordDTO);

        assertEquals(Optional.of(gameModel), result);
        verify(gameRepository, times(1)).save(any(GameModel.class));
    }

    @Test
    public void testUpdateGame_NotFound() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<GameModel> result = gameService.updateGame(UUID.randomUUID(), gameRecordDTO);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testDeleteGame_Success() {
        when(gameRepository.existsById(any(UUID.class))).thenReturn(true);

        boolean result = gameService.deleteGame(UUID.randomUUID());

        assertTrue(result);
        verify(gameRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    public void testDeleteGame_NotFound() {
        when(gameRepository.existsById(any(UUID.class))).thenReturn(false);

        boolean result = gameService.deleteGame(UUID.randomUUID());

        assertFalse(result);
    }

    @Test
    public void testUpdateGameLikesDislikes_Found() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(gameModel));
        when(gameRepository.save(any(GameModel.class))).thenReturn(gameModel);

        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO(UUID.randomUUID(), UUID.randomUUID(), LikeDislikeDTO.LikeDislikeType.LIKE);
        gameService.updateGameLikesDislikes(UUID.randomUUID(), likeDislikeDTO);

        assertEquals(1, gameModel.getLikes());
        verify(gameRepository, times(1)).save(any(GameModel.class));
    }

    @Test
    public void testUpdateGameLikesDislikes_NotFound() {
        when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        LikeDislikeDTO likeDislikeDTO = new LikeDislikeDTO(UUID.randomUUID(), UUID.randomUUID(), LikeDislikeDTO.LikeDislikeType.LIKE);

        assertThrows(RuntimeException.class, () -> gameService.updateGameLikesDislikes(UUID.randomUUID(), likeDislikeDTO));
    }
}
