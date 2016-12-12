package game;

import game.engine.WorldObj;
import game.engine.World;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import java.util.Map;
import processing.core.PImage;

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
    private final PhysicsEngine physics;
    
    /**
     * The component that draws all game entities and provides interaction with
     * the game.
     */
    private final Stage stage;
    
    /** Static game world (terrain and tanks) */
    private final World world;
    
    /** Game entity catalog */
    private final Catalog catalog;
    
//    private Map<String, ObservableAttribute> observableAttributes;
    
    private final CircularIterator<Player> players; // TODO Implement CircularList
    
    public Game(GameManager manager, String map, Player[] players) {
        instance = this;
        this.manager = manager;
        
        physics = new PhysicsEngine();
        stage = new Stage();
        catalog = new Catalog();
        
        AssetManager assetManager = manager.getAssetManager();
        Map<String, Object> assets 
                = assetManager.readConfigurationFile("maps.json");
        
        PImage terrainTexture = (PImage) assets.get("terrain.texture");
        String[] files = assetManager.getFiles("terrain/surface");
        PImage terrainSurface = assetManager.loadAsset(
                files[(int)(Math.random() * files.length)]);
        PImage terrainImage = AssetManager.mask(terrainTexture, terrainSurface);
        Terrain terrain = new Terrain(terrainImage, 2);
        
        world = new World(terrainImage.width, 768); // TODO Buğra: Specify height
              // Height should be fixed and can be greater than the screen height
        stage.setWorld(world);
        addEntity(terrain);
        
        Decoration decoration = Decoration
                .create((String) assets.get("decoration.name"));
        decoration.setResources((PImage[]) assets.get("decoration.resources"));
        decoration.setTerrain(terrain); // Terrain.getMask() -> image
        stage.setDecoration(decoration);
        
        CircularList<Player> playerList = new CircularList<>(players.length);
        int free = 50;
        int occupy = (terrainImage.width - (players.length - 1) * free)
                / players.length;
        int left = 0;
        for (Player player : players) {
            int x = left + (int)(Math.random() * occupy);
            left += free;
            int y = terrainImage.height - 1;
            while (terrainImage.pixels[x + y * terrainImage.height] >>> 24 == 0)
                y--;
            Tank tank = Tank.create(player.getMode(), x, y, player.getColor());
            addEntity(tank);
            player.setTank(tank);
            playerList.add(player);
        }
        this.players = playerList.iterator();
    }
    
    public Player getCurrentPlayer() {
        return players.get();
    }
    
    /** Returns the tank of the current players. */
    public Tank getActiveTank() {
        return players.get().getTank();
    }
    
    /**
     * Adds the specified game entity to the related control objects
     * (PhysicsEngine, World, Stage).
     */
    void addEntity(Object entity) {
        if (entity instanceof PhysicsObj)
            physics.add((PhysicsObj) entity);
        if (entity instanceof WorldObj)
            world.add((WorldObj) entity);
        if (entity instanceof RenderObj)
            stage.getRenderer().add((RenderObj) entity);
    }
    
    /**
     * Removes the specified game entity from the related control objects
     * (PhysicsEngine, World, Stage).
     */
    void removeEntity(Object entity) {
        if (entity instanceof PhysicsObj)
            physics.remove((PhysicsObj) entity);
        if (entity instanceof WorldObj)
            world.remove((WorldObj) entity);
        if (entity instanceof RenderObj)
            stage.getRenderer().remove((RenderObj) entity);
    }
    
    /**
     * Increments the score and cash of the current player by an amount 
     * proportional to the given damage.
     * @param damageGiven The total damage delivered to other players
     */
    void updatePlayerStatus(float damageGiven) {
        players.get().updateScore((int)(damageGiven * 0)); // TODO Specify factor
        players.get().updateCash((int)(damageGiven * 0)); // TODO Specify factor
    }
    
    void switchTurn() {
        Player curPlayer = players.get();
        players.next();
        while (!players.get().isAlive())
            players.next();
        
        if (players.get() == curPlayer) {
            gameOver();
            return;
        }
        Tank tank = getActiveTank();
        stage.shiftCamera((int)tank.getX(), (int)tank.getY());
        if (players.get() instanceof AI)
            ((AI)players.get()).play();
    }
    
    /**
     * Sets a delayed turn switch that occurs when the particle animation ends.
     * A delayed turn switch can be cancelled by calling this method before it
     * occurs.
     * @param switchTurn True to set a delayed turn switch. False to cancel a
     *                   previously set delayed turn switch.
     */
    void switchTurnWhenStabilized(boolean switchTurn) {
        physics.switchTurnWhenStabilized = switchTurn;
    }
    
    /**
     * Called when the game is over.
     * Loads the end-game screen.
     */
    void gameOver() {
        
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
