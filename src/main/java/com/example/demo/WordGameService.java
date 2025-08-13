package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WordGameService {
    private static final int WORD_LENGTH = 6;
    private static final String TARGET_WORD_FILE = "words.txt";
    private static final String VALIDATION_WORD_FILE = "wordBank.txt";

    private final String targetWord;
    private final Set<String> validWords;
    private int guessesMade = 0;

    public WordGameService() {
        List<String> targets = loadWordsFromFile(TARGET_WORD_FILE);
        this.validWords = new HashSet<>(loadWordsFromFile(VALIDATION_WORD_FILE));

        if (targets.isEmpty()) {
            targets = List.of("banana");
        }

        Random random = new Random();
        this.targetWord = targets.get(random.nextInt(targets.size()));
    }

    public String getTargetWord() {
        return targetWord;
    }

    public WordGameResult checkGuessDetailed(String guess) {
        System.out.println("checkGuessDetailed called with: " + guess);
        System.out.println("Target word is: " + targetWord);
        System.out.println("Valid word list size: " + validWords.size());

        validateGuess(guess);
        guessesMade++;
        return evaluateGuess(guess);
    }

    private WordGameResult evaluateGuess(String guess) {
        char[] targetArr = targetWord.toCharArray();
        char[] guessArr = guess.toCharArray();
        boolean[] matched = new boolean[WORD_LENGTH];
        boolean[] used = new boolean[WORD_LENGTH];

        int correct = 0;
        String[] feedback = new String[WORD_LENGTH];
        Arrays.fill(feedback, "INCORRECT");

        // First pass: correct letters
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guessArr[i] == targetArr[i]) {
                matched[i] = true;
                used[i] = true;
                correct++;
                feedback[i] = "CORRECT";
            }
        }

        // Second pass: misplaced letters
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (matched[i]) continue;

            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!used[j] && guessArr[i] == targetArr[j]) {
                    used[j] = true;
                    feedback[i] = "MISPLACED";
                    break;
                }
            }
        }

        int misplaced = 0;
        int incorrect = 0;
        for (String status : feedback) {
            if ("MISPLACED".equals(status)) misplaced++;
            if ("INCORRECT".equals(status)) incorrect++;
        }

        return new WordGameResult(
            guess,
            feedback,
            correct,
            misplaced,
            incorrect
        );
    }

    private void validateGuess(String guess) {
        if (guess == null || guess.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Guess must be exactly " + WORD_LENGTH + " characters");
        }
        if (!guess.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Guess must contain only letters");
        }
        if (!validWords.contains(guess.toLowerCase())) {
            throw new IllegalArgumentException("Not in word list");
        }
    }

    private List<String> loadWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                System.err.println("File not found: " + fileName);
                return words;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.length() == WORD_LENGTH && line.matches("[a-z]+")) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading " + fileName + ": " + e.getMessage());
        }
        return words;
    }
}
