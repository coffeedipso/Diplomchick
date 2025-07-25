package com.main.reflect_diary.service.service_impl;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.repository.UserRepository;
import com.main.reflect_diary.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        logger.info("Получение всех пользователей");
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID id) {
        logger.info("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
    }

    @Override
    @Transactional
    public User save(User user) {
        logger.info("Сохранение пользователя: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(UUID id, User user) {
        logger.info("Обновление пользователя с ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        logger.info("Удаление пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Поиск пользователя по username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }
    @Override
    public boolean existsByUsername(String username) {
        logger.info("Проверка существования пользователя с username: {}", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User updateUserPreferences(User user) {
        logger.info("Обновление настроек пользователя: {}", user.getUsername());
        return userRepository.save(user);
    }
}
