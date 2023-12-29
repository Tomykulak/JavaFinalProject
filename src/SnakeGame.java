import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

import static java.awt.Event.DOWN;
import static java.awt.Event.UP;
import static java.lang.Long.SIZE;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;

public class SnakeGame extends JPanel implements ActionListener {
    private GameManager gameManager;
    private Timer timer;
    private JButton restartButton;
    private Image backgroundImage;
    private Image snakeHeadImage;
    private Image snakeBodyImage;
    private Image snakeTailImage;

    private Snake snake;
    private Apple apple;

    public SnakeGame() {
        snake = new Snake();
        apple = new Apple();
        gameManager = new GameManager(snake, apple);

        this.setPreferredSize(new Dimension(gameManager.getSIZE(), gameManager.getSIZE()));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        ImageIcon ibg = new ImageIcon("images/sforestBackground.jpg");
        backgroundImage = ibg.getImage().getScaledInstance(gameManager.getSIZE(), gameManager.getSIZE(), Image.SCALE_SMOOTH);

        ImageIcon iit = new ImageIcon("images/snakeTail.png");
        snakeTailImage = iit.getImage().getScaledInstance(gameManager.getSIZE(), gameManager.getDOT_SIZE(), Image.SCALE_SMOOTH);

        ImageIcon iib = new ImageIcon("images/snakeBody.png");
        snakeBodyImage = iib.getImage().getScaledInstance(gameManager.getSIZE(), gameManager.getDOT_SIZE(), Image.SCALE_SMOOTH);

        ImageIcon iis = new ImageIcon("images/snakeHead.png");
        snakeHeadImage = iis.getImage().getScaledInstance(gameManager.getSIZE(), gameManager.getDOT_SIZE(), Image.SCALE_SMOOTH);

        restartButton();
        restartButton.setVisible(false);
        add(restartButton);

        startGame();
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
        timer = new Timer(150, this);
        timer.start();

        restartButton.setVisible(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        draw(g);
    }

    private void draw(Graphics g) {
        if (gameManager.isRunning()) {
            g.drawImage(apple.getAppleImage(), apple.getAppleX(), apple.getAppleY(), this);
            // Draw Snake
            Graphics2D g2d = (Graphics2D) g;


            for (int i = 0; i < snake.getBodyParts(); i++) {
                if (i == 0) {
                    // Rotate the head image based on the direction
                    AffineTransform transform = new AffineTransform();
                    double rotationAngle = 0.0;

                    switch (gameManager.getDirection()) {
                        case 'U': // Up
                            rotationAngle = -Math.PI / 2; // Corrected angle for up
                            break;
                        case 'D': // Down
                            rotationAngle = Math.PI / 2; // Corrected angle for down
                            break;
                        case 'L': // Left
                            rotationAngle = Math.PI; // Angle for left
                            break;
                        case 'R': // Right
                            // No rotation needed for right
                            break;
                    }

                    int headX = gameManager.getX()[0];
                    int headY = gameManager.getY()[0];

                    transform.translate(headX + gameManager.getDOT_SIZE() / 2.0, headY + gameManager.getDOT_SIZE() / 2.0);
                    transform.rotate(rotationAngle);
                    transform.translate(-gameManager.getDOT_SIZE() / 2.0, -gameManager.getDOT_SIZE() / 2.0);

                    g2d.drawImage(snake.getSnakeHeadImage(), transform, null);
                } else {
                    // Draw body
                    g2d.drawImage(snake.getSnakeBodyImage(), gameManager.getX()[i], gameManager.getY()[i], this);
                }
            }
            // Draw text
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            // Draw the highest score
            g.drawString("High Score: " + snake.getMaxApplesEaten(), 10, 30);
            // Draw the current score
            g.drawString("Score: " + snake.getApplesEaten(), 10, 60);
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        // "Game Over" text setup
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics40 = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        int xGameOverText = (gameManager.getSIZE() - metrics40.stringWidth(gameOverText)) / 2;
        int yGameOverText = gameManager.getSIZE() / 2 - 50; // Positioned slightly above the middle
        g.drawString(gameOverText, xGameOverText, yGameOverText);

        // Display the final score
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics30 = getFontMetrics(g.getFont());
        String scoreText = "Score: " + snake.getApplesEaten();
        int xScoreText = (gameManager.getSIZE() - metrics30.stringWidth(scoreText)) / 2;
        int yScoreText = gameManager.getSIZE() / 2 + 20; // Positioned in the middle
        g.drawString(scoreText, xScoreText, yScoreText);

        // Positioning the restart button
        int buttonWidth = 100;
        int buttonHeight = 30;
        int xButton = (gameManager.getSIZE() - buttonWidth) / 2;
        int yButton = gameManager.getSIZE() / 2 + 70; // Positioned below the score
        restartButton.setBounds(xButton, yButton, buttonWidth, buttonHeight);
        restartButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameManager.isRunning()) {
            gameManager.move();
            gameManager.checkApple();
            gameManager.checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            gameManager.setDirectionBasedOnKey(e.getKeyCode());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
