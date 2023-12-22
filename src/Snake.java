import java.awt.*;

public class Snake {
    private final int DOT_SIZE = 24;
    private final int SIZE = 640;
    private int bodyParts = 3;
    private int applesEaten = 0;
    private int maxApplesEaten = 0;
    private final Image snakeHeadImage;
    private final Image snakeBodyImage;
    private final Image snakeTailImage;

    public Snake() {
        this.snakeHeadImage = getSnakeHeadImage();
        this.snakeBodyImage = getSnakeBodyImage();
        this.snakeTailImage = getSnakeTailImage();
        this.bodyParts = getBodyParts();
        this.applesEaten = getApplesEaten();
        this.maxApplesEaten = getMaxApplesEaten();
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

    public void setMaxApplesEaten(int maxApplesEaten) {
        this.maxApplesEaten = maxApplesEaten;
    }

    public void setApplesEaten(int applesEaten) {
        this.applesEaten = applesEaten;
    }
    public void setBodyParts(int bodyParts) {
        this.bodyParts = bodyParts;
    }
}
