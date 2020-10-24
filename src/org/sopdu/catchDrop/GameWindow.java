package org.sopdu.catchDrop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow  extends JFrame {
    private static GameWindow gameWindow;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image drop;
    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropSpeed = 200;
    private static int  score;
    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("resurse/img/background.png"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("resurse/img/game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("resurse/img/drop.png"));
	    gameWindow = new GameWindow();
	    gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // При закрытии окна программа завершаеться
        gameWindow.setLocation(200, 100); // Позиция окна на экране
        gameWindow.setSize(906, 478); // Размер окна
        gameWindow.setResizable(false); // Изменение размера окна - запрещено
        lastFrameTime = System.nanoTime();
        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft+drop.getWidth(null);
                float dropBottom = dropTop+drop.getHeight(null);
                boolean isDrop = x>= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;
                if(isDrop){
                    dropTop = -100;
                    dropLeft = (int)(Math.random()*(gameField.getWidth()-drop.getWidth(null)));
                    dropSpeed = dropSpeed+30;
                    score++;
                    gameWindow.setTitle("Очки: "+score);
                }
            }
        });
        gameWindow.add(gameField);
        gameWindow.setVisible(true); // Включение видимости окна
    }
    private static void onReaint(Graphics graphics){
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime)*0.000000001f;
        lastFrameTime = currentTime;
        dropTop = dropTop+dropSpeed*deltaTime;
        graphics.drawImage(background, 0, 0, null);
        graphics.drawImage(drop, (int)dropLeft, (int)dropTop, null);
        if(dropTop > gameWindow.getHeight()) { // если больше окна
            graphics.drawImage(gameOver, 280, 120, null);
        }
    }
    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics graphics){
            super.paintComponent(graphics);
            onReaint(graphics);
            repaint();
        }
    }
}
