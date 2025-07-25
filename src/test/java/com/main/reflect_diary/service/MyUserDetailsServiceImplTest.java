package com.main.reflect_diary.service;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.repository.UserRepository;
import com.main.reflect_diary.secutiry.MyUserDetails;
import com.main.reflect_diary.service.service_impl.MyUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsServiceImpl userDetailsService;

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
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof MyUserDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound");
        });
    }
}
