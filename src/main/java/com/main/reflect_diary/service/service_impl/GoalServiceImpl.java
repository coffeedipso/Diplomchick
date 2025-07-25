package com.main.reflect_diary.service.service_impl;

import com.main.reflect_diary.model.Goal;
import com.main.reflect_diary.repository.GoalRepository;
import com.main.reflect_diary.service.GoalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GoalServiceImpl implements GoalService {

    private static final Logger logger = LoggerFactory.getLogger(GoalServiceImpl.class);

    private final GoalRepository goalRepository;

    public GoalServiceImpl(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public Goal findGoalById(UUID id) {
        logger.info("Поиск цели по ID: {}", id);
        return goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found with id: " + id));
    }

    @Override
    @Transactional
    public Goal save(Goal goal) {
        logger.info("Сохранение цели: {}", goal.getDescription());
        return goalRepository.save(goal);
    }

    @Override
    @Transactional
    public Goal updateGoal(UUID id, Goal goal) {
        logger.info("Обновление цели с ID: {}", id);
        Goal excistingGoal = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found with id: " + id));
        excistingGoal.setCompleted(goal.isCompleted());
        excistingGoal.setDescription(goal.getDescription());
        excistingGoal.setDeadline(goal.getDeadline());
        return goalRepository.save(excistingGoal);
    }

    @Override
    @Transactional
    public void deleteGoal(UUID id) {
        logger.info("Удаление цели с ID: {}", id);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found with id: " + id));
        goalRepository.delete(goal);
    }

    @Override
    public List<Goal> findAllGoalsByUserId(UUID userId) {
        logger.info("Получение всех целей пользователя с ID: {}", userId);
        return goalRepository.findByUserId(userId);
    }
}
