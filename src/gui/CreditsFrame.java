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
public class CreditsFrame extends Frame  {
    
    private PImage background;
    
    @Override
    public void init(PApplet context){
        super.init(context);
        setSize(580, 640);
        setLocation(350, 64);
        background = context.loadImage("background/credits.png");
        setBackground(background);
    }
}