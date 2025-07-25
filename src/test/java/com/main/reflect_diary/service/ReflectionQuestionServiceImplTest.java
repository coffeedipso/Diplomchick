package com.main.reflect_diary.service;

import com.main.reflect_diary.model.ReflectionQuestion;
import com.main.reflect_diary.repository.ReflectionQuestionRepository;
import com.main.reflect_diary.service.service_impl.ReflectionQuestionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReflectionQuestionServiceImplTest {

    @Mock
    private ReflectionQuestionRepository reflectionQuestionRepository;

    @InjectMocks
    private ReflectionQuestionServiceImpl reflectionQuestionService;

    private ReflectionQuestion question;
    private final UUID questionId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        question = new ReflectionQuestion();
        question.setId(questionId);
        question.setText("How do you feel today?");
    }

    @Test
    public void testGetRandomQuestion() {
        when(reflectionQuestionRepository.findRandom()).thenReturn(question);

        ReflectionQuestion result = reflectionQuestionService.getRandomQuestion();

        assertNotNull(result);
        assertEquals(question.getId(), result.getId());
        assertEquals(question.getText(), result.getText());
    }

    @Test
    public void testFindByIdSuccess() {
        when(reflectionQuestionRepository.findById(questionId)).thenReturn(Optional.of(question));

        ReflectionQuestion result = reflectionQuestionService.findById(questionId);

        assertNotNull(result);
        assertEquals(questionId, result.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(reflectionQuestionRepository.findById(questionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            reflectionQuestionService.findById(questionId);
        });

        assertTrue(exception.getMessage().contains("ReflectionQuestion with id: " + questionId));
    }
}
