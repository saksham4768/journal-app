package com.Journal.JournalApp.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "UsersTable")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userID")
public class User {

    //here use the @GeneratedValue(strategy = GenerationType.UUID) for to ensure uniqueness and automatic generate
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false, unique = true)
    //@NonNull
    private String username;

    //@NonNull is lombok annotation that applies only at the constructor level. so for Database constraint use
    //@Column(nullable = false)

    //@NonNull

    @Column(nullable = false)
    private String password;

    // One-to-Many relationship with JournalEntity, meaning one user can have multiple journal entries.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntity> journalEntities = new ArrayList();

    //Issue: @ManyToMany(fetch = FetchType.EAGER) loads roles every time a user is retrieved, which can impact performance.
    //Fix: Use FetchType.LAZY to load roles only when needed.

    // Many-to-Many relationship for roles; a user can have multiple roles, and a role can be assigned to multiple users.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userID"), // FK for the User entity
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId") // FK for the Roles entity
    )
    private Set<Roles> roles = new HashSet<>();


//    Issue: You are using List<Roles>, which allows duplicate roles.
//    Fix: Use Set<Roles> to prevent duplicates.
    //private List<Roles>roles = new ArrayList<>();
}
