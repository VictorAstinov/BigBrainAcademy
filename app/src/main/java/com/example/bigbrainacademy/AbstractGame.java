package com.example.bigbrainacademy;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class AbstractGame {
    // private attributes common to all games
    protected int correct_streak;
    protected int diff;
    protected double score;
    // Constructor, currently doesn't do much. Could be useful later
    AbstractGame() {
        correct_streak = 0;
        diff = DifficultyLevel.SPROUT.getValue();
        score = 0;
    }
    // private methods common to all games
    // basic function for incrementing difficulty, will be modified later
    protected void incDifficulty(boolean wasRight) {
        if (wasRight) {
            if (diff < DifficultyLevel.INTER.getValue()) {
                ++diff;
            }
        }
        else {
            --diff;
        }
    }

    // pure virtual functions to be overridden by subclasses
}
