package com.ms.user.controllers;

import com.ms.user.dtos.UserRecordDto;
import com.ms.user.models.UserModel;
import com.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        logger.info("Recebida solicitação para registrar usuário: {}", userRecordDto.email());
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        UserModel user = userService.save(userModel);
        logger.info("Usuário registrado com sucesso. ID: {}", userModel.getUserId());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        logger.info("Solicitação para obter todos os usuários.");
        List<UserModel> users = userService.findAll();
        logger.info("Total de usuários encontrados: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID id) {
        logger.info("Solicitação para obter usuário com ID: {}", id);
        UserModel user = userService.findById(id);
        if (user == null) {
            logger.warn("Usuário não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UserModel> findByCpf(@PathVariable String cpf) {
        logger.info("Solicitação para obter usuário com CPF: {}", cpf);
        UserModel user = userService.findByCpf(cpf);
        if (user == null) {
            logger.warn("Usuário não encontrado com CPF: {}", cpf);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<UserModel> findByCnpj(@PathVariable String cnpj) {
        logger.info("Solicitação para obter usuário com CNPJ: {}", cnpj);
        UserModel user = userService.findByCnpj(cnpj);
        if (user == null) {
            logger.warn("Usuário não encontrado com CNPJ: {}", cnpj);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> editUser(@PathVariable UUID id, @RequestBody @Valid UserRecordDto userRecordDto) {
        logger.info("Solicitação para editar usuário com ID: {}", id);
        UserModel existingUser = userService.findById(id);
        if (existingUser == null) {
            logger.warn("Usuário não encontrado para edição com ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        BeanUtils.copyProperties(userRecordDto, existingUser, "userId");
        UserModel updatedUser = userService.save(existingUser);
        logger.info("Usuário com ID: {} atualizado com sucesso.", updatedUser.getUserId());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        logger.info("Solicitação para deletar usuário com ID: {}", id);
        if (!userService.delete(id)) {
            logger.warn("Usuário não encontrado para deleção com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Usuário com ID: {} deletado com sucesso.", id);
        return ResponseEntity.noContent().build();
    }
}
