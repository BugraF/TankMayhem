package game;

import gui.Parent;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 *
 * @author Burak Gök
 */
public class ScreenManager extends PApplet {
    /**
     * Maintains game menus.
     */
    private final Parent topLevelParent = new Parent() {
        @Override
        public PApplet getApplicationFrame() {
            return ScreenManager.this;
        }
    };
    
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
                topLevelParent.mousePressed(event);
                break;
            case MouseEvent.RELEASE:
                topLevelParent.mouseReleased(event);
                break;
            case MouseEvent.CLICK:
                topLevelParent.mouseClicked(event);
                break;
            case MouseEvent.DRAG:
                topLevelParent.mouseDragged(event);
                break;
            case MouseEvent.MOVE:
                topLevelParent.mouseMoved(event);
                break;
            case MouseEvent.ENTER:
                topLevelParent.mouseEntered(event);
                break;
            case MouseEvent.EXIT:
                topLevelParent.mouseExited(event);
                break;
            case MouseEvent.WHEEL:
                topLevelParent.mouseWheel(event);
                break;
        }
    }
    
}
