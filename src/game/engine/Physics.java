package game.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Burak Gök
 */
public class Physics {
    
    private final List<List<PhysicsObj>> objectClasses;
    
    /** Last checked system time in milliseconds */
    private long previousTime;
    
    /** Current system time in milliseconds */
    private long currentTime;
    
    /**
     * The time difference between two consecutive time steps in milliseconds
     * (default value is 16 ms).
     */
    private int deltaTime;
    
    /**
     * The time difference between two consecutive time steps in seconds.
     */
    private float deltaTimeInSeconds;
    
    /**
     * The time passed in between two checks, which is smaller than the delta
     * time and so could not be used to iterate a time step. {@see #deltaTime}
     * This value is pushed over to the next update.
     */
    private int leftOverDeltaTime = 0;
    
    /** The common accelerations for object classes */
    private List<int[]> acceleration;
    
    public Physics() {
        this(16);
    }
    
    /**
     * Creates a physics engine by specifying the delta time
     * @param deltaTime @see #deltaTime
     */
    public Physics(int deltaTime) {
        this.deltaTime = deltaTime;
        deltaTimeInSeconds = (float)deltaTime / 1000.0f;
        objectClasses = new ArrayList<>(5);
    }
    
    /**
     * Adds the specified object classes.
     * Object classes can be used to assign different acceleration values for
     * different kinds of physics objects.
     */
    public void addObjectClasses(List<PhysicsObj>... objClasses) {
        objectClasses.addAll(Arrays.asList(objClasses));
    }
    
    /**
     * Sets the acceleration of the physics objects that are in the specified
     * object classes.
     * @param x X component of the acceleration
     * @param y Y component of the acceleration
     * @param classes The indices of the object classes desired to update
     */
    public void setAcceleration(int x, int y, int... classes) {
        for (int index : classes) {
            acceleration.get(index)[0] = x;
            acceleration.get(index)[1] = y;
        }
    }
    
    /**
     * Updates the positions and velocities of all physics objects.
     * At most one-time step is iterated in the end of this method call to
     * prevent the game from iterating too much time steps when the game becomes
     * unresponsive. Collisions are checked by calling checkConstraints() method
     * of each physics object.
     */
    public void update() {
        currentTime = System.currentTimeMillis();
        long deltaTimeMS = currentTime - previousTime;
    
        previousTime = currentTime; // Reset previousTime
    
        // Find out how many timesteps we can fit inside the elapsed time
        int timeStepAmt = (int)((float)(deltaTimeMS + leftOverDeltaTime)
                / (float)deltaTime); 
    
        // Limit the timestep amount to prevent freezing
        timeStepAmt = Math.min(timeStepAmt, 1);
  
        // Store left over time for the next frame
        leftOverDeltaTime = (int)deltaTimeMS - (timeStepAmt * deltaTime);
    
        for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
            for (int i = 0; i < objectClasses.size(); i++) {
                List<PhysicsObj> objClass = objectClasses.get(i);
                for (PhysicsObj object : objClass) {
                    if (!object.isXStable()) {
                        float velX = object.getVx();
                        velX += acceleration.get(i)[0] * deltaTimeInSeconds;
                        object.setX(object.getX() + velX * deltaTimeInSeconds);
                    }
                    if (!object.isYStable()) {
                        float velY = object.getVy();
                        velY += acceleration.get(i)[1] * deltaTimeInSeconds;
                        object.setY(object.getY() + velY * deltaTimeInSeconds);
                    }
                    object.checkConstraints();
                }
            }
        }
    }
    
}
