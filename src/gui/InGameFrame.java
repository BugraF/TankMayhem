package gui;

import game.Game;
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
        Game game = gameScreen.detachGame();
        game.stop();
        ((ScreenManager) context)
                .switchScreen(ScreenManager.SCREEN_MAIN_MENU);
    }
}
