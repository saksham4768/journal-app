package com.Journal.JournalApp.Services;

import com.Journal.JournalApp.Entity.JournalEntity;
import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Repository.JournalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalRepo JournalEntryRepo;

    @Autowired
    private UserService userService;
    public ResponseEntity<?> saveAndUpdateEntry(JournalEntity entry, String username){
        try {
            System.out.println("In saveAndUpdateEntry method called" + entry + " UserName is -> " + username);

            User user = userService.findByUserName(username);
            if(user == null){
                return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
            }

            // Set the user in the journal entry
            entry.setUser(user);

            // Save the journal entry
            JournalEntity saved = JournalEntryRepo.save(entry);

            // No need to manually add to user.getJournalEntities() list and re-save user,
            // because JPA will handle it due to the @OneToMany relationship
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    public ResponseEntity<?> saveAndUpdateEntry(JournalEntity entry){
        try{
            JournalEntryRepo.save(entry);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(entry);
    }

    public List<JournalEntity> fetchAllJournal(){
        return JournalEntryRepo.findAll();
    }

    public JournalEntity fetchById(String id){
        return JournalEntryRepo.findById(id).orElse(null);
    }

    public void deleteEntry(String id){
        JournalEntryRepo.deleteById(id);
    }
}
