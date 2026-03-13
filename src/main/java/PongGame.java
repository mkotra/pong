import javax.swing.*;
import java.awt.*;

public class PongGame extends JPanel {

    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;

    private final Ball ball;
    private final Paddle userPaddle, pcPaddle;

    public PongGame() {
        ball = new Ball(300, 200, 3, 3, 3, Color.YELLOW, 10);
        userPaddle = new Paddle(10, 200, 75, 3, Color.BLUE);
        pcPaddle = new Paddle(610, 200, 75, 3, Color.RED);
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        ball.paint(g);

        userPaddle.paint(g);
        pcPaddle.paint(g);
    }

    public void gameLogic(){
        ball.move();
        ball.bounceOffEdges(0, WINDOW_HEIGHT);
        //in the gameLogic method
        userPaddle.moveTowards(0);
        pcPaddle.moveTowards(600);
    }
}
