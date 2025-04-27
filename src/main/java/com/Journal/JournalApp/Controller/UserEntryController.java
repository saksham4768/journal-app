package com.Journal.JournalApp.Controller;

import com.Journal.JournalApp.Entity.JournalEntity;
import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Repository.UserRepo;
import com.Journal.JournalApp.Services.JournalEntryService;
import com.Journal.JournalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<User> getUsers() {
        return userService.fetchAllUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String username = authentication.getName();
            User userInDb = userService.findByUserName(username);
            if (userInDb != null) {
                userInDb.setUsername(user.getUsername());
                userInDb.setPassword(user.getPassword());
                userService.saveAndUpdateUser(userInDb);
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            String username = authentication.getName();
            User userInDb = userService.findByUserName(username);
            if (userInDb!= null){
                userRepo.deleteByusername(username);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
