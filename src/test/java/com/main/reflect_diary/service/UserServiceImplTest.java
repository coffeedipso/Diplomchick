package com.main.reflect_diary.service;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.repository.UserRepository;
import com.main.reflect_diary.service.service_impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setPassword("rawpass");
        user.setEmail("test@example.com");
    }

    @Test
    public void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.findAllUsers();
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    public void testFindByIdSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.findById(userId);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void testSave() {
        when(passwordEncoder.encode("rawpass")).thenReturn("encodedpass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User saved = userService.save(user);
        assertEquals("encodedpass", saved.getPassword());
    }

    @Test
    public void testUpdateUserSuccess() {
        User updated = new User();
        updated.setUsername("newname");
        updated.setPassword("newpass");
        updated.setEmail("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(userId, updated);
        assertEquals("newname", result.getUsername());
        assertEquals("newpass", result.getPassword());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, new User()));
    }

    @Test
    public void testDeleteSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.delete(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.delete(userId));
    }

    @Test
    public void testFindByUsernameSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        User result = userService.findByUsername("testuser");
        assertEquals(userId, result.getId());
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername("unknown"));
    }

    @Test
    public void testExistsByUsername() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        assertTrue(userService.existsByUsername("testuser"));
    }
}
