package com.main.reflect_diary.repository;

import com.main.reflect_diary.model.ReflectionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReflectionQuestionRepository extends JpaRepository<ReflectionQuestion, UUID> {
    @Query(value = "SELECT * FROM reflection_questions ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ReflectionQuestion findRandom();
}
