package com.Journal.JournalApp.Entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "RolesTable")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roleId")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    //roleName is not marked as unique, meaning duplicate roles can be created.
    //@Column(nullable = false)

    @Column(nullable = false, unique = true)
    private String roleName;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();// To manage the inverse side of the relationship

//    Using List<User> allows duplicate users in the users list.
//    Since a role should be assigned to a unique set of users, using Set<User> is better.
}
