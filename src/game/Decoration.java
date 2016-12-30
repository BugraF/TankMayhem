package game;

import game.engine.World;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Decorations wrap render objects by a background and a foreground. A
 * decoration background may simply be an image or an animation. It can even
 * generate particles for precipitation (rain, snow). A foreground essentially
 * contains vegetation. The tanks belonging to eliminated players can also be
 * treated like vegetation in the decoration foreground.
 * 
 * @author Burak Gök
 */
public abstract class Decoration {
    
    protected World world;
    protected Terrain terrain;
    
    static Decoration create(String name) {
        switch (name) {
            case "Desert": return new DesertDecoration();
            case "Sunny": return new SunnyDecoration();
            case "Rainy": return new RainyDecoration();
            case "Snowy": return new SnowyDecoration();
            default: throw new RuntimeException("No such decoration: " + name);
        }
    }
    
    /**
     * Draws the background which may be a static image or an animation within
     * the scope of the specified bounds. The background may also generate
     * precipitation particles such as rain drops or snowflakes.
     */
    abstract void drawBackground(PGraphics g, int[] bounds);
    
    /**
     * Draws the foreground which consists of vegetation and dead players within
     * the scope of the specified bounds.
     */
    abstract void drawForeground(PGraphics g, int[] bounds);
    
    /**
     * Specifies the required resources of this decoration.
     */
    abstract void setResources(PImage... resources);
    
    /**
     * Associates this decoration to the specified world.
     */
    void setWorld(World world) {
        this.world = world;
    }
    
    /**
     * Associates this decoration to the specified terrain.
     * The terrain is used to compute the positions of foreground elements.
     */
    void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    
}
