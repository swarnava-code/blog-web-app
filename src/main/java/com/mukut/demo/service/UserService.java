package com.mukut.demo.service;

import com.mukut.demo.entity.User;
import com.mukut.demo.repo.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    public User save(UserRepository userRepository, User user){
        User userInsertInfo = userRepository.save(user);
        return userInsertInfo;
    }

    public List<User> findAll(UserRepository userRepository){
        return userRepository.findAll();
    }
}
