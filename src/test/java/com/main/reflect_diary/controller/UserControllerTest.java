package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import java.util.UUID;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
    public void testCreateUser() {
        userController.createUser(user);
        verify(userService, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        userController.deleteUser(userId);
        verify(userService, times(1)).delete(userId);
    }
}
