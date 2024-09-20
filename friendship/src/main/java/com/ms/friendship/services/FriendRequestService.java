package com.ms.friendship.services;

import com.ms.friendship.dtos.FriendRequestDTO;
import com.ms.friendship.models.FriendRequestModel;
import com.ms.friendship.repositories.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import java.util.UUID;

@Service
public class FriendRequestService {

    private final RestTemplate restTemplate;
    private final FriendRequestRepository repository;

    private static final String PLAYER_SERVICE_URL = "http://localhost:8083/players/playerId/";

    public FriendRequestService(RestTemplate restTemplate, FriendRequestRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    @Transactional
    public void sendFriendRequest(FriendRequestDTO requestDto) {
        verifyPlayerExists(requestDto.senderId());
        verifyPlayerExists(requestDto.receiverId());

        FriendRequestModel existingRequest = repository.findBySenderIdAndReceiverId(requestDto.senderId(), requestDto.receiverId());
        if (existingRequest != null) {
            throw new RuntimeException("Friend request already sent.");
        }

        FriendRequestModel request = new FriendRequestModel();
        request.setSenderId(requestDto.senderId());
        request.setReceiverId(requestDto.receiverId());
        request.setAccepted(false);
        repository.save(request);
    }

    @Transactional
    public void acceptFriendRequest(FriendRequestDTO requestDto) {
        FriendRequestModel request = repository.findById(requestDto.requestId()).orElseThrow(() -> new RuntimeException("Request not found"));
        if (request.isAccepted()) {
            throw new RuntimeException("Friend request already accepted.");
        }

        request.setAccepted(true);
        repository.save(request);

    }

    private void verifyPlayerExists(UUID playerId) {
        ResponseEntity<Void> response = restTemplate.getForEntity(PLAYER_SERVICE_URL + playerId, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
    }
}
