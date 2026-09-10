import java.awt.*;

public class Paddle {

    public static final int PADDLE_WIDTH = 14;

    private int height;
    private int x, y;
    private int speed;
    private int verticalVelocity;
    private Color color;

    /**
     * A paddle is a rectangle that can move up and down
     *
     * @param x      the x position to start drawing the paddle
     * @param y      the y position to start drawing the paddle
     * @param height the paddle height
     * @param speed  the amount the paddle may move per frame
     * @param color  the paddle color
     */
    public Paddle(int x, int y, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    /**
     * Paints the paddle with rounded corners and highlight
     *
     * @param g graphics object passed from calling method
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paddle body
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, PADDLE_WIDTH, height, 6, 6);

        // Inner highlight border
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(x, y, PADDLE_WIDTH, height, 6, 6);
    }

    public void moveTowards(int moveToY, int topLimit, int bottomLimit) {
        int previousY = y;
        int centerY = y + height / 2;
        int diff = moveToY - centerY;

        if (Math.abs(diff) > speed) {
            y += (diff > 0 ? speed : -speed);
        } else {
            y += diff;
        }

        // Clamp to boundaries
        y = Math.clamp(y, topLimit, bottomLimit - height);
        verticalVelocity = y - previousY;
    }

    public void moveUp(int topLimit) {
        int previousY = y;
        y = Math.max(topLimit, y - speed);
        verticalVelocity = y - previousY;
    }

    public void moveDown(int bottomLimit) {
        int previousY = y;
        y = Math.min(bottomLimit - height, y + speed);
        verticalVelocity = y - previousY;
    }

    public boolean isCollidingWithBall(Ball ball) {
        int ballX = ball.getX();
        int ballY = ball.getY();
        int ballSize = ball.getSize();

        return ballX < x + PADDLE_WIDTH &&
               ballX + ballSize > x &&
               ballY < y + height &&
               ballY + ballSize > y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public int getVerticalVelocity() {
        return verticalVelocity;
    }

    public void stopVerticalMovement() {
        verticalVelocity = 0;
    }

    public void setY(int y) {
        this.y = y;
        verticalVelocity = 0;
    }
}