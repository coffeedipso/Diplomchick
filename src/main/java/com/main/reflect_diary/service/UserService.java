package com.main.reflect_diary.service;

import com.main.reflect_diary.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAllUsers();
    User findById(UUID id);
    User save(User user);
    User updateUser(UUID id, User user);
    void delete(UUID id);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    User updateUserPreferences(User username);

}
