package com.ms.likesdislikes.services;

import com.ms.likesdislikes.dtos.LikeDislikeDTO;
import com.ms.likesdislikes.models.LikeDislikeModel;
import com.ms.likesdislikes.repositories.LikeDislikeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeDislikeService {

    private final LikeDislikeRepository likeDislikeRepository;
    private final RestTemplate restTemplate;

    @Value("${game.service.url}")
    private String gameServiceUrl;

    public LikeDislikeService(LikeDislikeRepository likeDislikeRepository, RestTemplate restTemplate) {
        this.likeDislikeRepository = likeDislikeRepository;
        this.restTemplate = restTemplate;
    }

    public void addLikeDislike(LikeDislikeDTO likeDislikeDTO) {
        if (likeDislikeRepository.existsByGameIdAndUserId(likeDislikeDTO.gameId(), likeDislikeDTO.userId())) {
            throw new RuntimeException("User has already voted on this game.");
        }

        LikeDislikeModel likeDislikeModel = new LikeDislikeModel();
        likeDislikeModel.setGameId(likeDislikeDTO.gameId());
        likeDislikeModel.setUserId(likeDislikeDTO.userId());
        likeDislikeModel.setType(likeDislikeDTO.type());

        likeDislikeRepository.save(likeDislikeModel);

        String url = gameServiceUrl + "/games/" + likeDislikeDTO.gameId() + "/update";
        restTemplate.postForEntity(url, likeDislikeDTO, Void.class);
    }
    public int countDislikes(UUID gameId) {
        return likeDislikeRepository.countByGameIdAndType(gameId, LikeDislikeDTO.LikeDislikeType.DISLIKE);
    }

    public int countLikes(UUID gameId) {
        return likeDislikeRepository.countByGameIdAndType(gameId, LikeDislikeDTO.LikeDislikeType.LIKE);
    }


}
