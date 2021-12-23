package com.mukut.demo.service;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.repo.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    public Comment save(CommentRepository commentRepository, Comment comment){
        String timestamp = new HelperService().makeDataAndTime();
        comment.setCreatedAt(timestamp);
        comment.setUpdatedAt(timestamp);
        return commentRepository.save(comment);
    }
}
