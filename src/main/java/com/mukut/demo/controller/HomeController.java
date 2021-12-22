package com.mukut.demo.controller;

import com.mukut.demo.entity.Posts;
import com.mukut.demo.entity.User;
import com.mukut.demo.repo.PostsRepository;
import com.mukut.demo.repo.UserRepository;
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
    private PostsRepository postsRepository;

//    @Autowired
//    public UserRepository userRepository ;

//    @Autowired
//    public UserService userService;




    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("page", "Welcome to Landing Page");
        return "index";
    }



    //################################################# for POST


    @GetMapping("/newpost")
    public String newpost(){
        return "newpost";
    }

    @PostMapping("/newpost_submitted")
    public String newpost_submit(
            @ModelAttribute Posts posts,
            Model model
    ) {
        Posts post_inserted = postsRepository.save(posts);
        model.addAttribute("heading_message", "Your blog successfully submitted");
        model.addAttribute("message", post_inserted.getTitle()+" successfully posted.\n");
        return "welcome";
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
        return "users/users_list";
    }

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
