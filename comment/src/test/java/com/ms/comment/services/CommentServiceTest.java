package com.ms.comment.services;

import com.ms.comment.dtos.CommentDTO;
import com.ms.comment.models.CommentModel;
import com.ms.comment.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private CommentDTO commentDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commentDto = new CommentDTO(UUID.randomUUID(), UUID.randomUUID(), "This is a comment");
    }

    @Test
    public void testSaveComment() {
        commentService.saveComment(commentDto);

        ArgumentCaptor<CommentModel> commentModelCaptor = ArgumentCaptor.forClass(CommentModel.class);
        verify(commentRepository).save(commentModelCaptor.capture());

        CommentModel savedModel = commentModelCaptor.getValue();
        assertEquals(commentDto.gameId(), savedModel.getGameId());
        assertEquals(commentDto.userId(), savedModel.getUserId());
        assertEquals(commentDto.message(), savedModel.getMessage());
    }
}
