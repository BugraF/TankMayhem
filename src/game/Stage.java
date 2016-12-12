package game;

import game.engine.Renderer;
import game.engine.World;
import gui.core.InteractiveComponent;
import gui.core.MouseEvent;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class Stage extends InteractiveComponent {

    private World world;
    private Decoration decoration;
    private final Renderer renderer = new Renderer();
    private final int[] camera = new int[4];
    
    private Interaction interaction;
    private int z_order;
    
    private final TracerInteraction tracer = new TracerInteraction();
    private boolean tracerEnabled = false;
    
    /**
     * Creates a reference to the game world.
     * This reference is used to learn world constraints.
     */
    void setWorld(World world) {
        this.world = world;
    }
    
    void setDecoration(Decoration decoration) {
        this.decoration = decoration;
    }
    
    Renderer getRenderer() {
        return renderer;
    }
    
    Interaction getInteraction() {
        return interaction;
    }
    
    /**
     * Sets the interaction by specifying its z-order.
     * @param z_order Use negative values to draw the specified interaction back
     *                of any render object. For non-negative values, the
     *                specified interaction is drawn as the fore-most layer.
     */
    void setInteraction(int z_order, Interaction interaction) {
        this.z_order = z_order;
        this.interaction = interaction;
    }
    
    /**
     * Enabled/disables the tracer.
     */
    void setTracer(boolean enabled) {
        tracerEnabled = enabled;
    }
    
    /**
     * Shifts the camera to the specified focus point slowly.
     */
    void shiftCamera(int x, int y) {
        // TODO Implement shiftCamera()
    }
    
    /**
     * Sets the visible region of the game world.
     * The specified point is used as the focus of the camera.
     */
    void setCamera(int x, int y) {
        int hWidth = width / 2;
        int hHeight = height / 2;
        
        if (x < hWidth) x = hWidth;
        else if (x > world.width() - hWidth) x = world.width() - hWidth;
        if (y < hHeight) x = hHeight;
        else if (x > world.height() - hHeight) x = world.height()- hHeight;
        
        camera[0] = x - hWidth;
        camera[1] = y - hHeight;
        camera[2] = x + hWidth;
        camera[3] = y + hHeight;
    }
    
    @Override
    public void draw(PGraphics g) {
        decoration.drawBackground(g, bounds);
        if (tracerEnabled)
            tracer.draw(g);
        if (z_order < 0)
            interaction.draw(g);
        renderer.draw(g, bounds);
        decoration.drawForeground(g, bounds);
        if (z_order >= 0)
            interaction.draw(g);
    }
    
    @Override
    public void handleKeyEvent(KeyEvent event) {
        if (enabled)
            propagateKeyEvent(interaction, event);
    }
    
    @Override
    public boolean handleMouseEvent(MouseEvent event) {
        if (!enabled) return true;
        return propagateMouseEvent(interaction, event);
    }
    
}
