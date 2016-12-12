package game.engine;

/**
 * Any object that will need motion integrated will implement this.
 * @author Burak Gök
 */
public interface PhysicsObj {
    float getX();
    float getY();
    float getVx();
    float getVy();
    
    void setX(float x);
    void setY(float y);
    void setVx(float vX);
    void setVy(float vY);
    
    /** Indicates whether the object is stable along the x-axis. */
    boolean isXStable();
    /** Indicates whether the object is stable along the y-axis. */
    boolean isYStable();
    /** Triggers the object to check against the boundaries and collisions. */
    void checkConstraints();
}
