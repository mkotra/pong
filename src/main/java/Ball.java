import java.awt.*;

public class Ball {

    public static final double INITIAL_SPEED = 5.5;
    public static final double MAX_SPEED = 12.0;
    private static final double PADDLE_MOTION_INFLUENCE = 0.35;
    private static final double MAX_BOUNCE_ANGLE = Math.PI / 3.0;

    private double x, y;
    private double vx, vy;
    private double speed;
    private int size;
    private final Color color;

    public Ball(int x, int y, double vx, double vy, double speed, Color color, int size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.speed = speed;
        this.color = color;
        this.size = size;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Outer glow
        g2d.setColor(new Color(255, 255, 0, 80));
        g2d.fillOval((int) Math.round(x) - 2, (int) Math.round(y) - 2, size + 4, size + 4);

        // Main ball
        g2d.setColor(color);
        g2d.fillOval((int) Math.round(x), (int) Math.round(y), size, size);
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public boolean bounceOffTopBottom(int top, int bottom) {
        if (y <= top) {
            y = top;
            vy = Math.abs(vy);
            SoundEffect.playWallBounce();
            return true;
        } else if (y + size >= bottom) {
            y = bottom - size;
            vy = -Math.abs(vy);
            SoundEffect.playWallBounce();
            return true;
        }
        return false;
    }

    public void bouncePaddle(Paddle paddle, boolean isUserPaddle) {
        // Increase speed gradually on each successful paddle return
        speed = Math.min(speed * 1.05 + 0.1, MAX_SPEED);

        // Reposition ball outside paddle to prevent sticking/multi-triggering
        if (isUserPaddle) {
            x = paddle.getX() + Paddle.PADDLE_WIDTH;
        } else {
            x = paddle.getX() - size;
        }

        // Calculate bounce angle based on where ball struck the paddle (-1.0 top to +1.0 bottom)
        double ballCenterY = y + (size / 2.0);
        double paddleCenterY = paddle.getY() + (paddle.getHeight() / 2.0);
        double normalizedIntersect = (ballCenterY - paddleCenterY) / (paddle.getHeight() / 2.0);
        normalizedIntersect = Math.max(-1.0, Math.min(1.0, normalizedIntersect));

        // Max deflection angle: 60 degrees (PI / 3 radians)
        double bounceAngle = normalizedIntersect * MAX_BOUNCE_ANGLE;
        double verticalSpeed = speed * Math.sin(bounceAngle);

        // Moving a paddle adds a small, capped vertical "spin" to the return.
        // Recalculate vx afterwards so the ball keeps its current total speed.
        double maxVerticalSpeed = speed * Math.sin(MAX_BOUNCE_ANGLE);
        vy = Math.clamp(
                verticalSpeed + paddle.getVerticalVelocity() * PADDLE_MOTION_INFLUENCE,
                -maxVerticalSpeed,
                maxVerticalSpeed
        );
        double horizontalSpeed = Math.sqrt(speed * speed - vy * vy);
        vx = isUserPaddle ? horizontalSpeed : -horizontalSpeed;

        SoundEffect.playPaddleHit();
    }

    public void reset(int screenWidth, int screenHeight, int directionTo) {
        this.size = 12;
        this.x = (screenWidth - size) / 2.0;
        this.y = (screenHeight - size) / 2.0;
        this.speed = INITIAL_SPEED;

        // Launch at an angle between -30 and +30 degrees
        double angle = (Math.random() * (Math.PI / 3.0)) - (Math.PI / 6.0);
        this.vx = directionTo * speed * Math.cos(angle);
        this.vy = speed * Math.sin(angle);
    }

    public int getX() {
        return (int) Math.round(x);
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public double getExactX() {
        return x;
    }

    public double getExactY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public int getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setPosition(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public void setVelocity(double newVx, double newVy) {
        this.vx = newVx;
        this.vy = newVy;
    }
}
