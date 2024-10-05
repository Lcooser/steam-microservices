package com.ms.friendship.controllers;

import com.ms.friendship.dtos.FriendRequestDTO;
import com.ms.friendship.services.FriendRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FriendRequestControllerTest {

    @InjectMocks
    private FriendRequestController friendRequestController;

    @Mock
    private FriendRequestService friendRequestService;

    private FriendRequestDTO friendRequestDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        friendRequestDTO = new FriendRequestDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testSendFriendRequest() {
        ResponseEntity<String> response = friendRequestController.sendFriendRequest(friendRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Friend request sent", response.getBody());
        verify(friendRequestService, times(1)).sendFriendRequest(friendRequestDTO);
    }

    @Test
    public void testAcceptFriendRequest() {
        ResponseEntity<String> response = friendRequestController.acceptFriendRequest(friendRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Friend request accepted", response.getBody());
        verify(friendRequestService, times(1)).acceptFriendRequest(friendRequestDTO);
    }
}
