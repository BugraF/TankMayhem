package gui;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 *
 * @author Burak Gök
 */
public abstract class Component { //implements KeyListener, MouseListener {
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
     * Top, Left, Bottom, Right // TODO Unify bounds
     */
    protected int[] bounds;
    
//    protected final List<KeyListener> keyListeners = new ArrayList<>(1);
//    protected final List<MouseListener> mouseListeners = new ArrayList<>(1);
//    
//    public boolean addKeyListener(KeyListener listener) {
//        if (keyListeners.contains(listener))
//            return false;
//        keyListeners.add(listener);
//        return true;
//    }
//    public boolean removeKeyListener(KeyListener listener) {
//        return keyListeners.remove(listener);
//    }
//    
//    public boolean addMouseListener(MouseListener listener) {
//        if (mouseListeners.contains(listener))
//            return false;
//        mouseListeners.add(listener);
//        return true;
//    }
//    public boolean removeMouseListener(MouseListener listener) {
//        return mouseListeners.remove(listener);
//    }
    
//    protected KeyListener keyListener = null;
//    protected MouseListener mouseListener = null;
    
//    public void setKeyListener(KeyListener listener) {
//        keyListener = listener;
//    }
//    public void setMouseListener(MouseListener listener) {
//        mouseListener = listener;
//    }
    
    /**
     * Sets the parent of this component
     */
    public void setParent(Parent parent) {
        this.parent = parent;
//        g = parent.getGraphics();
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
    public void setBounds(int top, int left, int bottom, int right) { // TODO Unify bounds
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
    public abstract void draw(PGraphics g);
    
}
