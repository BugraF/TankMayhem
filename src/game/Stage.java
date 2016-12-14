package game;

import game.engine.Renderer;
import game.engine.World;
import gui.core.InteractiveComponent;
import gui.core.MouseEvent;
import processing.core.PApplet;
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
    private final int[] camBounds = new int[4], // x1, y1, x2, y2 (l, t, r, b)
                        cam = new int[2], camNext = new int[2]; // x, y (center)
    
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
    
    private boolean updateCamera = false,
                    shiftCamera = false;
    
    /**
     * Shifts the camera to the specified point slowly.
     * @see #setCamera(int, int) 
     */
    void shiftCamera(int x, int y) {
        setCamera(x, y);
        shiftCamera = true;
    }
    
    private int hWidth, hHeight;
    
    /**
     * Sets the visible region of the game world.
     * The specified point is used as the focus of the camera.
     */
    void setCamera(int x, int y) {
        hWidth = width / 2;
        hHeight = height / 2;
        
        camNext[0] = PApplet.constrain(x, hWidth, world.width() - hWidth);
        camNext[1] = PApplet.constrain(y, hHeight, world.height() - hHeight);
        
        updateCamera = true;
    }
    
    // TODO Another graphics object should be passed to this method since
    // images do not support drawing a specific region.
    @Override
    public void draw(PGraphics g) {
        if (shiftCamera) {
            cam[0] = (int)PApplet.lerp(cam[0], camNext[0], 0.1f);
            cam[1] = (int)PApplet.lerp(cam[1], camNext[1], 0.1f);
            if (cam[0] == camNext[0] && cam[1] == camNext[1])
                shiftCamera = updateCamera = false;
        } else if (updateCamera) {
            cam[0] = camNext[0];
            cam[1] = camNext[1];
            updateCamera = false;
        }
        if (updateCamera) {
            camBounds[0] = cam[0] - hWidth;
            camBounds[1] = cam[1] - hHeight;
            camBounds[2] = cam[0] + hWidth;
            camBounds[3] = cam[1] + hHeight;
        }
        g.translate(camBounds[0], camBounds[1]);
        
        decoration.drawBackground(g, camBounds);
        if (tracerEnabled)
            tracer.draw(g);
        if (z_order < 0)
            interaction.draw(g);
        renderer.draw(g, camBounds);
        decoration.drawForeground(g, camBounds);
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
