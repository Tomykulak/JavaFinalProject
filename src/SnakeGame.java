import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int SIZE = 640;
    private final int DOT_SIZE = 24;
    private final int ALL_DOTS = 400;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int bodyParts;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private JButton restartButton;

    private Image appleImage;
    private Image snakeHeadImage;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        //images
        ImageIcon iid = new ImageIcon("images/apple.png");
        appleImage = iid.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iis = new ImageIcon("images/snakeHead.png");
        snakeHeadImage = iis.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        // Initialize and set up the restart button
        restartButton = new JButton("Restart");
        restartButton.setBounds(SIZE / 2 - 50, SIZE / 2, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                repaint();
            }
        });
        restartButton.setVisible(false);
        add(restartButton);

        startGame();
    }

    public void startGame() {
        bodyParts = 3;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 48 - i * DOT_SIZE; // Reset snake's position
            y[i] = 48;
        }
        applesEaten = 0;
        direction = 'R'; // Reset snake's direction
        running = true; // Make sure the game is set to running
        spawnApple(); // Spawn a new apple

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(150, this); // Reset the timer
        timer.start();

        restartButton.setVisible(false); // Hide the restart button
    }

    public void spawnApple() {
        appleX = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
        appleY = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (running) {
            g.drawImage(appleImage, appleX, appleY, this);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    // Calculate the rotation angle based on the direction
                    double rotationAngle = 0;
                    switch (direction) {
                        case 'U': rotationAngle = Math.toRadians(-90); break;
                        case 'D': rotationAngle = Math.toRadians(90); break;
                        case 'L': rotationAngle = Math.toRadians(180); break;
                        case 'R': // No rotation needed
                    }

                    // Apply rotation
                    AffineTransform old = g2d.getTransform();
                    AffineTransform transform = new AffineTransform();
                    transform.rotate(rotationAngle, x[i] + DOT_SIZE / 2.0, y[i] + DOT_SIZE / 2.0);
                    g2d.setTransform(transform);

                    // Draw the snake head
                    g2d.drawImage(snakeHeadImage, x[i], y[i], this);

                    // Reset the transformation
                    g2d.setTransform(old);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
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
        if ((Math.abs(x[0] - appleX) < DOT_SIZE) && (Math.abs(y[0] - appleY) < DOT_SIZE)) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
    }
    public void checkCollisions() {
        // Check for collision with body
        for (int i = bodyParts - 1; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        // Check for collision with walls
        if (x[0] < 0 || x[0] >= SIZE || y[0] < 0 || y[0] >= SIZE) {
            running = false;
        }

        // Stop the timer if the game is no longer running
        if (!running) {
            timer.stop();
            restartButton.setVisible(true);
        }
    }


    public void gameOver(Graphics g) {
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        int xText = (SIZE - metrics.stringWidth(gameOverText)) / 2;
        int yText = SIZE / 2;
        g.drawString(gameOverText, xText, yText);

        // Position and show the restart button
        int buttonWidth = 100;
        int buttonHeight = 30;
        int xButton = (SIZE - buttonWidth) / 2;
        int yButton = yText + metrics.getHeight(); // position it below the text
        restartButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
        restartButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
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
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
