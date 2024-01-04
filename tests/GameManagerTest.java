import org.junit.jupiter.api.Test;

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
}
