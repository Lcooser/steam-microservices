package com.ms.game.services;

import com.ms.game.dtos.CommentDTO;
import com.ms.game.dtos.GameRecordDTO;
import com.ms.game.dtos.LikeDislikeDTO;
import com.ms.game.models.GameModel;
import com.ms.game.repositories.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String COMPANY_SERVICE_URL = "http://localhost:8086/company/";

    @Transactional
    public GameModel createGame(GameRecordDTO gameDto) {
        ResponseEntity<Void> response = restTemplate.getForEntity(COMPANY_SERVICE_URL + gameDto.companyId(), Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            GameModel gameModel = new GameModel();
            gameModel.setName(gameDto.name());
            gameModel.setGenre(gameDto.genre());
            gameModel.setDescription(gameDto.description());
            gameModel.setCompanyId(gameDto.companyId());

            return gameRepository.save(gameModel);
        } else {
            throw new RuntimeException("Company not found");
        }
    }

    public List<GameModel> findGamesByCompanyId(UUID companyId) {
        return gameRepository.findByCompanyId(companyId);
    }

    public List<GameModel> findAllGames() {
        return gameRepository.findAll();
    }

    public Optional<GameModel> findGameById(UUID gameId) {
        return gameRepository.findById(gameId);
    }

    @Transactional
    public void addCommentToGame(UUID gameId, CommentDTO commentDto) {
        GameModel gameModel = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        gameModel.addComment(commentDto);

        gameRepository.save(gameModel);
    }

    @Transactional
    public Optional<GameModel> updateGame(UUID gameId, GameRecordDTO gameDto) {
        return gameRepository.findById(gameId).map(game -> {
            game.setName(gameDto.name());
            game.setGenre(gameDto.genre());
            game.setDescription(gameDto.description());
            game.setCompanyId(gameDto.companyId());

            return gameRepository.save(game);
        });
    }

    @Transactional
    public void updateGameLikesDislikes(UUID gameId, LikeDislikeDTO likeDislikeDTO) {
        GameModel gameModel = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        if (likeDislikeDTO.type() == LikeDislikeDTO.LikeDislikeType.LIKE) {
            gameModel.setLikes(gameModel.getLikes() + 1);
        } else if (likeDislikeDTO.type() == LikeDislikeDTO.LikeDislikeType.DISLIKE) {
            gameModel.setDislikes(gameModel.getDislikes() + 1);
        }

        gameRepository.save(gameModel);
    }

    public boolean deleteGame(UUID gameId) {
        if (gameRepository.existsById(gameId)) {
            gameRepository.deleteById(gameId);
            return true;
        }
        return false;
    }
}
