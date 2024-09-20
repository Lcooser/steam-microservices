package com.ms.likesdislikes.controllers;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.services.LikeDislikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/likes-dislikes")
public class LikeDislikeController {

    private final LikeDislikeService likeDislikeService;

    public LikeDislikeController(LikeDislikeService likeDislikeService) {
        this.likeDislikeService = likeDislikeService;
    }

    @PostMapping
    public ResponseEntity<Void> addLikeDislike(@RequestBody LikeDislikeDTO likeDislikeDTO) {
        likeDislikeService.addLikeDislike(likeDislikeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/likes/{gameId}")
    public ResponseEntity<Long> getLikes(@PathVariable UUID gameId) {
        long likes = likeDislikeService.countLikes(gameId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/dislikes/{gameId}")
    public ResponseEntity<Long> getDislikes(@PathVariable UUID gameId) {
        long dislikes = likeDislikeService.countDislikes(gameId);
        return ResponseEntity.ok(dislikes);
    }
}
