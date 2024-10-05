package com.ms.friendship.services;

import com.ms.friendship.dtos.FriendRequestDTO;
import com.ms.friendship.models.FriendRequestModel;
import com.ms.friendship.repositories.FriendRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendRequestServiceTest {

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Mock
    private FriendRequestRepository repository;

    @Mock
    private RestTemplate restTemplate;

    private FriendRequestDTO friendRequestDTO;
    private FriendRequestModel friendRequestModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        friendRequestDTO = new FriendRequestDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        friendRequestModel = new FriendRequestModel();
        friendRequestModel.setRequestId(friendRequestDTO.requestId());
        friendRequestModel.setSenderId(friendRequestDTO.senderId());
        friendRequestModel.setReceiverId(friendRequestDTO.receiverId());
        friendRequestModel.setAccepted(false);
    }

    @Test
    public void testSendFriendRequest_Success() {
        when(restTemplate.getForEntity(any(String.class), eq(Void.class))).thenReturn(ResponseEntity.ok().build());
        when(repository.findBySenderIdAndReceiverId(any(), any())).thenReturn(null);

        friendRequestService.sendFriendRequest(friendRequestDTO);

        verify(repository, times(1)).save(any(FriendRequestModel.class));
    }

    @Test
    public void testSendFriendRequest_PlayerNotFound() {
        when(restTemplate.getForEntity(any(String.class), eq(Void.class))).thenReturn(ResponseEntity.notFound().build());

        assertThrows(RuntimeException.class, () -> friendRequestService.sendFriendRequest(friendRequestDTO));
    }

    @Test
    public void testSendFriendRequest_AlreadySent() {
        when(restTemplate.getForEntity(any(String.class), eq(Void.class))).thenReturn(ResponseEntity.ok().build());
        when(repository.findBySenderIdAndReceiverId(any(), any())).thenReturn(friendRequestModel);

        assertThrows(RuntimeException.class, () -> friendRequestService.sendFriendRequest(friendRequestDTO));
    }

    @Test
    public void testAcceptFriendRequest_Success() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(friendRequestModel));

        friendRequestService.acceptFriendRequest(friendRequestDTO);

        verify(repository, times(1)).save(friendRequestModel);
    }

    @Test
    public void testAcceptFriendRequest_RequestNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> friendRequestService.acceptFriendRequest(friendRequestDTO));
    }

    @Test
    public void testAcceptFriendRequest_AlreadyAccepted() {
        friendRequestModel.setAccepted(true);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(friendRequestModel));

        assertThrows(RuntimeException.class, () -> friendRequestService.acceptFriendRequest(friendRequestDTO));
    }
}
