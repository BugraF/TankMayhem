package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * <h3>Composite Component</h3>
 * 
 * When a parent catches a mouse event, it checks its children one by one in
 * their z-order. The look-up process ends when the mouse event is consumed by
 * a child.
 * 
 * Some components such as buttons can be free-shape (or non-rectangular). When
 * a free-shape component catches a mouse event, it checks the location of the
 * mouse pointer against the alpha channel of its image and consumes the mouse
 * event accordingly.
 * 
 * When a parent catches a key event, it checks whether there is a child
 * listening to the key. If a key listener is found, it is invoked by passing
 * the key event to it. If the key is not listened by any component, the key
 * event is passed to the focused child, so that it can continue the search if
 * it is a parent or it can consume the event. This process continues until a
 * component accepts the key or the end of the component tree is reached.
 *
 * @author Burak Gök
 */
public class Parent extends InteractiveComponent {
    /**
     * The child that has the focus.
     * If null, there is no focused child.
     */
    private Component focusedChild = null;
    
    /**
     * The code of the key used by this parent to be focused.
     */
    private int focusKey;
    
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
                (m1, m2) -> ((Component)m1).z_order - ((Component)m2).z_order);
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
        if (comp instanceof MouseListener)
            mouseListeners.add((MouseListener) comp);
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
        if (success) {
            comp.setParent(null);
            if (comp instanceof MouseListener)
                mouseListeners.remove((MouseListener) comp);
        }
        return success;
    }
    
    // TODO updateComponents() -> Collections (Sets, Lists) -> Arrays
    //                            to enable fast iteration
    
    /**
     * Draws the components owned by this parent.
     */
    private void drawComponents(PGraphics g) {
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
        focusKey = key;
        parent.associateKeys(this, key);
        return true;
    }
    
    /**
     * Associates the given keys with the specified key listener.
     */
    public void associateKeys(KeyListener listener, int... interestedKeys) {
        for (int key : interestedKeys) {
            if (keyListeners.containsKey(key))
                throw new KeyConflictException(key, keyListeners.get(key));
            keyListeners.put(key, listener);
        }
    }
    
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
        super.setEnabled(enabled);
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
    
    /*
    The castings from KeyListener/MouseListener to Component/InteractiveComponent
    in associateKeys(), handleKeyEvent(), handleMouseEvent() are because event
    listeners of components are handled by this class. Since a component can only
    be listened by itself in the current design, they are not wrong, albeit ugly.
    If the listener handling logic is decoupled from this class and moved to
    InteractiveComponent, this problem would be solved.
    */

    @Override
    public void handleKeyEvent(KeyEvent e) {
        if (!enabled) return;
        propagateKeyEvent(this, e);
        KeyListener listener = keyListeners.get(e.getKeyCode());
        if (listener != null)
            ((InteractiveComponent)listener).handleKeyEvent(e);
        else if (focusedChild != null && focusedChild instanceof KeyListener)
            ((InteractiveComponent)focusedChild).handleKeyEvent(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == focusKey)
            if (parent != null)
                parent.setFocusedChild(this);
    }
    
//    public void keyPressed(KeyEvent event) {
//        if (!enabled) return;
//        KeyListener listener = keyListeners.get(event.getKeyCode());
//        if (listener != null)
//            listener.keyPressed(event);
//        else if (focusedChild != null && focusedChild instanceof KeyListener)
//            ((KeyListener) focusedChild).keyPressed(event);
//    }
//    public void keyReleased(KeyEvent event) {
//        // TODO: Test keyPressed()
//    }
//    public void keyTyped(KeyEvent event) {
//        // TODO: Test keyPressed()
//    }

    @Override
    public boolean handleMouseEvent(MouseEvent e) {
        if (!enabled) return true;
        for (MouseListener listener : mouseListeners) {
            if (inside(((Component)listener).bounds, e.getX(), e.getY())) {
                boolean consumed = ((InteractiveComponent)listener)
                        .handleMouseEvent(e.translate(bounds[1], bounds[0]));
                if (consumed)
                    return true;
                e.translate(-bounds[1], -bounds[0]);
            }
        }
        return propagateMouseEvent(this, e);
    }
    
//    public boolean mousePressed(MouseEvent e) {
//        if (!enabled) return true;
//        for (MouseListener listener : mouseListeners) {
//            if (inside(((Component)listener).bounds, e.getX(), e.getY())) {
//                boolean consumed = listener.mousePressed(
//                        e.translate(bounds[1], bounds[0]));
//                if (consumed)
//                    return true;
//                e.translate(-bounds[1], -bounds[0]);
//            }
//        }
//        return false;
//    }
    
    // TODO Other mouse events
    
    private static boolean inside(int[] bounds, int x, int y) {
        return bounds[1] <= x && x <= bounds[3] && 
               bounds[0] <= y && y <= bounds[2]; 
    }
    
}
