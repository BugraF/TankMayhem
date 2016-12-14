package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;
import game.engine.World;
import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class Particle implements PhysicsObj, RenderObj {
    /** Context of this bomb */
    private final Game game;
    
    /** Position */
    private float x, y;
    private float lastX, lastY;
    
    /** Velocity */
    private float velX, velY;
    
    private final int color;
    
    /** Determines the width and height of this particle. */
    private final int size;
    
    public Particle(Game game, int color) {
        this(game, color, 1);
    }
    
    public Particle(Game game, int color, int size) {
        this.game = game;
        this.color = color;
        this.size = size;
    }
    
    /**
     * Sets the position and velocity of this particle.
     */
    public void init(float x, float y, float velX, float velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.fill(color);
        g.noStroke();
        g.rect(x, x, size, size);
    }
    
    @Override
    public void checkConstraints() {
        World world = game.getWorld();
        int[] collision = world.rayCast((int)lastX, (int)lastY, (int)x, (int)y);
        
        if (collision.length > 0) {
            Terrain terrain = game.getTerrain();
            
            // Stick to the terrain
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    terrain.addPixel(color, (int)x + i, (int)y + j);
            
            game.removeEntity(this);
        }

        // Reset last positions
        lastX = x;
        lastY = y;

        // Boundary constraints
        if (x < 0 || x > world.width() || y > world.height())
            game.removeEntity(this);
    }
    
    // PhysicsObj accessors & mutators
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVx() { return velX; }
    public float getVy() { return velY; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVx(float vX) { this.velX = vX; }
    public void setVy(float vY) { this.velY = vY; }
    
    // Particles disapper when they are stabilized.
    public boolean isXStable() { return false; }
    public boolean isYStable() { return false; }
    
}
