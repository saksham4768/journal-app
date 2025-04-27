package com.Journal.JournalApp.Repository;

import com.Journal.JournalApp.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT u.userid, u.password, u.username,r.role_name  FROM users_table u
                JOIN user_roles ur ON u.userid = ur.user_id
                JOIN roles_table r ON ur.role_id = r.role_id
                WHERE u.username = :username
    """, nativeQuery = true)
     User findByusername(String username);


    void deleteByusername(String username);
}
