package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Button;
import processing.core.PApplet;

/**
 * Pause frame, includes: 
 *      #musicSlider
 *      #soundSlider
 *      #helpBtn
 *      #exitBtn
 * 
 * @author Bugra Felekoglu
 */
public class PauseMenu extends Frame implements ActionListener {

    private final Button helpBtn = new Button();
    private final Button exitBtn = new Button();

    // TODO add music slider and sound slider
    
    @Override
    public void init(PApplet context){
        setSize(430, 520);
//        helpBtn.setStateImages(context
//                .loadImage("component/button/help_btn_small.png"), true);
        helpBtn.setFreeShape(true);
        helpBtn.setLocation(70, 290);
        helpBtn.setSize(300, 90);
        helpBtn.addActionListener(this);
        
//        exitBtn.setStateImages(context
//                .loadImage("component/button/exit_btn.png"), true);
        exitBtn.setFreeShape(true);
        exitBtn.setLocation(70, 400);
        exitBtn.setSize(300, 90);
        exitBtn.addActionListener(this);
        
        // TODO specify sliders
        // loc 70, 80  - size 300, 80
        // loc 70, 180 - size 300, 80
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if(comp == helpBtn)
            ((ScreenManager)getContext()).switchFrame(ScreenManager.FRAME_HELP);
        else if(comp == exitBtn)
            getContext().exit();
        
        // TODO add listeners to sliders
    }
}
