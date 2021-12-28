package com.mukut.demo.controller;

import com.mukut.demo.entity.Post;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.service.PostService;
import com.mukut.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    private PostService postService = new PostService();
    private TagService  tagService =  new TagService();

    @GetMapping("/newpost")
    public String newpost() {
        return "post/newpost";
    }


    @PostMapping("/newpost_submitted")
    public String newpost_submit(@ModelAttribute Post posts, @RequestParam("tags") String tags) {
        Post postInserted = new PostService().save(postRepository, posts);
        tagService.saveEachTag(tagRepository, tags, postInserted.getId(), postInserted.getCreated_at(), postInserted.getUpdated_at());
        return "redirect:/";
    }

    @PostMapping("post_updated")
    public String postUpdate(@ModelAttribute Post post) {
        postService.save(postRepository, post);
        return "redirect:/";
    }

}
