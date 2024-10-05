package com.ms.player.controllers;

import com.ms.player.dtos.PlayerRecordDTO;
import com.ms.player.models.PlayerModel;
import com.ms.player.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerControllerTest {

    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPlayerById_ShouldReturnPlayer() {
        UUID playerId = UUID.randomUUID();
        PlayerModel player = new PlayerModel();
        player.setPlayerId(playerId);

        when(playerService.getPlayerById(playerId)).thenReturn(player);

        ResponseEntity<PlayerModel> response = playerController.getPlayerById(playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(player, response.getBody());
    }

    @Test
    void getPlayerByUserId_ShouldReturnPlayer() {
        UUID userId = UUID.randomUUID();
        PlayerModel player = new PlayerModel();
        player.setUserId(userId);

        when(playerService.getPlayerByUserId(userId)).thenReturn(player);

        ResponseEntity<PlayerModel> response = playerController.getPlayerByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(player, response.getBody());
    }

    @Test
    void getAllPlayers_ShouldReturnPlayers() {
        List<PlayerModel> players = Collections.singletonList(new PlayerModel());
        when(playerService.getAllPlayers()).thenReturn(players);

        ResponseEntity<List<PlayerModel>> response = playerController.getAllPlayers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(players, response.getBody());
    }

    @Test
    void updatePlayer_ShouldReturnUpdatedPlayer() {
        UUID playerId = UUID.randomUUID();
        PlayerRecordDTO playerRecordDTO = new PlayerRecordDTO(playerId, "John Doe", "john@example.com", "123456789", "Male", LocalDate.now(), "000.000.000-00");
        PlayerModel updatedPlayer = new PlayerModel();
        updatedPlayer.setPlayerId(playerId);

        when(playerService.updatePlayer(eq(playerId), any(PlayerModel.class))).thenReturn(updatedPlayer);

        ResponseEntity<PlayerModel> response = playerController.updatePlayer(playerId, playerRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPlayer, response.getBody());
    }

    @Test
    void updatePlayer_ShouldReturnNotFound() {
        UUID playerId = UUID.randomUUID();
        PlayerRecordDTO playerRecordDTO = new PlayerRecordDTO(playerId, "John Doe", "john@example.com", "123456789", "Male", LocalDate.now(), "000.000.000-00");

        when(playerService.updatePlayer(eq(playerId), any(PlayerModel.class))).thenThrow(new RuntimeException("Player not found"));

        ResponseEntity<PlayerModel> response = playerController.updatePlayer(playerId, playerRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
