package com.ms.comment.controllers;

import com.ms.comment.dtos.CommentDTO;
import com.ms.comment.services.CommentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

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
        commentService.saveComment(commentDto);

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
