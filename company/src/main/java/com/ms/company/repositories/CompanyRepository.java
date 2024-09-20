package com.ms.company.repositories;

import com.ms.company.models.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyModel, UUID> {
    Optional<CompanyModel> findByCnpj(String cnpj);
    Optional<CompanyModel> findByUserId(UUID user);
}
