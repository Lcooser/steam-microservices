package com.ms.player.controllers;

import com.ms.player.dtos.PlayerRecordDTO;
import com.ms.player.models.PlayerModel;
import com.ms.player.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @GetMapping("/playerId/{playerId}")
    public ResponseEntity<PlayerModel> getPlayerById(@PathVariable UUID playerId) {
        logger.info("Recebida solicitação para obter jogador com ID: {}", playerId);
        PlayerModel player = playerService.getPlayerById(playerId);
        if (player == null) {
            logger.warn("Jogador não encontrado com ID: {}", playerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Jogador encontrado: {}", player);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<PlayerModel> getPlayerByUserId(@PathVariable UUID userId) {
        logger.info("Recebida solicitação para obter jogador com User ID: {}", userId);
        PlayerModel player = playerService.getPlayerByUserId(userId);
        if (player == null) {
            logger.warn("Jogador não encontrado com User ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Jogador encontrado: {}", player);
        return ResponseEntity.ok(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerModel>> getAllPlayers() {
        logger.info("Solicitação recebida para obter todos os jogadores.");
        List<PlayerModel> players = playerService.getAllPlayers();
        logger.info("Total de jogadores encontrados: {}", players.size());
        return ResponseEntity.ok(players);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerModel> updatePlayer(@PathVariable UUID playerId, @RequestBody PlayerRecordDTO playerRecordDTO) {
        logger.info("Recebida solicitação para atualizar jogador com ID: {}", playerId);
        var playerModel = new PlayerModel();
        BeanUtils.copyProperties(playerRecordDTO, playerModel);

        try {
            PlayerModel updatedPlayer = playerService.updatePlayer(playerId, playerModel);
            logger.info("Jogador com ID: {} atualizado com sucesso.", playerId);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            logger.warn("Erro ao atualizar jogador com ID: {} - {}", playerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
