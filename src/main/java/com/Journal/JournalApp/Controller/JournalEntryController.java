package com.Journal.JournalApp.Controller;

import com.Journal.JournalApp.Entity.JournalEntity;
import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Services.JournalEntryService;
import com.Journal.JournalApp.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalService;

    @Autowired
    private UserService userService;

    @GetMapping("/entries")
    public ResponseEntity<?> getUserJournalEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("UserName is -> " + username);
        User user = userService.findByUserName(username);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        List<JournalEntity> entries = user.getJournalEntities();
        if(entries != null && entries.size() > 0) {
            return new ResponseEntity<>(entries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/createEntry")
    public ResponseEntity<?> createJournalEntries(@RequestBody JournalEntity myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("Request Body is -> " + myEntry + " and usernsme is -> " + username);
            myEntry.setDate(LocalDateTime.now());
            journalService.saveAndUpdateEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getEntry/{id}")
    public ResponseEntity<?> getJournalById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String username = authentication.getName();
            User user = userService.findByUserName(username);

            List<JournalEntity> journalEntities = user.getJournalEntities().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if (!journalEntities.isEmpty()) {
                Optional<JournalEntity>journal = Optional.ofNullable(journalService.fetchById(id));
                if(journal.isPresent()) {
                    return new ResponseEntity<>(journal.get(),HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteEntry/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if(user!= null) {
                List<JournalEntity> journalEntities = user.getJournalEntities().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
                if (!journalEntities.isEmpty()) {
                    journalService.deleteEntry(id);
                    return ResponseEntity.ok("Entry deleted successfully.");
                }
            }
        }
        return new ResponseEntity<>("Entry Not Found",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/putEntry/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable String id, @RequestBody JournalEntity newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (user != null) {
                List<JournalEntity> journalEntities = user.getJournalEntities().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
                if (!journalEntities.isEmpty()) {
                    JournalEntity oldEntity = journalService.fetchById(id);
                    oldEntity.setTitle((newEntry.getTitle() != null && !newEntry.getTitle().equals("")) ? newEntry.getTitle() : oldEntity.getTitle());
                    oldEntity.setContent((newEntry.getContent() != null && !newEntry.getContent().equals("")) ? newEntry.getContent() : oldEntity.getContent());

                    journalService.saveAndUpdateEntry(oldEntity);
                    return new ResponseEntity(oldEntity, HttpStatus.OK);
                }
            }
        }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
