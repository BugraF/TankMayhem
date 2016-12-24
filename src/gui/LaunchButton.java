package gui;

import gui.core.Button;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 *
 * @author Buğra Felekoğlu
 */
public class LaunchButton extends Button {
    
    /** Color Attributes */
    private int tint = 255;
    
    /** Text Attributes */
    private String text;
    private PFont font;
    
    public void setTint(int tint) {
        this.tint = tint;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setFont(PFont font) {
        this.font = font;
    }

    @Override
    public void draw(PGraphics g) {
        g.tint(tint);
        super.draw(g);
        g.noTint();
    }
}
