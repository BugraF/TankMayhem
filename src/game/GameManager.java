package game;

import processing.core.PApplet;

/**
 *
 * @author Burak Gök
 */
public class GameManager {
    /**
     * Currently active instance of this class.
     */
    private static GameManager instance = null;
    
    private ScreenManager screenManager;
    
    public GameManager() {
        instance = this;
        PApplet.main(new String[] {"game.ScreenManager"});
        screenManager = ScreenManager.getInstance();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new GameManager();
    }
    
    /**
     * Returns the currently active instance of this class.
     */
    public static GameManager getInstance() {
        return instance;
    }
    
    @Override
    public void finalize() {
        if (instance == this)
            instance = null;
    }
    
}
