package game.engine;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Burak Gök
 */
public class World {
    /**
     * <b>The collision mask of the world objects added to this world.</b><br>
     * One dimensional array (in row-major order) is preferred for the sake of
     * performance, so indexing is done manually. Since every object layer is
     * stored by using one bit for each pixel, the collision mask can store up
     * to 32 different object layers. The layers are stored in little-endian
     * format, so the right-most bit has the lowest index.
     */
    private final int[] collisionMask;
    
    /** The width of the collision mask */
    private final int width;
    
    /** The width of the collision mask */
    private final int height;
    
    /**
     * Maps world objects to their corresponding bit indices in the collision
     * mask.
     */
    private final Map<WorldObj, Integer> bitMap = new HashMap<>(32);
    
    /**
     * Maps bit indices to their corresponding world objects.
     */
    private final Map<Integer, WorldObj> objMap = new HashMap<>(32);
    
    /**
     * Indicates the occupied object layers as 1’s in the binary representation.
     */
    private int occupied = 0;
    
    /**
     * The object bounds that are obtained during the last collision mask update.
     */
    private final int[][] lastBounds = new int[32][];
    
    /**
     * Creates a world that has the specified size.
     * The world created can support up to 32 object layers.
     */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        collisionMask = new int[width * height];
    }
    
    /** Returns the width of this world. */
    public int width() {
        return width;
    }
    
    /** Returns the height of this world. */
    public int height() {
        return height;
    }
    
    /** Adds the specified world object to this world. */
    public void add(WorldObj obj) {
        //int available = -1; // -1: 1..1 may speed up
        int index = 0;
        while ((occupied & (1 << index)) != 0) // Right to Left
            index++;
        occupied |= (1 << index); // 1: 0..01
        bitMap.put(obj, index);
        objMap.put(index, obj);
        lastBounds[index] = obj.getBounds();
        updateMask(obj, obj.getBounds());
    }
    
    /** Removed the specified world object from this world. */
    public void remove(WorldObj obj) {
        int index = bitMap.get(obj);
        bitMap.remove(obj);
        objMap.remove(index);
        occupied &= ~(1 << index); // Alternative: ^ (-1)
    }
    
    /** Returns the world objects that occupy the specified point. */
    public WorldObj[] analyze(int x, int y) {
        List<WorldObj> objects = new ArrayList<>(32);
        int value = collisionMask[x + y * width];
        for (int i = 0; i < 32; i++)
            if ((value & (1 << i)) != 0)
                objects.add(objMap.get(i));
        return objects.toArray(new WorldObj[0]);
    }
    
    /**
     * Uses Bresenham's line algorithm to efficiently loop between two points,
     * and find the first solid pixel.
     * @see #rayCast(int, int, int, int, int)
     */
    public int[] rayCast(int startX, int startY, int lastX, int lastY) {
        return rayCast(startX, startY, lastX, lastY, -1); // -1: 1..1
    }
    
    /**
     * Uses Bresenham's line algorithm to efficiently loop between two points,
     * and find the first solid pixel belonging to any world object specified
     * by the {@code checkMask} parameter.
     * This particular variation always starts from the first point, so
     * collisions don't happen at the wrong end.
     * @param checkMask The interested world objects are indicated by 1's in
     *                  the binary representation.
     * @return The collision information as an integer array
     * <pre>
     *        ||| x = ([0],[1]) point in empty space before collision point
     *        ||| o = ([2],[3]) collision point
     * (end)--||ox------- (start)
     *        ||| m = [4] value of collision mask at collision point, filtered
     * </pre>
     * @author Jared Counts
     * @see <a href="https://gamedevelopment.tutsplus.com/tutorials/coding-
     *      destructible-pixel-terrain-how-to-make-everything-explode--gamedev-45">
     *      Coding Destructible Pixel Terrain: How to Make Everything Explode</a>
     * @see <a href="http://www.gamedev.net/page/resources/_/technical/game-
     *      programming/line-drawing-algorithm-explained-r1275">
     *      Resource Used</a>
     */
    public int[] rayCast(int startX, int startY, int lastX, int lastY, 
            int checkMask) {
        int deltax = (int) Math.abs(lastX - startX);  // The difference between the x's
        int deltay = (int) Math.abs(lastY - startY);  // The difference between the y's
        int x = (int) startX;   // Start x off at the first pixel
        int y = (int) startY;   // Start y off at the first pixel
        int xinc1, xinc2, yinc1, yinc2;
        
        if (lastX >= startX) {    // The x-values are increasing
            xinc1 = 1;
            xinc2 = 1;
        } else {                  // The x-values are decreasing
            xinc1 = -1;
            xinc2 = -1;
        }

        if (lastY >= startY) {    // The y-values are increasing
            yinc1 = 1;
            yinc2 = 1;
        } else {                  // The y-values are decreasing
            yinc1 = -1;
            yinc2 = -1;
        }
        
        int den, num, numadd, numpixels;
        if (deltax >= deltay) {      // There is at least one x-value for every y-value
            xinc1 = 0;               // Don't change the x when numerator >= denominator
            yinc2 = 0;               // Don't change the y for every iteration
            den = deltax;
            num = deltax / 2;
            numadd = deltay;
            numpixels = deltax;      // There are more x-values than y-values
        } else {                     // There is at least one y-value for every x-value
            xinc2 = 0;               // Don't change the x for every iteration
            yinc1 = 0;               // Don't change the y when numerator >= denominator
            den = deltay;
            num = deltay / 2;
            numadd = deltax;
            numpixels = deltay;      // There are more y-values than x-values
        }
        
        int prevX = (int) startX;
        int prevY = (int) startY;
        for (int curpixel = 0; curpixel <= numpixels; curpixel++) {
            int check = isPixelSolid(x, y, checkMask);
            if (check != 0)
                return new int[] {prevX, prevY, x, y, check};
            
            prevX = x;
            prevY = y;

            num += numadd;           // Increase the numerator by the top of the fraction

            if (num >= den) {        // Check if numerator >= denominator
                num -= den;          // Calculate the new numerator value
                x += xinc1;          // Change the x as appropriate
                y += yinc1;          // Change the y as appropriate
            }

            x += xinc2;              // Change the x as appropriate
            y += yinc2;              // Change the y as appropriate
        }
        return new int[]{};
    }
    
    /**
     * Checks whether there is a world object at the specified point.
     */
    public boolean isPixelSolid(int x, int y) {
        return isPixelSolid(x, y, -1) != 0;
    }
    
    /**
     * Checks whether any world object specified by the {@code checkMask}
     * parameter occupies the specified point.
     * @param checkMask The interested world objects are indicated by 1's in
     *                  the binary representation.
     * @return Value of the collision mask at the specified point, filtered
     *         according to the {@code checkMask}.
     */
    public int isPixelSolid(int x, int y, int checkMask) {
        int value = collisionMask[x + y * width];
        return value & checkMask;
    }
    
    /**
     * Generates a check mask for the specified world objects.
     */
    public int generateCheckMask(WorldObj... objects) {
        int mask = 0;
        for (WorldObj obj : objects)
            mask &= (1 << bitMap.get(obj));
        return mask;
    }
    
    /**
     * Updates a portion of the collision mask for the specified world object.
     * @param diffBounds The region that needs to be updated on the collision
     *                   mask. If null is passed, this region is computed
     *                   automatically as the union of the current and last
     *                   bounds of the object (see: {@see #lastBounds}).
     */
    public void updateMask(WorldObj obj, int[] diffBounds) {
        int index = bitMap.get(obj);
        int[] pixels = obj.getMask().pixels;
        int imgWidth = obj.getMask().width;
        int[] bounds = obj.getBounds();
        
        if (diffBounds == null) {
            int[] lastBounds = this.lastBounds[index];
            diffBounds = new int[] {
                min(lastBounds[0], bounds[0]), min(lastBounds[1], bounds[1]), // l, t
                max(lastBounds[2], bounds[2]), max(lastBounds[3], bounds[3])  // r, b
            };
        }
        this.lastBounds[index] = bounds;
        
        int solidMask = (1 << index);
        int emptyMask = ~solidMask; // Alternative: ^ (-1)
        // The loop order is optimized for spatial locality.
        for (int y = diffBounds[1]; y < diffBounds[3]; y++)
            for (int x = diffBounds[0]; x < diffBounds[2]; x++)
                // If the current point is inside the object bounds and has a
                // non-transparent color value.
                if (bounds[1] <= y && y < bounds[3]
                        && bounds[0] <= x && x < bounds[2]
                        && (pixels[x - bounds[0] + (y - bounds[1]) * imgWidth] 
                            >>> 24) != 0) // Unsigned shift
                                          // Other alternative: >> 24) & 0xFF
                    collisionMask[x + y * width] |= solidMask;
                // If the current point is outside the object bounds or has a
                // transparent color value.
                else
                    collisionMask[x + y * width] &= emptyMask;
    }
    
    /**
     * Refreshes or re-creates the collision mask.
     */
    public void refreshCollisionMask(boolean recreate) {
        if (recreate) {
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    collisionMask[x + y * width] = 0;
            for (WorldObj obj : bitMap.keySet())
                updateMask(obj, obj.getBounds());
        }
        else
            for (WorldObj obj : bitMap.keySet())
                updateMask(obj, null);
    }
    
}
