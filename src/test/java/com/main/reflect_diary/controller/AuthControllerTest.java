package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Role;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletRequest mockRequest;

    private User user;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testCreateEntry_NewUser() {
        when(userService.existsByUsername("testuser")).thenReturn(false);

        authController.createEntry(user,mockRequest);

        verify(userService, times(1)).save(user);
        assert user.getRole() == Role.USER;
        assert user.getRegistrationDate() != null;
    }

    @Test


    public void testCreateEntry_ExistingUser() {
        when(userService.existsByUsername("testuser")).thenReturn(true);

        authController.createEntry(user,mockRequest);

        verify(userService, never()).save(user);
    }
}
