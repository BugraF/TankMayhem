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
    private int tint = 0;
    
    /** Text Attributes */
    private String text;
    private PFont font;
    private int size;
    
    public void setTint(int tint) {
        this.tint = tint;
    }
    
    public void setText(String text, int size) {
        this.text = text;
        this.size = size;
    }
    
    public void setFont(PFont font) {
        this.font = font;
    }

    @Override
    public void draw(PGraphics g) {
        if (state != 3)
            g.tint(tint);
        super.draw(g);
        g.noTint();
        g.textAlign(g.CENTER, g.CENTER);
        g.textFont(font, size);
        if(state == 2)
            g.text(text, width/2, (height-36)/2 + 10);
        else
            g.text(text, width/2, (height-36)/2);
    }
}
