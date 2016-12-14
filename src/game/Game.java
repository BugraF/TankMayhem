package game;

import game.engine.WorldObj;
import game.engine.World;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import processing.core.PImage;

/**
 * Singleton + Mediator + Façade
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
    private final Terrain terrain;
    
    /** Static game world (terrain and tanks) */
    private final World world;
    
    /** Game entity catalog */
    private final Catalog catalog;
    
//    private Map<String, ObservableAttribute> observableAttributes;
    
    private final CircularList<Player>.CircularIterator players; // TODO Implement CircularList
    
    public Game(GameManager manager, String map, Player[] players) {
        instance = this;
        this.manager = manager;
        
        physics = new PhysicsEngine();
        stage = new Stage();
        catalog = new Catalog();
        
        AssetManager assetManager = manager.getAssetManager();
        Map<String, Object> maps = assetManager.readJSONObject("config/maps.json");
        Map<String, Object> assets = (Map<String, Object>) maps.get(map);
        
        PImage terrainTexture = (PImage) assets.get("terrain.texture");
        Object[] surfaces = assetManager.readJSONArray("config/surfaces.json");
        Map<String, Object> surface = (Map<String, Object>)
                surfaces[(int)(Math.random() * surfaces.length)];
        PImage terrainSurface = assetManager.loadAsset((String)surface.get("name"));
        PImage terrainImage = AssetManager.mask(terrainTexture, terrainSurface);
        terrain = new Terrain(terrainImage, 2, (int)surface.get("sky"));
        
        world = new World(terrainImage.width, 768); // TODO Buğra: Specify height
              // Height should be fixed and can be greater than the screen height
        stage.setWorld(world);
        addEntity(terrain);
        
        Map<String, Object> decInfo = (Map<String, Object>) assets.get("decoration");
        Decoration decoration = Decoration.create((String) decInfo.get("name"));
        decoration.setResources((PImage[]) decInfo.get("resources"));
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
        return players.current();
    }
    
    /** Returns the tank of the current players. */
    public Tank getActiveTank() {
        return players.current().getTank();
    }
    
    Terrain getTerrain() {
        return terrain;
    }
    
    PhysicsEngine getPhysicsEngine() {
        return physics;
    }
    
    World getWorld() {
        return world;
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
        players.current().updateScore((int)(damageGiven * 0)); // TODO Specify factor
        players.current().updateCash((int)(damageGiven * 0)); // TODO Specify factor
    }
    
    void switchTurn() {
        Player curPlayer = players.current();
        players.next();
        while (!players.current().isAlive())
            players.next();
        
        if (players.current() == curPlayer) {
            gameOver();
            return;
        }
        Tank tank = getActiveTank();
        stage.shiftCamera((int)tank.getX(), (int)tank.getY());
        if (players.current() instanceof AI)
            ((AI)players.current()).play();
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
        // observableAttributes.get("game_over").set(true);
    }
    
//    public Map<String, ObservableAttribute> getAttributes() {
//        return observableAttributes;
//    }
    
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
    
    private class CircularList<E> extends ArrayList<E> {
        @Override
        public CircularIterator iterator() {
            return new CircularIterator();
        }
        
        class CircularIterator implements Iterator<E> {
            int cursor = 0;
            
            @Override
            public boolean hasNext() {
                return !isEmpty();
            }
            
            public E current() {
                return get(cursor);
            }

            @Override
            public E next() {
                cursor = cursor++ % size();
                return get(cursor);
            }
        }
    }
    
}
