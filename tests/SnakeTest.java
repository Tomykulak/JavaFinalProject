import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnakeTest {
    @Test
    void updateMaxApplesEatenTest() {
        Snake snake = new Snake(3, 2);
        snake.updateMaxApplesEaten();
        assertEquals(snake.getMaxApplesEaten(), 3);
    }
}
