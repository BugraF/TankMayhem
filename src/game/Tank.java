/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.WorldObj;
import processing.core.PGraphics;
import processing.core.PImage;


/**
 *
 * @author aytajabbaszade
 */
public abstract class Tank 
        implements PhysicsObj, WorldObj, RenderObj{
    
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
    private Game game;
    private int terrainMask;
    private int tanksMask;
    private int tankHeight;
    private int tankWidth;
    public Tank(Game game, int color){
        this.game=game;
        velX = 0;
        velY = 0;		
    }

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
    public float getHP() {
        return hp;
    }

    public float getFirePower() {
        return firePower;
    }

    public float getFireAngle() {
        return fireAngle;
    }

    public int getFuel() {
        return fuel;
    }

    public boolean isOnGround() {
        return onGround;
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
    public void checkConstraints(){// world border, movement, hold surface
        if(getX()<0){
            setX(0);
        }
        if(getX()>game.getWorld().width()){
            setX(game.getWorld().width());
        }
        if(getY()>game.getWorld().height()){
            setY(game.getWorld().height());
        }
        // movement
        if (goLeft) {
          if (getVx() > -500)
            setVx(getVx()-40); 
        }
        else if (velX < 0)
            setVx( getVx()*0.8f );     // slow down side-ways velocity if we're not moving left

        if (goRight) {
          if (getVx() < 500)
            setVx(getVx()+40);
        }
        else if (velX > 0)
          setVx( getVx()*0.8f );

        onGround=false;

        int []ref = { (int)(getX()- tankWidth/2), (int)(getY()+ tankHeight/2),  (int)(getX()+ tankWidth/2), (int)(getY()- tankHeight/2) };
        int[] leftCollision = game.getWorld().rayCast(ref[0], ref[1], (int)getX(), ref[0], terrainMask );
        int[] rightCollision = game.getWorld().rayCast(ref[2], ref[1], (int)getX(), ref[0], terrainMask );
        
        if(leftCollision.length>0 && rightCollision.length>0){
            onGround=true;
            setVy(0);
        }
        else if(leftCollision.length>0){
            rotate(-0.1f);//5 degree 
        }
        else if(rightCollision.length>0){
            rotate(0.1f);
        }
        else{
            if (getVx() < 500)
                setVy(getVy()+40); 
        }
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*@Override
    public PImage getMask(){
        PImage tank = loadImage("tank.jpg");
        return tank;
    }
    @Override
    public int[] getBounds(){
        return
    }*/

    private void rotate(float delta) {
        rotation+=delta;
    }


   
    
    
}
