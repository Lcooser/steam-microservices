package com.ms.player.services;

import com.ms.player.models.PlayerModel;
import com.ms.player.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public PlayerModel createPlayer(PlayerModel playerModel) {
        return playerRepository.save(playerModel);
    }

    @Transactional
    public PlayerModel getPlayerById(UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @Transactional
    public PlayerModel getPlayerByUserId(UUID userId) {
        return playerRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @Transactional
    public List<PlayerModel> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Transactional
    public PlayerModel updatePlayer(UUID playerId, PlayerModel playerModel) {
        if (playerRepository.existsById(playerId)) {
            playerModel.setPlayerId(playerId);
            return playerRepository.save(playerModel);
        }
        throw new RuntimeException("Player not found with id: " + playerId);
    }

}
