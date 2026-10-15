# Slime Survivor

A small Java desktop survival game built with **LibGDX**. Move the player, avoid slimes, and use the automatic slash attack to survive and earn points. The game stores completed scores in a local SQLite database.

## Features

- Desktop GUI built with LibGDX and LWJGL3.
- WASD movement, bounded player movement, enemy spawning, collision detection, health, and score tracking.
- Object-oriented game entities: `GameObject`, `Player`, `Enemy`, and `Attack`.
- `Updatable` interface and polymorphic game-object updates.
- Generic collections (`Array<Enemy>`, `Array<Attack>`) for game entities.
- SQLite persistence through JDBC, `DatabaseManager`, and `ScoreDAO`.
- A single-thread database executor keeps SQL operations off the render loop; DAO methods are synchronized and SQL uses prepared statements.
- Exception handling around database initialization and score operations.

## Requirements

- JDK 25 (project source/target is configured for Java 25).
- Internet access on the first Gradle build so Gradle and dependencies can be downloaded.
- Windows, macOS, or Linux desktop.

## Run the game

### Windows (PowerShell or Command Prompt)

```powershell
.\gradlew.bat :lwjgl3:run
```

### macOS / Linux

```bash
./gradlew :lwjgl3:run
```

If the wrapper is not executable on macOS/Linux, run `chmod +x gradlew` first. Gradle downloads dependencies on the first run.

## Build a desktop JAR

```bash
./gradlew :lwjgl3:jar
```

On Windows, use `gradlew.bat :lwjgl3:jar`. The JAR is created under `lwjgl3/build/libs/`.

## Controls

- **W / A / S / D** — Move.
- **SPACE** — Start from the controls screen.
- **R** — Restart after game over.

## Database

The SQLite database is created automatically at `~/.slime-survivor/slime-survivor.db` (the user's home directory). The `game_scores` table stores each completed run. The high score is loaded when a run starts. No external database server is needed.

## Project structure

```text
assets/                         Images, font, and audio
core/src/main/java/.../         Game logic and persistence classes
  GameObject.java               Shared game-object base class
  Updatable.java                Per-frame update interface
  Player.java / Enemy.java      Player and enemy behavior
  Attack.java                   Attack animation/hitbox
  GameScreen.java               Main game loop, collisions, and UI
  DatabaseManager.java          SQLite connection/schema setup
  ScoreDAO.java                 Prepared-statement score queries
  ScoreService.java              Background database executor
lwjgl3/                         Desktop launcher
teavm/                          TeaVM/web build module
```

## Notes

- The game is a desktop LibGDX application, not a Servlet-based Java web application.
- The SQLite driver is declared in `core/build.gradle` and is downloaded by Gradle.
- Build output and local IDE/cache files should not be committed to GitHub.

## Credits

Game art and audio assets are retained from the original project. See the asset links in the original project history for their licenses and attribution.
