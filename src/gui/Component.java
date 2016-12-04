package gui;

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
     * Top, Left, Bottom, Right
     */
    protected int[] bounds;
    
//    /**
//     * Indicates whether this component is responsive to key and mouse events.
//     */
//    protected boolean enabled;
    
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
    
    /**
     * Sets the bounds of this component.
     */
    public void setBounds(int top, int left, int bottom, int right) {
        bounds = new int[] {top, left, bottom, right};
    }
    
//    /**
//     * Enables/disables this component.
//     */
    public abstract void setEnabled(boolean enabled);
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
    
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
    public PApplet getApplicationFrame() {
        return getTopLevelParent().getApplicationFrame();
    }
    
    /**
     * Draws this component inside its bounds.
     */
    public abstract void draw(PGraphics g);
    
}
