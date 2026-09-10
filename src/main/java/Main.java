import javax.swing.*;

void main() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PongGame game = new PongGame();
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.requestFocusInWindow();

        // 60 FPS Game Loop (~16.6ms per tick)
        Timer timer = new Timer(16, _ -> {
            game.gameLogic();
            game.repaint();
        });
        timer.start();
    });
}
