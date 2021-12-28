package com.mukut.demo.controller;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class CommentController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    private CommentService commentService = new CommentService();

    @PostMapping("/comments_submitted")
    public String comments(@ModelAttribute("comments") Comment comment) {
        Comment comment_inserted = new CommentService().save(commentRepository, comment);
        return "redirect:/showBlogPost?postId=" + comment.getPostId();
    }

    @PostMapping("showBlogPost/comment_updated")
    public String commentUpdated(@RequestParam(value = "id", required = false) Integer commentId, @RequestParam(value = "comment", required = false) String commentMsg) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        comment.setComment(commentMsg);
        comment.setId(commentId);
        int postId = comment.getPostId();
        Comment commentInserted = new CommentService().update(commentRepository, comment);
        return "redirect:/showBlogPost?postId=" + postId;
    }

    @GetMapping("/showBlogPost/updateComment")
    public String updateComment(@RequestParam("id") int commentId, Model model) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        model.addAttribute("comment", comment);
        return "comment/update_comment";
    }

    @GetMapping("/showBlogPost/deleteComment")
    public String deleteComment(@RequestParam(value = "id", required = false) int id) {
        Optional<Comment> commentResult = commentRepository.findById(id);
        commentService.deleteById(commentRepository ,id);
        return "redirect:/showBlogPost?postId=" + commentResult.get().getPostId();
    }

}
