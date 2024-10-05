package com.ms.comment.controllers;

import com.ms.comment.dtos.CommentDTO;
import com.ms.comment.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "gameExchange";
    private static final String ROUTING_KEY = "comment.added";

    public CommentController(CommentService commentService, RabbitTemplate rabbitTemplate) {
        this.commentService = commentService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentDTO commentDto) {
        logger.info("Recebida solicitação para adicionar comentário: {}", commentDto);

        commentService.saveComment(commentDto);
        logger.info("Comentário salvo com sucesso.");

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, commentDto);
        logger.info("Comentário enviado para a fila com sucesso. Exchange: {}, Routing Key: {}", EXCHANGE, ROUTING_KEY);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
