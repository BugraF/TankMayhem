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
    
    public final static int DEFAULT_ACTION_COLOR = 0xFFD70000;
    
    /** Context of this interaction */
    protected final Game game;
    
    /** Associated tank */
    protected Tank tank;
    
    /** Camera bounds of the stage of the game associated to this interaction */
    protected final int[] camBounds;
    
    private boolean enabled = true;
    
    public Interaction(Game game) {
        this.game = game;
        tank = game.getActiveTank();
        camBounds = game.getStage().getCameraBounds();
    }
    
    void setTank(Tank tank) {
        this.tank = tank;
        enabled = true;
    }
    
    /**
     * Returns the action name of the interaction such as "Fire".
     * The action name is displayed in the launch button.
     */
    public abstract String getAction();
    
    /**
     * Returns the action color of the interaction.
     * The action color is used to paint the launch button.
     */
    public int getActionColor() {
        return DEFAULT_ACTION_COLOR;
    }
    
    /**
     * Draws the interaction visuals behind the terrain.
     */
    public void drawBehindTerrain(PGraphics g) {}
    
    /**
     * Draws the interaction visuals after the terrain.
     */
    public void drawAfterTerrain(PGraphics g) {}
    
    /**
     * Finalizes the interaction and also expected to remove the related item
     * from the current player’s inventory.
     * 
     * (Since the "finalize" method is called by garbage collector before
     * releasing the objects, we use an underscore to differentiate them.)
     */
    public void _finalize() {
        enabled = false;
        tank.stopLeft();
        tank.stopRight();
    }
    
    boolean isEnabled() {
        return enabled;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 65) // A
            tank.moveLeft();
        else if (e.getKeyCode() == 68) // D
            tank.moveRight();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 65 || e.getKeyCode() == 68) {
            if (e.getKeyCode() == 65) // A
                tank.stopLeft();
            else // D
                tank.stopRight();
            game.getStage().shiftCamera((int)tank.getX(), (int)tank.getY());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
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
