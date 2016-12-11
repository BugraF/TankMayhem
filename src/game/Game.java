package game;

import java.util.Map;

/**
 * Singleton
 * @author Burak Gök
 */
public class Game {
    /**
     * Currently active instance of this class.
     */
    private static Game instance = null;
    
    private final GameManager manager;
    
    /** Physics Engine */
    private PhysicsEngine physics;
    
    /**
     * The component that draws all game entities and provides interaction with
     * the game.
     */
    private Stage stage;
    
    /** Static game world (terrain and tanks) */
    private World world;
    
    /** Game entity catalog */
    private Catalog catalog;
    
    private Map<String, ObservableAttribute> observableAttributes;
    
    private CircularListIterator<Player> players; // TODO Implement
    
    public Game(GameManager manager, String map, Player[] players) {
        instance = this;
        this.manager = manager;
        
        physics = new PhysicsEngine();
        stage = new Stage();
        world = new World();
        catalog = new Catalog();
        
        Map<String, Object> assets = manager.getAssetManager()
                .readConfigurationFile("maps.json");
        
    }
    
    /**
     * Returns the currently active instance of this class.
     */
    public static Game getInstance() {
        return instance;
    }
    
    @Override
    public void finalize() {
        if (instance == this)
            instance = null;
    }
    
}
