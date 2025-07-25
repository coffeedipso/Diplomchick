package com.main.reflect_diary.repository;

import com.main.reflect_diary.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {
    List<Entry> findByUserId(UUID userId);
}
