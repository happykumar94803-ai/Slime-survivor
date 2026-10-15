package io.github.quillraven.slimesurvivor;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/** Runs database work on a background thread so the game loop stays responsive. */
public final class ScoreService implements AutoCloseable {
    private final ScoreDAO scoreDAO = new ScoreDAO();
    private final ExecutorService databaseExecutor;

    public ScoreService() {
        ThreadFactory factory = task -> {
            Thread thread = new Thread(task, "slime-survivor-database");
            thread.setDaemon(true);
            return thread;
        };
        databaseExecutor = Executors.newSingleThreadExecutor(factory);
    }

    public void saveScoreAsync(int score, Consumer<String> errorHandler) {
        databaseExecutor.submit(() -> {
            try {
                scoreDAO.saveScore(score);
            } catch (SQLException | IllegalArgumentException exception) {
                if (errorHandler != null) {
                    errorHandler.accept("Could not save score: " + exception.getMessage());
                }
            }
        });
    }

    public void loadHighScoreAsync(Consumer<Integer> resultHandler, Consumer<String> errorHandler) {
        databaseExecutor.submit(() -> {
            try {
                int highScore = scoreDAO.getHighScore();
                if (resultHandler != null) resultHandler.accept(highScore);
            } catch (SQLException exception) {
                if (errorHandler != null) {
                    errorHandler.accept("Could not load high score: " + exception.getMessage());
                }
            }
        });
    }

    @Override
    public void close() {
        databaseExecutor.shutdown();
    }
}
