//  cd "C:\VSC projects\demo"  ./gradlew clean bootRun


package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/game")
public class GameController {

    private final WordGameService game;

    @Autowired
    public GameController(WordGameService game) {
        this.game = game;
    }

    // Start or reset a new game
    @GetMapping("/start")
    public String startGame() {
        game.startNewGame();  // pick a new random word
        return "New game started. Start guessing!";
    }

    // Submit a guess
    @PostMapping("/guess")
    public WordGameResult makeGuess(@RequestParam String guess) {
        System.out.println("Received guess: " + guess);

        try {
            WordGameResult result = game.checkGuessDetailed(guess.toLowerCase());
            return result;
        } catch (IllegalArgumentException e) {
            return new WordGameResult(
                guess,
                new String[] { "Invalid guess: " + e.getMessage() },
                0,
                0,
                0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new WordGameResult(
                guess,
                new String[] { "Server error: " + e.getMessage() },
                0,
                0,
                0
            );
        }
    }

    // Reveal current target word (for debugging)
    @GetMapping("/target")
    public String revealWord() {
        return "Target word is: " + game.getTargetWord();
    }
}
