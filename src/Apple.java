import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Apple {
    private final int DOT_SIZE = 24;
    private final int SIZE = 640;
    private int appleX;
    private int appleY;
    private Image appleImage;
    private Random random;

    public Apple() {
        this.random = new Random();
        ImageIcon iid = new ImageIcon("images/apple.png");
        this.appleX = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
        this.appleY = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
        this.appleImage = iid.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);
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
    public void spawnApple() {
        appleX = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
        appleY = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
    }
}
