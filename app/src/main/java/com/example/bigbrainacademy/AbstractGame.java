package com.example.bigbrainacademy;

public abstract class AbstractGame {
    // private attributes common to all games
    protected int correct_streak;
    protected DifficultyLevel diff = DifficultyLevel.SPROUT;
    protected double score;
    // Constructor, currently doesn't do much. Could be useful later
    AbstractGame() {
        correct_streak = 0;
        diff = DifficultyLevel.SPROUT;
        score = 0;
    }
    // private methods common to all games
    protected void calcDiff() {

    }
    // pure virtual functions to be overridden by subclasses

}
