package com.mukut.demo.service;

import com.mukut.demo.entity.Posts;
import com.mukut.demo.repo.PostsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PostService {
    final int EXCERPT_LIMIT = 20;
    public Posts save(PostsRepository postsRepository, Posts posts){
        String timestamp = PostService.makeDataAndTime();
        String content = posts.getContent();
        String excerpt = content.substring(0, Math.min(content.length(), EXCERPT_LIMIT))+"...";
        posts.setCreated_at(timestamp);
        posts.setUpdated_at(timestamp);
        posts.setPublished_at(timestamp);
        posts.setExcerpt(excerpt);
        Posts postsInsertInfo = postsRepository.save(posts);
        return postsInsertInfo;
    }

    public List<Posts> findAll(PostsRepository postsRepository){
        return postsRepository.findAll();
    }

    public static String makeDataAndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }
}
