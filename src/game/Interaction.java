package game;

import gui.core.KeyListener;
import gui.core.MouseEvent;
import gui.core.MouseListener;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 * Adapter + Strategy
 * This class is used to decouple interaction details for different
 * collectible game entities (inventory items) from the stage.
 * @author Burak Gök
 */
public abstract class Interaction implements KeyListener, MouseListener {
    
    final static int DEFAULT_ACTION_COLOR = 0; // TODO Specify default color
    
    /**
     * Returns the action name of the interaction such as "Fire".
     * The action name is displayed in the launch button.
     */
    abstract String getAction();
    
    /**
     * Returns the action color of the interaction.
     * The action color is used to paint the launch button.
     */
    int getActionColor() {
        return DEFAULT_ACTION_COLOR;
    }
    
    /**
     * Draws the interaction visuals such as aiming assistant.
     */
    abstract void draw(PGraphics g);
    
    /**
     * Finalizes the interaction and also expected to remove the related item
     * from the current player’s inventory.
     * 
     * (Since the "finalize" method is called by garbage collector before
     * releasing the objects, we use an underscore to differentiate them.)
     */
    abstract void _finalize();

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public boolean mousePressed(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseReleased(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseDragged(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseEntered(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        return true;
    }

    @Override
    public boolean mouseWheel(MouseEvent e) {
        return true;
    }
}
