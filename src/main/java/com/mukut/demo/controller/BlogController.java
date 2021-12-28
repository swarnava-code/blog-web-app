package com.mukut.demo.controller;

import com.mukut.demo.entity.Post;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    private PostService postService = new PostService();

    @GetMapping("/deleteBlogPost")
    public String deleteBlogPost(@RequestParam("postId") int postId) {
        postRepository.deleteById(postId);
        return "redirect:/";
    }

    @GetMapping("/updateBlogPost")
    public String updateBlogPost(@RequestParam("postId") int postId, Model model) {
        Post post = postService.findPostById(postRepository, postId);
        //Post post = postRepository.getById(postId);
        model.addAttribute("post", post);
        return "post/update_post";
    }

}
