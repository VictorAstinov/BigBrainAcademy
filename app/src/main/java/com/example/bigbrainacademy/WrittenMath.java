package com.example.bigbrainacademy;

// model for the written math minigame
public class WrittenMath extends AbstractComputeGame {
    private String input;
    private String answer;
    private String problem;

    public WrittenMath() {
        super();
        input = new String("");
        generateProblem();
    }

    public void addDigit(char c) {
        input = input + c;
    }

    public String getInput() {
        return input;
    }

    // makes problem and returns a formatted string, sets answer after generating problem
    private void generateProblem() {
        // TODO: make algorithm for generating problems
        answer = new String("123");
    }

    public boolean isContinue() {
        return (answer.charAt(input.length() - 1) == input.charAt(input.length() - 1));
    }

    public boolean isCorrect() {
        return answer.equals(input);
    }


}
