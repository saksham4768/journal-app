package com.Journal.JournalApp.Services;

import com.Journal.JournalApp.Entity.JournalEntity;
import com.Journal.JournalApp.Entity.Roles;
import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Repository.JournalRepo;
import com.Journal.JournalApp.Repository.RoleRepo;
import com.Journal.JournalApp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void saveAndUpdateUser(User user){
        try{
            if(user != null){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
            else{
                System.out.println("User Object is null");
            }
        }
        catch (Exception e) {
            System.out.println("Error present in user Service ->" + e.getMessage());
        }
    }

    public List<User> fetchAllUsers(){

        return userRepository.findAll();
    }

    public User findByUserName(String username){
        User user = userRepository.findByusername(username);
        if(user == null){
            return null;
        }
        return userRepository.findByusername(username);
    }

    public void deleteEntry(Long id){

        userRepository.deleteById(id);
    }

    public ResponseEntity<?> saveAdmin(User user){
        System.out.println("Inside the UserService in save admin function -> " + user);
        try{
            User userInDB = userRepository.findByusername(user.getUsername());
            System.out.println("Inside the UserService user In DB -> " + userInDB);
            //User is not present in Database so create the user
            if(userInDB == null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                Set<Roles> rolePresent = user.getRoles();
                if(rolePresent != null && !rolePresent.isEmpty()){
                    Set<Roles> validRoles = new HashSet<>();
                    //let say more than one role comming for the particular user check the all role present in DB
                    for(Roles role : rolePresent){
                        if(role.getRoleName() != null && !role.getRoleName().isEmpty()){
                            System.out.println("Role Is -> " + role.getRoleName());
                            Roles roleInDB = roleRepository.findByRoleName(role.getRoleName());

                            System.out.println("Role In DB -> " + roleInDB);
                            if(roleInDB == null) {
                                return new ResponseEntity<>(role.getRoleName() + " is Not found", HttpStatus.NOT_FOUND);
                            }
                            System.out.println("Role Comming from request -> " + role);
                            validRoles.add(roleInDB); // Use existing role from DB
                        }
                        else{
                            return new ResponseEntity<>("Role Can not be Null", HttpStatus.NOT_FOUND);
                        }
                    }
                    System.out.println("Valid Role is -> " + validRoles);
                    user.setRoles(validRoles);
                    userRepository.save(user);
                }
                else{
                    return new ResponseEntity<>("Role Can not be empty", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public void saveNewuser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setRoles(Arrays.asList("USER"));
        // Fetching the "ADMIN" role from the database instead of creating a new one
        Roles adminRole = roleRepository.findByRoleName("ADMIN");

        Set<Roles> roles = new HashSet<>();
        roles.add(adminRole);  // Adding existing role

        user.setRoles(roles);
        userRepository.save(user);
    }
}
