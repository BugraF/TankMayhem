package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author aytajabbaszade
 */
public abstract class PowerUp implements PhysicsObj, RenderObj {
    /** The context of this power-up */
    private final Game game;
    private final World world;
    
    /** Position of this power-up */
    private float x, y;
    
    /** Velocity of this power-up */
    private float velX, velY;
    
    /* Physical Properties */
    private float rotation;
    private boolean onGround = false; // Test
    
    /* Image & Bounds */
    private PImage box, parachute;
    private final int[] bounds = new int[4];
    
    /* Last position */
    private float lastX, lastY;
    
    /** Collision masks */
    private final int terrainMask, tankMask;
    
    public PowerUp(Game game){
        this.game = game;
        this.world = game.getWorld();
        terrainMask = world.generateCheckMask(game.getTerrain());
        tankMask = world.generateCheckMask(game.getPhysicsEngine().getTanks());
    }
 
    public abstract void activate(Tank tank);
    
    void init(int x, int y) {
        this.x = lastX = x;
        this.y = lastY = y;
        updateBounds();
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        if (inside(this.bounds, bounds)) {
            g.pushMatrix();
            g.translate(x, y);
            g.rotate(-rotation);
            if (parachute != null)
                g.image(parachute, -parachute.width / 2, 
                        -box.height / 2 - parachute.height);
            g.image(box, -box.width / 2, -box.height / 2);
            g.noTint();
            g.popMatrix();
        }
    }
    
    private static boolean inside(int[] b, int[] bb) {
        return true; // TODO Burak: Bound checking for PowerUp
    }
     
    @Override
    public void checkConstraints() {
        boolean update = lastX != x || lastY != y;
   
//        onGround = false;

//        int[] leftCollision = game.getWorld().rayCast(getBounds()[0], getBounds()[3], (int)getX(), getBounds()[3], terrainMask );
//        int[] rightCollision = game.getWorld().rayCast(getBounds()[2], getBounds()[3], (int)getX(), getBounds()[3], terrainMask );
//        
//        if(leftCollision.length>0 || rightCollision.length>0){
//            onGround=true;
//            setVy(0);
//        }
//        /*else if(leftCollision.length>0){
//            rotate(-1/12);//5 degree 
//        }
//        else if(rightCollision.length>0){
//            rotate(1/12);
//        }*/
//        else{
//            if (getVy() < 2)
//                setVy(getVy()+0.1f);
//        }
        
        //activate tank
//        Tank tanks[]=game.getPhysicsEngine().getTanks();
//        int[] TankMasks = new int[tanks.length];
//        for(int i=0; i<tanks.length; i++){
//            TankMasks[i] = game.getWorld().generateCheckMask(tanks[i]);
//            int[] tankInteraction1 = game.getWorld().rayCast( getBounds()[0], getBounds()[1], getBounds()[2], getBounds()[1], TankMasks[i] );//check top line
//            int[] tankInteraction2 = game.getWorld().rayCast( getBounds()[0], getBounds()[3], getBounds()[2], getBounds()[3], TankMasks[i] );//check bottom
//            int[] tankInteraction3 = game.getWorld().rayCast( getBounds()[0], getBounds()[1], getBounds()[0], getBounds()[3], TankMasks[i] );//check left
//            int[] tankInteraction4 = game.getWorld().rayCast( getBounds()[2], getBounds()[1], getBounds()[2], getBounds()[3], TankMasks[i] );//check right
//            if(tankInteraction1.length>0 || tankInteraction2.length>0 || tankInteraction3.length>0 || tankInteraction4.length>0){
//                activate(tanks[i]);
//                game.removeEntity(this);
//                break;
//            }
//        }

        if (update)
            updateBounds();
        lastX = x;
        lastY = y;
        
        // Boundary constraints
        if (x < 0 || x > world.width() || y > world.height())
            game.removeEntity(this);
    }
    
    private void updateBounds() {
        bounds[0] = (int)x - box.width / 2;
        bounds[1] = (int)y - box.height / 2;
        bounds[2] = bounds[0] + box.width; 
        bounds[3] = bounds[1] + box.height;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVx() { return velX; }
    public float getVy() { return velY; }
    
    public void setX(float x) { this.x = x; /*updateBounds();*/ }
    public void setY(float y) { this.y = y; /*updateBounds();*/ }
    public void setVx(float vX) { this.velX = vX; }
    public void setVy(float vY) { this.velY = vY; }
    
    public boolean isXStable() { return false; }
    public boolean isYStable() { return onGround; }
    
    void setImages(PImage box, PImage parachute) {
        this.box = box;
        this.parachute = parachute;
    }
    
}
