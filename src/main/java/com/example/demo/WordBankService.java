

package com.example.demo;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WordBankService {
    private static final int WORD_LENGTH = 6;
    private static final String WORD_FILE = "wordbank.txt";

    private final Set<String> validWords;
    private final String targetWord;
    private int guessesMade = 0;

    public WordBankService() {
        validWords = loadWords();
        if (validWords.isEmpty()) {
            validWords.add("banana"); // fallback
        }
        List<String> wordList = new ArrayList<>(validWords);
        Random rand = new Random();
        targetWord = wordList.get(rand.nextInt(wordList.size()));
        System.out.println("Target word chosen: " + targetWord);
    }

    public String getTargetWord() {
        return targetWord;
    }

    public WordGameResult checkGuessDetailed(String guess) {
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

        // First pass: correct letters in correct positions
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

    public boolean isValidGuess(String guess) {
        return validWords.contains(guess.toLowerCase());
    }

    public void validateGuess(String guess) {
        if (guess == null || guess.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Guess must be exactly " + WORD_LENGTH + " characters");
        }
        if (!guess.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Guess must contain only letters");
        }
        if (!isValidGuess(guess)) {
            throw new IllegalArgumentException("Guess not found in word bank");
        }
    }

    private Set<String> loadWords() {
        Set<String> words = new HashSet<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(WORD_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            if (is == null) {
                System.err.println("Word file not found: " + WORD_FILE);
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
            System.err.println("Error loading words: " + e.getMessage());
        }
        return words;
    }
}