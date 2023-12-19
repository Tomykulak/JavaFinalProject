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
    private int highestScore = 0; // New variable to store the highest score
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private JButton restartButton;

    private Image appleImage;
    private Image snakeHeadImage;
    private Image snakeBodyImage;
    private Image snakeTailImage;
    private Image backgroundImage;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        ImageIcon iid = new ImageIcon("images/apple.png");
        appleImage = iid.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iis = new ImageIcon("images/snakeHead.png");
        snakeHeadImage = iis.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iib = new ImageIcon("images/snakeBody.png");
        snakeBodyImage = iib.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon iit = new ImageIcon("images/snakeTail.png");
        snakeTailImage = iit.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon ibg = new ImageIcon("images/sforestBackground.jpg");
        backgroundImage = ibg.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);

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
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        applesEaten = 0;
        direction = 'R';
        running = true;
        spawnApple();

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(150, this);
        timer.start();

        restartButton.setVisible(false);
    }

    public void spawnApple() {
        appleX = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
        appleY = random.nextInt((int) (SIZE / DOT_SIZE)) * DOT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (running) {
            g.drawImage(appleImage, appleX, appleY, this);

            for (int i = 0; i < bodyParts; i++) {
                double rotationAngle = 0;
                if (i < bodyParts - 1) {
                    rotationAngle = calculateRotationAngle(x[i], y[i], x[i + 1], y[i + 1], false);
                }

                AffineTransform old = g2d.getTransform();
                AffineTransform transform = new AffineTransform();
                transform.rotate(rotationAngle, x[i] + DOT_SIZE / 2.0, y[i] + DOT_SIZE / 2.0);
                g2d.setTransform(transform);

                if (i == 0) {
                    double headRotationAngle = 0;
                    switch (direction) {
                        case 'U': headRotationAngle = Math.toRadians(-90); break;
                        case 'D': headRotationAngle = Math.toRadians(90); break;
                        case 'L': headRotationAngle = Math.toRadians(180); break;
                    }

                    AffineTransform headTransform = new AffineTransform();
                    headTransform.rotate(headRotationAngle, x[i] + DOT_SIZE / 2.0, y[i] + DOT_SIZE / 2.0);
                    g2d.setTransform(headTransform);

                    g2d.drawImage(snakeHeadImage, x[i], y[i], this);
                } else if (i == bodyParts - 1) {
                    double tailRotationAngle = calculateRotationAngle(x[i - 1], y[i - 1], x[i], y[i], true);
                    AffineTransform tailTransform = new AffineTransform();
                    tailTransform.rotate(tailRotationAngle, x[i] + DOT_SIZE / 2.0, y[i] + DOT_SIZE / 2.0);
                    g2d.setTransform(tailTransform);

                    g2d.drawImage(snakeTailImage, x[i], y[i], this);
                } else {
                    g2d.drawImage(snakeBodyImage, x[i], y[i], this);
                }

                g2d.setTransform(old);
            }


            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            // Draw the highest score
            g.drawString("High Score: " + highestScore, 10, 30);
            // Draw the current score
            g.drawString("Score: " + applesEaten, 10, 60);

        } else {
            gameOver(g);
        }
    }

    private double calculateRotationAngle(int x1, int y1, int x2, int y2, boolean isTail) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        if (isTail) {
            angle += Math.PI;
        }
        return angle;
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
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            if (applesEaten > highestScore) {
                highestScore = applesEaten; // Update the highest score if current score is greater
            }
            spawnApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts - 1; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0 || x[0] >= SIZE || y[0] < 0 || y[0] >= SIZE) {
            running = false;
        }

        if (!running) {
            timer.stop();
            restartButton.setVisible(true);
        }
    }

    public void gameOver(Graphics g) {
        // "Game Over" text setup
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics40 = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        int xGameOverText = (SIZE - metrics40.stringWidth(gameOverText)) / 2;
        int yGameOverText = SIZE / 2 - 50; // Positioned slightly above the middle
        g.drawString(gameOverText, xGameOverText, yGameOverText);

        // Display the final score
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics30 = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        int xScoreText = (SIZE - metrics30.stringWidth(scoreText)) / 2;
        int yScoreText = SIZE / 2 + 20; // Positioned in the middle
        g.drawString(scoreText, xScoreText, yScoreText);

        // Positioning the restart button
        int buttonWidth = 100;
        int buttonHeight = 30;
        int xButton = (SIZE - buttonWidth) / 2;
        int yButton = SIZE / 2 + 70; // Positioned below the score
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
        JFrame frame = new JFrame("SNAKE GAME");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
