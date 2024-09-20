package com.ms.friendship.repositories;

import com.ms.friendship.models.FriendRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestModel, UUID> {
    FriendRequestModel findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
}
