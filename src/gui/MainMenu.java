package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import gui.core.Button;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Main Menu Screen includes 
 *      - 3 buttons
 *      - 2 sliders
 * 
 * @author Bugra Felekoglu
 */
public class MainMenu extends Parent implements ActionListener{
    
    /**
     * Background Image
     */
    private PImage background; 
    
    /**
    * Main buttons
    */
    private final Button playBtn = new Button();
    private final Button helpBtn = new Button();
    private final Button creditsBtn = new Button();
    
    /**
    * Sliders
    */
    private final Slider musicSlider = new Slider();
    private final Slider soundSlider = new Slider();
    
    @Override
    public void init(PApplet context) {
        background = context.loadImage("background/main_menu.png");
        setBackground(background);
        
        add(playBtn, helpBtn, creditsBtn, musicSlider, soundSlider);
        
        playBtn.setStateImages(context
                .loadImage("component/button/play_btn.png"), true);
        playBtn.setFreeShape(true);
        playBtn.setLocation(160, 333);
        playBtn.setSize(300, 200);
        playBtn.addActionListener(this);
        playBtn.setMnemonic(80);    // mnemonic => p
       
        helpBtn.setStateImages(context
                .loadImage("component/button/help_btn_big.png"), true);
        helpBtn.setFreeShape(true);
        helpBtn.setLocation(470, 333);
        helpBtn.setSize(350, 200);
        helpBtn.addActionListener(this);
        helpBtn.setMnemonic(72);    // mnemonic => h
        
        creditsBtn.setStateImages(context
                .loadImage("component/button/credits_btn.png"), true);
        creditsBtn.setFreeShape(true);
        creditsBtn.setLocation(820, 333);
        creditsBtn.setSize(300, 200);
        creditsBtn.addActionListener(this);
        creditsBtn.setMnemonic(67); // mnemonic => c
        
        musicSlider.setScaleRate((float)0.5);
        musicSlider.setLocation(20,650);
        musicSlider.setSize(150, 40);
        musicSlider.setIcon(context.loadImage("component/button/music_icon.png"));
        musicSlider.setFocusKey(77);    // mnemonic => m
        
        soundSlider.setScaleRate((float)0.5);
        soundSlider.setLocation(20,710);
        soundSlider.setSize(150, 40);
        soundSlider.setIcon(context.loadImage("component/button/sound_icon.png"));
        soundSlider.setFocusKey(83);    // mnemonic => s
        
        super.init(context);
    }
    
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
        
        // TODO @Buğra add listeners to sliders
        // TODO @Buğra set mnemonics of sliders
    }
}