package com.main.reflect_diary.repository;

import com.main.reflect_diary.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByUserId(UUID userId);
}
