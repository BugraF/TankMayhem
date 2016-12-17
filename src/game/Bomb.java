package game;

import game.engine.PhysicsObj;
import game.engine.RenderObj;

/**
 *
 * @author Burak Gök
 */
public abstract class Bomb implements PhysicsObj, RenderObj {
    /** Context of this bomb */
    protected final Game game;
    
    /** Position */
    protected float x, y;
    protected float lastX, lastY;
    
    /** Velocity */
    protected float velX, velY;
    
    int blastPower;
    
    public Bomb(Game game) {
        this.game = game;
    }
    
    /**
     * Sets the position and velocity of this bomb.
     */
    public void init(float x, float y, float velX, float velY) {
        this.x = lastX = x;
        this.y = lastY = y;
        this.velX = velX;
        this.velY = velY;
    }
    
    /**
     * Destructs the terrain by generating particles for the pixels that are in
     * the blast region of this bomb.
     */
    public void destructTerrain(int xPos, int yPos) {
        float radius = blastPower * 1; // TODO Specify factor
        float radiusSq = radius * radius;
        Terrain terrain = game.getTerrain();
        int[] bounds = terrain.getBounds();
        int destRes = terrain.destructionResolution;
        
//        float[] normal = terrain.getNormal(xPos, yPos);
//        System.out.format("normal x: %s, y: %s", normal[0], normal[1]);
  
        // TODO Order?
        for (int x = xPos - (int)radius; x < xPos + (int)radius; x += destRes)
        if (bounds[0] < x && x < bounds[2]) // Boundary check
        for (int y = yPos - (int)radius; y < yPos + (int)radius; y += destRes)
        if (bounds[1] < y && y < bounds[3]) { // Boundary check
            // First determine if this pixel (or if any contained within its
            // square area) is solid.
            int solidX = 0, solidY = 0;
            boolean solid = false;
            for (int i = 0; i < destRes && !solid; i++)
                for (int j = 0; j < destRes && !solid; j++)
                    if (terrain.isPixelSolid(x + i, y + j)) {
                        solid = true;
                        solidX = x + i;
                        solidY = y + j;
                    }
                    
            if (solid) { // Now we need to find out if it is the blast radius.
                int xDiff = x - xPos;
                int yDiff = y - yPos;
                int distSq = xDiff * xDiff + yDiff * yDiff;
                
                if (distSq < radiusSq) {
                    // finally calculate the distance
                    float distance = (float)Math.sqrt(distSq);

                    // the speed will be based on how far the pixel is from the center
                    float speed = 400 * (1 - distance / radius);

                    if (distance == 0)
                        distance = 0.001f; // prevent divide by zero in next two statements
                    
                    // velocity
                    float velX = speed * (xDiff + ((float)Math.random() * 20) - 10) / distance;
                    float velY = speed * (yDiff + ((float)Math.random() * 20) - 10) / distance;
//                    System.out.format("vel x: %s y: %s\n", velX, velY);

                    // Create a particle
                    Particle particle = new Particle(
                            game, terrain.getColor(solidX, solidY), destRes);
                    particle.init(x, y, velX, velY);
                    game.addEntity(particle);

                    // Remove the corresponding pixels
                    for (int i = 0; i < destRes; i++)
                        for (int j = 0; j < destRes; j++)
                            terrain.removePixel(x + i, y + j);
                }
            }    
        }
        
        game.getWorld().updateMask(terrain, new int[] {
                    xPos - (int)radius, yPos - (int)radius,
                    xPos + (int)radius, yPos + (int)radius});
        
        damageTanks(xPos, yPos);
    }
    
    /**
     * Damages the tanks within the blast region according to their shield
     * bonuses and the blast power of this bomb.
     */
    public void damageTanks(int xPos, int yPos) {
        float totalDamage = 0;
        float radius = blastPower * 1; // TODO Specify factor
        float radiusSq = radius * radius;
        
        Tank[] tanks = game.getPhysicsEngine().getTanks();
        for (Tank tank : tanks) {
            float xDiff = tank.getX() - xPos;
            float yDiff = tank.getY() - yPos;
            float distSq = xDiff * xDiff + yDiff * yDiff;

            if (distSq < radiusSq) {
                float damage = blastPower * 1 / distSq / tank.getShieldBonus(); // TODO Specify factor
                tank.updateHp(damage);
                if (tank != game.getActiveTank())
                    totalDamage += damage;
            }
        }
        
        game.updatePlayerStatus(totalDamage);
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
    
    // Bombs are explosive objects, so cannot be stabilized.
    public boolean isXStable() { return false; }
    public boolean isYStable() { return false; }
}
