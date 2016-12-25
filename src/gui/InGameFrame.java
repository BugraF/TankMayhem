package gui;

import processing.core.PApplet;

/**
 *
 * @author Burak Gök
 */
public class InGameFrame extends Frame {
    
    protected void returnToMainMenu() {
        GameScreen gameScreen = (GameScreen)owner;
        PApplet context = getContext();
            ((ScreenManager) context).closeFrame();
        gameScreen.detachGame();
        ((ScreenManager) context)
                .switchScreen(ScreenManager.SCREEN_MAIN_MENU);
    }
}
