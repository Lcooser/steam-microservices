package com.ms.game.controllers;

import com.ms.game.dtos.GameRecordDTO;
import com.ms.game.dtos.LikeDislikeDTO;
import com.ms.game.models.GameModel;
import com.ms.game.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<GameModel> createGame(@RequestBody GameRecordDTO gameRecordDto) {
        logger.info("Recebida solicitação para criar um novo jogo: {}", gameRecordDto);
        GameModel gameModel = gameService.createGame(gameRecordDto);
        logger.info("Jogo criado com sucesso: {}", gameModel.getGameId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameModel);
    }

    @GetMapping
    public ResponseEntity<List<GameModel>> getAllGames() {
        logger.info("Solicitação recebida para obter todos os jogos.");
        List<GameModel> games = gameService.findAllGames();
        logger.info("Total de jogos encontrados: {}", games.size());
        return ResponseEntity.ok(games);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<GameModel>> getGamesByCompanyId(@PathVariable UUID companyId) {
        logger.info("Solicitação recebida para obter jogos pela empresa ID: {}", companyId);
        List<GameModel> games = gameService.findGamesByCompanyId(companyId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameModel> getGameById(@PathVariable UUID gameId) {
        logger.info("Solicitação recebida para obter jogo com ID: {}", gameId);
        Optional<GameModel> game = gameService.findGameById(gameId);
        return game.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Jogo não encontrado com ID: {}", gameId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameModel> updateGame(@PathVariable UUID gameId, @RequestBody GameRecordDTO gameRecordDto) {
        logger.info("Solicitação recebida para atualizar jogo com ID: {}", gameId);
        Optional<GameModel> updatedGame = gameService.updateGame(gameId, gameRecordDto);
        return updatedGame.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Jogo não encontrado para atualização com ID: {}", gameId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID gameId) {
        logger.info("Solicitação recebida para deletar jogo com ID: {}", gameId);
        if (gameService.deleteGame(gameId)) {
            logger.info("Jogo deletado com sucesso, ID: {}", gameId);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Jogo não encontrado para deleção com ID: {}", gameId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{gameId}/updateLikesDislikes")
    public ResponseEntity<Void> updateLikesDislikes(@PathVariable UUID gameId, @RequestBody LikeDislikeDTO likeDislikeDTO) {
        logger.info("Solicitação recebida para atualizar likes/dislikes do jogo com ID: {}", gameId);
        gameService.updateGameLikesDislikes(gameId, likeDislikeDTO);
        logger.info("Likes/dislikes atualizados com sucesso para o jogo ID: {}", gameId);
        return ResponseEntity.ok().build();
    }
}
