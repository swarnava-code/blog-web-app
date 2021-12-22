package com.mukut.demo.service;

import com.mukut.demo.entity.Posts;
import com.mukut.demo.repo.PostsRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    public Posts save(PostsRepository postsRepository, Posts posts){
        Posts postsInsertInfo = postsRepository.save(posts);
        return postsInsertInfo;
    }

    public List<Posts> findAll(PostsRepository postsRepository){
        return postsRepository.findAll();
    }
}
