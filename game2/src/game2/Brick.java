    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game2;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Brick extends GameObject{
    private int health;
    private long lastHit;
    public Brick(int x, int y, int len_x, int len_y, int screen_x, int screen_y,int health) {
        super(x, y, len_x, len_y, screen_x, screen_y);
        this.health = health;
    }
    public void hit(){
        this.health -= 1;
        this.lastHit = (new Date().getTime()); 
   }
    public long getLasthitStamp(){
        return this.lastHit;
    }
    public int getHealth(){
        return this.health;
    }
    public boolean isDead(){
        return this.getHealth() <= 0;
    }
}