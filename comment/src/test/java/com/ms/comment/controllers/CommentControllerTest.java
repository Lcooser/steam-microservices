package com.ms.comment.controllers;

import com.ms.comment.dtos.CommentDTO;
import com.ms.comment.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private CommentDTO commentDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commentDto = new CommentDTO(UUID.randomUUID(), UUID.randomUUID(), "This is a comment");
    }

    @Test
    public void testAddComment_Success() {
        ResponseEntity<Void> response = commentController.addComment(commentDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(commentService).saveComment(commentDto);
        verify(rabbitTemplate).convertAndSend("gameExchange", "comment.added", commentDto);
    }

    @Test
    public void testAddComment_InvalidData() {
        CommentDTO invalidCommentDto = new CommentDTO(UUID.randomUUID(), UUID.randomUUID(), "");

        doThrow(new RuntimeException("Validation error")).when(commentService).saveComment(invalidCommentDto);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            commentController.addComment(invalidCommentDto);
        });

        assertEquals("Validation error", thrown.getMessage());
    }
}
