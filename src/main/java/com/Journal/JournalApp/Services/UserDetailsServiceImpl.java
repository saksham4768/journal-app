package com.Journal.JournalApp.Services;

import com.Journal.JournalApp.Entity.User;
import com.Journal.JournalApp.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Inside the userDetailsServiceIMP -> " + username);
        try {
            User user = userRepository.findByusername(username);
            System.out.println("Loading user: " + user);
            if (user != null) {
                System.out.println("Roles assigned to user: " + user.getRoles());
                // Modify roles by adding "ROLE_" prefix
                List<String> modifiedRoles = user.getRoles().stream()
                        .map(role -> "ROLE_" + role.getRoleName()) // Adding prefix
                        .toList();
                System.out.println("Loading user: " + username);
                System.out.println("Assigned roles: " + modifiedRoles);

                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(modifiedRoles.toArray(new String[0])) // Apply modified roles
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in UserDetailsServiceImpl -> " + e.getMessage());
        }
        return null;
    }

}
