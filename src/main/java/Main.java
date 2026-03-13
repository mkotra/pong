import javax.swing.*;

private static final JFrame FRAME = new JFrame("Pong");

void main() {

    //make it so program exits on close button click
    FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    //the size of the game will be 480x640, the size of the JFrame needs to be slightly larger
    FRAME.setSize(650, 495);

    //make the new PongGame
    PongGame game = new PongGame();

    //add the game to the JFrame
    FRAME.add(game);

    //show the window
    FRAME.setVisible(true);

    //make a new Timer
    Timer timer = new Timer(33, e -> {
        game.gameLogic();
        game.repaint();
    });

    //start the timer after it's been created
    timer.start();
}
