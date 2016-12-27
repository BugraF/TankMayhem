package game.engine;

import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public class Renderer {
    
    private final List<RenderObj> objects = new ArrayList<>(200);
    
    /**
     * Draws all render objects by providing them with the bounds of the region
     * that is desired to draw.
     */
    public void draw(PGraphics g, int[] bounds) {
        for (int i = 0; i < objects.size(); i++)
            objects.get(i).draw(g, bounds);
    }
    
    /**
     * Adds the specified render object to this renderer.
     * The objects are drawn in the order they are added to the renderer.
     */
    public void add(RenderObj obj) {
        objects.add(obj);
    }
    
    /**
     * Removes the specified object from this renderer.
     */
    public void remove(RenderObj obj) {
        objects.remove(obj);
    }
    
}
