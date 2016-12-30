package game;

import game.engine.WorldObj;
import game.engine.World;
import game.engine.PhysicsObj;
import game.engine.RenderObj;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Façade
 * @author Burak Gök
 */
public class Game implements SelectionChangeListener {
    
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
    
    private final CircularList<Player>.CircularIterator players;
    
    /**
     * Maintains and notifies turn switch listeners.
     * Whether the game is over is passed an argument to the listeners.
     */
    private final Turn turn = new Turn();
    
    public Game(GameManager manager, String map, Player[] players) {
        this.manager = manager;
        
        physics = new PhysicsEngine(this);
        stage = new Stage(this);
        catalog = new Catalog(this);
        
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
        decoration.setResources((PImage[]) decInfo.get("resources"));
        decoration.setWorld(world); // World boundaries
        decoration.setTerrain(terrain); // Terrain.getMask() -> image
        stage.setDecoration(decoration);
        
        JSONObject modes = assetManager.readJSONObject("config/modes.json");
        Map<String, Object> modeInfo = assetManager.process(modes);
        CircularList<Player> playerList = new CircularList<>(players.length);
        final int free = 150;
        final int occupy = (terrainImage.width - (players.length + 1) * free)
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
            Tank tank = new Tank(this, (PImage) mode.get("image"), 
                    (int)mode.get("barrel"), player.getColor(),
                    (int[])mode.get("hitbox"));
            
            y += (int)surface.get("sky") - ((PImage)mode.get("image")).height / 2;
            System.out.println("x: " + x + ", y: " + y);
            
            tank.init(x, y, ((Number)mode.get("damage")).floatValue(), 
                            ((Number)mode.get("shield")).floatValue());
            
            tank.fireAngle = (float)(-Math.PI / 6);
            tank.firePower = 0.8f;
            
            addEntity(tank);
            player.setTank(tank);
//            player.getInventory().add(0, Integer.MAX_VALUE);
            for (int i = 0; i < Catalog.SIZE; i++) // Test
                player.getInventory().add(i, 5);
            playerList.add(player);
        }
        this.players = playerList.iterator();
        
        catalog.init(); // Initialization order does not matter.
    }
    
    public void start() {
        physics.setWind((int)(Math.random() * 100) - 50);
        Tank tank = getActiveTank();
        stage.shiftCamera((int)tank.getX(), (int)tank.getY());
        turn.next();
    }
    public void stop() {
        physics.stop();
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
    
    public Catalog getCatalog() {
        return catalog;
    }
    
    public int getWind() {
        return physics.getWind();
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
    
    @Override
    public void selectionChanged(int itemId) {
        Interaction interaction = ((Catalog)catalog).getInteraction(itemId);
        if (interaction != null) {
            interaction.setTank(getActiveTank());
            stage.setInteraction(interaction);
        }
        else { // null indicates Tracer
            stage.setTracer(true);
            getCurrentPlayer().getInventory()
                    .remove(catalog.get("tracer").getId());
        } 
    }
    
    /**
     * Increments the score and cash of the current player by an amount 
     * proportional to the given damage.
     * @param damageGiven The total damage delivered to other players
     */
    void updatePlayerStatus(float damageGiven) {
        players.current().updateScore((int)(damageGiven * 10));
        players.current().updateCash((int)(damageGiven * 50));
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
        stage.getInteraction().setTank(tank);
        
        physics.setWind((int)(Math.random() * 100) - 50);
        
        turn.next(); // Notify listeners
        
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
        turn.end(); // Notify listeners
    }
    
    /**
     * Associates the specified listener to the turn switch event of this game.
     * The specified listener is provided with a boolean flag that indicates
     * whether the game is over at each turn switch.
     */
    public void addTurnListener(Observer listener) {
        turn.addObserver(listener);
    }
    public void removeTurnListener(Observer listener) {
        turn.deleteObserver(listener);
    }
    
    private class Turn extends Observable {
        public void next() {
            setChanged();
            notifyObservers(false);
        }
        public void end() {
            setChanged();
            notifyObservers(true);
        }
    }
    
    /**
     * Returns the asset manager associated to the manager of this class.
     */
    AssetManager getAssetManager() {
        return manager.getAssetManager();
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
