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
    /** Context of this stage */
    private final Game game;
    private World world;
    
    private Decoration decoration;
    private Terrain terrain;
    private final Renderer renderer = new Renderer();
    
    /** Camera Bounds (x1, y1, x2, y2) -> (l, t, r, b) */
    private final int[] camBounds = new int[4];
    /** Camera Focus (x, y) */
    private final float[] cam = new float[2], camNext = new float[2];
    
    private Interaction interaction;
//    private final TracerInteraction tracer = new TracerInteraction();
    private boolean tracerEnabled = false;
    
    public Stage(Game game) {
        this.game = game;
    }
    
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
    
    void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    
    Renderer getRenderer() {
        return renderer;
    }
    
    Interaction getInteraction() {
        return interaction;
    }
    
    int[] getCameraBounds() {
        return camBounds;
    }

    void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
    
    public void finalizeInteraction() {
        interaction._finalize();
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
        camNext[0] = PApplet.constrain(x, hWidth, world.width() - hWidth);
        camNext[1] = PApplet.constrain(y, hHeight, world.height() - hHeight);

        updateCamera = true;
//        System.out.format("Next Camera: [%s, %s]\n", camNext[0], camNext[1]);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        hWidth = width / 2;
        hHeight = height / 2;
        if (camBounds[2] + camBounds[3] == 0) { // Initialization
            cam[0] = hWidth;
            cam[1] = hHeight;
        }
        setCamera((int)camNext[0], (int)camNext[1]);
    }
    
    // TODO Another graphics object should be passed to this method since
    // images do not support drawing a specific region.
    @Override
    public void draw(PGraphics g) {
        if (shiftCamera) {
            cam[0] = PApplet.lerp(cam[0], camNext[0], 0.1f);
            cam[1] = PApplet.lerp(cam[1], camNext[1], 0.1f);
            updateCamera();
            if (Math.abs(cam[0] - camNext[0]) < 1 
                    && Math.abs(cam[1] - camNext[1]) < 1)
                shiftCamera = false;
        } else if (updateCamera) {
            cam[0] = camNext[0];
            cam[1] = camNext[1];
            updateCamera();
            updateCamera = false;
        }
        
        decoration.drawBackground(g, camBounds);
        g.translate(-camBounds[0], -camBounds[1]);
        
//        g.image(world.getImageRepresentation(getContext()), 0, 0);
//        if (tracerEnabled)
//            tracer.drawBehindTerrain(g);
        if (interaction.isEnabled())
            interaction.drawBehindTerrain(g);
        terrain.draw(g, camBounds);
        if (interaction.isEnabled())
            interaction.drawAfterTerrain(g);
        renderer.draw(g, camBounds);
        
        g.translate(camBounds[0], camBounds[1]);
        decoration.drawForeground(g, camBounds);
    }
    
    private void updateCamera() {
        camBounds[0] = (int)cam[0] - hWidth;
        camBounds[1] = (int)cam[1] - hHeight;
        camBounds[2] = (int)cam[0] + hWidth;
        camBounds[3] = (int)cam[1] + hHeight;
//        System.out.format("Camera: [%s, %s]\n", cam[0], cam[1]);
    }
    
    @Override
    public void handleKeyEvent(KeyEvent event) {
        if (enabled)
            propagateKeyEvent(this, event);
        if (interaction.isEnabled())
            propagateKeyEvent(interaction, event);
    }
    
    @Override
    public boolean handleMouseEvent(MouseEvent event) {
        if (!enabled || !interaction.isEnabled()) return true;
        return propagateMouseEvent(interaction, event);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 100) // Numpad 4
            shiftCamera((int)cam[0] - 100, (int)cam[1]);
        else if (e.getKeyCode() == 102) // Numpad 6
            shiftCamera((int)cam[0] + 100, (int)cam[1]);
        else if (e.getKeyCode() == 104) // Numpad 8
            shiftCamera((int)cam[0], (int)cam[1] - 50);
        else if (e.getKeyCode() == 98) // Numpad 2
            shiftCamera((int)cam[0], (int)cam[1] + 50);
    }
    
}
