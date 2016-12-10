/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import processing.core.PImage;


/**
 *
 * @author aytajabbaszade
 */
public abstract class Tank 
        implements PhysicsObj, WorldObj, RenderObj{
    public Tank(int color){
      velX = 0;
      velY = 0;		
    }
    
    private float x;
    private float y;
    private float velX;
    private float velY;
    private boolean goLeft;
    private boolean goRight;
    private float firePower;
    private float fireAngle;
    private int hp;
    private int fuel;
    private float agility;
    private float shieldBonus;
    private float damageBonus;
    public float rotation;
    public boolean onGround;
    
    
    //methods
    public void moveLeft(){
	goLeft = true;
    }
    public void moveRight(){
        goRight = true;
    }
    public void stopLeft(){
	goLeft = false;
    }
    public void stopRight(){
        goRight = false;    
    }
    public void updateHp(int delta){
	hp = hp + delta;
    }
    public void setAgility(float agility){
        this.agility = agility;
    }
    @Override
    public float getX(){
        return x;
    }
     @Override
    public float getY(){
        return y;
    }
     @Override
     public float getVx(){
        return velX;
    }
     @Override
    public float getVy(){
        return velY;
    }
     @Override
    public void setX(float x){
        this.x = x;
    }
     @Override
    public void setY(float y){
        this.y = y;
    }
     @Override
    public void setVx(float vX){
        this.velX = vX;
    }
     @Override
    public void setVy(float vY){
        this.velY = vY;
    }
     @Override
    public boolean isXStable(){
        return true;
    }
    
    @Override
    public boolean isYStable(){
        if (onGround == true){
            return true;
        }
        else
            return false;
    }
     
    @Override
    public void checkConstraints(){}
    
    @Override
    public PImage getMask(){
        PImage tank = loadImage("tank.jpg");
        return tank;
    }
    @Override
    public int[] getBounds(){
        return
    }
   
    
    
}
