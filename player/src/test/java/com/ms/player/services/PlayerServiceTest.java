package com.ms.player.services;

import com.ms.player.models.PlayerModel;
import com.ms.player.repositories.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayer_ShouldSavePlayer() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setPlayerId(UUID.randomUUID());

        when(playerRepository.save(any(PlayerModel.class))).thenReturn(playerModel);

        PlayerModel savedPlayer = playerService.createPlayer(playerModel);

        assertEquals(playerModel, savedPlayer);
        verify(playerRepository, times(1)).save(playerModel);
    }

    @Test
    void getPlayerById_ShouldReturnPlayer() {
        UUID playerId = UUID.randomUUID();
        PlayerModel playerModel = new PlayerModel();
        playerModel.setPlayerId(playerId);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(playerModel));

        PlayerModel foundPlayer = playerService.getPlayerById(playerId);

        assertEquals(playerModel, foundPlayer);
    }

    @Test
    void getPlayerById_ShouldThrowExceptionWhenNotFound() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.getPlayerById(playerId));
    }

    @Test
    void getPlayerByUserId_ShouldReturnPlayer() {
        UUID userId = UUID.randomUUID();
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserId(userId);

        when(playerRepository.findByUserId(userId)).thenReturn(Optional.of(playerModel));

        PlayerModel foundPlayer = playerService.getPlayerByUserId(userId);

        assertEquals(playerModel, foundPlayer);
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() {
        when(playerRepository.findAll()).thenReturn(List.of(new PlayerModel()));

        List<PlayerModel> players = playerService.getAllPlayers();

        assertEquals(1, players.size());
    }

    @Test
    void updatePlayer_ShouldUpdatePlayer() {
        UUID playerId = UUID.randomUUID();
        PlayerModel playerModel = new PlayerModel();
        playerModel.setPlayerId(playerId);

        when(playerRepository.existsById(playerId)).thenReturn(true);
        when(playerRepository.save(any(PlayerModel.class))).thenReturn(playerModel);

        PlayerModel updatedPlayer = playerService.updatePlayer(playerId, playerModel);

        assertEquals(playerId, updatedPlayer.getPlayerId());
        verify(playerRepository, times(1)).save(playerModel);
    }

    @Test
    void updatePlayer_ShouldThrowExceptionWhenNotFound() {
        UUID playerId = UUID.randomUUID();
        PlayerModel playerModel = new PlayerModel();

        when(playerRepository.existsById(playerId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> playerService.updatePlayer(playerId, playerModel));
    }
}
