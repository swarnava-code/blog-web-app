package com.mukut.demo.controller;


import com.mukut.demo.entity.User;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.PostService;
import com.mukut.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
public class UserController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    public UserRepository userRepository;



    private PostService postService = new PostService();

    @PostMapping("/register_done")
    public String register(@ModelAttribute User user, Model model) {
        User userDetails = new UserService().save(userRepository, user);
        model.addAttribute("message", userDetails.getFname() + " inserted.\n");
        return "welcome";
    }

    @GetMapping("/userslist")
    public String getUserList(Model model) {
        List<User> theUsers = new UserService().findAll(userRepository);
        model.addAttribute("users_list", theUsers);
        model.addAttribute("user5", userRepository.findById(5).toString());
        return "user/users_list";
    }

    @GetMapping("/register")
    public String index() {
        return "user/register";
    }


}
