package gui;

import gui.core.*;
import java.util.ArrayList;
import java.util.List;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 *
 * @author Buğra Felekoğlu
 */
public class FlatButton extends InteractiveComponent {
    
    /**
     * Current state of this button.
     */
    private int state = 0;  // normal, hover, pressed, disabled
    
    /**
     * Color states of button
     */
    private int normalColor = 0,
                hoverColor = 0,
                pressedColor = 0;
    
    /**
     * Image of this button.
     */
    private PImage icon; 
    
    /**
     * Key code of the mnemonic of this button.
     * A mnemonic can be a key combination.
     */
    private int mnemonic;
    
    /**
     * The objects that listen to the invocation of this button.
     */
    private final List<ActionListener> actionListeners = new ArrayList<>(1);
    
    @Override
    public void draw(PGraphics g) {
        switch(state){
            case 0:
                g.tint(normalColor);
                g.image(icon, 0, 0);
                g.noTint();
                break;
            case 1:
                g.tint(hoverColor);
                g.image(icon, 0, 0);
                g.noTint();
                break;
            case 2:
                g.tint(pressedColor);
                g.image(icon, 0, 0);
                g.noTint();
                break;
            case 3:
                g.tint(80);
                g.image(icon, 0, 0);
                g.noTint();
                break;
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        state = enabled ? 0 : 3;
    }
    
    /**
     * Sets the image of button
     * @param icon 
     */
    public void setImage(PImage icon) {
        this.icon = icon;
    }
    
    /**
     * Sets the colors of button according to the states of it
     * @param normal
     * @param hover
     * @param pressed 
     */
    public void setTintValues(int normal, int hover, int pressed) {
        normalColor = normal;
        hoverColor = hover;
        pressedColor = pressed;
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
        if (event.getAction() != processing.event.MouseEvent.EXIT
                && !consumeEvent(event)) return false;
        if (!enabled) return true;
        return propagateMouseEvent(this, event);
    }
    
    private boolean consumeEvent(MouseEvent e) {
        if (state == 3)
            return false;
        else
            return true;
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        state = 2;
        return true;
    }

    @Override
    public boolean mouseReleased(MouseEvent e) {
        state = 1;
        return true;
    }
    
    @Override
    public boolean mouseClicked(MouseEvent e) {
        invoke();
        return true;
    }
    
    @Override
    public boolean mouseEntered(MouseEvent e) {
        state = 1;
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        state = 0;
        return true;
    }
    
}
