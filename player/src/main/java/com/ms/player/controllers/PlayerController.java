package com.ms.player.controllers;

import com.ms.player.dtos.PlayerRecordDTO;
import com.ms.player.models.PlayerModel;
import com.ms.player.services.PlayerService;
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

    @Autowired
    private PlayerService playerService;

    @GetMapping("/playerId/{playerId}")
    public ResponseEntity<PlayerModel> getPlayerById(@PathVariable UUID playerId) {
        PlayerModel player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<PlayerModel> getPlayerByUserId(@PathVariable UUID userId){
        PlayerModel player = playerService.getPlayerByUserId(userId);
        return ResponseEntity.ok(player);
    }

    @GetMapping
    public ResponseEntity<List<PlayerModel>> getAllPlayers() {
        List<PlayerModel> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerModel> updatePlayer(@PathVariable UUID playerId, @RequestBody PlayerRecordDTO playerRecordDTO) {
        var playerModel = new PlayerModel();
        BeanUtils.copyProperties(playerRecordDTO, playerModel);

        try {
            PlayerModel updatedPlayer = playerService.updatePlayer(playerId, playerModel);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
