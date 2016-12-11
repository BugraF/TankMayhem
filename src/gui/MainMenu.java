package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import gui.core.Button;

/**
 * Main Menu Screen includes 
 *      - 3 buttons
 *      - 2 sliders
 * 
 * @author Bugra Felekoglu
 */
public class MainMenu extends Parent implements ActionListener{
    
    /**
    * Main buttons
    */
    private final Button playBtn = new Button();
    private final Button helpBtn = new Button();
    private final Button creditsBtn = new Button();
    
    /**
    * Sliders
    */
    // TODO add sliders - music slider and sound slider
    
    public MainMenu() {
        add(playBtn, helpBtn, creditsBtn);
        
        playBtn.setStateImages(getContext()
                .loadImage("component/button/play_btn.png"));
        playBtn.setFreeShape(true);
        playBtn.setLocation(160, 333);
        playBtn.setSize(300, 200);
        playBtn.addActionListener(this);
        playBtn.setMnemonic(80);
       
        helpBtn.setStateImages(getContext()
//                .loadImage("component/button/help_btn_big.png"));
                .loadImage("component/button/play_btn.png"));
        helpBtn.setFreeShape(true);
        helpBtn.setLocation(470, 333);
        helpBtn.setSize(350, 200);
        helpBtn.addActionListener(this);
        helpBtn.setMnemonic(72);
        
        creditsBtn.setStateImages(getContext()
//                .loadImage("component/button/credits_btn.png"));
                .loadImage("component/button/play_btn.png"));
        creditsBtn.setFreeShape(true);
        creditsBtn.setLocation(810, 333);
        creditsBtn.setSize(300, 200);
        creditsBtn.addActionListener(this);
        creditsBtn.setMnemonic(62);
        
        // TODO specify sliders
        // loc 20, 650 - size 140, 40
        // loc 20, 710 - size 140, 40
    }
    
    private Frame helpFrame = null;
    private Frame creditsFrame = null;
    
    @Override
    public void actionPerformed(Component comp) {
        if (comp == playBtn)
            ((ScreenManager)getContext())
                    .switchScreen(ScreenManager.SCREEN_NEW_GAME_MENU);
        else if (comp == helpBtn)
            ((ScreenManager)getContext())
                    .showFrame(ScreenManager.FRAME_HELP);
        else if (comp == creditsBtn)
            ((ScreenManager)getContext())
                    .showFrame(ScreenManager.FRAME_CREDITS);
        
        // TODO add listeners to sliders
    }
}