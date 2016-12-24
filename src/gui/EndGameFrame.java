package gui;

import game.Player;
import gui.core.Component;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * End Game Frame of the game.
 * 
 * @author Bugra Felekoglu
 */
public class EndGameFrame extends Frame {
    
    private PImage background;
    private Player winner;
    
    public void setWinner(Player player) {
        winner = player;
    }
    
    @Override
    public void init(PApplet context){
        super.init(context);
        setSize(1000, 700);
        setLocation(140, 34);
        background = context.loadImage("background/end_game.png");
        setBackground(background);
    }
    
    @Override
    public void draw(PGraphics g) {
        g.pushMatrix();
        g.fill(0xFFECC93F);
        g.textSize(200);
        g.textAlign(g.CENTER, g.BASELINE);
        g.text(winner.getName(), 500, 480);
        g.popMatrix();
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if (comp == this.closeBtn) {
            PApplet context = getContext();
            ((ScreenManager) context).closeFrame();
            ((ScreenManager) context)
                    .switchScreen(ScreenManager.SCREEN_MAIN_MENU);
        }
    }
}
