import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application extends JFrame {
    private int userId; // Store userId for logged-in user

    private JButton hangmanGameButton;
    private JButton riddleButton;
    private JButton rollingButton;
    private JButton leaderboardButton; // Moved the initialization up

    public Application(int userId) {
        this.userId = userId;

        setTitle("Game Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new FlowLayout());

        hangmanGameButton = new JButton("Hangman Game");
        riddleButton = new JButton("Riddle Game");
        rollingButton = new JButton("Rolling Dice Game");
        leaderboardButton = new JButton("LeaderBoard");

        hangmanGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHangmanLevels();
            }
        });

        riddleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startRiddleGame();
            }
        });

        rollingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startRollingForPointsGame();
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leaderboard();
            }
        });

        add(hangmanGameButton);
        add(riddleButton);
        add(rollingButton);
        add(leaderboardButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showHangmanLevels() {
        JFrame levelFrame = new JFrame("Select Level");
        levelFrame.setSize(300, 200);
        levelFrame.setLayout(new GridLayout(1, 3));

        int unlockedLevel = getUnlockedLevelForUser(userId);

        for (int i = 1; i <= 3; i++) {
            JButton levelButton = new JButton("Level " + i);
            final int level = i;
            levelButton.setEnabled(i <= unlockedLevel); // Enable based on unlocked level
            levelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    levelFrame.dispose();
                    new HangmanGameGUI(userId, level).setVisible(true);
                }
            });
            levelFrame.add(levelButton);
        }

        levelFrame.setLocationRelativeTo(this);
        levelFrame.setVisible(true);
    }

    private int getUnlockedLevelForUser(int userId) {
        // Implement logic to retrieve unlocked level for the user, e.g., from a database
        // For demonstration, assume level 1 is unlocked
        return 1;
    }

    private void startRiddleGame() {
        JOptionPane.showMessageDialog(this, "Starting Riddle game...");
        new Riddle(userId).setVisible(true);
    }

    private void startRollingForPointsGame() {
        JOptionPane.showMessageDialog(this, "Starting Rolling for Points game...");
        new RollingForPointsGUI().setVisible(true);
    }

    private void Leaderboard() {
        JOptionPane.showMessageDialog(this, "");
        new Leaderboard().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login();
            }
        });
    }
}
