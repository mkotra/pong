import java.awt.*; //needed for Color

public class Paddle {

    static final int PADDLE_WIDTH = 15;

    //declare instance variables
    private int height, x, y, speed;
    private Color color;

    /**
     * A paddle is a rectangle/block that can move up and down
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
     * Paints a rectangle on the screen
     *
     * @param g graphics object passed from calling method
     */
    public void paint(Graphics g) {

        //paint the rectangle for the paddle
        g.setColor(color);
        g.fillRect(x, y, PADDLE_WIDTH, height);

    }

    public void moveTowards(int moveToY) {

        //find the location of the center of the paddle
        int centerY = y + height / 2;

        //determine if we need to move more than the speed away from where we are now
        if (Math.abs(centerY - moveToY) > speed) {
            //if the center of the paddle is too far down
            if (centerY > moveToY) {
                //move the paddle up by the speed
                y -= speed;
            }
            //if the center of the paddle is too far up
            if (centerY < moveToY) {
                //move the paddle down by speed
                y += speed;
            }
        }

    }
}