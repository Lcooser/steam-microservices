package com.ms.game.controllers;

import com.ms.game.dtos.GameRecordDTO;
import com.ms.game.dtos.LikeDislikeDTO;
import com.ms.game.models.GameModel;
import com.ms.game.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<GameModel> createGame(@RequestBody GameRecordDTO gameRecordDto) {
        GameModel gameModel = gameService.createGame(gameRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(gameModel);
    }

    @GetMapping
    public ResponseEntity<List<GameModel>> getAllGames() {
        List<GameModel> games = gameService.findAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<GameModel>> getGamesByCompanyId(@PathVariable UUID companyId) {
        List<GameModel> games = gameService.findGamesByCompanyId(companyId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameModel> getGameById(@PathVariable UUID gameId) {
        Optional<GameModel> game = gameService.findGameById(gameId);
        return game.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameModel> updateGame(@PathVariable UUID gameId, @RequestBody GameRecordDTO gameRecordDto) {
        Optional<GameModel> updatedGame = gameService.updateGame(gameId, gameRecordDto);
        return updatedGame.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID gameId) {
        if (gameService.deleteGame(gameId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{gameId}/updateLikesDislikes")
    public ResponseEntity<Void> updateLikesDislikes(@PathVariable UUID gameId, @RequestBody LikeDislikeDTO likeDislikeDTO) {
        gameService.updateGameLikesDislikes(gameId, likeDislikeDTO);
        return ResponseEntity.ok().build();
    }
}
