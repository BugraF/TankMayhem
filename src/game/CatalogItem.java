package game;

import processing.core.PImage;

/**
 *
 * @author aytajabbaszade
 */
public abstract class CatalogItem {
    
    protected final int id;
    protected final String key;
    protected final String name;
    protected final PImage icon;
    
    public CatalogItem(int id, String key, String name, PImage icon) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.icon = icon;
    }
    
    public int getId() { return id; }
    public String getKey() { return key; }
    public String getName() { return name; }
    public PImage getIcon() { return icon; }
    
    public abstract Object create(Object... args);
    public abstract Interaction interact(Object... args);
    
}
