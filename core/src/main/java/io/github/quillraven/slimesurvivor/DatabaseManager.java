package io.github.quillraven.slimesurvivor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates and opens the local SQLite database used for score persistence. */
public final class DatabaseManager {
    private static final Path DATABASE_DIRECTORY = Path.of(
        System.getProperty("user.home"), ".slime-survivor");
    private static final String JDBC_URL = "jdbc:sqlite:" +
        DATABASE_DIRECTORY.resolve("slime-survivor.db").toAbsolutePath();

    private DatabaseManager() { }

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(DATABASE_DIRECTORY);
        } catch (Exception exception) {
            throw new SQLException("Could not create the game data directory.", exception);
        }

        Connection connection = DriverManager.getConnection(JDBC_URL);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS game_scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER NOT NULL, " +
                "played_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException exception) {
            connection.close();
            throw exception;
        }
        return connection;
    }
}
