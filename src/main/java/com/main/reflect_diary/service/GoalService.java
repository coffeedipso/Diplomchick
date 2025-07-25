package com.main.reflect_diary.service;

import com.main.reflect_diary.model.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalService {
    Goal findGoalById(UUID id);
    Goal save(Goal dto);
    Goal updateGoal(UUID id, Goal dto);
    void deleteGoal(UUID id);
    List<Goal> findAllGoalsByUserId(UUID userId);
}
