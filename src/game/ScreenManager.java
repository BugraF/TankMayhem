package game;

import gui.Parent;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Singleton
 * @author Burak Gök
 */
public class ScreenManager extends PApplet {
    /**
     * Currently active instance of this class.
     */
    private static ScreenManager instance = null;
    
    /**
     * Maintains game menus.
     */
    private final Parent topLevelParent = new Parent() {
        @Override
        public PApplet getApplicationFrame() {
            return ScreenManager.this;
        }
    };
    
    public ScreenManager() {
        instance = this;
    }
    
    @Override
    public void settings() {
        // TODO Buğra: Update screen size
        size(1280, 768, "processing.awt.PGraphicsJava2D");
    }
    
    @Override
    public void setup() {
        
    }
    
    @Override
    public void draw() {
        
    }
    
    public Parent getTopLevelParent() {
        return topLevelParent;
    }
    
    @Override
    public void handleKeyEvent(KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.PRESS:
                topLevelParent.keyPressed(event);
                break;
            case KeyEvent.RELEASE:
                topLevelParent.keyReleased(event);
                break;
            case KeyEvent.TYPE:
                topLevelParent.keyTyped(event);
                break;
        }
    }
    
    @Override
    public void handleMouseEvent(MouseEvent event) {
        switch (event.getAction()) {
            case MouseEvent.PRESS:
                topLevelParent.mousePressed(new gui.MouseEvent(event));
                break;
            case MouseEvent.RELEASE:
                topLevelParent.mouseReleased(new gui.MouseEvent(event));
                break;
            case MouseEvent.CLICK:
                topLevelParent.mouseClicked(new gui.MouseEvent(event));
                break;
            case MouseEvent.DRAG:
                topLevelParent.mouseDragged(new gui.MouseEvent(event));
                break;
            case MouseEvent.MOVE:
                topLevelParent.mouseMoved(new gui.MouseEvent(event));
                break;
            case MouseEvent.ENTER:
                topLevelParent.mouseEntered(new gui.MouseEvent(event));
                break;
            case MouseEvent.EXIT:
                topLevelParent.mouseExited(new gui.MouseEvent(event));
                break;
            case MouseEvent.WHEEL:
                topLevelParent.mouseWheel(new gui.MouseEvent(event));
                break;
        }
    }
    
    /**
     * Returns the currently active instance of this class.
     */
    public static ScreenManager getInstance() {
        return instance;
    }
    
    @Override
    public void finalize() {
        if (instance == this)
            instance = null;
    }
    
}
