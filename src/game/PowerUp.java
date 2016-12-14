/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import processing.core.PGraphics;

/**
 *
 * @author aytajabbaszade
 */
public abstract class PowerUp 
        implements PhysicsObj, RenderObj{
    
    //parameters
    private float x;
    private float y;
    private float velX;
    private float velY;
    private final float PowerUpHeight=40;//TODO
    private final float PowerUpWidth=40;//TODO 
    private Game game;
    public float rotation;
    public boolean onGround;
    private int terrainMask;
    private int tanksMask;
    
    //constructor
    public PowerUp(Game game, int x, int y){
        this.game = game;
	this.x = x;
	this.y = y;
        terrainMask = game.getWorld().generateCheckMask(game.getTerrain());
        tanksMask = game.getWorld().generateCheckMask(game.getPhysicsEngine().getTanks());
        
    }
 
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

    public void rotate(float delta) {
        rotation += delta;
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
    public void checkConstraints(){
        rotation=0;
        if(getX()<0 || getX()>game.getWorld().width() || getY()<0 || getY()>game.getWorld().height()){
            game.removeEntity(this);
        }
   
        onGround=false;
            //referance points = leftBottom x-y, rightTop x,y
        int []ref = { (int)(getX()- PowerUpWidth/2), (int)(getY()+ PowerUpHeight/2),  (int)(getX()+ PowerUpWidth/2), (int)(getY()- PowerUpHeight/2) };
        int[] leftCollision = game.getWorld().rayCast(ref[0], ref[1], (int)getX(), ref[0], terrainMask );
        int[] rightCollision = game.getWorld().rayCast(ref[2], ref[1], (int)getX(), ref[0], terrainMask );
        
        if(leftCollision.length>0 && rightCollision.length>0){
            onGround=true;
            setVy(0);
        }
        else if(leftCollision.length>0){
            rotate(-1/12);//5 degree 
        }
        else if(rightCollision.length>0){
            rotate(1/12);
        }
        else{
            setVy(getVy()+1);
        }
        
        int[] tankInteraction1 = game.getWorld().rayCast( ref[0], ref[1], ref[2], ref[1], tanksMask );//check bottom line
        int[] tankInteraction2 = game.getWorld().rayCast( ref[0], ref[3], ref[2], ref[3], tanksMask );//check top
        int[] tankInteraction3 = game.getWorld().rayCast( ref[0], ref[1], ref[0], ref[3], tanksMask );//check left
        int[] tankInteraction4 = game.getWorld().rayCast( ref[2], ref[3], ref[2], ref[3], tanksMask );//check right
        if(tankInteraction1.length>0 || tankInteraction2.length>0 || tankInteraction3.length>0 || tankInteraction4.length>0){
            ;//TODO tank gets powerup
            //activate(tank);
        } 
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


     
}
