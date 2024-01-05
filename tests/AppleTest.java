import org.junit.jupiter.api.Test;

class AppleTest {

    @Test
    void spawnAppleTest() {
        Apple apple = new Apple();

        // Capture the initial positions
        int initialX = apple.getAppleX();
        int initialY = apple.getAppleY();

        // Spawn the apple again
        apple.spawnApple();

        // Check if either the new X or Y position is different
        boolean xChanged = initialX != apple.getAppleX();
        boolean yChanged = initialY != apple.getAppleY();

        assert(xChanged || yChanged);
    }
}