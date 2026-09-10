# AGENTS.md for Pong Game

## Architecture Overview
Enhanced 2D Pong game using Java Swing and Java 25 preview features.

- **Core Components**:
  - `PongGame`: `JPanel` managing game loop, states (`START`, `PLAYING`, `PAUSED`, `GAME_OVER`), scoring, input handling, and AI logic.
  - `Ball`: Sub-pixel double-precision entity handling physics, dynamic bounce angles, speed acceleration, and wall reflections.
  - `Paddle`: Player and AI paddles with smooth vertical clamping and AABB collision checks.
  - `SoundEffect`: Real-time synthesized 8-bit retro audio generator using standard `javax.sound.sampled` API.
  - `Main`: Swing entry point on Event Dispatch Thread with a 60 FPS (~16ms) timer.

## Key Files
- `src/main/java/Main.java`: Launches `JFrame`, packs around `PongGame.getPreferredSize()`, starts 60 FPS timer.
- `src/main/java/PongGame.java`: Renders court (center net, scoreboard, rally counter, state overlays) and coordinates game states.
- `src/main/java/Ball.java`: Manages velocity, dynamic angle deflection off paddles, position resets, and speed scaling.
- `src/main/java/Paddle.java`: Renders rounded paddles, handles smooth movement and AI tracking.
- `src/main/java/SoundEffect.java`: Generates synthesized tones for hits, bounces, scoring, win, and game over.

## Developer Workflows
- **Compile**: `javac --enable-preview --release 25 -d target/classes src/main/java/*.java` (or via IDE/Maven).
- **Run**: `java --enable-preview -cp target/classes Main`.

## Controls
- `W` / `UP Arrow`: Move player paddle up.
- `S` / `DOWN Arrow`: Move player paddle down.
- `SPACE` / `ENTER`: Serve / Pause / Resume / Play Again.
- `P`: Pause / Resume.
- `R`: Restart game.
