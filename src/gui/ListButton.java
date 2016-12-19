package gui;

import gui.core.*;
import java.util.ArrayList;
import java.util.List;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * Selectable list button
 * 
 * @see gui.core.Button
 * 
 * @author Buğra Felekoğlu
 */
public class ListButton extends InteractiveComponent {
    
    /**
     * Current state of this button.
     *      - 0: normal
     *      - 1: both rollOn and toggled
     */
    private int state = 0;
    
    /**
     * String property of the button
     */
    private String attribute;
    
    /**
     * Drawn string on the button
     */
    private String text;
    
    /**
     * Font type of the String printed
     */
    private PFont font;
    
    /**
     * Status of the button
     */
    private boolean isSelected = false;
    
    /**
     * The objects that listen to the invocation of this button.
     */
    private final List<ActionListener> actionListeners = new ArrayList<>(1);
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getAttribute() {
        return attribute;
    }
    
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    
    public boolean isSelected(){
        return isSelected;
    }
    
    public void setSelected(boolean select) {
        if(select){
            bounds[0] = bounds[0] - width/7;
            isSelected = select;
            state = 1;
        }
        else{
            bounds[0] = bounds[0] + width/7;
            isSelected = false;
            state = 0;
        }
    }
    
    public void setFont(PFont font){
        this.font = font;
    }
    
    @Override
    public void draw(PGraphics g) {
        g.textFont(font, 28);
        g.noStroke();
        
        // TODO @Buğra set freeshape
        
        if (state == 0){
            g.fill(0, 192);
            g.quad(width/12, 0, width, 0, width, height, 0, height);
            g.fill(255);
            g.textAlign(g.CENTER, g.CENTER);
            if(g.textWidth(text) > 270)
                g.textSize((float) 25);
            else
                g.textSize((float) 32);
            g.text(text, width/2-10, height/2-5);
        }
        else {
            g.fill(82, 195, 42, 192);
            g.quad(width/12, 0, width, 0, width, height, 0, height);
            g.fill(0);
            g.textAlign(g.CENTER, g.CENTER);
            if(isSelected){
                if(g.textWidth(text) > 270)
                    g.textSize((float) 28);
                else 
                    g.textSize((float) 35);
                g.text(text, width/2+5, height/2-5);
            }
            else{
                if(g.textWidth(text) > 270)
                    g.textSize((float) 25);
                else
                    g.textSize((float) 32);
                g.text(text, width/2-10, height/2-5);
            }
        }
    }
    
    /**
     * Associates the specified action listener to this button.
     * @return False if this button is already associated to the specified
     *         action listener, true otherwise.
     */
    public boolean addActionListener(ActionListener listener) {
        if (actionListeners.contains(listener))
            return false;
        actionListeners.add(listener);
        return true;
    }
    
    /**
     * Disassociates the specified action listener from this button.
     * @return False if the specified action listener is not associated to this
     *         button, true otherwise.
     */
    public boolean removeActionListener(ActionListener listener) {
        return actionListeners.remove(listener);
    }
    
    private void invoke() {
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(this);
    }
    
    @Override
    public boolean handleMouseEvent(MouseEvent event) {
        if (event.getAction() != processing.event.MouseEvent.EXIT
                && !consumeEvent(event)) return false;
        if (!enabled) return true;
        return propagateMouseEvent(this, event);
    }
    
    private boolean consumeEvent(MouseEvent e) {
        if (isSelected)
            return false;
        else
            return true;
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        state = 1;
        return true;
    }

    @Override
    public boolean mouseReleased(MouseEvent e) {
        state = 1;
        return true;
    }
    
    @Override
    public boolean mouseClicked(MouseEvent e) {
        if(!isSelected){
            setSelected(true);
            invoke();
        }
        return true;
    }
    
    @Override
    public boolean mouseEntered(MouseEvent e) {
        if(!isSelected)
            state = 1;
        return true;
    }

    @Override
    public boolean mouseExited(MouseEvent e) {
        if(!isSelected)
            state = 0;
        return true;
    }
}
