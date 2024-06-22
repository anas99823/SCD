import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RollingForPointsGUI extends JFrame {
    private JLabel currentPlayerLabel;
    private JLabel playerScoreLabel;
    private JLabel computerScoreLabel;
    private JButton rollButton;
   // private JButton noButton;
    private JButton playAgainButton;
    private JButton returnToMenuButton;

    private int targetScore = 20;
    private int playerScore = 0;
    private int computerScore = 0;
    private boolean isPlayerTurn = true;

    public RollingForPointsGUI() {
        setTitle("Rolling for Points");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(7, 1));

        currentPlayerLabel = new JLabel("Player's turn");
        playerScoreLabel = new JLabel("Player's Score: " + playerScore);
        computerScoreLabel = new JLabel("Computer's Score: " + computerScore);
        rollButton = new JButton("Roll Dice");
       // noButton = new JButton("No (Next Turn)");
        playAgainButton = new JButton("Play Again");
        returnToMenuButton = new JButton("Return to Main Menu");

        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlayerTurn) {
                    int rollResult = rollDice();
                    playerScore += rollResult;

                    if (rollResult == 1) {
                        JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                                "Turn ends. No points scored.",
                                "Rolling for Points",
                                JOptionPane.INFORMATION_MESSAGE);

                        isPlayerTurn = false;
                        updateLabels();
                        computerTurn();
                    } else {
                        JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                                "You rolled: " + rollResult + "\nPoints scored: " + rollResult,
                                "Rolling for Points",
                                JOptionPane.INFORMATION_MESSAGE);

                        if (playerScore >= targetScore) {
                            JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                                    "Congratulations! You win!",
                                    "Rolling for Points",
                                    JOptionPane.INFORMATION_MESSAGE);
                            playAgainButton.setEnabled(true);
                        } else {
                            updateLabels();
                        }
                    }
                }
            }
        });

        

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                playAgainButton.setEnabled(false);
            }
        });

        returnToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Application.main(new String[]{});
            }
        });

        playAgainButton.setEnabled(false);

        add(currentPlayerLabel);
        add(playerScoreLabel);
        add(computerScoreLabel);
        add(rollButton);
        //add(noButton);
        add(playAgainButton);
        add(returnToMenuButton);
    }

    public void updateLabels() {
        if (isPlayerTurn) {
            currentPlayerLabel.setText("Player's turn");
        } else {
            currentPlayerLabel.setText("Computer's turn");
        }
        playerScoreLabel.setText("Player's Score: " + playerScore);
        computerScoreLabel.setText("Computer's Score: " + computerScore);
    }

    public void resetGame() {
        playerScore = 0;
        computerScore = 0;
        isPlayerTurn = true;
        updateLabels();
    }

    public int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    public void computerTurn() {
        if (!isPlayerTurn) {
            // Simulate computer's turn
            int rollResult = rollDice();
            computerScore += rollResult;

            if (rollResult == 1) {
                JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                        "Computer's turn ends. No points scored.",
                        "Rolling for Points",
                        JOptionPane.INFORMATION_MESSAGE);
                isPlayerTurn = true;
            } else {
                JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                        "Computer rolled: " + rollResult + "\nPoints scored: " + rollResult,
                        "Rolling for Points",
                        JOptionPane.INFORMATION_MESSAGE);

                if (computerScore >= targetScore) {
                    JOptionPane.showMessageDialog(RollingForPointsGUI.this,
                            "Computer wins!",
                            "Rolling for Points",
                            JOptionPane.INFORMATION_MESSAGE);
                    playAgainButton.setEnabled(true);
                } else {
                    updateLabels();
                    computerTurn(); // Continue computer's turn until it ends
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RollingForPointsGUI gameGUI = new RollingForPointsGUI();
                gameGUI.setVisible(true);
            }
        });
    }
}
