/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author aytajabbaszade
 */
public abstract class PowerUp 
        implements PhysicsObj, RenderObj{
    //constructor
    public PowerUp(int x, int y){
	this.x = x;
	this.y = y;
    }
    //parameters
    private float x;
    private float y;
    private float velX;
    private float velY;
    public float rotation;
    public boolean onGround;
    
    //methods
    public void activate(Tank tank){
       
        
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
        if (onGround)
            return true;
        else
            return false;
    }
    
    @Override
    public boolean isYStable(){
        if (onGround)
            return true;
        else
            return false;
    }
     
    @Override
    public void checkConstraints(){}
     
}
