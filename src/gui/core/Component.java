package gui.core;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public abstract class Component {
    /**
     * Parent of this component.
     */
    protected Parent parent = null;
    
    /**
     * Z-order of this component.
     * The components closer to the front has higher z-orders.
     */
    int z_order;
    
    /**
     * Bounds of this component.
     * Left, Top, Right, Bottom
     */
    protected final int[] bounds = new int[4];
    
    /**
     * Size of this component.
     */
    protected int width, height;
    
    /**
     * Indicates whether this component is responsive to key and mouse events.
     */
    protected boolean enabled = true;
    
    /**
     * Sets the parent of this component
     */
    public void setParent(Parent parent) {
        this.parent = parent;
    }
    
//    /**
//     * Sets the z_order of this component.
//     * @param z_order @see #z_order
//     */
//    public void setZOrder(int z_order) {
//        this.z_order = z_order;
//    }
    
    public void setLocation(int x, int y) {
        bounds[0] = x;
        bounds[1] = y;
        bounds[2] = x + width;
        bounds[3] = y + height;
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        bounds[2] = bounds[0] + width;
        bounds[3] = bounds[1] + height;
    }
    
//    /**
//     * Enables/disables this component.
//     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
//    public abstract void setEnabled(boolean enabled);
    
    /**
     * Returns the top-level parent of this component.
     * If this component has no parent, this function returns null.
     */
    public Parent getTopLevelParent() {
        if (parent == null)
            return null;
        Parent p = parent;
        while (p.parent != null)
            p = p.parent;
        return p;
//        return parent == null ? null : parent.getTopLevelParent();
    }
    
    /**
     * Returns the application frame at which this component is placed.
     */
    public PApplet getContext() {
        return getTopLevelParent().getContext();
    }
    
    /**
     * Draws this component inside its bounds.
     */
    public abstract void draw(PGraphics g);
    
}
