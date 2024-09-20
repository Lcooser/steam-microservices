package com.ms.friendship.controllers;

import com.ms.friendship.dtos.FriendRequestDTO;
import com.ms.friendship.services.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDTO requestDto) {
        friendRequestService.sendFriendRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody FriendRequestDTO requestDto) {
        friendRequestService.acceptFriendRequest(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("Friend request accepted");
    }
}
