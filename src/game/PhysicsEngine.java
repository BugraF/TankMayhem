package game;

import game.engine.Physics;
import game.engine.PhysicsObj;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Burak Gök
 */
public class PhysicsEngine {
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
    
    public PhysicsEngine() {
        physics.addObjectClasses(bombs, particles, powerups, tanks);
    }
    
    /**
     * Sets the wind (this applies a common acceleration to all physics objects).
     * @param wind Negative values are associated with the left direction.
     */
    void setWind(int wind) {
        physics.setAcceleration(wind, 980, 0, 1, 2);
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
        if (switchTurnWhenStabilized && bombs.isEmpty() && particles.isEmpty())
            Game.getInstance().switchTurn();
    }
    
    /**
     * Adds the specified physics object to the underlying physics engine.
     */
    void add(PhysicsObj obj) {
        if (obj instanceof Bomb)
            bombs.add(obj);
        else if (obj instanceof Particle)
            particles.add(obj);
        else if (obj instanceof PowerUp)
            powerups.add(obj);
        else if (obj instanceof Tank)
            tanks.add(obj);
    }
    
    /**
     * Removes the specified physics object from the underlying physics engine.
     */
    void remove(PhysicsObj obj) {
        if (obj instanceof Bomb)
            bombs.remove(obj);
        else if (obj instanceof Particle)
            particles.remove(obj);
        else if (obj instanceof PowerUp)
            powerups.remove(obj);
        else if (obj instanceof Tank)
            tanks.remove(obj);
    }
    
}
