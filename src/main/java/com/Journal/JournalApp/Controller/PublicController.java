package com.Journal.JournalApp.Controller;

import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")  // This controller is not secured by Spring Security, it's for public access.
// It is recommended to keep this controller as simple as possible and not include any sensitive information.
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String HealthCheck(){
        return "OK";
    }

    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.fetchAllUsers();
    }

    @PostMapping(value = "/create-user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.saveAndUpdateUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
