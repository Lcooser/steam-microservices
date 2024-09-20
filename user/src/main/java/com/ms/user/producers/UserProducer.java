package com.ms.user.producers;

import com.ms.user.dtos.CompanyDTO;
import com.ms.user.dtos.playerDTO;
import com.ms.user.dtos.EmailDto;
import com.ms.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String emailRoutingKey;

    public void publishMessageEmail(UserModel userModel){
        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userModel.getName() + ", seja bem-vindo(a)! \nAgradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", emailRoutingKey, emailDto);
    }

    public void publishMessagePlayer(UserModel userModel) {
        var playerDTO = new playerDTO();
        playerDTO.setUserId(userModel.getUserId());
        playerDTO.setName(userModel.getName());
        playerDTO.setEmail(userModel.getEmail());
        playerDTO.setGender(userModel.getGender());
        playerDTO.setCpf(userModel.getCpf());
        playerDTO.setPhone(userModel.getPhone());
        rabbitTemplate.convertAndSend("user.exchange", "user.player", playerDTO);
    }

    public void publishMessageCompany(UserModel userModel) {
        var companyDTO = new CompanyDTO();
        companyDTO.setUserId(userModel.getUserId());
        companyDTO.setName(userModel.getName());
        companyDTO.setEmail(userModel.getEmail());
        companyDTO.setCnpj(userModel.getCnpj());
        companyDTO.setPhone(userModel.getPhone());
        companyDTO.setSocialReason(userModel.getSocialReason());;
        rabbitTemplate.convertAndSend("user.exchange", "user.company", companyDTO);
    }
}
