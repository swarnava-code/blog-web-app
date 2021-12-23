package com.mukut.demo.service;

import com.mukut.demo.entity.Post;
import com.mukut.demo.repo.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    final int EXCERPT_LIMIT = 20;
    public Post save(PostRepository postsRepository, Post posts){
        String timestamp = new HelperService().makeDataAndTime();
        String excerpt = new HelperService().makeExcerpt(posts.getContent(), EXCERPT_LIMIT);
        posts.setCreated_at(timestamp);
        posts.setUpdated_at(timestamp);
        posts.setPublished_at(timestamp);
        posts.setExcerpt(excerpt);
        Post postsInsertInfo = postsRepository.save(posts);
        return postsInsertInfo;
    }

    public List<Post> findAll(PostRepository postsRepository){
        return postsRepository.findAll();
    }

    public Post findPostById(PostRepository postRepository, int id){
        Optional<Post> optionalResult = postRepository.findById(id);
        Post post = null;

        if (optionalResult.isPresent()) {
            post = optionalResult.get();
        }
        else {
            throw new RuntimeException("Did not find post id - " + id);
        }
        return post;
    }

//    public static String makeDataAndTime() {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        return dateTimeFormatter.format(now);
//    }
//    public static String makeExcerpt(String content, int EXCERPT_LIMIT) {
//        String excerpt = content.substring(0, Math.min(content.length(), EXCERPT_LIMIT))+"...";
//        return excerpt;
//    }


}
