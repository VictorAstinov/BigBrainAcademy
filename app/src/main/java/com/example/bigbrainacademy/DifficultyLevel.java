package com.example.bigbrainacademy;

// Enum class for 6 difficulty levels
// https://www.nintendo.com/whatsnew/detail/2021/ask-the-developer-vol-3-big-brain-academy-brain-vs-brain-part-2/
public enum DifficultyLevel {
    SPROUT(0), // 1 star
    BEGIN(1), // 2 star
    INTER(2), // 3 star
    ADVANCE(3), // 4 star
    ELITE(4), // 5 star
    S_ELITE(5); // 6 star
    private final int value;
    DifficultyLevel(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
