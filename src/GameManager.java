import java.awt.event.KeyEvent;

public class GameManager {
    private final int ALL_DOTS = 400;
    private final int DOT_SIZE = 24;
    private final int SIZE = 840;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private Snake snake;
    private Apple apple;
    private char direction = 'R';
    private boolean running = false;
    private boolean hasStarted = false;
    private SoundManager soundManager;
    private boolean directionChangeAllowed = true; // Added flag to control direction changes

    public void setRunning(boolean running) {
        this.running = running;
        if (!running) {
            hasStarted = false;  // Reset when the game stops
        }
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public GameManager(Snake snake, Apple apple) {
        this.snake = snake;
        this.apple = apple;
        soundManager = new SoundManager();
        initializeGame();
    }

    public GameManager() {
    }

    public void initializeGame() {
        // Sets bodyparts of the snake
        snake.setBodyParts(3);
        snake.setApplesEaten(0);
        for (int i = 0; i < snake.getBodyParts(); i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        direction = 'R';
        running = true;
        hasStarted = true;
        soundManager.playBackgroundMusic(); // Start background music when the game initializes
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

        directionChangeAllowed = true; // Reset the direction change flag
    }

    public void checkApple() {
        int range = apple.getBiggerAppleDotSize() / 2;
        if (Math.abs(x[0] - apple.getAppleX()) < range && Math.abs(y[0] - apple.getAppleY()) < range) {
            snake.setBodyParts(snake.getBodyParts() + 1);
            snake.setApplesEaten(snake.getApplesEaten() + 1);
            apple.spawnApple();
            soundManager.playEatSound(); // Play the eat sound using the SoundManager
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

        if (!running) {
            soundManager.stopBackgroundMusic(); // Stop the background music on game over
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setDirectionBasedOnKey(int keyCode) {
        if (directionChangeAllowed) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                        directionChangeAllowed = false;
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                        directionChangeAllowed = false;
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                        directionChangeAllowed = false;
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                        directionChangeAllowed = false;
                    }
                }
            }
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
