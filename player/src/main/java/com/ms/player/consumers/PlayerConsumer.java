package com.ms.player.consumers;

import com.ms.player.dtos.PlayerRecordDTO;
import com.ms.player.models.PlayerModel;
import com.ms.player.services.PlayerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PlayerConsumer {

    private final PlayerService playerService;
    private final RabbitTemplate rabbitTemplate;

    private static final String BANK_EXCHANGE = "bank.exchange";
    private static final String BANK_ROUTING_KEY = "bank.create.account";

    public PlayerConsumer(PlayerService playerService, RabbitTemplate rabbitTemplate) {
        this.playerService = playerService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @RabbitListener(queues = "${broker.queue.player.name}")
    public void listenPlayerQueue(@Payload PlayerRecordDTO playerRecordDto) {
        var playerModel  = new PlayerModel();
        BeanUtils.copyProperties(playerRecordDto, playerModel);
        playerService.createPlayer(playerModel);
        rabbitTemplate.convertAndSend(BANK_EXCHANGE, BANK_ROUTING_KEY, playerModel.getUserId());
    }
}
