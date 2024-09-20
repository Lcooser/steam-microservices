package com.ms.game.consumers;

import com.ms.game.dtos.CommentDTO;
import com.ms.game.services.GameService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class GameConsumer {

    private final GameService gameService;

    public GameConsumer(GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(queues = "${comment.queue.name}")
    public void listenCommentQueue(@Payload CommentDTO commentDto) {
        gameService.addCommentToGame(commentDto.gameId(), commentDto);
    }
}
