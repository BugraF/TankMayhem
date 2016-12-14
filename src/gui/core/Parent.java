package gui.core;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import processing.core.PApplet;
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
     * Only interactive components can be focused.
     */
    private InteractiveComponent focusedChild = null;
    
    /**
     * Code of the key used by this parent to be focused.
     */
    private int focusKey;
    
    /**
     * Components owned by this parent.
     */
    private final SortedSet<Component> components;
    
    /**
     * Interactive components mapped to their interested keys.
     */
    private final Map<Integer, InteractiveComponent> keyListeners;
    
    /**
     * Interactive components sorted according to their z-order.
     */
    private final SortedSet<InteractiveComponent> mouseListeners;
    
    public Parent() {
        components = new TreeSet<>((c1, c2) -> c1.z_order - c2.z_order);
        keyListeners = new HashMap<>();
        mouseListeners = new TreeSet<>((m1, m2) -> m2.z_order - m1.z_order);
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
        if (comp instanceof InteractiveComponent)
            mouseListeners.add((InteractiveComponent) comp);
    }
    
    /**
     * Adds the given components to this parent by assigning them z-orders
     * according to the order in which they are given.
     */
    public void add(Component... comps) {
        int z = components.isEmpty() ? 0 : components.last().z_order;
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
            if (comp instanceof InteractiveComponent)
                mouseListeners.remove((InteractiveComponent) comp);
        }
        return success;
    }
    
    // TODO updateComponents() -> Collections (Sets, Lists) -> Arrays
    //                            to enable fast iteration
    
    @Override
    public void init(PApplet context) {
        super.init(context);
        components.forEach((c) -> c.init(context));
    }
    
    /**
     * Sets the background image.
     * @param background Null to remove the background image.
     */
    public void setBackground(PImage background) {
        this.background = background;
    }
    
    /**
     * Draws the components owned by this parent.
     */
    protected void drawComponents(PGraphics g) {
        components.forEach((c) -> {
            g.translate(c.bounds[0], c.bounds[1]);
            c.draw(g);
            g.translate(-c.bounds[0], -c.bounds[1]);
        });
    }
    
    /**
     * Sets the child that has the focus.
     * @see #focusedChild
     */
    void setFocusedChild(InteractiveComponent comp) {
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
     * Associates the given keys with the specified component.
     */
    public void associateKeys(InteractiveComponent comp, int... interestedKeys) {
        for (int key : interestedKeys) {
            if (keyListeners.containsKey(key))
                throw new KeyConflictException(key, keyListeners.get(key));
            keyListeners.put(key, comp);
        }
    }
    
    private PImage background = null;
    private PImage disabledImage = null;
    
    @Override
    public void draw(PGraphics g) {
        if (enabled) {
            if (background != null)
                g.image(background, 0, 0);
            drawComponents(g);
        }
        else
            g.image(disabledImage, 0, 0);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled)
            disabledImage = null;
        else {
            PGraphics g2 = getContext().createGraphics(width, height);
            g2.beginDraw();
            drawComponents(g2);
            g2.endDraw();
            g2.filter(PImage.BLUR, 6);
            disabledImage = g2;
        }
    }

    @Override
    public void handleKeyEvent(KeyEvent e) {
        if (!enabled) return;
        propagateKeyEvent(this, e);
        InteractiveComponent listener = keyListeners.get(e.getKeyCode());
        if (listener != null)
            listener.handleKeyEvent(e);
        else if (focusedChild != null)
            focusedChild.handleKeyEvent(e);
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

    private InteractiveComponent lastMouseHandler = this; // avoid null-checking
    
    @Override
    public boolean handleMouseEvent(MouseEvent e) {
        if (!enabled) return true;
        for (InteractiveComponent listener : mouseListeners) {
            if (inside(listener.bounds, e.getX(), e.getY())) {
                if (lastMouseHandler != listener)
                    e.derive(processing.event.MouseEvent.ENTER);
                
                boolean consumed = listener.handleMouseEvent(
                        e.translate(-listener.bounds[0], -listener.bounds[1]));
                if (consumed) {
                    if (lastMouseHandler != listener && lastMouseHandler != this)
                        lastMouseHandler.handleMouseEvent(
                                e.derive(processing.event.MouseEvent.EXIT));
                    lastMouseHandler = listener;
                    return true;
                }
                e.revert();
                e.translate(listener.bounds[0], listener.bounds[1]);
            }
        }
        if (lastMouseHandler != this) {
            lastMouseHandler.handleMouseEvent(
                    e.derive(processing.event.MouseEvent.EXIT));
            e.revert();
        }
        lastMouseHandler = this;
        return propagateMouseEvent(this, e);
    }
    
    private static boolean inside(int[] bounds, int x, int y) {
        // Right and bottom bounds are exclusive (see: Component#bounds)
        return bounds[0] <= x && x < bounds[2] && 
               bounds[1] <= y && y < bounds[3];
    }
    
}
