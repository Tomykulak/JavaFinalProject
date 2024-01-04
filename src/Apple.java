import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Apple {
    private final GameManager gameManager;
    private final int biggerAppleDotSize;
    private int appleX;
    private int appleY;
    private final Image appleImage;
    private final Random random;

    public Apple() {
        gameManager = new GameManager();
        this.random = new Random();
        biggerAppleDotSize = gameManager.getDOT_SIZE()+6;
        ImageIcon iid = new ImageIcon("images/jahoda.png");
        this.appleX = random.nextInt((int) (gameManager.getSIZE() / gameManager.getDOT_SIZE())) * gameManager.getDOT_SIZE();
        this.appleY = random.nextInt((int) (gameManager.getSIZE() / gameManager.getDOT_SIZE())) * gameManager.getDOT_SIZE();
        this.appleImage = iid.getImage().getScaledInstance(biggerAppleDotSize, biggerAppleDotSize, Image.SCALE_SMOOTH);
    }
    public int getAppleX() {
        return appleX;
    }

    public int getAppleY() {
        return appleY;
    }

    public Image getAppleImage() {
        return appleImage;
    }

    public Random getRandom() {
        return random;
    }

    public int getBiggerAppleDotSize() {
        return biggerAppleDotSize;
    }

    public void spawnApple() {
        int gridSize = gameManager.getSIZE() / gameManager.getDOT_SIZE();
        int maxPosition = gridSize - 1; // Maximum position within the grid

        // Generate random coordinates within the grid
        appleX = random.nextInt(maxPosition) * gameManager.getDOT_SIZE();
        appleY = random.nextInt(maxPosition) * gameManager.getDOT_SIZE();

        // Ensure the apple remains within the grid boundaries, considering its size
        int maxApplePosition = maxPosition * gameManager.getDOT_SIZE();
        appleX = Math.min(appleX, maxApplePosition);
        appleY = Math.min(appleY, maxApplePosition);
    }

}
