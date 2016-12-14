package gui;

import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Parent;
import gui.core.Button;
import processing.core.PApplet;
import processing.core.PGraphics;
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
    private final Slider musicSli = new Slider();
    private final Slider soundSli = new Slider();
    
    @Override
    public void init(PApplet context) {
        background = context.loadImage("backgrounds/main_menu.png");
        
        add(playBtn, helpBtn, creditsBtn, musicSli, soundSli);
        
        playBtn.setStateImages(context
                .loadImage("component/button/play_btn.png"), true);
        playBtn.setFreeShape(true);
        playBtn.setLocation(160, 333);
        playBtn.setSize(300, 200);
        playBtn.addActionListener(this);
        playBtn.setMnemonic(80);
       
        helpBtn.setStateImages(context
                .loadImage("component/button/help_btn_big.png"), true);
        helpBtn.setFreeShape(true);
        helpBtn.setLocation(470, 333);
        helpBtn.setSize(350, 200);
        helpBtn.addActionListener(this);
        helpBtn.setMnemonic(72);
        
        creditsBtn.setStateImages(context
                .loadImage("component/button/credits_btn.png"), true);
        creditsBtn.setFreeShape(true);
        creditsBtn.setLocation(820, 333);
        creditsBtn.setSize(300, 200);
        creditsBtn.addActionListener(this);
        creditsBtn.setMnemonic(62);
        
        musicSli.setLocation(20,650);
        musicSli.setSize(140, 40);
        soundSli.setLocation(20,710);
        soundSli.setSize(140, 40);
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
    
    @Override
    public void draw(PGraphics g){
        g.image(background, 0, 0);
        drawComponents(g);
    }
}