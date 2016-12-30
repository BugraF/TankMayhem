package game;

import game.engine.RenderObj;
import game.engine.Renderer;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author Burak Gök
 */
public class Badge implements RenderObj {

    private final Renderer renderer;
    private final PImage badge;
    private final float x, y;
    private final long creation;
    
    public Badge(Renderer renderer, PImage badge, float x, float y) {
        this.renderer = renderer;
        this.badge = badge;
        this.x = x;
        this.y = y;
        creation = System.currentTimeMillis();
    }
    
    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.translate(x, y);
        g.image(badge, -badge.width / 2, -badge.height);
        g.translate(-x, -y);
        
        long now = System.currentTimeMillis();
        if (now - creation >= 2000)
            renderer.remove(this);
    }
    
}
