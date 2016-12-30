package game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import processing.core.PImage;

/**
 *
 * @author aytajabbaszade
 */
public class Catalog {
    /** Context of this catalog */
    private final Game context;
    
    public final static int SIZE = 6;
    private final Map<String, Integer> idMap = new HashMap<>(SIZE);
    private final CatalogItem[] items = new CatalogItem[SIZE];
    
    private final Interaction[] interactions = new Interaction[SIZE];
    
    public Catalog(Game context) {
        this.context = context;
    }
    
    void init() {
        AssetManager assetManager = context.getAssetManager();
        
        PImage i0 = assetManager.loadAsset("entity/bomb/simple.png");
        items[0] = new BombDefinition(0, "simple_bomb", "Simple Bomb", i0) {
            public Bomb create(Object... args) {
                Bomb bomb = new SimpleBomb(context);
                bomb.blastPower = args.length > 0 ? (int)args[0] : 60;
                return bomb;
            }
        };
        
        PImage i1 = assetManager.loadAsset("entity/bomb/bouncing.png");
        items[1] = new BombDefinition(1, "bouncing_bomb", "Bouncing Bomb", i1) {
            public Bomb create(Object... args) {
                Bomb bomb = new BouncingBomb(context);
                bomb.blastPower = args.length > 0 ? (int)args[0] : 60;
                return bomb;
            }
        };
        
        PImage i2 = assetManager.loadAsset("entity/bomb/one-bounce.png");
        items[2] = new BombDefinition(2, "1bounce_bomb", "1-Bounce Bomb", i2) {
            public Bomb create(Object... args) {
                Bomb bomb = new OneBounceBomb(context);
                bomb.blastPower = args.length > 0 ? (int)args[0] : 60;
                return bomb;
            }
        };
        
        PImage i3 = assetManager.loadAsset("entity/bomb/volcano.png");
        items[3] = new BombDefinition(3, "volcano_bomb", "Volcano Bomb", i3) {
            public Bomb create(Object... args) {
                Bomb bomb = new VolcanoBomb(context);
                bomb.blastPower = args.length > 0 ? (int)args[0] : 60;
                return bomb;
            }
        };
        
        PImage box = assetManager.loadAsset("entity/powerup/box.png");
        PImage parachute = assetManager.loadAsset("entity/powerup/parachute.png");
        
        PImage i4 = assetManager.loadAsset("entity/powerup/airstrike.png");
        PImage b4 = assetManager.loadAsset("entity/powerup/airstrike_badge.png");
        items[4] = new CollectiblePowerUp(4, "airstrike", "Airstrike", i4, b4,
                box, parachute) {
            public Interaction interact(Object... args) {
                return new AirstrikeInteraction(context);
            }
        };
        
        PImage i5 = assetManager.loadAsset("entity/powerup/tracer.png");
        PImage b5 = assetManager.loadAsset("entity/powerup/tracer_badge.png");
        items[5] = new CollectiblePowerUp(5, "tracer", "Tracer", i5, b5,
                box, parachute) {
            public Interaction interact(Object... args) {
                return null;
            }
        };
        
        for (CatalogItem item : items)
            idMap.put(item.getKey(), item.getId());
        
        Interaction fire = new FireInteraction(context);
        for (int id = 0; id < 4; id++)
            interactions[id] = fire;
        interactions[4] = new AirstrikeInteraction(context);
    }
    
    public Object create(int itemId, Object... args) {
        return get(itemId).create(args);
    }
    
    public Interaction interact(int itemId, Object... args) {
        return get(itemId).interact(args);
    }

    public CatalogItem get(int itemId) {
        return items[itemId];
    }

    public CatalogItem get(String itemKey) {
        return items[idMap.get(itemKey)];
    }
    
    public boolean isBomb(int itemID) {
        return items[itemID] instanceof BombDefinition;
    }
    
    public boolean isPowerUp(int itemID) {
        return items[itemID] instanceof PowerUpDefinition;
    }

    public Iterator<CatalogItem> iterator() {
        return new Iterator<CatalogItem>() {
            private int cursor = 0;
            
            @Override
            public boolean hasNext() {
                return cursor != SIZE;
            }

            @Override
            public CatalogItem next() {
                return items[cursor++];
            }
        };
    }
    
    Interaction getInteraction(int itemId) {
        Interaction interaction = interactions[itemId];
        if (isBomb(itemId))
            ((FireInteraction)interaction).setItemId(itemId);
        return interaction;
    }
    
    PowerUp getRandomPowerUp() {
        return (PowerUp) items[4 + (int)(Math.random() * 2)].create();
    }
    
    private abstract class BombDefinition extends CatalogItem {
        public BombDefinition(int id, String key, String name, PImage icon) {
            super(id, key, name, icon);
        }

        @Override
        public abstract Bomb create(Object... args);

        @Override
        public FireInteraction interact(Object... args) {
            FireInteraction interaction = new FireInteraction(context);
            interaction.setItemId(id);
            return interaction;
        }
    }
    
    private abstract class PowerUpDefinition extends CatalogItem {
        protected final PImage badge;
        
        public PowerUpDefinition(int id, String key, String name, PImage icon, 
                PImage badge) {
            super(id, key, name, icon);
            this.badge = badge;
        }

        @Override
        public abstract PowerUp create(Object... args);
    }
    
    private abstract class CollectiblePowerUp extends PowerUpDefinition {
        private final PImage box, parachute;
        
        public CollectiblePowerUp(int id, String key, String name, PImage icon,
                PImage badge, PImage box, PImage parachute) {
            super(id, key, name, icon, badge);
            this.box = box;
            this.parachute = parachute;
        }

        @Override
        public PowerUp create(Object... args) {
            PowerUp powerup = new PowerUp(context) {
                public void activate(Tank tank) {
                    Inventory inventory = tank.getPlayer().getInventory();
                    inventory.add(id);
                    inventory.notifyObservers();
                    showBadge(badge);
                }
            };
            powerup.setImages(box, parachute);
            powerup.setY(-box.height / 2);
            return powerup;
        }
    }
    
}
