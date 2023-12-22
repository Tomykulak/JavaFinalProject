import java.awt.*;

public class Snake {
    private final int DOT_SIZE = 24;
    private final int SIZE = 640;
    private final Image snakeHeadImage;
    private final Image snakeBodyImage;
    private final Image snakeTailImage;

    public Snake() {
        this.snakeHeadImage = getSnakeHeadImage();
        this.snakeBodyImage = getSnakeBodyImage();
        this.snakeTailImage = getSnakeTailImage();
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
}
