package game.engine;

import processing.core.PGraphics;

/**
 * Any object (game entity) that needs to be drawn will implement this.
 * @author Burak Gök
 */
public interface RenderObj {
    /**
     * @param bounds The bounds of the region desired to draw.
     *               Bounds are given in the format: Left, Top, Right, Bottom
     *               Bound-checking before any draw operation is encouraged.
     */
    void draw(PGraphics g, int[] bounds);
}
