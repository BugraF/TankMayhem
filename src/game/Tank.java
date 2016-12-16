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
public class Tank implements PhysicsObj, WorldObj, RenderObj {
    /** The context of this tank */
    private final Game game;
    
    /** Position of this tank */
    private float x, y;
    
    /** Velocity of this tank */
    private float velX, velY;
    
    private boolean goLeft, goRight;
    
    float firePower, fireAngle;
    
    /* Tank Status */
    private float hp;
    private int fuel;
    
    /* Bonuses & Penalties */
    private float agility = 1;
    private float damageBonus = 1;
    private float shieldBonus = 1;
    
    /* Physical Properties */
    private final int color;
    private float rotation;
    private boolean onGround;
    
    /* Image & Bounds */
    private final PImage image; // without barrel
    private final int[] bounds = new int[4];
    
    private int terrainMask;
    private int tanksMask;
    private int tankHeight;
    private int tankWidth;
    
    public Tank(Game game, PImage image, int color) {
        this.game = game;
        this.image = image;
        this.color = color;
    }
    
    public void init(String mode, int x, int y) {
        this.x = x;
        this.y = y;
        
        if ("assault".equals(mode)) {
            damageBonus = 1.2f;
            shieldBonus = 0.8f;
        }
        else if ("armored".equals(mode)) {
            damageBonus = 0.8f;
            shieldBonus = 1.2f;
        }
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        if (inside(this.bounds, bounds)) {
            g.pushMatrix();
            g.translate(x, y);
            g.rotate(rotation);
            g.image(image, x, y);
            g.rotate(fireAngle);
            g.fill(color);
            g.rect(0, -2, 20, 4);
            g.popMatrix();
        }
    }
    
    private static boolean inside(int[] b, int[] bb) {
        return true; // TODO Burak: Bound checking for Tank
    }

    public void moveLeft() {
	goLeft = true;
    }
    public void moveRight() {
        goRight = true;
    }
    public void stopLeft() {
	goLeft = false;
    }
    public void stopRight() {
        goRight = false;    
    }
    
    /**
     * Updates the health points of this tank by the specified amount.
     */
    public void updateHp(float delta){
	hp += delta;
    }
    
    /**
     * Sets the maneuver capability of this tank. This method effectively
     * changes the speed of the tank.
     * @param agility Default value is 1. To slow down the tank, a value less
     *                than 1 should be specified. Similarly, to speed up the
     *                tank, a value greater than 1 is used.
     */
    public void setAgility(float agility){
        this.agility = agility;
    }
    
    /**
     * Returns the health points of this tank.
     */
    public float getHP() {
        return hp;
    }
    
    public int getFuel() {
        return fuel;
    }

    public float getFirePower() {
        return firePower;
    }

    public float getFireAngle() {
        return fireAngle;
    }
     
    @Override
    public void checkConstraints() { // world border, movement, hold surface
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
    public PImage getMask() {
        return image;
    }
    
    @Override
    public int[] getBounds() {
        return bounds;
    }

    private void rotate(float delta) {
        rotation += delta;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVx() { return velX; }
    public float getVy() { return velY; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVx(float vX) { this.velX = vX; }
    public void setVy(float vY) { this.velY = vY; }
    
    public boolean isXStable() { return true; }
    public boolean isYStable() { return onGround; }
    
}
