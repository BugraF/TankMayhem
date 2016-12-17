package game;

import game.engine.WorldObj;
import game.engine.World;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Singleton + Mediator + Façade
 * @author Burak Gök
 */
public class Game implements SelectionChangeListener {
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
//    private final Catalog catalog;
    
//    private Map<String, ObservableAttribute> observableAttributes;
    
    private final CircularList<Player>.CircularIterator players;
    
    public Game(GameManager manager, String map, Player[] players) {
        instance = this;
        this.manager = manager;
        
        physics = new PhysicsEngine(this);
        stage = new Stage(this);
//        catalog = new Catalog();
        
        AssetManager assetManager = manager.getAssetManager();
        JSONObject mapInfo = assetManager.readJSONObject("config/maps.json")
                .getJSONObject(map);
        Map<String, Object> assets = assetManager.process(mapInfo);
        
        PImage terrainTexture = (PImage) assets.get("terrain.texture");
        JSONArray surfaces = assetManager.readJSONArray("config/surfaces.json");
        Map<String, Object> surface = assetManager.process(
                surfaces.getJSONObject((int)(Math.random() * surfaces.size())));
        PImage terrainSurface = (PImage) surface.get("image");
        PImage terrainImage = AssetManager.mask(terrainTexture, terrainSurface);
        terrain = new Terrain(terrainImage, 2, (int)surface.get("sky"));
        
        world = new World(terrain.getBounds()[2], terrain.getBounds()[3]);
        stage.setWorld(world);
        world.add(terrain);
        stage.setTerrain(terrain);
        
        Map<String, Object> decInfo = (Map<String, Object>) assets.get("decoration");
        Decoration decoration = Decoration.create((String) decInfo.get("name"));
//        decoration.setResources((PImage[]) decInfo.get("resources"));
        decoration.setTerrain(terrain); // Terrain.getMask() -> image
        stage.setDecoration(decoration);
        
        JSONObject modes = assetManager.readJSONObject("config/modes.json");
        Map<String, Object> modeInfo = assetManager.process(modes);
        CircularList<Player> playerList = new CircularList<>(players.length);
        int free = 50;
        int occupy = (terrainImage.width - (players.length + 1) * free)
                / players.length;
        int left = free;
        for (Player player : players) {
            int x = left + (int)(Math.random() * occupy);
            left += occupy + free;
            int y = terrainImage.height - 1;
            while (terrainImage.pixels[x + y * terrainImage.width] >>> 24 != 0)
                y--;
            
            Map<String, Object> mode = (Map<String, Object>)
                    modeInfo.get(player.getMode().toString());
            Tank tank = new Tank(this, 
                    AssetManager.fill((PImage) mode.get("image"), 0,
                    player.getColor()), (int)mode.get("barrel"), player.getColor());
            
            y += (int)surface.get("sky") - ((PImage)mode.get("image")).height / 2;
            System.out.println("x: " + x + ", y: " + y);
            
            tank.init(x, y, ((Number)mode.get("damage")).floatValue(), 
                            ((Number)mode.get("shield")).floatValue());
            
            tank.fireAngle = (float)(Math.random() * Math.PI); // Test
            tank.firePower = 0.8f; // Test
            tank.updateHp(100); // Test, otherwise infinite loop in switchTurn()
            
            addEntity(tank);
            player.setTank(tank);
            playerList.add(player);
        }
        this.players = playerList.iterator();
        
        Tank tank = getActiveTank();
        stage.setSize(1280, 500); // TODO Decoupling ????
        stage.shiftCamera((int)tank.getX(), (int)tank.getY());
        fi = new FireInteraction(this);
    }
    
    public Player[] getPlayers() {
        return players.list().toArray(new Player[players.count()]);
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
    
    public Stage getStage() {
        return stage;
    }
    
    public void update() {
        physics.update();
        terrain.update();
//        world.updateMask(terrain, terrain.getBounds()); // Test
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
    
    private final Interaction fi; // Test
    @Override
    public void selectionChanged(int itemId) {
        stage.setInteraction(0, fi);
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
        ((FireInteraction)fi).setTank(tank); // Test
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
    
//    /**
//     * Returns the asset manager associated to the manager of this class.
//     */
//    AssetManager getAssetManager() {
//        return manager.getAssetManager();
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
        public CircularList(int initialCapacity) {
            super(initialCapacity);
        }
        
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
                cursor = ++cursor % size();
                return get(cursor);
            }
            
            public CircularList<E> list() {
                return CircularList.this;
            }
            
            public int count() {
                return size();
            }
        }
    }
    
}
