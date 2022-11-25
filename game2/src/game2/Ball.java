/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game2;

/**
 *
 * @author Administrator
 */
class Ball extends GameObject {

    protected boolean isFalling = false, isLeft = true;
    protected int toMove = 0;
    protected double baseAngle = 0;
    protected double maxAngle = 20;
    protected double minAngle = 70;
    public Ball() {
        this.isDead = false;
    }
    
    public Ball(int x, int y, int len_x, int len_y, int screen_x, int screen_y) {
        super(x, y, len_x, len_y, screen_x, screen_y);
        this.isDead = false;
    }

    public Ball(int x, int y, int len_x, int len_y, boolean isFalling, boolean isLeft, int angle, int screen_x, int screen_y) {
        this(x, y, len_x, len_y, screen_x, screen_y);
        this.isFalling = isFalling;		//initial direction
        this.isLeft = isLeft;
        this.baseAngle = angle;
    }

    void newDirection() {//make it more likely to go into brick
        if (this.isDead) {
            return;
        }
        this.baseAngle = Utils.randint(minAngle, maxAngle);
    }

    void down() {
        this.y += 1;
    }

    void up() {
        this.y -= 1;
    }

    void right() {
        this.x += 1;
    }

    void left() {
        this.x -= 1;
    }

    void updatePos(GameObject platForm) {
        if (this.isDead) {
            return;
        }
        if (this.isTouchingUpperY()) {		//use this instead of switching because it buggy
            isFalling = true;
            this.updateLasthit();
        } else if (this.isTouchingLowerY()) {
            isFalling = false;
            isDead = true;
            this.updateLasthit();
        }
        if (this.isTouchingLeftX()) {		//use this instead of switching because it buggy
            isLeft = false;
            this.updateLasthit();
        } else if (this.isTouchingRightX()) {
            isLeft = true;
            this.updateLasthit();
        }
        if (this.isTouching(platForm)) {
            isFalling = false;
            this.updateLasthit();
            this.newDirection();
        }

        double angle;
        if ((isLeft && !isFalling) || (!isLeft && isFalling)) {
            angle = this.baseAngle;
        } else {
            angle = -this.baseAngle;
        }
        if (this.toMove == 0) {
            if (isFalling && !this.isTouchingLowerY()) {
                this.down();
            } else if (!this.isTouchingUpperY()) {
                this.up();
            }
            int dy = y - lastHitY;
            this.toMove = lastHitX + (int) Math.round((double) dy * Math.tan(angle * 3.14 / 180.0)) - this.x;
        }

        if (this.toMove < 0 && this.x > 0) {
            this.left();
            this.toMove += 1;
        } else if (this.toMove > 0 && this.x < this.screen_x - 1) {
            this.right();
            this.toMove -= 1;
        } else {
            this.toMove = 0;
        }
    }

    boolean collide(GameObject br) {
        CollisionResult cr = this.getCollision(br);
        if (cr.isCollide) {
            if (cr.diffY != 0) {
                this.isFalling = cr.diffY < 0;
            }
            if (cr.diffX != 0) {
                this.isLeft = cr.diffX > 0;
            }
            this.toMove = 0;
        }
        return cr.isCollide;
    }

    Ball[] blowUp() {
        Ball[] added = new Ball[4];
        int l = Math.max(this.x - 1, 0);
        int r = Math.min(this.x + this.len_x, screen_x - 1);
        int u = Math.max(this.y - 1, 0);
        int d = Math.min(this.y + this.len_y, screen_y - 1);
        int newSx = Math.max(len_x-5,5);
        int newSy = Math.max(len_y-5,5);

        added[0] = new Ball(l, u, newSx,newSy,false,true,45, screen_x, screen_y);
        added[1] = new Ball(l, d,newSx,newSy,true,true,45, screen_x, screen_y);
        added[2] = new Ball(r, u, newSx,newSy,false,false,45, screen_x, screen_y);
        added[3] = new Ball(r, d, newSx,newSy,true,false,45, screen_x, screen_y);

        return added;
    }

    boolean isDestroyed() {
        return this.isDead;
    }
};
