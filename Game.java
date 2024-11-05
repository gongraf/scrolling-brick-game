/***
 Copyright (C) 2015 Gonzalo Graf
 Email: grafgonzalo@gmail.com

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, 51 Franklin Street, Suite 500, Boston, MA 02110-1335, USA.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

class Game extends JPanel implements ActionListener,Commons {

    private final static int DELAY        = 13;
    private final static int INTRO        = 0;
    private final static int IN_GAME      = 1;
    private final static int GAME_OVER    = 2;
    private final static int START        = 3;
    private final static int WIN          = 4;
    private final static int PAUSE        = 5;
    private final static int HI_SCORES    = 6;
    private final static int ADD_HISCORES = 7;

    private int bricksActive;
    private int thinkTime     = MAX_RELOAD;
    private boolean showEnter = true;

    boolean quit;
    int score,level,state;
    String input = "";

    private Player player;
    private Brick[] bricks;
    private SpecialBrick[] spBricks;
    private Ball ball;
    private Background background;
    private HiScore hiscores;
    private LevelMap map;

    Timer timer;

    InputStream in = Game.class.getResourceAsStream("fonts/dos.ttf");
    Font font;

    Game(){
        super();

        loadFont();

        addKeyListener(new Key());
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);

        this.quit = false;

        this.timer = new Timer(DELAY, this);
        this.timer.start();
        this.state = INTRO;

    }

    private void loadFont(){
        // FOR java < 7 support
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        }catch (IOException e) {
            e.printStackTrace();

        }catch (FontFormatException f) {
            f.printStackTrace();
        }
    }

    private void initGame() {

        this.hiscores = new HiScore();

        this.player   = new Player();

        this.ball     = new Ball();

        this.level    = 1;

        this.score    = 0;

        initLevel();

    }

    private void initLevel() {

        this.player.setX(SCREEN_WIDTH /2 - (this.player.getW() /2));

        this.player.setY(834);

        this.ball.setLaunched(false);

        this.map        = new LevelMap(this.level);

        this.background = new Background(this.level);

        this.bricks     = new Brick[this.map.MAX_NORMAL_TILES];

        for (int i = 0; i < this.map.MAX_NORMAL_TILES; i++) {
            this.bricks[i] = new Brick();
            addBrick();
        }

        this.bricksActive = this.map.MAX_NORMAL_TILES;

        this.spBricks     = new SpecialBrick[this.map.MAX_SPECIAL_TILES];

        for (int i = 0; i < this.map.MAX_SPECIAL_TILES; i++) {
            this.spBricks[i] = new SpecialBrick(this.map.tileImages[i]+".png");
            addSpBrick();
        }

    }

    private void changeLevel() {

        this.bricks = null;

        this.spBricks = null;

        this.background = null;

        this.map = null;

        this.level += 1;

        initLevel();

    }

    private void resetGame() {
        this.state = IN_GAME;
        initGame();
    }

    @Override
    public void actionPerformed(ActionEvent e){

        switch(this.state) {
            case INTRO:
                blinkEnter();
                break;
            case IN_GAME:
                doGame();
                doCollisions();
                break;
            case GAME_OVER:
                blinkEnter();
                break;
            case ADD_HISCORES:
                blinkEnter();
                break;
            case HI_SCORES:
                blinkEnter();
                break;
            default:
                break;
        }

        if(quit)this.timer.stop();

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        switch(this.state) {
            case INTRO:
                this.font = font.deriveFont(Font.PLAIN, 30);

                g.setFont(font);

                g.setColor(Color.cyan);

                g.drawString("Scrolling Brick Game!", 10, 50);
                /***************/
                this.font = font.deriveFont(Font.PLAIN, 20);

                g.setFont(font);
                g.setColor(Color.cyan);
                if(showEnter) g.drawString("Press Enter", 130, SCREEN_HEIGHT /2 + 50);
                /***************/
                this.font = font.deriveFont(Font.PLAIN, 15);

                g.setFont(font);

                g.setColor(Color.white);

                g.drawString("(c) 2015 Gonzalo Graf", 95, 80);

                break;

            case IN_GAME:
                g.drawImage(this.background.getImage(),0,0,SCREEN_WIDTH,SCREEN_HEIGHT,0,(int)background.y,SCREEN_WIDTH,(int)background.y + SCREEN_HEIGHT,this);

                g.drawImage(this.player.getImage(),this.player.getX() - this.map.startX,this.player.getY()-this.map.startY, this);

                for (int i = 0; i < this.map.MAX_NORMAL_TILES; i++) {

                    if (!this.bricks[i].isActive()) continue;

                    int x =  this.bricks[i].getX() - this.map.startX;

                    int y =  this.bricks[i].getY() - this.map.startY;

                    g.drawImage(this.bricks[i].getImage(),x,y,this);
                }

                for (int s = 0; s < this.map.MAX_SPECIAL_TILES; s++) {

                    if (!this.spBricks[s].isActive()) continue;

                    int x =  this.spBricks[s].getX() - this.map.startX;

                    int y =  this.spBricks[s].getY() - this.map.startY;

                    g.drawImage(this.spBricks[s].getImage(),x,y,this);
                }

                g.drawImage(this.ball.getImage(),(int)this.ball.getX() - this.map.startX ,(int)this.ball.getY() - this.map.startY,this);

                this.font = font.deriveFont(Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(Color.WHITE);

                g.drawString("Score:" + this.score, 10, 15);

                g.drawString("Level:" + this.level, 290, 15);

                g.drawString("Resets:" + this.ball.resets, SCREEN_WIDTH/2-35, 15);

                g.setColor(Color.cyan);
                if(!this.ball.isLaunched())
                    g.drawString("Press Space Bar", SCREEN_WIDTH/2-76,SCREEN_HEIGHT/2);

                break;

            case GAME_OVER:
                this.font = font.deriveFont(Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(Color.white);

                g.drawString("Score: " + this.score, 10, 15);


                this.font = font.deriveFont(Font.PLAIN, 30);
                g.setFont(font);
                g.drawString("Game Over!!", 105, SCREEN_HEIGHT / 2);

                this.font = font.deriveFont(Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(Color.cyan);
                if(showEnter) g.drawString("Press Enter", 130, SCREEN_HEIGHT /2 + 50);

                break;
            case HI_SCORES:
                this.font = font.deriveFont(Font.PLAIN, 40);
                g.setFont(font);
                g.setColor(Color.cyan);

                g.drawString("***HI SCORES***",20,50);


                this.font = font.deriveFont(Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(Color.white);

                int y = 100;
                for(int i = 0;i<hiscores.table.length;i++){
                    g.drawString((i+1)+". "+hiscores.table[i][NAME],10,y+(i*30));
                    g.drawString(hiscores.table[i][SCORE],SCREEN_WIDTH/2,y+(i*30));
                }

                g.setColor(Color.cyan);
                if(showEnter) g.drawString("Press Enter", 130, SCREEN_HEIGHT /2 + 160);

                break;
            case ADD_HISCORES:
                this.font = font.deriveFont(Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("New HiScore!",20,30);
                g.drawString("Type your name, and press enter.",20,55);
                g.drawString(">"+input,20,75);
                break;
            default:
                break;
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void doGame () {

        this.player.doPlayer();

        this.ball.doEntity(player,map);

        this.background.doBackground(map);

    }

    private void doCollisions() {


        for (int b = 0; b < this.map.MAX_NORMAL_TILES; b++){

            if(!bricks[b].isActive()) continue;

            if(collision((int)ball.getX(),(int)ball.getY(),ball.getW(),ball.getH(),bricks[b].getX(),bricks[b].getY(),bricks[b].getW(),bricks[b].getH())) {

                computeBallDir(this.ball.getX(),this.bricks[b].getX(),this.bricks[b].getW(),0.29);

                bricks[b].setActive(false);

                this.score += 10;

                if ( (this.score % 1000) == 0 ) ball.resets += 1;

                this.bricksActive -=1;

                if (this.bricksActive == 0) changeLevel();

                break;
            }

        }


        for (int b = 0; b < this.map.MAX_SPECIAL_TILES; b++){

            if(!spBricks[b].isActive()) continue;

            if(collision((int)ball.getX(),(int)ball.getY(),ball.getW(),ball.getH(),spBricks[b].getX(),spBricks[b].getY(),spBricks[b].getW(),spBricks[b].getH())) {

                computeBallDir(this.ball.getX(),this.spBricks[b].getX(),this.spBricks[b].getW(),0.15);

                if(spBricks[b].getHits() > 1) {
                    spBricks[b].setHits();
                    break;
                }
                else {
                    spBricks[b].setActive(false);
                }

                this.score += 10;

                if ( (this.score % 1000) == 0 ) ball.resets += 1;

                break;
            }

        }

        if(collision((int)ball.getX(),(int)ball.getY(),ball.getW()-1,ball.getH(),player.getX(),player.getY(),48,5)){

            this.ball.setY(this.ball.getY()- 2);
            computeBallDir(this.ball.getX(),player.getX(),this.player.getW(),0.75);

        }

        if (!this.ball.isAlive()) {
            this.state = GAME_OVER;
        }

    }

    private void computeBallDir(double ballX, double entityX, int entityW, double influence) {

        final double influenceX = influence;

        int ballWidth = this.ball.getW();
        double ballCenterX = ballX + ballWidth/2;
        int paddleWidth = entityW;
        double paddleCenterX = entityX + paddleWidth/2;
        double speedX = this.ball.getDirX();
        double speedY = this.ball.getDirY();

        double speedXY = Math.sqrt(speedX*speedX + speedY*speedY);

        double posX = (ballCenterX - paddleCenterX) / (paddleWidth/2);

        this.ball.hits += 1;

        if ( (ball.hits % 5) == 0 ) speedXY += 0.5;

        if(Math.abs(posX) > 0.5) {
            if(speedXY < 10)speedXY += 1;
        }
        else {
            speedXY = 5;
        }

        speedX = speedXY * posX * influenceX;

        this.ball.setDirX(speedX);

        speedY = Math.sqrt(speedXY*speedXY - speedX*speedX) * (speedY > 0? -1 : 1);

        this.ball.setDirY(speedY);
    }

    private void addSpBrick() {
        int i = getFreeSpEntity();

        if (i == -1) return;

        this.spBricks[i].setX(this.map.levelSpecialTiles[i][1]);
        this.spBricks[i].setY(this.map.levelSpecialTiles[i][0]);
        this.spBricks[i].setActive(true);

    }

    private void addBrick() {
        int i = getFreeEntity();

        if (i == -1) return;

        this.bricks[i].setX(this.map.levelMap[i][1]);
        this.bricks[i].setY(this.map.levelMap[i][0]);
        this.bricks[i].setActive(true);

    }

    private int getFreeEntity() {

        for (int i = 0; i < this.map.MAX_NORMAL_TILES; i++) {

            if(!this.bricks[i].isActive()){
                this.bricks[i].setActive(true);
                return i;
            }

        }

        return -1;
    }

    private int getFreeSpEntity() {

        for (int i = 0; i < this.map.MAX_SPECIAL_TILES; i++) {

            if(!this.spBricks[i].isActive()){

                this.spBricks[i].setActive(true);
                return i;
            }

        }

        return -1;
    }

    private boolean collision (int x0,int y0,int w0,int h0,int x2,int y2,int w1,int h1) {

        //e1X+e1W
        int x1 = x0 + w0 - 1;

        //e1Y+e1H
        int y1 = y0 + h0 - 1;

        //e2X+e2W
        int x3 = x2 + w1 - 1;

        //e2Y+e2H
        int y3 = y2 + h1 - 1;

        return !(x1<x2 || x3<x0 || y1<y2 || y3<y0);

    }

    private void blinkEnter(){
        if(--thinkTime == 0) {
            if(showEnter == true) showEnter = false;
            else if (showEnter == false) showEnter = true;
            thinkTime = MAX_RELOAD;
        }
    }

    private class Key extends KeyAdapter {

        public void keyPressed(KeyEvent e){

            if(state == ADD_HISCORES){

                switch(e.getKeyCode()){

                    case KeyEvent.VK_A:
                        input = input+"a";
                        break;
                    case KeyEvent.VK_B:
                        input = input+"b";
                        break;
                    case KeyEvent.VK_C:
                        input = input+"c";
                        break;
                    case KeyEvent.VK_D:
                        input = input+"d";
                        break;
                    case KeyEvent.VK_E:
                        input = input+"e";
                        break;
                    case KeyEvent.VK_F:
                        input = input+"f";
                        break;
                    case KeyEvent.VK_G:
                        input = input+"g";
                        break;
                    case KeyEvent.VK_H:
                        input = input+"h";
                        break;
                    case KeyEvent.VK_I:
                        input = input+"i";
                        break;
                    case KeyEvent.VK_J:
                        input = input+"j";
                        break;
                    case KeyEvent.VK_K:
                        input = input+"k";
                        break;
                    case KeyEvent.VK_M:
                        input = input+"m";
                        break;
                    case KeyEvent.VK_N:
                        input = input+"n";
                        break;
                    case KeyEvent.VK_L:
                        input = input+"l";
                        break;
                    case KeyEvent.VK_O:
                        input = input+"o";
                        break;
                    case KeyEvent.VK_P:
                        input = input+"p";
                        break;
                    case KeyEvent.VK_Q:
                        input = input+"q";
                        break;
                    case KeyEvent.VK_R:
                        input = input+"r";
                        break;
                    case KeyEvent.VK_S:
                        input = input+"s";
                        break;
                    case KeyEvent.VK_T:
                        input = input+"t";
                        break;
                    case KeyEvent.VK_U:
                        input = input+"u";
                        break;
                    case KeyEvent.VK_V:
                        input = input+"v";
                        break;
                    case KeyEvent.VK_W:
                        input = input+"w";
                        break;
                    case KeyEvent.VK_X:
                        input = input+"x";
                        break;
                    case KeyEvent.VK_Y:
                        input = input+"y";
                        break;
                    case KeyEvent.VK_Z:
                        input = input+"z";
                        break;
                    case KeyEvent.VK_0:
                        input = input+"0";
                        break;
                    case KeyEvent.VK_1:
                        input = input+"1";
                        break;
                    case KeyEvent.VK_2:
                        input = input+"2";
                        break;
                    case KeyEvent.VK_3:
                        input = input+"3";
                        break;
                    case KeyEvent.VK_4:
                        input = input+"4";
                        break;
                    case KeyEvent.VK_5:
                        input = input+"5";
                        break;
                    case KeyEvent.VK_6:
                        input = input+"6";
                        break;
                    case KeyEvent.VK_7:
                        input = input+"7";
                        break;
                    case KeyEvent.VK_8:
                        input = input+"8";
                        break;
                    case KeyEvent.VK_9:
                        input = input+"9";
                        break;
                }
            }

            if(e.getKeyCode() == KeyEvent.VK_SPACE && state == IN_GAME){
                if(!ball.isLaunched()) {
                    ball.setLaunchDir();
                    ball.setLaunched(true);
                }
            }

            else if(e.getKeyCode() == KeyEvent.VK_LEFT && state == IN_GAME){
                player.setLeft(true);
            }

            else if(e.getKeyCode() == KeyEvent.VK_RIGHT && state == IN_GAME){
                player.setRight(true);
            }

            if(e.getKeyCode() == KeyEvent.VK_ENTER){

                switch(state){
                    case GAME_OVER:

                        if(hiscores.isHiscore(score)) {
                            state = ADD_HISCORES;
                        }
                        else {
                            state = HI_SCORES;
                        }

                        break;
                    case ADD_HISCORES:
                        hiscores.addNewRecord(score,input);
                        input = "";
                        state = HI_SCORES;
                        break;
                    case HI_SCORES:
                        state = INTRO;
                        break;
                    case INTRO:
                        resetGame();
                        break;
                    default:
                        break;
                }
            }
        }

        public void keyReleased(KeyEvent e){

            if(e.getKeyCode() == KeyEvent.VK_LEFT && state == IN_GAME){
                player.setLeft(false);
            }

            else if(e.getKeyCode() == KeyEvent.VK_RIGHT && state == IN_GAME){
                player.setRight(false);
            }
        }
    }
}
