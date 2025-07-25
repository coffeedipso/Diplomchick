package com.main.reflect_diary.repository;

import com.main.reflect_diary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = "entries")
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
