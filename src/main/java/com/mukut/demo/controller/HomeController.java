package com.mukut.demo.controller;

import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.entity.User;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.CommentService;
import com.mukut.demo.service.PostService;
import com.mukut.demo.service.TagService;
import com.mukut.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.util.List;

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

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("page", "Welcome to Landing Page");
        return "index";
    }

//    //blog list to include tag
//    @GetMapping("includetag")
//    public String includeTag(Model model) {
//        model.addAttribute("page", "Welcome to Landing Page");
//        return "index";
//    }

    //################################################# for POST

    @GetMapping("/newpost")
    public String newpost(){
        return "post/newpost";
    }


    @PostMapping("/newpost_submitted")
    public String newpost_submit(
            @ModelAttribute Post posts,
            @RequestParam("tags") String tags,
            Model model
    ) {
        Post postInserted = new PostService().save(postRepository, posts);
        new TagService().saveEachTag(tagRepository, tags, postInserted.getId(), postInserted.getCreated_at(), postInserted.getUpdated_at());
        model.addAttribute("heading_message", "Your blog successfully submitted");
        model.addAttribute("message", "Your post, '"+postInserted.getTitle()+"' with the tag : ("+tags+") successfully posted.\n");
        return "welcome";
    }

    @GetMapping("/blogslist")
    public String getBlogList (
            @RequestParam(value = "search", required = false) String keyword,
            Model model
    ) {
        List<Post> thePosts;
        if(keyword==null){
            thePosts = new PostService().findAll(postRepository);
        }else{
            thePosts = postRepository.findByKeyword(keyword);
        }
        model.addAttribute("posts_list", thePosts);
        return "post/posts_list";
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
        System.out.println("\n\ngettingFromHTMLForm:\n"+comment+"\n\n");
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
