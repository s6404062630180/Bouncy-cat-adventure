/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game2;

/**
 *
 * @author Administrator
 */
public class CollisionResult {
    public int diffX,diffY;
    public boolean isCollide;
    public CollisionResult(int diffX,int diffY,boolean isCollide){
        this.diffX = diffX;
        this.diffY = diffY;
        this.isCollide = isCollide;
    }
}
