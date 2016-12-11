package game;

import gui.ScreenManager;
import processing.core.PApplet;

/**
 * Singleton
 * @author Burak Gök
 */
public class GameManager {
    /**
     * Currently active instance of this class.
     */
    private static GameManager instance = null;
    
    private Game game = null;
    
    private ScreenManager screenManager;
    private AssetManager assetManager; // TODO Implement using PApplet
                                       // If functions are trivial, remove this.
//    private SoundManager soundManager;
    
    public GameManager() {
        instance = this;
        PApplet.main(new String[] {"gui.ScreenManager"});
        screenManager = ScreenManager.getInstance();
        assetManager = new AssetManager(screenManager);
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
//    public void playMusic(Mode mode) {
//        
//    }
//    
//    public void playSound(String key) {
//        
//    }
    
    /**
     * Starts a new game with the specified map and players.
     * @param map Game map
     * @param players Player list
     */
    public void startNewGame(String map, Player[] players) {
        game = new Game(map, players);
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
