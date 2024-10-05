package com.ms.company.services;

import com.ms.company.models.CompanyModel;
import com.ms.company.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    private UUID companyId;
    private CompanyModel companyModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        companyId = UUID.randomUUID();
        companyModel = new CompanyModel();
        companyModel.setId(companyId);
        companyModel.setCnpj("12345678000195");
        companyModel.setUserId(UUID.randomUUID());
    }

    @Test
    public void testProcessCompany() {
        when(companyRepository.save(companyModel)).thenReturn(companyModel);

        CompanyModel savedCompany = companyService.processCompany(companyModel);

        assertEquals(companyModel.getId(), savedCompany.getId());
        verify(companyRepository).save(companyModel);
    }

    @Test
    public void testFindAll() {
        List<CompanyModel> companies = new ArrayList<>();
        companies.add(companyModel);
        when(companyRepository.findAll()).thenReturn(companies);

        List<CompanyModel> result = companyService.findAll();

        assertEquals(1, result.size());
        assertEquals(companyModel, result.get(0));
    }

    @Test
    public void testFindById_Success() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyModel));

        Optional<CompanyModel> result = companyService.findById(companyId);

        assertTrue(result.isPresent());
        assertEquals(companyModel, result.get());
    }

    @Test
    public void testFindById_NotFound() {
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        Optional<CompanyModel> result = companyService.findById(companyId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateCompany_Success() {
        when(companyRepository.existsById(companyId)).thenReturn(true);
        when(companyRepository.save(companyModel)).thenReturn(companyModel);

        CompanyModel updatedCompany = companyService.updateCompany(companyId, companyModel);

        assertEquals(companyId, updatedCompany.getId());
        verify(companyRepository).save(companyModel);
    }

    @Test
    public void testUpdateCompany_NotFound() {
        when(companyRepository.existsById(companyId)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            companyService.updateCompany(companyId, companyModel);
        });

        assertEquals("Company not found with id: " + companyId, thrown.getMessage());
    }

    @Test
    public void testDeleteCompany_Success() {
        when(companyRepository.existsById(companyId)).thenReturn(true);

        boolean result = companyService.deleteCompany(companyId);

        assertTrue(result);
        verify(companyRepository).deleteById(companyId);
    }

    @Test
    public void testDeleteCompany_NotFound() {
        when(companyRepository.existsById(companyId)).thenReturn(false);

        boolean result = companyService.deleteCompany(companyId);

        assertFalse(result);
        verify(companyRepository, never()).deleteById(companyId);
    }
}
