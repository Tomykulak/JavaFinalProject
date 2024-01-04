import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppleTest {

    @Test
    void spawnAppleTest() {
        GameManager gameManager = new GameManager();
        Apple apple = new Apple();
        apple.spawnApple();
        // could be improved comparing against 0
        assertTrue(apple.getAppleX() >= 0 && apple.getAppleX() < gameManager.getSIZE());
        assertTrue(apple.getAppleY() >= 0 && apple.getAppleY() < gameManager.getSIZE());
    }
}
