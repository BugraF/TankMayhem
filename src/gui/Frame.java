package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import gui.core.Button;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * A scheme for mini screens (frames). 
 *      #HelpFrame
 *      #MarketFrame
 *      #PauseFrame
 *      #CreditsFrame
 * 
 * @author Bugra Felekoglu
 */
public class Frame extends Parent implements ActionListener{
    
    private final Button closeBtn = new Button();
    
    @Override
    public void init(PApplet context){
        add(closeBtn);
        closeBtn.setStateImages(context
                .loadImage("component/button/close_btn.png"), true);
        closeBtn.setFreeShape(true);
        closeBtn.setSize(45, 45);
        closeBtn.addActionListener(this);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        closeBtn.setLocation(width-55, 10);
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if(comp == closeBtn)
            ((ScreenManager)getContext()).closeFrame();
    }
}