package com.ms.comment.services;

import com.ms.comment.dtos.CommentDTO;
import com.ms.comment.models.CommentModel;
import com.ms.comment.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void saveComment(CommentDTO commentDto) {
        CommentModel commentModel = new CommentModel();
        commentModel.setGameId(commentDto.gameId());
        commentModel.setUserId(commentDto.userId());
        commentModel.setMessage(commentDto.message());
        commentRepository.save(commentModel);
    }
}
