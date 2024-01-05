import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    @Test
    void initializeGameTest() {
        Snake snake = new Snake();
        Apple apple = new Apple();
        GameManager gameManager = new GameManager(snake, apple);
        gameManager.initializeGame();
        assertEquals(3, snake.getBodyParts());
        assertNotEquals(2, snake.getBodyParts());
    }
    @Test
    void checkCollisionsTest() {
        Snake snake = new Snake();
        Apple apple = new Apple();
        GameManager gameManager = new GameManager(snake, apple);

        // Test collision with the wall
        gameManager.getX()[0] = gameManager.getSIZE(); // Move the snake outside the canvas
        gameManager.checkCollisions();
        assertFalse(gameManager.isRunning());

        // Test collision with the snake own body
        snake.setBodyParts(4);
        gameManager.getX()[0] = 48;
        gameManager.getY()[0] = 48;
        gameManager.getX()[1] = 72;
        gameManager.getY()[1] = 48;
        gameManager.getX()[2] = 96;
        gameManager.getY()[2] = 48;
        gameManager.getX()[3] = 120;
        gameManager.getY()[3] = 48;
        gameManager.checkCollisions();
        assertFalse(gameManager.isRunning());
    }
    @Test
    void checkAppleTest() {
        Snake snake = new Snake();
        Apple apple = new Apple();
        GameManager gameManager = new GameManager(snake, apple);

        // Set the snake's position to be close to the apple
        var oldAppleX = gameManager.getX()[0] = apple.getAppleX() - 10;
        var oldAppleY = gameManager.getY()[0] = apple.getAppleY();

        // Call checkApple to eat the apple
        gameManager.checkApple();

        // Check if the snake's body parts increased by 1
        assertEquals(4, snake.getBodyParts());

        // Check if the apple's position has changed
        assertNotEquals(apple.getAppleX(), oldAppleX);
        assertNotEquals(apple.getAppleY(), oldAppleY);
    }
    @Test
    void moveTest() {
        Snake snake = new Snake();
        Apple apple = new Apple();
        GameManager gameManager = new GameManager(snake, apple);

        // Set initial positions
        gameManager.getX()[0] = 48;
        gameManager.getY()[0] = 48;

        // Set direction to 'R' using setDirectionBasedOnKey
        gameManager.setDirectionBasedOnKey(KeyEvent.VK_RIGHT);

        // Call the move method
        gameManager.move();

        // Check if the snake's head moved to the right
        assertEquals(72, gameManager.getX()[0]);
        assertEquals(48, gameManager.getY()[0]);
    }
}
