package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Button;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Pause frame, includes: 
 *      #musicSlider
 *      #soundSlider
 *      #helpBtn
 *      #exitBtn
 * 
 * @author Bugra Felekoglu
 */
public class PauseMenu extends InGameFrame implements ActionListener {

    private final Button helpBtn = new Button();
    private final Button mainMenuBtn = new Button();
    private final Slider musicSlider = new Slider();
    private final Slider soundSlider = new Slider();
    
    private PImage background;
    
    @Override
    public void init(PApplet context){
        super.init(context);
        setSize(430, 520);
        setLocation(425, 60);
        background = context.loadImage("background/pause_menu.png");
        setBackground(background);
        
        add(helpBtn, mainMenuBtn);
        
        helpBtn.setStateImages(context
                .loadImage("component/button/help_btn_small.png"), true);
        helpBtn.setLocation(65, 290);
        helpBtn.setSize(300, 90);
        helpBtn.addActionListener(this);
        helpBtn.setMnemonic(72);    // Mnemonic => H
        
        mainMenuBtn.setStateImages(context
                .loadImage("component/button/main_menu_btn.png"), true);
        mainMenuBtn.setLocation(65, 400);
        mainMenuBtn.setSize(300, 90);
        mainMenuBtn.addActionListener(this);
        mainMenuBtn.setMnemonic(69);    // Mnemonic => E
        
        musicSlider.setLocation(65,80);
        musicSlider.setSize(300, 80);
        musicSlider.setIcon(context.loadImage("component/button/music_icon.png"));
        musicSlider.setFocusKey(77);    // Mnemonic => M
        
        soundSlider.setLocation(65,180);
        soundSlider.setSize(300, 80);
        soundSlider.setIcon(context.loadImage("component/button/sound_icon.png"));
        soundSlider.setFocusKey(83);    // Mnemonic => S
    }
    
    @Override
    public void actionPerformed(Component comp) {
        super.actionPerformed(comp);
        if(comp == helpBtn)
            ((ScreenManager)getContext()).switchFrame(ScreenManager.FRAME_HELP);
        else if(comp == mainMenuBtn)
            returnToMainMenu();
        // TODO @Buğra add listeners to sliders
    }
}
