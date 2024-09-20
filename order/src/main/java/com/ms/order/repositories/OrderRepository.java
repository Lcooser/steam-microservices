package com.ms.order.repositories;

import com.ms.order.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
}
