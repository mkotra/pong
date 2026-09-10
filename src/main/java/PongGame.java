import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class PongGame extends JPanel implements KeyListener {

    public enum State {
        START,
        PLAYING,
        PAUSED,
        GAME_OVER
    }

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int WINNING_SCORE = 10;

    private State gameState = State.START;
    private final Ball ball;
    private final Paddle userPaddle;
    private final Paddle pcPaddle;
    private final Set<Integer> keysPressed = new HashSet<>();

    private int playerScore = 0;
    private int computerScore = 0;
    private String winnerText = "";
    private int rallyCount = 0;
    private int maxRally = 0;

    public PongGame() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        ball = new Ball(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 5.5, 0, 5.5, Color.YELLOW, 12);
        userPaddle = new Paddle(20, (WINDOW_HEIGHT - 80) / 2, 80, 7, new Color(64, 150, 255));
        pcPaddle = new Paddle(WINDOW_WIDTH - 20 - Paddle.PADDLE_WIDTH, (WINDOW_HEIGHT - 80) / 2, 80, 6, new Color(255, 75, 75));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Court background
        g2d.setColor(new Color(15, 15, 20));
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Top & bottom border lines
        g2d.setColor(new Color(60, 60, 80));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(0, 2, WINDOW_WIDTH, 2);
        g2d.drawLine(0, WINDOW_HEIGHT - 2, WINDOW_WIDTH, WINDOW_HEIGHT - 2);

        // Center dashed line (Net)
        float[] dashPattern = {10, 10};
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        g2d.setColor(new Color(100, 100, 130, 180));
        g2d.drawLine(WINDOW_WIDTH / 2, 0, WINDOW_WIDTH / 2, WINDOW_HEIGHT);

        // Render entities
        ball.paint(g);
        userPaddle.paint(g);
        pcPaddle.paint(g);

        // Scoreboard
        drawScoreboard(g2d);

        // Overlays depending on state
        if (gameState == State.START) {
            drawStartOverlay(g2d);
        } else if (gameState == State.PAUSED) {
            drawPausedOverlay(g2d);
        } else if (gameState == State.GAME_OVER) {
            drawGameOverOverlay(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawScoreboard(Graphics2D g2d) {
        g2d.setFont(new Font("Monospaced", Font.BOLD, 42));

        // Player Score (Left)
        g2d.setColor(new Color(64, 150, 255, 200));
        g2d.drawString(String.format("%02d", playerScore), WINDOW_WIDTH / 2 - 90, 50);

        // PC Score (Right)
        g2d.setColor(new Color(255, 75, 75, 200));
        g2d.drawString(String.format("%02d", computerScore), WINDOW_WIDTH / 2 + 40, 50);

        // Rally info
        if (gameState == State.PLAYING && rallyCount > 0) {
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2d.setColor(new Color(200, 200, 200, 160));
            String rallyText = "Rally: " + rallyCount;
            int textWidth = g2d.getFontMetrics().stringWidth(rallyText);
            g2d.drawString(rallyText, (WINDOW_WIDTH - textWidth) / 2, 75);
        }
    }

    private void drawStartOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 48));
        drawCenteredString(g2d, "P O N G", WINDOW_HEIGHT / 2 - 70);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2d.setColor(new Color(220, 220, 220));
        drawCenteredString(g2d, "Controls: W / S or UP / DOWN Arrow Keys", WINDOW_HEIGHT / 2 - 10);
        drawCenteredString(g2d, "P: Pause  |  R: Restart  |  First to 10 Wins", WINDOW_HEIGHT / 2 + 20);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        drawCenteredString(g2d, "Press SPACE or ENTER to Serve", WINDOW_HEIGHT / 2 + 80);
    }

    private void drawPausedOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 42));
        drawCenteredString(g2d, "PAUSED", WINDOW_HEIGHT / 2 - 30);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2d.setColor(new Color(200, 200, 200));
        drawCenteredString(g2d, "Press P or SPACE to Resume", WINDOW_HEIGHT / 2 + 20);
        drawCenteredString(g2d, "Press R to Restart Game", WINDOW_HEIGHT / 2 + 50);
    }

    private void drawGameOverOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 190));
        g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        boolean playerWon = "PLAYER".equals(winnerText);
        g2d.setColor(playerWon ? new Color(75, 220, 100) : new Color(255, 75, 75));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 44));
        drawCenteredString(g2d, playerWon ? "YOU WIN!" : "PC WINS!", WINDOW_HEIGHT / 2 - 50);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "Final Score: " + playerScore + " - " + computerScore, WINDOW_HEIGHT / 2);
        drawCenteredString(g2d, "Best Rally: " + maxRally + " hits", WINDOW_HEIGHT / 2 + 30);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        drawCenteredString(g2d, "Press SPACE or R to Play Again", WINDOW_HEIGHT / 2 + 80);
    }

    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (WINDOW_WIDTH - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    public void gameLogic() {
        if (gameState != State.PLAYING) {
            return;
        }

        // Update ball position
        ball.move();

        // Check top and bottom wall collisions
        ball.bounceOffTopBottom(0, WINDOW_HEIGHT);

        // Check scoring conditions
        if (ball.getX() + ball.getSize() < 0) {
            // PC scores
            computerScore++;
            SoundEffect.playScore();
            if (computerScore >= WINNING_SCORE) {
                gameState = State.GAME_OVER;
                winnerText = "COMPUTER";
                SoundEffect.playGameOver();
            } else {
                resetBall(1); // Serve towards PC
            }
            return;
        } else if (ball.getX() > WINDOW_WIDTH) {
            // Player scores
            playerScore++;
            SoundEffect.playScore();
            if (playerScore >= WINNING_SCORE) {
                gameState = State.GAME_OVER;
                winnerText = "PLAYER";
                SoundEffect.playVictory();
            } else {
                resetBall(-1); // Serve towards Player
            }
            return;
        }

        // User paddle collision (Left side)
        if (ball.getVx() < 0 && userPaddle.isCollidingWithBall(ball)) {
            ball.bouncePaddle(userPaddle, true);
            rallyCount++;
            if (rallyCount > maxRally) {
                maxRally = rallyCount;
            }
        }

        // PC paddle collision (Right side)
        if (ball.getVx() > 0 && pcPaddle.isCollidingWithBall(ball)) {
            ball.bouncePaddle(pcPaddle, false);
            rallyCount++;
            if (rallyCount > maxRally) {
                maxRally = rallyCount;
            }
        }

        // User paddle movement
        if (keysPressed.contains(KeyEvent.VK_W) || keysPressed.contains(KeyEvent.VK_UP)) {
            userPaddle.moveUp(0);
        }
        if (keysPressed.contains(KeyEvent.VK_S) || keysPressed.contains(KeyEvent.VK_DOWN)) {
            userPaddle.moveDown(WINDOW_HEIGHT);
        }

        // PC AI Paddle Movement
        updateAiPaddle();
    }

    private void updateAiPaddle() {
        int targetY;
        if (ball.getVx() > 0 && ball.getX() > WINDOW_WIDTH / 3) {
            // Predict and follow ball when heading towards PC
            targetY = ball.getY() + ball.getSize() / 2;
        } else {
            // Return towards center when ball is on player's half
            targetY = WINDOW_HEIGHT / 2;
        }
        pcPaddle.moveTowards(targetY, 0, WINDOW_HEIGHT);
    }

    private void resetBall(int serveDirection) {
        rallyCount = 0;
        ball.reset(WINDOW_WIDTH, WINDOW_HEIGHT, serveDirection);
    }

    public void restartFullGame() {
        playerScore = 0;
        computerScore = 0;
        rallyCount = 0;
        maxRally = 0;
        winnerText = "";
        userPaddle.setY((WINDOW_HEIGHT - userPaddle.getHeight()) / 2);
        pcPaddle.setY((WINDOW_HEIGHT - pcPaddle.getHeight()) / 2);
        resetBall(Math.random() > 0.5 ? 1 : -1);
        gameState = State.PLAYING;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        keysPressed.add(code);

        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (gameState == State.START) {
                restartFullGame();
            } else if (gameState == State.PLAYING) {
                gameState = State.PAUSED;
            } else if (gameState == State.PAUSED) {
                gameState = State.PLAYING;
            } else if (gameState == State.GAME_OVER) {
                restartFullGame();
            }
        } else if (code == KeyEvent.VK_P) {
            if (gameState == State.PLAYING) {
                gameState = State.PAUSED;
            } else if (gameState == State.PAUSED) {
                gameState = State.PLAYING;
            }
        } else if (code == KeyEvent.VK_R) {
            restartFullGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
