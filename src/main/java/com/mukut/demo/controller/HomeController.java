package com.mukut.demo.controller;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.entity.User;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import javax.transaction.Transactional;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository ;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("/test")
    public String test(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "v3", required = false) String v3
    ){
        System.out.println(author+"-"+tag+"-"+v3);
        return "redirect:/";
    }


    @GetMapping("/")
    public String getBlogList (
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "publishedAt", required = false) Integer publishedAt,
            @RequestParam(value = "tag", required = false) String tag,
            Model model
    ) {
        List<Post> thePosts = null;
        Set<Post> postByKeywordSearch = null;
        Set<Post> postByTags = null; //filter
        Set<Post> postByAuthor = null; //filter
        Set<Post> postByPubDate = null; //filter
        Set<Post> mergedSet = new HashSet<>(); //merge result
        thePosts = new PostService().findAll(postRepository);

        if (keyword!=null && keyword.length()>1) {
            postByKeywordSearch = postRepository.findByKeyword(keyword);
            mergedSet.addAll(searchTag(keyword));
        }
        if (author!=null && author.length()>1 && (!author.equals("all"))) {
            List<String> authorList = new HelperService().makeListFromCSV(author);
            for(String authorName: authorList){
                System.out.println("'"+authorName+"'");
                postByAuthor = postRepository.findByAuthor(authorName);
                mergedSet.addAll(postByAuthor);
            }
        }

        if ( (start!=null && limit!=null) ) {
            mergedSet.addAll(postRepository.pagination(limit, start));
        }

        if (tag!=null && tag.length()>1 && (!tag.equals("all"))) {
            List<String> tagList = new HelperService().makeListFromCSV(tag);
            for(String tagName: tagList){
                mergedSet.addAll(searchTag(tagName));
            }
        }

        if(postByAuthor!=null){
            mergedSet.addAll(postByAuthor);
        }
        if(postByKeywordSearch!=null){
            mergedSet.addAll(postByKeywordSearch);
        }

        if (mergedSet.isEmpty()){
            model.addAttribute("posts_list", thePosts);
        }
        else{
            model.addAttribute("posts_list", mergedSet);
        }
        return "post/posts_list";
    }



    public Set<Post> searchTag(String tag){
        List<Tag> tags = tagRepository.findByTag(tag);
        Set<Post> tagSet = new LinkedHashSet<>();
        for (Tag eachTag: tags) {
            Optional<Post> optionalPost = postRepository.findById(eachTag.getPostId());
            if(!optionalPost.isEmpty()){
                if(optionalPost.get()!=null)
                    tagSet.add(optionalPost.get());
            }
        }
        return tagSet;
    }


    @GetMapping("/deleteBlogPost")
    public String deleteBlogPost(
            @RequestParam("postId") int postId
    ) {
        postRepository.deleteById(postId);
        return "redirect:/";
    }

    PostService postService = new PostService();

    @GetMapping("/updateBlogPost")
    public String updateBlogPost(
            @RequestParam("postId") int postId,
            Model model
    ) {
        Post post = postService.findPostById(postRepository ,postId);
        //Post post = postRepository.getById(postId);
        model.addAttribute("post", post);
        return "post/update_post";
    }
    @PostMapping("post_updated")
    public String postUpdate(
            @ModelAttribute Post post
    ){
        new PostService().save(postRepository, post);
        return "redirect:/";
    }

    @GetMapping("/showBlogPost/deleteComment")
    public String deleteComment(
            @RequestParam(value="id", required=false) int id
    ) {
        Optional<Comment> commentResult = commentRepository.findById(id);
        commentRepository.deleteById(id);
        return "redirect:/showBlogPost?postId="+commentResult.get().getPostId();
    }

    @GetMapping("/showBlogPost/updateComment")
    public String updateComment(
            @RequestParam("id") int commentId,
            Model model
    ) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        model.addAttribute("comment" ,comment);
        return "comment/update_comment";
    }

    @PostMapping("showBlogPost/comment_updated")
    public String commentUpdated(
            @RequestParam(value = "id", required = false) Integer commentId,
            @RequestParam(value = "comment", required = false) String commentMsg
    ) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        comment.setComment(commentMsg);
        comment.setId(commentId);
        int postId = comment.getPostId();
        Comment commentInserted = new CommentService().update(commentRepository ,comment);
        return "redirect:/showBlogPost?postId="+postId;
    }

    @GetMapping("/newpost")
    public String newpost(){
        return "post/newpost";
    }


    @PostMapping("/newpost_submitted")
    public String newpost_submit(
            @ModelAttribute Post posts,
            @RequestParam("tags") String tags
    ) {
        Post postInserted = new PostService().save(postRepository, posts);
        new TagService().saveEachTag(tagRepository, tags, postInserted.getId(), postInserted.getCreated_at(), postInserted.getUpdated_at());
        return "redirect:/";
    }





    //################################################# for user

    @GetMapping("/register")
    public String index(){
        return "user/register";
    }

    @PostMapping("/register_done")
    public String register(
            @ModelAttribute User user,
            Model model
    ) {
        User userDetails = new UserService().save(userRepository ,user);
        model.addAttribute("message", userDetails.getFname()+" inserted.\n");
        return "welcome";
    }

    //################################################# for retrieve

    @GetMapping("/userslist")
    public String getUserList (
            Model model
    ) {
        List<User> theUsers = new UserService().findAll(userRepository);
        model.addAttribute("users_list", theUsers);
        model.addAttribute("user5", userRepository.findById(5).toString());
        return "user/users_list";
    }

    @GetMapping("/showBlogPost")
    public String showBlogPost(
            @RequestParam("postId") int postId,
            Model postModel,
            Model tagModel,
            Model commentModel
    ) {
        Post post = null;
        try{
            post = new PostService().findPostById(postRepository ,postId);
        }catch (Exception e){
            e.printStackTrace();
        }
        postModel.addAttribute("post", post);

        List<Tag> tagList = new TagService().findByPostId(tagRepository, postId);
        tagModel.addAttribute("tags", tagList);

        List<Comment> commentList = new CommentService().findByPostId(commentRepository, postId);
        commentModel.addAttribute("comments", commentList);

        return "post/show_post";
    }


    @PostMapping("/comments_submitted")
    public String comments(
            @ModelAttribute("comments") Comment comment
    ) {
        Comment comment_inserted = new CommentService().save(commentRepository ,comment);
        return "redirect:/showBlogPost?postId="+comment.getPostId();
    }



//    findByKeyword
//
//    @GetMapping("/comments_submitted")
//    public String comments(
//            @ModelAttribute("comments") Comment comment
//    ) {
//        System.out.println("\n\ngettingFromHTMLForm:\n"+comment+"\n\n");
//        Comment comment_inserted = new CommentService().save(commentRepository ,comment);
//        return "redirect:/showBlogPost?postId="+comment.getPostId();
//    }

}
