package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.Renderer;
import game.engine.World;
import java.util.HashMap;
import java.util.Map;
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
    private float rotation; // in clockwise direction
    private boolean onGround = false;
    
    /* Image & Bounds */
    private PImage box, parachute;
    private final float[] vertices = new float[8],
                          rotated = new float[8];
    private final int[] bounds = new int[4];
    
    /* Last position */
    private float lastX, lastY;
    
    /** Collision masks */
    private final int terrainMask, tankMask;
    private final static Map<Integer, Tank> tanks = new HashMap<>(4);
    
    public PowerUp(Game game){
        this.game = game;
        this.world = game.getWorld();
        terrainMask = world.generateCheckMask(game.getTerrain());
        Tank[] tanks = game.getPhysicsEngine().getTanks();
        if (PowerUp.tanks.isEmpty())
            for (int i = 0; i < tanks.length; i++)
                PowerUp.tanks.put(world.generateCheckMask(tanks[i]), tanks[i]);
        tankMask = world.generateCheckMask(tanks);
    }

    public abstract void activate(Tank tank);
    
    protected void showBadge(PImage badge) {
        Renderer renderer = game.getStage().getRenderer();
        renderer.add(new Badge(renderer, badge, x, y));
    }
    
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
            g.rotate(rotation);
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
        
        onGround = false;
        
        int x1 = (int)(x + rotated[6]);
        int y1 = (int)(y + rotated[7]) + 1;
        int x2 = (int)(x + rotated[4]);
        int y2 = (int)(y + rotated[5]) + 1;
        
        int[] leftCollision = world.rayCast(x1, y1, x2, y2, terrainMask);
        
        if (leftCollision.length > 0) {
            onGround = true;
            parachute = null;
            velY = 0;
            int[] rightCollision = world.rayCast(x2, y2, x1, y1, terrainMask);
            float bias = rightCollision.length == 0 ? x2 - x1
                    : (leftCollision[2] - x1) - (x2 - rightCollision[2]);
            if (Math.abs(bias) > 3) {
                float rotate = bias > 0 ? -0.01f : 0.01f;
                rotation += rotate;
                update = true;
            }
            else if (world.rayCast(x1, y1 - 2, x2, y2 - 2, terrainMask).length != 0) {
                y -= 1f;
            }
        }
        
        for (int i = 0; i < rotated.length; i += 2) {
            int j = (i + 2) % rotated.length;
            int[] overlap = world.rayCast(
                    (int)(x + rotated[i]), (int)(y + rotated[i + 1]), 
                    (int)(x + rotated[j]), (int)(y + rotated[j + 1]), tankMask);
            if (overlap.length != 0) {
                System.out.println("overlap");
                Tank tank = tanks.get(overlap[4]); // Risky, does not consider combinations
                this.activate(tank);
                game.removeEntity(this);
                return;
            }
        }

        if (update)
            updateBounds();
        lastX = x;
        lastY = y;
        
        // Boundary constraints
        if (x < 0 || x > world.width() || y > world.height())
            game.removeEntity(this);
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
            
            if (x < bounds[0]) bounds[0] = (int)x;
            else if (x > bounds[2]) bounds[2] = (int)x;
            if (y < bounds[1]) bounds[1] = (int)y;
            else if (y > bounds[3]) bounds[3] = (int)y;
        }
        
        bounds[0] += (int)x;
        bounds[1] += (int)y;
        bounds[2] += (int)x;
        bounds[3] += (int)y;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVx() { return velX; }
    public float getVy() { return velY; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVx(float vX) { this.velX = vX; }
    public void setVy(float vY) { this.velY = vY; }
    
    public boolean isXStable() { return parachute == null; }
    public boolean isYStable() { return onGround; }
    
    void setImages(PImage box, PImage parachute) {
        this.box = box;
        this.parachute = parachute;
        float w = box.width / 2f, h = box.height / 2f;
        vertices[0] = -w; vertices[1] = -h;
        vertices[2] =  w; vertices[3] = -h;
        vertices[4] =  w; vertices[5] =  h;
        vertices[6] = -w; vertices[7] =  h;
    }
    
}
