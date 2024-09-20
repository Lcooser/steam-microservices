package com.ms.company.services;

import com.ms.company.models.CompanyModel;
import com.ms.company.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public CompanyModel processCompany(CompanyModel companyModel) {
        return companyRepository.save(companyModel);
    }

    public List<CompanyModel> findAll() {
        return companyRepository.findAll();
    }

    public Optional<CompanyModel> findById(UUID id) {
        return companyRepository.findById(id);
    }

    public Optional<CompanyModel> findByCnpj(String cnpj) {
        return companyRepository.findByCnpj(cnpj);
    }
    public Optional<CompanyModel> findByUserId(UUID user) {
        return companyRepository.findByUserId(user);
    }


    @Transactional
    public CompanyModel updateCompany(UUID id, CompanyModel companyModel) {
        if (companyRepository.existsById(id)) {
            companyModel.setId(id);
            return companyRepository.save(companyModel);
        }
        throw new RuntimeException("Company not found with id: " + id);
    }

    @Transactional
    public boolean deleteCompany(UUID id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
