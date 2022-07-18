package com.example.bigbrainacademy;

import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Random;

public class FlashMemory extends AbstractMemorizeGame {
    private String input;
    private String answer;
    final private char[] special_chars = {'\u2660', '\u2665', '\u2666', '\u2663'}; // unicode chars for each suit (spade, heart, diamond, club)
    private char special;
    private Random rng;
    private ArrayList<ArrayList<FlashTemplates>> templates;
    private int flashTimeinMS = 1000;

    public class FlashTemplates {
        int flashTimeinMS;
        int length;
        boolean special;

        FlashTemplates(int len, int time, boolean special) {
            length = len;
            flashTimeinMS = time;
            this.special = special;
        }
    }

    public FlashMemory() {
        input = "";
        answer = "";
        templates = new ArrayList<>();
        rng = new Random(123); // set constant seed for testing
        special = special_chars[rng.nextInt(special_chars.length)];

        for (int i = 0; i <= DifficultyLevel.S_ELITE.getValue(); ++i) {
            templates.add(new ArrayList<>());
            setTemplate(i, templates.get(i));
        }

        generateNewSpecial();
    }

    // this should prob be a static member of FlashTemplates
    private void setTemplate(int difficulty, ArrayList<FlashTemplates> template) {

        if (difficulty == DifficultyLevel.SPROUT.getValue()) {
            template.add(new FlashTemplates(2, 1000, false));
        }
        else if (difficulty == DifficultyLevel.BEGIN.getValue()) {
            template.add(new FlashTemplates(2, 900, false));
            template.add(new FlashTemplates(3, 1000, false));
        }
        else if (difficulty == DifficultyLevel.INTER.getValue()) {
            template.add(new FlashTemplates(3, 750, false));
            template.add(new FlashTemplates(4, 1000, true));
        }
        else if (difficulty == DifficultyLevel.ADVANCE.getValue()) {

        }
        else if (difficulty == DifficultyLevel.ELITE.getValue()) {

        }
        else {

        }
    }

    private String getProblem() {
        int size = templates.get(diff).size();
        FlashTemplates problemTemplate = templates.get(diff).get(rng.nextInt(size));
        StringBuilder str = new StringBuilder();

        flashTimeinMS = problemTemplate.flashTimeinMS;

        for (int i = 0; i < problemTemplate.length; ++i) {
            str.append(rng.nextInt(10));
        }
        if (problemTemplate.special) {
            str.setCharAt(1 + rng.nextInt(problemTemplate.length - 2), getSpecial());
        }
        return String.valueOf(str);
    }


    public String generateProblem() {
        // constant string for prototyping
        answer = getProblem();
        return answer;
    }

    public void addDigit(char c) {
        input += c;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String in) {
        input = in;
    }

    // TODO: should prob decouple
    public boolean isCorrect() {
        boolean wasRight = answer.equals(input);
        if (wasRight || !isContinue()) {
            incDifficulty(wasRight);
            calcScore(wasRight);
            generateNewSpecial();
        }
        return wasRight;
    }

    public boolean isContinue() {
        return input.charAt(input.length() - 1) == answer.charAt(input.length() - 1);
    }

    public char getSpecial() {
        return special;
    }

    private void generateNewSpecial() {
        special = special_chars[rng.nextInt(special_chars.length)];
    }

    // return time that answer will be displayed on screen, in milliseconds
    public int getTimeInterval() {
        return flashTimeinMS;
    }

    @Override
    protected void calcScore(boolean wasRight) {
        if (wasRight) {
            score += 10 + diff;
        }
        else {
            score -= (5 + diff);
        }
    }
}
