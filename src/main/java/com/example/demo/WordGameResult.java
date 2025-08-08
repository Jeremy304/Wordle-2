package com.example.demo;

public class WordGameResult {
    private String guess;
    private String[] feedback;      // Or message, rename as you want
    private int correct;
    private int misplaced;
    private int incorrect;

    public WordGameResult(String guess, String[] feedback2, int correct, int misplaced, int incorrect) {
        this.guess = guess;
        this.feedback = feedback2;
        this.correct = correct;
        this.misplaced = misplaced;
        this.incorrect = incorrect;
    }

    // Getters (required for JSON serialization)
    public String getGuess() {
        return guess;
    }

    public String[] getFeedback() {
        return feedback;
    }

    public int getCorrect() {
        return correct;
    }

    public int getMisplaced() {
        return misplaced;
    }

    public int getIncorrect() {
        return incorrect;
    }
}
