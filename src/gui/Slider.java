package gui;

import gui.core.InteractiveComponent;
import gui.core.MouseEvent;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * Slider consist of a button and a basic slider.
 * Aim of the button is mute the sound or music.
 * 
 * @author Bugra Felekoglu
 */
public class Slider extends InteractiveComponent {
    
    // Clickable image which makes volume 0 or previous value
    private  PImage icon;
    
    private boolean isMuted = false;
    
    // Scale rate
    private float rate = 1; 
    
    // Bounds of clickable line
    private int limX1 = 105;
    private int limY1 = 20;
    private int limX2 = 285;
    private int limY2 = 60;
    
    // Bound of clickable box
    private int boxSize = 80;
    
    // Slider Location (cur and prev)
    private int loc = 195; 
    private int prev = 195;
    
    private int focusKey;
    
    @Override
    public void init(PApplet context){
        
    }
    
    public void setFocusKey(int key) {
        focusKey = key;
        if (parent != null)
            parent.associateKeys(this, key);
    }
    
    public void setIcon(PImage icon){
        this.icon = icon;
    }
    
    // TODO @Buğra Calculate with respect to heigth
    
    @Override
    public void draw(PGraphics g) {
        // Orange rectangle
        g.noStroke();
        g.fill(236, 204, 129);
        g.rect(80 * rate, 20 * rate, 220 * rate, 40 * rate);

        // Volume line
        g.stroke(0);
        g.strokeWeight((float) 2.4 * rate);
        g.line(90 * rate, 40 * rate, 285 * rate, 40 * rate);

        // Limit
        g.strokeWeight(1);
        g.line(105 * rate, 37 * rate, 105 * rate, 43 * rate);

        // Slider 
        g.noStroke();
        g.fill(0);
        g.rect((loc-5) * rate, 25 * rate, 10 * rate, 30 * rate);

        // Box
        g.fill(0);
        g.quad(0, 0, 100 * rate, 0, 80 * rate, 80 * rate, 0, 80 * rate);
        
        // Icon
        g.image(icon, 0, 0, 80 * rate, 80 * rate);
        
        if(isMuted){
            // Cross Line
            g.stroke(241, 203, 130);
            g.strokeWeight((float) 3.5 * rate);
            g.line(15 * rate, 70 * rate, 75 * rate, 10 * rate);
        }
    }
    
    public void setScaleRate(float rate){
        this.rate = rate;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == focusKey)
            parent.setFocusedChild(this);
        else if (e.getKeyCode() == 37)
            loc -= 10;
        else if (e.getKeyCode() == 39)
            loc += 10;
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        // toggle mute
        // set value
        if ((e.getY() > limY1 * rate) && (e.getY() < limY2 * rate)
                && (e.getX() > limX1 * rate) && (e.getX() < limX2 * rate)){
            loc = (int) (e.getX() / rate);
            prev = loc;
            isMuted = false;
            // TODO @Buğra update volume
            System.out.println(loc);
            return true;
        }
        else if ((e.getY() < boxSize * rate) && (e.getX() < boxSize * rate)){
            if(!isMuted) {
                isMuted = true;
                loc = 106;
                // TODO @Buğra Mute volume
            }
            else {
                isMuted = false;
                loc = prev;
                // TODO @Buğra Unmute volume
            }
            return true;
        }
        else
            return false;
    }    
    
    @Override
    public boolean mouseDragged(MouseEvent e) {
        // toggle mute
        // set value
        if ((e.getY() > limY1 * rate) && (e.getY() < limY2 * rate)
                && (e.getX() > limX1 * rate) && (e.getX() < limX2 * rate)){
            loc = (int) (e.getX() / rate);
            prev = loc;
            if(loc == 106)
                isMuted = true;
            else
                isMuted = false;
            // TODO @Buğra update volume
            return true;
        }
        else
            return false;
    } 
}
