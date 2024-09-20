package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProducer userProducer;

    @Transactional
    public UserModel save(UserModel userModel) {
        userModel = userRepository.save(userModel);

        userProducer.publishMessageEmail(userModel);

        if (userModel.getCpf() != null && !userModel.getCpf().isEmpty()) {
            userProducer.publishMessagePlayer(userModel);
        } else if (userModel.getCnpj() != null && !userModel.getCnpj().isEmpty()) {
            userProducer.publishMessageCompany(userModel);
        }

        return userModel;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UserModel findById(UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public UserModel findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }

    public UserModel findByCnpj(String cnpj) {
        return userRepository.findByCnpj(cnpj);
    }

    @Transactional
    public boolean delete(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
