package com.ms.bank.repositories;

import com.ms.bank.models.BankModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BankRepository extends JpaRepository<BankModel, UUID> {
    Optional<BankModel> findByUserId(UUID userId);
}
