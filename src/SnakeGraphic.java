import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class SnakeGraphic extends JPanel implements ActionListener {
    private final GameManager gameManager;
    private Timer timer;
    private JButton restartButton;
    private final JButton startButton;
    private final Image backgroundImage;
    private final Snake snake;
    private final Apple apple;
    private boolean keyHeldDown = false; // Added flag to track if a key is held down
    private final int delay = 75; // Initial delay value (adjust as needed)

    public SnakeGraphic() {
        snake = new Snake();
        apple = new Apple();
        gameManager = new GameManager(snake, apple);

        this.setPreferredSize(new Dimension(gameManager.getSIZE(), gameManager.getSIZE()));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        ImageIcon ibg = new ImageIcon("images/waterBackground.jpg");
        backgroundImage = ibg.getImage().getScaledInstance(gameManager.getSIZE(), gameManager.getSIZE(), Image.SCALE_SMOOTH);

        // Calculate center position for the button
        int buttonWidth = 150;
        int buttonHeight = 40;
        int xButton = gameManager.getSIZE() / 2 - buttonWidth / 2;
        int yButton = gameManager.getSIZE() / 2 - buttonHeight / 2 + 50;
        startButton = new JButton("Start Game");
        startButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        restartButton();
        restartButton.setVisible(false);
        add(restartButton);

        gameManager.setRunning(false);
    }

    private void restartButton() {
        restartButton = new JButton("Restart");
        restartButton.setBounds(gameManager.getSIZE() / 2 - 50, gameManager.getSIZE() / 2, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                repaint();
            }
        });
    }

    private void startGame() {
        gameManager.initializeGame();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(delay, this);
        timer.start();
        startButton.setVisible(false);
        restartButton.setVisible(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        draw(g);
    }

    private void draw(Graphics g) {
        if (gameManager.isRunning()) {
            drawApple(g);
            drawSnake(g);
            drawScores(g);
        } else if (!gameManager.hasStarted()) {
            drawWelcomeScreen(g);
        } else {
            gameOver(g);
        }
    }

    private void drawApple(Graphics g) {
        g.drawImage(apple.getAppleImage(), apple.getAppleX(), apple.getAppleY(), this);
    }

    private void drawSnake(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < snake.getBodyParts(); i++) {
            if (i == 0) {
                // Draw head
                drawSnakeHead(g2d, gameManager.getX()[i], gameManager.getY()[i]);
            } else if (i == snake.getBodyParts() - 1) {
                // Draw tail for the last part
                drawSnakeTail(g2d, gameManager.getX()[i], gameManager.getY()[i]);
            } else {
                // Draw body for other parts
                drawSnakeBody(g2d, gameManager.getX()[i], gameManager.getY()[i]);
            }
        }
    }



    private void drawSnakeHead(Graphics2D g2d, int x, int y) {
        AffineTransform transform = new AffineTransform();
        double rotationAngle = getRotationAngle();
        transform.translate(x + gameManager.getDOT_SIZE() / 2.0, y + gameManager.getDOT_SIZE() / 2.0);
        transform.rotate(rotationAngle);
        transform.translate(-gameManager.getDOT_SIZE() / 2.0, -gameManager.getDOT_SIZE() / 2.0);
        g2d.drawImage(snake.getSnakeHeadImage(), transform, null);
    }

    private void drawSnakeBody(Graphics2D g2d, int x, int y) {
        g2d.drawImage(snake.getSnakeBodyImage(), x, y, this);
    }

    private void drawSnakeTail(Graphics2D g2d, int x, int y) {
        AffineTransform transform = new AffineTransform();
        double rotationAngle = getTailRotationAngle();
        transform.translate(x + gameManager.getDOT_SIZE() / 2.0, y + gameManager.getDOT_SIZE() / 2.0);
        transform.rotate(rotationAngle);
        transform.translate(-gameManager.getDOT_SIZE() / 2.0, -gameManager.getDOT_SIZE() / 2.0);
        g2d.drawImage(snake.getSnakeTailImage(), transform, null);
    }


    private void drawScores(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("High Score: " + snake.getMaxApplesEaten(), 10, 30);
        g.drawString("Score: " + snake.getApplesEaten(), 10, 60);
    }

    private double getRotationAngle() {
        switch (gameManager.getDirection()) {
            case 'U': return -Math.PI / 2;
            case 'D': return Math.PI / 2;
            case 'L': return Math.PI;
            case 'R': return 0.0;
        }
        return 0.0;
    }

    private double getVerticalTailRotationAngle(int tailY, int penultimateY) {
        // Tail is moving up if the penultimate segment is below the tail
        if (penultimateY > tailY) {
            return Math.PI / 2; // Tail pointing up
        } else {
            return -Math.PI / 2; // Tail pointing down
        }
    }

    private double getHorizontalTailRotationAngle(int tailX, int penultimateX) {
        // Tail is moving right if the penultimate segment is to the left of the tail
        if (penultimateX < tailX) {
            return Math.PI; // Tail pointing right
        } else {
            return 0; // Tail pointing left
        }
    }

    private double getTailRotationAngle() {
        int tailIndex = snake.getBodyParts() - 1;
        int penultimateIndex = tailIndex - 1;

        int tailX = gameManager.getX()[tailIndex];
        int tailY = gameManager.getY()[tailIndex];
        int penultimateX = gameManager.getX()[penultimateIndex];
        int penultimateY = gameManager.getY()[penultimateIndex];

        if (penultimateX == tailX) {
            // Vertical movement (up or down)
            return getVerticalTailRotationAngle(tailY, penultimateY);
        } else {
            // Horizontal movement (left or right)
            return getHorizontalTailRotationAngle(tailX, penultimateX);
        }
    }



    private void drawWelcomeScreen(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String welcomeText = "Welcome to Snake Game";
        int xText = (gameManager.getSIZE() - metrics.stringWidth(welcomeText)) / 2;
        int yText = gameManager.getSIZE() / 2;
        g.drawString(welcomeText, xText, yText);

        // Position the start button below the welcome text
        int buttonWidth = 150;
        int buttonHeight = 40;
        int xButton = gameManager.getSIZE() / 2 - buttonWidth / 2;
        int yButton = yText + metrics.getHeight(); // Place the button right below the text
        startButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
    }


    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics40 = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        int xGameOverText = (gameManager.getSIZE() - metrics40.stringWidth(gameOverText)) / 2;
        int yGameOverText = gameManager.getSIZE() / 2 - 50;
        g.drawString(gameOverText, xGameOverText, yGameOverText);
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics30 = getFontMetrics(g.getFont());
        String scoreText = "Score: " + snake.getApplesEaten();
        int xScoreText = (gameManager.getSIZE() - metrics30.stringWidth(scoreText)) / 2;
        int yScoreText = gameManager.getSIZE() / 2 + 20;
        g.drawString(scoreText, xScoreText, yScoreText);
        int buttonWidth = 100;
        int buttonHeight = 30;
        int xButton = (gameManager.getSIZE() - buttonWidth) / 2;
        int yButton = gameManager.getSIZE() / 2 + 70;
        restartButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
        restartButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameManager.isRunning()) {
            gameManager.move();
            gameManager.checkApple();
            snake.updateMaxApplesEaten();
            gameManager.checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        private long lastKeyPressTime = 0;

        @Override
        public void keyPressed(KeyEvent e) {
            gameManager.setDirectionBasedOnKey(e.getKeyCode());
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastKeyPressTime < 300) {
                keyHeldDown = true; // If key is pressed again within 300 milliseconds, consider it as holding
                // Speed multiplier when a key is held down
                int speedMultiplier = 2;
                timer.setDelay(delay / speedMultiplier); // Increase the game speed
            }
            lastKeyPressTime = currentTime;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyHeldDown = false;
            timer.setDelay(delay); // Restore the original game speed
        }
    }

}
