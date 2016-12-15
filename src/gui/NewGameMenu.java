package gui;

import game.GameManager;
import gui.core.Component;
import gui.core.ActionListener;
import gui.core.Button;
import gui.core.Parent;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * New Game Menu Screen
 * 
 * @author Bugra Felekoglu
 */
public class NewGameMenu extends Parent implements ActionListener{
        
    /**
     * Background Image
     */
    private PImage background; 
    
    
    
    /**
     * Buttons
     */
    private final Button start_btn = new Button();
    private final Button back_btn = new Button();
    
//    private final Button back_btn = new Button();
//    private final Button back_btn = new Button();
//    private final Button back_btn = new Button();
//    private final Button back_btn = new Button();
    
    
    @Override
    public void init(PApplet context) {
        background = context.loadImage("background/new_game_menu.png");
        setBackground(background);
        
        add(start_btn, back_btn);
        
        start_btn.setStateImages(context
                .loadImage("component/button/start_btn.png"), true);
        start_btn.setFreeShape(true);
        start_btn.setLocation(980, 648);
        start_btn.setSize(300, 120);
        start_btn.addActionListener(this);
        start_btn.setMnemonic(83);    // mnemonic => s
        
        back_btn.setStateImages(context
                .loadImage("component/button/back_btn.png"), true);
        back_btn.setFreeShape(true);
        back_btn.setLocation(0, 688);
        back_btn.setSize(200, 80);
        back_btn.addActionListener(this);
        back_btn.setMnemonic(66);    // mnemonic => b
    }
    
    @Override
    public void actionPerformed(Component comp) {
        if(comp == start_btn){
            ((ScreenManager)getContext())
                    .switchScreen(ScreenManager.SCREEN_GAME);
//            GameManager.getInstance().startNewGame("Desert", );
        }
            
        if(comp == back_btn)
            ((ScreenManager)getContext())
                    .switchScreen(ScreenManager.SCREEN_MAIN_MENU);
    }
}