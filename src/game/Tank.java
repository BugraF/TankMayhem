package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import game.engine.WorldObj;
import processing.awt.PGraphicsJava2D;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

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
    private Player player;
    private float hp = 100;
    private float fuel = 100;
    
    /* Bonuses & Penalties */
    private float agility = 1;
    private float damageBonus = 1;
    private float shieldBonus = 1;
    
    /* Physical Properties */
    private final int color;
    private float rotation; // in clockwise direction
    private boolean onGround = true; // Test
    
    /* Image & Bounds */
    private final PImage image; // without barrel
    private final PGraphics mask; // without barrel
    private final PShape hitbox;
    private final float[] vertices, rotated;
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
        
        vertices = new float[hitbox.length];
        rotated = new float[hitbox.length];
        this.hitbox = mask.createShape();
        this.hitbox.beginShape();
        this.hitbox.noStroke();
        this.hitbox.fill(0);
        for (int i = 0; i < hitbox.length; i += 2) {
            vertices[i] = hitbox[i] - w;
            vertices[i + 1] = hitbox[i + 1] - h;
            this.hitbox.vertex(vertices[i], vertices[i + 1]);
//            this.hitbox.vertex(hitbox[i], hitbox[i + 1]);
        }
        this.hitbox.endShape(PShape.CLOSE);
    }
    
    void init(int x, int y, float damageBonus, float shieldBonus) {
        this.x = lastX = x;
        this.y = lastY = y;
//        rotation = (float)-Math.PI / 6;
        updateBounds();
        this.damageBonus = damageBonus;
        this.shieldBonus = shieldBonus;
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        if (inside(this.bounds, bounds)) {
            g.pushMatrix();
            g.translate(x, y);
            g.rotate(rotation);
            
            g.pushMatrix();
            g.translate(0, barrel); // Barrel pivot position
            g.rotate(fireAngle);
            g.fill(color);
            g.rect(0, -3, 40, 6);
            g.popMatrix();
            
            g.tint(color);
            g.image(image, -image.width / 2, -image.height / 2);
            g.noTint();
            g.rotate(-rotation);
//            g.image(mask, this.bounds[0] - x, this.bounds[1] - y);
            g.stroke(0, 0, 255);
//            g.line(vertices[10], vertices[11] + 1, 
//                    vertices[8], vertices[9] + 1);
            g.line(rotated[10], rotated[11] + 1, rotated[8], rotated[9] + 1);
            g.fill(0, 0, 255);
            g.ellipse(lcx - x - 1, lcy - y - 1, 2, 2);
            g.ellipse(rcx - x - 1, rcy - y - 1, 2, 2);
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
     * Returns the player associated to this tank.
     */
    public Player getPlayer() {
        return player;
    }
    void setPlayer(Player player) {
        this.player = player;
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
    
    private int lcx, lcy, rcx, rcy;
     
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

        onGround = false;
        
        int x1 = (int)(x + rotated[10]);
        int y1 = (int)(y + rotated[11]) + 1;
        int x2 = (int)(x + rotated[8]);
        int y2 = (int)(y + rotated[9]) + 1;
        
        int[] leftCollision = world.rayCast(x1, y1, x2, y2, terrainMask);
        
        if (leftCollision.length > 0) {
            onGround = true;
            velY = 0;
            int[] rightCollision = world.rayCast(x2, y2, x1, y1, terrainMask);
            float bias = rightCollision.length == 0 ? x2 - x1
                    : (leftCollision[2] - x1) - (x2 - rightCollision[2]);
            if (Math.abs(bias) > 3) {
                float rotate = bias > 0 ? -0.01f : 0.01f;
                lcx = leftCollision[2]; lcy = leftCollision[3];
                if (rightCollision.length > 0) {
                    rcx = rightCollision[2]; rcy = rightCollision[3];
                } else { rcx = rcy = 0; }
                rotation += rotate;
                update = true;
            }
            else if (world.rayCast(x1, y1 - 2, x2, y2 - 2, terrainMask).length != 0) {
                y -= 1f;
            }
        }

        if (update) {
//            rotation = (rotation + 0.01f) % (float)Math.PI;
//            System.out.format("pos: %s, %s\n", x, y);
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
        bounds[0] = bounds[2] = 0;
        bounds[1] = bounds[3] = 0;
        
        float sin = (float)Math.sin(rotation);
        float cos = (float)Math.cos(rotation);
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
            float newX = x * cos - y * sin;
            rotated[i + 1] = y = x * sin + y * cos;
            rotated[i] = x = newX;
//            System.out.format("vertex [%s, %s] -> [%s, %s]\n", 
//                    hitboxVertices[i], hitboxVertices[i + 1], x, y);
            
            if (x < bounds[0]) bounds[0] = (int)x;
            else if (x > bounds[2]) bounds[2] = (int)x;
            if (y < bounds[1]) bounds[1] = (int)y;
            else if (y > bounds[3]) bounds[3] = (int)y;
        }
//        System.out.format("bounds: %s, %s, %s, %s\n", 
//                bounds[0], bounds[1], bounds[2], bounds[3]);

        mask.beginDraw();
        mask.clear();
        // Bounding Box
//        mask.stroke(0);
//        mask.strokeWeight(2);
//        mask.noFill();
//        mask.rect(0, 0, bounds[2] - bounds[0], bounds[3] - bounds[1]);
        mask.translate(-bounds[0], -bounds[1]);
        mask.rotate(rotation);
        mask.shape(hitbox);
        mask.endDraw();
        
        bounds[0] += (int)x;
        bounds[1] += (int)y;
        bounds[2] += (int)x;
        bounds[3] += (int)y;
    }
    
    private void updateBounds1() {
        bounds[0] = (int)x - image.width / 2;
        bounds[1] = (int)y - image.height / 2;
        bounds[2] = bounds[0] + image.width; // odd width
        bounds[3] = bounds[1] + image.height; // odd height
//        bounds[2] = (int)x + image.width / 2;
//        bounds[3] = (int)y + image.height / 2;
    }
    
    float getRotation() {
        return rotation;
    }
    
    float getBarrelX() {
        return x + barrel * (float)Math.cos(rotation + Math.PI / 2);
    }
    float getBarrelY() {
        return y + barrel * (float)Math.sin(rotation + Math.PI / 2);
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
