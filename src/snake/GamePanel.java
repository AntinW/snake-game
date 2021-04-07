/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 *
 * @author Antin Williams
 */
public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GameFrame frame;
    
    public GamePanel(){
        random = new Random();
        setGUI();
    }
    
    public final void setGUI(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
    }
    //what happens at game start? create new apple, set running true, instantiate timer and call start method on it
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

     @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    } 
    //draws components
    public void draw(Graphics g){
        if(running){
            //Draws gridlines
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            // draws apple
            g.setColor(Color.magenta);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            //draws snake
            for(int i = 0; i < bodyParts; i++){
                //this part fills the head of snake
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
                }
                //fills body of snake
                else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillOval(x[i],y[i],UNIT_SIZE, UNIT_SIZE);
                }
            }
            //draws score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " +applesEaten))/2 , g.getFont().getSize());

        }
        else{
            gameOver(g);
        }
    }
    //generates new apple at random location on screen
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(int i = 0; i <= bodyParts; i++){
            if(x[i] == appleX && y[i] == appleY){
                appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
                appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
            }
        }
    }
    //moves snake
    public void move(){
        //replaces each body part with the one before it in the array
        for(int i = bodyParts;i > 0;i--){
            x[i] = x[i -1];
            y[i] = y[i -1];
        }
        //changing direction
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
                
            
        }
    }
    //checks if apple has been eaten
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //checks if head collides with body
        for(int i = 1; i <= bodyParts;i++){
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
        }
        //checks if head touches left border
        if(x[0] < 0){
            running = false;
        }
        //checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //checks if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }
        
    }
    public void gameOver(Graphics g){
        //score text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " +applesEaten))/2 , g.getFont().getSize());
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2 , SCREEN_HEIGHT/2);
   
    }
    public void restart(){
        if(!running){
            bodyParts =6;
            applesEaten = 0;
            newApple();
            running = true;
            timer.restart();
            repaint();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();   
        }
        //refreshes game after each movement
        repaint();
    }
    public class myKeyAdapter extends KeyAdapter{
        @Override
        //reacts to player input
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(timer.isRunning()){
                    timer.stop();
                    }
                    else{
                        timer.start();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    restart();
                    break;
            }
        }
    }
    
}
