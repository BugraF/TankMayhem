package game;

import processing.core.PImage;

/**
 * Any object that will need to be detectable will implement this.
 * @author Burak Gök
 */
public interface WorldObj {
    /**
     * Returns the collision mask image of this object.
     * The real collision mask for the object is created on its world by
     * checking the alpha channel of the mask image.
     */
    PImage getMask();
    
    /**
     * Returns the bounds of this object.
     */
    int[] getBounds();
}
