package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import game.engine.WorldObj;
import processing.awt.PGraphicsJava2D;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

/**
 *
 * @author aytajabbaszade
 */
public class Tank implements PhysicsObj, WorldObj, RenderObj {
    /** The context of this tank */
    private final Game game;
    private final World world;
    
    /** Position of this tank */
    private float x, y;
    
    /** Velocity of this tank */
    private float velX, velY;
    
    private boolean goLeft, goRight;
    
    float firePower, fireAngle;
    
    /* Tank Status */
    private float hp = 100;
    private float fuel = 100;
    
    /* Bonuses & Penalties */
    private float agility = 1;
    private float damageBonus = 1;
    private float shieldBonus = 1;
    
    /* Physical Properties */
    private final int color;
    private float rotation;
    private boolean onGround = true; // Test
    
    /* Image & Bounds */
    private final PImage image; // without barrel
    private final PGraphics mask; // without barrel
    private final PShape hitbox;
    private final float[] hitboxVertices;
    private final int barrel; // barrel pivot translation
    private final int[] bounds = new int[4];
    
    /* Last position */
    private float lastX, lastY;
    
    private final int terrainMask;
    
    public Tank(Game game, PImage image, int barrel, int color, int[] hitbox) {
        this.game = game;
        this.image = image;
        this.barrel = barrel;
        this.color = color;
        
        this.world = game.getWorld();
        terrainMask = world.generateCheckMask(game.getTerrain());
        
        mask = new PGraphicsJava2D();
        float w = image.width / 2f, h = image.height / 2f;
        double diameter = 2 * Math.sqrt(w * w + h * h);
        mask.setSize((int)diameter, (int)diameter);
        
        hitboxVertices = new float[hitbox.length];
        this.hitbox = mask.createShape();
        this.hitbox.beginShape();
        this.hitbox.noStroke();
        this.hitbox.fill(0);
        for (int i = 0; i < hitbox.length; i += 2) {
            hitboxVertices[i] = hitbox[i] - w;
            hitboxVertices[i + 1] = hitbox[i + 1] - h;
            this.hitbox.vertex(hitboxVertices[i], hitboxVertices[i + 1]);
        }
        this.hitbox.endShape(PShape.CLOSE);
    }
    
    void init(int x, int y, float damageBonus, float shieldBonus) {
        this.x = lastX = x;
        this.y = lastY = y;
        rotation = (float)Math.PI / 6;
        updateBounds();
        this.damageBonus = damageBonus;
        this.shieldBonus = shieldBonus;
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        if (inside(this.bounds, bounds)) {
            g.pushMatrix();
            g.translate(x, y);
            g.rotate(-rotation);
            
            g.pushMatrix();
            g.translate(0, barrel); // Barrel pivot position
            g.rotate(-fireAngle);
            g.fill(color);
            g.rect(0, -3, 40, 6);
            g.popMatrix();
            
            g.tint(color);
            g.image(image, -image.width / 2, -image.height / 2);
            g.noTint();
            g.popMatrix();
        }
    }
    
    private static boolean inside(int[] b, int[] bb) {
        return true; // TODO Burak: Bound checking for Tank
    }

    void moveLeft() {
        goLeft = true;
    }
    void moveRight() {
        goRight = true;
    }
    void stopLeft() {
	goLeft = false;
    }
    void stopRight() {
        goRight = false;
    }
    
    /**
     * Updates the health points of this tank by the specified amount.
     */
    void updateHp(float delta) {
        float newHP = hp + delta;
        hp = newHP < 0 ? 0 : (newHP > 100 ? 100 : newHP);
        if (hp == 0)
            game.getWorld().remove(this);
	else
            hp = newHP > 100 ? 100 : newHP;
    }
    
    /**
     * Updates the fuel of this tank by the specified amount.
     */
    void updateFuel(float delta) {
        float newFuel = fuel + delta;
        fuel = newFuel < 0 ? 0 : (newFuel > 100 ? 100 : newFuel);
    }
    
    /**
     * Sets the maneuver capability of this tank. This method effectively
     * changes the speed of the tank.
     * @param agility Default value is 1. To slow down the tank, a value less
     *                than 1 should be specified. Similarly, to speed up the
     *                tank, a value greater than 1 is used.
     */
    void setAgility(float agility) {
        this.agility = agility;
    }
    
    /**
     * Returns the health points of this tank.
     */
    public float getHP() {
        return hp;
    }
    
    public float getFuel() {
        return fuel;
    }
    
    public float getDamageBonus() {
        return damageBonus;
    }
    public float getShieldBonus() {
        return shieldBonus;
    }
     
    @Override
    public void checkConstraints() { // world border, movement, hold surface
        boolean update = lastX != x || lastY != y;
        
//        if(getX()<0){
//            setX(0);
//        }
//        if(getX()>game.getWorld().width()){
//            setX(game.getWorld().width());
//        }
//        if(getY()>game.getWorld().height()){
//            setY(game.getWorld().height());
//        }
        
        // Movement
        if (goLeft ^ goRight && fuel > 0) { // Either of them is true, but not both
            velX = goLeft ? -30 : 30;
            fuel = Math.max(fuel - 0.05f, 0);
        }
        else // Both are false or true
            velX = 0;

//        onGround=false;
//
//        int []ref = { (int)(getX()- tankWidth/2), (int)(getY()+ tankHeight/2),  (int)(getX()+ tankWidth/2), (int)(getY()- tankHeight/2) };
//        int[] leftCollision = game.getWorld().rayCast(ref[0], ref[1], (int)getX(), ref[0], terrainMask );
//        int[] rightCollision = game.getWorld().rayCast(ref[2], ref[1], (int)getX(), ref[0], terrainMask );
//        
//        if(leftCollision.length>0 && rightCollision.length>0){
//            onGround=true;
//            setVy(0);
//        }
//        else if(leftCollision.length>0){
//            rotate(-0.1f);//5 degree 
//        }
//        else if(rightCollision.length>0){
//            rotate(0.1f);
//        }
//        else{
//            if (getVx() < 500)
//                setVy(getVy()+40); 
//        }

        if (update) {
            rotation += 0.01f;
            System.out.format("pos: %s, %s\n", x, y);
            updateBounds();
            if (hp != 0)
                world.updateMask(this, null);
        }
        lastX = x;
        lastY = y;
    }
    
    @Override
    public PImage getMask() {
        return mask;
    }
    
    @Override
    public int[] getBounds() {
        return bounds;
    }
    
    private void updateBounds() {
        mask.beginDraw();
//        mask.shapeMode(PGraphics.CENTER);
        mask.stroke(0);
        mask.strokeWeight(2);
        mask.noFill();
        mask.rect(0, 0, mask.width - 2, mask.height - 2);
        mask.translate(image.width / 2f, image.height / 2f);
        mask.rotate(-rotation);
        mask.shape(hitbox);
        mask.endDraw();
        
        bounds[0] = bounds[2] = (int)x;
        bounds[1] = bounds[3] = (int)y;
        
        double sin = Math.sin(rotation);
        double cos = Math.cos(rotation);
        for (int i = 0; i < hitboxVertices.length; i += 2) {
            double x = hitboxVertices[i];
            double y = hitboxVertices[i + 1];
            double newX =  this.x + x * cos - y * sin;
            y = this.y + x * sin + y * cos;
            x = newX;
//            System.out.format("vertex [%s, %s] -> [%s, %s]\n", 
//                    hitboxVertices[i], hitboxVertices[i + 1], x, y);
            
            if (x < bounds[0]) bounds[0] = (int)x;
            else if (x > bounds[2]) bounds[2] = (int)x;
            if (y < bounds[1]) bounds[1] = (int)y;
            else if (y > bounds[3]) bounds[3] = (int)y;
        }
//        System.out.format("bounds: %s, %s, %s, %s\n", bounds[0], bounds[1], bounds[2], bounds[3]);
    }
    
//    private void updateBounds() {
//        bounds[0] = (int)x - image.width / 2;
//        bounds[1] = (int)y - image.height / 2;
//        bounds[2] = bounds[0] + image.width; // odd width
//        bounds[3] = bounds[1] + image.height; // odd height
////        bounds[2] = (int)x + image.width / 2;
////        bounds[3] = (int)y + image.height / 2;
//    }

    private void rotate(float delta) {
        rotation += delta;
    }
    
    float getRotation() {
        return rotation;
    }
    
    /** Vertical position of the barrel pivot */
    float getBarrelPosition() {
        return y + barrel;
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
    
}
