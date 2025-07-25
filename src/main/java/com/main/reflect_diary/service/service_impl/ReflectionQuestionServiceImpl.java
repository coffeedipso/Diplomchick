package com.main.reflect_diary.service.service_impl;

import com.main.reflect_diary.model.ReflectionQuestion;
import com.main.reflect_diary.repository.ReflectionQuestionRepository;
import com.main.reflect_diary.service.ReflectionQuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReflectionQuestionServiceImpl implements ReflectionQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionQuestionServiceImpl.class);

    private final ReflectionQuestionRepository reflectionQuestionRepository;

    public ReflectionQuestionServiceImpl(ReflectionQuestionRepository reflectionQuestionRepository) {
        this.reflectionQuestionRepository = reflectionQuestionRepository;
    }

    @Override
    public ReflectionQuestion getRandomQuestion() {
        logger.info("Получение случайного вопроса для рефлексии");
        return reflectionQuestionRepository.findRandom();
    }

    @Override
    public ReflectionQuestion findById(UUID id) {
        logger.info("Поиск вопроса по ID: {}", id);
        return reflectionQuestionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ReflectionQuestion.class.getSimpleName() + " with id: " + id));
    }
}
