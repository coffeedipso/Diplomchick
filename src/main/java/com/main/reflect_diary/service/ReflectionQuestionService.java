package com.main.reflect_diary.service;

import com.main.reflect_diary.model.ReflectionQuestion;

import java.util.UUID;

public interface ReflectionQuestionService {
    ReflectionQuestion getRandomQuestion();
    ReflectionQuestion findById(UUID id);
}
