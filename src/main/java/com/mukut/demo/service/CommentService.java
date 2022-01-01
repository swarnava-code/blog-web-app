package com.mukut.demo.service;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.util.HelperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    public Comment save(CommentRepository commentRepository, Comment comment) {
        String timestamp = new HelperUtil().makeDataAndTime();
        comment.setCreatedAt(timestamp);
        comment.setUpdatedAt(timestamp);
        return commentRepository.save(comment);
    }

    public Comment update(CommentRepository commentRepository, Comment comment) {
        String timestamp = new HelperUtil().makeDataAndTime();
        comment.setUpdatedAt(timestamp);
        return commentRepository.save(comment);
    }

    public List<Comment> findByPostId(CommentRepository commentRepository, Integer postId) {
        return commentRepository.findByPostId(postId);
    }

    public void deleteById(CommentRepository commentRepository, Integer id) {
        commentRepository.deleteById(id);
    }
}
