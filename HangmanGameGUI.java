import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HangmanGameGUI extends JFrame implements ActionListener {
    private JLabel wordLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JButton playAgainButton;
    private JButton returnToMenuButton;

    private HangmanGame game;
    private HangmanVisual hangmanVisual;

    private int userId;

    public HangmanGameGUI(int userId, int level) {
        this.userId = userId;
        game = new HangmanGame(level);
        hangmanVisual = new HangmanVisual(6);

        setTitle("Hangman Game - Level " + level);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        wordLabel = new JLabel(game.getMaskedWord());
        wordLabel.setFont(new Font("Arial", Font.BOLD, 24));
        attemptsLabel = new JLabel("Attempts: " + game.getAttemptsRemaining());
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel = new JLabel("Score: " + game.getScore());
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        levelLabel = new JLabel("Level: " + game.getLevel());
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        guessField = new JTextField(10);
        guessButton = new JButton("Guess");
        guessButton.addActionListener(this);
        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(this);
        playAgainButton.setEnabled(false);
        returnToMenuButton = new JButton("Return to Main Menu");
        returnToMenuButton.addActionListener(this);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(wordLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(hangmanVisual, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(guessField);
        bottomPanel.add(guessButton);
        bottomPanel.add(attemptsLabel);
        bottomPanel.add(scoreLabel);
        bottomPanel.add(levelLabel);
        bottomPanel.add(playAgainButton);
        bottomPanel.add(returnToMenuButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guessButton) {
            String guess = guessField.getText();
            if (guess.length() > 0) {
                char letter = guess.charAt(0);
                boolean isCorrect = game.guess(letter);
                wordLabel.setText(game.getMaskedWord());
                attemptsLabel.setText("Attempts: " + game.getAttemptsRemaining());
                scoreLabel.setText("Score: " + game.getScore());
                levelLabel.setText("Level: " + game.getLevel());

                if (!isCorrect) {
                    hangmanVisual.updateHangman(game.getAttemptsRemaining());
                }

                if (game.isGameOver()) {
                    showGameResult();
                }
            }
            guessField.setText("");
            guessField.requestFocus();
        } else if (e.getSource() == playAgainButton) {
            resetGame();
        } else if (e.getSource() == returnToMenuButton) {
            dispose();
            new Application(userId).setVisible(true);
        }
    }

    private void resetGame() {
        game.initializeLevel(); // Reset to the current level
        hangmanVisual.reset();
        wordLabel.setText(game.getMaskedWord());
        attemptsLabel.setText("Attempts: " + game.getAttemptsRemaining());
        scoreLabel.setText("Score: " + game.getScore());
        levelLabel.setText("Level: " + game.getLevel());
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);
    }

    private void showGameResult() {
        String message;
        if (game.isWordGuessed()) {
            message = "Congratulations! You guessed the word!";
            unlockNextLevel();
        } else {
            message = "Game over! You lost! The word was: " + game.getWord();
        }
        JOptionPane.showMessageDialog(this, message);
        saveScoreToDatabase();
        playAgainButton.setEnabled(true);
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
    }

    private void unlockNextLevel() {
        try (Connection connection = Database.getConnection()) {
            String sql = "UPDATE users SET unlocked_level = GREATEST(unlocked_level, ?) WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, game.getLevel() + 1);
            statement.setInt(2, userId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveScoreToDatabase() {
        try (Connection connection = Database.getConnection()) {
            String sql = "INSERT INTO scores (user_id, game, score) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, "Hangman");
            statement.setInt(3, game.getScore());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HangmanGameGUI(1, 1)); // Example user ID and level
    }
}
