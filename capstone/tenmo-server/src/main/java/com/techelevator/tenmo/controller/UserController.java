package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDao dao;
    public UserController(UserDao dao){ //constructor
        this.dao = dao;
    }

    @GetMapping
    public List<User> findAll(@RequestParam(required = false) String username){
        List<User> users = new ArrayList<>();
        if(username==null){
            users = dao.findAll();
        } else {
            users.add(dao.findByUsername(username));
        }
        return users;
    }

    @GetMapping(path="/{id}")
    public User findUserById(@PathVariable int id){
        return dao.getUserById(id);
    }
}
