package com.Journal.JournalApp.Repository;

import com.Journal.JournalApp.Entity.JournalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepo extends JpaRepository<JournalEntity, String> {
}
