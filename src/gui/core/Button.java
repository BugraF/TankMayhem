package gui.core;

import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class Button extends InteractiveComponent {
    /**
     * Indicates whether this button is non-rectangular.
     * Non-rectangular buttons consume mouse events only if the mouse pointer
     * is on a non-transparent point.
     */
    private boolean freeShape = false;
    
    /**
     * Current state of this button.
     */
    private int state = 0;
    
    /**
     * Sprite sheet of the state images of this button.
     */
    private PImage stateImages; // normal, hover, pressed, disabled
    
    /** Height of a state image. **/
    private int stateHeight;
    
    /**
     * Key code of the mnemonic of this button.
     * A mnemonic can be a key combination.
     */
    private int mnemonic;
    
    /**
     * The objects that listen to the invocation of this button.
     */
    private final List<ActionListener> actionListeners = new ArrayList<>(1);
    
    /**
     * Sets whether this button is non-rectangular.
     * @see #freeShape
     */
    public void setFreeShape(boolean freeShape) {
        this.freeShape = freeShape;
    }
    
    /**
     * Sets the state images for this button.
     * @param stateImages Sprite sheet of the state images of this button.
     *                    The images should be placed vertically.
     * @param neverDisabled Indicates whether {@code stateImages} contains the
     *                      disabled-state image. The height of a state image is
     *                      computed using this parameter.
     */
    public void setStateImages(PImage stateImages, boolean neverDisabled) {
        this.stateImages = stateImages;
        stateHeight = stateImages.height / (neverDisabled ? 3 : 4);
    }
    
    @Override
    public void draw(PGraphics g) {
        g.image(stateImages, 0, 0, width, height,
                             0, state * stateHeight, width, stateHeight);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled)
            state = 3;
    }
    
    /**
     * Sets the interested key for this button.
     * When this key is pressed, the button will be invoked and do its
     * click-action.
     * @param key Button mnemonic, can be a key combination.
     */
    public void setMnemonic(int key) {
        mnemonic = key;
        if (parent != null)
            parent.associateKeys(this, key);
    }
    
    /**
     * Associates the specified action listener to this button.
     * @return False if this button is already associated to the specified
     *         action listener, true otherwise.
     */
    public boolean addActionListener(ActionListener listener) {
        if (actionListeners.contains(listener))
            return false;
        actionListeners.add(listener);
        return true;
    }
    
    /**
     * Disassociates the specified action listener from this button.
     * @return False if the specified action listener is not associated to this
     *         button, true otherwise.
     */
    public boolean removeActionListener(ActionListener listener) {
        return actionListeners.remove(listener);
    }
    
    private void invoke() {
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == mnemonic)
            invoke();
    }

    @Override
    public boolean handleMouseEvent(MouseEvent event) {
        if (!consumeEvent(event)) return false;
        if (!enabled) return true;
        return propagateMouseEvent(this, event);
    }
    
    private boolean consumeEvent(MouseEvent e) {
        if (freeShape)
            if (stateImages.pixels[e.getX() + e.getY() * width] >>> 24 == 0)
                return false;
        return true;
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        state = 2;
        return true;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        state = 1;
        return true;
    }
    
    @Override
    public boolean mouseClicked(MouseEvent e) {
        invoke();
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        state = 0;
        return true;
    }
    
}
