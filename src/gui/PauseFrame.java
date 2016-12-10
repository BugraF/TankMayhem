package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Button;

/**
 * Pause frame, includes:
 *      #helpBtn
 *      #
 * 
 * @author Bugra Felekoglu
 */
public class PauseFrame extends Frame implements ActionListener {

    private final Button helpBtn = new Button();
    private final Button exitBtn = new Button();

    // TODO add music slider and sound slider
    
    public PauseFrame(){
        setSize(430, 520);
        helpBtn.setStateImages(getContext()
                .loadImage("component/button/help_btn_in_pauseframe.png"));
        helpBtn.setFreeShape(true);
        helpBtn.setLocation(70, 290);
        helpBtn.setSize(300, 90);
        helpBtn.addActionListener(this);
        
        exitBtn.setStateImages(getContext()
                .loadImage("component/button/exit_btn.png"));
        exitBtn.setFreeShape(true);
        exitBtn.setLocation(70, 400);
        exitBtn.setSize(300, 90);
        exitBtn.addActionListener(this);
        
        // TODO specify sliders
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
