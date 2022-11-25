package game2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.sound.sampled.Clip;
import java.io.*;
import java.util.Date;

public class maingame extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    private boolean started = false;
    private boolean locked = true;

    private Timer time;

    private int delay = 14;
    private int maxBall = 10;
    private int screen_x = 961, screen_y = 637;
    private int ball_speed = 5;
    private int wave = 1;
    private boolean gameOver = false;
    private GameObject platform;

    private ArrayList<Ball> Balls = new ArrayList<>();
    private ArrayList<Brick> obstacles = new ArrayList<>();

    private Image waveMessage;

    ImageIcon bgi = new ImageIcon(getClass().getResource("/game2/resources/GAMEbg.jpg"));
    ImageIcon paddle = new ImageIcon(getClass().getResource("/game2/resources/croppedpaddlecat.png"));
    ImageIcon brick = new ImageIcon(getClass().getResource("/game2/resources/cropbrickdoge.png"));
    ImageIcon brick_pain = new ImageIcon(getClass().getResource("/game2/resources/cropbrick_shy.png"));
    ImageIcon catBall = new ImageIcon(getClass().getResource("/game2/resources/pballcat.png"));
    ImageIcon pausebg = new ImageIcon(getClass().getResource("/game2/resources/pausebg.png"));
    ImageIcon pause = new ImageIcon(getClass().getResource("/game2/resources/pause.png"));
    ImageIcon defeated = new ImageIcon(getClass().getResource("/game2/resources/defeated.png"));
    Clip bgMusic = Utils.clipReader(getClass().getResourceAsStream("/game2/resources/Intro.wav"));
    Clip ouch = Utils.clipReader(getClass().getResourceAsStream("/game2/resources/hit.wav"));
    Clip ded = Utils.clipReader(getClass().getResourceAsStream("/game2/resources/death.wav"));
    boolean bgPlaying = false;
    boolean somethingWasHit = false;
    boolean somethingDed = false;

    private int ballSize = 30;
    private int startPosX = screen_x / 2, startPosY = screen_y - 100;
    private int dogHealth = 2;

    public maingame() {
        bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        this.Balls.add(new Ball(startPosX, startPosY - ballSize, ballSize, ballSize, false, true, 0, screen_x, screen_y));
        this.platform = new GameObject(startPosX, startPosY, 100, 35, screen_x, screen_y);
        this.generateLevel();

        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
    }

    public void generateLevel() {
        for (int i = 0; i < 30 + (wave * 5); i++) {
            this.obstacles.add(new Brick(
                    (int) Utils.randint(0, screen_x - 100),
                    (int) Utils.randint(0, screen_y / 2),
                    100, 40, screen_x, screen_y, dogHealth)
            );
        }
        this.waveMessage = Utils.drawText("WAVE:" + this.wave);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //background
        g.drawImage(bgi.getImage(), 0, 0, null);

        //texts
        g.drawImage(this.waveMessage, 0, 540, null);

        //obstacle
        for (Brick ob : this.obstacles) {
            ImageIcon selected = ((new Date().getTime()) - ob.getLasthitStamp() < 500)?brick_pain:brick;
            g.drawImage(selected.getImage(), ob.x, ob.y, ob.len_x, ob.len_y, null);
        }

        //paddle
        g.drawImage(paddle.getImage(), this.platform.x, this.platform.y, this.platform.len_x, this.platform.len_y, null);

        for (GameObject b : this.Balls) {
            g.drawImage(catBall.getImage(), b.x, b.y, b.len_x, b.len_y, null);
        }

        boolean shouldPlayBg = this.started && !this.gameOver;
        if (shouldPlayBg != bgPlaying) {
            if (shouldPlayBg) {
                bgMusic.start();
            } else {
                bgMusic.stop();
            }
            bgPlaying = shouldPlayBg;
        }

        if (somethingWasHit || somethingDed) {
            if (somethingDed) {
                ded.setFramePosition(0);
                ded.start();
                somethingDed = false;
            } else {
                ouch.setFramePosition(0);
                ouch.start();
            }
            somethingWasHit = false;
        }

        if (!this.started) {
            g.drawImage(this.pausebg.getImage(), 0, 0, null);
            g.drawImage(this.pause.getImage(), 350, 200, 250, 75, null);
        }
        if (this.gameOver) {
            g.drawImage(this.pausebg.getImage(), 0, 0, null);
            g.drawImage(this.defeated.getImage(), 250, 50, 350, 125, null);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (started) {

            this.platform.x = Math.min(e.getX(), screen_x);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.gameOver) {
            this.gameOver = false;
            this.Balls.add(new Ball(startPosX, startPosY - ballSize, ballSize, ballSize, false, true, 0, screen_x, screen_y));
            this.wave = 0;
            obstacles.clear();
            generateLevel();
        } else {
            this.started = !this.started;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!started || gameOver) {
            return;
        }
        for (int i = 0; i < this.ball_speed; i++) {
            ArrayList<Ball> safeClone = new ArrayList<>();
            safeClone.addAll(this.Balls);
            for (Ball ball : safeClone) {
                for (Brick ob : this.obstacles) {
                    if (ball.collide(ob)) {
                        ob.hit();
                        somethingWasHit = true;
                        if (Math.random() < 0.1 && this.Balls.size() < this.maxBall) {
                            for (Ball x : ball.blowUp()) {
                                this.Balls.add(x);
                            }
                        }
                        if (Math.random() < 0.17 && Math.random() > 0.05) {
                            ball_speed++;
                        }
                        if (Math.random() < 0.075 && Math.random() > 0.07) {
                            if (ball_speed - 1 > 0) {
                                ball_speed = +5;
                            }
                        }

                    }
                }
                for (Ball other : safeClone) {
                    if (other == ball) {
                        continue;
                    }
                    ball.collide(other);
                }
                this.Balls.removeIf(b -> b.isDestroyed());
                int prevSize = this.obstacles.size();
                this.obstacles.removeIf(ob -> ob.isDead());
                if(prevSize != this.obstacles.size()){
                 somethingDed = true;   
                }
                if (this.obstacles.size() <= 0) {
                    this.wave++;
                    this.generateLevel();
                }
                ball.updatePos(this.platform);

            }
        }
        if (this.Balls.size() == 0) {
            gameOver = true;
        }
        repaint();
    }

}
