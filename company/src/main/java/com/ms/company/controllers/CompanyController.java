package com.ms.company.controllers;

import com.ms.company.dtos.CompanyRecordDTO;
import com.ms.company.models.CompanyModel;
import com.ms.company.services.CompanyService;
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

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyModel>> getAllCompanies() {
        List<CompanyModel> companies = companyService.findAll();
        return ResponseEntity.ok(companies);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable UUID id) {
        Optional<CompanyModel> company = companyService.findById(id);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<CompanyModel> getCompanyByCnpj(@PathVariable String cnpj) {
        Optional<CompanyModel> company = companyService.findByCnpj(cnpj);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/userId/{user}")
    public ResponseEntity<CompanyModel> getCompanyUserId(@PathVariable UUID user) {
        Optional<CompanyModel> company = companyService.findByUserId(user);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyModel> updateCompany(@PathVariable UUID id, @RequestBody CompanyRecordDTO companyRecordDTO) {
        var companyModel = new CompanyModel();
        BeanUtils.copyProperties(companyRecordDTO, companyModel);

        try {
            CompanyModel updatedCompany = companyService.updateCompany(id, companyModel);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        boolean deleted = companyService.deleteCompany(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
