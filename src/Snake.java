import javax.swing.*;
import java.awt.*;

public class Snake {
    private final int DOT_SIZE = 24;
    private int bodyParts = 3;
    private int applesEaten = 0;
    private int maxApplesEaten = 0;
    private Image snakeHeadImage;
    private Image snakeBodyImage;
    private Image snakeTailImage;

    public Snake() {
        loadImages();
        this.bodyParts = 3;
        this.applesEaten = 0;
        this.maxApplesEaten = 0;
    }

    private void loadImages() {
        ImageIcon iis = new ImageIcon("images/snakeHead.png");
        snakeHeadImage = iis.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iib = new ImageIcon("images/snakeBody.png");
        snakeBodyImage = iib.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iit = new ImageIcon("images/snakeTail.png");
        snakeTailImage = iit.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);
    }

    public void updateMaxApplesEaten() {
        if (applesEaten > maxApplesEaten) {
            maxApplesEaten = applesEaten;
        }
    }

    public Image getSnakeHeadImage() {
        return snakeHeadImage;
    }

    public Image getSnakeBodyImage() {
        return snakeBodyImage;
    }

    public Image getSnakeTailImage() {
        return snakeTailImage;
    }

    public int getBodyParts() {
        return bodyParts;
    }
    public int getApplesEaten() {
        return applesEaten;
    }

    public int getMaxApplesEaten() {
        return maxApplesEaten;
    }
    public void setApplesEaten(int applesEaten) {
        this.applesEaten = applesEaten;
    }
    public void setBodyParts(int bodyParts) {
        this.bodyParts = bodyParts;
    }
}
