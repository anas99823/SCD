import javax.swing.*;
import java.awt.*;

public class HangmanVisual extends JPanel {
    private int maxAttempts;
    private int attempts;

    public HangmanVisual(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        this.attempts = 0;
        setPreferredSize(new Dimension(300, 400));
    }

    public void updateHangman(int attemptsRemaining) {
        this.attempts = maxAttempts - attemptsRemaining;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw the gallows
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(50, getHeight() - 50, getWidth() / 2, getHeight() - 50);
        g2d.drawLine(getWidth() / 2, getHeight() - 50, getWidth() / 2, 50);
        g2d.drawLine(getWidth() / 2, 50, getWidth() / 2 - 100, 50);

        // Draw the head
        if (attempts >= 1) {
            g2d.setColor(Color.RED);
            g2d.fillOval(getWidth() / 2 - 25, 75, 50, 50);
        }

        // Draw the body
        if (attempts >= 2) {
            g2d.setColor(Color.RED);
            g2d.drawLine(getWidth() / 2, 125, getWidth() / 2, 250);
        }

        // Draw the left arm
        if (attempts >= 3) {
            g2d.setColor(Color.RED);
            g2d.drawLine(getWidth() / 2, 140, getWidth() / 2 - 60, 200);
        }

        // Draw the right arm
        if (attempts >= 4) {
            g2d.setColor(Color.RED);
            g2d.drawLine(getWidth() / 2, 140, getWidth() / 2 + 60, 200);
        }

        // Draw the left leg
        if (attempts >= 5) {
            g2d.setColor(Color.RED);
            g2d.drawLine(getWidth() / 2, 250, getWidth() / 2 - 60, 310);
        }

        // Draw the right leg
        if (attempts >= 6) {
            g2d.setColor(Color.RED);
            g2d.drawLine(getWidth() / 2, 250, getWidth() / 2 + 60, 310);
        }
    }

    public void reset() {
        attempts = 0;
        repaint();
    }
}
