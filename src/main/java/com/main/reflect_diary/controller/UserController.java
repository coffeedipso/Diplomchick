package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Пользователи", description = "Операции с пользователями")
@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping("/create")
    public String createUser(@Parameter(description = "Пользователь") @ModelAttribute @Valid User user) {
        logger.info("Создание пользователя: {}", user.getUsername());
        userService.save(user);
        return "redirect:/users";
    }

    @Operation(summary = "Удалить пользователя по ID")
    @PostMapping("/{userId}/delete")
    public String deleteUser(@Parameter(description = "ID пользователя") @PathVariable UUID userId) {
        logger.info("Удаление пользователя с ID: {}", userId);
        userService.delete(userId);
        return "redirect:/";
    }

    @Operation(summary = "Переключить тёмную тему для пользователя")
    @PostMapping("/toggle-darkmode")
    @ResponseBody
    public ResponseEntity<?> toggleDarkMode(Principal principal) {
        if (principal == null) {
            logger.warn("Попытка переключить тёмную тему без авторизации");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            logger.warn("Пользователь не найден: {}", principal.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        boolean currentSetting = user.isDarkModeEnabled();
        user.setDarkModeEnabled(!currentSetting);
        userService.updateUserPreferences(user);
        logger.info("Пользователь {} переключил тёмную тему на {}", user.getUsername(), !currentSetting);

        return ResponseEntity.ok("Dark mode toggled");
    }

}
