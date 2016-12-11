package gui;

import gui.core.Component;
import gui.core.ActionListener;
import processing.core.PApplet;

/**
 * A frame which includes guide for Tank Mayhem
 * 
 * @author Bugra Felekoglu
 */
public class HelpFrame extends Frame implements ActionListener {
    
    @Override
    public void init(PApplet context){
        setSize(1000, 700);
    }
    
    @Override
    public void actionPerformed(Component comp) {
        
    }
    
}
