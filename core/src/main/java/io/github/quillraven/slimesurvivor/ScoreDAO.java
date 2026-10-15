package io.github.quillraven.slimesurvivor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Data-access object for reading and writing game scores.
 * Synchronized methods serialize access when called by multiple threads.
 */
public class ScoreDAO {
    public synchronized void saveScore(int score) throws SQLException {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        String sql = "INSERT INTO game_scores(score) VALUES (?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, score);
            statement.executeUpdate();
        }
    }

    public synchronized int getHighScore() throws SQLException {
        String sql = "SELECT COALESCE(MAX(score), 0) AS high_score FROM game_scores";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt("high_score") : 0;
        }
    }
}
