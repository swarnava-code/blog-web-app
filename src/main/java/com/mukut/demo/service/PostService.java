package com.mukut.demo.service;

import com.mukut.demo.entity.Post;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {
    final int EXCERPT_LIMIT = 20;

    public Post save(PostRepository postsRepository, Post posts) {
        String timestamp = new HelperUtil().makeDataAndTime();
        String excerpt = new HelperUtil().makeExcerpt(posts.getContent(), EXCERPT_LIMIT);
        posts.setCreatedAt(timestamp);
        posts.setUpdatedAt(timestamp);
        posts.setPublishedAt(timestamp);
        posts.setExcerpt(excerpt);
        Post postsInsertInfo = postsRepository.save(posts);
        return postsInsertInfo;
    }

    public Post update(PostRepository postsRepository, Post posts) {
        String timestamp = new HelperUtil().makeDataAndTime();
        String excerpt = new HelperUtil().makeExcerpt(posts.getContent(), EXCERPT_LIMIT);
        posts.setUpdatedAt(timestamp);
        posts.setPublishedAt(timestamp);
        posts.setExcerpt(excerpt);
        Post postsInsertInfo = postsRepository.save(posts);
        return postsInsertInfo;
    }

    public Set<Post> findByDate(PostRepository postsRepository, String date){
        date = date.substring(0,10);
        date = date.replaceAll("-", "/");
        return postsRepository.findByDate(date);
    }

    public List<Post> findAll(PostRepository postsRepository) {
        return postsRepository.findAll();
    }

    @Transactional
    public Post findPostById(PostRepository postRepository, int id) {
        Optional<Post> optionalResult = postRepository.findById(id);
        Post post = null;

        if (optionalResult.isPresent()) {
            post = optionalResult.get();
        } else {
            throw new RuntimeException("Did not find post id - " + id);
        }
        return post;
    }

    public Page<Post> findPaginated(PostRepository postRepository ,int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return postRepository.findAll(pageable);
    }

    public Set<Post>  findByKeyword(PostRepository postRepository ,String keyword){
        return postRepository.findByKeyword(keyword);
    }

}
