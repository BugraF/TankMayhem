package gui;

import gui.core.Component;
import gui.core.ActionListener;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * A frame includes guide for Tank Mayhem
 * 
 * @author Bugra Felekoglu
 */
public class HelpFrame extends Frame {
    
    private PImage background;
    
    @Override
    public void init(PApplet context){
        super.init(context);
        setSize(1000, 700);
        setLocation(140, 34);
        background = context.loadImage("background/help.png");
        setBackground(background);
    }
}
