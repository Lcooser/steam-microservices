package com.ms.likesdislikes.controllers;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.services.LikeDislikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/likes-dislikes")
public class LikeDislikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeDislikeController.class);

    private final LikeDislikeService likeDislikeService;

    public LikeDislikeController(LikeDislikeService likeDislikeService) {
        this.likeDislikeService = likeDislikeService;
    }

    @PostMapping
    public ResponseEntity<Void> addLikeDislike(@RequestBody LikeDislikeDTO likeDislikeDTO) {
        logger.info("Recebida solicitação para adicionar like/dislike: {}", likeDislikeDTO);
        likeDislikeService.addLikeDislike(likeDislikeDTO);
        logger.info("Like/dislike adicionado com sucesso: {}", likeDislikeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/likes/{gameId}")
    public ResponseEntity<Long> getLikes(@PathVariable UUID gameId) {
        logger.info("Solicitação recebida para contar likes do jogo com ID: {}", gameId);
        long likes = likeDislikeService.countLikes(gameId);
        logger.info("Total de likes para o jogo ID {}: {}", gameId, likes);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/dislikes/{gameId}")
    public ResponseEntity<Long> getDislikes(@PathVariable UUID gameId) {
        logger.info("Solicitação recebida para contar dislikes do jogo com ID: {}", gameId);
        long dislikes = likeDislikeService.countDislikes(gameId);
        logger.info("Total de dislikes para o jogo ID {}: {}", gameId, dislikes);
        return ResponseEntity.ok(dislikes);
    }
}
