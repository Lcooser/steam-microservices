package com.ms.friendship.controllers;

import com.ms.friendship.dtos.FriendRequestDTO;
import com.ms.friendship.services.FriendRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    private static final Logger logger = LoggerFactory.getLogger(FriendRequestController.class);

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDTO requestDto) {
        logger.info("Recebida solicitação para enviar pedido de amizade: {}", requestDto);
        friendRequestService.sendFriendRequest(requestDto);
        logger.info("Pedido de amizade enviado com sucesso de {} para {}", requestDto.senderId(), requestDto.receiverId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody FriendRequestDTO requestDto) {
        logger.info("Recebida solicitação para aceitar pedido de amizade: {}", requestDto);
        friendRequestService.acceptFriendRequest(requestDto);
        logger.info("Pedido de amizade aceito com sucesso entre {} e {}", requestDto.senderId(), requestDto.receiverId());
        return ResponseEntity.status(HttpStatus.OK).body("Friend request accepted");
    }
}
