package com.main.reflect_diary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "entries")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @NotBlank
    @Size(min = 1, max = 10000)
    @Column(name = "main_text")
    private String mainText;

    @Size(max = 1000)
    @Column(name = "highlights")
    private String highlights;

    @Size(max = 1000)
    @Column(name = "difficulties")
    private String difficulties;

    @Size(max = 1000)
    @Column(name = "tomorrow_plan")
    private String tomorrowPlan;

    @Min(1)
    @Max(5)
    @Column(name = "score")
    private int score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mood")
    private Mood mood;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reflection_question_id")
    private UUID reflectionQuestionId;

    @Column(name = "question_answer")
    private String questionAnswer;

    public Entry() {
    }

    public Entry(LocalDate date, String mainText, String highlights, String difficulties, String tommorowPlan, int score, Mood mood, LocalDateTime createdAt, User user) {
        this.date = date;
        this.mainText = mainText;
        this.highlights = highlights;
        this.difficulties = difficulties;
        this.tomorrowPlan = tommorowPlan;
        this.score = score;
        this.mood = mood;
        this.createdAt = createdAt;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }

    public String getTomorrowPlan() {
        return tomorrowPlan;
    }

    public void setTomorrowPlan(String tomorrowPlan) {
        this.tomorrowPlan = tomorrowPlan;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public UUID getReflectionQuestionId() {
        return reflectionQuestionId;
    }

    public void setReflectionQuestionId(UUID reflectionQuestionId) {
        this.reflectionQuestionId = reflectionQuestionId;
    }
}
