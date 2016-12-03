package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

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
    
    /**
     * The children that listen to key events.
     */
    private Map<Integer, KeyListener> keyListeners;
    
    /**
     * The children that listen to mouse events.
     */
    private SortedSet<MouseListener> mouseListeners;
    
    public Parent() {
        components = new TreeSet<>((c1, c2) -> c2.z_order - c1.z_order);
        keyListeners = new HashMap<>();
        mouseListeners = new TreeSet<>(
                (m1, m2) -> ((Component)m2).z_order - ((Component)m1).z_order);
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
    
    /**
     * Draws the components owned by this parent.
     */
    private void drawComponents() {
        components.forEach((c) -> c.draw());
    }
    
    /**
     * Returns the graphics object of this parent.
     */
    PGraphics getGraphics() {
        return g;
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
     */
    public void setFocusKey(int key) {
        if (parent != null)
            parent.addKeyListener(this, key);
    }
    
    /**
     * Associates the keys given with the specified key listener.
     */
    public void addKeyListener(KeyListener listener, int... interestedKeys) {
        for (int key : interestedKeys) {
            if (keyListeners.containsKey(key))
                throw new KeyConflictException(key, keyListeners.get(key));
            keyListeners.put(key, listener);
        }
    }
    
    @Override
    public void draw() {
        drawComponents();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        // TODO Gray-scaled, Unhandle key & mouse events
    }
    
    public void keyPressed(KeyEvent event) {
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
    
    public boolean mousePressed(MouseEvent event) {
//        for (MouseListener comp : mouseListeners) {
//            boolean consumed = comp.mousePressed(event);
//            if (consumed)
//                return true;
//        }
//        return false;
        return mouseListeners.stream()
                .map((comp) -> comp.mousePressed(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseReleased(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseReleased(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseClicked(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseClicked(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseDragged(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseDragged(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseMoved(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseMoved(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseEntered(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseEntered(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseExited(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseExited(event))
                .anyMatch((consumed) -> (consumed));
    }
    public boolean mouseWheel(MouseEvent event) {
        return mouseListeners.stream()
                .map((comp) -> comp.mouseWheel(event))
                .anyMatch((consumed) -> (consumed));
    }
    
}
