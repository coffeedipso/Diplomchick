package com.main.reflect_diary.model;

public enum Mood {
    HAPPY,
    NEUTRAL,
    SAD;

    public String getEmoji() {
        return switch (this) {
            case HAPPY -> "😊";
            case SAD -> "😢";
            case NEUTRAL -> "😐";
        };
    }
}
