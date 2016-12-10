package gui;

import gui.core.Parent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        public PApplet getContext() {
            return ScreenManager.this;
        }
    };
    
    private final List<Parent> screens = new ArrayList<>(3);
    private Parent currentScreen;
    
    private final List<Frame> frames = new ArrayList<>(4);
    private Frame currentFrame = null;
    
    final static int SCREEN_MAIN_MENU = 0,
                     SCREEN_NEW_GAME_MENU = 1,
                     SCREEN_GAME = 2;
    final static int FRAME_HELP = 0,
                     FRAME_CREDITS = 1,
                     FRAME_PAUSE_MENU = 2,
                     FRAME_MARKET = 3;
    
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
        screens.add(new MainMenu(),
                    new NewGameMenu(),
                    new GameScreen());
        frames.add(new HelpFrame(),
                   new CreditsFrame(),
                   new PauseMenu(),
                   new MarketFrame());
        currentScreen = screens.get(0);
        topLevelParent.add(currentScreen);
    }
    
    @Override
    public void draw() {
        topLevelParent.draw(g);
    }
    
    void switchScreen(int screen) {
        topLevelParent.remove(currentScreen);
        currentScreen = screens.get(screen);
        topLevelParent.add(currentScreen);
    }
    
    void switchFrame(int frame) {
        if (currentFrame == null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is no frame to be replaced.");
            return;
        }
        topLevelParent.remove(currentFrame);
        currentFrame = frames.get(frame);
        topLevelParent.add(currentFrame);
    }
    
    void showFrame(int frame) {
        if (currentFrame != null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is already a frame shown.");
            return;
        }
        currentScreen.setEnabled(false);
        currentFrame = frames.get(frame);
        topLevelParent.add(currentFrame);
    }
    
    void closeFrame() {
        if (currentFrame == null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is no frame to be closed.");
            return;
        }
        currentScreen.setEnabled(true);
        topLevelParent.remove(currentFrame);
        currentFrame = null;
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
        topLevelParent.handleMouseEvent(new gui.core.MouseEvent(event));
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
