package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Entry;
import com.main.reflect_diary.model.ReflectionQuestion;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.EntryService;
import com.main.reflect_diary.service.ReflectionQuestionService;
import com.main.reflect_diary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntryControllerTest {

    @Mock
    private EntryService entryService;

    @Mock
    private ReflectionQuestionService reflectionQuestionService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @InjectMocks
    private EntryController entryController;

    private Entry entry;
    private User user;
    private ReflectionQuestion question;
    private final UUID entryId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final UUID questionId = UUID.randomUUID();
    private final UUID tagId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        entry = new Entry();
        entry.setId(entryId);
        entry.setDate(LocalDate.now());

        user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        question = new ReflectionQuestion();
        question.setId(questionId);

    }

    @Test
    public void testGetAllEntries() {
        when(principal.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(entryService.findByUserId(userId)).thenReturn(List.of(entry));

        String view = entryController.getAllEntries(model, principal);

        assertEquals("entries", view);
        verify(model).addAttribute(eq("entries"), any());
    }

    @Test
    public void testShowCreateForm_withQuestion() {
        when(reflectionQuestionService.getRandomQuestion()).thenReturn(question);

        String view = entryController.showCreateForm(model);

        assertEquals("create-entry", view);
        verify(model).addAttribute(eq("entry"), any(Entry.class));
        verify(model).addAttribute("question", question);
    }

    @Test
    public void testCreateEntry_withTags() {
        List<UUID> tagIds = List.of(tagId);
        when(principal.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        String result = entryController.createEntry(entry, principal, tagIds);

        assertEquals("redirect:/entries", result);
        assertEquals(user, entry.getUser());
        verify(entryService).save(entry);
    }

    @Test
    public void testDeleteEntry() {
        String result = entryController.deleteEntry(entryId);

        assertEquals("redirect:/entries", result);
        verify(entryService).deleteById(entryId);
    }
}
