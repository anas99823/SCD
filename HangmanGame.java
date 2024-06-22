import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HangmanGame {
    private static final int MAX_ATTEMPTS = 6;
    private static final String[] WORDS = {
        "hangman", "germany", "riphah",  "pakistan",
        "laptop", "haasan", "Annaas", "apples", "orange"
    };

    private String word;
    private List<Character> guessedLetters;
    private int attemptsRemaining;
    private int score;
    private int level;

    public HangmanGame(int level) {
        this.level = level;
        this.score = 0;
        initializeLevel();
    }

    public void initializeLevel() {
        word = generateRandomWord();
        guessedLetters = new ArrayList<>();
        attemptsRemaining = MAX_ATTEMPTS;
    }

    private String generateRandomWord() {
        // Ensure word length increases with level
        int minLength = 6 + (level - 1); // Minimum 6 letters for level 1, increase by level number
        int maxLength = minLength + 1;   // Maximum 1 letter more than minimum

        List<String> candidateWords = new ArrayList<>();
        for (String w : WORDS) {
            if (w.length() >= minLength && w.length() <= maxLength) {
                candidateWords.add(w);
            }
        }

        Random random = new Random();
        int index = random.nextInt(candidateWords.size());
        return candidateWords.get(index);
    }

    public boolean guess(char letter) {
        if (!isGameOver() && !guessedLetters.contains(letter)) {
            guessedLetters.add(letter);
            if (!wordContains(letter)) {
                attemptsRemaining--;
            } else {
                // If correct guess, increment score and check if word is completely guessed
                score += 10;
                if (isWordGuessed()) {
                    incrementLevel();
                }
            }
            return true;
        }
        return false;
    }

    public String getMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessedLetters.contains(c)) {
                masked.append(c);
            } else {
                masked.append('_');
            }
            masked.append(' ');
        }
        return masked.toString();
    }

    public boolean isWordGuessed() {
        for (char c : word.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return attemptsRemaining <= 0;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public String getWord() {
        return word;
    }

    private boolean wordContains(char letter) {
        return word.indexOf(letter) != -1;
    }

    public int getScore() {
        return score;
    }

    private void incrementLevel() {
        level++;
        initializeLevel();
    }

    public int getLevel() {
        return level;
    }
}
