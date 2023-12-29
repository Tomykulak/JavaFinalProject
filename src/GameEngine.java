import javax.swing.*;

public class GameEngine {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGraphic snakeGraphic = new SnakeGraphic();
        frame.add(snakeGraphic);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
