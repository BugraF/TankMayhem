package game;

import game.engine.RenderObj;
import game.engine.WorldObj;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author Burak Gök
 */
public class Terrain implements WorldObj, RenderObj {
    /**
     * The atomicity of particles (particle width in pixels).
     */
    int destructionResolution;
    
    /**
     * Current image of this terrain.
     */
    private final PImage image;
    
    /**
     * Top padding.
     * The space left for the sky.
     */
    private final int sky;
    private final int[] bounds;
    
    /**
     * Creates a terrain with the specified image.
     * @param destructionResolution Particle width
     * @param sky Top padding, the space left for the sky
     */
    public Terrain(PImage image, int destructionResolution, int sky) {
        this.image = image;
        this.destructionResolution = destructionResolution;
        this.sky = sky;
        bounds = new int[] {0, sky, image.width, image.height + sky};
    }
    
    /**
     * Locks the updates on the terrain image.
     * Must be called before drawing this terrain.
     */
    public void update() {
        image.updatePixels();
    }
    
    /**
     * Color the pixel at the specified point.
     */
    public void addPixel(int color, int x, int y) {
        if (x > 0 && x < image.width && y > 0 && y < image.height)
            image.pixels[x + y * image.width] = color;
    }
    
    /**
     * Makes the pixel at the specified point transparent.
     * Equivalent to addPixel(0, x, y).
     */
    public void removePixel(int x, int y) {
        if (x > 0 && x < image.width && y > 0 && y < image.height)
            image.pixels[x + y * image.width] = 0;
    }
    
    /**
     * Returns the color of the pixel at the specified point.
     * If the specified point is out of the bounds of this terrain,
     * #00000000 is returned.
     */
    public int getColor(int x, int y) {
        if (x > 0 && x < image.width && y > 0 && y < image.height)
            return image.pixels[x + y * image.width];
        return 0;
    }
    
    /**
     * Computes the normal vector at the specified point.
     * @return X and Y components of the normal vector.
     */
    public float[] getNormal(int x, int y) {
        // First find all nearby solid pixels, and create a vector to the
        // average solid pixel from (x,y)
        float avgX = 0;
        float avgY = 0;
        for (int w = -3; w <= 3; w++)
            for (int h = -3; h <= 3; h++)
                if (isPixelSolid(x + w, y + h)) {
                    avgX -= w;
                    avgY -= h;
                }
        // Get the distance from (x,y)
        float len = (float)Math.sqrt(avgX * avgX + avgY * avgY);
        // Normalize the vector by dividing by that distance
        return new float[] {avgX/len, avgY/len};
    }
    
    /**
     * Determines if the pixel at the specified point is solid based on whether
     * or not it's transparent.
     */
    public boolean isPixelSolid(int x, int y) {
        if (x > 0 && x < image.width && y > 0 && y < image.height)
            return image.pixels[x + y * image.width] != 0;
        return false; // out of bounds, not solid
    }
    
    @Override
    public PImage getMask() {
        return image;
    }

    @Override
    public int[] getBounds() {
        return bounds;
    }

    @Override
    public void draw(PGraphics g, int[] bounds) {
        g.image(image, bounds[0], bounds[1] + sky);
    }
    
}
