import java.awt.event.KeyEvent;

public class GameManager {
    private final int ALL_DOTS = 400;
    private final int DOT_SIZE = 24;
    private final int SIZE = 640;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private Snake snake;
    private Apple apple;

    private char direction = 'R';
    private boolean running = false;

    public GameManager(Snake snake, Apple apple) {
        this.snake = snake;
        this.apple = apple;
        initializeGame();
    }

    public GameManager(){
    }

    public void initializeGame() {
        snake.setBodyParts(3);
        snake.setApplesEaten(0);
        for (int i = 0; i < snake.getBodyParts(); i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        direction = 'R';
        running = true;
    }

    public void move() {
        for (int i = snake.getBodyParts(); i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= DOT_SIZE;
                break;
            case 'D':
                y[0] += DOT_SIZE;
                break;
            case 'L':
                x[0] -= DOT_SIZE;
                break;
            case 'R':
                x[0] += DOT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == apple.getAppleX()) && (y[0] == apple.getAppleY())) {
            snake.setBodyParts(snake.getBodyParts() + 1);
            snake.setApplesEaten(snake.getApplesEaten() + 1);
            apple.spawnApple();
            if(snake.getMaxApplesEaten() < snake.getApplesEaten()){
                snake.setMaxApplesEaten(snake.getApplesEaten());
            }
        }
    }

    public void checkCollisions() {
        for (int i = snake.getBodyParts() - 1; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0 || x[0] >= SIZE || y[0] < 0 || y[0] >= SIZE) {
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setDirectionBasedOnKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
        }
    }

    // Getters for necessary properties
    public int getSIZE() {
        return SIZE;
    }

    public int getDOT_SIZE() {
        return DOT_SIZE;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }
    public char getDirection() {
        return direction;
    }
}
