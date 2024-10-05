package com.ms.user.controllers;

import com.ms.user.dtos.UserRecordDto;
import com.ms.user.models.UserModel;
import com.ms.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ShouldCreateUser() {
        UserRecordDto userRecordDto = new UserRecordDto("John Doe", "john@example.com", "password", null, null, "123456789", "Male", null, null);
        UserModel userModel = new UserModel();
        userModel.setName("John Doe");

        when(userService.save(any(UserModel.class))).thenReturn(userModel);

        ResponseEntity<UserModel> response = userController.saveUser(userRecordDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userModel, response.getBody());
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        List<UserModel> users = Collections.singletonList(new UserModel());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserModel>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setUserId(userId);

        when(userService.findById(userId)).thenReturn(user);

        ResponseEntity<UserModel> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_ShouldReturnNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<UserModel> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByCpf_ShouldReturnUser() {
        String cpf = "123.456.789-00";
        UserModel user = new UserModel();
        user.setCpf(cpf);

        when(userService.findByCpf(cpf)).thenReturn(user);

        ResponseEntity<UserModel> response = userController.findByCpf(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void findByCpf_ShouldReturnNotFound() {
        String cpf = "123.456.789-00";

        when(userService.findByCpf(cpf)).thenReturn(null);

        ResponseEntity<UserModel> response = userController.findByCpf(cpf);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByCnpj_ShouldReturnUser() {
        String cnpj = "12.345.678/0001-95";
        UserModel user = new UserModel();
        user.setCnpj(cnpj);

        when(userService.findByCnpj(cnpj)).thenReturn(user);

        ResponseEntity<UserModel> response = userController.findByCnpj(cnpj);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void findByCnpj_ShouldReturnNotFound() {
        String cnpj = "12.345.678/0001-95";

        when(userService.findByCnpj(cnpj)).thenReturn(null);

        ResponseEntity<UserModel> response = userController.findByCnpj(cnpj);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void editUser_ShouldReturnUpdatedUser() {
        UUID userId = UUID.randomUUID();
        UserRecordDto userRecordDto = new UserRecordDto("John Doe", "john@example.com", "password", null, null, "123456789", "Male", null, null);
        UserModel updatedUser = new UserModel();
        updatedUser.setUserId(userId);

        when(userService.findById(userId)).thenReturn(updatedUser);
        when(userService.save(any(UserModel.class))).thenReturn(updatedUser);

        ResponseEntity<UserModel> response = userController.editUser(userId, userRecordDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void editUser_ShouldReturnNotFound() {
        UUID userId = UUID.randomUUID();
        UserRecordDto userRecordDto = new UserRecordDto("John Doe", "john@example.com", "password", null, null, "123456789", "Male", null, null);

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<UserModel> response = userController.editUser(userId, userRecordDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        UUID userId = UUID.randomUUID();

        when(userService.delete(userId)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.delete(userId)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
