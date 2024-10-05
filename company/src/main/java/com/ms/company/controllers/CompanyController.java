package com.ms.company.controllers;

import com.ms.company.dtos.CompanyRecordDTO;
import com.ms.company.models.CompanyModel;
import com.ms.company.services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyModel>> getAllCompanies() {
        logger.info("Solicitação recebida para obter todas as empresas.");
        List<CompanyModel> companies = companyService.findAll();
        logger.info("Total de empresas encontradas: {}", companies.size());
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable UUID id) {
        logger.info("Solicitação recebida para obter a empresa com ID: {}", id);
        Optional<CompanyModel> company = companyService.findById(id);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Empresa não encontrada com ID: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<CompanyModel> getCompanyByCnpj(@PathVariable String cnpj) {
        logger.info("Solicitação recebida para obter a empresa com CNPJ: {}", cnpj);
        Optional<CompanyModel> company = companyService.findByCnpj(cnpj);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Empresa não encontrada com CNPJ: {}", cnpj);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/userId/{user}")
    public ResponseEntity<CompanyModel> getCompanyUserId(@PathVariable UUID user) {
        logger.info("Solicitação recebida para obter a empresa com ID do usuário: {}", user);
        Optional<CompanyModel> company = companyService.findByUserId(user);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Empresa não encontrada para o usuário com ID: {}", user);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyModel> updateCompany(@PathVariable UUID id, @RequestBody CompanyRecordDTO companyRecordDTO) {
        logger.info("Solicitação recebida para atualizar a empresa com ID: {}", id);
        var companyModel = new CompanyModel();
        BeanUtils.copyProperties(companyRecordDTO, companyModel);

        try {
            CompanyModel updatedCompany = companyService.updateCompany(id, companyModel);
            logger.info("Empresa com ID: {} atualizada com sucesso.", id);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            logger.warn("Falha ao atualizar a empresa com ID: {}. Empresa não encontrada.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        logger.info("Solicitação recebida para deletar a empresa com ID: {}", id);
        boolean deleted = companyService.deleteCompany(id);
        if (deleted) {
            logger.info("Empresa com ID: {} deletada com sucesso.", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Empresa não encontrada para deleção com ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
