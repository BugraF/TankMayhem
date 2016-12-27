package game;

import game.engine.Physics;
import game.engine.PhysicsObj;
import game.engine.World;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Burak Gök
 */
public class PhysicsEngine {
    /** Context of this physics engine */
    private final Game game;
    
    /**
     * Underlying physics engine
     */
    private final Physics physics = new Physics(16);
    
    private final List<Bomb> bombs = new ArrayList<>(5);
    private final List<Particle> particles = new ArrayList<>(100);
    private final List<PowerUp> powerups = new ArrayList<>(5);
    private final List<Tank> tanks = new ArrayList<>(2);
    
    /**
     * Indicates that the turn will be switched once the bomb and particle
     * containers gets empty.
     */
    boolean switchTurnWhenStabilized = false;
    
    private final Thread powerupGenerator = new Thread(
            new PowerUpGenerator(), "PowerUpGenerator");
    
    public PhysicsEngine(Game game) {
        this.game = game;
        physics.addObjectClasses(bombs, particles, powerups, tanks);
        physics.setAcceleration(0, 980, 0, 1, 3);
        physics.setAcceleration(0, 50, 2);
        powerupGenerator.start();
    }
    
    private int wind = 0;
    
    /**
     * Sets the wind (this applies a common acceleration to all physics objects).
     * @param wind Negative values are associated with the left direction.
     */
    void setWind(int wind) {
        this.wind = wind;
        physics.setAcceleration(wind * 5, 980, 0, 1);
        physics.setAcceleration(wind * 2, 50, 2);
    }
    
    int getWind() {
        return wind;
    }
    
    /**
     * (Copied from Physics)
     * Updates the positions and velocities of all physics objects.
     * At most one-time step is iterated in the end of this method call to
     * prevent the game from iterating too much time steps when the game becomes
     * unresponsive. Collisions are checked by calling checkConstraints() method
     * of each physics object.
     */
    void update() {
        physics.update();
        if (!bombs.isEmpty()) {
            World world = game.getWorld();
            float[] bounds = new float[] {world.width(), world.height(), 0, 0};
            for (Bomb bomb : bombs) {
                bounds[0] = Math.min(bounds[0], bomb.getX());
                bounds[1] = Math.min(bounds[1], bomb.getY());
                bounds[2] = Math.max(bounds[2], bomb.getX());
                bounds[3] = Math.max(bounds[3], bomb.getY());
            }
            float camX = (bounds[0] + bounds[2]) / 2;
            float camY = (bounds[1] + bounds[3]) / 2;
            game.getStage().shiftCamera((int)camX, (int)camY);
        }
//        if (switchTurnWhenStabilized)
//            System.out.format("Bombs: %s Particles: %s\n", bombs.size(), particles.size());
        if (switchTurnWhenStabilized && bombs.isEmpty() && particles.isEmpty()) {
            game.switchTurn();
            switchTurnWhenStabilized = false;
        }
    }
    
    /**
     * Adds the specified physics object to the underlying physics engine.
     */
    void add(PhysicsObj obj) {
        if (obj instanceof Bomb)
            bombs.add((Bomb) obj);
        else if (obj instanceof Particle)
            particles.add((Particle) obj);
        else if (obj instanceof PowerUp)
            powerups.add((PowerUp) obj);
        else if (obj instanceof Tank)
            tanks.add((Tank) obj);
    }
    
    /**
     * Removes the specified physics object from the underlying physics engine.
     */
    void remove(PhysicsObj obj) {
        if (obj instanceof Bomb)
            bombs.remove((Bomb) obj);
        else if (obj instanceof Particle)
            particles.remove((Particle) obj);
        else if (obj instanceof PowerUp)
            powerups.remove((PowerUp) obj);
        else if (obj instanceof Tank)
            tanks.remove((Tank) obj);
    }
    
    /**
     * Returns the tanks that are not destroyed.
     * Destroyed tanks are still processed by this engine, but they are not
     * exposed to outside to avoid confusions.
     */
    Tank[] getTanks() {
        return tanks.stream().filter((t) -> t.getHP() != 0)
                .toArray((count) -> new Tank[count]);
    }
    
    private class PowerUpGenerator implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (powerups) {
                    do {
                        try {
                            powerups.wait(15000); // 15 seconds
                        }
                        catch (InterruptedException e) {}
                    } while (powerups.size() >= 5);
                }
                PowerUp powerup = game.getCatalog().getRandomPowerUp();
                Tank[] tanks = getTanks();
                Tank tank = tanks[(int)(Math.random() * tanks.length)];
                float noise = (float)Math.random() * 500 - 250;
                int w = game.getWorld().width();
                noise = noise < 0 ? 0 : (noise > w ? w - 150 : noise);
                powerup.init((int)(tank.getX() + noise), (int)powerup.getY());
                game.addEntity(powerup);
            }
        }
    }
    
}
