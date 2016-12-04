package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * 
 * When a parent catches a mouse event, it checks its children one by one in
 * their z-order. The look-up process ends when the mouse event is consumed by
 * a child. (Some components such as buttons can be free-shape (or
 * non-rectangular). When a free-shape component catches a mouse event, it
 * checks the location of the mouse pointer against the alpha channel of its
 * image and consumes the mouse event accordingly.)
 * 
 * When a parent catches a key event, it checks whether there is a child
 * listening to the key. If a key listener is found, it is invoked by passing
 * the key event to it. If the key is not listened by any component, the key
 * event is passed to the focused child, so that it can continue the search in
 * the case it is a parent (it does not have to be an instance of this class).
 * This process continues until a component accepts the key or the leaf focused
 * child of the component tree is reached.

 * @author Burak Gök
 */
public class Parent extends Component implements InputListener {
    /**
     * The child that has the focus.
     * If null, there is no focused child.
     */
    private Component focusedChild = null;
    
    /**
     * The components owned by this parent.
     */
    private SortedSet<Component> components;
    
    private Map<Integer, KeyListener> keyListeners;
    private SortedSet<MouseListener> mouseListeners;
    
    public Parent() {
        components = new TreeSet<>((c1, c2) -> c2.z_order - c1.z_order);
        keyListeners = new HashMap<>();
        mouseListeners = new TreeSet<>(
                (m1, m2) -> m1.getZOrder() - m2.getZOrder());
    }
    
    /**
     * Adds the given component to this parent by specifying its z-order.
     * @param z_order The components closer to the front has higher z-orders.
     * @param comp The component wanted to add to this parent.
     */
    public void add(int z_order, Component comp) {
        comp.z_order = z_order;
        components.add(comp);
        comp.setParent(this);
    }
    
    /**
     * Adds the given components to this parent by assigning them z-orders
     * according to the order in which they are given.
     */
    public void add(Component... comps) {
        int z = components.last().z_order;
        for (Component comp : comps) 
            add(++z, comp);
    }
    
    /**
     * Removes the specified component from this parent.
     * @return True if this parent has the specified component
     */
    public boolean remove(Component comp) {
        boolean success = components.remove(comp);
        if (success)
            comp.setParent(null);
        return success;
    }
    
    // TODO updateComponents() -> Collections (Sets, Lists) -> Arrays
    //                            to enable fast iteration
    
    /**
     * Draws the components owned by this parent.
     */
    private void drawComponents(PGraphics g) { // TODO Unify bounds
        components.forEach((c) -> {
            g.translate(c.bounds[1], c.bounds[0]);
            c.draw(g);
            g.translate(-c.bounds[1], -c.bounds[0]);
        });
    }
    
    /**
     * Sets the child that has the focus.
     */
    void setFocusedChild(Component comp) {
        focusedChild = comp;
    }
    
    /**
     * Sets the interested key for this parent.
     * A parent catches the focus when the key in which it is interested is
     * pressed, so that its children can be invoked during the key events
     * involving their interested keys.
     * @return True if the focus key is successfully set. If this parent is a
     *         top-level parent, false is returned.
     */
    public boolean setFocusKey(int key) {
        if (parent == null)
            return false;
        parent.addKeyListener(this, key);
        return true;
    }
    
    /**
     * Associates the given keys with the specified key listener.
     */
    public void addKeyListener(KeyListener listener, int... interestedKeys) {
        for (int key : interestedKeys) {
            if (keyListeners.containsKey(key))
                throw new KeyConflictException(key, keyListeners.get(key));
            keyListeners.put(key, listener);
        }
    }
    
    /**
     * Adds the specified mouse listener.
     */
    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }
    
    private boolean enabled = true;
    private PImage disabledImage = null;
    
    @Override
    public void draw(PGraphics g) {
        if (enabled)
            drawComponents(g);
        else
            g.image(disabledImage, 0, 0);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        // TODO Unify bounds
        this.enabled = enabled;
        if (enabled)
            disabledImage = null;
        else {
            PGraphics g2 = getApplicationFrame()
                .createGraphics(bounds[3] - bounds[1], bounds[2] - bounds[0]);
            draw(g2);
            g2.filter(PImage.BLUR, 6);
            disabledImage = g2;
        }
    }
    
    public void keyPressed(KeyEvent event) {
        if (!enabled) return;
        KeyListener listener = keyListeners.get(event.getKeyCode());
        if (listener != null)
            listener.keyPressed(event);
        else if (focusedChild != null && focusedChild instanceof KeyListener)
            ((KeyListener) focusedChild).keyPressed(event);
    }
    public void keyReleased(KeyEvent event) {
        // TODO: Test keyPressed()
    }
    public void keyTyped(KeyEvent event) {
        // TODO: Test keyPressed()
    }
    
    public int[] getBounds() {
        return bounds;
    }
    public int getZOrder() {
        return z_order;
    }
    
    public boolean mousePressed(MouseEvent e) {
//        MouseEvent event = new MouseEvent(e.getNative(), e.getMillis(),
//                e.getAction(), e.getModifiers(), bounds[1], bounds[0], 
//                e.getButton(), e.getCount()); // slow and space-intensive
        // TODO Unify bounds
//        return mouseListeners.stream()
//                .filter((l) -> inside(l.getBounds(), e.getX(), e.getY()))
//                .map((listener) -> listener.mousePressed(
//                    e.translate(bounds[1], bounds[0])))
//                .anyMatch((consumed) -> (consumed));
        if (!enabled) return false;
        for (MouseListener listener : mouseListeners) {
            if (inside(listener.getBounds(), e.getX(), e.getY())) {
                boolean consumed = listener.mousePressed(
                        e.translate(bounds[1], bounds[0]));
                if (consumed)
                    return true;
                e.translate(-bounds[1], -bounds[0]);
            }
        }
        return false;
    }

    // A parent can catch a mouse event:
//    @Override
//    public boolean mousePressed(MouseEvent e) {
//        if (!super.mousePressed(e)) {
//            // Parent catches the event.
//        }
//    }
    
    public boolean mouseReleased(MouseEvent event) {
        return false;
    }
    public boolean mouseClicked(MouseEvent event) {
        return false;
    }
    public boolean mouseDragged(MouseEvent event) {
        return false;
    }
    public boolean mouseMoved(MouseEvent event) {
        return false;
    }
    public boolean mouseEntered(MouseEvent event) {
        return false;
    }
    public boolean mouseExited(MouseEvent event) {
        return false;
    }
    public boolean mouseWheel(MouseEvent event) {
        return false;
    }
    
    private static boolean inside(int[] bounds, int x, int y) { // TODO Unify bounds
        return bounds[1] <= x && x <= bounds[3] && 
               bounds[0] <= y && y <= bounds[2]; 
    }
    
}
