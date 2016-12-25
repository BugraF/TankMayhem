package gui;

import gui.core.Parent;
import java.util.ArrayList;
import java.util.Arrays;
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
                     FRAME_MARKET = 3,
                     FRAME_END_GAME = 4;
    
    public ScreenManager() {
        instance = this;
    }
    
    @Override
    public void settings() {
        size(1280, 768);
    }
    
    @Override
    public void setup() {
        topLevelParent.setSize(width, height);
        
        screens.addAll(Arrays.asList(
                new MainMenu(),
                new NewGameMenu(),
                new GameScreen()
        ));
        frames.addAll(Arrays.asList(
                new HelpFrame(),
                new CreditsFrame(),
                new PauseMenu(),
                new MarketFrame(),
                new EndGameFrame()
        ));
        
        screens.forEach((s) -> {
            s.setSize(width, height);
            s.init(this);
        });
        frames.forEach((f) -> f.init(this));
        
        currentScreen = screens.get(0);
        topLevelParent.add(currentScreen);
        topLevelParent.setFocusedChild(currentScreen);
    }
    
    long last;
    int frameRate;
    
    @Override
    public void draw() {
        long now = System.currentTimeMillis();
        if (now - last >= 1000) {
            System.out.println(frameRate);
            frameRate = 0;
            last = now;
        }
        frameRate++;
        topLevelParent.draw(g);
    }
    
    Parent switchScreen(int screen) {
        topLevelParent.remove(currentScreen);
        currentScreen = screens.get(screen);
        topLevelParent.add(currentScreen);
        topLevelParent.setFocusedChild(currentScreen);
        return currentScreen;
    }
    
    Frame switchFrame(int frame) {
        if (currentFrame == null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is no frame to be replaced.");
            return null;
        }
        topLevelParent.remove(currentFrame);
        currentFrame.setOwner(null);
        currentFrame = frames.get(frame);
        currentFrame.setOwner(currentScreen);
        topLevelParent.add(currentFrame);
        topLevelParent.setFocusedChild(currentFrame);
        return currentFrame;
    }
    
    Frame showFrame(int frame) {
        if (currentFrame != null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is already a frame shown.");
            return currentFrame;
        }
        currentScreen.setEnabled(false);
        currentFrame = frames.get(frame);
        currentFrame.setOwner(currentScreen);
        topLevelParent.add(currentFrame);
        topLevelParent.setFocusedChild(currentFrame);
        return currentFrame;
    }
    
    void closeFrame() {
        if (currentFrame == null) {
            Logger.getLogger("ScreenManager")
                    .log(Level.WARNING, "There is no frame to be closed.");
            return;
        }
        currentScreen.setEnabled(true);
        topLevelParent.remove(currentFrame);
        currentFrame.setOwner(null);
        currentFrame = null;
        topLevelParent.setFocusedChild(currentScreen);
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
