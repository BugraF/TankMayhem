package gui;

import gui.core.Component;
import gui.core.ActionListener;
import processing.core.PApplet;

/**
 * A frame which includes market items.
 * 
 * @author Bugra Felekoglu
 */
public class MarketFrame extends Frame implements ActionListener {
    
    @Override
    public void init(PApplet context){
        setSize(1080, 580);
    }
    
    @Override
    public void actionPerformed(Component comp) {
        
    }
    
}
