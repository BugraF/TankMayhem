/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import game.engine.WorldObj;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author aytajabbaszade
 */
public abstract class PowerUp 
        implements PhysicsObj, WorldObj, RenderObj{
    
    //parameters
    private Game game;
    private final World world;
    
    private float x;
    private float y;
    private float lastX;
    private float lastY;
    private float velX;
    private float velY;
    /*private final float PowerUpHeight=40;//TODO
    private final float PowerUpWidth=40;//TODO */

    private final int[] bounds = new int[4];
    private final PImage image;
    
    public float rotation;
    public boolean onGround;
    private final int terrainMask;
    private final int tanksMask;
    Player[] players;
    Tank[] tanks;
    //int[] TankMasks;
    
    
    //constructor

    /**
     *
     * @param game
     * @param image
     * @param x
     * @param y
     */
    public PowerUp(Game game, PImage image, int x, int y){
        this.game = game;
        this.image = image;//PImage image,
        this.world = game.getWorld();
        this.x = lastX = x;
        this.y = lastY = y;

        updateBounds();
        terrainMask = game.getWorld().generateCheckMask(game.getTerrain());
        tanksMask = game.getWorld().generateCheckMask(game.getPhysicsEngine().getTanks());
    }
 
    //methods
    public void activate(Tank tank){
        //TODO HALUK
        System.out.println(tank.getPlayer().getName()+" got a powerUp. ");
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
    public PImage getMask() {
        return image;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public int[] getBounds() {
        return bounds;
    }
    private void updateBounds() {
        bounds[0] = (int)x - image.width / 2;
        bounds[1] = (int)y - image.height / 2;
        bounds[2] = bounds[0] + image.width; 
        bounds[3] = bounds[1] + image.height;
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
        boolean update = lastX != x || lastY != y;
        rotation=0;
        if(getX()<0 || getX()>game.getWorld().width() || getY()<0 || getY()>game.getWorld().height()){
            game.removeEntity(this);
        }
   
        onGround=false;

        int[] leftCollision = game.getWorld().rayCast(getBounds()[0], getBounds()[3], (int)getX(), getBounds()[3], terrainMask );
        int[] rightCollision = game.getWorld().rayCast(getBounds()[2], getBounds()[3], (int)getX(), getBounds()[3], terrainMask );
        
        if(leftCollision.length>0 || rightCollision.length>0){
            onGround=true;
            setVy(0);
        }
        /*else if(leftCollision.length>0){
            rotate(-1/12);//5 degree 
        }
        else if(rightCollision.length>0){
            rotate(1/12);
        }*/
        else{
            if (getVy() < 2)
                setVy(getVy()+0.1f);
        }
        

        if (update) 
        {
            updateBounds();
            world.updateMask(this, null);
        }
        lastX = x;
        lastY = y;
        
        //avtivate tank
        Tank tanks[]=game.getPhysicsEngine().getTanks();
        int[] TankMasks = new int[tanks.length];
        for(int i=0; i<tanks.length; i++){
            TankMasks[i] = game.getWorld().generateCheckMask(tanks[i]);
            int[] tankInteraction1 = game.getWorld().rayCast( getBounds()[0], getBounds()[1], getBounds()[2], getBounds()[1], TankMasks[i] );//check top line
            int[] tankInteraction2 = game.getWorld().rayCast( getBounds()[0], getBounds()[3], getBounds()[2], getBounds()[3], TankMasks[i] );//check bottom
            int[] tankInteraction3 = game.getWorld().rayCast( getBounds()[0], getBounds()[1], getBounds()[0], getBounds()[3], TankMasks[i] );//check left
            int[] tankInteraction4 = game.getWorld().rayCast( getBounds()[2], getBounds()[1], getBounds()[2], getBounds()[3], TankMasks[i] );//check right
            if(tankInteraction1.length>0 || tankInteraction2.length>0 || tankInteraction3.length>0 || tankInteraction4.length>0){
                activate(tanks[i]);
                game.removeEntity(this);
                break;
            }
        }
    }
    @Override
    public void draw(PGraphics g, int[] bounds) {
        if (inside(this.bounds, bounds)) 
        {
            g.pushMatrix();
            g.translate(x, y);
            g.rotate(-rotation);
            
            g.pushMatrix();
            //g.translate(0, barrel); // Barrel pivot position
            //g.rotate(-fireAngle);
            //g.fill(color);
            g.rect(0, -3, 40, 6);
            g.popMatrix();
            
            //g.tint(color);
            g.image(image, -image.width / 2, -image.height / 2);
            g.noTint();
            g.popMatrix();
        }
    }
    private static boolean inside(int[] b, int[] bb) {
        return true; // TODO Burak: Bound checking for PowerUp
    }


     
}
