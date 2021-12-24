package com.mukut.demo.service;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    public Comment save(CommentRepository commentRepository, Comment comment){
        String timestamp = new HelperService().makeDataAndTime();
        comment.setCreatedAt(timestamp);
        comment.setUpdatedAt(timestamp);
        return commentRepository.save(comment);
    }

    public Comment update(CommentRepository commentRepository, Comment comment){
        String timestamp = new HelperService().makeDataAndTime();
        comment.setUpdatedAt(timestamp);
        return commentRepository.save(comment);
    }

    public List<Comment> findByPostId(CommentRepository commentRepository, Integer postId){
        return commentRepository.findByPostId(postId);
    }
}
