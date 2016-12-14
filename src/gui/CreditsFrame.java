package gui;

import gui.core.Component;
import gui.core.ActionListener;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * A frame which includes credits for Tank Mayhem
 * 
 * @author Bugra Felekoglu
 */
public class CreditsFrame extends Frame implements ActionListener {
    
    private PImage background;
    
    @Override
    public void init(PApplet context){
        setSize(1000, 700);
        background = context.loadImage("backgrounds/credits.png");
    }
    
    @Override
    public void actionPerformed(Component comp) {
        
    }
    
    @Override
        public void draw(PGraphics g){
        g.image(background, 1140, 34);
        drawComponents(g);
    }
    
}