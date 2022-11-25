/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game2;

/**
 *
 * @author AdMath.ministrator
 */
public class GameObject {

    public int x, y;
    protected int lastHitX, lastHitY;
    protected int len_x, len_y;
    protected boolean isDead = false;
    protected int screen_x, screen_y;
    
    public GameObject() {
        this.x = 0;
        this.y = 0;
        this.len_x = 0;
        this.len_y = 0;
        this.screen_x = 0;
        this.screen_y = 0;
        this.updateLasthit();
    }
    public GameObject(int x, int y, int len_x, int len_y, int screen_x, int screen_y) {
        this.x = x;
        this.y = y;
        this.len_x = len_x;
        this.len_y = len_y;
        this.screen_x = screen_x;
        this.screen_y = screen_y;
        this.updateLasthit();
    }

    void updateLasthit() {
        this.lastHitX = x;
        this.lastHitY = y;
    }

    int validSum(int value, int addition, int bound) {
        int result = value + addition;
        if (result > bound) {
            result = bound;
        } else if (result <= 0) {
            result = 0;
        }
        return result;
    }

    boolean isfullContact(int p1, int p2, int len1, int len2) {
        return p1 >= p2 && p1 + len1 <= p2 + len2;
    }

    CollisionResult getCollision(GameObject another) {
        if (this.isDead || another.isDead) {
            return new CollisionResult(0, 0, false);
        }

        int xMin = Math.max(this.x - another.len_x, 0);
        int xMax = Math.min(this.x + this.len_x, this.screen_x);
        boolean TouchX = another.x >= xMin && another.x <= xMax;
        int yMin = Math.max(this.y - another.len_y, 0);
        int yMax = Math.min(this.y + this.len_y, screen_y);
        boolean TouchY = another.y >= yMin && another.y <= yMax;

        int diffX = another.x - this.x;
        int diffY = another.y - this.y;

        if (TouchX && TouchY) {
            this.lastHitX = x;
            this.lastHitY = y;
            if (isfullContact(x, another.x, len_x, another.len_x) || isfullContact(another.x, x, another.len_x, len_x)) {
                diffX = 0;
            }
            if (isfullContact(y, another.y, len_y, another.len_y) || isfullContact(another.y, y, another.len_y, len_y)) {
                diffY = 0;
            }
            this.updateLasthit();
        }
        return new CollisionResult(diffX, diffY, TouchX && TouchY);
    }

    boolean isTouching(GameObject another) {//simple check
        return (this.y + this.len_y >= another.y && this.y <= another.y + another.len_y)
                && (this.x + this.len_x >= another.x && this.x <= another.x + another.len_x);
    }

    boolean isTouchingLeftX() {
        return this.x <= 0;
    }

    boolean isTouchingRightX() {
        return this.x + len_x >= this.screen_x;
    }

    boolean isTouchingUpperY() {
        return this.y <= 0;
    }

    boolean isTouchingLowerY() {
        return this.y + len_y >= screen_y;
    }

};
