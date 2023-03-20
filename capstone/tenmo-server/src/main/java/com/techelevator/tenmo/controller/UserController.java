package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
public class UserController {
    /*
    ########################################   Attributes   ##########################################
     */

    private UserDao dao;

    /*
   ########################################   Constructor   ##########################################
    */

    public UserController(UserDao dao){ //constructor
        this.dao = dao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

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
        User user = dao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown User");
        }
        return user;
    }

    @GetMapping(path="/me")
    public User getSelf(Principal principal){
        int currentUserId = dao.findIdByUsername(principal.getName());
        return dao.getUserById(currentUserId);
    }
}
