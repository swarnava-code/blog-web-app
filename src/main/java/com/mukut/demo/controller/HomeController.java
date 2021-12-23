package com.mukut.demo.controller;

import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.User;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.PostService;
import com.mukut.demo.service.TagService;
import com.mukut.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository ;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

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
    public String getPostList (
            Model model
    ) {
        List<Post> thePosts = new PostService().findAll(postRepository);
        model.addAttribute("posts_list", thePosts);
        //model.addAttribute("user5", postsRepository.findById(5).toString());
        return "post/posts_list";
    }

    //################################################# for user

    @GetMapping("/register")
    public String index(){
        return "users/register";
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

    //####################################update
//    @PostMapping("/update")
//    public String updateTest(
//            @ModelAttribute User user,
//            Model model
//    ) {
//        mo
//        userRepository.save();
//        User userDetails = new UserService().save(userRepository ,user);
//        model.addAttribute("message", userDetails.getFname()+" inserted.\n");
//        return "welcome";
//    }

    //################################################# practice to use service

//
//    //UserRepository userRepository2 =
//    UserServiceImpl userService = new UserServiceImpl();
//
////    @Autowired
////    public HomeController (UserServiceImpl theEmployeeService) {
////        userService = theEmployeeService;
////    }
//
//    @GetMapping("/register123")
//    public String testkj(){
//        return "users/register";
//    }
//
//    @PostMapping("/test123")
//    public String saveEmployee(@ModelAttribute("users") User theUser, Model model) {
//
//        // save the employee
//        userService.save(theUser);
//
//        model.addAttribute("message", " inserted.\n");
//        return "welcome";
//
//    }

}
