import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Dice extends JFrame {
    private JLabel currentPlayerLabel;
    private JLabel currentScoreLabel;
    private JButton rollButton;
    private JButton endTurnButton;
    private JLabel rollResultLabel;
    private JLabel pointsScoredLabel;
    private JLabel winnerLabel;

    private int targetScore = 10; // Change this value to set the target score
    private int[] playerScores = new int[2];
    private int currentPlayer = 0;

    public Dice() {
        setTitle("Rolling for Points");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        updateUI();

        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentScoreLabel = new JLabel();
        currentScoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        rollButton = new JButton("Roll Dice");
        endTurnButton = new JButton("End Turn");

        buttonPanel.add(rollButton);
        buttonPanel.add(endTurnButton);

        rollResultLabel = new JLabel();
        rollResultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rollResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pointsScoredLabel = new JLabel();
        pointsScoredLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pointsScoredLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winnerLabel = new JLabel();
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(currentPlayerLabel);
        mainPanel.add(currentScoreLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(rollResultLabel);
        mainPanel.add(pointsScoredLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(winnerLabel);

        getContentPane().add(mainPanel);

        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rollResult = rollDice();
                rollResultLabel.setText("Player " + (currentPlayer + 1) + " rolled: " + rollResult);

                if (rollResult == 1 && currentPlayer == 0) {
                    pointsScoredLabel.setText("Player " + (currentPlayer + 1) + " turn ends. No points scored.");
                    currentPlayer = 1;
                } else {
                    playerScores[currentPlayer] += rollResult;
                    pointsScoredLabel.setText("Player " + (currentPlayer + 1) + " scored: " + rollResult);

                    if (playerScores[currentPlayer] >= targetScore) {
                        endGame();
                    } else {
                        currentPlayer = 0;
                    }
                }

                updateUI();
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPlayer = 0;
                updateUI();
            }
        });
    }

    private void updateUI() {
        currentPlayerLabel.setText("Player " + (currentPlayer + 1) + "'s turn");
        currentScoreLabel.setText("Player " + (currentPlayer + 1) + " score: " + playerScores[currentPlayer]);
        rollResultLabel.setText("");
        pointsScoredLabel.setText("");
        winnerLabel.setText("");
    }

    private void endGame() {
        rollButton.setEnabled(false);
        endTurnButton.setEnabled(false);

        if (playerScores[0] > playerScores[1]) {
            winnerLabel.setText("Player 1 wins!");
        } else if (playerScores[1] > playerScores[0]) {
            winnerLabel.setText("Player 2 wins!");
        } else {
            winnerLabel.setText("It's a tie!");
        }
    }

    public static int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dice().setVisible(true);
            }
        });
    }
}
