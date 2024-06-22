import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

public class Riddle extends JFrame {
    private JLabel questionLabel;
    private JTextField answerTextField;
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JButton submitButton;
    private JButton playAgainButton;
    private JButton returnToMenuButton;

    private int currentQuestion = 1;
    private int totalQuestions = 5;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private Map<String, String> questionAnswerMap;
    private ArrayList<String> randomQuestions;
    private boolean gameOver = false;
    private int userId;  // Store the user ID

    public Riddle(int userId) {
        this.userId = userId;
        setTitle("Riddle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        initializeQuestionAnswerMap();
        createRandomQuestionsList();
        createComponents();
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        questionLabel = new JLabel();
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(questionLabel, gbc);

        gbc.gridy++;
        JPanel answerPanel = new JPanel(new FlowLayout());
        JLabel answerLabel = new JLabel("Your answer:");
        answerPanel.add(answerLabel);

        answerTextField = new JTextField(20);
        answerPanel.add(answerTextField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitAnswer());
        answerPanel.add(submitButton);

        mainPanel.add(answerPanel, gbc);

        gbc.gridy++;
        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(resultLabel, gbc);

        gbc.gridy++;
        scoreLabel = new JLabel("Score: 0 out of " + totalQuestions);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(scoreLabel, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout());

        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> resetGame());
        playAgainButton.setVisible(false);  // Initially hidden
        buttonPanel.add(playAgainButton);

        returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.addActionListener(e -> returnToMenu());
        returnToMenuButton.setVisible(false);  // Initially hidden
        buttonPanel.add(returnToMenuButton);

        mainPanel.add(buttonPanel, gbc);

        setContentPane(mainPanel);
        displayQuestion();
    }

    private void initializeQuestionAnswerMap() {
        questionAnswerMap = new HashMap<>();
        questionAnswerMap.put("I am taken from a mine, and shut up in a wooden case, from which I am never released, and yet I am used by almost every person. What am I?", "Pencil Lead");
        questionAnswerMap.put("What has keys but can't open locks?", "Piano");
        questionAnswerMap.put("I speak without a mouth and hear without ears. I have no body, but I come alive with the wind. What am I?", "Echo");
        questionAnswerMap.put("The more you take, the more you leave behind. What am I?", "Footsteps");
        questionAnswerMap.put("What has to be broken before you can use it?", "Egg");
        // Add more questions and answers
    }

    private void createRandomQuestionsList() {
        randomQuestions = new ArrayList<>(questionAnswerMap.keySet());
        Collections.shuffle(randomQuestions);
    }

    private void displayQuestion() {
        if (currentQuestion <= totalQuestions) {
            String question = randomQuestions.get(currentQuestion - 1);
            questionLabel.setText("Question " + currentQuestion + ": " + question);
            answerTextField.setText("");
            resultLabel.setText("");  // Clear previous result
        } else {
            gameOver = true;
            saveScoreToDatabase();
            questionLabel.setText("Game Over");
            answerTextField.setEnabled(false);
            submitButton.setEnabled(false);
            playAgainButton.setVisible(true);
            returnToMenuButton.setVisible(true);

            // Display current score and check high score
            String scoreText = "<html>Correct answers: " + correctAnswers + "<br>Incorrect answers: " + incorrectAnswers + ".<br>";
            int highScore = getHighScoreFromDatabase();
            if (correctAnswers > highScore) {
                scoreText += "Congratulations! You've set a new high score!<br>Your score: " + correctAnswers + "</html>";
            } else {
                scoreText += "Your score: " + correctAnswers + ".<br>High score: " + highScore + "</html>";
            }
            resultLabel.setText(scoreText);
        }
    }

    private void submitAnswer() {
        if (gameOver) {
            return;
        }

        String question = randomQuestions.get(currentQuestion - 1);
        String correctAnswer = questionAnswerMap.get(question);
        String userAnswer = answerTextField.getText().trim();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            resultLabel.setText("Correct!");
            correctAnswers++;
        } else {
            resultLabel.setText("Incorrect. The correct answer was: " + correctAnswer);
            incorrectAnswers++;
        }

        scoreLabel.setText("Score: " + correctAnswers + " out of " + totalQuestions);
        currentQuestion++;
        displayQuestion();
    }

    private void saveScoreToDatabase() {
        try {
            Connection connection = Database.getConnection();
            String sql = "INSERT INTO scores (user_id, game, score) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, "Riddle");
            statement.setInt(3, correctAnswers);

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getHighScoreFromDatabase() {
        int highScore = 0;
        try {
            Connection connection = Database.getConnection();
            String sql = "SELECT MAX(score) AS high_score FROM scores WHERE user_id = ? AND game = 'Riddle'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                highScore = resultSet.getInt("high_score");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }

    private void resetGame() {
        currentQuestion = 1;
        correctAnswers = 0;
        incorrectAnswers = 0;
        gameOver = false;
        answerTextField.setEnabled(true);
        submitButton.setEnabled(true);
        playAgainButton.setVisible(false);
        returnToMenuButton.setVisible(false);
        createRandomQuestionsList();
        displayQuestion();
    }

    private void returnToMenu() {
        dispose();
        new Application(userId).setVisible(true);
    }

    // Database class for getConnection method (simulated)
    static class Database {
        public static Connection getConnection() throws SQLException {
            // Replace with your actual database connection logic
            String url = "jdbc:mysql://localhost:3306/game_app_db";
            String username = "root";
            String password = "";
            return DriverManager.getConnection(url, username, password);
        }
    }

    public static void main(String[] args) {
        // Replace userId with actual user ID from your application logic
        int userId = 1;  // Example user ID
        SwingUtilities.invokeLater(() -> {
            Riddle riddleGame = new Riddle(userId);
            riddleGame.setVisible(true);
        });
    }
}
