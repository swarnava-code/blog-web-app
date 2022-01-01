package com.mukut.demo.controller;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.service.CommentService;
import com.mukut.demo.service.PostService;
import com.mukut.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService = new PostService();

    private CommentService commentService = new CommentService();
    private TagService tagService = new TagService();

    @GetMapping("/deleteBlogPost")
    public String deleteBlogPost(@RequestParam("postId") int postId) {
        postRepository.deleteById(postId);
        return "redirect:/";
    }

    @GetMapping("/updateBlogPost")
    public String updateBlogPost(@RequestParam("postId") int postId, Model model) {
        Post post = postService.findPostById(postRepository, postId);
        model.addAttribute("post", post);
        return "post/update_post";
    }

    @GetMapping("/showBlogPost")
    public String showBlogPost(@RequestParam("postId") int postId, Model postModel, Model tagModel, Model commentModel) {
        Post post = null;
        try {
            post = postService.findPostById(postRepository, postId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        postModel.addAttribute("post", post);

        List<Tag> tagList = tagService.findByPostId(tagRepository, postId);
        tagModel.addAttribute("tags", tagList);

        List<Comment> commentList = commentService.findByPostId(commentRepository, postId);
        commentModel.addAttribute("comments", commentList);

        return "post/show_post";
    }
}
