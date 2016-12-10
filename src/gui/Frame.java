package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import gui.core.Button;

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
    
    public Frame(){
        closeBtn.setStateImages(getContext()
                .loadImage("component/button/close_btn.png"));
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