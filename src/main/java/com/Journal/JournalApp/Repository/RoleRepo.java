package com.Journal.JournalApp.Repository;

import com.Journal.JournalApp.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Long> {
    Roles findByRoleName(String roleName);
}
