package com.example.bigbrainacademy;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class AbstractGame {
    // protected attributes common to all games
    protected int correct_streak;
    protected int diff;
    protected int score = 0;
    protected int totalRight = 0;
    protected int totalWrong = 0;

    // Constructor, currently doesn't do much. Could be useful later
    AbstractGame() {
        diff = DifficultyLevel.SPROUT.getValue();
    }
    // TODO: make this a pure abstract function to be defined for each game
    // protected methods common to all games
    // basic function for incrementing difficulty, will be modified later
    protected void incDifficulty(boolean wasRight) {
        if (wasRight) {
            if (diff < DifficultyLevel.INTER.getValue()) {
                ++diff;
            }
            ++totalRight;
        }
        else {
            if (diff > 0) {
                --diff;
            }
            ++totalWrong;
        }

    }

    // public functions
    public int getTotalRight() {
        return totalRight;
    }

    public int getTotalWrong() {
        return totalWrong;
    }

    public int getScore() {
        return score;
    }

    // pure virtual functions to be overridden by subclasses
    protected abstract void calcScore(boolean wasRight);
}
