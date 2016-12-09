package gui;

import java.util.ArrayList;
import java.util.List;
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
     * Maintains game menus/screens.
     */
    private final Parent topLevelParent = new Parent() {
        @Override
        public PApplet getApplicationFrame() {
            return ScreenManager.this;
        }
    };
    
    private final List<Parent> screens = new ArrayList<>(3);
    private int currentScreen;
    
    final static int SCREEN_MAIN_MENU = 0,
                     SCREEN_NEW_GAME_MENU = 1,
                     SCREEN_GAME = 2;
    
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
        screens.add(new MainMenu());
        screens.add(new NewGameMenu());
        screens.add(new GameScreen());
        currentScreen = SCREEN_MAIN_MENU;
        topLevelParent.add(screens.get(currentScreen));
    }
    
    @Override
    public void draw() {
        topLevelParent.draw(g);
    }
    
    void switchScreen(int screen) {
        topLevelParent.remove(screens.get(currentScreen));
        topLevelParent.add(screens.get(screen));
        currentScreen = screen;
    }
    
    // There is no point for exposing the top-level parent.
    // ScreenManager should be responsible for all screen-related operations.
//    public Parent getTopLevelParent() {
//        return topLevelParent;
//    }

    @Override
    protected void handleKeyEvent(KeyEvent event) {
        topLevelParent.handleKeyEvent(event);
    }

    @Override
    protected void handleMouseEvent(MouseEvent event) {
        topLevelParent.handleMouseEvent(new gui.MouseEvent(event));
    }
    
//    /**
//     * Propagates received key events to the top-level parent.
//     */
//    @Override
//    public void handleKeyEvent(KeyEvent event) {
//        switch (event.getAction()) {
//            case KeyEvent.PRESS:
//                topLevelParent.keyPressed(event);
//                break;
//            case KeyEvent.RELEASE:
//                topLevelParent.keyReleased(event);
//                break;
//            case KeyEvent.TYPE:
//                topLevelParent.keyTyped(event);
//                break;
//        }
//    }
//    
//    /**
//     * Propagates received mouse events to the top-level parent.
//     */
//    @Override
//    public void handleMouseEvent(MouseEvent event) {
//        switch (event.getAction()) {
//            case MouseEvent.PRESS:
//                topLevelParent.mousePressed(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.RELEASE:
//                topLevelParent.mouseReleased(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.CLICK:
//                topLevelParent.mouseClicked(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.DRAG:
//                topLevelParent.mouseDragged(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.MOVE:
//                topLevelParent.mouseMoved(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.ENTER:
//                topLevelParent.mouseEntered(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.EXIT:
//                topLevelParent.mouseExited(new gui.MouseEvent(event));
//                break;
//            case MouseEvent.WHEEL:
//                topLevelParent.mouseWheel(new gui.MouseEvent(event));
//                break;
//        }
//    }
    
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
