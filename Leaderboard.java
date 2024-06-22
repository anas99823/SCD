import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Leaderboard extends JFrame {
    private JTable leaderboardTable;
    private JComboBox<String> gameComboBox;

    public Leaderboard() {
        setTitle("Leaderboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        leaderboardTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        gameComboBox = new JComboBox<>();
        gameComboBox.addItem("All Games"); // Add option for all games
        gameComboBox.addItem("riddle");
        gameComboBox.addItem("hangman");
        gameComboBox.addItem("dice game");

        // Add action listener to the combo box
        gameComboBox.addActionListener(e -> {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            fetchScores(selectedGame); // Fetch scores based on selected game
        });

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter by Game:"));
        filterPanel.add(gameComboBox);

        // Add components to JFrame
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set JFrame properties
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // Initially fetch scores for all games
        fetchScores("All Games");
    }

    private void fetchScores(String selectedGame) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Rank");
        model.addColumn("User Name");
        model.addColumn("Game");
        model.addColumn("Score");

        List<String> gamesToFilter;
        if (selectedGame.equals("All Games")) {
            gamesToFilter = Arrays.asList("riddle", "hangman", "dice game");
        } else {
            gamesToFilter = Arrays.asList(selectedGame);
        }

        try (Connection connection = Database.getConnection()) {
            // Prepare SQL query with dynamic game filtering
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT users.username, scores.game, MAX(scores.score) AS highest_score ");
            sqlBuilder.append("FROM users ");
            sqlBuilder.append("LEFT JOIN scores ON users.id = scores.user_id ");
            sqlBuilder.append("WHERE scores.game IN (");

            // Append placeholders for each game
            for (int i = 0; i < gamesToFilter.size(); i++) {
                if (i > 0) {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append("?");
            }

            sqlBuilder.append(") ");
            sqlBuilder.append("GROUP BY users.username, scores.game ");
            sqlBuilder.append("ORDER BY scores.game, highest_score DESC");

            String sql = sqlBuilder.toString();
            System.out.println("Executing SQL: " + sql);
            System.out.println("Games to filter: " + gamesToFilter);

            PreparedStatement statement = connection.prepareStatement(sql);

            // Set parameters for each game in the list
            for (int i = 0; i < gamesToFilter.size(); i++) {
                statement.setString(i + 1, gamesToFilter.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            int rank = 1;
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String game = resultSet.getString("game");
                int score = resultSet.getInt("highest_score");
                model.addRow(new Object[]{rank, username, game, score});
                rank++;
            }

            leaderboardTable.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Leaderboard::new);
    }
}
