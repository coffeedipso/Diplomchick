package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Role;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Tag(name = "Аутентификация", description = "Операции входа и регистрации")
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Показать страницу логина")
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @Operation(summary = "Показать страницу регистрации")
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @Operation(summary = "Зарегистрировать нового пользователя")
    @PostMapping("/register")
    public String createEntry(@Parameter(description = "Пользователь") @ModelAttribute @Valid User user, HttpServletRequest request) {
        logger.info("Регистрация нового пользователя: {}", user.getUsername());
        if (userService.existsByUsername(user.getUsername())) {
            logger.warn("Попытка регистрации с уже существующим именем: {}", user.getUsername());
            return "redirect:/auth/register?error=true";
        }
        user.setRole(Role.USER);
        user.setRegistrationDate(LocalDateTime.now());
        userService.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return "redirect:/";
    }
}
