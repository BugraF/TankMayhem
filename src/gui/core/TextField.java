package gui.core;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 *
 * @author Burak Gök
 */
public class TextField extends InteractiveComponent {
    
    private PFont font;
    private float fontSize;
    private final StringBuilder sb = new StringBuilder();
    
    public void setFont(PFont font) {
        this.font = font;
    }
    
    public void setFontSize(float size) {
        fontSize = size;
    }

    @Override
    public void draw(PGraphics g) {
        g.textFont(font, fontSize);
        g.text(sb.toString(), 0, 0, width, height);
    }
    
    public String getText() {
        return sb.toString();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 8)
            sb.deleteCharAt(sb.length() - 
                    Character.charCount(sb.codePointAt(sb.length() - 1)));
        else
            sb.append(e.getKey());
    }
    
}
