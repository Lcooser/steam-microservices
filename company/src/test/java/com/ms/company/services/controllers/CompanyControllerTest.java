package com.ms.company.controllers;

import com.ms.company.dtos.CompanyRecordDTO;
import com.ms.company.models.CompanyModel;
import com.ms.company.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    private UUID companyId;
    private CompanyModel companyModel;
    private CompanyRecordDTO companyRecordDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        companyId = UUID.randomUUID();
        companyModel = new CompanyModel();
        companyModel.setId(companyId);
        companyModel.setCnpj("12345678000195");
        companyModel.setUserId(UUID.randomUUID());

        companyRecordDTO = new CompanyRecordDTO(
                UUID.randomUUID(),
                "Steam",
                "steam@gmail.com",
                "12345678000195",
                "(11) 91234-5678",
                "Steam ltda"
        );
    }


    @Test
    public void testGetCompanyById_Success() {
        when(companyService.findById(companyId)).thenReturn(Optional.of(companyModel));

        ResponseEntity<CompanyModel> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyModel, response.getBody());
    }

    @Test
    public void testGetCompanyById_NotFound() {
        when(companyService.findById(companyId)).thenReturn(Optional.empty());

        ResponseEntity<CompanyModel> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateCompany_Success() {
        when(companyService.updateCompany(eq(companyId), any(CompanyModel.class))).thenReturn(companyModel);

        ResponseEntity<CompanyModel> response = companyController.updateCompany(companyId, companyRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyModel, response.getBody());
    }

    @Test
    public void testUpdateCompany_NotFound() {
        when(companyService.updateCompany(eq(companyId), any(CompanyModel.class))).thenThrow(new RuntimeException("Company not found"));

        ResponseEntity<CompanyModel> response = companyController.updateCompany(companyId, companyRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteCompany_Success() {
        when(companyService.deleteCompany(companyId)).thenReturn(true);

        ResponseEntity<Void> response = companyController.deleteCompany(companyId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteCompany_NotFound() {
        when(companyService.deleteCompany(companyId)).thenReturn(false);

        ResponseEntity<Void> response = companyController.deleteCompany(companyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
