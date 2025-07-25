package com.main.reflect_diary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "username", unique = true,  nullable = false, length = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @Email
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "email",  length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "dark_mode_preference")
    private boolean darkModeEnabled = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entry> entries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;

    public User() {
    }

    public User(String username, String password, String email, List<Entry> entries,  List<Goal> goals, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.entries = entries;
        this.goals = goals;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setDarkModeEnabled(boolean darkModeEnabled) {
        this.darkModeEnabled = darkModeEnabled;
    }
}

