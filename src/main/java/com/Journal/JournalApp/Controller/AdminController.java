package com.Journal.JournalApp.Controller;

import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping("/all-users")  // This endpoint is secured by Spring Security with ADMIN role.
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.fetchAllUsers();
        if(users != null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createuser(@RequestBody User user){
        System.out.println("Inside AdminController");
        userService.saveAdmin(user);
    }
}
