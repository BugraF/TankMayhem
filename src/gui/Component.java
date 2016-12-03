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
     * Graphics object of this component.
     */
    protected PGraphics g;
    
    /**
     * Bounds of this component.
     * Top, Left, Bottom, Right // TODO Unify bounds
     */
    protected int[] bounds;
    
    /**
     * Sets the parent of this component
     */
    public void setParent(Parent parent) {
        this.parent = parent;
        g = parent.getGraphics();
    }
    
//    /**
//     * Sets the z_order of this component.
//     * @param z_order @see #z_order
//     */
//    public void setZOrder(int z_order) {
//        this.z_order = z_order;
//    }
    
    /**
     * Sets the graphics object of this component.
     */
    public void setGraphics(PGraphics g) {
        this.g = g;
    }
    
    /**
     * Sets the bounds of this component.
     * @param bounds Top, Left, Bottom, Right // TODO Unify bounds
     */
    public void setBounds(int[] bounds) {
        this.bounds = bounds;
    }
    
    /**
     * Enables/disables this component.
     */
    public abstract void setEnabled(boolean enabled);
    
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
    public abstract void draw();
    
}
