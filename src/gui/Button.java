package gui;

import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;
import processing.core.PImage;

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
    private boolean freeShape;
    
    /**
     * Current state of this button.
     */
    private int state = 0;
    
    /**
     * All state images of this button.
     * When the button is disabled, the gray-scaled normal state image is used.
     */
    private PImage[] stateImages; // normal, hover, pressed, disabled
    
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
     * @param normal Drawn when the mouse pointer is outside of this button
     * @param hover Drawn when the mouse pointer is inside of this button
     * @param pressed Drawn when the mouse is pressed on this button
     */
    public void setImages(PImage normal, PImage hover, PImage pressed) {
        PImage disabled = normal.copy();
        disabled.filter(PImage.GRAY);
        stateImages = new PImage[] {normal, hover, pressed, disabled};
    }
    
    @Override
    public void draw(PGraphics g) {
        g.image(stateImages[state], bounds[1], bounds[0]);
    }
    
    private boolean enabled = true;
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public boolean mousePressed(MouseEvent e) {
        if (freeShape) {
            PImage curImg = stateImages[state];
            if (getApplicationFrame().alpha(
                    curImg.pixels[e.getX() + e.getY() * curImg.width]) == 0)
                return false;
        }
        state = 2;
        return true;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        if (freeShape) {
            PImage curImg = stateImages[state];
            if (getApplicationFrame().alpha(
                    curImg.pixels[e.getX() + e.getY() * curImg.width]) == 0) {
                state = 0;
                return false;
            }
        }
        state = 1;
        return true;
    }
    
    @Override
    public boolean mouseClicked(MouseEvent e) {
        if (freeShape) {
            PImage curImg = stateImages[state];
            if (getApplicationFrame().alpha(
                    curImg.pixels[e.getX() + e.getY() * curImg.width]) == 0)
                return false;
        }
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(this);
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        state = 0;
        return true;
    }
    
}
